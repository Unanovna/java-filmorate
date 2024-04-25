package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
    private Set<Integer> friendIds;

    public void addFriend(Integer friendId) {
        if (friendIds == null) {
            friendIds = new HashSet<>();
        }
        friendIds.add(friendId);
    }
}
