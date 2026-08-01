package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Psu;
import com.codegym.computercomponents.repository.PsuRepository;
import com.codegym.computercomponents.service.IPsuService;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PsuService implements IPsuService {

    private final PsuRepository psuRepository;

    @Override
    public List<Psu> findAll() {
        return psuRepository.findAll();
    }

    @Override
    public Psu findById(Long id) {
        return psuRepository.findById(id).orElse(null);
    }

    @Override
    public Psu save(Psu psu) {
        return psuRepository.save(psu);
    }

    @Override
    public void deleteById(Long id) {
        psuRepository.deleteById(id);
    }
}
