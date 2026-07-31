package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Mainboard;
import com.codegym.computercomponents.repository.IMainboardRepository;
import com.codegym.computercomponents.service.IMainboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainboardService implements IMainboardService {

    @Autowired
    private IMainboardRepository mainboardRepository;

    @Override
    public List<Mainboard> findAll() {
        return mainboardRepository.findAll();
    }

    @Override
    public Mainboard findById(Long id) {
        return mainboardRepository.findById(id).orElse(null);
    }

    @Override
    public Mainboard save(Mainboard mainboard) {
        return mainboardRepository.save(mainboard);
    }

    @Override
    public void deleteById(Long id) {
        mainboardRepository.deleteById(id);
    }
}
