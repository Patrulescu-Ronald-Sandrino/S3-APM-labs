package model.statement;

import model.adts.IDictionary;
import model.state.ProgramState;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

public class CountDown implements IStatement {
    private final String variable;

    public CountDown(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        Value variableValue = programState.getSymbolTable().lookUp(variable);
        if (variableValue == null) {
            throw new Exception("[CountDown.execute()] Error: variable " + variable + " not found in SymbolTable");
        }
        if (variableValue.getType() != IntType.T) {
            throw new Exception("[CountDown.execute()] Error: variable type is " + variableValue.getType() + ", expected int");
        }
        int indexInLatchTable = ((IntValue)variableValue).getValue();

        if (!programState.getLatchTable().isDefined(indexInLatchTable)) {
            throw new Exception("[CountDown.execute()] Error: index " + indexInLatchTable + " is not defined in the LatchTable");
        }
        int indexValueInLatchTable = programState.getLatchTable().lookUp(indexInLatchTable);
        if (indexValueInLatchTable > 0) {
            programState.getLatchTable().update(indexInLatchTable, indexValueInLatchTable - 1);
        }
        programState.getOutList().add(new IntValue(programState.getThreadID()));
        
        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type variableType = typeEnv.lookUp(variable);
        if (variableType != IntType.T) {
            throw new Exception("[CountDown.typecheck()] Error: variable type is " + variableType + ", expected int");
        }
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new CountDown(variable);
    }

    @Override
    public String toString() {
        return "countDown(" + variable + ")";
    }
}
