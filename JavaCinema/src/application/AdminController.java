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
	void initialize(){
		datePicker.setValue(LocalDate.now());
	}

	public void openAddNewScreeningWindow(ActionEvent e){
		
		Stage newScreeningStage = new Stage();
		try {	
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AddFilmForm.fxml"));
		    Parent root = loader.load();
			
			Scene scene = new Scene(root,500,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newScreeningStage.setScene(scene);
			newScreeningStage.show(); 	
			
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		
		
	}
	
	

}
