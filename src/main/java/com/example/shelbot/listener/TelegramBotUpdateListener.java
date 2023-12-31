package com.example.shelbot.listener;

import com.example.shelbot.model.CatOwners;
import com.example.shelbot.model.DogOwners;
import com.example.shelbot.service.DogOwnersService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.example.shelbot.constant.ShelterType;
import com.example.shelbot.keyboard.KeyBoard;
import com.example.shelbot.model.Context;
import com.example.shelbot.constant.ButtonCommand;
import com.example.shelbot.model.ReportData;
import com.example.shelbot.service.CatOwnersService;
import com.example.shelbot.service.ContextService;
import com.example.shelbot.service.ReportDataService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Класс обработки сообщений
 *
 * @author Riyaz Karimullin
 */
@Component
public class TelegramBotUpdateListener implements UpdatesListener {
    private static final String REGEX_MESSAGE = "(Рацион:)(\\s)(\\W+)(;)\n" +
            "(Самочувствие:)(\\s)(\\W+)(;)\n" +
            "(Поведение:)(\\s)(\\W+)(;)";
    private final Pattern pattern = Pattern.compile(REGEX_MESSAGE);

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);
    private final TelegramBot telegramBot;
    private final KeyBoard keyBoard;
    private final ContextService contextService;
    private final CatOwnersService catOwnersService;
    private final DogOwnersService dogOwnerService;
    private final ReportDataService reportDataService;
    @Value("${volunteer-chat-id}")
    private Long volunteerChatId;

    public TelegramBotUpdateListener(TelegramBot telegramBot, KeyBoard keyBoard, ContextService contextService,
                                     CatOwnersService catOwnersService, DogOwnersService dogOwnerService,
                                     ReportDataService reportDataService) {
        this.telegramBot = telegramBot;
        this.keyBoard = keyBoard;
        this.contextService = contextService;
        this.catOwnersService = catOwnersService;
        this.dogOwnerService = dogOwnerService;
        this.reportDataService = reportDataService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод предназначенный для switch-case,
     * который принимает текст сообщения пользователя и сравнивает со значениями enum класса ButtonCommand
     *
     * @param buttonCommand
     * @return
     */
    public static ButtonCommand parse(String buttonCommand) {
        ButtonCommand[] values = ButtonCommand.values();
        for (ButtonCommand command : values) {
            if (command.getCommand().equals(buttonCommand)) {
                return command;
            }
        }
        return ButtonCommand.NOTHING;
    }

    /**
     * Метод, позволяющий отслеживать и организовывать весь процесс общения с пользователем.
     *
     * @param updates
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);
                Message message = update.message();
                long chatId = message.chat().id();
                String text = message.text();
                int messageId = message.messageId();
                Contact contact = update.message().contact();

                if (text != null && update.message().photo() == null && contact == null) {
                    switch (parse(text)) {
                        case START -> {
                            if (contextService.getByChatId(chatId).isEmpty()) {
                                sendResponseMessage(chatId, "Привет! Я могу показать информацию о приютах," +
                                        "как взять животное из приюта и принять отчет о питомце");
                                Context context = new Context();
                                context.setChatId(chatId);
                                contextService.saveContext(context);
                            }
                            keyBoard.chooseMenu(chatId);
                        }
                        case CAT -> {
                            if (contextService.getByChatId(chatId).isPresent()) {
                                Context context = contextService.getByChatId(chatId).get();
                                if (context.getCatOwner() == null) {
                                    CatOwners catOwner = new CatOwners();
                                    catOwner.setChatId(chatId);
                                    catOwnersService.create(catOwner);
                                    context.setCatOwner(catOwner);
                                }
                                context.setShelterType(ShelterType.CAT);
                                contextService.saveContext(context);
                                sendResponseMessage(chatId, "Вы выбрали кошачий приют.");
                                keyBoard.shelterMainMenu(chatId);
                            }

                        }
                        case DOG -> {
                            if (contextService.getByChatId(chatId).isPresent()) {
                                Context context = contextService.getByChatId(chatId).get();
                                if (context.getDogOwner() == null) {
                                    DogOwners dogOwner = new DogOwners();
                                    dogOwner.setChatId(chatId);
                                    dogOwnerService.create(dogOwner);
                                    context.setDogOwner(dogOwner);
                                }
                                context.setShelterType(ShelterType.DOG);
                                contextService.saveContext(context);
                                sendResponseMessage(chatId, "Вы выбрали собачий приют.");
                                keyBoard.shelterMainMenu(chatId);
                            }
                        }
                        case MAIN_MENU -> {
                            keyBoard.shelterMainMenu(chatId);
                        }
                        case SHELTER_INFO_MENU -> {
                            keyBoard.shelterInfoMenu(chatId);
                        }
                        case HOW_ADOPT_PET_INFO -> {
                            keyBoard.shelterInfoHowAdoptPetMenu(chatId);
                        }
                        case SHELTER_INFO -> {
                            if (contextService.getByChatId(chatId).isPresent()) {
                                Context context = contextService.getByChatId(chatId).get();
                                if (context.getShelterType().equals(ShelterType.CAT)) {
                                    sendResponseMessage(chatId, """
                                            Информация о кошачем приюте - ...
                                            Рекомендации о технике безопасности на территории кошачего приюта - ...
                                            Контактные данные охраны - ...
                                            """);
                                } else if (context.getShelterType().equals(ShelterType.DOG)) {
                                    sendResponseMessage(chatId, """
                                            Информация о собачем приюте - ...
                                            Рекомендации о технике безопасности на территории собачего приюта - ...
                                            Контактные данные охраны - ...
                                            """);
                                }
                            }
                        }
                        case SHELTER_ADDRESS_SCHEDULE -> {
                            if (contextService.getByChatId(chatId).isPresent()) {
                                Context context = contextService.getByChatId(chatId).get();
                                if (context.getShelterType().equals(ShelterType.CAT)) {
                                    sendResponseMessage(chatId, """
                                            Адрес кошачего приюта - ...
                                            График работы - ...
                                            """);
                                } else if (context.getShelterType().equals(ShelterType.DOG)) {
                                    sendResponseMessage(chatId, """
                                            Адрес собачего приюта - ...
                                            График работы - ...
                                            """);
                                }
                            }
                        }
                        case RECOMMENDATIONS_LIST -> {
                            if (contextService.getByChatId(chatId).isPresent()) {
                                Context context = contextService.getByChatId(chatId).get();
                                if (context.getShelterType().equals(ShelterType.CAT)) {
                                    sendResponseMessage(chatId, """
                                            Правила знакомства с животным - ...
                                            Список рекомендаций - ...
                                            Список причин отказа в выдаче животного - ...
                                            """);
                                } else if (context.getShelterType().equals(ShelterType.DOG)) {
                                    sendResponseMessage(chatId, """
                                            Правила знакомства с животным - ...
                                            Список рекомендаций - ...
                                            Советы кинолога по первичному общению с собакой - ...
                                            Рекомендации по проверенным кинологам для дальнейшего обращения к ним
                                            Список причин отказа в выдаче животного - ...
                                            """);
                                }
                            }
                        }
                        case DOCUMENTS_LIST -> {
                            if (contextService.getByChatId(chatId).isPresent()) {
                                Context context = contextService.getByChatId(chatId).get();
                                if (context.getShelterType().equals(ShelterType.CAT)) {
                                    sendResponseMessage(chatId,
                                            "Для взятия кота из приюта необходимы такие документы: ...");
                                } else if (context.getShelterType().equals(ShelterType.DOG)) {
                                    sendResponseMessage(chatId,
                                            "Для взятия собаки из приюта необходимы такие документы: ...");
                                }
                            }
                        }
                        case VOLUNTEER -> {
                            sendResponseMessage(chatId, "Мы передали ваше сообщение волонтеру. " +
                                    "Если у вас закрытый профиль отправьте контактные данные," +
                                    "с помощью кнопки в меню - Отправить контактные данные");
                            sendForwardMessage(chatId, messageId);
                        }
                        case SEND_REPORT -> {
                            sendResponseMessage(chatId, """
                                    Для отчета необходима фотография, рацион,
                                    самочувствие и изменение в поведении питомца.
                                    Загрузите фото, а в подписи к нему, скопируйте и заполните текст ниже.
                                                                        
                                    Рацион: ваш текст;
                                    Самочувствие: ваш текст;
                                    Поведение: ваш текст;
                                    """);
                        }
                        default -> sendResponseMessage(chatId, "Неизвестная команда!");
                    }
                } else if (update.message().contact() != null && contextService
                        .getByChatId(chatId).isPresent()) {
                    Context context = contextService.getByChatId(chatId).get();
                    if (context.getShelterType().equals(
                            ShelterType.CAT) && update.message() != null && contact != null) {
                        CatOwners catOwner = context.getCatOwner();
                        catOwner.setPhone(contact.phoneNumber());
                        catOwner.setName(contact.firstName());
                        catOwnersService.update(catOwner);
                    } else if (context.getShelterType().equals(
                            ShelterType.DOG) && update.message() != null && contact != null) {
                        DogOwners dogOwner = context.getDogOwner();
                        dogOwner.setPhone(contact.phoneNumber());
                        dogOwner.setName(contact.firstName());
                        dogOwnerService.update(dogOwner);
                    }
                    sendForwardMessage(chatId, messageId);
                    sendResponseMessage(chatId, "Мы получили ваши контактные данные");

                } else if (update.message().photo() != null && update.message().caption() != null) {
                    Calendar calendar = new GregorianCalendar();
                    long compareTime = calendar.get(Calendar.DAY_OF_MONTH);
                    long daysOfReports = reportDataService.getAll().stream()
                            .filter(s -> s.getChatId() == chatId)
                            .count();
                    Date lastMessageDate = reportDataService.getAll().stream()
                            .filter(s -> s.getChatId() == chatId)
                            .map(ReportData::getLastMessage)
                            .max(Date::compareTo)
                            .orElse(null);
                    long numberOfDay = 0L;
                    if (lastMessageDate != null) {
                        numberOfDay = lastMessageDate.getDate();
                    } else {
                        numberOfDay = message.date();
                    }
                    if (daysOfReports < 30) {
                        if (compareTime != numberOfDay) {
                            Context context = contextService.getByChatId(chatId).get();
                            if (context.getShelterType().equals(ShelterType.CAT)
                                    && context.getCatOwner().getCat() != null) {
                                String petName = context.getCatOwner().getCat().getName();
                                getReport(message, petName);
                                daysOfReports++;
                            } else if (context.getShelterType().equals(ShelterType.DOG)
                                    && context.getDogOwner().getDog() != null) {
                                String petName = context.getDogOwner().getDog().getName();
                                getReport(message, petName);
                                daysOfReports++;
                            } else {
                                sendResponseMessage(chatId, "У вас нет животного!");
                            }
                        } else {
                            sendResponseMessage(chatId, "Вы уже отправляли сегодня отчет");
                        }

                    }
                    if (daysOfReports == 30) {
                        sendResponseMessage(chatId, "Вы прошли испытательный срок!");
                        sendResponseMessage(volunteerChatId, "Владелец животного с chatId " + chatId
                                + " прошел испытательный срок!");
                    }

                } else if (update.message().photo() != null && update.message().caption() == null) {
                    sendResponseMessage(chatId, "Отчет нужно присылать с описанием!");
                }

            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод отправки текстовых сообщений.
     *
     * @param chatId
     * @param text
     */
    public void sendResponseMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    /**
     * Метод пересылки сообщения волонтеру
     *
     * @param chatId
     * @param messageId
     */
    public void sendForwardMessage(long chatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage(volunteerChatId, chatId, messageId);
        SendResponse sendResponse = telegramBot.execute(forwardMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    /**
     * Метод получения отчета и отправки его волонтеру
     *
     * @param message
     */
    public void getReport(Message message, String petName) {
        PhotoSize photo = message.photo()[0];
        String caption = message.caption();
        Long chatId = message.chat().id();

        List<String> captionMatcher = splitCaption(caption);

        String ration = captionMatcher.get(0);
        String health = captionMatcher.get(1);
        String behaviour = captionMatcher.get(2);

        GetFile getFile = new GetFile(photo.fileId());
        GetFileResponse getFileResponse = telegramBot.execute(getFile);

        try {
            File file = getFileResponse.file();
            byte[] fileContent = telegramBot.getFileContent(file);

            long date = message.date();
            Date lastMessage = new Date(date * 1000);
            reportDataService.uploadReportData(
                    chatId, petName, fileContent, ration,
                    health, behaviour, lastMessage);
            sendForwardMessage(chatId, message.messageId());
            sendResponseMessage(chatId, "Ваш отчет принят!");
        } catch (IOException e) {
            System.out.println("Ошибка загрузки фото!");
        }

    }

    /**
     * Метод для разбивки описания под фотографии для добавления полученного текста в отчет
     *
     * @param caption
     * @return
     */
    private List<String> splitCaption(String caption) {
        if (caption == null || caption.isBlank()) {
            throw new IllegalArgumentException("Описание под фотографией не должно быть пустым. Отправьте отчёт заново!");
        }
        Matcher matcher = pattern.matcher(caption);
        if (matcher.find()) {
            return new ArrayList<>(List.of(matcher.group(3), matcher.group(7), matcher.group(11)));
        } else {
            throw new IllegalArgumentException("Проверьте правильность введённых данных и отправьте отчёт ещё раз.");
        }
    }

    /**
     * Метод отслеживания своеврменной отправки отчетов
     */
    @Scheduled(cron = "@daily")
    public void sendWarning() {
        for (Context context : contextService.getAll()) {
            long chatId = context.getChatId();
            long daysOfReports = reportDataService.getAll().stream()
                    .filter(s -> Objects.equals(s.getChatId(), chatId))
                    .count();
            if (daysOfReports < 30 && daysOfReports != 0) {
                long twoDay = 172800000;
                Date nowTime = new Date(new Date().getTime() - twoDay);
                Date lastMessageDate = reportDataService.getAll().stream()
                        .filter(s -> Objects.equals(s.getChatId(), chatId))
                        .map(ReportData::getLastMessage)
                        .max(Date::compareTo)
                        .orElse(null);
                if (lastMessageDate != null) {
                    if (lastMessageDate.before(nowTime)) {
                        sendResponseMessage(chatId, "Вы не отправляли отчёты уже более двух дней. " +
                                "Пожалуйста, отправьте отчёт или выйдите на связь с волонтёрами.");
                        sendResponseMessage(volunteerChatId, "Владелец животного с chatId " + chatId
                                + " не отправлял отчёты уже более двух дней!");
                    }
                }
            }
        }

    }
}
