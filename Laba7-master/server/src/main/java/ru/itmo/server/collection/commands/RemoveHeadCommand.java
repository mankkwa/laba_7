package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.ArrayDequeDAO;
import ru.itmo.server.collection.dao.DAO;
import ru.itmo.server.collection.dao.PostgreSqlDao;

import java.util.ArrayList;

public class RemoveHeadCommand implements Command {
    private final PostgreSqlDao postgresqlDAO = new PostgreSqlDao();

    @Override
    public Response execute(Object arguments, User user) {
        ArrayList<Integer> indexes = postgresqlDAO.getAllSQL();
        int index = indexes.get(0);
        if (postgresqlDAO.delete(index, user)) {
            HumanBeing human = ArrayDequeDAO.getInstance().removeHead(user);
            if (human != null) return new Response(Response.Status.OK, "remove_head: " + human, new User("", ""));
        }
        return new Response(Response.Status.WARNING, "remove_head: Коллекция пуста либо элемент был добавлен другим пользователем", new User("", ""));
    }
}
