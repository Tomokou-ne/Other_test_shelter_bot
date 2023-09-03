package com.example.shelbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
Класс собаки
@автор Елена Никитина
*/
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_dog")
    private String name;
    private String breed;
    private int yearOfBirth;
    private String description;

    @Override
    public String toString() {
        return "Кличка собаки - " + getName() + ", год рождения - " + getYearOfBirth() +
                ", порода - " + getBreed();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Dog == false || obj == null)
            return false;
        Dog dog = (Dog)obj;
        if (getId() == (dog.getId()) && getName().equals(dog.getName()))
            return true;
        return false;
    }

    @Override
    public int hashCode()
    {
        return id.hashCode() + name.hashCode();
    }
}
