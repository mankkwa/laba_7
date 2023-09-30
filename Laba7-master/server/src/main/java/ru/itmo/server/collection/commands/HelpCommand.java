package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.responses.Response;

public class HelpCommand implements Command{
    @Override
    public Response execute(Object arguments, User user) {
        return new Response(Response.Status.OK, "help: Доступные команды: \n" +
                "help                     - вывести справку по доступным командам \n" +
                "info                     - вывести в стандартный поток вывода информацию о коллекции (тип, количество элементов и т.д.) \n" +
                "show                     - вывести в стандартный поток вывода все элементы коллекции в строковом представлении  \n" +
                "add {element}            - добавить новый элемент в коллекцию \n" +
                "update id {element}      - обновить значение элемента коллекции, id которого равен заданному \n" +
                "remove_by_id id          - удалить элемент из коллекции по его id \n" +
                "clear                    - очистить коллекцию \n" +
                "exit                     - завершить программу (с сохранением) \n" +
                "head                     - вывести первый элемент коллекции \n" +
                "remove_head              - вывести первый элемент коллекции и удалить его \n" +
                "remove_greater           - вывести последний элемент коллекции и удалить его \n" +
                "filter_by_minutes_of_waiting minutesOfWaiting - вывести элементы, значение поля minutesOfWaiting которых равно заданному \n" +
                "filter_greater_than_impact_speed impactSpeed  - вывести элементы, значение поля impactSpeed которых больше заданного \n" +
                "print_unique_impact_speed - вывести уникальные значения поля impactSpeed всех элементов в коллекции \n",new User("", "") );
    }
}
