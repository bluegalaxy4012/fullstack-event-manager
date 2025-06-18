package mpp.repository.database;

import mpp.domain.Inscriere;
import mpp.domain.Participant;
import mpp.domain.Proba;
import mpp.repository.InscriereRepository;
import mpp.repository.ParticipantRepository;
import mpp.repository.ProbaRepository;
import mpp.repository.database.ParticipantDbRepository;
import mpp.repository.database.ProbaDbRepository;
import mpp.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class InscriereDbRepository implements InscriereRepository {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private final ParticipantRepository participantRepo;
    private final ProbaRepository probaRepo;

    public InscriereDbRepository(Properties props, ParticipantRepository participantRepo, ProbaRepository probaRepo) {
        logger.info("Initializing InscriereDbRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
        this.participantRepo = participantRepo;
        this.probaRepo = probaRepo;
    }

    @Override
    public Participant findParticipantById(int id) {
        Optional<Participant> participant = participantRepo.findOne(id);
        return participant.orElse(null);
    }

    @Override
    public Proba findProbaById(int id) {
        Optional<Proba> proba = probaRepo.findOne(id);
        return proba.orElse(null);
    }

    @Override
    public Iterable<Participant> findParticipantiByProba(int idProba) {
        logger.traceEntry("finding participants for proba with id {}", idProba);
        Connection con = dbUtils.getConnection();
        List<Participant> participanti = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select participant_id from inscrieri where proba_id = ?")) {
            preStmt.setInt(1, idProba);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int participantId = result.getInt("participant_id");
                    Participant participant = findParticipantById(participantId);
                    participanti.add(participant);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(participanti);
        return participanti;
    }


    @Override
    public Optional<Inscriere> findOne(Integer id) {
        logger.traceEntry("finding inscriere by id {}", id);
        Connection con = dbUtils.getConnection();
        Inscriere inscriere = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from inscrieri where id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int participantId = result.getInt("participant_id");
                    int probaId = result.getInt("proba_id");
                    inscriere = new Inscriere();
                    inscriere.setId(id);
                    inscriere.setParticipant(findParticipantById(participantId));
                    inscriere.setProba(findProbaById(probaId));
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(inscriere);
        return Optional.ofNullable(inscriere);
    }

    @Override
    public Iterable<Inscriere> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Inscriere> inscrieri = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from inscrieri")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int participantId = result.getInt("participant_id");
                    int probaId = result.getInt("proba_id");
                    Inscriere inscriere = new Inscriere();
                    inscriere.setId(id);
                    inscriere.setParticipant(findParticipantById(participantId));
                    inscriere.setProba(findProbaById(probaId));
                    inscrieri.add(inscriere);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(inscrieri);
        return inscrieri;
    }

    @Override
    public Optional<Inscriere> save(Inscriere entity) {
        logger.traceEntry("saving inscriere {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into inscrieri (participant_id, proba_id) values (?,?)")) {
            preStmt.setInt(1, entity.getParticipant().getId());
            preStmt.setInt(2, entity.getProba().getId());
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
    public Optional<Inscriere> delete(Integer id) {
        logger.traceEntry("deleting inscriere with id {}", id);
        Connection con = dbUtils.getConnection();
        Optional<Inscriere> inscriere = findOne(id);
        try (PreparedStatement preStmt = con.prepareStatement("delete from inscrieri where id = ?")) {
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return inscriere;
    }

    @Override
    public Optional<Inscriere> update(Inscriere entity) {
        logger.traceEntry("updating inscriere {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update inscrieri set participant_id = ?, proba_id = ? where id = ?")) {
            preStmt.setInt(1, entity.getParticipant().getId());
            preStmt.setInt(2, entity.getProba().getId());
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
    public int getNrParticipantiInscrisi(int idProba) {
        logger.traceEntry("getting number of participants for proba with id {}", idProba);
        Connection con = dbUtils.getConnection();
        int nrParticipanti = 0;
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) from inscrieri where proba_id = ?")) {
            preStmt.setInt(1, idProba);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    nrParticipanti = result.getInt(1);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(nrParticipanti);
        return nrParticipanti;
    }

    @Override
    public int getNrProbeParticipant(int idParticipant) {
        logger.traceEntry("getting number of probes for participant with id {}", idParticipant);
        Connection con = dbUtils.getConnection();
        int nrProbe = 0;
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) from inscrieri where participant_id = ?")) {
            preStmt.setInt(1, idParticipant);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    nrProbe = result.getInt(1);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(nrProbe);
        return nrProbe;
    }



}