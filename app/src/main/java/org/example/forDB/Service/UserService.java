package org.example.forDB.Service;

import java.util.List;

import org.example.forDB.Repository.UserRepository;
import org.example.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        if (userRepository.findByUserTelegramName(user.getUserTelegramName()) != null) {
            return user;
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public int findCountTasksByUserId(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user.getUserCountTasks();
        }
        return 0;
    }

    public void updateUserCountTasks(Long id, int countTasks) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUserCountTasks(countTasks);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void updateToOneUserCountTasks(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUserCountTasks(user.getUserCountTasks() + 1);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void updateUserCounterInvite(String userPromo, int countInvite) {
        User user = userRepository.findByUserPromo(userPromo);
        if (user != null) {
            user.setUserCounterInvite(user.getUserCounterInvite() + countInvite);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }


}
