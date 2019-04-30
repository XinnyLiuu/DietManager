package view;

import controller.LogController;
import javafx.application.Platform;
import model.exercise.ExerciseCollection;
import model.food.FoodCollection;
import model.log.LogCollection;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Alert.*;
import javafx.geometry.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.List;

/**
 * The view that enables users to log foods
 *
 * @author Team A
 */
public class LogFoodPanel extends FlowPane implements EventHandler<ActionEvent>, Observer {
   private LogController lController;

   private TextField tfServings;
   private Button btnLog, btnDelete, btnList;
   private ComboBox cbFoods; // Think HTML select of foods from model

   private NutritionTrackerPanel nPanel;

   public LogFoodPanel(FoodCollection f, LogCollection l, ExerciseCollection e, NutritionTrackerPanel n) {
   	  // Layout
      super(10,10);
      this.setAlignment( Pos.CENTER );
   
   	  // Prepare controller
      this.lController = new LogController( f, l, e );
   
      this.nPanel = n;
   
      f.addObserver(this);
   
   	  // Create components
      this.tfServings = this.createTextField( tfServings, "Servings - ie.) 1.0" );
      this.btnLog = this.createButton( btnLog, "Log" );
      this.btnDelete = this.createButton( btnDelete, "Delete" );
      this.btnList = this.createButton( btnList, "Logged Foods" );
   
   	  // Creates ComboBox (HTML Select)
      this.cbFoods = new ComboBox();
      this.populateComboBox();
   
   	  // Add components
      this.getChildren().addAll(
         cbFoods,
         tfServings,
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
      ArrayList<String> foodNames = (ArrayList<String>)lController.getFoodData();
      foodNames.add(0, "-- Select a Food --");
      this.cbFoods.setItems( FXCollections.observableList( foodNames ) );
      this.cbFoods.getSelectionModel().select(0);
   }

   /**
    * Creates an Alert
    *
    * @param type
    * @param text
    * @return Alert
    */
   private Alert createAlert(AlertType type, String text) {
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
         case "Logged Foods":
            doListFood();
            break;
      }
   }

   /**
    * Fires when the `Log` button is clicked and logs a food
    */
   private void doLog() {
   	  // Get currently selected date
      String date = nPanel.getCurrDate();
   
   	  // Get entered food name
      String name = "";
      if(cbFoods.valueProperty().getValue() != null) {
         name = cbFoods.valueProperty().getValue().toString();
      }

      // Check for default
      if(name.equals("-- Select a Food --")) {
         createAlert(Alert.AlertType.WARNING, "Please choose a food to log!").showAndWait();
         return;
      }
   
   	  // Get entered servings
      String servings = tfServings.getText().trim();
   
      // Log the food and get the output string
      String errorString = lController.logFood(date, name, servings);
      if (errorString.equals("")) {
         createAlert(AlertType.INFORMATION, name + " logged successfully").showAndWait();
      }
      else {
         createAlert(AlertType.WARNING, errorString).showAndWait();
      }
   }
   

   /**
    * List all the logged foods when the `Logged Food` button is clicked
    */
   private void doListFood(){
   	  // Get currently selected date
      String date = nPanel.getCurrDate();

      // Get Food Data
      List<String> foodData = lController.getFoodData(date);
      String list = "";
   
      if(foodData != null) { // Checks if there is any logged Food
	      for(int i=0; i<foodData.size(); i+=2) {
		      list += foodData.get(i) + " for " + foodData.get(i+1) + " servings\n";
	      }
         createAlert(AlertType.INFORMATION, list).showAndWait(); // Displays logged food
      }
      else {
         createAlert(AlertType.WARNING,"No logged food for the selected date").showAndWait();
      }   
   }

   /**
    * Fires when the delete button is clicked, removes an entry the logs using today's date
    */
   private void doDelete() {
   	  // Get currently selected date
      String date = nPanel.getCurrDate();
   
   	  // Get entered food name
      String name = "";
      if(cbFoods.valueProperty().getValue() != null) {
         name = cbFoods.valueProperty().getValue().toString();
      };
   
   	  // Get entered servings
      String servings = tfServings.getText().trim();
   
      // Remove the food and get the output string
      String errorString = lController.removeFoodLog(date, name, servings);
      if (errorString.equals("")) {
         createAlert(AlertType.INFORMATION, name + " removed successfully").showAndWait();
      }
      else {
         createAlert(AlertType.WARNING, errorString).showAndWait();
      }
   }
}
