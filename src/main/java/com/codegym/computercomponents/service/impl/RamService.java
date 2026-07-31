package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Ram;
import com.codegym.computercomponents.repository.RamRepository;
import com.codegym.computercomponents.repository.ProductImageRepository;
import com.codegym.computercomponents.service.IRamService;
import org.springframework.stereotype.Service;

@Service
public class RamService extends BaseService<Ram, RamRepository> implements IRamService {

    public RamService(RamRepository ramRepository, ProductImageRepository productImageRepository) {
        super(ramRepository, productImageRepository);
    }
}
