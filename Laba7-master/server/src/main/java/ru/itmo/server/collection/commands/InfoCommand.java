package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.ArrayDequeDAO;

public class InfoCommand implements Command{

    @Override
    public Response execute(Object arguments, User user) {
        int size = ArrayDequeDAO.getInstance().getAll().size();
        return new Response(Response.Status.OK, "info: Таблица HUMAN_BEING_COLLECTION,\n"
                + "Количество элементов: " + size + "\n", new User("", ""));
    }
}
