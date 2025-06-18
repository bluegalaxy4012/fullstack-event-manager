package mpp.protobuffprotocol;

import mpp.domain.*;
import mpp.dto.DTOUtils;
import mpp.dto.InscriereDTO;
import mpp.dto.ParticipantDTO;
import mpp.dto.ProbaDTO;
import mpp.dto.UtilizatorDTO;

import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {



    public static OficiuProtobuffs.OficiuRequest createLoginRequest(Utilizator utilizator) {
        OficiuProtobuffs.UtilizatorDTO utilizatorDTO = OficiuProtobuffs.UtilizatorDTO.newBuilder()
                .setId(utilizator.getId())
                .setNumeUtilizator(utilizator.getNumeUtilizator())
                .setParola(utilizator.getParola())
                .build();

        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.LOGIN)
                .setUtilizator(utilizatorDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuRequest createLogoutRequest(Utilizator utilizator) {
        OficiuProtobuffs.UtilizatorDTO utilizatorDTO = OficiuProtobuffs.UtilizatorDTO.newBuilder()
                .setId(utilizator.getId())
                .setNumeUtilizator(utilizator.getNumeUtilizator())
                .setParola(utilizator.getParola())
                .build();

        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.LOGOUT)
                .setUtilizator(utilizatorDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuRequest createSaveParticipantRequest(Participant participant) {
        int id = (participant.getId() != null) ? participant.getId() : 0;

        OficiuProtobuffs.ParticipantDTO participantDTO = OficiuProtobuffs.ParticipantDTO.newBuilder()
                .setId(id)
                .setNume(participant.getNume())
                .setVarsta(participant.getVarsta())
                .build();

        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.SAVE_PARTICIPANT)
                .setParticipant(participantDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuRequest createFindAllProbeRequest() {
        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.FIND_ALL_PROBE)
                .build();
    }

    public static OficiuProtobuffs.OficiuRequest createFindProbaByIdRequest(Integer id) {
        OficiuProtobuffs.ProbaDTO probaDTO = OficiuProtobuffs.ProbaDTO.newBuilder()
                .setId(id)
                .setDistanta("")
                .setStil("")
                .build();

        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.FIND_PROBA_BY_ID)
                .setProba(probaDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuRequest createGetNrParticipantiInscrisiRequest(int idProba) {
        OficiuProtobuffs.ProbaDTO probaDTO = OficiuProtobuffs.ProbaDTO.newBuilder()
                .setId(idProba)
                .setDistanta("")
                .setStil("")
                .build();

        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.GET_NR_PARTICIPANTI_INSCRISI)
                .setProba(probaDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuRequest createFindParticipantiByProbaRequest(int idProba) {
        OficiuProtobuffs.ProbaDTO probaDTO = OficiuProtobuffs.ProbaDTO.newBuilder()
                .setId(idProba)
                .setDistanta("")
                .setStil("")
                .build();

        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.FIND_PARTICIPANTI_BY_PROBA)
                .setProba(probaDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuRequest createSaveInscriereRequest(Inscriere inscriere) {
        OficiuProtobuffs.ParticipantDTO participantDTO = OficiuProtobuffs.ParticipantDTO.newBuilder()
                .setId(inscriere.getParticipant().getId())
                .setNume(inscriere.getParticipant().getNume())
                .setVarsta(inscriere.getParticipant().getVarsta())
                .build();

        OficiuProtobuffs.ProbaDTO probaDTO = OficiuProtobuffs.ProbaDTO.newBuilder()
                .setId(inscriere.getProba().getId())
                .setDistanta(inscriere.getProba().getDistanta())
                .setStil(inscriere.getProba().getStil())
                .build();

        int id = (inscriere.getId() != null) ? inscriere.getId() : 0;

        OficiuProtobuffs.InscriereDTO inscriereDTO = OficiuProtobuffs.InscriereDTO.newBuilder()
                .setId(id)
                .setParticipant(participantDTO)
                .setProba(probaDTO)
                .build();

        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.SAVE_INSCRIERE)
                .setInscriere(inscriereDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuRequest createGetNrProbeParticipantRequest(int idParticipant) {
        OficiuProtobuffs.ParticipantDTO participantDTO = OficiuProtobuffs.ParticipantDTO.newBuilder()
                .setId(idParticipant)
                .setNume("")
                .setVarsta(0)
                .build();

        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.GET_NR_PROBE_PARTICIPANT)
                .setParticipant(participantDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuRequest createFindUtilizatorRequest(String username, String password) {
        OficiuProtobuffs.UtilizatorDTO utilizatorDTO = OficiuProtobuffs.UtilizatorDTO.newBuilder()
                .setId(0)
                .setNumeUtilizator(username)
                .setParola(password)
                .build();

        return OficiuProtobuffs.OficiuRequest.newBuilder()
                .setType(OficiuProtobuffs.OficiuRequest.Type.FIND_UTILIZATOR)
                .setUtilizator(utilizatorDTO)
                .build();
    }




    public static OficiuProtobuffs.OficiuResponse createOkResponse() {
        return OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.OK)
                .build();
    }

    public static OficiuProtobuffs.OficiuResponse createErrorResponse(String errorMessage) {
        return OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.ERROR)
                .setErrorMessage(errorMessage)
                .build();
    }

    public static OficiuProtobuffs.OficiuResponse createParticipantSavedResponse(Participant participant) {
        OficiuProtobuffs.ParticipantDTO participantDTO = OficiuProtobuffs.ParticipantDTO.newBuilder()
                .setId(participant.getId())
                .setNume(participant.getNume())
                .setVarsta(participant.getVarsta())
                .build();

        return OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.PARTICIPANT_SAVED)
                .setParticipant(participantDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuResponse createAllProbeResponse(Proba[] probeArray) {
        OficiuProtobuffs.OficiuResponse.Builder responseBuilder = OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.ALL_PROBE);

        for (Proba proba : probeArray) {
            OficiuProtobuffs.ProbaDTO probaDTO = OficiuProtobuffs.ProbaDTO.newBuilder()
                    .setId(proba.getId())
                    .setDistanta(proba.getDistanta())
                    .setStil(proba.getStil())
                    .build();
            responseBuilder.addProbe(probaDTO);
        }

        return responseBuilder.build();
    }

    public static OficiuProtobuffs.OficiuResponse createProbaFoundResponse(Proba proba) {
        OficiuProtobuffs.ProbaDTO probaDTO = OficiuProtobuffs.ProbaDTO.newBuilder()
                .setId(proba.getId())
                .setDistanta(proba.getDistanta())
                .setStil(proba.getStil())
                .build();

        return OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.PROBA_FOUND)
                .setProba(probaDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuResponse createNrParticipantiInscrisiResponse(int count) {
        return OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.NR_PARTICIPANTI_INSCRISI)
                .setCount(count)
                .build();
    }

    public static OficiuProtobuffs.OficiuResponse createParticipantiByProbaResponse(Participant[] participanti) {
        OficiuProtobuffs.OficiuResponse.Builder responseBuilder = OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.PARTICIPANTI_BY_PROBA);

        for (Participant participant : participanti) {
            OficiuProtobuffs.ParticipantDTO participantDTO = OficiuProtobuffs.ParticipantDTO.newBuilder()
                    .setId(participant.getId())
                    .setNume(participant.getNume())
                    .setVarsta(participant.getVarsta())
                    .build();
            responseBuilder.addParticipanti(participantDTO);
        }

        return responseBuilder.build();
    }

    public static OficiuProtobuffs.OficiuResponse createInscriereSavedResponse(Inscriere inscriere) {
        OficiuProtobuffs.ParticipantDTO participantDTO = OficiuProtobuffs.ParticipantDTO.newBuilder()
                .setId(inscriere.getParticipant().getId())
                .setNume(inscriere.getParticipant().getNume())
                .setVarsta(inscriere.getParticipant().getVarsta())
                .build();

        OficiuProtobuffs.ProbaDTO probaDTO = OficiuProtobuffs.ProbaDTO.newBuilder()
                .setId(inscriere.getProba().getId())
                .setDistanta(inscriere.getProba().getDistanta())
                .setStil(inscriere.getProba().getStil())
                .build();

        OficiuProtobuffs.InscriereDTO inscriereDTO = OficiuProtobuffs.InscriereDTO.newBuilder()
                .setId(inscriere.getId())
                .setParticipant(participantDTO)
                .setProba(probaDTO)
                .build();

        return OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.INSCRIERE_SAVED)
                .setInscriere(inscriereDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuResponse createNrProbeParticipantResponse(int count) {
        return OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.NR_PROBE_PARTICIPANT)
                .setCount(count)
                .build();
    }

    public static OficiuProtobuffs.OficiuResponse createUtilizatorLoggedInResponse(Utilizator utilizator) {
        OficiuProtobuffs.UtilizatorDTO utilizatorDTO = OficiuProtobuffs.UtilizatorDTO.newBuilder()
                .setId(utilizator.getId())
                .setNumeUtilizator(utilizator.getNumeUtilizator())
                .setParola(utilizator.getParola())
                .build();

        return OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.LOGIN_SUCCESS)
                .setUtilizator(utilizatorDTO)
                .build();
    }

    public static OficiuProtobuffs.OficiuResponse createInscriereAddedResponse(Inscriere inscriere) {
        OficiuProtobuffs.ParticipantDTO participantDTO = OficiuProtobuffs.ParticipantDTO.newBuilder()
                .setId(inscriere.getParticipant().getId())
                .setNume(inscriere.getParticipant().getNume())
                .setVarsta(inscriere.getParticipant().getVarsta())
                .build();

        OficiuProtobuffs.ProbaDTO probaDTO = OficiuProtobuffs.ProbaDTO.newBuilder()
                .setId(inscriere.getProba().getId())
                .setDistanta(inscriere.getProba().getDistanta())
                .setStil(inscriere.getProba().getStil())
                .build();

        OficiuProtobuffs.InscriereDTO inscriereDTO = OficiuProtobuffs.InscriereDTO.newBuilder()
                .setId(inscriere.getId())
                .setParticipant(participantDTO)
                .setProba(probaDTO)
                .build();

        return OficiuProtobuffs.OficiuResponse.newBuilder()
                .setType(OficiuProtobuffs.OficiuResponse.Type.INSCRIERE_ADDED)
                .setInscriere(inscriereDTO)
                .build();
    }



    public static Utilizator getUtilizator(OficiuProtobuffs.UtilizatorDTO utilizatorDTO) {
        return new Utilizator(utilizatorDTO.getId(), utilizatorDTO.getNumeUtilizator(), utilizatorDTO.getParola());
    }

    public static Utilizator getUtilizator(OficiuProtobuffs.OficiuResponse response) {
        return new Utilizator(
                response.getUtilizator().getId(),
                response.getUtilizator().getNumeUtilizator(),
                response.getUtilizator().getParola()
        );
    }

    public static Participant getParticipant(OficiuProtobuffs.ParticipantDTO participantDTO) {
        return new Participant(participantDTO.getId(), participantDTO.getNume(), participantDTO.getVarsta());
    }

    public static Participant getParticipant(OficiuProtobuffs.OficiuResponse response) {
        return new Participant(
                response.getParticipant().getId(),
                response.getParticipant().getNume(),
                response.getParticipant().getVarsta()
        );
    }

    public static Proba getProba(OficiuProtobuffs.ProbaDTO probaDTO) {
        return new Proba(probaDTO.getId(), probaDTO.getDistanta(), probaDTO.getStil());
    }

    public static Proba getProba(OficiuProtobuffs.OficiuResponse response) {
        return new Proba(
                response.getProba().getId(),
                response.getProba().getDistanta(),
                response.getProba().getStil()
        );
    }

    public static Inscriere getInscriere(OficiuProtobuffs.InscriereDTO inscriereDTO) {
        Participant participant = getParticipant(inscriereDTO.getParticipant());
        Proba proba = getProba(inscriereDTO.getProba());

        Inscriere inscriere = new Inscriere();
        inscriere.setId(inscriereDTO.getId());
        inscriere.setParticipant(participant);
        inscriere.setProba(proba);

        return inscriere;
    }

    public static Inscriere getInscriere(OficiuProtobuffs.OficiuResponse response) {
        return getInscriere(response.getInscriere());
    }

    public static Proba[] getProbe(OficiuProtobuffs.OficiuResponse response) {
        Proba[] result = new Proba[response.getProbeCount()];
        for (int i = 0; i < response.getProbeCount(); i++) {
            result[i] = getProba(response.getProbe(i));
        }
        return result;
    }

    public static Participant[] getParticipanti(OficiuProtobuffs.OficiuResponse response) {
        Participant[] result = new Participant[response.getParticipantiCount()];
        for (int i = 0; i < response.getParticipantiCount(); i++) {
            result[i] = getParticipant(response.getParticipanti(i));
        }
        return result;
    }
}