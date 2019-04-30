package view;

import model.exercise.ExerciseCollection;
import controller.ExerciseController;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import java.util.Optional;

/**
 * This view allows the creation of exercises
 *
 * @author Team A
 */
public class CreateExercisePanel extends FlowPane implements EventHandler<ActionEvent> {
    private ExerciseController exerciseController;

    private TextField tfName, tfCalories;
    private Button btnAdd;

    public CreateExercisePanel(ExerciseCollection e) {
        // Layout
        super(10, 10);
        this.setAlignment( Pos.CENTER );

        // Prepare controller
        this.exerciseController = new ExerciseController( e );

        // Create components
        this.tfName = createTextField( tfName, "Name - ie.) Lawn Mowing" );
        this.tfCalories = createTextField( tfCalories, "Calories Burned - ie.) 100.0" );

        this.btnAdd = createButton( btnAdd, "Create Exercise" );

        // Add components
        this.getChildren().addAll(
                tfName,
                tfCalories,
                btnAdd
        );
    }

    /**
     * Creates a TextField
     *
     * @param tf
     * @param placeholder
     * @return TextField
     */
    private TextField createTextField(TextField tf, String placeholder) {
        tf = new TextField();
        tf.setPromptText( placeholder );
        tf.setPrefWidth(200);
        return tf;
    }

    /**
     * Creates a Button and attaches an EventHandler
     *
     * @param btn
     * @param text
     * @return Button
     */
    private Button createButton(Button btn, String text) {
        btn = new Button( text );
        btn.setOnAction(this);
        return btn;
    }

    /**
     * Creates an Alert
     *
     * @param type
     * @param text
     * @return Alert
     */
    private Alert createAlert(Alert.AlertType type, String text) {
        Alert alert = new Alert( type, text );
        return alert;
    }

    /**
     * Creates an Confirmation Alert with buttons
     *
     * @param type
     * @param header
     * @param buttons
     * @return Alert
     */
    private Alert createConfirmAlert(Alert.AlertType type, String header, ButtonType... buttons) {
        Alert alert = new Alert( type );
        alert.setHeaderText( header );
        alert.getButtonTypes().setAll( buttons );
        return alert;
    }

    /**
     * Handles actions on a button
     *
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        // Get button name
        Button btn = (Button) actionEvent.getSource();

        switch(btn.getText()) {
            case "Create Exercise":
                doCreateExercise();
                break;
        }
    }

    /**
     * Fires when the `Create Exercise` button is clicked and creates an exercise
     */
    public void doCreateExercise() {
        // Get the name
        String name = tfName.getText().toLowerCase().trim();

        // Get the calories per hour
        String caloriesPerHour = tfCalories.getText().trim();

        // Add the exercise
        String errorString = exerciseController.addExercise(name, caloriesPerHour, false);

        if(errorString.equals("")) {
            this.createAlert(Alert.AlertType.INFORMATION, String.format("%s has been created!", name)).showAndWait();
        }
        else if(errorString.contains("exists")) {
            // Show confirm dialog
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            Alert alert = createConfirmAlert( Alert.AlertType.CONFIRMATION, String.format("%s already exists, do you want to overwrite?", name), yes, no);

            // Check selected choice
            Optional<ButtonType> selected = alert.showAndWait();
            if(selected.get() == yes) {

                // Overwrite the exercise
                String res = exerciseController.addExercise(name, caloriesPerHour, true);
                if(res.equals("")) {
                    createAlert(Alert.AlertType.INFORMATION, String.format("%s has been updated!", name)).showAndWait();
                }
                else {
                    createAlert(Alert.AlertType.ERROR, errorString).showAndWait() ;
                }
            }
        }
        else {
            createAlert(Alert.AlertType.ERROR, errorString).showAndWait();
        }
    }
}
