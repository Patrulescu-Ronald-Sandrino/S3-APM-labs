package model.statement;

import model.adts.IDictionary;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;

public class ConditionalAssignment implements IStatement {
    private final String variable;
    private final Expression condition;
    private final Expression thenExpression;
    private final Expression elseExpression;

    public ConditionalAssignment(String variable, Expression condition, Expression thenExpression, Expression elseExpression) {
        this.variable = variable;
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        // TODO: verifications (are they necessary?)
        IStatement newStatement = new IfStatement(
                condition,
                new AssignmentStatement("v", thenExpression),
                new AssignmentStatement("v", elseExpression)
        );
        programState.getExecutionStack().push(newStatement);

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type conditionType = condition.typecheck(typeEnv);
        if (conditionType != BoolType.T) {
            throw new Exception("[ConditionalAssignment.typecheck()] Error: condition has type " + conditionType + ", expected boolean");
        }

        Type variableType = typeEnv.lookUp(variable);
        Type thenExpressionType = thenExpression.typecheck(typeEnv);
        Type elseExpressionType = elseExpression.typecheck(typeEnv);

        if (variableType != thenExpressionType || variableType != elseExpressionType) {
            throw new Exception("[ConditionalAssignment.typecheck()] Error: the types of variable, thenExpression and elseExpression do not match");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ConditionalAssignment(variable, condition.deepCopy(), thenExpression.deepCopy(), elseExpression.deepCopy());
    }

    @Override
    public String toString() {
        return "v=(" + condition + ")?" + thenExpression + ":" + elseExpression;
    }
}
