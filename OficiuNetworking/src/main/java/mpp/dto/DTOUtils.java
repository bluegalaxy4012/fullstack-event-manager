package mpp.dto;

import mpp.domain.Inscriere;
import mpp.domain.Participant;
import mpp.domain.Proba;
import mpp.domain.Utilizator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DTOUtils {

    public static UtilizatorDTO getDTO(Utilizator utilizator) {
        return new UtilizatorDTO(utilizator.getId(), utilizator.getNumeUtilizator(), utilizator.getParola());
    }

    public static Utilizator getFromDTO(UtilizatorDTO dto) {
        Utilizator utilizator = new Utilizator(dto.getNumeUtilizator(), dto.getParola());
        utilizator.setId(dto.getId());
        return utilizator;
    }

    public static ParticipantDTO getDTO(Participant participant) {
        int id = (participant.getId() != null) ? participant.getId() : 0;
        return new ParticipantDTO(id, participant.getNume(), participant.getVarsta());
    }

    public static Participant getFromDTO(ParticipantDTO dto) {
        Participant participant = new Participant(dto.getNume(), dto.getVarsta());
        participant.setId(dto.getId());
        return participant;
    }

    public static ProbaDTO getDTO(Proba proba) {
        return new ProbaDTO(proba.getId(), proba.getDistanta(), proba.getStil());
    }

    public static Proba getFromDTO(ProbaDTO dto) {
        Proba proba = new Proba(dto.getDistanta(), dto.getStil());
        proba.setId(dto.getId());
        return proba;
    }

    public static Inscriere getFromDTO(InscriereDTO dto) {
        var participant = new Participant();
        participant.setId(dto.getParticipantId());
        var proba = new Proba();
        proba.setId(dto.getProbaId());
        var inscriere = new Inscriere(participant, proba);
        inscriere.setId(dto.getId());
        return inscriere;
    }

    public static InscriereDTO getDTO(Inscriere inscriere) {
        int id = (inscriere.getId() != null) ? inscriere.getId() : 0;
        return new InscriereDTO(id, inscriere.getParticipant().getId(), inscriere.getProba().getId());
    }

    public static ParticipantDTO[] getDTO(Participant[] participants) {
        ParticipantDTO[] participantDTOs = new ParticipantDTO[participants.length];
        for (int i = 0; i < participants.length; i++) {
            participantDTOs[i] = getDTO(participants[i]);
        }
        return participantDTOs;
    }

    public static Participant[] getFromDTO(ParticipantDTO[] participantDTOs) {
        Participant[] participants = new Participant[participantDTOs.length];
        for (int i = 0; i < participantDTOs.length; i++) {
            participants[i] = getFromDTO(participantDTOs[i]);
        }
        return participants;
    }

    public static ProbaDTO[] getDTO(Proba[] probe) {
        ProbaDTO[] probaDTOs = new ProbaDTO[probe.length];
        for (int i = 0; i < probe.length; i++) {
            probaDTOs[i] = getDTO(probe[i]);
        }
        return probaDTOs;
    }

    public static Proba[] getFromDTO(ProbaDTO[] probaDTOs) {
        Proba[] probe = new Proba[probaDTOs.length];
        for (int i = 0; i < probaDTOs.length; i++) {
            probe[i] = getFromDTO(probaDTOs[i]);
        }
        return probe;
    }
}