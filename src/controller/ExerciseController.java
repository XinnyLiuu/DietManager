package controller;

import model.exercise.Exercise;
import model.exercise.ExerciseCollection;

import java.util.List;

/**
 * All actions from the Exercise related panels will pass through here
 *
 * @author Team A
 */
public class ExerciseController {
    private ExerciseCollection exerciseCollection;

    public ExerciseController(ExerciseCollection e){
        this.exerciseCollection = e;
    }

    /**
     * Gets an list of all exercises
     *
     * @return List
     */
    public List<Exercise> getAllExercises(){
        return this.exerciseCollection.getAllExercises();
    }

    /**
     * Gets a specific exercise by name
     *
     * @param name
     * @return Exercise
     */
    public Exercise getExercise( String name ) {
        return this.exerciseCollection.getExercise(name);
    }

    /**
     * Creates an exercise with given name and calories per hour
     *
     * @param name
     * @param caloriesPerHour
     * @param overwrite
     * @return String
     */
    public String addExercise(String name, String caloriesPerHour, boolean overwrite) {
        try {
            // Check that a valid name was entered
            if (name.equals("")) {
                return "Please enter a name for the exercise";
            }

            // Check that the calories per hour value entered is valid
            if (Double.parseDouble(caloriesPerHour) < 0) {
                throw new NumberFormatException();
            }

            // Check that this exercise does not already exist in the ExerciseCollection before adding.
            if(!overwrite) {
                Exercise exercise = exerciseCollection.getExercise(name);

                if(exercise != null) {
                    // Exercise already exists
                    return name + " already exists!";
                }
            }

            // Create exercise
            Exercise exercise = new Exercise(name, Double.parseDouble(caloriesPerHour));
            exerciseCollection.addExercise(name, exercise);
            return "";
        }
        catch(NumberFormatException nfe){
            return "Please enter a number greater than or equal to 0 for calories burned per hour.";
        }
    }
}
