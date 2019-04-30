package model.food;

/**
 * Represents a basic food and holds the food's properties (name, calorie, fat, etc.)
 *
 * @author Team A
 */
public class BasicFood implements IFood {
	private String name;
	private double calories;
	private double fat;
	private double carb;
	private double protein;

	public BasicFood(String name, double calories, double fat, double carb, double protein) {
		this.name = name;
		this.calories = calories;
		this.fat = fat;
		this.carb = carb;
		this.protein = protein;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public double getCalories() {
		return calories;
	}

	@Override
	public double getFat() {
		return fat;
	}

	@Override
	public double getCarb() {
		return carb;
	}

	@Override
	public double getProtein() {
		return protein;
	}
}
