package view;

import controller.LogController;
import javafx.application.Platform;
import model.exercise.ExerciseCollection;
import model.food.FoodCollection;
import model.log.LogCollection;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.List;

/**
 * This view allows users to log, delete and view all exercises from their daily log
 *
 * @author Team A
 */
public class LogExercisePanel extends FlowPane implements EventHandler<ActionEvent>, Observer {
	private LogController lController;

	private TextField tfDuration;

	private Button btnLog, btnDelete, btnList;
	private ComboBox cbExercises;

	private NutritionTrackerPanel nPanel;

	public LogExercisePanel(FoodCollection f, LogCollection l, ExerciseCollection e, NutritionTrackerPanel n) {
		// Layout
		super(10,10);
		this.setAlignment( Pos.CENTER );

		// Prepare controller
		this.lController = new LogController( f, l, e );

		this.nPanel = n;

		e.addObserver(this);

		// Create exercise combo box
		this.cbExercises = new ComboBox();
		this.populateComboBox();

		// Create duration text field
		this.tfDuration = createTextField( tfDuration, "Duration - ie.) 1.0" );

		// Create Log and Delete Buttons
		this.btnLog = createButton( btnLog, "Log" );
		this.btnDelete = createButton( btnDelete, "Delete" );
		this.btnList = createButton( btnList, "Logged Exercises" );
		// Add components
		this.getChildren().addAll(
				cbExercises,
				tfDuration,
				btnLog,
				btnDelete,
				btnList
		);
	}

	/**
	 * Updates the UI
	 *
	 * @param o
	 * @param obj
	 */
	@Override
	public void update(Observable o, Object obj) {
		Platform.runLater(new Runnable() {
			public void run() {
				populateComboBox();
			}
		});
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
	 * Creates a ComboBox of all foods in FoodCollection
	 *
	 * @return
	 */
	private void populateComboBox() {
		ArrayList<String> exerciseNames = (ArrayList<String>)lController.getExerciseData();
		exerciseNames.add(0, "-- Select an Exercise --");

		this.cbExercises.setItems( FXCollections.observableList( exerciseNames ) );
		this.cbExercises.getSelectionModel().select(0);
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
	 * Handles actions on a button
	 *
	 * @param actionEvent
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		// Get button name
		Button btn = (Button) actionEvent.getSource();

		switch(btn.getText()) {
			case "Log":
				doLog();
				break;
			case "Delete":
				doDelete();
				break;
			case "Logged Exercises":
				doListExercise();
				break;
		}
	}

	/**
	 * Fires when the `Log` button is clicked and logs an exercise
	 */
	private void doLog() {
		// Get the date
		String date = nPanel.getCurrDate();

		// Get exercise
		String name = "";
		if(cbExercises.valueProperty().getValue() != null) {
			name = cbExercises.valueProperty().getValue().toString();
		}

		// Check for default
		if(name.equals("-- Select an Exercise --")) {
			createAlert(Alert.AlertType.WARNING, "Please choose an exercise to log!").showAndWait();
			return;
		}

		// Get duration
		String duration = tfDuration.getText().trim();

		// Log the exercise and get the output string
		String errString = lController.logExercise(date, name, duration);
		if(errString.equals("")) {
			createAlert(Alert.AlertType.INFORMATION, name + " logged successfully!").showAndWait();
		}
		else {
			createAlert(Alert.AlertType.WARNING, errString).showAndWait();
		}
	}

	/**
	 * List all the logged exercises when the `Logged Exercise` button is clicked
	 */
	private void doListExercise() {
		// Get currently selected date
		String date = nPanel.getCurrDate();

		// Get Exercise Data
		List<String> exerciseData = lController.getExerciseData(date);
		String list = "";

		if(exerciseData != null) { // Checks if there is any logged Exercise
			for(int i=0; i<exerciseData.size(); i+=2) {
				list += exerciseData.get(i) + " for " + exerciseData.get(i+1) + " minutes\n";
			}
			createAlert(Alert.AlertType.INFORMATION, list).showAndWait();

		}
		else {
			createAlert(Alert.AlertType.WARNING, "No logged exercises for the selected date!").showAndWait();
		}
	}

	/**
	 * Fires when the `Delete` button is clicked and removes a food from the log
	 */
	private void doDelete() {
		// Get the date
		String date = nPanel.getCurrDate();

		// Get exercise
		String name = "";
		if(cbExercises.valueProperty().getValue() != null) {
			name = cbExercises.valueProperty().getValue().toString();
		}

		// Get duration
		String duration = tfDuration.getText().trim();

		// Remove the exercise and get the output
		String errString = lController.removeExerciseLog(date, name, duration);
		if(errString.equals("")) {
			createAlert(Alert.AlertType.INFORMATION, name + " removed successfully").showAndWait();
		}
		else {
			createAlert(Alert.AlertType.WARNING, errString).showAndWait();
		}
	}
}
