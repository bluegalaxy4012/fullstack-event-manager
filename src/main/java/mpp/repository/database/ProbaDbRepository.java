package mpp.repository.database;

import mpp.domain.Proba;
import mpp.repository.ProbaRepository;
import mpp.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ProbaDbRepository implements ProbaRepository {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public ProbaDbRepository(Properties props) {
        logger.info("Initializing ProbaDbRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Proba> findOne(Integer id) {
        logger.traceEntry("finding proba by id {}", id);
        Connection con = dbUtils.getConnection();
        Proba proba = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from probe where id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String distanta = result.getString("distanta");
                    String stil = result.getString("stil");
                    proba = new Proba();
                    proba.setId(id);
                    proba.setDistanta(distanta);
                    proba.setStil(stil);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(proba);
        return Optional.ofNullable(proba);
    }

    @Override
    public Iterable<Proba> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Proba> probe = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from probe")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String distanta = result.getString("distanta");
                    String stil = result.getString("stil");
                    Proba proba = new Proba();
                    proba.setId(id);
                    proba.setDistanta(distanta);
                    proba.setStil(stil);
                    probe.add(proba);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(probe);
        return probe;
    }

    @Override
    public Optional<Proba> save(Proba entity) {
        logger.traceEntry("saving proba {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into probe (distanta, stil) values (?,?)")) {
            preStmt.setString(1, entity.getDistanta());
            preStmt.setString(2, entity.getStil());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Optional<Proba> delete(Integer id) {
        logger.traceEntry("deleting proba with id {}", id);
        Connection con = dbUtils.getConnection();
        Optional<Proba> proba = findOne(id);
        try (PreparedStatement preStmt = con.prepareStatement("delete from probe where id = ?")) {
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return proba;
    }

    @Override
    public Optional<Proba> update(Proba entity) {
        logger.traceEntry("updating proba {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update probe set distanta = ?, stil = ? where id = ?")) {
            preStmt.setString(1, entity.getDistanta());
            preStmt.setString(2, entity.getStil());
            preStmt.setInt(3, entity.getId());
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }
}