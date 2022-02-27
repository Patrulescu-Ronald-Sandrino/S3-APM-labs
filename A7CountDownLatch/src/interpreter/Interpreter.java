package interpreter;

import model.adts.DictionaryWithClonableValues;
import model.adts.IHeap;
import model.state.ProgramState;
import model.statement.CompoundStatement;
import model.statement.IStatement;
import model.values.Value;
import repository.IRepository;
import repository.Repository;
import view.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Interpreter {
    @Override
    public String toString() {
        return repository.getLatestOriginalStatement().toString();
    }

    public List<ProgramState> getProgramStateList() {
        return repository.getProgramList();
    }

    private final IRepository<ProgramState> repository;
    private final ExecutorService executor;
    private boolean displayStateFlag = true;
    private boolean displayInFileFlag = true;
    private boolean hideCompoundsFlag = false;

    public Interpreter(IRepository<ProgramState> repository) {
        this.repository = repository;
        executor = Executors.newFixedThreadPool(10);
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
                case "-d" -> setDisplayStateFlag(true);
                case "-dh" -> {
                    setDisplayStateFlag(true);
                    setHideCompoundsFlag(true);

                }
                case "-df" -> {
                    setDisplayStateFlag(true);
                    setDisplayInFileFlag(true);
                }
                case "-dhf" -> {
                    setDisplayStateFlag(true);
                    setDisplayInFileFlag(true);
                    setHideCompoundsFlag(true);

                }
            }
        }
    }

    private void ranGarbageCollector(List<ProgramState> programList) {
        if (programList.isEmpty()) {
            return;
        }

        IHeap<Integer, Value> heap = programList.get(0).getHeap();
        int heapSizeBeforeCleaning = heap.getContent().size();
        if (heapSizeBeforeCleaning == 0) {
            return;
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

//        return (cleanedHeapEntries.size() < heapSizeBeforeCleaning);
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

        List<Callable<ProgramState>> callableList =
                programList.stream()
                .filter(ProgramState::isNotCompleted) // since removeCompletedPrograms was no longer called
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

//        repository.setProgramList(programList.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList()));
        repository.setProgramList(programList);
    }

    public void oneStepForAllPrograms() {
        this.oneStepForAllPrograms(repository.getProgramList());
    }

    public void allStep() {

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

    public int getRepoSize() {
        return repository.size();
    }

    public boolean isNotFinished() {
        // turn the Program State List into a stream
//        return repository.getProgramList().stream()
//                // map it to their Execution Stack
//                .map(ProgramState::getExecutionStack)
//                // and map those stack to the value of them being  empty
//                .map(IExecutionStack::isEmpty)
//                // if there's any of them which is not empty, then our program is not finished
//                .anyMatch(aBoolean -> !aBoolean);
        return repository.getProgramList().stream().anyMatch(ProgramState::isNotCompleted);
    }

    public boolean isFinished() { return ! isNotFinished();}

    public void setNewProgram(IStatement programStatement) {
        repository.setNewProgram(new ProgramState(programStatement));
    }


    public Optional<ProgramState> getAnyProgramState() {
        return this.getProgramStateList().stream().findAny();
    }

    public static Interpreter createExampleInterpreter(IStatement statement, String logFilePath, String[] args) {

        ProgramState programState = new ProgramState(statement);
        IRepository<ProgramState> repository = new Repository(programState, logFilePath);
        Interpreter interpreter = new Interpreter(repository);
        interpreter.setInterpreterArguments(args);

        return interpreter;
    }

    public static void main(String[] args) {
        /* main Version 4 */
        System.out.println("Current Working Directory = " + System.getProperty("user.dir"));

        TextMenu textMenu = new TextMenu();

        textMenu.addCommand(new ExitCommand("0", "exit"));

        List<IStatement> examples = IStatement.IStatementExamples.examples;
        int counter;
        for (counter = 1; counter <= examples.size(); counter++) {
            IStatement statement = examples.get(counter - 1);

            try {
                statement.typecheck(new DictionaryWithClonableValues<>());
            }
            catch (Exception exception) {
                String message = String.format("Typechecker failed for statement: %d: %s\nReason: %s", counter, statement.toString(), exception.getMessage());
                System.out.println(message);
                continue;

            }

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

        // create a command to run all the examples and add it to the menu
        Map<String, Command> allRunExampleCommandEntries = textMenu.getCommands().entrySet().stream()
                .filter(entry -> entry.getValue() instanceof RunExampleCommand)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        textMenu.addCommand(new RunMultipleCommand(Integer.toString(counter), "Run all", allRunExampleCommandEntries));

        textMenu.show();
    }
}
