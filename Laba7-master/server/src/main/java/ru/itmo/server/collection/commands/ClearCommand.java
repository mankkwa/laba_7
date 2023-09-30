package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.ArrayDequeDAO;
import ru.itmo.server.collection.dao.PostgreSqlDao;

import java.util.ArrayList;

public class ClearCommand implements Command{
    private final PostgreSqlDao postgresqlDAO = new PostgreSqlDao();
    @Override
    public Response execute(Object arguments, User user) {
        ArrayList<Integer> indexes = postgresqlDAO.getAllSQL();
        if (indexes.size() != 0) {
            for(int i : indexes) {
                if(!postgresqlDAO.delete(i, user)) {
                    return new Response(Response.Status.ERROR,
                            "clear: В процессе очистки коллекции произошла ошибка", new User("", ""));
                }
            }
            ArrayDequeDAO.getInstance().clear(user);
            return new Response(Response.Status.OK, "clear: Коллекция успешно очищена", new User("", ""));
        } else {
            return new Response(Response.Status.WARNING, "clear: Коллекция пуста", new User("", ""));
        }
    }
}
