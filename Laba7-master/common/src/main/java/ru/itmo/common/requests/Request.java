package ru.itmo.common.requests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.itmo.common.User;
import ru.itmo.common.commands.CommandType;

public class Request {
    public final CommandType command;
    public final Object argument;
    private final User user;

    public Request(CommandType command, Object argument, User user) {
        this.command = command;
        this.argument = new Gson().toJson(argument);
        this.user = user;
    }

    public static Request fromJson(String json) {
        return new Gson().fromJson(json, Request.class);
    }

    public <T> T getArgumentAs(Class<T> clazz) {
        return new Gson().fromJson((String) argument, clazz);
    }

    public <T> Object getArgumentAs(TypeToken<T> typeToken) {
        return new Gson().fromJson((String) argument, typeToken.getType());
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public CommandType getCommand() {
        return command;
    }

    public User getUser() {
        return user;
    }
}
