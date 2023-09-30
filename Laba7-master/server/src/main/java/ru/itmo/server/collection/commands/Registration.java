package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.UserSQL;

public class Registration implements Command{

    UserSQL userManager = new UserSQL();
    @Override
    public Response execute(Object arguments, User user) {
        Boolean res = userManager.add(user);
        if(res == null) {
            return new Response(Response.Status.ALREADY_EXIST, "Такой логин уже существует", new User("", ""));
        } else if(res) {
            return new Response(Response.Status.OK, "Новый пользователь успешно добавлен. Вы можете войти.", new User(null, ""));
        }
        else {
            return new Response(Response.Status.ERROR, "Не удалось добавить нового пользователя.", new User("", ""));
        }
    }
}
