/* changes from last version
 * - updated some documentation
 * - tried storing the questions in an observable list. The model for this is contained in the TableListCourseInfo class
 * - cousreListController has an initialize method that creates the table view, some columns and calls the default method, which adds a course if the list is empty
 * - attempted to update the addCourseController class so that a user can input information for a new course, click the add Course button, and the course will be added to the courseList.fxml 
 *   as well as return them to the courseList.fxml page. I can't get the button to return to courseList.fxml, but i can't add a course to the list.
 */


package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.MxQuestion;
import models.MyQuestionsCollection;
import models.Questions;
import models.TableListCourseInfo;

/**
 * This is the main controller class for the our application. 
 */

public class Main extends javafx.application.Application{
	
	@FXML
	private static Stage primaryStage;
	@FXML
	private Scene scene;
	@FXML
	private Button coursePage;
	@FXML
	private Button questionPage;
	@FXML
	private Button quizPage;
	/** static course list for */
	private static final TableListCourseInfo courseList = new TableListCourseInfo();
	/** static course question list */
	private static final MyQuestionsCollection myQuestions = new MyQuestionsCollection(); 
	
	/**
	 * This is the start method for the application. It loads the main fxml file. 
	 * @param stage for the main.fxml file
	 */
	@Override
	public void start(Stage stage) {
	    try {
			primaryStage = stage;
			loader("Main.fxml");
			primaryStage.setTitle("My Flashcards");
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will return the list of course objects. 
	 * 
	 * @return courseList
	 */
	public static TableListCourseInfo getCourseList() {
		return courseList;
	}
	
	/**
	 * This method will return the list of question objects.
	 * 
	 * @return myQuestions
	 */
	public static MyQuestionsCollection getMyQuestions() {
		return myQuestions;
	}
	
	
	/*
	 * This method will take the user to the questionList.fxml page
	 */
	@FXML
	public void goToQuestionPage(ActionEvent event) throws IOException {
		Main.loader("questionList.fxml");
	}
	
	/*
	 * This method will take the user to the courseList.fxml page
	 */
	public void goToCoursePage(ActionEvent event) throws IOException {
		Main.loader("courseList.fxml");
	}
	
	public static void main(String[] args) {
        launch(args);
    }
	
	/**
	 * This is a loader method that all other controller classes will use to load their respective fxml files
	 */
	public static void loader(String fxml) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource(fxml));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loaderWithQuestion(String fxml, Questions question)	{
		try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
            Parent root = loader.load();
            // check if we're loading the ModifyQuestion screen
            if ("modifyQuestion.fxml".equals(fxml)) {
                ModifyQuestionController controller = loader.getController();
                controller.setQuestion(question);
            }
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void addQuestion(String fxml) throws IOException{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("What would you like to do?");
		alert.setContentText("Are you ok with this?");
		
		// create the buttons
		ButtonType writeQuestion = new ButtonType("Write my own");
		ButtonType upLoadQuestion = new ButtonType("Upload a file");
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(writeQuestion, upLoadQuestion, cancel);
		
		// 4 different ways to handle the result
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == writeQuestion) {
		    Main.loader(fxml);
		} else  if (result.get() == upLoadQuestion) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			File selectedFile = fileChooser.showOpenDialog(null);
	 
			StringBuilder textBuilder = new StringBuilder();
		
			try {
				// reads each line from the file and append it to the StringBuilder
				Files.lines(Paths.get(selectedFile.getAbsolutePath())).forEach(line -> textBuilder.append(line));
				String text = textBuilder.toString();
				MxQuestion newQuestion = new MxQuestion("General", "MC", text, null, false, null, false, null, false, null, false);
				Main.getMyQuestions().addQuestion(newQuestion);
			}
			catch (IOException e) {
	            e.printStackTrace();
	        }
			
			
		}
	}

}