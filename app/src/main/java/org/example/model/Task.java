package org.example.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_link")
    private String taskLink;

    @Column(name = "task_language")
    private String taskLanguage;

    @Column(name = "task_description")
    private String taskDescription; // описание задачи

    @Column(name = "task_date")
    private Date taskDate;

    @Column(name = "task_status")
    private String taskStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "chat_id")
    private Long taskChatId;
    
    // Конструктор по умолчанию для Hibernate
    public Task() { }

    public Task(String taskLink, String taskLanguage, String taskDescription, Date taskDate, String taskStatus, User user, Long chatId) {
        this.taskLink = taskLink;
        this.taskLanguage = taskLanguage;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
        this.taskStatus = taskStatus;
        this.user = user;
        this.taskChatId = chatId;
    }


    public Task(String taskLink, String taskLanguage, String taskDescription, String taskStatus, User user, Long chatId) {
        this.taskLink = taskLink;
        this.taskLanguage = taskLanguage;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.user = user;
        this.taskChatId = chatId;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getTaskLink() {
        return taskLink;
    }
    
    public String getTaskLanguage() {
        return taskLanguage;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {   
        this.id = id;
    }

    public void setTaskLink(String taskLink) {
        this.taskLink = taskLink;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public void setTaskLanguage(String taskLanguage) {
        this.taskLanguage = taskLanguage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskLink='" + taskLink + '\'' +
                ", taskLanguage='" + taskLanguage + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", user=" + user +
                '}';
    }
}
