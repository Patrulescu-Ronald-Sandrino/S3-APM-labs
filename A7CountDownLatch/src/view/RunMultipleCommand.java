package view;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class RunMultipleCommand extends Command {
    private final Map<String, Command> commands;

    public RunMultipleCommand(String key, String description, Map<String, Command> commands) {
        super(key, description);
        this.commands = commands;
    }

    @Override
    public void execute() {
//        for (Command command: commands.values()) {
        for (Command command: commands.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getKey()))) // for sorting the commands by key, which is a String, numerically
                .map(Map.Entry::getValue)
                .collect(Collectors.toList())) {
            command.execute();
        }
    }
}
