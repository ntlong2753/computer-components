package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Mainboard;
import com.codegym.computercomponents.repository.MainboardRepository;
import com.codegym.computercomponents.service.IMainboardService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainboardService implements IMainboardService {

    private final MainboardRepository mainboardRepository;

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
