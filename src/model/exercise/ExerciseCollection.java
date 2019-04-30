package model.exercise;

import java.util.*;

/**
 * Stores Exercises in a collective hash map
 *
 * @author Team A
 */
public class ExerciseCollection extends Observable {
    private Map<String, Exercise> exercises;

    public ExerciseCollection() {
        this.exercises = new HashMap<>()    ;
    }

    /**
     * Adds an exercise
     *
     * @param name
     * @param exercise
     */
    public void addExercise(String name, Exercise exercise){
        exercises.put(name, exercise);

        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
    }

    /**
     * Gets an exercise from the collection by name
     *
     * @param name exercise name
     * @return Food object
     */
    public Exercise getExercise(String name) {
        return exercises.get( name );
    }

    /**
     * Gets the exercise hash map
     *
     * @return map
     */
    public List<Exercise> getAllExercises() {
        return new ArrayList(exercises.values());
    }
}
