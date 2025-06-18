package mpp.repository.database;

import mpp.domain.Utilizator;
import mpp.repository.UtilizatorRepository;
import mpp.utils.HashUtils;
import mpp.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class UtilizatorDbRepository implements UtilizatorRepository {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public UtilizatorDbRepository(Properties props) {
        logger.info("Initializing UtilizatorDbRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Utilizator> findOne(Integer id) {
        logger.traceEntry("finding utilizator by id {}", id);
        Connection con = dbUtils.getConnection();
        Utilizator utilizator = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from utilizatori where id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String numeUtilizator = result.getString("numeUtilizator");
                    String parola = result.getString("parola");
                    utilizator = new Utilizator();
                    utilizator.setId(id);
                    utilizator.setNumeUtilizator(numeUtilizator);
                    utilizator.setParola(parola);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(utilizator);
        return Optional.ofNullable(utilizator);
    }

    @Override
    public Iterable<Utilizator> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Utilizator> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from utilizatori")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String numeUtilizator = result.getString("numeUtilizator");
                    String parola = result.getString("parola");
                    Utilizator utilizator = new Utilizator();
                    utilizator.setId(id);
                    utilizator.setNumeUtilizator(numeUtilizator);
                    utilizator.setParola(parola);
                    utilizatori.add(utilizator);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(utilizatori);
        return utilizatori;
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {

        logger.traceEntry("saving utilizator {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into utilizatori (numeUtilizator, parola) values (?,?)")) {
            preStmt.setString(1, entity.getNumeUtilizator());
            String hashedParola = HashUtils.hashPassword(entity.getParola());
            preStmt.setString(2, hashedParola);
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
    public Optional<Utilizator> delete(Integer id) {
        logger.traceEntry("deleting utilizator with id {}", id);
        Connection con = dbUtils.getConnection();
        Optional<Utilizator> utilizator = findOne(id);
        try (PreparedStatement preStmt = con.prepareStatement("delete from utilizatori where id = ?")) {
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return utilizator;
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        logger.traceEntry("updating utilizator {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update utilizatori set numeUtilizator = ?, parola = ? where id = ?")) {
            preStmt.setString(1, entity.getNumeUtilizator());
            String hashedParola = HashUtils.hashPassword(entity.getParola());
            preStmt.setString(2, hashedParola);
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

    @Override
    public Utilizator findUtilizator(String numeUtilizator, String parola) {
        logger.traceEntry("finding utilizator by numeUtilizator {} and parola {}", numeUtilizator, parola);
        Connection con = dbUtils.getConnection();
        Utilizator utilizator = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from utilizatori where numeUtilizator = ? and parola = ?")) {
            preStmt.setString(1, numeUtilizator);
            String hashedParola = HashUtils.hashPassword(parola);
            preStmt.setString(2, hashedParola);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    utilizator = new Utilizator();
                    utilizator.setId(id);
                    utilizator.setNumeUtilizator(numeUtilizator);
                    utilizator.setParola(parola);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(utilizator);
        return utilizator;
    }
}