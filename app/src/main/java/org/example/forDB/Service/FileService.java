// package org.example.forDB.Service;

// import org.example.telegram.Bot;
// import org.springframework.beans.factory.annotation.Value;
// import org.telegram.telegrambots.meta.api.objects.Document;

// public class FileService {
//     @Value("${telegram.chat.token}")
//     private static Long chatIdOfChatForFiles;

//     public static void saveFileInChat(Document document) {
//         Bot.sendDocument(chatIdOfChatForFiles, document);
//     }
// }
