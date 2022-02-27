package view;

import interpreter.Interpreter;
import model.statement.IStatement;

public class RunExampleCommand extends Command {
    private final Interpreter interpreter;

    public RunExampleCommand(String key, String description, Interpreter interpreter) {
        super(key, description);
        this.interpreter = interpreter;
    }

    public RunExampleCommand(String key, IStatement statement, String logFilePath, String[] args) {
        super(key, statement.toString());
        this.interpreter = Interpreter.createExampleInterpreter(statement, logFilePath, args);
    }

    @Override
    public void execute() {
        try {
            interpreter.allStep();
        } catch (Exception exception) {
            System.out.println("[RunExampleCommand.execute()] " + exception.getMessage());
        }
    }
}
