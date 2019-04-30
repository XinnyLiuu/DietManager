package model.exercise;

/**
 * This class represents an exercise
 *
 * @author Team A
 */
public class Exercise {
    private String name;
    private double caloriesPerHour;

    public Exercise(String name, double caloriesPerHour) {
        this.name = name;
        this.caloriesPerHour = caloriesPerHour;
    }

    public String getName() {
        return name;
    }

    public double getCaloriesPerHour() {
        return caloriesPerHour;
    }

    public double calculateCalories(double weight, double duration) {
        return this.caloriesPerHour * (weight / 100.0) * (duration / 60.0);
    }
}
