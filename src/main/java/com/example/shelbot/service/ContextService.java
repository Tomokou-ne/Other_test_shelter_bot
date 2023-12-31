package com.example.shelbot.service;

import com.example.shelbot.model.Context;
import com.example.shelbot.repository.ContextRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Сервис класса Context
 *
 * @author Riyaz Karimullin
 */
@Service
public class ContextService {
    private final ContextRepository contextRepository;

    public ContextService(ContextRepository contextRepository) {
        this.contextRepository = contextRepository;
    }
    public Context saveContext(Context context) {
        return contextRepository.save(context);
    }
    public Collection<Context> getAll() {
        return contextRepository.findAll();
    }

    public Optional<Context> getByChatId(Long chatId) {

        return contextRepository.findByChatId(chatId);
    }
}
