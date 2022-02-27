package model.statement;

import model.adts.IDictionary;
import model.state.ProgramState;
import model.types.Type;

public class Nop implements IStatement {
    public Nop() {
    }

    @Override
    public ProgramState execute(ProgramState programState) {
        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new Nop();
    }

    @Override
    public String toString() {
        return "NOP";
    }
}
