package com.example.shelbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
/**
 * Класс Владельцев собак
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dog_owners")
public class DogOwners {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //name пользователя
    @Column(name = "name_dog_owner")
    private String name;
    //yearOfBirth год рождения пользователя
    @Column(name = "yearOfBirth")
    private int yearOfBirth;
    //phone телефон пользователя
    @Column(name = "phone")
    private String phone;
    //mail електроная почта пользователя
    @Column(name = "mail")
    private String mail;
    //address пользователя
    @Column(name = "address")
    private String address;

    //chat id пользователя
    @Column(name = "chat_id")
    private Long chatId;

    //связь OneToOne с собакой
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", referencedColumnName = "id")
    private Dog dog;

    public DogOwners(Long id, String name, Long chatId) {
        this.id = id;
        this.name = name;
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogOwners dogOwner = (DogOwners) o;
        return Objects.equals(id, dogOwner.id) && Objects.equals(name, dogOwner.name) && Objects.equals(chatId, dogOwner.chatId) && Objects.equals(dog, dogOwner.dog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, chatId, dog);
    }

    @Override
    public String toString() {
        return "DogOwner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chatId='" + chatId + '\'' +
                ", dog=" + dog +
                '}';
    }
}
