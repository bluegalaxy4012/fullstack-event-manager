package mpp.repository;

import mpp.domain.Participant;

import java.util.Optional;

public interface ParticipantRepository extends Repository<Integer, Participant> {
    Optional<Participant> getParticipantMRU();

}