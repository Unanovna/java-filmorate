//package ru.yandex.practicum.filmorate;
//
//
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.controller.FilmController;
//import ru.yandex.practicum.filmorate.exception.ValidateException;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class FilmControllerTest {
//
//    @Test
//    public void shouldThrowException() {
//        FilmController controller = new FilmController();
//        Film film1 = new Film(1, "film1", "ds f1",
//                LocalDate.MIN, 5);
//        final ValidateException exp1 = assertThrows(
//                ValidateException.class,
//                () -> controller.validate(film1)
//        );
//        Assertions.assertEquals("дата релиза — не раньше 28 декабря 1895", exp1.getMessage());
//        Film film2 = new Film(1, "film2",
//                RandomStringUtils.random(201),
//                LocalDate.now(), 5);
//        final ValidateException exp2 = assertThrows(
//                ValidateException.class,
//                () -> controller.validate(film2)
//        );
//        Assertions.assertEquals("максимальная длина описания — 200 символов", exp2.getMessage());
//    }
//    }
//
