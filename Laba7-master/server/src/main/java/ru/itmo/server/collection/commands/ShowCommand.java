package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.ArrayDequeDAO;

public class ShowCommand implements Command{
    @Override
    public Response execute(Object arguments, User user) {
        String result = ArrayDequeDAO.getInstance().showCollection();
        if(result != null) return new Response(Response.Status.OK, "show: "+result, new User("", ""));
        return new Response(Response.Status.WARNING, "show: Коллекция пуста", new User("", ""));
    }
}
