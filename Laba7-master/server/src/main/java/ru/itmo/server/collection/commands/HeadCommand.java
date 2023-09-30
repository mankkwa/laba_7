package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.ArrayDequeDAO;

public class HeadCommand implements Command{
    @Override
    public Response execute(Object arguments, User user) {
        HumanBeing human = ArrayDequeDAO.getInstance().getHead();
        if(human != null) return new Response(Response.Status.OK, "head: "+human, new User("", ""));
        return new Response(Response.Status.WARNING, "head: Коллекция пуста", new User("", ""));
    }
}
