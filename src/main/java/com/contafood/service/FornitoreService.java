package com.contafood.service;

import com.contafood.model.Fornitore;
import com.contafood.repository.FornitoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FornitoreService {

    private final FornitoreRepository fornitoreRepository;

    @Autowired
    public FornitoreService(final FornitoreRepository fornitoreRepository){
        this.fornitoreRepository = fornitoreRepository;
    }

    public Set<Fornitore> getAll(){
        return fornitoreRepository.findAll();
    }

    public void create(Fornitore fornitore){
        fornitoreRepository.save(fornitore);
    }
}
