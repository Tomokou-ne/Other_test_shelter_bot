package com.example.shelbot.controller;

import com.example.shelbot.model.DogOwners;
import com.example.shelbot.service.DogOwnersService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Класс Контролер для владельцев собак
 */
@RestController
@RequestMapping("dog-owners")
public class DogOwnersController {
    private final DogOwnersService service;

    public DogOwnersController(DogOwnersService service) {
        this.service = service;
    }

    @Operation(summary = "Получение пользователя по id")
    @GetMapping("/{id}")
    public DogOwners getById(@PathVariable Long id) {
        return this.service.getById(id);
    }

    @Operation(summary = "Просмотр всех пользователей",
            description = "Просмотр всех пользователей, либо определенного пользователя по chat_id")
    @GetMapping("/all")
    public Collection<DogOwners> getAll(@RequestParam(required = false) Long chatId) {
        if (chatId != null) {
            return service.getByChatId(chatId);
        }
        return service.getAll();
    }
    @Operation(summary = "Создание пользователя")
    @PostMapping()
    public DogOwners save(@RequestBody DogOwners dogOwner) {
        return this.service.create(dogOwner);
    }
    @Operation(summary = "Изменение данных пользователя")
    @PutMapping
    public DogOwners update(@RequestBody DogOwners dogOwner) {
        return this.service.update(dogOwner);
    }
    @Operation(summary = "Удаление пользователей по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        service.removeById(id);
        return ResponseEntity.ok().build();
    }
}

