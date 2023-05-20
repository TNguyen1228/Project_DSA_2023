package version2withGUI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
	private BNH bnh;
	private TextField valueField;
	private TextField newValueField;
	private Label resultLabel;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		bnh = new BNH();

		primaryStage.setTitle("Binomial Heap GUI");

		// Create input fields
		valueField = new TextField();
		newValueField = new TextField();

		// Create buttons
		Button insertButton = new Button("Insert");
//        Button deleteButton = new Button("Delete");
		Button decreaseButton = new Button("Decrease");
		Button findButton = new Button("Find");
		Button visualizeButton = new Button("Visualize");

		// Create result label
		resultLabel = new Label();

		// Set button event handlers
		insertButton.setOnAction(e -> insertNode());
//        deleteButton.setOnAction(e -> deleteNode());
		decreaseButton.setOnAction(e -> decreaseValue());
		findButton.setOnAction(e -> findNode());
		visualizeButton.setOnAction(e -> visualize());

		// Create grid pane for layout
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10));

		// Add input fields and buttons to the grid pane
		gridPane.add(new Label("Value:"), 0, 0);
		gridPane.add(valueField, 1, 0);
		gridPane.add(new Label("New Value:"), 0, 1);
		gridPane.add(newValueField, 1, 1);
		gridPane.add(insertButton, 0, 2);
//      gridPane.add(deleteButton, 1, 2);
		gridPane.add(decreaseButton, 0, 3);
		gridPane.add(findButton, 1, 3);
		gridPane.add(visualizeButton, 1, 2);

		// Create a VBox for the result label
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(resultLabel);

		// Create a root VBox to hold the grid pane and result label
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(gridPane, vbox);

		// Set the scene
		Scene scene = new Scene(root, 400, 200);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void insertNode() {
		try {
			String inputValues = valueField.getText();
			List<Integer> values = parseValues(inputValues);

			// int value = Integer.parseInt(valueField.getText());
			// Call the insert method of BNH class
			for (int value : values) {
				bnh.insert(value);
			}
			valueField.clear();
			resultLabel.setText("Nodes inserted: " + values);
		} catch (NumberFormatException e) {
			showError("Invalid input format. Please enter an integer value.");
		}
	}

	private void decreaseValue() {
		try {
			int value = Integer.parseInt(valueField.getText());
			int newValue = Integer.parseInt(newValueField.getText());
			// Call the decreaseValue method of BNH class
			bnh.decreaseValue(bnh.findNodeWithValue(value), newValue);
			valueField.clear();
			newValueField.clear();
			resultLabel.setText("Value decreased for node: " + value);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			showError("Invalid input format. Please enter an integer value.");
		}

	}

	private void findNode() {
		try {
			int value = Integer.parseInt(valueField.getText());
			// Call the findNodeWithValue method of BNH class
			BNH.HeapNode node = bnh.findNodeWithValue(value);
			valueField.clear();
			resultLabel.setText(node != null ? "Node found: " + value : "Node not found");
		} catch (NumberFormatException e) {
			showError("Invalid input format. Please enter an integer value.");
		}
	}

	private void visualize() {
		StringBuilder visualization = new StringBuilder();
		bnh.visualize();
		resultLabel.setText(visualization.toString());
	}

	private void showError(String message) {
		resultLabel.setText("Error: " + message);
	}

	private List<Integer> parseValues(String input) {
		try {
			return Arrays.stream(input.split(" ")).map(String::trim).map(Integer::parseInt)
					.collect(Collectors.toList());
		} catch (NumberFormatException e) {
			return null;
		}
	}
}