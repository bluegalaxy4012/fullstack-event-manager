package mpp.service;

import mpp.domain.Inscriere;
import mpp.domain.Participant;
import mpp.repository.InscriereRepository;

import java.util.Optional;

public class InscriereService {
    private final InscriereRepository inscriereRepo;

    public InscriereService(InscriereRepository inscriereRepo) {
        this.inscriereRepo = inscriereRepo;
    }


    public int getNrParticipantiInscrisi(int idProba) {
        return inscriereRepo.getNrParticipantiInscrisi(idProba);
    }

    public Iterable<Participant> findParticipantiByProba(int idProba) {
        return inscriereRepo.findParticipantiByProba(idProba);
    }

    public Optional<Inscriere> save(Inscriere entity) {
        return inscriereRepo.save(entity);
    }

    public int getNrProbeParticipant(int idParticipant) {
        return inscriereRepo.getNrProbeParticipant(idParticipant);
    }
}