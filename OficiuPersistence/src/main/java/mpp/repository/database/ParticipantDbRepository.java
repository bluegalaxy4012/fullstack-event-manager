//package mpp.repository.database;
//
//import mpp.domain.Participant;
//import mpp.repository.ParticipantRepository;
//import utils.JdbcUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.Properties;
//
//public class ParticipantDbRepository implements ParticipantRepository {
//
//    private final JdbcUtils dbUtils;
//    private static final Logger logger = LogManager.getLogger();
//
//    public ParticipantDbRepository(Properties props) {
//        logger.info("Initializing ParticipantDbRepository with properties: {}", props);
//        dbUtils = new JdbcUtils(props);
//    }
//
//    @Override
//    public Optional<Participant> findOne(Integer id) {
//        logger.traceEntry("finding participant by id {}", id);
//        Connection con = dbUtils.getConnection();
//        Participant participant = null;
//        try (PreparedStatement preStmt = con.prepareStatement("select * from participanti where id = ?")) {
//            preStmt.setInt(1, id);
//            try (ResultSet result = preStmt.executeQuery()) {
//                if (result.next()) {
//                    String nume = result.getString("nume");
//                    int varsta = result.getInt("varsta");
//                    participant = new Participant();
//                    participant.setId(id);
//                    participant.setNume(nume);
//                    participant.setVarsta(varsta);
//                }
//            }
//        } catch (SQLException ex) {
//            logger.error(ex);
//            System.err.println("Error DB " + ex);
//        }
//        logger.traceExit(participant);
//        return Optional.ofNullable(participant);
//    }
//
//    @Override
//    public Iterable<Participant> findAll() {
//        logger.traceEntry();
//        Connection con = dbUtils.getConnection();
//        List<Participant> participants = new ArrayList<>();
//        try (PreparedStatement preStmt = con.prepareStatement("select * from participanti")) {
//            try (ResultSet result = preStmt.executeQuery()) {
//                while (result.next()) {
//                    int id = result.getInt("id");
//                    String nume = result.getString("nume");
//                    int varsta = result.getInt("varsta");
//                    Participant participant = new Participant();
//                    participant.setId(id);
//                    participant.setNume(nume);
//                    participant.setVarsta(varsta);
//                    participants.add(participant);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error(e);
//            System.err.println("Error DB " + e);
//        }
//        logger.traceExit(participants);
//        return participants;
//    }
//
//    @Override
//    public Optional<Participant> save(Participant entity) {
//        logger.traceEntry("saving participant {}", entity);
//        Connection con = dbUtils.getConnection();
//        try (PreparedStatement preStmt = con.prepareStatement(
//                "INSERT INTO participanti (nume, varsta) VALUES (?, ?)",
//                PreparedStatement.RETURN_GENERATED_KEYS)) {
//            preStmt.setString(1, entity.getNume());
//            preStmt.setInt(2, entity.getVarsta());
//            int result = preStmt.executeUpdate();
//            logger.trace("Saved {} instances", result);
//
//            if (result > 0) {
//                try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        int generatedId = generatedKeys.getInt(1);
//                        entity.setId(generatedId);
//                        return Optional.of(entity);
//                    }
//                }
//            }
//        } catch (SQLException ex) {
//            logger.error(ex);
//            System.err.println("Error DB " + ex);
//        }
//        logger.traceExit();
//        return Optional.empty();
//    }
//
//
//
//    @Override
//    public Optional<Participant> delete(Integer id) {
//        logger.traceEntry("deleting participant with id {}", id);
//        Connection con = dbUtils.getConnection();
//        Optional<Participant> participant = findOne(id);
//        try (PreparedStatement preStmt = con.prepareStatement("delete from participanti where id = ?")) {
//            preStmt.setInt(1, id);
//            int result = preStmt.executeUpdate();
//            logger.trace("Deleted {} instances", result);
//        } catch (SQLException ex) {
//            logger.error(ex);
//            System.err.println("Error DB " + ex);
//        }
//        logger.traceExit();
//        return participant;
//    }
//
//    @Override
//    public Optional<Participant> update(Participant entity) {
//        logger.traceEntry("updating participant {}", entity);
//        Connection con = dbUtils.getConnection();
//        try (PreparedStatement preStmt = con.prepareStatement("update participanti set nume = ?, varsta = ? where id = ?")) {
//            preStmt.setString(1, entity.getNume());
//            preStmt.setInt(2, entity.getVarsta());
//            preStmt.setInt(3, entity.getId());
//            int result = preStmt.executeUpdate();
//            logger.trace("Updated {} instances", result);
//        } catch (SQLException ex) {
//            logger.error(ex);
//            System.err.println("Error DB " + ex);
//        }
//        logger.traceExit();
//        return Optional.empty();
//    }
//}