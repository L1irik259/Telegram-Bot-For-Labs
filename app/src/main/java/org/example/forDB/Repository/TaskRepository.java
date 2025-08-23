package org.example.forDB.Repository;

import java.sql.Date;
import java.util.List;

import org.example.model.Task;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskLanguage(String taskLanguage);

    List<Task> findByTaskStatus(String taskStatus);

    List<Task> findByUser(User user);
    
    List<Task> findByUserId(Long userId);

    List<Task> findByTaskDate(Date date);

    List<Task> findByTaskLanguageOrderByTaskDateAsc(String taskLanguage);

}
