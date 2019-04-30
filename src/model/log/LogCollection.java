package model.log;

import java.util.*;

/**
 * Stores information taken from log.csv
 *
 * @author Team A
 */
public class LogCollection extends Observable {
	private Map<String, List<String>> loggedFoods; // ie.) "2018-10-15" -> [ "Hot Dog", "1.5", "Coffee", "2.5" ]
	private Map<String, List<String>> loggedExercises; // ie.) "2018-10-15" -> [ "Gardening", "1.5", "Jogging (5 mph)", "2.5" ]
	private Map<String, Double> loggedWeights;
	private Map<String, Double> loggedCaloriesLimit;

	public LogCollection() {
		this.loggedFoods = new HashMap<>();
		this.loggedExercises = new HashMap<>();
		this.loggedWeights = new HashMap<>();
		this.loggedCaloriesLimit = new HashMap<>();
	}

	/**
	 * Adds an entry to loggedFoods
	 *
	 * @param date
	 * @param food
	 * @param servings
	 */
	public void addFoodLog(String date, String food, String servings) {
		// Format servings to one point double
		double s = Math.round( Double.parseDouble(servings) * 10.0) / 10.0;

		// Check if list of food exists for date
		if(loggedFoods.get(date) == null) {
			List<String>foodByServing = new ArrayList<>();
			foodByServing.add(food);
			foodByServing.add( Double.toString( s ) );
			loggedFoods.put( date, foodByServing );
		}
		else {
			List<String>foodByServing = loggedFoods.get(date);
			foodByServing.add( food );
			foodByServing.add( Double.toString( s ) );
			loggedFoods.put( date, foodByServing );
		}

		// Notify observers that the data has changed
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}

	/**
	 * Adds an entry to loggedExercises
	 *
	 * @param date Date of exercise in yyyy-mm-dd format
	 * @param exerciseName The name of the exercies
	 * @param exerciseDuration The duration in minutes
	 */
	public void addExerciseLog(String date, String exerciseName, String exerciseDuration) {
		// Format servings to one point double
		double d = Math.round( Double.parseDouble(exerciseDuration) * 10.0) / 10.0;

		List<String> exerciseList = loggedExercises.get(date) == null ? new ArrayList<>() : loggedExercises.get(date);
		exerciseList.add(exerciseName);
		exerciseList.add( Double.toString(d) );
		this.loggedExercises.put( date, exerciseList );

		// Notify observers that the data has changed
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}

	/**
	 * Adds a weight to loggedWeights
	 *
	 * @param date
	 * @param weight
	 */
	public void addWeightLog(String date, double weight) {
		loggedWeights.put( date, weight );

		//Update observers
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}

	/**
	 * Adds a calorie limit to loggedCaloriesLimit
	 *
	 * @param date
	 * @param calorie
	 */
	public void addCalorieLimit(String date, double calorie) {
		loggedCaloriesLimit.put( date, calorie );

		//Update observers
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}

	/**
	 * Removes a logged food from logged Foods
	 *
	 * @param date
	 * @param name
	 * @param servings
	 * @return
	 */
	public String removeFoodLog(String date, String name, String servings) {
		// Format servings to one point double
		double s = Math.round(Double.parseDouble(servings) * 10.0) / 10.0;
		servings = Double.toString(s);

		// Check if a value exists for that date
		if( loggedFoods.get(date) != null) {
			List<String> foods = loggedFoods.get(date); // All logged foods for that date
			Map<String, Integer> freq = new HashMap<>(); // This map stores the count of repeating name, servings (if any)
			String foodServings = name + servings; // Concatenate the name and servings as key for map

			// Check the list for matching entries
			for(int i=0; i<foods.size(); i++) {
				if(foods.get(i).equals( name )) {
					if(foods.get(i+1).equals(servings)) {

						// Add to map
						if(freq.containsKey(foodServings)) {
							freq.put( foodServings, freq.get(foodServings)+1 );
						}
						else {
							freq.put(foodServings, 1);
						}
					}
				}
			}

			if(freq.size() > 0) {
				if(freq.get( foodServings ) > 1) { // If there are duplicates, remove one and return a string to the controller
					int num = freq.get(foodServings);
					freq.put( foodServings, freq.get(foodServings)-1 );

					for(int i=0; i<foods.size(); i++) {
						if(foods.get(i).equals( name )) {
							if(foods.get(i+1).equals(servings)) {
								foods.remove(i);
								foods.remove(i);

								// Notify observers that the data has changed
								this.setChanged();
								this.notifyObservers();
								this.clearChanged();

								break;
							}
						}
					}

					return String.format("There are %s duplicate entries! Removing one...", num);
				}
				else { // When there is only one duplicate remaining, remove it
					for(int i=0; i<foods.size(); i++) {
						if(foods.get(i).equals( name )) {
							if(foods.get(i+1).equals(servings)) {
								foods.remove(i);
								foods.remove(i);

								// Notify observers that the data has changed
								this.setChanged();
								this.notifyObservers();
								this.clearChanged();

								return "";
							}
						}
					}
				}
			}
		}

		return "Null";
	}

