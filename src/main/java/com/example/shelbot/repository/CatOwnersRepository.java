package com.example.shelbot.repository;

import com.example.shelbot.model.CatOwners;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CatOwnersRepository extends JpaRepository<CatOwners, Long> {
    Set<CatOwners> getCatOwnerByChatId(Long chatId);
}
