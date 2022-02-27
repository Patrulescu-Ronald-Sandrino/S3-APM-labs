package model.statement;


import model.adts.IDictionary;
import model.expression.*;
import model.interfaces.Cloneable;
import model.state.ProgramState;
import model.types.*;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;

import java.util.ArrayList;
import java.util.List;

public interface IStatement extends Cloneable<IStatement> {
    ProgramState execute(ProgramState programState) throws Exception;

    IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception;

    class IStatementExamples {
        public static final List<IStatement> examples = addExamples();
        
        private static List<IStatement> addExamples() {
            List<IStatement> statementList = new ArrayList<>();

            statementList.add(
                    new CompoundStatement(
                            new VariableDeclarationStatement("v", IntType.T),
                            new CompoundStatement(
                                    new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                                    new PrintStatement(new VariableExpression("v")))));

            statementList.add(
                    new CompoundStatement(
                            new VariableDeclarationStatement("a", IntType.T),
                            new CompoundStatement(
                                    new VariableDeclarationStatement("b", IntType.T),
                                    new CompoundStatement(
                                            new AssignmentStatement(
                                                    "a",
                                                    new ArithmeticExpression("+", new ValueExpression(new IntValue(2)),
                                                            new ArithmeticExpression("*", new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                                            new CompoundStatement(
                                                    new AssignmentStatement(
                                                            "b",
                                                            new ArithmeticExpression("+", new VariableExpression("a"),
                                                                    new ValueExpression(new IntValue(1)))), new PrintStatement(new VariableExpression("b")))))));

            statementList.add(
                    new CompoundStatement(
                            new VariableDeclarationStatement("a", BoolType.T),
                            new CompoundStatement(
                                    new VariableDeclarationStatement("v", IntType.T),
                                    new CompoundStatement(
                                            new AssignmentStatement("a", new ValueExpression(BoolValue.TRUE)),
                                            new CompoundStatement(
                                                    new IfStatement(
                                                            new VariableExpression("a"),
                                                            new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                                                            new AssignmentStatement("v", new ValueExpression(new IntValue(3)))),
                                                    new PrintStatement(new VariableExpression("v")))))));

            statementList.add(
                    new CompoundStatement(new VariableDeclarationStatement("varf", StringType.T),
                            new CompoundStatement( new AssignmentStatement("varf", new ValueExpression(new StringValue("input/test1.in"))),
                                    new CompoundStatement(new openRFileStatement(new VariableExpression("varf")),
                                            new CompoundStatement(new VariableDeclarationStatement("varc", IntType.T),
                                                    new CompoundStatement(new readFileStatement(new VariableExpression("varf"), "varc"),
                                                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                    new CompoundStatement(new readFileStatement(new VariableExpression("varf"), "varc"),
                                                                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                                    new closeRFileStatement(new VariableExpression("varf")))))))))));

            statementList.add(
                    new CompoundStatement(new VariableDeclarationStatement("varf", StringType.T),
                            new CompoundStatement(new AssignmentStatement("varf", new ValueExpression(new StringValue("input/test2.in"))),
                                    new CompoundStatement(new openRFileStatement(new VariableExpression("varf")),
                                            new CompoundStatement(new VariableDeclarationStatement("varc", IntType.T),
                                                    new CompoundStatement(new readFileStatement(new VariableExpression("varf"), "varc"),
                                                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                    new CompoundStatement(new readFileStatement(new VariableExpression("varf"), "varc"),
                                                                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                                    new closeRFileStatement(new VariableExpression("varf")))))))))));

            statementList.add(
                    new CompoundStatement(
                            new VariableDeclarationStatement("v", new RefType(IntType.T)),
                            new CompoundStatement(
                                    new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                                    new CompoundStatement(
                                            new VariableDeclarationStatement("a", new RefType(new RefType(IntType.T))),
                                            new CompoundStatement(
                                                    new HeapAllocationStatement("a", new VariableExpression("v")),
                                                    new CompoundStatement(
                                                            new PrintStatement(new VariableExpression("v")),
                                                            new PrintStatement(new VariableExpression("a")))
                                            )
                                    )
                            )
                    ));

