package by.alst.je.jdbc.dao;

import by.alst.je.jdbc.entity.Aircraft;
import by.alst.je.jdbc.exception.DaoException;
import by.alst.je.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AircraftDao implements Dao<Long, Aircraft> {
    private final static AircraftDao INSTANCE = new AircraftDao();

    private AircraftDao() {
    }

    public static AircraftDao getInstance() {
        return INSTANCE;
    }

    private final static String SAVE_SQL = """
            INSERT INTO flight_repo.aircraft(model)
            VALUES (?)
            """;

    private final static String DELETE_SQL = """
            DELETE FROM flight_repo.aircraft
            WHERE id = ?
            """;

    private final static String FIND_ALL_SQL = """
            SELECT id, model
            FROM flight_repo.aircraft
            """;

    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private final static String UPDATE_SQL = """
            UPDATE flight_repo.aircraft
            SET model = ?
            WHERE id = ?
            """;

    @Override
    public boolean update(Aircraft aircraft) {
        if (aircraft != null) {
            try (var connection = ConnectionManager.get();
                 var statement = connection.prepareStatement(UPDATE_SQL)) {
                statement.setString(1, aircraft.getModel());
                statement.setLong(2, aircraft.getId());
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            return false;
        }
    }

    @Override
    public Optional<Aircraft> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            Aircraft aircraft = null;

            var result = statement.executeQuery();
            while (result.next()) {
                aircraft = buildAircraft(result);
            }
            return Optional.ofNullable(aircraft);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Aircraft buildAircraft(ResultSet result) throws SQLException {
        return new Aircraft(result.getLong("id"),
                result.getString("model"));
    }

    @Override
    public List<Aircraft> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Aircraft> aircrafts = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                aircrafts.add(
                        buildAircraft(result)
                );
            }
            return aircrafts;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Aircraft save(Aircraft aircraft) {
        if (aircraft != null) {
            try (var connection = ConnectionManager.get();
                 var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, aircraft.getModel());

                statement.executeUpdate();
                var keys = statement.getGeneratedKeys();
                if (keys.next())
                    aircraft.setId(keys.getLong("id"));
                return aircraft;
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            return new Aircraft();
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
