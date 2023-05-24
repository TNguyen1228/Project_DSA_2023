package InternalStructure;

import java.util.*;

import javax.swing.JOptionPane;

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
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.*;

public class TaskGenerate extends Application {
	private final int MIN_DAY = 1;
	private final int MAX_DAY = 28;
	private final int MIN_MONTH = 1;
	private final int MAX_MONTH = 12;
	private final int MIN_YEAR = 2000;
	private final int MAX_YEAR = 2500;

	private TextField valueField;
	private BinomialHeap heap;

	public static void main(String[] args) {
		try {

			launch(args);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("TASK SCHEDULE");
		valueField = new TextField();

		// Create buttons
		Button generateRandomSetButton = new Button("_Generate A Random Set.");
		Button scheduleButton = new Button("_Schedule Tasks");
		scheduleButton.setVisible(false);
		Button showResultButton = new Button("Show _Result");
		showResultButton.setVisible(false);
		FileChooser fileChooser = new FileChooser();

		Button selectFileButton = new Button("Import From _File...");

		// Set styles
		generateRandomSetButton.setMnemonicParsing(true);
		scheduleButton.setMnemonicParsing(true);
		generateRandomSetButton.setStyle("-fx-text-fill: #ff0000 ");
		scheduleButton.setStyle("-fx-text-fill: #ff0000 ");

		// Set butoon event handlers
		generateRandomSetButton.setOnAction(e -> {
			showResultButton.setVisible(false);
			generateRandomTask();
			if (!valueField.getText().isBlank()) {
				scheduleButton.setVisible(true);
			}
		});
		scheduleButton.setOnAction(e -> {
			taskScheduling();
			showResultButton.setVisible(true);

		});
		selectFileButton.setOnAction(e -> {
			scheduleButton.setVisible(false);
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			String[] tasksFromFile = readTasksFromFile(selectedFile.toString());
			heap = new BinomialHeap();
			for (int i = 0; i < tasksFromFile.length; i++) {
				Node node = extractDateString(tasksFromFile[i]);
				heap.insert(node);
			}
			PrintWriter writer;
			try {
				// Sắp xấp lại thứ tự ưu tiên rồi in kết quả ra 1 file txt khác
				writer = new PrintWriter(new FileWriter("result.txt"));
				for (int i = 0; i < tasksFromFile.length; i++) {
					writer.println(heap.printOutPriorityTask());
				}
				writer.close();
				JOptionPane.showMessageDialog(null, "All tasks scheduled.");
				showResultButton.setVisible(true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
		showResultButton.setOnAction(e -> {
			try {
				File fileOutput = new File("result.txt");
				// check if Desktop is supported by Platform or not
				if (!Desktop.isDesktopSupported()) {
					System.out.println("not supported");
					return;
				}
				Desktop desktop = Desktop.getDesktop();
				if (fileOutput.exists()) // checks file exists or not
					desktop.open(fileOutput); // opens the specified file

			} catch (Exception error) {
				// TODO: handle exception
				error.printStackTrace();
			}

		});

		// Create grid pane for layout
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(10));

		// Add input fields and buttons and slider to the grid pane
		gridPane.add(new Label("Size of set:"), 0, 0);
		gridPane.add(generateRandomSetButton, 0, 5);
		gridPane.add(scheduleButton, 1, 5);
		gridPane.add(selectFileButton, 0, 4);
		gridPane.add(valueField, 1, 0);
		gridPane.add(showResultButton, 0, 6);

		// Create a VBox for the result label
		VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);

		// Create a root VBox to hold the grid pane and result label
		VBox root = new VBox(20);
		root.setAlignment(Pos.TOP_LEFT);
		root.getChildren().addAll(gridPane, vbox);

		// Set the scene
		Scene scene = new Scene(root, 600, 300);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void generateRandomTask() {
		Random random = new Random();

		try (PrintWriter writer = new PrintWriter(new FileWriter("randomSet.txt"))) {
			writer.flush();
			String input = valueField.getText();
			if (input.isBlank()) {
				JOptionPane.showMessageDialog(null, "Invalid Input. Enter a non - negative integer.");
				return;
			}

			for (int i = 1; i <= Integer.parseInt(input); i++) {
				int day = random.nextInt(MAX_DAY - MIN_DAY + 1) + MIN_DAY;
				int month = random.nextInt(MAX_MONTH - MIN_MONTH + 1) + MIN_MONTH;
				int year = random.nextInt(MAX_YEAR - MIN_YEAR + 1) + MIN_YEAR;

				String taskDetails = "Task" + i + " " + day + "/" + month + "/" + year;
				writer.println(taskDetails);
				// System.out.println(taskDetails);

			}
			JOptionPane.showMessageDialog(null, "Tasks created.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void taskScheduling() {

		String[] tasks = readTasksFromFile("randomSet.txt");
		heap = new BinomialHeap();
		// Truyền các node vào trong 1 BNH
		for (int i = 0; i < tasks.length; i++) {
			Node node = extractDateString(tasks[i]);
			heap.insert(node);
		}

		// Sắp xấp lại thứ tự ưu tiên rồi in kết quả ra 1 file txt khác
		try (PrintWriter writer = new PrintWriter(new FileWriter("result.txt"))) {
			for (int i = 0; i < tasks.length; i++) {
				writer.println(heap.printOutPriorityTask());
			}
			JOptionPane.showMessageDialog(null, "Tasks scheduled.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Đọc dữ liệu từ file truyền vào rồi cho hết vào 1 String[] array
	 */
	private String[] readTasksFromFile(String filename) {
		List<String> taskList = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				taskList.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return taskList.toArray(new String[0]);
	}

	/**
	 * Từ mỗi dòng trong String[] array lấy ra tên task và deadline và tạo 1 node
	 * tương ứng
	 */
	private Node extractDateString(String task) {
		String[] words = task.split(" ");
		Node node = new Node(words[0], words[1]);

		return node;
	}
}
