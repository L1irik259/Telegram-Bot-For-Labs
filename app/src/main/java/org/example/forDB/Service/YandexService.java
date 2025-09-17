package org.example.forDB.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class YandexService {
    private final String oauthToken;

    public YandexService(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    /**
     * Скачивает файл по URL сразу на Яндекс.Диск
     *
     * @param fileUrl   — ссылка на скачиваемый файл (например https://example.com/file.pdf)
     * @param remotePath — путь на Яндекс.Диске (например "For-Labs-Of-Money/file.pdf")
     */
    public void uploadFromUrl(String fileUrl, String remotePath) throws IOException {
        if (oauthToken == null || oauthToken.isEmpty()) {
            throw new IllegalStateException("OAuth token is not set.");
        }

        // Кодируем параметры
        String encodedUrl = URLEncoder.encode(fileUrl, StandardCharsets.UTF_8);
        String encodedPath = URLEncoder.encode("disk:/" + remotePath, StandardCharsets.UTF_8);

        // Формируем URL запроса
        String requestUrl = String.format(
                "https://cloud-api.yandex.net/v1/disk/resources/upload?url=%s&path=%s&overwrite=true",
                encodedUrl, encodedPath
        );

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "OAuth " + oauthToken);
        connection.setDoOutput(true);

        // нужно отправить хоть пустое тело, иначе POST иногда глючит
        try (OutputStream os = connection.getOutputStream()) {
            os.write(new byte[0]);
        }

        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        responseCode >= 200 && responseCode < 300
                                ? connection.getInputStream()
                                : connection.getErrorStream(),
                        StandardCharsets.UTF_8
                )
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        if (responseCode == 202) {
            System.out.println("Файл принят на загрузку. Ответ API: " + response);
        } else {
            throw new IOException("Ошибка при загрузке файла: HTTP " + responseCode + " - " + response);
        }
    }
}
