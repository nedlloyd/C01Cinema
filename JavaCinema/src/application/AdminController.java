package application;

import java.time.LocalDate;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class AdminController {
	
	@FXML 
	Button addScreeningButton;
	
	@FXML
	DatePicker datePicker; 
	
	@FXML
	ComboBox addScreening;
	

//	void showViewings(ActionEvent e){
//		LocalDate date = datePicker.getValue();
//		
//		
//	}
//	
	public void openAddNewScreeningWindow(ActionEvent e){
		
		Stage newScreeningStage = new Stage();
		try {	
			Parent root = FXMLLoader.load(getClass().getResource("/application/AddFilmForm.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newScreeningStage.setScene(scene);
			newScreeningStage.show();
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		
		
	}
	
	

}
