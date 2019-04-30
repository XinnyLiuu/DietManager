package view;

import controller.FoodController;
import model.food.FoodCollection;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import java.util.Optional;

/**
 * The view that enables users to create new food item by entering nutrition information
 *
 * @author Team A
 */
public class CreateFoodPanel extends FlowPane implements EventHandler<ActionEvent> {
	private FoodController foodController;

	private TextField tfName, tfCalories, tfFat, tfCarb, tfProtein;
	private Button btnAdd;

	public CreateFoodPanel(FoodCollection f) {
		// Layout
		super(10, 10);
		this.setAlignment( Pos.CENTER );

		// Prepare controller
		this.foodController = new FoodController( f );

		// Create components
		this.tfName = this.createTextField( tfName, "Name - ie.) Hot Dog" );
		this.tfCalories = this.createTextField( tfCalories, "Calories - ie.) 100.0" );
		this.tfFat = this.createTextField( tfFat, "Fat - ie.) 12.0" );
		this.tfCarb = this.createTextField( tfCarb, "Carb - ie.) 25.0" );
		this.tfProtein = this.createTextField( tfProtein, "Protein - ie.) 5.0" );

		this.btnAdd = this.createButton( btnAdd, "Create Food" );

		// Add components
		this.getChildren().addAll(
			tfName,
			tfCalories,
			tfFat,
			tfCarb,
			tfProtein,
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
		tf.setPrefWidth(150);
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
			case "Create Food":
				doCreateFood();
				break;
		}
	}

	/**
	 * Fires when "Create Food" button is clicked, adds a BasicFood to collection
	 */
	private void doCreateFood() {
		// Get name
		String name = tfName.getText().trim().toLowerCase();

		// Get calories
		String calories = tfCalories.getText().trim();

		// Get fat
		String fat = tfFat.getText().trim();

		// Get carb
		String carb = tfCarb.getText().trim();

		// Get protein
		String protein = tfProtein.getText().trim();

		// Add the BasicFood
		String errorString = foodController.addBasicFood(name, calories, fat, carb, protein, false);

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

				// Overwrite the food
				String res = foodController.addBasicFood(name, calories, fat, carb, protein, true);
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