	/**
	 * Removes a logged exercise from logged exercises
	 *
	 * @param date
	 * @param name
	 * @param duration
	 * @return
	 */
	public String removeExerciseLog(String date, String name, String duration) {
		// Format duration to one point double
		double d = Math.round(Double.parseDouble(duration) * 10.0) / 10.0;
		duration = Double.toString(d);

		// Check if a value exists for that date
		if( loggedExercises.get(date) != null) {
			List<String> exercises = loggedExercises.get(date); // All logged exercises for that date
			Map<String, Integer> freq = new HashMap<>(); // This map stores the count of repeating name, servings (if any)
			String exerciseDuration = name + duration; // Concatenate the name and duration as key for map

			// Check the list for matching entries
			for(int i=0; i<exercises.size(); i++) {
				if(exercises.get(i).equals( name )) {
					if(exercises.get(i+1).equals(duration)) {

						// Add to map
						if(freq.containsKey(exerciseDuration)) {
							freq.put( exerciseDuration, freq.get(exerciseDuration)+1 );
						}
						else {
							freq.put(exerciseDuration, 1);
						}
					}
				}
			}

			if(freq.size() > 0) {
				if(freq.get( exerciseDuration ) > 1) { // If there are duplicates, remove one and return a string to the controller
					int num = freq.get(exerciseDuration);
					freq.put( exerciseDuration, freq.get(exerciseDuration)-1 );

					for(int i=0; i<exercises.size(); i++) {
						if(exercises.get(i).equals( name )) {
							if(exercises.get(i+1).equals(duration)) {
								exercises.remove(i);
								exercises.remove(i);

								// Notify observers that the data has changed
								this.setChanged();
								this.notifyObservers();
								this.clearChanged();

								break;
							}
						}
					}

					return String.format("There are %s duplicate entries! Removing one...", num);
				}
				else { // When there is only one duplicate remaining, remove it
					for(int i=0; i<exercises.size(); i++) {
						if(exercises.get(i).equals( name )) {
							if(exercises.get(i+1).equals(duration)) {
								exercises.remove(i);
								exercises.remove(i);

								// Notify observers that the data has changed
								this.setChanged();
								this.notifyObservers();
								this.clearChanged();

								return "";
							}
						}
					}
				}
			}
		}

		return "Null";
	}

	/**
	 * Gets the list of food by serving for the day, refer to loggedFoods
	 *
	 * @param date
	 * @return List of food, serving
	 */
	public List<String> getFoodLog(String date) {
		return loggedFoods.get( date );
	}

	/**
	 * Gets the list of exercises by serving for the day, refer to loggedExercises
	 *
	 * @param date
	 * @return List of food, serving
	 */
	public List<String> getExerciseLog(String date) {
		return loggedExercises.get( date );
	}

	/**
	 * Gets the weight for the day
	 *
	 * @param date
	 * @return Weight
	 */
	public Double getWeight(String date) {
		return loggedWeights.get( date );
	}

	/**
	 * Gets the calorie limit for the day
	 *
	 * @param date
	 * @return Calorie limit
	 */
	public Double getDesiredCalories(String date) {
		return loggedCaloriesLimit.get( date );
	}

	/**
	 * Returns all dates and logged foods
	 *
	 * @return
	 */
	public Map<String, List<String>> getFoodLogCollection() {
		return this.loggedFoods;
	}

	/**
	 * Returns all dates and logged exercises
	 *
	 * @return
	 */
	public Map<String, List<String>> getExerciseLogCollection() {
		return this.loggedExercises;
	}

	/**
	 * Returns all dates and weights
	 *
	 * @return
	 */
	public Map<String, Double> getWeightLogCollection() {
		return this.loggedWeights;
	}

	/**
	 * Returns all dates and calories
	 *
	 * @return
	 */
	public Map<String, Double> getCalorieLogCollection() {
		return this.loggedCaloriesLimit;
	}
}
