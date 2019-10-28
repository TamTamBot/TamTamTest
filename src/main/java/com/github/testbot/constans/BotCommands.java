package com.github.testbot.constans;

public enum BotCommands {

    HELP ("help", "help command"),
    SET_TOKEN ("set_token", "Set token for creating webhook on github"),
    SUBSCRIBE("subscribe", "Subscribe to github repository updates"),
    UNSUBSCRIBE("unsubscribe", "Unsubscribe from repository updates"),
    LIST("list", "List of your connected GitHub repositories"),
    NOT_FOUND ("", "");

    private String commandName;
    private String description;

    BotCommands(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return this.commandName;
    }




}
