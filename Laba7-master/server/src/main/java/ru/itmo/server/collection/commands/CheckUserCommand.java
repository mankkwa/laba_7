package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.UserSQL;

public class CheckUserCommand implements Command{

    UserSQL userManager = new UserSQL();

    @Override
    public Response execute(Object arguments, User user) {

        Boolean res = userManager.search(user);
        if(res == null) {
            return new Response(Response.Status.ERROR, "Произошла ошибка :(", new User(null, null));
        } else if(res) {
            return new Response(Response.Status.OK, "Авторизация прошла успешно", user);
        }
        else {
            return new Response(Response.Status.WARNING, "Неверный логин, либо пароль", new User(null, null));
        }
    }
}
