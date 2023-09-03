package com.example.shelbot.service;

import com.example.shelbot.exceptions.CatNotFoundException;
import com.example.shelbot.model.Cat;
import com.example.shelbot.repository.CatRepository;
import org.springframework.stereotype.Service;

/**
 * Класс сервис для котов (CRUD операции)
 * @author Герасименко Максим
 */
@Service
public class CatService {
    private final CatRepository repository;

    public CatService(CatRepository repository) {
        this.repository = repository;
    }

    /**
     Добавление нового кота в список
     */
    public Cat addCat(Cat cat) {
        return this.repository.save(cat);
    }

    /**
     получение кота по ID
     @param id кота
     */
    public Cat getById(Long id) {
        return this.repository.findById(id).orElseThrow();
    }
    /**
     * Обновление кота
     */

    public Cat update(Cat cat) {
        if (cat.getId() != null && getById(cat.getId()) != null) {
            return repository.save(cat);
        }
        throw new CatNotFoundException();
    }
    /**
     * Удаление кота из списка по ID
     * @param id кота
     */

    public void removeById(Long id) {
        this.repository.deleteById(id);
    }
}
