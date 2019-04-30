import controller.IOHandler;
import model.exercise.ExerciseCollection;
import model.food.FoodCollection;
import model.log.LogCollection;
import view.*;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * Diet Manager is an application that seeks to help
 * users pursue their diet goals by keeping them informed on what they are eating daily. Users of the
 * application can set their daily caloric limit, add foods, log foods and view their daily summaries to
 * make it more easy to watch their daily intake and balance their nutrition.
 *
 * This class will act as the Main for the entire application.
 *
 * @author Team A
 */
public class DietManager extends Application implements EventHandler<ActionEvent> {
	private static FoodCollection fCollection = new FoodCollection();
	private static ExerciseCollection eCollection = new ExerciseCollection();
	private static LogCollection lCollection = new LogCollection();

	private static IOHandler ioHandler = new IOHandler(fCollection, lCollection, eCollection);

	private Stage stage;
	private Scene scene;
	private Button btnSave; // Button to allow User's to save their entries for the day
	private VBox root = new VBox(); // The main container that holds every other view
	private Label lblLogFood, lblLogExercise, lblFood, lblRecipe, lblExercise; // Labels for each section

	private NutritionTrackerPanel nPanel;

	/**
	 * Starts JavaFx
	 * @param stage
	 */
	public void start(Stage stage) {
		// GUI setup
		this.stage = stage;
		this.stage.setTitle("Diet Manager V2.0");

		// Save button
		this.btnSave = new Button("Save & Quit");
		this.btnSave.setOnAction(this);

		// Labels
		this.lblLogFood = new Label("Log a Food");
		this.lblLogFood.setFont( new Font(16) );
		this.lblLogExercise = new Label("Log an Exercise");
		this.lblLogExercise.setFont( new Font(16) );
		this.lblFood = new Label("Create a Food");
		this.lblFood.setFont( new Font(16) );
		this.lblRecipe = new Label("Create a Recipe");
		this.lblRecipe.setFont( new Font(16) );
		this.lblExercise = new Label("Create an Exercise");
		this.lblExercise.setFont( new Font(16) );

		// Nutrition Panel
		this.nPanel = new NutritionTrackerPanel( fCollection, lCollection, eCollection );

		// Root configs
		this.root.setSpacing( 10 );
		this.root.setAlignment(Pos.CENTER);
		this.root.getChildren().addAll(
				nPanel,
				nPanel.getNutrientBarChart(),
				nPanel.getCalorieBarChart(),
				lblLogFood,
				new LogFoodPanel( fCollection, lCollection, eCollection, nPanel ),
				lblLogExercise,
				new LogExercisePanel( fCollection, lCollection, eCollection, nPanel ),
				lblFood,
				new CreateFoodPanel( fCollection ),
				lblRecipe,
				new CreateRecipePanel( fCollection ),
				lblExercise,
				new CreateExercisePanel( eCollection ),
				btnSave
		);
		this.root.setMargin(btnSave, new Insets(0, 0, 10, 0)); // top, right, bottom, left

		// Show GUI
		this.scene = new Scene(root, 950, 800); // W and H
		this.stage.setScene( scene );
		this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() { // On close event
			@Override
			public void handle(WindowEvent e) {
				saveAndClose();
			}
		});
		this.stage.show();
	}

	/**
	 * Event listener for 'Save & Quit' button
	 *
	 * @param actionEvent
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		// Get button name
		Button btn = (Button) actionEvent.getSource();

		switch(btn.getText()) {
			case "Save & Quit":
				saveAndClose();
				break;
		}
	}

	/**
	 * Save all data to CSV files and close the application
	 */
	public void saveAndClose() {
		// Saves weight and calorie for the day
		nPanel.doSavePreferences();

		// Writes all data back into csv files
		ioHandler.write();

		// Success alert
		new Alert(Alert.AlertType.INFORMATION, "Saved! Diet Manager will now close...").showAndWait();
		System.exit(1);
	}

	/**
	 * When this program runs:
	 *
	 * 1.) Start the read of csv files
	 * 2.) Start the CLI
	 * 3.) Start the GUI
 	 */
	public static void main(String[] args) {
		System.out.println("Starting Diet Manager V2.0...");

		// Read in csv files to load models
		ioHandler.read();

		// Start both CLI and JavaFX in parallel
		Thread cli = new Thread() {
			public void run() {
				// Start CLI
				new DietManagerCLI(fCollection, eCollection, lCollection).main(args);
			}
		};
		cli.start();

		// Start GUI
		launch(args);
	}
}
