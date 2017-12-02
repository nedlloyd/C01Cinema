package employee;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import sqlitedatabases.FilmsDatabase;
import sqlitedatabases.ScreeningsDatabase;

public class AddFilmController {

	ObservableList<String> hourOptions = FXCollections.observableArrayList();
	ObservableList<String> minuteOptions = FXCollections.observableArrayList();

	@FXML
	ChoiceBox<String> filmTypeChoiceBox;
	@FXML
	DatePicker datePicker;
	@FXML
	ComboBox<String> startTimeHour;
	@FXML
	ComboBox<String> startTimeMinute;
	@FXML 
	Button addScreeningBtn;

	@FXML
	Label chooseFilmLbl;
	@FXML
	ChoiceBox<String> chooseFilmChoiceBox;


	@FXML 
	TextField title;
	@FXML
	TextArea description;
	@FXML
	TextField duration;
	
	@FXML
	Button addImageButton;

	@FXML
	Label filmTitleLabel;
	@FXML
	Label filmDescriptionLabel;
	@FXML
	Label filmDurationLabel;
	@FXML 
	Label filmImageLabel;
	
	String filePath = "/Users/nedlloyd/Desktop/vertigo.png";



	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {
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

		//Adding options to box which selects whether we are screening a film which
		//has been previously screened or a new film
		filmTypeChoiceBox.getItems().add("New Film");
		filmTypeChoiceBox.getItems().add("Repeat Screening of film");
		filmTypeChoiceBox.setValue("Repeat Screening of film");

		//Populating the chooseFilmChoiceBox from previously screened films in the database
		FilmsDatabase films = new FilmsDatabase();
		ResultSet filmNames = films.displayColumns("filmName"); 
		while(filmNames.next()){
			chooseFilmChoiceBox.getItems().add(filmNames.getString("filmName"));
		}

		//Hiding controls. Toggle controls displays them again
		filmDescriptionLabel.setVisible(false);
		filmTitleLabel.setVisible(false);
		filmDurationLabel.setVisible(false);
		filmImageLabel.setVisible(false);

		title.setVisible(false);
		title.setDisable(false);

		description.setVisible(false);
		description.setDisable(false);

		addImageButton.setVisible(false);
		addImageButton.setDisable(true);

		duration.setVisible(false);
		duration.setDisable(true);
	}

	public void toggleControls(ActionEvent e){
		if(filmTypeChoiceBox.getValue().equals("Repeat Screening of film")){

			filmDescriptionLabel.setVisible(false);
			filmTitleLabel.setVisible(false);
			filmDurationLabel.setVisible(false);
			filmImageLabel.setVisible(false);

			title.setVisible(false);
			title.setDisable(false);

			description.setVisible(false);
			description.setDisable(false);

			addImageButton.setVisible(false);
			addImageButton.setDisable(true);

			duration.setVisible(false);
			duration.setDisable(true);	

			//Displaying the repeated screening
			chooseFilmLbl.setVisible(true);
			chooseFilmChoiceBox.setVisible(true);
			chooseFilmChoiceBox.setDisable(false);
		} else {
			filmDescriptionLabel.setVisible(true);
			filmTitleLabel.setVisible(true);
			filmDurationLabel.setVisible(true);
			filmImageLabel.setVisible(true);

			title.setVisible(true);
			title.setDisable(false);

			description.setVisible(true);
			description.setDisable(false);

			addImageButton.setVisible(true);
			addImageButton.setDisable(false);

			duration.setVisible(true);
			duration.setDisable(false);

			chooseFilmLbl.setVisible(false);
			chooseFilmChoiceBox.setVisible(false);
			chooseFilmChoiceBox.setDisable(true);

		}
	}

	public void addScreening(ActionEvent e){

		FilmsDatabase databaseFilms = new FilmsDatabase();
		ScreeningsDatabase databaseScreenings = new ScreeningsDatabase();
		try {
			if(filmTypeChoiceBox.getValue().equals("New Film")){//if film has not been screened before
				databaseFilms.addFilm(title.getText(),  description.getText(), filePath);
		
				//ADD BIT FOR IMAGE/DURATION^
				databaseScreenings.addScreening(title.getText(), startTimeHour.getValue()+startTimeMinute.getValue(), datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy")));

			} else /*if film has been screened before*/{
				databaseScreenings.addScreening(chooseFilmChoiceBox.getValue(), startTimeHour.getValue()+startTimeMinute.getValue(), datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
			}	

		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}
	
	public void addImage(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
          
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        filePath = file.getAbsolutePath();
        System.out.print(filePath);
        	}



}
