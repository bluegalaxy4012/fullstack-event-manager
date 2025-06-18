package mpp.repository;

import mpp.domain.Inscriere;
import mpp.domain.Participant;
import mpp.domain.Proba;

public interface InscriereRepository extends Repository<Integer, Inscriere> {

    Participant findParticipantById(int id);

    Proba findProbaById(int id);

    Iterable<Participant> findParticipantiByProba(int idProba);

    int getNrParticipantiInscrisi(int idProba);

    int getNrProbeParticipant(int idParticipant);
}