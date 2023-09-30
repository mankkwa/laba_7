package ru.itmo.common.exceptions;

public enum TypeOfError {
    OUT_OF_RANGE("Заданное значение находится вне допустимого диапазона значений"), // находится вне допустимого диапазона
    WRONG_TYPE("Тип введённых данных не соответствует ожидаемому"), // некорректный тип данных
    UNKNOWN("Введённую строку невозможно распознать"),
    EMPTY("Введена пустая строка"), // нераспознаваемая строка данных
    NOT_FOUND("Файл на найден"),
    SWITCH_READER("Произведено переключение на считывание с файла"),
    IGNORE_COMMAND("Команда будет проигнорирована"),
    IGNORE_STRING("Введённая строка будет проигнорирована"),
    ALREADY_EXECUTED("Файл уже был вызван ранее и ещё не завершил свою работу"),
    NOT_STARTED("Сервер ещё не запущен"),
    CONNECTED_REFUSE("Соединение разорвано"),
    CONNECTION_BROKEN("Попытка переподключения к серверу...");

    private final String description;

    TypeOfError(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
