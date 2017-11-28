package employee;

import java.time.LocalDate;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminController {
	
	@FXML 
	Button addScreeningButton;
	
	@FXML
	Label viewingsLbl;
	
	@FXML
	DatePicker datePicker; 
	
	@FXML 
	void initialize(){
		datePicker.setValue(LocalDate.now());	
		viewingsLbl.setText("Viewings on "+datePicker.getValue().toString());
		//Display viewings on that date
	}
	
	public void changeViewingDayListings(ActionEvent e){
		viewingsLbl.setText("Viewings on "+datePicker.getValue().toString());
		//Change to viewings on the given day
	}

	public void openAddNewScreeningWindow(ActionEvent e){
	
		try {
			Stage newScreeningStage = new Stage();
		    Parent root = FXMLLoader.load(getClass().getResource("/employee/AddScreeningForm.fxml"));
			Scene scene = new Scene(root,500,500);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newScreeningStage.setScene(scene);
			newScreeningStage.setTitle("New Screening");
			newScreeningStage.show(); 	
			
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		
		
	}
	
	

}
