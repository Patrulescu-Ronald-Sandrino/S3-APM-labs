# TO DO
## Ask list
2. 1-liners vs non 1-liners (see ProgramExecutionController.setNumberOfActiveProgramStatesTextField())
3. can it be improved? (see ProgramExecutionController.setProgramStateIDsListView())
4. 
5. 
6. 
7. 
8. call to setCellValueFactory can set in initialize? (see ProgramExecutionController.setSymbolTableTableViewAndExecutionStackListView())
9. Should the programStateIDsListView have single selection?



## Self-Assigned:
2. Ce se intampla la final? daca apelam update() nu se golesc cumva View-urile
inainte sa apucam sa le vedem?
3. 
4. .
5. What happens when you load a program multiple times and how do you fix it if it doesn't work?
6. See what happens to ThreadIDs at others
7. test alert.setHeaderText(null);
8. 
9. 
10. cleanup
11. See Evernote/To Do Tree/Uni


## Lab9/10.pdf
1. A window to select the program that will be executed. You can display the  
   list of possible programs as a ListView. Each item of the ListView is the string  
   representation of a possible program (IStmt).

2. The main window that displays the following information:

a. the number of PrgStates as a TextField

b. the HeapTable as a TableView with two columns: address and Value

c. the Out as a ListView

d. the FileTable as a ListView

e. the list of PrgState identifiers as a ListView

f. a Table View with two columns: variable name and Value, which displays the  
SymTable of the PrgState whose ID has been selected from the list described
at `e.`

g. a List View which displays the ExeStack of the PrgState whose ID has been  
selected from the list described at `e.` First element of the ListView is  
a string represenatation of the top of ExeStack, the second element of the  
ListView represents the second element from the ExeStack and so on.

h. A button "Run one step" that runs oneStepForAllPrg you have to design and  
implement the handler of this button based on the previous method allStep  
After each run the displayed information is updated. You may want to write a  
service which wraps the repository and signals any change of the list of  
PrgStates.


# Done
1. can selectionModel().getSelectedItem() return null? ANSWER: YES IT CAN



# Requirements

You must extend the JAVA project from the assignment A6. Please implement in  
JavaFX a GUI for your interpreter from the assignment A6. The GUI must support  
the following
operations:
1. A window to select the program that will be executed. You can display the  
   list of possible programs as a ListView. Each item of the ListView is the string  
   representation of a possible program (IStmt).

2. The main window that displays the following information:

a. the number of PrgStates as a TextField

b. the HeapTable as a TableView with two columns: address and Value

c. the Out as a ListView

d. the FileTable as a ListView

e. the list of PrgState identifiers as a ListView

f. a Table View with two columns: variable name and Value, which displays the  
SymTable of the PrgState whose ID has been selected from the list described
at `e.`

g. a List View which displays the ExeStack of the PrgState whose ID has been  
selected from the list described at `e.` First element of the ListView is  
a string represenatation of the top of ExeStack, the second element of the  
ListView represents the second element from the ExeStack and so on.

h. A button "Run one step" that runs oneStepForAllPrg you have to design and  
implement the handler of this button based on the previous method allStep  
After each run the displayed information is updated. You may want to write a  
service which wraps the repository and signals any change of the list of  
PrgStates.

| HBox(Active Program States)         | VBox(1StepButton, AllStepButton) |                          | VBox1(APS) |
|-------------------------------------|----------------------------------|--------------------------|------------|
| VBox(SymbolTableTableView)          | VBox(HeapTableView)              | VBox(FileTableTableView) | VBox1(APS) |
| HBox1(VBox(ExecutionStackListView)) | HBox1(VBox(OutListListView))     |                          | VBox1(APS) |






# How to configure JavaFX

1. File | Project Structures | Libraries | + (New Project Library) | path/to/javaFX_SDK
2. Add  
`--module-path /home/kamui/Programs/javafx-sdk-17.0.1/lib --add-modules javafx.controls,javafx.fxml`  
to Run Configuration | VM Options

## Some guides
1. https://stackoverflow.com/questions/7065402/how-to-add-external-library-in-intellij-idea
2. https://stackoverflow.com/questions/51478675/error-javafx-runtime-components-are-missing-and-are-required-to-run-this-appli 
   1. https://stackoverflow.com/questions/52467561/intellij-cant-recognize-javafx-11-with-openjdk-11 