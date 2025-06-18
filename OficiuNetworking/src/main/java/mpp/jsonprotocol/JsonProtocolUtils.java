package mpp.jsonprotocol;

import mpp.dto.*;
import mpp.domain.*;

public class JsonProtocolUtils {

    public static Response createOkResponse() {
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Response createErrorResponse(String errorMessage) {
        Response resp = new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

    public static Response createParticipantSavedResponse(Participant participant) {
        Response resp = new Response();
        resp.setType(ResponseType.PARTICIPANT_SAVED);
        resp.setParticipant(DTOUtils.getDTO(participant));
        return resp;
    }

    public static Response createAllProbeResponse(Proba[] probeArray) {
        Response resp = new Response();
        resp.setType(ResponseType.ALL_PROBE);
        resp.setProbe(DTOUtils.getDTO(probeArray));
        return resp;
    }



    public static Response createProbaFoundResponse(Proba proba) {
        Response resp = new Response();
        resp.setType(ResponseType.PROBA_FOUND);
        resp.setProba(DTOUtils.getDTO(proba));
        return resp;
    }

    public static Response createNrParticipantiInscrisiResponse(int count) {
        Response resp = new Response();
        resp.setType(ResponseType.NR_PARTICIPANTI_INSCRISI);
        resp.setCount(count);
        return resp;
    }

    public static Response createParticipantiByProbaResponse(Participant[] participanti) {
        Response resp = new Response();
        resp.setType(ResponseType.PARTICIPANTI_BY_PROBA);
        resp.setParticipanti(DTOUtils.getDTO(participanti));
        return resp;
    }

    public static Response createInscriereSavedResponse(Inscriere inscriere) {
        Response resp = new Response();
        resp.setType(ResponseType.INSCRIERE_SAVED);
        resp.setInscriere(DTOUtils.getDTO(inscriere));
        return resp;
    }

    public static Response createNrProbeParticipantResponse(int count) {
        Response resp = new Response();
        resp.setType(ResponseType.NR_PROBE_PARTICIPANT);
        resp.setCount(count);
        return resp;
    }

    public static Response createUtilizatorLoggedInResponse(Utilizator utilizator) {
        Response resp = new Response();
        resp.setType(ResponseType.LOGIN_SUCCESS);
        resp.setUtilizator(DTOUtils.getDTO(utilizator));
        return resp;
    }

    public static Request createFindAllProbeRequest() {
        Request req = new Request();
        req.setType(RequestType.FIND_ALL_PROBE);
        return req;
    }

    public static Request createLoginRequest(Utilizator utilizator) {
        Request req = new Request();
        req.setType(RequestType.LOGIN);
        req.setUtilizator(DTOUtils.getDTO(utilizator));
        return req;
    }

    public static Request createLogoutRequest(Utilizator utilizator) {
        Request req = new Request();
        req.setType(RequestType.LOGOUT);
        req.setUtilizator(DTOUtils.getDTO(utilizator));
        return req;
    }

    public static Request createSaveParticipantRequest(Participant participant) {
        Request req = new Request();
        req.setType(RequestType.SAVE_PARTICIPANT);
        req.setParticipant(DTOUtils.getDTO(participant));
        return req;
    }


    public static Request createFindProbaByIdRequest(Integer id) {
        Request req = new Request();
        req.setType(RequestType.FIND_PROBA_BY_ID);
        ProbaDTO probaDTO = new ProbaDTO(id, null, null);
        req.setProba(probaDTO);
        return req;
    }

    public static Request createGetNrParticipantiInscrisiRequest(int idProba) {
        Request req = new Request();
        req.setType(RequestType.GET_NR_PARTICIPANTI_INSCRISI);
        ProbaDTO probaDTO = new ProbaDTO(idProba, null, null);
        req.setProba(probaDTO);
        return req;
    }

    public static Request createFindParticipantiByProbaRequest(int idProba) {
        Request req = new Request();
        req.setType(RequestType.FIND_PARTICIPANTI_BY_PROBA);
        ProbaDTO probaDTO = new ProbaDTO(idProba, null, null);
        req.setProba(probaDTO);
        return req;
    }

    public static Request createSaveInscriereRequest(Inscriere inscriere) {
        Request req = new Request();
        req.setType(RequestType.SAVE_INSCRIERE);
        req.setInscriere(DTOUtils.getDTO(inscriere));
        return req;
    }

    public static Request createGetNrProbeParticipantRequest(int idParticipant) {
        Request req = new Request();
        req.setType(RequestType.GET_NR_PROBE_PARTICIPANT);
        ParticipantDTO participantDTO = new ParticipantDTO(idParticipant, null, 0);
        req.setParticipant(participantDTO);
        return req;
    }

    public static Request createFindUtilizatorRequest(String username, String password) {
        Request req = new Request();
        req.setType(RequestType.FIND_UTILIZATOR);
        UtilizatorDTO utilizatorDTO = new UtilizatorDTO(0, username, password);
        req.setUtilizator(utilizatorDTO);
        return req;
    }
}