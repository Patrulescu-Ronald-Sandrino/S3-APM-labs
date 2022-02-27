package model.expression;

import exceptions.expression.InvalidOperandException;
import model.adts.IDictionary;
import model.adts.IHeap;
import model.types.RefType;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;

public class HeapReadExpression implements Expression {
    private final Expression expression;

    public HeapReadExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value evaluate(IDictionary<String, Value> symbolTable, IHeap<Integer, Value> heap) throws Exception {
        Value expressionValue = expression.evaluate(symbolTable, heap);
        if (!(expressionValue instanceof RefValue)) {
            throw new Exception("[HeapReadExpression.execute()] expression must be evaluated to RefValue");
        }

        int address = ((RefValue)expressionValue).getAddress();
        if (!heap.containsKey(address)) {
            throw new Exception("[HeapReadExpression.execute()] Heap doesn't contain address " + address + " given by expression " + expression);
        }

        return heap.get(address);
    }

    @Override
    public Type typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = expression.typecheck(typeEnv);

        if (!(type instanceof RefType)) {
            throw new InvalidOperandException("The read heap argument is not a Ref Type");
        }

        RefType refType = (RefType) type;
        return refType.getInner();
    }

    @Override
    public Expression deepCopy() {
        return new HeapReadExpression(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "read(" + expression.toString() + ")";
    }
}
