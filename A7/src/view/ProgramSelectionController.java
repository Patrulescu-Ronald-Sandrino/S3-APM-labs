package view;

import interpreter.Interpreter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.adts.DictionaryWithClonableValues;
import model.state.ProgramState;
import model.statement.IStatement;
import repository.Repository;
import utils.FSUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProgramSelectionController implements Initializable {

    private List<Repository> repositoryList;
    private List<Interpreter> interpretersList;
    private String programExecutionFXMLName;

    public void setProgramExecutionFXMLName(String programExecutionFXMLName) {
        this.programExecutionFXMLName = programExecutionFXMLName;
    }

    @FXML
    private ListView<Interpreter> interpretersListView;

    @FXML
    private Button loadButton;

    private void interpretersSetUp() {
        List<Repository> repositoryList = new ArrayList<>();
        List<Interpreter> interpretersList = new ArrayList<>();

        List<IStatement> programs = IStatement.IStatementExamples.examples;
        int programsListIndex;
        int addedPrograms = 0; // used to give the log files the proper number
        for (programsListIndex = 0; programsListIndex < programs.size(); programsListIndex++) {
            IStatement program = programs.get(programsListIndex);

            try {
                program.typecheck(new DictionaryWithClonableValues<>());
            }
            catch (Exception exception) {
                String message = String.format("Typechecker failed for program: %s\nReason: %s", program.toString(), exception.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ProgramSelection Error");
                alert.setHeaderText("Type check for program " + programsListIndex + " (counting starts at 0) failed!");
                alert.setContentText("Reason: " + exception.getMessage());
                alert.showAndWait();
                boolean printTypeCheckerError = false;
                if (printTypeCheckerError) {
                    System.out.println("Type check for program " + programsListIndex + " (counting starts at 0) failed!");
                    System.out.println(program);
                    ProgramState programState = new ProgramState(program);
//                    System.out.println(programState.getExecutionStack());
                    System.out.println("Reason:");
                    exception.printStackTrace();
                    System.out.println();
                }
                continue;
            }

            ProgramState programState = new ProgramState(program);
            String logFilePath = String.format("log/log%02d.txt", addedPrograms++);

            Repository repository = new Repository(programState, logFilePath);
            repositoryList.add(repository);

            interpretersList.add(new Interpreter(repository));
        }
        // add all the repositories and interpreters to the instance's fields
        this.repositoryList = repositoryList;
        this.interpretersList = interpretersList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // copy the .fxml to the classpath, so it can be accessed during runtime
        this.setProgramExecutionFXMLName("ProgramExecutionLayout.fxml"); // the name of the .fxml file used for the ProgramExecution Stage
        try {
            FSUtils.copyFile("src/view/" + programExecutionFXMLName, "out/production/A7/view/" + programExecutionFXMLName);
        } catch (IOException ex) {
            System.out.println("[ProgramSelectionController.initialize()] Failed to copy " + programExecutionFXMLName);
            ex.printStackTrace();
        }

        // initialize interpreters and add them to the ListView
        interpretersSetUp();
        ObservableList<Interpreter> interpreterObservableList = FXCollections.observableArrayList(interpretersList);

        interpretersListView.setItems(interpreterObservableList);

        // set the ListView Selection Mode to SINGLE Selection
        interpretersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // set the handler for pressing the Load Button
        // note:
        //      this is redundant and stupid;
        //      I could have provided the code for the handleLoadButtonAction here OR
        //      I could have added to the `loadButton` in the .fxml file the property:
        //      onAction="#handleLoadButtonAction" and add an ActionEvent parameter to handleLoadButtonAction() function
        //      I just did it to have the both ways as close to each other as possible, for future reference
        loadButton.setOnAction(e -> handleLoadButtonAction());
    }

    public void handleLoadButtonAction() {
        // make sure the user made a selection
        // note:
        //      this could have been avoided by setting the selection of the ListView to the first Item,
        //      aka, by calling interpreterListView.getSelectionModel().selectFirst();
        int selectedProgramIndex = interpretersListView.getSelectionModel().selectedIndexProperty().get();
        if (selectedProgramIndex == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Program Selection Error");
//            alert.setHeaderText("You didn't select any program to load!");
            alert.setHeaderText(null);
            alert.setContentText("Please select a program before pressing the Load Button.");

            Button confirm = (Button) alert.getDialogPane().lookupButton( ButtonType.OK );
            confirm.setDefaultButton(false);
            confirm.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
            alert.showAndWait();
            return;
        }
        //System.out.println("Pressed Load on: " + interpretersListView.getItems().get(selectedProgramIndex));

        // create a factory to be used for instantiating the ProgramExecutionController
        Callback<Class<?>, Object> programExecutionControllerFactory =  type -> {
            if (type == ProgramExecutionController.class) {
                return new ProgramExecutionController(interpretersListView.getSelectionModel().getSelectedItem());
            }
            else {
                try {
                    return type.getDeclaredConstructor().newInstance(); // added .getDeclaredConstructor() to get rid of compiler warning
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    System.out.println("[ProgramSelectionController.initialize()] Failed to create controller for " + type.getName());
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }
        };

        // start the ProgramExecution Stage for the currently loaded program
        try {
            // load the .fxml for the ProgramExecution Stage
            FXMLLoader programExecutionLoader = new FXMLLoader(getClass().getResource(this.programExecutionFXMLName));
            programExecutionLoader.setControllerFactory(programExecutionControllerFactory);
            Parent parentOfProgramExecutionScene = programExecutionLoader.load(); // note that inside the Parent will be stored a GridPane instance

            // set up the Stage's Scene and the Stage
            Scene programExecutionScene = new Scene(parentOfProgramExecutionScene, 800, 750);
            Stage programExecutionStage = new Stage();
            programExecutionStage.setTitle("Program Execution");
            programExecutionStage.setScene(programExecutionScene);
            programExecutionStage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }



//    public void handleLoadButtonAction(ActionEvent actionEvent) {
//    public void handleLoadButtonAction(ActionEvent actionEvent) {
//        // TODO
//        int index = interpreterListView.getSelectionModel().selectedIndexProperty().get();
//        if (index != -1) {
//            System.out.println("Pressed Load on: " + interpreterListView.getItems().get(index).toString());
//            loadedProgram = interpreterListView.getItems().get(index);
//        }
//        else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("Please select a program before pressing load button");
//            alert.setHeaderText("You didn't select any program to load");
//            alert.setTitle("Load without selection");
//            alert.showAndWait();
//        }
//
//
////        TODO: what the hell is this below?
//                  EDIT: probably useful for the ProgramStateIDsListView
////        programsListListView.getSelectionModel().selectedItemProperty().addListener(e -> {
////            int index = programsListListView.getSelectionModel().getSelectedIndex();
////
////            // TODO notify ProgramExecutionController (or the SymbolTableTableView ?)
////        });
//    }
}
