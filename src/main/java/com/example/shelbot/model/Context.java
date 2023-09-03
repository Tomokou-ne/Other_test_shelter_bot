package com.example.shelbot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.shelbot.constant.ShelterType;


/**
 * Класс, необходимый для отслеживания состояния пользователя
 *
 * @author Riyaz Karimullin
 */
@Entity
@Data
@NoArgsConstructor
public class Context {
    @Id
    private Long chatId;
    @Column(name = "shelter")
    @Enumerated
    private ShelterType shelterType;
    @OneToOne
    private CatOwners catOwner;
    @OneToOne
    private DogOwners dogOwner;

    public Context(Long chatId, ShelterType shelterType) {
        this.chatId = chatId;
        this.shelterType = shelterType;
    }
}
