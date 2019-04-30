package controller;

import model.exercise.Exercise;
import model.exercise.ExerciseCollection;
import model.food.*;
import model.log.LogCollection;

import java.io.*;
import java.util.*;

/**
 * Handles the read/write of data to/from food.csv and log.csv
 *
 * @author Team A
 */
public class IOHandler {
	// Files
	public static final String FOOD_CSV = "food.csv";
	public static final String EXERCISE_CSV = "exercise.csv";
	public static final String LOG_CSV = "log.csv";

	// I/O
	private Scanner scn;
	private PrintWriter pw;

	private FoodFactory fFactory;
	private FoodCollection fCollection;
	private ExerciseCollection eCollection;
	private LogCollection lCollection;

	public IOHandler(FoodCollection f, LogCollection l, ExerciseCollection e) {
		// Get models
		this.fCollection = f;
		this.lCollection = l;
		this.eCollection = e;
		this.fFactory = new FoodFactory();
	}

	/**
	 * Reads in the data from the csv files and prepares them at program execution
	 */
	public void read() {
		readFood();
		readExercise();
		readLog();
	}

	/**
	 * Writes data from models back into food.csv and log.csv
	 */
	public void write() {
		writeFood();
		writeExercise();
		writeLog();
	}

	/**
	 * Reads from food.csv
	 */
	private void readFood() {
		String line = ""; // Holds current line

		// Read food.csv
		try {
			scn = new Scanner( new FileInputStream( FOOD_CSV ) );
			Map<IFood, Double> ingredientsByServing;

			while(scn.hasNextLine()) {
				line = scn.nextLine();

				// Split the line into values separated by commas
				String[] values = line.split(",");

				if(values.length > 1) {
					// Recipe needs to be processed differently from BasicFood
					if(values[0].equals("r")) {
						ingredientsByServing = new HashMap<>();

						// Get the objects of the ingredients
						for(int i=2; i<values.length; i+=2) {
							String name = values[i].toLowerCase(); // Name of ingredient
							String serving = values[i+1]; // Serving size of ingredient
							IFood ingredient =  fCollection.getCollection().get(name); // Ingredient food object

							if(ingredient != null) {
								ingredientsByServing.put(
										ingredient, Double.parseDouble( serving )
								);
							}
						}

						IFood food = fFactory.createFood( values, ingredientsByServing );
						fCollection.addFood( food.getName().toLowerCase(), food );
					}
					else {
						ingredientsByServing = new HashMap<>();
						IFood food = fFactory.createFood( values, ingredientsByServing );
						fCollection.addFood( food.getName().toLowerCase(), food );
					}
				}
			}
			// Close scanner
			scn.close();
		}
		catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.exit( 1 );
		}
	}

	/**
	 * Reads from exercise.csv
	 */
	private void readExercise() {
		String line = ""; // Holds current line

		// Read exercise.csv
		try {
			scn = new Scanner( new FileInputStream( EXERCISE_CSV ) );

			while(scn.hasNextLine()) {
				line = scn.nextLine();

				// Split the line into values separated by commas
				String[] values = line.split(",");

				if(values.length > 1) {
					// Check for "e"
					if(values[0].equals("e")) {
						// Build new exercise Object
						String name = values[1];
						Double caloriesPerHour = Double.parseDouble(values[2]);
						Exercise exercise = new Exercise(name, caloriesPerHour);

						// Add exercise to hashmap
						eCollection.addExercise( exercise.getName().toLowerCase(), exercise );
					}
				}
			}

			// Close scanner
			scn.close();
		}
		catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.exit( 1 );
		}
	}

	/**
	 * Reads from log.csv
	 */
	private void readLog() {
		String line = "";

		// Read log.csv
		try {
			scn = new Scanner( new FileInputStream( LOG_CSV ) );

			while(scn.hasNextLine()) {
				line = scn.nextLine();

				String[] values = line.split(",");

				if(values.length > 1) {
					// Get the date as a String
					String date = values[0] + "-" + values[1] + "-" + values[2]; // yyyy-mm-dd

					// Check the type of log
					if(values[3].equals("w")) {
						// Weight
						lCollection.addWeightLog( date, Double.parseDouble(values[4]) );
					}
					else if(values[3].equals("c")) {
						// Calorie
						lCollection.addCalorieLimit( date, Double.parseDouble(values[4]));
					}
					else if(values[3].equals("f")) {
						// Food
						values[4] = values[4].toLowerCase();
						lCollection.addFoodLog( date, values[4], values[5] );
					}
					else if(values[3].equals("e")) {
						// Food
						values[4] = values[4].toLowerCase();
						lCollection.addExerciseLog( date, values[4], values[5] );
					}
				}
			}
			// Close scanner
			scn.close();
		}
		catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.exit( 1 );
		}
	}

	/**
	 * Writes data back into food.csv
	 */
	private void writeFood() {
		// Write to food.csv
		StringBuilder s = new StringBuilder();

		try {
			pw = new PrintWriter( new FileOutputStream( FOOD_CSV ) );

			// Iterate all basic foods and save into food.csv file
			for(IFood f : fCollection.getCollection().values()) {
				// Save the basic food objects first
				if(f instanceof BasicFood) {
					s.append(
							String.format("b,%s,%s,%s,%s,%s%n", f.getName(), ((BasicFood) f).getCalories(), ((BasicFood) f).getFat(), ((BasicFood) f).getCarb(), ((BasicFood) f).getProtein() )
					);
				}
			}

			// Iterate all recipes and save into food.csv file
			for(IFood f : fCollection.getCollection().values()) {
				if(f instanceof Recipe) {
					s.append(
							String.format("r,%s,", f.getName())
					);

					// Get all ingredients and their serving sizes
					for(IFood i : ((Recipe) f).getIngredients().keySet()) {
						s.append(
								String.format("%s,%s,", i.getName(), ((Recipe) f).getIngredients().get(i))
						);
					}

					s.setLength(s.length()-1); // Remove trailing comma
					s.append("\n");
				}
			}

			s.setLength(s.length()-1); // Remove trailing line break

			// Write file
			pw.println( s );
			pw.flush();
			pw.close();
		}
		catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.exit( 1 );
		}
	}

	/**
	 * Writes data back into exercise.csv
	 */
	private void writeExercise() {
		// Write to exercise.csv
		StringBuilder s = new StringBuilder();

		try {
			pw = new PrintWriter( new FileOutputStream( EXERCISE_CSV ) );

			// Iterate all exercises and save into exercise.csv file
			for(Exercise exercise : eCollection.getAllExercises()) {
				s.append(
						String.format("e,%s,%.1f %n", exercise.getName(), exercise.getCaloriesPerHour())
				);
			}

			s.setLength(s.length()-1); // Remove trailing line break

			// Write file
			pw.println( s );
			pw.flush();
			pw.close();
		}
		catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.exit( 1 );
		}
	}

	/**
	 * Writes data back into log.csv
	 */
	private void writeLog() {
		// Write to log.csv
		StringBuilder s = new StringBuilder();

		try {
			pw = new PrintWriter( new FileOutputStream( LOG_CSV ) );

			// Get all log data
			Map<String, List<String>> loggedFoods = lCollection.getFoodLogCollection();
			Map<String, List<String>> loggedExercises = lCollection.getExerciseLogCollection();
			Map<String, Double> loggedWeights = lCollection.getWeightLogCollection();
			Map<String, Double> loggedCalories = lCollection.getCalorieLogCollection();

			// Save all logged weights
			for(String d : loggedWeights.keySet()) {
				// Parse date back into yyyy,mm,dd format
				String[] dates = d.split("-");

				s.append(
						String.format("%s,%s,%s,w,", dates[0], dates[1], dates[2])
				);

				s.append( String.format("%s%n", loggedWeights.get(d)) );
			}

			// Save all logged calories
			for(String d : loggedCalories.keySet()) {
				// Parse date back into yyyy,mm,dd format
				String[] dates = d.split("-");

				s.append(
						String.format("%s,%s,%s,c,", dates[0], dates[1], dates[2])
				);

				s.append( String.format("%s%n", loggedCalories.get(d)) );
			}

			// Save all logged foods
			for(String d : loggedFoods.keySet()) {
				// Create an ArrayList of the foods by servings for the day
				List<String> foodsByServings = new ArrayList<>( loggedFoods.get(d) );

				// Iterate through the list
				for(int i=0; i < foodsByServings.size(); i+=2) {
					// Parse date back into yyyy,mm,dd format
					String[] dates = d.split("-");

					s.append(
							String.format("%s,%s,%s,f,", dates[0], dates[1], dates[2])
					);

					s.append( String.format("%s,%s", foodsByServings.get(i), foodsByServings.get(i+1)) );

					s.append("\n");
				}
			}

			// Save all logged exercises
			for(String d : loggedExercises.keySet()) {
				// Create an ArrayList of all exercises for the day
				List<String> exercisesAndDurations = new ArrayList<>( loggedExercises.get(d) );

				// Iterate through the list
				for(int i=0; i<exercisesAndDurations.size(); i+=2) {
					// Parse date back into yyyy,mm,dd format
					String[] dates = d.split("-");

					s.append(
							String.format("%s,%s,%s,e,", dates[0], dates[1], dates[2])
					);

					s.append( String.format("%s,%s\n", exercisesAndDurations.get(i), exercisesAndDurations.get(i+1)) );
				}
			}

			s.setLength(s.length()-1); // Remove trailing line break

			// Write file
			pw.print( s );
			pw.flush();
			pw.close();
		}
		catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.exit(1);
		}
	}
}
