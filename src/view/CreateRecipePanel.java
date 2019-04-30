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
 * The view that enables users to create new recipe item by entering nutrition information
 *
 * @author Team A
 */
public class CreateRecipePanel extends FlowPane implements EventHandler<ActionEvent> {
	private FoodController foodController;

	private TextField tfName;
	private TextField tfIngredients;
	private Button btnAdd;

	public CreateRecipePanel(FoodCollection f) {
		// Layout
		super(10, 10);
		this.setAlignment( Pos.CENTER );

		// Prepare controller
		this.foodController = new FoodController( f );

		// Create components
		this.tfName = this.createTextField( tfName, "Name - ie.) Hot Dog Bun" );
		this.tfName.setPrefWidth(200);
		this.tfIngredients = this.createTextField( tfIngredients, "Ingredients - ie.) hot dog bun,1.0,hot dog meat,1.0" );
		this.tfIngredients.setPrefWidth(500);

		this.btnAdd = this.createButton( btnAdd, "Create Recipe" );

		// Add components
		this.getChildren().addAll(
				tfName,
				tfIngredients,
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
			case "Create Recipe" :
				doCreateRecipe();
				break;
		}
	}

	/**
	 * Fires when the "Create Recipe" button is clicked and adds the recipe to the collection
	 */
	private void doCreateRecipe() {
		// Get name
		String name = tfName.getText().trim().toLowerCase();

		// Get ingredients
		String ingredients = tfIngredients.getText().trim().toLowerCase();

		// Add the recipe
		String errorString = foodController.addRecipe( name, ingredients, false );

		if(errorString.equals("")){
			createAlert(Alert.AlertType.INFORMATION,String.format("%s has been created!", this.tfName.getText())).showAndWait() ;
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
				String res = foodController.addRecipe(name, ingredients, true);
				if(res.equals("")) {
					createAlert(Alert.AlertType.INFORMATION, String.format("%s has been updated!", name)).showAndWait();
				}
				else {
					createAlert(Alert.AlertType.ERROR, errorString).showAndWait() ;
				}
			}
		}
		else {
			createAlert(Alert.AlertType.ERROR, errorString).showAndWait() ;
		}
	}
}
