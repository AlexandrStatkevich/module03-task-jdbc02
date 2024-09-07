package by.alst.je.jdbc.dao;

import by.alst.je.jdbc.entity.Seat;
import by.alst.je.jdbc.exception.DaoException;
import by.alst.je.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SeatDao implements Dao<Integer, List<Seat>> {
    private final static SeatDao INSTANCE = new SeatDao();

    public static SeatDao getInstance() {
        return INSTANCE;
    }

    private SeatDao() {
    }

    private final static String SAVE_SQL = """
            INSERT INTO flight_repo.seat(aircraft_id, seat_no)
            VALUES (?, ?)
            """;

    private final static String DELETE_SQL = """
            DELETE FROM flight_repo.seat
            WHERE aircraft_id = ?
            """;

    private final static String DELETE_SEAT_SQL = """
            DELETE FROM flight_repo.seat
            WHERE aircraft_id = ? AND seat_no = ?     
            """;

    private final static String FIND_ALL_SQL = """
            SELECT aircraft_id, seat_no
            FROM flight_repo.seat
            """;

    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE aircraft_id = ?
            """;

    @Override
    public boolean update(List<Seat> seats) {
        int updateResult = 0;
        boolean seatsAircraftIdEquals = true;
        if (seats != null) {
            seats = new LinkedHashSet<>(seats).stream().toList();
            for (Seat seat : seats) {
                seatsAircraftIdEquals = seatsAircraftIdEquals
                                        & Objects.equals(seats.get(0).getAircraftId(), seat.getAircraftId());
            }
            if (seatsAircraftIdEquals) {
                delete(seats.get(0).getAircraftId());
                for (Seat seat : seats) {
                    updateResult = updateResult + saveSeat(seat);
                }
            }
        }
        return updateResult > 0;
    }

    @Override
    public Optional<List<Seat>> findById(Integer flightId) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, flightId);
            List<Seat> list = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                list.add(buildSeat(result));
            }
            return Optional.ofNullable(list);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Seat buildSeat(ResultSet result) throws SQLException {
        return new Seat(result.getInt("aircraft_id"),
                result.getString("seat_no"));
    }

    @Override
    public List<List<Seat>> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<List<Seat>> listSeats = new ArrayList<>();
            List<Seat> seats = new ArrayList<>();
            Seat seat = new Seat(0, null);
            var result = statement.executeQuery();
            while (result.next()) {
                if (seat.getAircraftId() == 0 || result.getInt("aircraft_id") == seat.getAircraftId()) {
                    seat = buildSeat(result);
                    seats.add(seat);
                } else {
                    addSeatsToList(seats, listSeats);
                    seat = buildSeat(result);
                    seats.add(seat);
                }
            }
            addSeatsToList(seats, listSeats);
            return listSeats;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static void addSeatsToList(List<Seat> seats, List<List<Seat>> listSeats) {
        List<Seat> workigList = new ArrayList<>(seats);
        listSeats.add(workigList);
        seats.clear();
    }

    @Override
    public List<Seat> save(List<Seat> seats) {
        List<Seat> resultList = new ArrayList<>();
        if (seats != null) {
            for (Seat seat : seats) {
                saveSeat(seat);
            }
            resultList = findById(seats.get(0).getAircraftId()).get();
        }
        return resultList;
    }

    private static int saveSeat(Seat seat) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL)) {

            statement.setInt(1, seat.getAircraftId());
            statement.setString(2, seat.getSeatNo());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer flightId) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, flightId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean deleteSeat(Seat seat) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SEAT_SQL)) {
            statement.setInt(1, seat.getAircraftId());
            statement.setString(2, seat.getSeatNo());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
