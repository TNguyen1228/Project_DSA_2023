package version2withGUI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

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

	public static void main(String[] args) throws Exception {
		try {
			launch(args);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

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
		Button deleteButton = new Button("Delete Min root");
		Button mergeButton = new Button("Merge with another heap");
		Button findButton = new Button("Find");
		Button visualizeButton = new Button("Visualize");
		Button findMinButton = new Button("Find Min");
		// Create result label
		resultLabel = new Label();

		// Set button event handlers
		insertButton.setOnAction(e -> insertNode());
		deleteButton.setOnAction(e -> deleteMinRoot());
		findButton.setOnAction(e -> findNode());
		visualizeButton.setOnAction(e -> visualize());
		findMinButton.setOnAction(e -> findMinNode());
		mergeButton.setOnAction(e -> mergeHeap());
		// Create grid pane for layout
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(10));

		// Add input fields and buttons to the grid pane
		gridPane.add(new Label("Value:"), 0, 0);
		gridPane.add(valueField, 1, 0);
		gridPane.add(new Label("New Value:"), 0, 1);
		gridPane.add(newValueField, 1, 1);
		gridPane.add(insertButton, 0, 2);
		gridPane.add(deleteButton, 0, 3);
		gridPane.add(visualizeButton, 1, 2);
		gridPane.add(findButton, 1, 3);
		gridPane.add(findMinButton, 0, 4);
		gridPane.add(mergeButton, 1, 4);

		// Create a VBox for the result label
		VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(resultLabel);

		// Create a root VBox to hold the grid pane and result label
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(gridPane, vbox);

		// Set the scene
		Scene scene = new Scene(root, 600, 300);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void mergeHeap() {

		try {
			if (bnh.empty()) {
				JOptionPane.showMessageDialog(null, "Main heap is empty.");
				return;
			}
			BNH bnh2 = new BNH();
			List<Integer> values = parseValues(JOptionPane.showInputDialog("Input another heap."));
			for (int value : values) {
				if (value < 0) {
					return;
				}
				bnh2.insert(value);
			}
			BNH output = BNH.merge(bnh, bnh2);
			JOptionPane.showMessageDialog(null, output.visualize());

		} catch (Exception e) {
			// TODO: handle exception
			showError("Invalid input format. Please enter a non-negative integer value.");
		}
	}

	private void insertNode() {
		try {
			String inputValues = valueField.getText();
			List<Integer> values = parseValues(inputValues);

			// Call the insert method of BNH class
			for (int value : values) {
				if (value < 0) {
					return;
				}
				bnh.insert(value);
			}
			valueField.clear();
			resultLabel.setText("Nodes inserted: " + values);
		} catch (Exception e) {
			showError("Invalid input format. Please enter a non-negative integer value.");
		}
	}

//	private void decreaseValue() {
//	
//		try {
//			int value = Integer.parseInt(valueField.getText());
//			int newValue = Integer.parseInt(newValueField.getText());
//	
//			if (newValue > value) {
//				StringBuilder error = new StringBuilder(
//						"New value is greater than the current value. Value cannot be decreased.");
//				error.toString();
//				JOptionPane.showMessageDialog(null, error);
//				return;
//			}
//			// Call the decreaseValue method of BNH class
//			if (bnh.decreaseValue(bnh.findNodeWithValue(value), newValue)) {
//				resultLabel.setText("Value decreased for node: " + value);
//			}
//			valueField.clear();
//			newValueField.clear();
//	
//		} catch (NumberFormatException e) {
//			// TODO: handle exception
//			showError("Invalid input format. Please enter an integer value.");
//		}
//	
//	}

	private void findNode() {
		try {

			int value = Integer.parseInt(valueField.getText());
			if (value < 0) {
				return;
			}
			// Call the findNodeWithValue method of BNH class
			BNH.HeapNode node = bnh.findNodeWithValue(value);
			valueField.clear();
			JOptionPane.showMessageDialog(null, node != null ? "Node found: " + value : "Node not found");

		} catch (NumberFormatException e) {
			showError("Invalid input format. Please enter a non-negative integer value.");
		}
	}

	private void findMinNode() {
		try {
			int valueFound = bnh.findMin();
			if (valueFound == -1) {
				JOptionPane.showMessageDialog(null, "Empty Heap");
				return;
			}
			resultLabel.setText("Min found: " + valueFound);
		} catch (Exception e) {
			// TODO: handle exception

			showError("Invalid input format. Please enter a non-negative integer value.");
		}
	}

	private void deleteMinRoot() {
		// TODO Auto-generated method stub
		try {
			bnh.deleteMin();
			resultLabel.setText("Delete Min successful.");
		} catch (Exception e) {
			// TODO: handle exception
			showError("Invalid input format. Please enter a non-negative integer value.");
		}
	}

	private void visualize() {
		StringBuilder visualization = new StringBuilder(bnh.visualize());
		JOptionPane.showMessageDialog(null, visualization);

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