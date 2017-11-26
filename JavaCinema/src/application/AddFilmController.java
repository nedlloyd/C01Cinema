package application;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import viewings.Film;
import viewings.Viewing;

public class AddFilmController {

	@FXML 
	TextField title;

	@FXML
	TextArea description;

	@FXML
	DatePicker datePicker;

	@FXML
	ComboBox startTimeHour;
	@FXML
	ComboBox startTimeMinute;

	@FXML
	TextField duration;


	@FXML
	public void initialize() {

		for(byte hourCounter = 0; hourCounter < 24; hourCounter++){

			String hourOptions = Byte.toString(hourCounter);

			if(hourCounter<10){
				hourOptions = "0"+hourCounter;
			}
			startTimeHour.getItems().add(hourOptions);
		}
		
		for(byte minCounter = 0; minCounter < 24; minCounter++){

			String minuteOptions = Byte.toString(minCounter);

			if(minCounter<10){
				minuteOptions = "0"+minCounter;
			}
			startTimeMinute.getItems().add(minuteOptions);
		}



	}

	void createViewing(ActionEvent e){
	
		LocalDate date = datePicker.getValue();

	}




}
