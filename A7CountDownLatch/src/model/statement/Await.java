package model.statement;

import model.adts.IDictionary;
import model.state.ProgramState;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

public class Await implements IStatement {
    private final String variable;

    public Await(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        Value variableValue = programState.getSymbolTable().lookUp(variable);
        if (variableValue == null) {
            throw new Exception("[Await.execute()] Error: variable " + variable + " not found in SymbolTable");
        }
        if (variableValue.getType() != IntType.T) {
            throw new Exception("[Await.execute()] Error: variable type is " + variableValue.getType() + ", expected int");
        }
        int index = ((IntValue)variableValue).getValue();

//        Integer indexValue = programState.getLatchTable().lookUp(index); // + null check
        if (!programState.getLatchTable().isDefined(index)) {
            throw new Exception("[Await.execute()] Error: index " + index + " is not defined in the LatchTable");
        }
        int indexValueInLatchTable = programState.getLatchTable().lookUp(index);
        if (indexValueInLatchTable != 0) {
            programState.getExecutionStack().push(new Await(variable));
        }

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type variableType = typeEnv.lookUp(variable);
        if (variableType != IntType.T) {
            throw new Exception("[Await.typecheck()] Error: variable type is " + variableType + ", expected int");
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
