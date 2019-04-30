import controller.*;
import model.exercise.ExerciseCollection;
import model.food.FoodCollection;
import model.log.LogCollection;

import javax.xml.bind.SchemaOutputResolver;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The CLI for DietManager, this does the same functionality as the GUI.
 *
 * @author Team A
 */
public class DietManagerCLI {
	private static FoodCollection fCollection;
	private static ExerciseCollection eCollection;
	private static LogCollection lCollection;

	private static IOHandler ioHandler;
	private static FoodController fController;
	private static LogController lController;
	private static ExerciseController eController;
	private static NutritionTrackerController nController;

	private static Scanner scn;

	public DietManagerCLI(FoodCollection f, ExerciseCollection e, LogCollection l) {
		this.fCollection = f;
		this.eCollection = e;
		this.lCollection = l;

		this.ioHandler = new IOHandler(fCollection, lCollection, eCollection);
		this.fController = new FoodController(fCollection);
		this.lController = new LogController(fCollection, lCollection, eCollection);
		this.eController = new ExerciseController(eCollection);
		this.nController = new NutritionTrackerController(fCollection, lCollection, eCollection);

		this.scn = new Scanner(System.in); // Read user's input
	}

	public static void main(String[] args) {
		// Start CLI
		while (true) {
			System.out.println("\nPlease enter the number for what you wish to do:");
			System.out.println("1) Log a food");
			System.out.println("2) Log an exercise");
			System.out.println("3) Delete a logged food");
			System.out.println("4) Delete a logged exercise");
			System.out.println("5) Log a weight");
			System.out.println("6) Log a calorie limit");
			System.out.println("7) Create a basic food");
			System.out.println("8) Create a recipe");
			System.out.println("9) Create an exercise");
			System.out.println("10) View all loggable foods");
			System.out.println("11) View all loggable exercises");
			System.out.println("12) View all logged foods");
			System.out.println("13) View all logged exercises");
			System.out.println("14) View Calorie Information");
			System.out.println("15) View Nutrition Information");
			System.out.println("16) Exit Application");



			int command; // Numbered command
			try {
				command = Integer.parseInt(scn.nextLine().trim());
			}
			catch (NumberFormatException nfe) {
				System.out.println("Sorry, that was not a valid selection. Please try again...\n");
				continue;
			}

			// Log food
			if(command == 1) {
				while (true) {
					System.out.println("Enter q to quit this command at any time");

					//Request name
					System.out.print("Name: ");
					String name = scn.nextLine().trim();
					if (name.equals("q")) {
						break;
					}

					//Request number of servings
					System.out.print("Servings: ");
					String servings = scn.nextLine().trim();
					if (servings.equals("q")) {
						break;
					}

					//Request date
					System.out.print("Date (yyyy-mm-dd): ");
					String date = scn.nextLine().trim();
					if (date.equals("q")) {
						break;
					}

					String errorString = lController.logFood(date, name, servings);

					//On failure, report and restart command
					if (!errorString.equals("")) {
						System.out.println("ERROR: " + errorString + "\n");
						continue;
					}

					//On success, exit this command
					break;
				}
			}

			// Log exercise
			else if(command == 2) {
				while (true) {
					System.out.println("Enter q to quit this command at any time");

					System.out.print("Name: ");
					String name = scn.nextLine().trim();
					if (name.equals("q")) {
						break;
					}

					System.out.print("Minutes: ");
					String duration = scn.nextLine().trim();
					if (duration.equals("q")) {
						break;
					}

					//Request date
					System.out.print("Date (yyyy-mm-dd): ");
					String date = scn.nextLine().trim();
					if (date.equals("q")) {
						break;
					}

					String errorString = lController.logExercise(date, name, duration);

					//On failure, report and restart command
					if (!errorString.equals("")) {
						System.out.println("ERROR: " + errorString + "\n");
						continue;
					}

					//On success, exit this command
					break;
				}
			}

			// Delete food
			else if(command == 3) {
				while (true) {
					System.out.println("Enter q to quit this command at any time");

					//Request name
					System.out.print("Name: ");
					String name = scn.nextLine().trim();
					if (name.equals("q")) {
						break;
					}

					//Request number of servings
					System.out.print("Servings: ");
					String servings = scn.nextLine().trim();
					if (servings.equals("q")) {
						break;
					}

					//Request date
					System.out.print("Date (yyyy-mm-dd): ");
					String date = scn.nextLine().trim();
					if (date.equals("q")) {
						break;
					}

					// Add log of calorie and display output
					String errorString = lController.removeFoodLog(date, name, servings);

					//On failure, report and restart command
					if (!errorString.equals("")) {
						System.out.println("ERROR: " + errorString + "\n");
						continue;
					}

					//On success, exit this command
					break;
				}
			}

			// Delete exercise
			else if(command == 4) {
				while (true) {
					System.out.println("Enter q to quit this command at any time");

					System.out.print("Name: ");
					String name = scn.nextLine().trim();
					if (name.equals("q")) {
						break;
					}

					System.out.print("Minutes: ");
					String duration = scn.nextLine().trim();
					if (duration.equals("q")) {
						break;
					}

					//Request date
					System.out.print("Date (yyyy-mm-dd): ");
					String date = scn.nextLine().trim();
					if (date.equals("q")) {
						break;
					}

					String errorString = lController.removeExerciseLog(date, name, duration);

					//On failure, report and restart command
					if(errorString.contains("duplicate entries")){
						System.out.println(errorString);
					}
					else if (!errorString.equals("")) {
						System.out.println("ERROR: " + errorString + "\n");
						continue;
					}

					System.out.println("Deleted " + duration + " minutes of " + name + " for " + date);
					//On success, exit this command
					break;
				}
			}

			// Log a weight
			else if(command == 5) {
				while (true) {
					System.out.println("Enter q to quit this command at any time");

					System.out.print("Weight: ");
					String weight = scn.nextLine().trim();
					if (weight.equals("q")) {
						break;
					}

					System.out.print("date (yyyy-mm-dd): ");
					String date = scn.nextLine().trim();
					if (date.equals("q")) {
						break;
					}

					String errorString = lController.logWeight(date, weight);

					//On failure, report and restart command
					if (!errorString.equals("")) {
						System.out.println("ERROR: " + errorString + "\n");
						continue;
					}

					//On success, exit this command
					break;
				}
			}

			// Log calorie limit
			else if(command == 6) {
				while (true) {
					System.out.println("Enter q to quit this command at any time");

					System.out.print("Desired Calorie Limit: ");
					String calories = scn.nextLine().trim();
					if (calories.equals("q")) {
						break;
					}

					System.out.print("date (yyyy-mm-dd): ");
					String date = scn.nextLine().trim();
					if (date.equals("q")) {
						break;
					}

					String errorString = lController.logCalorieLimit(date, calories);

					//On failure, report and restart command
					if (!errorString.equals("")) {
						System.out.println("ERROR: " + errorString + "\n");
						continue;
					}

					//On success, exit this command
					break;
				}
			}

			// Create a food
			else if(command == 7) {
				while(true) {
					System.out.println("Enter q to quit this command at any time");

					System.out.print("Name: ");
					String name = scn.nextLine().trim();
					if (name.equals("q")) {
						break;
					}

					System.out.print("Calories: ");
					String calories = scn.nextLine().trim();
					if (calories.equals("q")) {
						break;
					}

					System.out.print("Fat (g): ");
					String fat = scn.nextLine().trim();
					if (fat.equals("q")) {
						break;
					}

					System.out.print("Carbohydrates (g): ");
					String carb = scn.nextLine().trim();
					if (carb.equals("q")) {
						break;
					}

					System.out.print("Protein (g): ");
					String protein = scn.nextLine().trim();
					if (protein.equals("q")) {
						break;
					}

					String errorString = fController.addBasicFood(name, calories, fat, carb, protein, false);

					//Catch all non-collision errors
					if (!errorString.equals("") && !errorString.contains("already exists")) {
						System.out.println("ERROR: " + errorString + "...\n");
						continue;
					}

					//Collision error handling for overwrite functionality
					else if (errorString.contains("already exists")) {
						System.out.println(errorString);
						System.out.println("Would you like to overwrite? (y/n)");
						String response = scn.nextLine().trim();

						// Check response
						if (response.toUpperCase().equals("Y")) {
							fController.addBasicFood(name, calories, fat, carb, protein, true);

							if (!errorString.equals("") && !errorString.contains("already exists")) {
								System.out.println("ERROR: " + errorString + "...\n");
								continue;
							}
							break;
						}
						else {
							continue;
						}
					}

					//exit on success
					break;
				}
			}

			// Create a recipe
			else if(command == 8) {
				while(true) {
					System.out.println("Enter q to quit this command at any time");

					System.out.print("Name: ");
					String name = scn.nextLine().trim();
					if (name.equals("q")) {
						break;
					}

					int index = 0;
					String ingredientsList = "";
					System.out.println("Enter 'done' when you are finished entering ingredient information");
					while (true) {
						System.out.print("Ingredient #" + (index + 1) + ": ");
						String userEntry = scn.nextLine().trim();
						if (userEntry.equals("done")) {
							break;
						}
						ingredientsList += userEntry + ",";

						System.out.print("Servings for ingredient #" + (index + 1) + ": ");
						userEntry = scn.nextLine().trim();
						if (userEntry.equals("done")) {
							break;
						}
						ingredientsList += userEntry + ",";

						index++;
					}

					ingredientsList = ingredientsList.substring(0, ingredientsList.length() - 1);


					String errorString = fController.addRecipe(name, ingredientsList, false);

					//Catch all non-collision errors
					if (!errorString.equals("") && !errorString.contains("already exists")) {
						System.out.println("ERROR: " + errorString + "...\n");
						continue;
					}

					//Collision error handling for overwrite functionality
					else if (errorString.contains("already exists")) {
						System.out.println(errorString);
						System.out.println("Would you like to overwrite? (y/n)");
						String response = scn.nextLine().trim();

						if (response.toUpperCase().equals("Y")) {
							errorString = fController.addRecipe(name, ingredientsList, true);

							if (!errorString.equals("") && !errorString.contains("already exists")) {
								System.out.println("ERROR: " + errorString + "...\n");
								continue;
							}
							break;
						}
						else {
							continue;
						}
					}

					//exit on success
					break;
				}
			}

			// Create an exercise
			else if(command == 9) {
				while(true) {
					System.out.println("Enter q to quit this command at any time");

					System.out.print("Name: ");
					String name = scn.nextLine().trim();
					if (name.equals("q")) {
						break;
					}

					System.out.print("Calories burned per hour: ");
					String calories = scn.nextLine().trim();
					if (calories.equals("q")) {
						break;
					}

					String errorString = eController.addExercise(name, calories, false);


					//Catch all non-collision errors
					if (!errorString.equals("") && !errorString.contains("already exists")) {
						System.out.println("ERROR: " + errorString + "...\n");
						continue;
					}

					//Collision error handling for overwrite functionality
					else if (errorString.contains("already exists")) {
						System.out.println(errorString);
						System.out.println("Would you like to overwrite? (y/n)");
						String response = scn.nextLine().trim();

						if (response.toUpperCase().equals("Y")) {
							errorString = eController.addExercise(name, calories, true);

							if (!errorString.equals("") && !errorString.contains("already exists")) {
								System.out.println("ERROR: " + errorString + "...\n");
								continue;
							}
							break;
						}
						else {
							continue;
						}
					}

					//exit on success
					break;
				}
			}

			// Log food
			else if(command == 10) {
				System.out.println("Foods:");
				ArrayList<String> foodNames = (ArrayList<String>)lController.getFoodData();
				for(String foodName : foodNames){
					System.out.println("\t" + foodName);
				}
			}

			// Log food
			else if(command == 11) {
				System.out.println("Exercises:");
				ArrayList<String> exerciseNames = (ArrayList<String>)lController.getExerciseData();
				for(String exerciseName : exerciseNames){
					System.out.println("\t" + exerciseName);
				}
			}

			// View all logged foods
			else if(command == 12) {
				String date;
				while (true) {
					System.out.print("date (yyyy-mm-dd): ");
					date = scn.nextLine().trim();
					if (date.split("-").length != 3) {
						System.out.println("Please enter a date in valid format");
						continue;
					}
					break;
				}

				if (date.equals("q")) {
					break;
				}

				ArrayList<String> loggedFoods = (ArrayList<String>) lController.getFoodData(date);
				if (loggedFoods == null) {
					System.out.println("No logged foods for entered date");
					continue;
				}

				for (int i = 0; i < loggedFoods.size() - 1; i += 2) {
					System.out.println("\t" + loggedFoods.get(i) + " for " + loggedFoods.get(i + 1) + " servings");
				}
			}

			// View all logged exercises
			else if(command == 13) {
				String date;
				while (true) {
					System.out.print("date (yyyy-mm-dd): ");
					date = scn.nextLine().trim();
					if (date.split("-").length != 3) {
						System.out.println("Please enter a date in valid format");
						continue;
					}
					break;
				}

				ArrayList<String> loggedExercises = (ArrayList<String>) lController.getExerciseData(date);
				if (loggedExercises == null) {
					System.out.println("No logged exercises for entered date");
					continue;
				}

				for (int i = 0; i < loggedExercises.size() - 1; i += 2) {
					System.out.println("\t" + loggedExercises.get(i) + " for " + loggedExercises.get(i + 1) + " minutes");
				}
			}

			// Get Calorie Information
			else if(command == 14) {
				String date;
				while (true) {
					System.out.print("date (yyyy-mm-dd): ");
					date = scn.nextLine().trim();
					if (date.split("-").length != 3) {
						System.out.println("Please enter a date in valid format");
						continue;
					}
					break;
				}
				if (date.equals("q")) {
					break;
				}

				ArrayList<Double> nutrientValues = nController.calculateTotalConsumedNutrients(date);
				Double desiredCalories = lController.getDesiredCalorieByDay(date);
				if (desiredCalories == null){
					desiredCalories = 2000.0;
				}
				Double consumedCalories = nutrientValues.remove(0);
				Double burnedCalories = nController.calculateBurnedCalories(date);
				Double netCalories = consumedCalories - burnedCalories;

				System.out.println("\tCalorie Limit: " + String.format("%.2f", desiredCalories));
				System.out.println("\tNet Calories: " + String.format("%.2f", netCalories));
				System.out.println("\tConsumed Calories: " + String.format("%.2f", consumedCalories));
				System.out.println("\tBurned Calories: " + String.format("%.2f", burnedCalories));
			}

			// Get Nutrition Information
			else if(command == 15) {
				String date;
				while (true) {
					System.out.print("date (yyyy-mm-dd): ");
					date = scn.nextLine().trim();
					if (date.split("-").length != 3) {
						System.out.println("Please enter a date in valid format");
						continue;
					}
					break;
				}
				if (date.equals("q")) {
					break;
				}

				//Get nutrition information
				ArrayList<Double> nutrientValues = nController.calculateTotalConsumedNutrients(date);
				nutrientValues.remove(0);

				// Calculate total grams of nutrients
				Double total = 0.0;
				for (Double nutrient : nutrientValues) {
					total += nutrient;
				}

				// Set the titles with the percentages calculated off of totals
				if (total != 0) {
					Double fatPercentage = (nutrientValues.get(0) / total) * 100;
					Double carbPercentage = (nutrientValues.get(1) / total) * 100;
					Double proteinPercentage = (nutrientValues.get(2) / total) * 100;

					System.out.println("\tFat: " + nutrientValues.get(0) + "g (" + String.format("%.2f", fatPercentage) + "%)");
					System.out.println("\tCarb: " + nutrientValues.get(1) + "g (" + String.format("%.2f", carbPercentage) + "%)");
					System.out.println("\tProtein: " + nutrientValues.get(2) + "g (" + String.format("%.2f", proteinPercentage) + "%)");
				} else {
					System.out.println("\tFat: " + 0 + "% (" + 0 + "g)");
					System.out.println("\tCarb: " + 0 + "% (" + 0 + "g)");
					System.out.println("\tProtein: " + 0 + "% (" + 0 + "g)");
				}
			}

			// Quit
			else if(command == 16) {
				ioHandler.write();
				System.exit(1);
			}
		}
	}
}
