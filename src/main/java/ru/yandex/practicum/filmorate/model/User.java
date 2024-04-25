package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    Long id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
    private Set<Long> friendIds;

    public void addFriend(Long friendId) {
        if (friendIds == null) {
            friendIds = new HashSet<Long>();
        }
        friendIds.add(friendId);
    }

    Set<Long> friendsList = new HashSet<>();
}
