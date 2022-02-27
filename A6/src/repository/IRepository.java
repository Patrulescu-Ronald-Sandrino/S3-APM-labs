package repository;

import model.state.ProgramState;

import java.util.List;

public interface IRepository<E> {

    void add(E e);

    void clear();

    int size();

    void logProgramStateExecution(ProgramState programState, String prefixAdt, String suffixAdt, String prefixProgramState) throws Exception;

    List<ProgramState> getProgramList();

    void setProgramList(List<ProgramState> programStateList);
}
