package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Film {
    private Long id;
    @NotBlank(message = "Отсутствует название фильма")
    String name;
    @NotNull
    @Size(max = 200, message = "Слишком длинное описание. Максимальное количество символов - 200")
    String description;
    @NotNull
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    Long duration;
    Set<Long> likesList = new HashSet<>();
}
