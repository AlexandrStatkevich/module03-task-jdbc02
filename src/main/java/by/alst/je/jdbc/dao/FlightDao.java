package by.alst.je.jdbc.dao;

import by.alst.je.jdbc.entity.Flight;
import by.alst.je.jdbc.entity.FlightStatus;
import by.alst.je.jdbc.exception.DaoException;
import by.alst.je.jdbc.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Long, Flight> {

    private final static FlightDao INSTANCE = new FlightDao();

    private FlightDao() {
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }

    private final static String SAVE_SQL = """
            INSERT INTO flight_repo.flight (flight_no, departure_date, departure_airport_code, arrival_date,
             arrival_airport_code, aircraft_id, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private final static String DELETE_SQL = """
            DELETE FROM flight_repo.flight
            WHERE id = ?
            """;

    private final static String FIND_ALL_SQL = """
            SELECT id, flight_no, departure_date, departure_airport_code, arrival_date, arrival_airport_code, aircraft_id, status
            FROM flight_repo.flight
            """;

    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private final static String UPDATE_SQL = """
            UPDATE flight_repo.flight
            SET flight_no = ?,
                departure_date = ?,
                departure_airport_code = ?,
                arrival_date = ?,
                arrival_airport_code = ?,
                aircraft_id = ?,
                status = ?
            WHERE id = ?
            """;

    @Override
    public boolean update(Flight flight) {
        if (flight != null) {
            try (var connection = ConnectionManager.get();
                 var statement = connection.prepareStatement(UPDATE_SQL)) {
                setFlightToSql(flight, statement);
                statement.setLong(8, flight.getId());
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            return false;
        }
    }

    private void setFlightToSql(Flight flight, PreparedStatement statement) throws SQLException {
        statement.setString(1, flight.getFlightNo());
        statement.setTimestamp(2, Timestamp.valueOf(flight.getDepartureDate()));
        statement.setString(3, flight.getDepartureAirportCode());
        statement.setTimestamp(4, Timestamp.valueOf(flight.getArrivalDate()));
        statement.setString(5, flight.getArrivalAirportCode());
        statement.setInt(6, flight.getAircraftId());
        statement.setString(7, flight.getStatus().name());
    }

    public Optional<Flight> findById(Long id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            Flight flight = null;

            var result = statement.executeQuery();
            while (result.next()) {
                flight = buildFlight(result);
            }
            return Optional.ofNullable(flight);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Flight> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Flight> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Flight> flights = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                flights.add(
                        buildFlight(result)
                );
            }
            return flights;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Flight buildFlight(ResultSet result) throws SQLException {
        return new Flight(
                result.getLong("id"),
                result.getString("flight_no"),
                result.getTimestamp("departure_date").toLocalDateTime(),
                result.getString("departure_airport_code"),
                result.getTimestamp("arrival_date").toLocalDateTime(),
                result.getString("arrival_airport_code"),
                result.getInt("aircraft_id"),
                FlightStatus.valueOf(result.getString("status"))
        );
    }

    @Override
    public Flight save(Flight flight) {
        if (flight != null) {
            try (var connection = ConnectionManager.get();
                 var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
                setFlightToSql(flight, statement);
                statement.executeUpdate();
                var keys = statement.getGeneratedKeys();
                if (keys.next())
                    flight.setId(keys.getLong("id"));
                return flight;
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            return new Flight();
        }
    }

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
