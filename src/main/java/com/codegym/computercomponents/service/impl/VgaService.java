package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Vga;
import com.codegym.computercomponents.repository.VgaRepository;
import com.codegym.computercomponents.repository.ProductImageRepository;
import com.codegym.computercomponents.service.IVgaService;
import org.springframework.stereotype.Service;

@Service
public class VgaService extends BaseService<Vga, VgaRepository> implements IVgaService {

    public VgaService(VgaRepository vgaRepository, ProductImageRepository productImageRepository) {
        super(vgaRepository, productImageRepository);
    }
}
