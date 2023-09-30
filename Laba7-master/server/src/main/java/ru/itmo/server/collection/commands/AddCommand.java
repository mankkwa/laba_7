package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.ArrayDequeDAO;
import ru.itmo.server.collection.dao.DAO;
import ru.itmo.server.collection.dao.PostgreSqlDao;

public class AddCommand implements Command{
    private final DAO postgresqlDAO = new PostgreSqlDao();

    @Override
    public Response execute(Object arguments, User user) {
        HumanBeing humanBeing = (HumanBeing) arguments;
        int id = postgresqlDAO.add(humanBeing, user);

        if(id != -1) {
            humanBeing.setId(id);
            humanBeing.setUserLogin(user.getUsername());
            return new Response(Response.Status.OK, "add: Элемент успешно добавлен в коллекцию, его id = "
                    + ArrayDequeDAO.getInstance().add(humanBeing, user), new User("", ""));
        }
        return new Response(Response.Status.ERROR,
                "add: Возникли проблемы с добавлением элемента. Пожалуйста, проверьте, подключена ли база данных.", new User("", ""));
    }

}
