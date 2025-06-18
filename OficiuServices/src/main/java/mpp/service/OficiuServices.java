package mpp.service;

import mpp.domain.Participant;
import mpp.domain.Proba;
import mpp.domain.Utilizator;
import mpp.domain.Inscriere;
import java.util.Optional;

public interface OficiuServices {
    Utilizator login(Utilizator utilizator, OficiuObserver client) throws OficiuException;
    void logout(Utilizator user) throws OficiuException;

    Optional<Participant> saveParticipant(Participant participant) throws OficiuException;

    Iterable<Proba> findAllProbe() throws OficiuException;
    Optional<Proba> findProbaById(Integer id) throws OficiuException;

    int getNrParticipantiInscrisi(int idProba) throws OficiuException;
    Iterable<Participant> findParticipantiByProba(int idProba) throws OficiuException;
    Optional<Inscriere> saveInscriere(Inscriere inscriere) throws OficiuException;
    int getNrProbeParticipant(int idParticipant) throws OficiuException;

    Utilizator findUtilizator(String username, String password) throws OficiuException;
}