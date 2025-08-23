package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data // генерит геттеры, сеттеры, toString, equals, hashCode
@AllArgsConstructor // конструктор со всеми аргами
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_name")
    private String userTelegramName;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "user_count_tasks")
    private int userCountTasks;

    @Column(name = "user_promo")
    private String userPromo;

    @Column(name = "user_counter_invite")
    private int userCounterInvite;

    @Column(name = "user_using_promo")
    private String userUsingPromo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;

    // Конструктор по умолчанию для Hibernate
    public User() {
        this.userCountTasks = 0;
        this.userPromo = UUID.randomUUID().toString().substring(0, 5);
        this.userCounterInvite = 0;
        this.userUsingPromo = null;
        this.chatId = null;
        this.userTelegramName = null;
    }

    // кастомные конструкторы, если тебе прям нужны:
    public User(String userTelegramName, String userUsingPromo) {
        this.userTelegramName = userTelegramName;
        this.userCountTasks = 0;
        this.userPromo = UUID.randomUUID().toString().substring(0, 5);
        this.userCounterInvite = 0;
        this.userUsingPromo = userUsingPromo;
        this.chatId = null;
    }

    public User(String userTelegramName, Long chatId) {
        this.userTelegramName = userTelegramName;
        this.userCountTasks = 0;
        this.userPromo = UUID.randomUUID().toString().substring(0, 5);
        this.userCounterInvite = 0;
        this.userUsingPromo = null;
        this.chatId = chatId;
    }

    public User(String userName, int userCountTasks) {
        this.userCountTasks = userCountTasks;
        this.userPromo = UUID.randomUUID().toString().substring(0, 5);
        this.userCounterInvite = 0;
        this.userUsingPromo = null;
        this.chatId = null;
    }
}
