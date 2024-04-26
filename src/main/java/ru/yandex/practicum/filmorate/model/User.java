package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    Long id;
    private String name;
    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Некорректная почта")
    String email;
    @NotBlank(message = "Логин не должен быть пустой")
    String login;
    @NotNull
    @PastOrPresent(message = "Некорректная дата рождения")
    LocalDate birthday;
    private Set<Long> friendIds;

    public void addFriend(Long friendId) {
        if (friendIds == null) {
            friendIds = new HashSet<Long>();
        }
        friendIds.add(friendId);
    }

    Set<Long> friendsList = new HashSet<>();
}
