package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.ArrayDequeDAO;
import ru.itmo.server.collection.dao.DAO;
import ru.itmo.server.collection.dao.PostgreSqlDao;

public class RemoveByIdCommand implements Command {
    private final DAO postgresqlDAO = new PostgreSqlDao();

    @Override
    public Response execute(Object arguments, User user) {
        HumanBeing humanBeing = (HumanBeing) arguments;
        int id = humanBeing.getId();

        if (postgresqlDAO.delete(id, user)) {
            if (ArrayDequeDAO.getInstance().delete(id, user)) {
                return new Response(Response.Status.OK, "remove_by_id: Элемент с id = " + id + " успешно удалён", new User("", ""));
            } else {
                return new Response(Response.Status.WARNING, "remove_by_id: Элемента с id = " + id + " не нашлось либо элемент был создан другим пользователем", new User("", ""));
            }
        } else {
            return new Response(Response.Status.ERROR, "remove_by_id: Элемента с id = " + id + " не нашлось", new User("", ""));
        }
    }
}
