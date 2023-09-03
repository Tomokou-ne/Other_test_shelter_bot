package com.example.shelbot.service;

import com.example.shelbot.exceptions.DogNotFoundException;
import com.example.shelbot.model.Dog;
import com.example.shelbot.repository.DogRepository;
import org.springframework.stereotype.Service;

/*
 * CRUD операции для собак
 * @автор Елена Никитина
 */
@Service
public class DogService {
    private final DogRepository repository;

    public DogService(DogRepository repository) {
        this.repository = repository;
    }

    public Dog getById(Long id) {
        return repository.findById(id).orElseThrow(DogNotFoundException::new);
    }

    public Dog addDog(Dog dog) {
        return repository.save(dog);
    }

    public Dog update(Dog dog) {
        if (dog.getId() != null) {
            if (getById(dog.getId()) != null) {
                return repository.save(dog);
            }
        }
        throw new DogNotFoundException();
    }

    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
