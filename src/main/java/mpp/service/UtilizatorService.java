package mpp.service;

import mpp.domain.Utilizator;
import mpp.repository.UtilizatorRepository;

public class UtilizatorService {
    private final UtilizatorRepository utilizatorRepo;

    public UtilizatorService(UtilizatorRepository utilizatorRepo) {
        this.utilizatorRepo = utilizatorRepo;
    }

    public Utilizator findUtilizator(String username, String password) {
        return utilizatorRepo.findUtilizator(username, password);
    }
}