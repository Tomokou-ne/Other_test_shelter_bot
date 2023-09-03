package com.example.shelbot.service;


import com.example.shelbot.exceptions.DogOwnersNotFoundException;
import com.example.shelbot.model.DogOwners;
import com.example.shelbot.repository.DogOwnersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс Сервис для владельцев котиков
 * @author Королёв Артем
 **/
@Service
public class DogOwnersService {
        private final DogOwnersRepository repository;
        private static final Logger logger = LoggerFactory.getLogger(DogOwnersRepository.class);
        public DogOwnersService(DogOwnersRepository repository) {
            this.repository = repository;
        }
        public DogOwners getById(Long id) {
            logger.info("Was invoked method to get a CatOwners by id={}", id);
            return this.repository.findById(id)
                    .orElseThrow();
        }
        public DogOwners create(DogOwners dogOwners) {
            logger.info("Was invoked method to create a catOwners");
            return this.repository.save(dogOwners);
        }
        public DogOwners update(DogOwners dogOwners) {
            logger.info("Was invoked method to update a catOwners");
            if (dogOwners.getId() != null && getById(dogOwners.getId()) != null) {
                return repository.save(dogOwners);
            }
            throw new DogOwnersNotFoundException();
        }
        public void removeById(Long id) {
            logger.info("Was invoked method to remove a catOwners by id={}", id);
            this.repository.deleteById(id);
        }
        public Collection<DogOwners> getAll() {
            logger.info("Was invoked method to get all catOwners");

            return this.repository.findAll();
        }
        public Collection<DogOwners> getByChatId(Long chatId) {
            logger.info("Was invoked method to remove a catOwners by chatId={}", chatId);

            return this.repository.getDogOwnerByChatId(chatId);
        }

}
