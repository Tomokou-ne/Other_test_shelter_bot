package com.example.shelbot.service;

import com.example.shelbot.exceptions.ReportDataNotFoundException;
import com.example.shelbot.model.ReportData;
import com.example.shelbot.repository.ReportDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Класс сервис отчетов
 * @author Королёв Артем
 **/
@Service
@Transactional
public class ReportDataService {
    private final ReportDataRepository repository;
    public ReportDataService(ReportDataRepository reportRepository) {
        this.repository = reportRepository;
    }
    public void uploadReportData(Long chatId,String name, byte[] pictureFile,
                                 String ration, String health, String behaviour,
                                 Date lastMessage) throws IOException {
        ReportData report = new ReportData();
        report.setChatId(chatId);
        report.setName(name);
        report.setData(pictureFile);
        report.setRation(ration);
        report.setHealth(health);
        report.setBehaviour(behaviour);
        report.setLastMessage(lastMessage);
        this.repository.save(report);
    }
    public ReportData findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(()->new ReportDataNotFoundException("Data not found exceptions"));
    }
    public ReportData findByChatId(Long chatId) {
        return this.repository.findByChatId(chatId);
    }
    public Collection<ReportData> findListByChatId(Long chatId) {
        return this.repository.findListByChatId(chatId);
    }
    public ReportData save(ReportData report) {
        return this.repository.save(report);
    }
    public void remove(Long id) {
        this.repository.deleteById(id);
    }
    public List<ReportData> getAll() {
        return this.repository.findAll();
    }

}
