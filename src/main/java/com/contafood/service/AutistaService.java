package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Autista;
import com.contafood.repository.AutistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AutistaService {

    private final AutistaRepository autistaRepository;

    @Autowired
    public AutistaService(final AutistaRepository autistaRepository){
        this.autistaRepository = autistaRepository;
    }

    public Set<Autista> getAll(){
        return autistaRepository.findAll();
    }

    public Autista getOne(Long autistaId){
        return autistaRepository.findById(autistaId).orElseThrow(ResourceNotFoundException::new);
    }

    public Autista create(Autista autista){
        Autista createdAutista = autistaRepository.save(autista);
        return createdAutista;
    }

    public Autista update(Autista autista){
        Autista updatedAutista = autistaRepository.save(autista);
        return updatedAutista;
    }

    public void delete(Long autistaId){
        autistaRepository.deleteById(autistaId);
    }
}
