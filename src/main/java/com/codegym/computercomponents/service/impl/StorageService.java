package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Storage;
import com.codegym.computercomponents.repository.IStorageRepository;
import com.codegym.computercomponents.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService implements IStorageService {

    @Autowired
    private IStorageRepository storageRepository;

    @Override
    public List<Storage> findAll() {
        return storageRepository.findAll();
    }

    @Override
    public Storage findById(Long id) {
        return storageRepository.findById(id).orElse(null);
    }

    @Override
    public Storage save(Storage storage) {
        return storageRepository.save(storage);
    }

    @Override
    public void deleteById(Long id) {
        storageRepository.deleteById(id);
    }
}
