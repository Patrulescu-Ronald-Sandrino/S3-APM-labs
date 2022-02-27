package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.FSUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage programExecutionStage) throws IOException {
        // copy the .fxml and .css files to classpath in order to have the latest version when they are required at run-time
        String programSelectionFXMLName = "ProgramSelectionLayout.fxml";
        FSUtils.copyFile("src/view/" + programSelectionFXMLName, "out/production/A7/view/" + programSelectionFXMLName);
        String programSelectionCSSName = "programSelection.css";
        FSUtils.copyFile("src/view/" + programSelectionCSSName, "out/production/A7/view/" + programSelectionCSSName);

        // set up the Program Selection Stage
        try {
            VBox programSelectionVBox = (VBox)FXMLLoader.load(Objects.requireNonNull(getClass().getResource(programSelectionFXMLName)));
            Scene programSelectionScene = new Scene(programSelectionVBox);
            Stage programSelectionStage = new Stage();
            programSelectionStage.setTitle("Program Selection");
            programSelectionStage.setScene(programSelectionScene);
            programSelectionStage.setOnCloseRequest(event -> System.exit(0));
            programSelectionStage.show();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
// VM Options for IntelliJ: --module-path /home/kamui/Programs/javafx-sdk-17.0.1/lib --add-modules javafx.controls,javafx.fxml
// compile + run:
// javafxc -d out/production/A7/ $(find src/ -name "*.java") && javafx -classpath out/production/A7/ view.Main
// jfxr out/production/A7/ src/ view.Main
