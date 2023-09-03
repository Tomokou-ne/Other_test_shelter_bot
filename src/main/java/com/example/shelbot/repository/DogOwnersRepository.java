package com.example.shelbot.repository;

import com.example.shelbot.model.DogOwners;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DogOwnersRepository extends JpaRepository<DogOwners, Long> {
    Set<DogOwners> getDogOwnerByChatId(Long chatId);
}
