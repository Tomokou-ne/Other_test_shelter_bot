package com.example.shelbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Класс модель котов
 * @author Герасименко Максим
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_cat")
    private String name;
    private String breed;
    private int yearOfBirth;
    private String description;


    @Override
    public String toString() {
        return "Кличка кота - " + getName() + ", год рождения - " + getYearOfBirth() +
                ", порода - " + getBreed() + ", дополнительная информация о коте - " + description;
    }

}
