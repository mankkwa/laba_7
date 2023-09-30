package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.ArrayDequeDAO;
import ru.itmo.server.collection.dao.PostgreSqlDao;

import java.util.ArrayList;

public class RemoveGreaterCommand implements Command{
    private final PostgreSqlDao postgresqlDAO = new PostgreSqlDao();

    @Override
    public Response execute(Object arguments, User user) {
        ArrayList<Integer> indexes = postgresqlDAO.getAllSQL();
        int index = indexes.get(indexes.size()-1);
        if (postgresqlDAO.delete(index, user)) {
            HumanBeing human = ArrayDequeDAO.getInstance().removeLast(user);
            if (human != null) return new Response(Response.Status.OK, "remove_greater: " + human, new User("", ""));
            else return new Response(Response.Status.WARNING, "remove_greater: Коллекция пуста или элемент был добавлен другим пользователем", new User("", ""));
        }
        return new Response(Response.Status.ERROR, "remove_greater: Произошла ошибка при удалении элемента", new User("", ""));
    }
}
