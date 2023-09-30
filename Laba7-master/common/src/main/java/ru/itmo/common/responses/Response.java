package ru.itmo.common.responses;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.itmo.common.User;

public class Response {
    public final Status status;
    public final Object argument;
    public final User user;

    public Response(Status status, Object argument, User user) {
        this.status = status;
        this.argument = new Gson().toJson(argument);
        this.user = user;
    }


    public static Response fromJson(String json)  {
        return new Gson().fromJson(json, Response.class);
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

    public User getUser() {
        return user;
    }

    public enum Status {
        OK,
        ERROR,
        WARNING,
        WRONG_PASSWORD,
        SERVER_EXIT,
        ALREADY_EXIST
    }
}
