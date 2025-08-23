package org.example.forDB.Repository;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserPromo(String userPromo);
    User findByChatId(Long chatId);
    User findByUserTelegramName(String userTelegramName);
}
