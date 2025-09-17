package org.example.telegram;

import org.example.forDB.Repository.TaskRepository;
import org.example.forDB.Repository.UserRepository;
import org.example.forDB.Service.TaskService;
import org.example.forDB.Service.UserService;
import org.example.forDB.Service.YandexService;
import org.example.model.Task;
import org.example.model.User;
import org.example.telegram.steps.UserStepForOrder;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PostConstruct;

// @SuppressWarnings("deprecation")
@Component
public class Bot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String botUsername;
    
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.token}")
    private Long chatIdOfChatForFiles;
   
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository orderTaskRepository;

    @Autowired
    private TaskService taskService;

    private final Map<Long, UserStepForOrder> mapForOrder = new HashMap<>();

    public Bot(@Value("${telegram.bot.username}") String botUsername, @Value("${telegram.bot.token}") String botToken) {
        super(botToken);
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("Бот запущен успешно");
        } catch (TelegramApiException e) {
            System.err.println("Ошибка при запуске бота: " + e.getMessage());
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println("Error when sending message: " + e.getMessage());
        }
    }

    public void sendDocument(Long chatId, Document doc) {
        try {
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId.toString());
            
            // Берём fileId из документа
            sendDocument.setDocument(new InputFile(doc.getFileId()));
            
            // Можно добавить подпись
            String caption = doc.getFileName() != null ? doc.getFileName() : "без имени";
            sendDocument.setCaption("#" + caption);
    
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        Message messageText = update.getMessage();

        try {
            if (messageText.getText() != null) {
                switch (messageText.getText()) {
                case "/start":
                        User user = new User(update.getMessage().getFrom().getUserName(), chatId);
                        userService.saveUser(user);
                        sendMessage(chatId, "Добро пожаловать!\n\nМы группа страшекурсиников и теперь у нас есть сервис, который не первый год выполняет лабораторные работы для студентов. Качественно, быстро и недорого.\n\n➡️ Чтобы сделать заказ — нажмите /order\n➡️ Чтобы получить скидку 500₽ — нажмите /promo\n\nМы работаем, чтобы вам не пришлось искать исполнителей. Просто доверьте задачу нам.");
                        break;
                    case "/order":
                        mapForOrder.put(chatId, new UserStepForOrder());
                        sendMessage(chatId, "Выберите язык программирования:");
                        break;
                    default:
                        handleUserInput(chatId, messageText);
                }
            } else if (messageText.getDocument() != null) {
                handleUserInput(chatId, messageText);
            }
        } catch (Exception e) {
            System.err.println("Error when sending message: " + e.getMessage());
        }
    }

    private void handleUserInput(Long chatId, Message messageText) throws Exception {
        UserStepForOrder state = mapForOrder.get(chatId);
        if (state == null) {
            sendMessage(chatId, "Пожалуйста, начните с команды /order");
            return;
        }

        String reply = processState(chatId, state, messageText);
        sendMessage(chatId, reply);
    }

    private String processState(Long chatId, UserStepForOrder state, Message messageText) throws Exception {
        switch (state.getCurrentStep()) {
            case LANGUAGE:
                state.saveAnswer("language", messageText.getText());
                state.nextStep();
                return "Укажи количество дней на выполнение лабораторной работы";
            case DATE:
                state.saveAnswer("date", messageText.getText());
                state.nextStep();
                return "Укажи технические характеристики лабораторной работы (ТЗ)";
            case TECHNICALSPECIFICATION:
                Document doc = messageText.getDocument();

                sendDocument(chatIdOfChatForFiles, doc);

                // String token = "2201d25d3d80466c94ae36844d833e33";
                // YandexService newYandexService = new YandexService(token);
                //
                // String a = "https://drive.google.com/file/d/1SFyV6ARL_WcqSW4135peN8i0Rt6Nh1O2/edit";
                // String b = "For-Labs-Of-Money/PXL_20250905_124158848";
                //
                // newYandexService.uploadFromUrl(a, b);

                state.saveAnswer("technicalSpecification", doc.getFileName());
                state.nextStep();
                return "Укажи описание задачи";
            case TASKDESCRIPTION:
                state.saveAnswer("taskDescription", messageText.getText());
                state.nextStep();
                saveOrder(chatId, state);
                return "Спасибо за ваш заказ! Мы свяжемся с вами в ближайшее время.";
            default:
                return "Ошибка: неизвестный шаг";
        }
    }

    private void saveOrder(Long chatId, UserStepForOrder state) {
        // Получаем ответы пользователя
        String language = state.getAnswers().get("language");
        Integer countDaysOff = Integer.parseInt(state.getAnswers().get("date"));
        String technicalSpecification = state.getAnswers().get("technicalSpecification");
        String taskDescription = state.getAnswers().get("taskDescription");

        Date taskDate = Date.valueOf(LocalDate.now().plusDays(countDaysOff));
        User nowUser = userService.findByChatId(chatId);
        // Создаем новую задачу
        Task orderTask = new Task(technicalSpecification, language, taskDescription, taskDate, "Ищет исполнителя", nowUser, chatId);

        // Сохраняем задачу через JPA репозиторий
        taskService.saveTask(orderTask);
    }


}
