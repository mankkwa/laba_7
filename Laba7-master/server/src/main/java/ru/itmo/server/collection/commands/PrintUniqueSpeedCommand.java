package ru.itmo.server.collection.commands;

import ru.itmo.common.User;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.responses.Response;
import ru.itmo.server.collection.dao.ArrayDequeDAO;

import java.util.ArrayList;
import java.util.List;

public class PrintUniqueSpeedCommand implements Command{
    @Override
    public Response execute(Object arguments, User user) {
        arguments = "";
        List<Integer> uniqueFieldsSpeed = new ArrayList<>();
        if (ArrayDequeDAO.getInstance().getAll().size() != 0) {
            for(HumanBeing human: ArrayDequeDAO.getInstance().getAll()) {
                Integer speed = human.getImpactSpeed();
                if(!uniqueFieldsSpeed.contains(speed)) {
                    uniqueFieldsSpeed.add(speed);
                } else {
                    uniqueFieldsSpeed.remove(speed);
                }
            }
            for(Integer element: uniqueFieldsSpeed) {
                arguments += element.toString() + "\n";
            }
            return new Response(Response.Status.OK, "print_unique_impact_speed: "+arguments, new User("", ""));
        } else {
            return new Response(Response.Status.WARNING, "print_unique_impact_speed: Коллекция пуста", new User("", ""));
        }
    }
}
