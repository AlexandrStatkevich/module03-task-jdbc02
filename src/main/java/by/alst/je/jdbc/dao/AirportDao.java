package by.alst.je.jdbc.dao;

import by.alst.je.jdbc.entity.*;
import by.alst.je.jdbc.exception.DaoException;
import by.alst.je.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirportDao implements Dao<AirportCode, Airport> {
    private final static AirportDao INSTANCE = new AirportDao();

    public static AirportDao getInstance() {
        return INSTANCE;
    }

    private AirportDao() {
    }

    private final static String SAVE_SQL = """
            INSERT INTO flight_repo.airport(code, country, city)
            VALUES (?, ?, ?)
            """;

    private final static String DELETE_SQL = """
            DELETE FROM flight_repo.airport
            WHERE code = ?
            """;

    private final static String FIND_ALL_SQL = """
            SELECT code, country, city
            FROM flight_repo.airport
            """;

    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE code = ?
            """;

    private final static String UPDATE_SQL = """
            UPDATE flight_repo.airport
            SET code = ?
            WHERE city = ? AND country = ?
            """;

    @Override
    public boolean update(Airport airport) {
        if (airport != null && !findAll().stream().map(Airport::getCode).toList().contains(airport.getCode())) {
            try (var connection = ConnectionManager.get();
                 var statement = connection.prepareStatement(UPDATE_SQL)) {
                statement.setString(1, airport.getCode().name());
                statement.setString(2, airport.getCity().name());
                statement.setString(3, airport.getCountry().name());
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            return false;
        }
    }

    @Override
    public Optional<Airport> findById(AirportCode airportCode) {
        if (airportCode != null) {
            try (var connection = ConnectionManager.get();
                 var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
                statement.setString(1, airportCode.name());
                Airport airport = null;

                var result = statement.executeQuery();
                while (result.next()) {
                    airport = buildAirport(result);
                }
                return Optional.ofNullable(airport);
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            return Optional.empty();
        }
    }

    private Airport buildAirport(ResultSet result) throws SQLException {
        return new Airport(AirportCode.valueOf(result.getString("code")),
                Country.valueOf(result.getString("country")),
                City.valueOf(result.getString("city")));
    }

    @Override
    public List<Airport> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Airport> airports = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                airports.add(
                        buildAirport(result)
                );
            }
            return airports;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Airport save(Airport airport) {
        if (airport != null && !findAll().stream().map(Airport::getCode).toList().contains(airport.getCode())) {
            try (var connection = ConnectionManager.get();
                 var statement = connection.prepareStatement(SAVE_SQL)) {
                List<AirportCode> airportCodelist = findAll().stream().map(Airport::getCode).toList();
                if (!airportCodelist.contains(airport.getCode())) {
                    statement.setString(1, airport.getCode().name());
                    statement.setString(2, airport.getCountry().name());
                    statement.setString(3, airport.getCity().name());
                    statement.executeUpdate();
                }
                airport = findById(airport.getCode()).orElse(null);
                return airport;
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            return new Airport();
        }
    }

    @Override
    public boolean delete(AirportCode airportCode) {
        if (airportCode != null) {
            try (var connection = ConnectionManager.get();
                 var statement = connection.prepareStatement(DELETE_SQL)) {
                statement.setString(1, airportCode.name());
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            return false;
        }
    }
}
