package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.FillQuestion;
import models.MxQuestion;
import models.Questions;
import models.ShortQuestion;
import models.TorFQuestion;

/**
 * This controller class allows users to add questions to the Question List.
 */

public class ModifyQuestionController {
	
	// FXML variables for the questionPage.fxml file
	
	/** Returns user to question list */
	@FXML
	private Button returnToQuestionList;
	/** Button to add a question */
	@FXML
	private Button saveQuestion;
	/** Button to delete the current question being modified */
	@FXML
	private Button deleteQuestion;
	/** ComboBox for selecting the course */
	@FXML
	private ComboBox<String> courseList;
	/** ComboBox for selecting the question type */
	@FXML
	private ComboBox<String> questionType;
	/** Text field for entering the question */
	@FXML
	private TextArea questionField;
	/** Text field for entering option A */
	@FXML
	private TextField optionA;
	/** Text field for entering option B */
	@FXML
	private TextField optionB;
	/** Text field for entering option C */	
	@FXML
	private TextField optionC;
	/** Text field for entering option D */	
	@FXML
	private TextField optionD;
	/** CheckBox for selecting answer A */
	@FXML
	private CheckBox answerA;
	/** CheckBox for selecting answer B */
    @FXML
    private CheckBox answerB;
    /** CheckBox for selecting answer C */
    @FXML
    private CheckBox answerC;
    /** CheckBox for selecting answer D */
    @FXML
    private CheckBox answerD;
    /** The current question being modified */
    private Questions currentQuestion;
    
	
	/** Loads the Question List Page. */
	
	public void start (Stage stage) throws Exception{
        Main.loader("modifyQuestion.fxml");
	}
	
	/** Returns the user to the question list */
	public void returnToQuestionList(ActionEvent event) {
		Main.loader("questionList.fxml");
	}
	
	/** Deletes current question being modified */
	public void removeQuestion(ActionEvent event) throws IOException {
		Main.getMyQuestions().removeQuestion(currentQuestion);
		Main.loader("questionList.fxml");
	}
	
	/**
	 * Sets the interface, program, question type and question text fields to the current question being modified.
	 * 
	 * @param The current question being modified
	 */
	
	public void setQuestion(Questions question) {
		this.currentQuestion = question;
		if (question instanceof MxQuestion)  {
			courseList.setValue(question.getCourseInfo());
			questionType.setValue("MC");
			questionField.setText(question.getBody());
			optionA.setText(((MxQuestion) question).getOptionA());
			optionB.setText(((MxQuestion) question).getOptionB());
			optionC.setText(((MxQuestion) question).getOptionC());
			optionD.setText(((MxQuestion) question).getOptionD());
			answerA.setSelected(((MxQuestion) question).getAnswerA());
			answerB.setSelected(((MxQuestion) question).getAnswerB());
			answerC.setSelected(((MxQuestion) question).getAnswerC());
			answerD.setSelected(((MxQuestion) question).getAnswerD());
		} else if (question instanceof TorFQuestion) {
			courseList.setValue(question.getCourseInfo());
            questionType.setValue("T/F");
            questionField.setText(question.getBody());
            optionA.setText("True");
            optionB.setText("False");
            answerA.setSelected(((TorFQuestion) question).getAnswerA());
            answerB.setSelected(((TorFQuestion) question).getAnswerB());
            optionC.setDisable(true);
            optionD.setDisable(true);
            answerC.setDisable(true);
            answerD.setDisable(true);
        } else if (question instanceof FillQuestion) {
        	courseList.setValue(question.getCourseInfo());
            questionType.setValue("Fill");
            questionField.setText(question.getBody());
            optionA.setText(((FillQuestion) question).getOptionA());
            optionB.setText(((FillQuestion) question).getOptionB());
            optionC.setText(((FillQuestion) question).getOptionC());
            optionD.setText(((FillQuestion) question).getOptionD());
            answerA.setDisable(true);
            answerB.setDisable(true);
            answerC.setDisable(true);
            answerD.setDisable(true);
        } else if (question instanceof ShortQuestion) {
        	courseList.setValue(question.getCourseInfo());
            questionType.setValue("Short");
            questionField.setText(question.getBody());
            optionA.setText(((ShortQuestion) question).getOptionA());
            optionB.setDisable(true);
            optionC.setDisable(true);
            optionD.setDisable(true);
            answerA.setDisable(true);
            answerB.setDisable(true);
            answerC.setDisable(true);
            answerD.setDisable(true);
        } else if (!(question instanceof Questions)) {
        	questionField.setText(question.getBody());
        }
	}	
	
	/**
	 * Opens a dialog box that lets the user save their question to a file. If they press cancel, they return to the question list.
	 * 
	 * @param this method takes a type of question object as a parameter.
	 */
	
