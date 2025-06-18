package mpp.service;

import mpp.domain.Proba;
import mpp.repository.ProbaRepository;

import java.util.Optional;

public class ProbaService {
    private final ProbaRepository probaRepo;

    public ProbaService(ProbaRepository probaRepo) {
        this.probaRepo = probaRepo;
    }

    public Iterable<Proba> findAll() {
        return probaRepo.findAll();
    }

    public Optional<Proba> findOne(Integer id) {
        return probaRepo.findOne(id);
    }

}