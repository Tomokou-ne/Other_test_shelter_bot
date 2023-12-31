package com.example.shelbot.service;

import com.example.shelbot.exceptions.CatOwnersNotFoundException;
import com.example.shelbot.model.CatOwners;
import com.example.shelbot.repository.CatOwnersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс Сервис для владельцев котиков
 * @author Королёв Артем
 **/
@Service
public class CatOwnersService {
    private final CatOwnersRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(CatOwnersRepository.class);
    public CatOwnersService(CatOwnersRepository repository) {
        this.repository = repository;
    }
    public CatOwners getById(Long id) {
        logger.info("Was invoked method to get a CatOwners by id={}", id);
        return this.repository.findById(id)
                .orElseThrow();
    }
    public CatOwners create(CatOwners catOwners) {
        logger.info("Was invoked method to create a catOwners");
        return this.repository.save(catOwners);
    }
    public CatOwners update(CatOwners catOwners) {
        logger.info("Was invoked method to update a catOwners");
        if (catOwners.getId() != null && getById(catOwners.getId()) != null) {
            return repository.save(catOwners);
        }
        throw new CatOwnersNotFoundException();
    }
    public void removeById(Long id) {
        logger.info("Was invoked method to remove a catOwners by id={}", id);
        this.repository.deleteById(id);
    }
    public Collection<CatOwners> getAll() {
        logger.info("Was invoked method to get all catOwners");

        return this.repository.findAll();
    }
    public Collection<CatOwners> getByChatId(Long chatId) {
        logger.info("Was invoked method to remove a catOwners by chatId={}", chatId);
        return this.repository.getCatOwnerByChatId(chatId);
    }
}
