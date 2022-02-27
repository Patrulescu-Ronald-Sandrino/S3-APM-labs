package controller;

import model.adts.IHeap;
import model.adts.Dictionary;
import model.state.ProgramState;
import model.statement.CompoundStatement;
import model.statement.IStatement;
import model.types.Type;
import model.values.Value;
import repository.IRepository;
import repository.Repository;
import view.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Interpreter {
    private final IRepository<ProgramState> repository;
    private ExecutorService executor;
    private boolean displayStateFlag = false;
    private boolean displayInFileFlag = false;
    private boolean hideCompoundsFlag = false;

    public Interpreter(IRepository<ProgramState> repository) {
        this.repository = repository;
    }

    public boolean isDisplayStateFlag() {
        return displayStateFlag;
    }

    public boolean isDisplayInFileFlag() {
        return displayInFileFlag;
    }

    public boolean isHideCompoundsFlag() {
        return hideCompoundsFlag;
    }

    public void setDisplayStateFlag(boolean displayStateFlag) {
        this.displayStateFlag = displayStateFlag;
    }

    public void setDisplayInFileFlag(boolean displayInFileFlag) {
        this.displayInFileFlag = displayInFileFlag;
    }

    public void setHideCompoundsFlag(boolean hideCompoundsFlag) {
        this.hideCompoundsFlag = hideCompoundsFlag;
    }

    public void setInterpreterArguments(String[] args) {
        for (String s : args) {
            switch (s) {
                case "-d": {
                    setDisplayStateFlag(true);
                    break;
                }
                case "-dh": {
                    setDisplayStateFlag(true);
                    setHideCompoundsFlag(true);
                    break;

                }
                case "-df": {
                    setDisplayStateFlag(true);
                    setDisplayInFileFlag(true);
                    break;
                }
                case "-dhf": {
                    setDisplayStateFlag(true);
                    setDisplayInFileFlag(true);
                    setHideCompoundsFlag(true);
                    break;

                }
            }
        }
    }

    private boolean ranGarbageCollector(List<ProgramState> programList) {
        if (programList.isEmpty()) {
            return false;
        }

        IHeap<Integer, Value> heap = programList.get(0).getHeap();
        int heapSizeBeforeCleaning = heap.getContent().size();
        if (heapSizeBeforeCleaning == 0) {
            return false;
        }

//        Function<Map<Integer, Value>, String> heapMapToStringOfAddresses = h -> h.keySet().stream()
//                .map(address-> Integer.toString(address))
//                .reduce((acc, address) -> acc + " " + address).orElse("-empty heap");
//        System.out.println("Heap Addresses Before: " + heapMapToStringOfAddresses.apply(heap.getContent()));

        List<Collection<Value>> symbolTablesValuesList = programList.stream()
                .map(programState -> programState.getSymbolTable().getContent().values())
                .collect(Collectors.toList());
        Map<Integer, Value> cleanedHeapEntries = GarbageCollector.getCleanedHeapEntries(symbolTablesValuesList, heap.getContent());
        heap.setContent(cleanedHeapEntries);

        return (cleanedHeapEntries.size() < heapSizeBeforeCleaning);
//        System.out.println();
    }

    public void logProgramStateExecution(List<ProgramState> programList, String prefixProgramState) {
        if (!isDisplayStateFlag()) {
            return;
        }

//        if (isHideCompoundsFlag() && programList.size() == 1 && programList.get(0).getExecutionStack().top() instanceof CompoundStatement) {
//            return;
//        }

        programList
//                .stream().filter(programState -> !(programState.getExecutionStack().top() instanceof CompoundStatement))
                .forEach(programState -> {
                    try {
                        String prefix = programState.getExecutionStack().top() instanceof CompoundStatement ? "\033[1;33m" : "\033[1;31m";
                        prefix = programState.getExecutionStack().isEmpty() ? "\033[1;90m": prefix;
                        String suffix = ":\033[0m";
                        if (isDisplayInFileFlag()) {
                            repository.logProgramStateExecution(programState, prefix, suffix, prefixProgramState);
                        } else {
                            System.out.println(programState.prettyPrintFunctional(prefix, suffix));
                        }
                    }
                    catch (Exception exception) {
                        System.out.println("[Interpreter.oneStepForAllPrograms] failed: " + exception.getMessage());
                    }
                });

    }

    public void oneStepForAllPrograms(List<ProgramState> programList) {
//        logProgramStateExecution(programList, "\033[1mBEFORE\033[0m\n"); // moved in allStep

        List<Callable<ProgramState>> callableList = programList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>)(p::oneStep))
                .collect(Collectors.toList());

        List<ProgramState> newProgramList = null;
        try {
            newProgramList = executor.invokeAll(callableList).stream()
                    .map(future -> {
                        try { return future.get();}
                        catch (Exception e) {System.out.println("[Interpreter.oneStepForAllPrograms] failed: " + e.getMessage());}
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (newProgramList != null) {
            programList.addAll(newProgramList);
        }

//        logProgramStateExecution(programList, "\033[1mAFTER\033[0m\n");
        logProgramStateExecution(programList, null);

        repository.setProgramList(programList);
    }

    public void allStep() {
        executor = Executors.newFixedThreadPool(2);

        List<ProgramState> programList = removeCompletedProgram(repository.getProgramList());
        logProgramStateExecution(programList, null); // moved here from oneStepForAllProgramState()
        while (programList.size() > 0) {
            ranGarbageCollector(programList);
            oneStepForAllPrograms(programList);
            programList = removeCompletedProgram(repository.getProgramList());
        }

        executor.shutdownNow();

        repository.setProgramList(programList);
    }

    List<ProgramState> removeCompletedProgram(List<ProgramState> programStateList) {
        return programStateList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public static Interpreter createExampleInterpreter(IStatement statement, String logFilePath, String[] args) {

        try {
            statement.typecheck(new Dictionary<>());
        }
        catch (Exception exception) {
            String message = String.format("Typechecker failed for statement: %s\nReason: %s", statement.toString(), exception.getMessage());
            System.out.println(message);
            System.exit(1);

        }
        ProgramState programState = new ProgramState(statement);
        IRepository<ProgramState> repository = new Repository(programState, logFilePath);
        Interpreter interpreter = new Interpreter(repository);
        interpreter.setInterpreterArguments(args);

        return interpreter;
    }

    public static void main(String[] args) {
        /* main Version 4 */
        TextMenu textMenu = new TextMenu();

        textMenu.addCommand(new ExitCommand("0", "exit"));

        List<IStatement> examples = IStatement.IStatementExamples.examples;
        int counter;
        for (counter = 1; counter <= examples.size(); counter++) {
            IStatement statement = examples.get(counter - 1);
            String logFilePath = String.format("log/log%02d.txt", counter);

            /* this */
//            ProgramState programState = new ProgramState(statement);
//            IRepository<ProgramState> repository = new Repository(programState, logFilePath);
//            Interpreter interpreter = new Interpreter(repository);
//            interpreter.setInterpreterArguments(args);
//            textMenu.addCommand(new RunExampleCommand(Integer.toString(counter), statement.toString(), interpreter));
            /* or this*/
            textMenu.addCommand(new RunExampleCommand(Integer.toString(counter), statement, logFilePath, args));
        }
        Map<String, Command> allRunExampleCommandEntries = textMenu.getCommands().entrySet().stream()
                .filter(entry -> entry.getValue() instanceof RunExampleCommand)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        textMenu.addCommand(new RunMultipleCommand(Integer.toString(counter), "Run all", allRunExampleCommandEntries));
        textMenu.show();
    }
}

/* main Version 1 */
//        ProgramState programState1 = new ProgramState(IStatement.example1);
//        IRepository<ProgramState> repository1 = new Repository(programState1, "log/log1.txt");
//        Interpreter interpreter1 = new Interpreter(repository1);
//        interpreter1.setInterpreterArguments(args);
//
//        ProgramState programState2 = new ProgramState(IStatement.example2);
//        IRepository<ProgramState> repository2 = new Repository(programState2, "log/log2.txt");
//        Interpreter interpreter2 = new Interpreter(repository2);
//        interpreter2.setInterpreterArguments(args);
//
//        ProgramState programState3 = new ProgramState(IStatement.example3);
//        IRepository<ProgramState> repository3 = new Repository(programState3, "log/log3.txt");
//        Interpreter interpreter3 = new Interpreter(repository3);
//        interpreter3.setInterpreterArguments(args);
//
//        TextMenu textMenu = new TextMenu();
//        textMenu.addCommand(new ExitCommand("0", "exit"));
//        textMenu.addCommand(new RunExampleCommand("1", IStatement.example1.toString(), interpreter1));
//        textMenu.addCommand(new RunExampleCommand("2", IStatement.example2.toString(), interpreter2));
//        textMenu.addCommand(new RunExampleCommand("3", IStatement.example3.toString(), interpreter3));
//        textMenu.addCommand(new RunExampleCommand("4", IStatement.example1A3.toString(), interpreter4));
//        textMenu.addCommand(new RunExampleCommand("5", IStatement.example2A3.toString(), interpreter5));
//        textMenu.show();


/* main Version 2 */
//        Interpreter interpreter1, interpreter2, interpreter3;
//        interpreter1 = Interpreter.createExampleInterpreter(IStatement.example1, "log/log1.txt", args);
//        interpreter2 = Interpreter.createExampleInterpreter(IStatement.example2, "log/log2.txt", args);
//        interpreter3 = Interpreter.createExampleInterpreter(IStatement.example3, "log/log3.txt", args);
//        Interpreter interpreter4 = Interpreter.createExampleInterpreter(IStatement.example1A3, "log/log4.txt", args);
//        Interpreter interpreter5 = Interpreter.createExampleInterpreter(IStatement.example2A3, "log/log5.txt", args);
//
//        TextMenu textMenu = new TextMenu();
//        textMenu.addCommand(new ExitCommand("0", "exit"));
//        textMenu.addCommand(new RunExampleCommand("1", IStatement.example1.toString(), interpreter1));
//        textMenu.addCommand(new RunExampleCommand("2", IStatement.example2.toString(), interpreter2));
//        textMenu.addCommand(new RunExampleCommand("3", IStatement.example3.toString(), interpreter3));
//        textMenu.addCommand(new RunExampleCommand("4", IStatement.example1A3.toString(), interpreter4));
//        textMenu.addCommand(new RunExampleCommand("5", IStatement.example2A3.toString(), interpreter5));
//        textMenu.show();


/* main Version 3 */
//    TextMenu textMenu = new TextMenu();
//        textMenu.addCommand(new ExitCommand("0", "exit"));
//                textMenu.addCommand(new RunExampleCommand("1", IStatement.example1A2, "log/log01.txt", args));
//                textMenu.addCommand(new RunExampleCommand("2", IStatement.example2A2, "log/log02.txt", args));
//                textMenu.addCommand(new RunExampleCommand("3", IStatement.example3A2, "log/log03.txt", args));
//                textMenu.addCommand(new RunExampleCommand("4", IStatement.example1A3, "log/log04.txt", args));
//                textMenu.addCommand(new RunExampleCommand("5", IStatement.example2A3, "log/log05.txt", args));
//                textMenu.addCommand(new RunExampleCommand("6", IStatement.example1A4, "log/log06.txt", args));
//                textMenu.addCommand(new RunExampleCommand("7", IStatement.example2A4, "log/log07.txt", args));
//                textMenu.addCommand(new RunExampleCommand("8", IStatement.example3A4, "log/log08.txt", args));
//                textMenu.addCommand(new RunExampleCommand("9", IStatement.example4A4, "log/log09.txt", args));
//                textMenu.addCommand(new RunExampleCommand("10", IStatement.example5A4, "log/log10.txt", args));
//                textMenu.addCommand(new RunExampleCommand("11", IStatement.e11, "log/log11.txt", args));
//                textMenu.addCommand(new RunExampleCommand("12", IStatement.example1A5, "log/log12.txt", args));
//                textMenu.addCommand(new RunExampleCommand("13", IStatement.e13, "log/log13.txt", args));
//
//                Map<String, Command> allRunExampleCommandEntries = textMenu.getCommands().entrySet().stream()
//        .filter(entry -> entry.getValue() instanceof RunExampleCommand)
//        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        textMenu.addCommand(new RunMultipleCommand("14", "Run all", allRunExampleCommandEntries));
//        textMenu.show();

/* Log */

// A3 changes:
// 2021.11.13 ~01:20:00
//      Fixed:
//      - referencing null pointer inside readFileStatement.execute() by checking the result of BufferedReader.nextLine()

// 2021.11.13 01:23:00
//      Fixed:
//      - improper logProgramStatement Handling for right interpreter arguments (added break; in the switch for passing arguments)
//

// A4 changes:
// 2021.11.16 ~23:54
//      - clearing the log file is now done inside the program, in the logProgramState method from repository
//      - CompoundStatement.toString no longer prints brackets "()
//      -

// A5 changes:
// 2021.11.22 21:15
//      - added ProgramState.prettyPrintFunctional(String, String), but it prints the outList first
//      - added a RunMultipleCommand whose constructor receives as arguments a map of commands
//      - added GarbageCollector.getAddressesFromSymbolTable(Collection<Value>, IHeap<Integer, Value>) which goes in depth for every RefValue found in the SymbolTable