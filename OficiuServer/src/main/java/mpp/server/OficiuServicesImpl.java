package mpp.server;

import mpp.domain.Participant;
import mpp.domain.Proba;
import mpp.domain.Utilizator;
import mpp.domain.Inscriere;
import mpp.repository.InscriereHibernateRepository;
import mpp.repository.ParticipantHibernateRepository;
//import mpp.repository.ParticipantRepository;
//import mpp.repository.ProbaRepository;
//import mpp.repository.UtilizatorRepository;
//import mpp.repository.InscriereRepository;
import mpp.repository.ProbaHibernateRepository;
import mpp.repository.UtilizatorHibernateRepository;
import mpp.service.OficiuException;
import mpp.service.OficiuObserver;
import mpp.service.OficiuServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OficiuServicesImpl implements OficiuServices {
//    private final ParticipantRepository participantRepo;
//    private final ProbaRepository probaRepo;
//    private final UtilizatorRepository utilizatorRepo;
//    private final InscriereRepository inscriereRepo;
//
//    private final Map<String, OficiuObserver> loggedClients;
//    private static final Logger logger = LogManager.getLogger(OficiuServicesImpl.class);
//    private final int defaultThreadsNo = 3;
//
//    public OficiuServicesImpl(ParticipantRepository participantRepo, ProbaRepository probaRepo, UtilizatorRepository utilizatorRepo, InscriereRepository inscriereRepo) {
//        this.participantRepo = participantRepo;
//        this.probaRepo = probaRepo;
//        this.utilizatorRepo = utilizatorRepo;
//        this.inscriereRepo = inscriereRepo;
//        this.loggedClients = new ConcurrentHashMap<>();
//    }


    private final ParticipantHibernateRepository participantRepo;
    private final ProbaHibernateRepository probaRepo;
    private final UtilizatorHibernateRepository utilizatorRepo;
    private final InscriereHibernateRepository inscriereRepo;

    private final Map<String, OficiuObserver> loggedClients;
    private static final Logger logger = LogManager.getLogger(OficiuServicesImpl.class);
    private final int defaultThreadsNo = 3;

    public OficiuServicesImpl(ParticipantHibernateRepository participantRepo, ProbaHibernateRepository probaRepo, UtilizatorHibernateRepository utilizatorRepo, InscriereHibernateRepository inscriereRepo) {
        this.participantRepo = participantRepo;
        this.probaRepo = probaRepo;
        this.utilizatorRepo = utilizatorRepo;
        this.inscriereRepo = inscriereRepo;
        this.loggedClients = new ConcurrentHashMap<>();
    }


    public synchronized Utilizator login(Utilizator utilizator, OficiuObserver client) throws OficiuException {
        Utilizator u = utilizatorRepo.findUtilizator(utilizator.getNumeUtilizator(), utilizator.getParola());
        if (u != null) {
            if (loggedClients.get(u.getNumeUtilizator()) != null) {
                throw new OficiuException("Utilizator deja logat");
            }
            loggedClients.put(u.getNumeUtilizator(), client);
            return u;
        } else {
            throw new OficiuException("Login esuat");
        }
    }

    @Override
    public synchronized void logout(Utilizator user) throws OficiuException {
        System.out.println("logging out - " + user.getNumeUtilizator());
        OficiuObserver localClient = loggedClients.remove(user.getNumeUtilizator());
        if (localClient == null) {
            throw new OficiuException("User " + user.getNumeUtilizator() + " is not logged in.");
        }
    }

    @Override
    public synchronized Optional<Participant> saveParticipant(Participant participant) throws OficiuException {

        Optional<Participant> p = participantRepo.save(participant);
        return p;
    }

    @Override
    public synchronized Iterable<Proba> findAllProbe() throws OficiuException {
        return probaRepo.findAll();
    }



    @Override
    public synchronized Optional<Proba> findProbaById(Integer id) throws OficiuException {
        return probaRepo.findOne(id);
    }

    @Override
    public synchronized int getNrParticipantiInscrisi(int idProba) throws OficiuException {
        return inscriereRepo.getNrParticipantiInscrisi(idProba);
    }

    @Override
    public synchronized Iterable<Participant> findParticipantiByProba(int idProba) throws OficiuException {
        return inscriereRepo.findParticipantiByProba(idProba);
    }

    @Override
    public synchronized Optional<Inscriere> saveInscriere(Inscriere inscriere) throws OficiuException {
        Optional<Inscriere> i = inscriereRepo.save(inscriere);
        if (i.isPresent()) {
            notifyObserversInscriereAdded(i.get());
        } else {
            throw new OficiuException("Failed to retrieve the most recently added inscriere.");
        }
        return i;
    }

    private void notifyObserversInscriereAdded(Inscriere inscriere) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (OficiuObserver observer : loggedClients.values()) {
            //System.out.println("notificam obs " + observer + " inscriere " + inscriere);
            executor.execute(() -> {
                try {
                    observer.inscriereAdded(inscriere);
                } catch (Exception e) {
                    logger.error("Error notifying observer about inscriere: {}", e.getMessage());
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public synchronized int getNrProbeParticipant(int idParticipant) throws OficiuException {
        return inscriereRepo.getNrProbeParticipant(idParticipant);
    }

    @Override
    public synchronized Utilizator findUtilizator(String username, String password) throws OficiuException {
        return utilizatorRepo.findUtilizator(username, password);
    }
}