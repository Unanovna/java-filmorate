package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private Long id;

    private String name;

    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Некорректная почта")
    private String email;

    @NotBlank(message = "Логин не должен быть пустой")
    private String login;

    @NotNull
    @PastOrPresent(message = "Некорректная дата рождения")
    private LocalDate birthday;

    private Set<Long> friendIds;

    public void addFriend(Long friendId) {
        if (friendIds == null) {
            friendIds = new HashSet<>();
        }
        friendIds.add(friendId);
    }

    public void removeFriend(Long friendId) {
        if (friendId != null) {
            friendIds.remove(friendId);
        }
    }
}