	/* 
	 * We would save the file in an xml format to allow for easier reading and writing of the question,
	 * or save the question in a database. Also, currently, if the user hits cancel while in the file chooser, they are returned to the question list.
	 * We would change this so that they are returned to modify question page. 
	 */
	public void saveQuestion(ActionEvent event) {
		if (questionType.getValue().equals("MC")) {
			MxQuestion mxQuestion = new MxQuestion(courseList.getValue(), questionType.getValue(), questionField.getText(), optionA.getText(),
					answerA.isSelected(), optionB.getText(), answerB.isSelected(), optionC.getText(), answerC.isSelected(), optionD.getText(), answerD.isSelected());
			writeQuestion(mxQuestion);
			returnToQuestionList(event);
		} else if (questionType.getValue().equals("Fill")) {
			FillQuestion fillQuestion = new FillQuestion(courseList.getValue(), questionType.getValue(), questionField.getText(), optionA.getText(), optionB.getText(), optionC.getText(), optionD.getText());
			writeQuestion(fillQuestion);
			returnToQuestionList(event);
		} else if (questionType.getValue().equals("T/F")) {
			TorFQuestion torfQuestion = new TorFQuestion (courseList.getValue(), questionType.getValue(), questionField.getText(), optionA.getText(), answerA.isSelected(), optionB.getText(), answerB.isSelected());
			writeQuestion(torfQuestion);
			returnToQuestionList(event);
		} else if(questionType.getValue().equals("Short")) {
            ShortQuestion shortAnswerQuestion = new ShortQuestion(courseList.getValue(), questionType.getValue(), questionField.getText(), optionA.getText());
            writeQuestion(shortAnswerQuestion);
            returnToQuestionList(event);
        } else if (questionType.getValue() == null) {
        	returnToQuestionList(event);
        }
	}
	
	public void writeQuestion(Questions questions) {
	
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file + ".txt"))) {
				writer.write(questions.toString());
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			Main.getMyQuestions().addQuestion(questions);	
		}  
	}
	
	@SuppressWarnings("unlikely-arg-type")
	
	/** Initializes the interface settings based on the question type of current questions. */
	
	/*
	 * Notes for future improvements: Currently, this method adjusts the interface by adjusting each individual element.
	 * Future improvements would put this into a css file and the method would call a style sheet to adjust the interface.
	 * Also, this method makes it so that the user can't change the question type of the question. We would consider 
	 * removing this restriction in future iterations.
	 */
	@FXML
	public void initialize() {
		courseList.setItems(CourseListController.getCourseInfoString());
		questionType.setItems(FXCollections.observableArrayList("MC", "T/F", "Fill", "Short", "Other"));
		
//		
//		questionType.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
			
			if (questionType.equals("MC") || questionType.equals("Other")) {
				optionA.setFont(Font.getDefault());
				optionA.setEditable(true);
				answerA.setDisable(false);
				optionB.setFont(Font.getDefault());
				optionB.setEditable(true);
				optionB.setDisable(false);
				answerB.setDisable(false);
				optionC.setDisable(false);
				optionC.setEditable(true);
				answerC.setDisable(false);
				optionD.setDisable(false);
				optionD.setEditable(true);
				answerD.setDisable(false);
			} else if (questionType.equals("T/F")) {
				optionA.setFont(Font.font("System", FontWeight.BOLD , 20));
				optionA.setText("True");
				optionA.setDisable(false);
				optionA.setEditable(false);
				answerA.setDisable(false);
				optionB.setFont(Font.font("System", FontWeight.BOLD , 20));
				optionB.setText("False");
				optionB.setEditable(false);
				optionB.setDisable(false);
				answerB.setDisable(false);
				optionC.setDisable(true);
				optionC.setEditable(false);
				answerC.setDisable(true);
				optionD.setDisable(true);
				optionD.setEditable(false);
				answerD.setDisable(true);
			} else if (questionType.equals("Fill")) {
				optionA.setFont(Font.getDefault());
				optionA.setEditable(true);
				answerA.setDisable(true);
				optionB.setFont(Font.getDefault());
				optionB.setEditable(true);
				optionB.setDisable(false);
				answerB.setDisable(true);
				optionC.setDisable(false);
				optionC.setEditable(true);
				answerC.setDisable(true);
				optionD.setDisable(false);
				optionD.setEditable(true);
				answerD.setDisable(true);
			} else if (questionType.equals("Short")) {
				optionA.setFont(Font.getDefault());
				optionA.setEditable(true);
				answerA.setDisable(true);
				optionB.setFont(Font.getDefault());
				optionB.setEditable(false);
				optionB.setDisable(true);
				answerB.setDisable(true);
				optionC.setDisable(true);
				optionC.setEditable(false);
				answerC.setDisable(true);
				optionD.setDisable(true);
				optionD.setEditable(false);
				answerD.setDisable(true);
			}
	}
	


}
	  

	


