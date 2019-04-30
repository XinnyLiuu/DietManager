package view;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.ArrayList;

/**
 * The view that displays user nutrition consumed in grams of fat, carbohydrates, and protein from left to right
 * Creates a new Graph panel for anything with a y axis of doubles
 *
 * Used generically for nutrient and calorie graphs
 *
 * @author Team A
 */
public class GraphPanel {
	private final CategoryAxis xAxis;
	private final NumberAxis yAxis;

	public ArrayList<XYChart.Series<String, Double>> data;

	private BarChart barChart;

	public GraphPanel(String xLabel, String yLabel, int numberOfColumns, String title) {
		// Prepare chart axis
		this.xAxis = new CategoryAxis();
		this.yAxis = new NumberAxis();
		this.xAxis.setLabel(xLabel);
		this.yAxis.setLabel(yLabel);

		// Set names of columns to titles passed in
		this.data = new ArrayList<>();
		for(int i = 0; i < numberOfColumns; i++){
			this.data.add(new XYChart.Series<>());
		}

		this.barChart = this.createBarChart( title );
	}

	/**
	 * Returns the BarChart created from this class
	 *
	 * @return BarChart
	 */
	public BarChart getBarChart() {
		return barChart;
	}

	/**
	 * Creates a BarChart
	 *
	 * @param title the title of the chart
	 * @return BarChart
	 */
	public BarChart createBarChart(String title) {
		BarChart bc = new BarChart(xAxis, yAxis);
		bc.setTitle(title);
		bc.getData().addAll(this.data);
		bc.setLegendVisible(false);

		return bc;
	}

	/**
	 * Updates the labels and values of the bars in the bar chart
	 *
	 * @param titles
	 * @param values
	 */
	public void updateBarChartValues(ArrayList<String> titles, ArrayList<Double> values){
		for(int i = 0; i < values.size(); i++) {
			String label = titles.get(i);
			Double value = values.get(i);
			data.get(i).getData().setAll(new XYChart.Data(label, value));
		}

		// Set colors for Fat, Carb and Protein as specified
		for( Object series : barChart.getData()) {
			XYChart.Series<String, Double> s = (XYChart.Series) series;

			for( XYChart.Data data : s.getData()) {
				if(data.getXValue().toString().contains("Fat")) {
					data.getNode().setStyle("-fx-bar-fill: red;");
				}
				else if(data.getXValue().toString().contains("Carb")) {
					data.getNode().setStyle("-fx-bar-fill: green;");
				}
				else if(data.getXValue().toString().contains("Protein")) {
					data.getNode().setStyle("-fx-bar-fill: blue;");
				}
			}
		}
	}
} 
