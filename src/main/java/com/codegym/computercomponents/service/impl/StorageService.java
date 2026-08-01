package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Storage;
import com.codegym.computercomponents.repository.StorageRepository;
import com.codegym.computercomponents.service.IStorageService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService implements IStorageService {

    private final StorageRepository storageRepository;

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
