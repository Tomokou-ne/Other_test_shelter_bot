package com.example.shelbot.repository;

import com.example.shelbot.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Класс репозиторий собак
@автор Елена Никитина
 */
@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
}