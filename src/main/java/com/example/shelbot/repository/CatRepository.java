package com.example.shelbot.repository;

import com.example.shelbot.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Класс репозиторий котов
 * @author Герасименко Максим
 */
public interface CatRepository extends JpaRepository<Cat, Long> {
}
