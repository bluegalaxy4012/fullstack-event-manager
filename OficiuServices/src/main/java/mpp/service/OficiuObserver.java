package mpp.service;

import mpp.domain.Participant;
import mpp.domain.Proba;
import mpp.domain.Inscriere;
import mpp.domain.Utilizator;

public interface OficiuObserver {
    //void participantAdded(Participant participant);

    void inscriereAdded(Inscriere i);
    //void utilizatorLoggedOut(Utilizator utilizator);
}