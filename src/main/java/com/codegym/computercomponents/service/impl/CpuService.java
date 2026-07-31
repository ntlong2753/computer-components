package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Cpu;
import com.codegym.computercomponents.repository.CpuRepository;
import com.codegym.computercomponents.repository.ProductImageRepository;
import com.codegym.computercomponents.service.ICpuService;
import org.springframework.stereotype.Service;

@Service
public class CpuService extends BaseService<Cpu, CpuRepository> implements ICpuService {

    public CpuService(CpuRepository cpuRepository, ProductImageRepository productImageRepository) {
        super(cpuRepository, productImageRepository);
    }
}
