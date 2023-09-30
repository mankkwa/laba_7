package ru.itmo.client.to_server;

import ru.itmo.common.User;
import ru.itmo.common.commands.CommandType;
import ru.itmo.common.exceptions.WrongArgumentException;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.responses.Response;


public interface ServerAPI {
    Response executeCommand(CommandType command, HumanBeing human, User user) throws WrongArgumentException;
    boolean connectToServer();
    int getAttempts();
    void closeConnection();
}
