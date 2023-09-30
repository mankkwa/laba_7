package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.responses.Response;

public class ExitCommand implements Command{

    @Override
    public Response execute(Object arguments, User user) {
        return new Response(Response.Status.SERVER_EXIT, "exit: Завершение работы программы", new User("", ""));
    }
}
