package view;

import interpreter.Interpreter;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.state.ProgramState;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.net.URL;
import java.util.*;


public class ProgramExecutionController implements Initializable {
    private final Interpreter interpreter;

    @FXML
    private TextField numberOfActiveProgramStatesTextField;

    @FXML
    private Button oneStepButton;
    @FXML
    private Button allStepButton;

    @FXML
    private TableView<Map.Entry<String, Value>> symbolTableTableView;
    @FXML
    private TableColumn<Map.Entry<String, Value>, String> symbolTableTableViewSymbolColumn;
    @FXML
    private TableColumn<Map.Entry<String, Value>, String> symbolTableTableViewValueColumn;
    
    @FXML
    private TableView<Map.Entry<Integer, Value>> heapTableTableView;
	@FXML
	private TableColumn<Map.Entry<Integer, Value>, String> heapTableTableViewAddressColumn;
	@FXML
	private TableColumn<Map.Entry<Integer, Value>, String> heapTableTableViewValueColumn;
    
    @FXML
    private TableView<Map.Entry<StringValue, BufferedReader>> fileTableTableView;
	@FXML
	private TableColumn<Map.Entry<StringValue, BufferedReader>, String> fileTableTableViewIDColumn;
	@FXML
	private TableColumn<Map.Entry<StringValue, BufferedReader>, String> fileTableTableViewFileColumn;

    @FXML
    private ListView<String> executionStackListView;
    @FXML
    private ListView<String> outListListView;

    @FXML
    private ListView<String> programStateIDsListView;

    public ProgramExecutionController(Interpreter interpreter) {
        this.interpreter = interpreter;
    }


    private void update() {
        // note: they are ordered left to right, top to bottom (kind of)
//        setNumberOfActiveProgramStatesTextField();
//        setProgramStateIDsListView();
//        programStateIDsListView.getSelectionModel().selectFirst();
//        if (programStateIDsListView.getSelectionModel().getSelectedItem() == null) {
//        }
//        setSymbolTableTableViewAndExecutionStackListView();
//        setHeapTableTableView();
//        setFileTableTableView();
//        setOutListListView();

        setNumberOfActiveProgramStatesTextField();
        setHeapTableTableView();
        setOutListListView();
        setFileTableTableView();
        setProgramStateIDsListView();
        if (programStateIDsListView.getSelectionModel().getSelectedItem() == null) {
            programStateIDsListView.getSelectionModel().selectFirst();
        }
        setSymbolTableTableViewAndExecutionStackListView();
    }

    private void setNumberOfActiveProgramStatesTextField() {
        numberOfActiveProgramStatesTextField.setText(
                Integer.toString((int) interpreter.getProgramStateList()
                        .stream()
                        .filter(ProgramState::isNotCompleted) // TODO: is this filter required? or is it correct?
                        .count()));
    }

    private void setProgramStateIDsListView() {
        programStateIDsListView.refresh();
        ObservableList<String>  threadIDStringObservableList = FXCollections.observableArrayList();
        interpreter.getProgramStateList().stream()
                .filter(ProgramState::isNotCompleted)
                .map(ProgramState::getThreadID)
                .map(String::valueOf)
                .forEach(threadIDStringObservableList::add);
        programStateIDsListView.setItems(threadIDStringObservableList);
    }

