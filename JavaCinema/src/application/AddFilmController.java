package application;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import viewings.Film;
import viewings.Viewing;

public class AddFilmController{
	
	ObservableList<String> hourOptions = FXCollections.observableArrayList();
	ObservableList<String> minuteOptions = FXCollections.observableArrayList();
	
	@FXML 
	TextField title;
	@FXML
	TextArea description;
	@FXML
	DatePicker datePicker;
	@FXML
	ComboBox<String> startTimeHour;
	@FXML
	ComboBox<String> startTimeMinute;
	@FXML
	TextField duration;

	@FXML
	public void initialize() {
		//Adding available start hours for the film
		for(int hour = 0; hour<24; hour++){
			String hourOption = Integer.toString(hour);
			if(hour<10){
				hourOption = "0"+hour;
			}
			hourOptions.add(hourOption);
		}
		startTimeHour.setItems(hourOptions);
		
		//Adding start minutes (can start at any multiple of 5 past the hour)
		for(int min = 0; min<12; min++){
			String minuteOption = Integer.toString(min*5);
			if(min<2){
				minuteOption = "0"+(min*5);
			}
			minuteOptions.add(minuteOption);
		}
		startTimeMinute.setItems(minuteOptions);
		
		datePicker.setValue(LocalDate.now());
		
	}

	void addScreening(ActionEvent e){
		
		Film newFilm = new Film(title.getText(), description.getText(), Integer.parseInt(duration.getText()));
		
		LocalDate date = datePicker.getValue();
		
		
	}



}
