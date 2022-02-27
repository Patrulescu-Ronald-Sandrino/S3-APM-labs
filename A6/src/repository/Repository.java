package repository;

import exceptions.repository.RepositoryEmptyException;
import model.state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository<ProgramState> {
    private List<ProgramState> elements;
    private final String logFilePath;
    private boolean appendInFile = false;

    public Repository(ProgramState programState, String logFilePath) {
        this.elements = new ArrayList<>();
        this.elements.add(programState);

        this.logFilePath = logFilePath;
    }

    public Repository(String logFilePath) {
        this.elements = new ArrayList<>();
        this.logFilePath = logFilePath;
    }

    @Override
    public void add(ProgramState e) {
        elements.add(e);
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public void logProgramStateExecution(ProgramState programState, String prefixAdt, String suffixAdt, String prefixProgramState) throws Exception {
        // TODO: error-checking ???
        if (elements.size() == 0) {
            throw new RepositoryEmptyException("[Repository.logProgramStateExecution()] Repository is empty");
        }

        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, appendInFile)))) {
            if (prefixProgramState != null) logFile.print(prefixProgramState);
            logFile.print(programState.prettyPrintFunctional(prefixAdt, suffixAdt));
            appendInFile = true;
        } catch (IOException exception) {
            throw new Exception("[Repository.logProgramStateExecution] Failed - Reason: " + exception.getMessage());
        }
    }

    @Override
    public List<ProgramState> getProgramList() {
        return elements;
    }

    @Override
    public void setProgramList(List<ProgramState> programStateList) {
        elements = programStateList;
    }
}
