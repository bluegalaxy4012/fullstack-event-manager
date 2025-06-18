package mpp.service;

import mpp.domain.Participant;
import mpp.repository.ParticipantRepository;

import java.util.Optional;

public class ParticipantService {
    private final ParticipantRepository participantRepo;

    public ParticipantService(ParticipantRepository participantRepo) {
        this.participantRepo = participantRepo;
    }

    public Optional<Participant> save(Participant entity) {
        return participantRepo.save(entity);

    }

    public Optional<Participant> getParticipantMRU() {
        return participantRepo.getParticipantMRU();
    }
}