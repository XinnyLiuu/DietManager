package view;

import controller.NutritionTrackerController;
import model.exercise.ExerciseCollection;
import model.food.FoodCollection;
import model.log.LogCollection;

import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import java.time.LocalDate;
import java.util.*;

/**
 * The view that displays summary data to the user for the current day’s total nutritional values
 *
 * @author Team A
 */
public class NutritionTrackerPanel extends FlowPane implements EventHandler<ActionEvent>, Observer {
	private NutritionTrackerController nController;

	private DatePicker dp;
	private Button btnLoad;
	private Label lblDate, lblWeight, lblCalories;
	public TextField tfWeight, tfCalories;
	private Button btnSavePreferences;

	private GraphPanel nutrientGraph;
	private GraphPanel calorieGraph;

	public NutritionTrackerPanel(FoodCollection f, LogCollection l, ExerciseCollection e ) {
		// Layout
		super(10,10);
		this.setAlignment( Pos.CENTER );

		// Prepare controller
		this.nController = new NutritionTrackerController( f, l, e );

		// Create Date Selection
		this.lblDate = new Label("Date" );
		this.dp = new DatePicker( );
		this.dp.setValue( LocalDate.now() );

		// Create Load Button
		this.btnLoad = new Button( "Load" );
		this.btnLoad.setOnAction(this);

		//Create Weight Entry
		this.lblWeight = new Label("Weight");
		this.tfWeight = new TextField();
		this.tfWeight.setPromptText("ie.) 150.0");

		// Create Calories Entry
		this.lblCalories = new Label( "Calories" );
		this.tfCalories = new TextField();
		this.tfCalories.setPromptText( "ie.) 2000.0" );

		// Create Load Button
		this.btnLoad = new Button( "Load" );
		this.btnLoad.setOnAction(this);

		// Create Save Preferences Button
		this.btnSavePreferences = new Button( "Save Preferences" );
		this.btnSavePreferences.setOnAction(this);

		// Set up graphs and update them with current data
		this.nutrientGraph = new GraphPanel("Nutrients", "Grams", 3, "Total Nutrition Intake");
		this.calorieGraph = new GraphPanel("Calorie Information", "Calories", 4, "Calorie Information");

		//Add this as an observer of the Log Collection
		l.addObserver(this);

		// Set user values and update display
		this.doLoad();

		// Add components
		this.getChildren().addAll(
				lblDate,
				dp,
				btnLoad,
				lblWeight,
				tfWeight,
				lblCalories,
				tfCalories,
				btnSavePreferences
		);
	}

	@Override
	public void update(Observable observable, Object o) {
		Platform.runLater(new Runnable() { // MultiThreading
			@Override
			public void run() {
				updateGraphData();
			}
		});
	}

	/**
	 * Updates the nutrient graph with current nutrient values
	 */
	public void updateGraphData(){
		// Get date
		String date = this.getCurrDate();

		// Get new nutrient data from the controller and update the graph
		ArrayList<Double> nutrientValues = nController.calculateTotalConsumedNutrients( date );

		/**
		 * Update Calorie Graph
		 */
		Double desiredCalories = Double.parseDouble(this.tfCalories.getText());
		Double consumedCalories = nutrientValues.remove(0);
		Double burnedCalories = nController.calculateBurnedCalories( date );
		Double netCalories = consumedCalories - burnedCalories;

		ArrayList<Double> calorieValues = new ArrayList<>();
		calorieValues.addAll(Arrays.asList(desiredCalories, netCalories, consumedCalories, burnedCalories));

		// Set the titles with the calories number included
		ArrayList<String> calorieTitles = new ArrayList<>();

		calorieTitles.add("Desired Calories - " + String.format("%.2f", desiredCalories));
		calorieTitles.add("Net Calories - " + String.format("%.2f", netCalories));
		calorieTitles.add("Consumed Calories - " + String.format("%.2f", consumedCalories));
		calorieTitles.add("Burned Calories - " + String.format("%.2f", burnedCalories));

		this.calorieGraph.updateBarChartValues(calorieTitles, calorieValues);

		/**
		 * Update Nutrient Graph
		 */
		// Calculate total grams of nutrients
		Double total = 0.0;
		for(Double nutrient : nutrientValues){
			total += nutrient;
		}

		// Set the titles with the percentages calculated off of totals
		ArrayList<String> nutrientTitles = new ArrayList<>();
		if(total != 0) {
			Double fatPercentage = (nutrientValues.get(0) / total) * 100;
			Double carbPercentage = (nutrientValues.get(1) / total) * 100;
			Double proteinPercentage = (nutrientValues.get(2) / total) * 100;

			nutrientTitles.add("Fat - " + String.format("%.2f", fatPercentage) + "%");
			nutrientTitles.add("Carb - " + String.format("%.2f", carbPercentage) + "%");
			nutrientTitles.add("Protein - " + String.format("%.2f", proteinPercentage) + "%");
		}
		else {
			nutrientTitles.addAll(Arrays.asList("Fat - 0%", "Carb - 0%", "Protein - 0%"));
		}

		this.nutrientGraph.updateBarChartValues(nutrientTitles, nutrientValues);
	}

	/**
	 * Gets Nutrient bar chart from GraphPanel
	 *
	 * @return BarChart
	 */
	public BarChart getNutrientBarChart() {
		return nutrientGraph.getBarChart();
	}

	/**
	 * Gets Calorie bar chart from GraphPanel
	 *
	 * @return BarChart
	 */
	public BarChart getCalorieBarChart() {
		return calorieGraph.getBarChart();
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
	 * Returns the current date selected
	 *
	 * @return Date
	 */
	public String getCurrDate() {
		return dp.getValue().toString();
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
			case "Load":
				doLoad();
				break;
			case "Save Preferences":
				doSavePreferences();
				break;
		}
	}

	/**
	 * Fired when the 'Load' button is clicked, loads all values
	 */
	public void doLoad() {
		String date = this.getCurrDate();

		String userWeight = nController.getWeight( date );
		String desiredCalories = nController.getDesiredCalorie( date );

		tfWeight.setText( userWeight );
		tfCalories.setText( desiredCalories );

		this.doSavePreferences();

		this.updateGraphData();
	}

	/**
	 * Saves the user's current calorie limit and weight on 'Save Preference' button on click
	 */
	public void doSavePreferences(){
		String date = this.getCurrDate();

		this.nController.setLogCalorie(date, this.tfCalories.getText());
		this.nController.setLogWeight(date, this.tfWeight.getText());
	}
}