    private void setSymbolTableTableViewAndExecutionStackListView() {
        symbolTableTableView.refresh();
        executionStackListView.refresh();
        // TODO
//         for which Symbol Table?
//         I believe we need single selection for the ProgramStateIDsListView, but in the template i didn't see one yet
//
//         if I understood well from the template, setCellValueFactory does exactly what it says, it tells the
//         ListView what to take from an Item and put into the column's cell, aka a mapping
        // TODO
        //  WAIT: can the call to setCellValueFactory be put in initialize(). I should try it out, but also Ask
        //          what if, for every ObservableList<> given to setItems, we need to set a new CellValueFactory?

//         UPDATE: I have no idea how this is done
//         UPDATE 2: it's 04:45, and I realized that I wrote setCellFactory() instead of setCellValueFactory()
//         when a Cell in the symbolTableTableViewSymbolColumn needs to be updated, then update it with the value of getKey() of
//          the object stored on that row of the TableView
        symbolTableTableViewSymbolColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
        symbolTableTableViewValueColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));

        Runnable clearExecutionStackListView = () -> { executionStackListView.setItems(FXCollections.observableList(new ArrayList<>()));};

        String selectedStateID = programStateIDsListView.getSelectionModel().getSelectedItem();
        if (selectedStateID == null) {
            if (interpreter.getAnyProgramState().isEmpty()) clearExecutionStackListView.run();
            return;
        }

        //         stream the Program States from the ListView,
        interpreter.getProgramStateList().stream()
                // filter the Program States by having the threadID equal to the threadID of the selected Program
                .filter(programState -> programState.getThreadID() == Integer.parseInt(selectedStateID))
                // take the first one,
                .limit(1)
                // and, if it exists, otherwise clear the executionStackListView
                .findAny()
                .ifPresentOrElse(
                        programState -> {
                          //System.out.println("setSymbolTableTableViewAndExecutionStackListView(): found present ID" + programState.getThreadID());
                            symbolTableTableView.setItems(FXCollections.observableList(programState.getSymbolTable().getContent().entrySet().stream().toList()));
                            executionStackListView.setItems(FXCollections.observableList(Arrays.asList(programState.getExecutionStack().toString().split("\n"))));
                        },
                        clearExecutionStackListView
                );
    }

    private void setHeapTableTableView() {
        heapTableTableView.refresh();
        heapTableTableViewAddressColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey().toString()));
        heapTableTableViewValueColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));

        interpreter.getAnyProgramState().ifPresent(programState -> {
            heapTableTableView.setItems(FXCollections.observableList(programState.getHeap().getContent().entrySet().stream().toList()));
        });
    }

    private void setFileTableTableView() {
        fileTableTableView.refresh();
        fileTableTableViewIDColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey().toString()));
        fileTableTableViewFileColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));

        interpreter.getAnyProgramState().ifPresent(programState -> {
            fileTableTableView.setItems(FXCollections.observableList(programState.getFileTable().getContent().stream().toList()));
        });
    }

    private void setOutListListView() {
        outListListView.refresh();
        interpreter.getAnyProgramState().ifPresent(programState -> {
            List<String> stringList = Arrays.stream(programState.getOutList().toString().split("\n")).toList();
            outListListView.setItems(FXCollections.observableList(stringList));
        });
    }

    private static void alert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Button confirm = (Button) alert.getDialogPane().lookupButton( ButtonType.OK );
        confirm.setDefaultButton(false);
        confirm.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        alert.showAndWait();
    }

    private void oneStep() {
        // alert the user when the execution is done
        if (interpreter.isFinished()) {
            alert(Alert.AlertType.INFORMATION, "Current Program finished", null, "One step successfully finished!");
        }
        else {
            // execute one step
            interpreter.oneStepForAllPrograms();
        }
        update();
    }

    @FXML
    public void handleOneStepButtonAction(ActionEvent actionEvent) {
//        System.out.println("1 Step Button Pressed");
        oneStep();
    }

    @FXML
    public void handleAllStepButtonAction(ActionEvent actionEvent) {
//        System.out.println("All Step Button Pressed");
        
        // pressing All Step on an already completed program
        if (interpreter.isFinished()) {
            alert(Alert.AlertType.INFORMATION, "Current Program finished", null, "Program execution is already finished");
            return;
        }
        // run All Step Execution
        while (interpreter.isNotFinished()) {
            oneStep();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // update the views
        update();

        // call the button handler to update the SymbolTableTableView and the HeapTableTableView when an Item inside
        // ListView is clicked
        programStateIDsListView.setOnMouseClicked(mouseEvent -> {this.setSymbolTableTableViewAndExecutionStackListView();});

    }
}
