package view;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private final Map<String, Command> commands;

    public TextMenu() {
        commands = new HashMap<>();
    }

    public void addCommand(Command command) {
        commands.put(command.getKey(), command);
    }

    private String menuToString() {
        return commands.values().stream()
                .sorted(Comparator.comparingInt((Command c) -> Integer.parseInt(c.getKey())))
                .map(command -> String.format("%4s : %s\n", command.getKey(), command.getDescription()))
                .reduce("", (result, commandAsString) -> result + commandAsString);
//        for (Command command : commands.values()) {
//            String line = String.format("%4s : %s", command.getKey(), command.getDescription());
//            System.out.println(line);
//        }
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) { // <-- TODO why --'while' statement cannot complete without throwing an exception-- ?
            System.out.print(menuToString() + "Input the option: ");
            String key = scanner.nextLine();
            Command command = commands.get(key);
            if (command == null) {
                System.out.println("Invalid Option");
                continue;
            }
            System.out.println();
            command.execute();
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
