DATE\_FORMAT=`YYYY.MM.DD [~]HH:MM[:SS]

# A? changes
## DATE\_FORMAT
1. feature 1
- step 1
- step 2
2. feature 2
...


# changes 2022.01.30 16:58 -> 
1. Reapeat Until
- add LogicNegationExpression + examples @ 17:40
- add RepeatUntil Statement @ 18:18
    - fix complete programs appearing in the State IDs @ 18:39 by filtering not complete programs in ProgramExecutionController.setProgramStateIDsListView
- found run-time bug at ProgramExecutionController.setSymbolTableTableViewAndExecutionStackListView() when getting the selected item -> null case wasn't checked @ 20:52
2. CyclicBarrier ~23:53 -> 3:11
phase 1:
phase 2: add BarrierTableTableView
phase 3:
- problem:barrierTableTableView not showing the list
- fix: refresh the table
- change ExecutionStack toString to do a regular print and the toString for CompoundStatement



# changes 2022.01.29 02:01
1. Trial #1: filter non finished program states in `Interpreter.oneStepForAllPrograms()`


# changes 2022.01.29 ????
1. added `.getDeclaredConstructor()` in ProgramSelectionController line 121 to get rid of the compiler warning
2. set the default value for logging to file to true
3. add typechecking with alert for the program examples in ProgramSelectionController.interpreterSetUp() UPDATE: exclude the failed programs from the list of added programs



# A7 changes 2022.01.?? ??:?? -> 2022.01.?? ??:??

## 2022.01.12 02:25 -> 2022.01.12 implement ProgramExecutionController
## EDIT: until 02:36 I lost time with syntax highlighting of the template for the GUI:
1. initialize()
    1. update()
    - since I'm only accessing the Repository's list of programs, I will write a function on the Interpreter that does
    exactly that, there's no need to provide access to the repository (Now I realize that this is how it should be actually, 
    the Service is between the Data and the GUI. Nice!)
    so there we go, `public List<ProgramState> Interpreter.getProgramStateList()`
    1 line of code. I'm melting
    - setProgramStateIDsListView()
        created `public int ProgramState.getThreadID()`
    - setSymbolTableTableViewAndExecutionStackListView()
        - for which Symbol Table?
        I believe we need single selection for the ProgramStateIDsListView, but in the template i didn't see one yet
        - if I understood well from the template, setCellValueFactory does exactly what it says, it tells the
        ListView what to take from an Item and put into the column's cell, aka a mapping 
        (EDIT @ 05:04 And the reason the Items in the TableColum are given from an ObservableList is that it is much easier
        to operate like this, they are Observable so updates can be made
        - WAIT: can setting the CellValue factory be put in initialize(). I should try it out, but also Ask
        - UPDATE: I have no idea how this is done
        - UPDATE 2: it's 04:45, and I realized that I wrote setCellFactory() instead of setCellValueFactory()
        when a Cell in the symbolTableTableViewSymbolColumn needs to be updated, then update it with the value of getKey() of
                  the object stored on that row of the TableView
        - when finding the programState with the thread id of the selected one in the list:
        can selectionModel().getSelectedItem() return null? time to test it
...
IDK_NR. 09:03 
- at view.ProgramExecutionController.setSymbolTableTableViewAndExecutionStackListView(ProgramExecutionController.java:122)
at view.ProgramExecutionController.update(ProgramExecutionController.java:73)
at view.ProgramExecutionController.initialize(ProgramExecutionController.java:196
probably because I didn't set any selection on the ProgramStateIDsListView
trial 1: inside update(), select the first Item if none is selected
- Caused by: java.lang.NullPointerException: Cannot invoke "java.util.concurrent.ExecutorService.invokeAll(java.util.Collection)" because "this.executor" is null
at interpreter.Interpreter.oneStepForAllPrograms(Interpreter.java:155)
at interpreter.Interpreter.oneStepForAllPrograms(Interpreter.java:178)
at view.ProgramExecutionController.oneStep(ProgramExecutionController.java:163)
at view.ProgramExecutionController.handleOneStepButtonAction(ProgramExecutionController.java:176)
I believe this is pretty self-explanatory: The invoker is initialized only in the
allStep() method from the Interpreter, this means that since the Threads Assignment,
the oneStepForAllPrograms() method wouldn't have worked
fix: initialize the executor in the Interpreter's constructor
UPDATE: is it really OK to have the executor initialized there?
UPDATE 2: I guess that as long as you don't run the same program from the same interpreter 
in parallel, then it's ok
-also noted that the number of threads is 2,
so increase it to 10
-[Interpreter.oneStepForAllPrograms] failed: java.lang.Exception: ProgramState's ExecutionStack is empty!
trial 1: when running 1 step and All Step, check the stack too
, besides checking the repository
fix 1: add `public bool Interpreter.isFinished()` and  replace in with the previous checking done in the  
-problem: the Execution Stack is not empty in the end
reason: I was wrong to assume that we shouldn't update the views and list after the last
repository change, since, the program States contain all the data that matters in that 
moment,
- 12:39
- fix: the function for setting up the heapTableTableView was not implemented
- while condition for allStep in ProgramExecutionController was while(interpreter.isFinished)
- discard particular messages for execution
- problem: SymbolTableTableView not updating properly
try changing the SymbolTable's Dictionary to a HashTable

        



## 2022.01.11 23:02 -> 2022.01.12 02:20 (nigga what? did it really pass 3 hours just for these runTime errors?; no way, there must be some... time-time error)
1. run-time erros after finishing ProgramSelectionController
- the selection mode the the intepretersListView was set before adding the interpreters to the ListView
- in the interpretersSetUp() the interpreters were being added to the local interpretersList, but not to the
interpretersList of the class's (ProgramSelectionController) instance field
- almost the same thing as above, but for the repository and with the difference that now I was only adding them to the instance's
field, which wasn't initialized, so NullPointerException (i'm sooo dumb)
- i'm not "sooo dumb", i'm "Dumb Dumb", the instance's fields for the repository and interpreter list are still not initialized, thus
null
- "Cannot invoke "javafx.scene.control.ListView.setItems(javafx.collections.ObservableList)" because "this.interpreterListView" is null; at view.ProgramSelectionController.initialize(ProgramSelectionController.java:79)" - the name of the ListView inside the class didn't match with the one in the .fxml file
2. other noticed problems:
- the ListView of interpreters shows addreses to Interpreter instance, since no toString function exists for this class, but I figuredout earlier that that would happen so I dealt with it, I just need to uncomment the toString() for the Interpreter class

...


## 2022.01.11 23:02
1. moved logs from Interpreter.java to `updates.md`
2. removed comments of old main() from Interpreter.java
3. Added a way to keep track of the original Statement of the latest ProgramState set to a Repository instance
- add: void IRepository.setNewProgram(ProgramState)
- add: IStatement IRepository.getLatestOriginalStatement()
    - add: private IStatement Repository.latestOriginalStatement
    - add: public void Repository.setNewProgram(ProgramState)
    - add: public IStatement Repository.getLatestOriginalStatement()
- add: public void Interpreter.setNewProgram(IStatement)
4. ProgramSelectionController
- changed: private ListView<IStatement> programsListListView to private ListView<Interpreter> interpretersListView
- add: private void interpretersSetUp() to prepare the interpretors for initialize()
- call interpreterSetUp() inside initialize() in order to set them up
- replace `public void handleLoadButtonAction(ActionEvent)` with calling setOnAction() on LoadButton inside initialize()
- 



# A5 changes:
## 2021.11.22 21:15
      - added ProgramState.prettyPrintFunctional(String, String), but it prints the outList first
      - added a RunMultipleCommand whose constructor receives as arguments a map of commands
      - added GarbageCollector.getAddressesFromSymbolTable(Collection<Value>, IHeap<Integer, Value>) which goes in depth for every RefValue found in the SymbolTable



# A4 changes:
## 2021.11.16 ~23:54
      - clearing the log file is now done inside the program, in the logProgramState method from repository
      - CompoundStatement.toString no longer prints brackets "()



# A3 changes:
## 2021.11.13 ~01:20:00
      Fixed:
      - referencing null pointer inside readFileStatement.execute() by checking the result of BufferedReader.nextLine()

 2021.11.13 01:23:00
      Fixed:
      - improper logProgramStatement Handling for right interpreter arguments (added break; in the switch for passing arguments)


