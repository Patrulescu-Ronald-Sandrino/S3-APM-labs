package model.statement;

import javafx.util.Pair;
import model.adts.IDictionary;
import model.state.ProgramState;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

import java.util.List;

public class Await implements IStatement{
    private final String variable;

    public Await(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        Value value = programState.getSymbolTable().lookUp(variable);

        if (value == null) {
            throw new Exception("[Await.execute()] Error: variable " + variable + " not found in SymbolTable");
        }
        if (value.getType() != IntType.T) {
            throw new Exception("[Await.execute()] Error: variable " + variable + " has type " + value.getType() + ", expected int");
        }

        int barrierTableKey = ((IntValue) value).getValue();
        Pair<Integer, List<Integer>> barrierTableValue;
        barrierTableValue = programState.getBarrierTable().lookUp(barrierTableKey);

        if (barrierTableValue == null) {
            throw new Exception("[Await.execute()] Error: " + barrierTableKey + " is not a key in the BarrierTable");
        }

        List<Integer> integerList = barrierTableValue.getValue();
        int N1 = barrierTableValue.getKey();
        int NL = integerList.size();

        String debugString = "ThreadID: " + String.valueOf(programState.getThreadID()) + '\n';
        debugString  += ("this: " + this) + '\n';
        debugString  += ("ExeStack.top(): " + programState.getExecutionStack().top()) + '\n';
        debugString  += ("integerList before: " + integerList) + '\n';
        if (N1 > NL) {
            debugString  += ("N1 > NL: " + N1 + " > " + NL) + '\n';
            if (integerList.contains(programState.getThreadID())) {
                debugString  += ("contains => no modification") + '\n';
                programState.getExecutionStack().push(new Await(variable));
            }
            else {
                integerList.add(programState.getThreadID());
                debugString  += ("!contains => added") + '\n';
                programState.getExecutionStack().push(new Await(variable));
            }
        }
        else {
            debugString  += ("!(N1 > NL): " + N1 + " <= " + NL) + '\n';
        }
        debugString  += ("integerList after: " + integerList) + '\n';
//        System.out.println(debugString);
        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type variableType = typeEnv.lookUp(variable);

        if (variableType != IntType.T) {
            throw new Exception("[Await.typecheck()] Error: variable " + variable + " has type " + variableType + ", expected int");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new Await(variable);
    }

    @Override
    public String toString() {
        return "await(" + variable + ")";
    }
}
