package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.CasePc;
import com.codegym.computercomponents.repository.CasePcRepository;
import com.codegym.computercomponents.repository.ProductImageRepository;
import com.codegym.computercomponents.service.ICasePcService;
import org.springframework.stereotype.Service;

@Service
public class CasePcService extends BaseService<CasePc, CasePcRepository> implements ICasePcService {

    public CasePcService(CasePcRepository casePcRepository, ProductImageRepository productImageRepository) {
        super(casePcRepository, productImageRepository);
    }
}
