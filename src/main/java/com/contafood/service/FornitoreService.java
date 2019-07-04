package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Fornitore;
import com.contafood.repository.FornitoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    public Fornitore getOne(Long fornitoreId){
        return fornitoreRepository.findById(fornitoreId).orElseThrow(ResourceNotFoundException::new);
    }

    public Fornitore create(Fornitore fornitore){
        Fornitore createdFornitore = fornitoreRepository.save(fornitore);
        return createdFornitore;
    }

    public Fornitore update(Fornitore fornitore){
        Fornitore updatedFornitore = fornitoreRepository.save(fornitore);
        return updatedFornitore;
    }

    public void delete(Long fornitoreId){
        fornitoreRepository.deleteById(fornitoreId);
    }
}
