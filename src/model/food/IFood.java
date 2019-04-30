package model.food;

/**
 * An interface that will be shared among all food related objects for dependency inversion and programming to an interface
 *
 * @author Team A
 */
public interface IFood {
	String getName();
	double getCalories();
	double getFat() ;
	double getCarb();
	double getProtein() ;
}
