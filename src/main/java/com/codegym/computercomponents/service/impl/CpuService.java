package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Cpu;
import com.codegym.computercomponents.repository.CpuRepository;
import com.codegym.computercomponents.service.ICpuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CpuService implements ICpuService {

    private final CpuRepository cpuRepository;

    public CpuService(CpuRepository cpuRepository) {
        this.cpuRepository = cpuRepository;
    }

    @Override
    public List<Cpu> findAll() {
        return cpuRepository.findAll();
    }

    @Override
    public Cpu findById(Long id) {
        return cpuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy CPU với id = " + id));
    }

    @Override
    public Cpu save(Cpu cpu) {
        return cpuRepository.save(cpu);
    }

    @Override
    public void deleteById(Long id) {
        cpuRepository.deleteById(id);
    }
}
