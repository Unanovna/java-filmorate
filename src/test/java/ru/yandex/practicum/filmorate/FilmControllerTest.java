package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    @Test
    public void shouldThrowException() {
        FilmController controller = new FilmController();
        Film film1 = new Film("film1", "ds f1", "1500-6-5", 120L);
        final ValidateException exp1 = assertThrows(
                ValidateException.class,
                () -> controller.validate(film1)
        );
        Assertions.assertEquals("дата релиза — не раньше 28 декабря 1985", exp1.getMessage());
        Film film2 = new Film("film2", " Пока родители борются за выживание в меняющемся на глазах мире, дети" +
                " сбиваются в стаи и «бьются за асфальт»." +
                " Два 14-летних парня, Андрей и Марат, ищут защиты и поддержки среди насилия и нищеты и находят ее на улице.", "2000-6-5", 120L);
        final ValidateException exp2 = assertThrows(
                ValidateException.class,
                () -> controller.validate(film2)
        );
        Assertions.assertEquals("максимальная длина описания — 200 символов", exp2.getMessage());
    }
    }