            statementList.add(
                    new CompoundStatement(new VariableDeclarationStatement("v", new RefType(IntType.T)),
                            new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                                    new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(IntType.T))),
                                            new CompoundStatement(new HeapAllocationStatement("a", new VariableExpression("v")),
                                                    new CompoundStatement(new PrintStatement(new HeapReadExpression(new VariableExpression("v"))),
                                                            new PrintStatement(new ArithmeticExpression(
                                                                    "+",
                                                                    new HeapReadExpression(new HeapReadExpression(new VariableExpression("a"))),
                                                                    new ValueExpression(new IntValue(5)))
                                                            )
                                                    )
                                            )
                                    )
                            )
                    ));

            statementList.add(
                    new CompoundStatement(new VariableDeclarationStatement("v", new RefType(IntType.T)),
                            new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                                    new CompoundStatement(new PrintStatement(new HeapReadExpression(new VariableExpression("v"))),
                                            new CompoundStatement(new HeapWriteStatement("v", new ValueExpression(new IntValue(30))),
                                                    new PrintStatement(new ArithmeticExpression(
                                                            "+",
                                                            new HeapReadExpression(new VariableExpression("v")),
                                                            new ValueExpression(new IntValue(5))
                                                    )
                                                    )
                                            )
                                    )
                            )
                    ));

            statementList.add(
                    new CompoundStatement(new VariableDeclarationStatement("v", new RefType(IntType.T)),
                            new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                                    new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(IntType.T))),
                                            new CompoundStatement(new HeapAllocationStatement("a", new VariableExpression("v")),
                                                    new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue(30))),
                                                            new PrintStatement(new HeapReadExpression(new HeapReadExpression(new VariableExpression("a"))))
                                                    )
                                            )
                                    )
                            )
                    ));

            statementList.add(
                    new CompoundStatement(
                            new VariableDeclarationStatement("v", IntType.T),
                            new CompoundStatement(
                                    new AssignmentStatement("v", new ValueExpression(new IntValue(4))),
                                    new CompoundStatement(
                                            new WhileStatement(
                                                    new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntValue(0))),
                                                    new CompoundStatement(
                                                            new PrintStatement(new VariableExpression("v")),
                                                            new AssignmentStatement("v", new ArithmeticExpression("-", new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                            new PrintStatement(new VariableExpression("v"))))));

            statementList.add(
                    new CompoundStatement(new VariableDeclarationStatement("v", new RefType(IntType.T)),
                            new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                                    new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue())),
                                            new PrintStatement(new VariableExpression("v"))))));

            statementList.add(
                    new CompoundStatement(
                            new VariableDeclarationStatement("v", IntType.T),
                            new CompoundStatement(
                                    new VariableDeclarationStatement("a", new RefType(IntType.T)),
                                    new CompoundStatement(
                                            new AssignmentStatement("v", new ValueExpression(new IntValue(10))),
                                            new CompoundStatement(
                                                    new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))),
                                                    new CompoundStatement(
                                                            new ForkStatement(
                                                                    new CompoundStatement(
                                                                            new HeapWriteStatement("a", new ValueExpression(new IntValue(30))),
                                                                            new CompoundStatement(
                                                                                    new AssignmentStatement("v", new ValueExpression(new IntValue(32))),
                                                                                    new CompoundStatement(
                                                                                            new PrintStatement(new VariableExpression("v")),
                                                                                            new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
                                                                                    )
                                                                            )
                                                                    )
                                                            ),
                                                            new CompoundStatement(
                                                                    new PrintStatement(new VariableExpression("v")),
                                                                    new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
                                                            )
                                                    )
                                            )
                                    )
                            )
                    ));

            statementList.add(
                    new CompoundStatement(
                            new VariableDeclarationStatement("v", IntType.T),
                            new CompoundStatement(
                                    new VariableDeclarationStatement("a", new RefType(IntType.T)),
                                    new CompoundStatement(
                                            new AssignmentStatement("v", new ValueExpression(new IntValue(10))),
                                            new CompoundStatement(
                                                    new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))),
                                                    new CompoundStatement(
                                                            new ForkStatement(
                                                                    new CompoundStatement(
                                                                            new HeapWriteStatement("a", new ValueExpression(new IntValue(30))),
                                                                            new CompoundStatement(
                                                                                    new AssignmentStatement("v", new ValueExpression(new IntValue(32))),
                                                                                    new CompoundStatement(
                                                                                            new PrintStatement(new VariableExpression("v")),
                                                                                            new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
                                                                                    )
                                                                            )
                                                                    )
                                                            ),
                                                            new CompoundStatement(
                                                                    new PrintStatement(new VariableExpression("v")),
                                                                    new CompoundStatement(new PrintStatement(new HeapReadExpression(new VariableExpression("a"))),
                                                                            new CompoundStatement(new Nop(),
                                                                                    new Nop()
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    ));

//            statementList.add(new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(10))), new PrintStatement(new VariableExpression("v"))));
            
            return statementList;
        }
    }
}
