package ru.itmo.server.utility;

import ru.itmo.common.User;
import ru.itmo.common.commands.CommandType;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.requests.Request;
import ru.itmo.common.responses.Response;
import ru.itmo.server.ServerLauncher;
import ru.itmo.server.collection.commands.*;

public class HandleCommands {
    private static HandleUsers handleUsers;

    public HandleCommands(HandleUsers handleUsers){
        HandleCommands.handleUsers = handleUsers;
    }

    public Response handleRequest(Request request) {
        if(request.getUser() != null) {
            return executeCommand(request.getCommand(), request.getArgumentAs(HumanBeing.class), request.getUser());
        }
        else {
            return executeCommand(request.getCommand(), request.getArgumentAs(HumanBeing.class), request.getUser());
        }
    }

    private Response executeCommand(CommandType command, Object commandArgument, User user){
        int commandIndex = command.ordinal();
        Response response = commands[commandIndex].execute(commandArgument, user);
        ServerLauncher.log.info("Запрос успешно обработан");
        return response;
    }

    /**
     * existed commands
     */
    private static final Command[] commands = {
            new AddCommand(),
            new ClearCommand(),
            new ExitCommand(),
            new FilterByMinutesCommand(),
            new FilterGreaterThanSpeedCommand(),
            new HeadCommand(),
            new HelpCommand(),
            new InfoCommand(),
            new PrintUniqueSpeedCommand(),
            new Registration(),
            new RemoveByIdCommand(),
            new RemoveGreaterCommand(),
            new RemoveHeadCommand(),
            new ShowCommand(),
            new UpdateCommand(),
            new CheckUserCommand()
    };
}
