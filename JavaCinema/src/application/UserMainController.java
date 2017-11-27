package application;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UserMainController {
	
	@FXML
	private Label HelloMessage;
	
	@FXML
	private Button seeScreenings;
	
	public void initialize() {

		HelloMessage.setText("Hello, ");
		
	}
	
	
	public void openAddNewScreeningWindow(ActionEvent e){
		
		Stage newScreeningStage = new Stage();
		try {	
			Parent root = FXMLLoader.load(getClass().getResource("/application/UserFilmScreening.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newScreeningStage.setScene(scene);
			newScreeningStage.show();
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		
		
	}
	
}
