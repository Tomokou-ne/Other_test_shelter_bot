package com.example.shelbot.controller;

import com.example.shelbot.model.Cat;
import com.example.shelbot.service.CatService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

/**
 * Класс контроллер для котов
 * @author Герасименко Максим
 */
@RestController
@RequestMapping("cat")
public class CatController {

    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @Operation(summary = "Создание кота")
    @PostMapping
    public Cat addCat(@RequestBody Cat cat) {
        return catService.addCat(cat);
    }


    @Operation(summary = "Получение кота по id")
    @GetMapping("/{id}")
    public Cat getCatById(@PathVariable Long id) {
        return catService.getById(id);
    }

    @Operation(summary = "Обновление данных кота")
    @PutMapping("/{id}")
    public Cat updateCatById(@PathVariable Long id, @RequestBody Cat cat) {
        return catService.update(cat);
    }

    @Operation(summary = "Удаление кота по id")
    @DeleteMapping("/{id}")
    public void removeCat(@PathVariable Long id) {
        catService.removeById(id);
    }
}
