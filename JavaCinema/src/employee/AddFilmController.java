package employee;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import sqlitedatabases.FilmsDatabase;
import sqlitedatabases.ScreeningsDatabase;
import user.AddDataToTable;

/**
 * Class gives the user the option to add a new film to the schedule.  If the film has not been screened before 
 * and so is not on the database it will be added.  The class contains a method to ensure the new screening does 
 * not overlap with other screenings.  If it does an error message is displayed and the film is not added to
 * the database.  The admin can also see the other films (and start and end dates) displayed on the day 
 * chosen to add the new film.   
 * 
 * @author nedlloyd and sam
 *
 */

public class AddFilmController {

	//constant representing one minute.  Used to obtain end time of a film by adding duration to start time
	static final long ONE_MINUTE_IN_MILLIS=60000;

	ObservableList<String> hourOptions = FXCollections.observableArrayList();
	ObservableList<String> minuteOptions = FXCollections.observableArrayList();

	@FXML ChoiceBox<String> filmTypeChoiceBox;
	@FXML DatePicker datePicker;
	@FXML ComboBox<String> startTimeHour;
	@FXML ComboBox<String> startTimeMinute;
	@FXML Button addScreeningBtn;
	@FXML Label chooseFilmLbl;
	@FXML Label screeningAlreadyInProgress;
	@FXML ChoiceBox<String> chooseFilmChoiceBox;
	@FXML TextField title;
	@FXML TextArea description;
	@FXML TextField duration;
	@FXML Button addImageButton;
	@FXML Label filmTitleLabel;
	@FXML Label filmDescriptionLabel;
	@FXML Label filmDurationLabel;
	@FXML Label filmImageLabel;
	@FXML TableView<AddDataToTable> currentScreenings;
	
	@FXML private TableColumn<AddDataToTable, String> filmNameColumn;
	@FXML private TableColumn<AddDataToTable, String> filmTime;
	@FXML private TableColumn<AddDataToTable, String> endTime;
	
	// observable list representing the screenings on day selected. Displayed in table. 
	ObservableList<AddDataToTable> todaysScreenings = FXCollections.observableArrayList();

	String filePath = "";


	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {
		
		currentScreenings.getStylesheets().add(getClass().getResource("/employee/tableview.css").toExternalForm());
		
		// populates table with current days screenings 
		LocalDate todaysDate = LocalDate.now(); 
		datePicker.setValue(todaysDate);
		String today = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
		setTodayScreenings(today);	
		currentScreenings.setItems(todaysScreenings);
		
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

		
		//Disables past dates in date picker
		final Callback<DatePicker, DateCell> dayCellFactory = 
				new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (item.isBefore(LocalDate.now())){
							setDisable(true);
							setStyle("-fx-background-color: gray;");
						}
					}
				};
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);

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

		//Hiding controls. ToggleControls method displays them again
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
		
		// initializes table columns
		filmNameColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmName"));
		filmTime.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmTime"));
		endTime.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("endTime"));
		
		// event listener for datePicker.  when date is changed films displayed are changed to that date.
		datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
			
		
				String theDate = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));

				setTodayScreenings(theDate);
				
				currentScreenings.setItems(todaysScreenings);

		});
		
		//end of initialize
	}
	/**
	 * When the user selects 'add new film' text boxes for title, description and duration and a button to add an image 
	 * are made visible.
	 * If select an existing film is chosen these are made invisible.
	 *  
	 * @param e
	 */
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
		} else /*if new adding screening of new film*/ {
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

	/**
	 * When 'Add Screening' button is pressed if the film is already in the database then the 'FilmsDatabase' and the
	 * 'ScreeningsDatabase' are queried in order to obtain information for the film screenings on that day and the 
	 * duration of the film being added. This is passed into the 'checkForOverlap' method.  If there is not overlap 
	 * the screening is added.  
	 * 
	 * If the film has not been screened before the information for the film duration is just obtained from the input
	 * form.  If there is overlap the film will not be added to the 'FilmDatabase' 
	 * 
	 * When a film is successfully added a message is displayed to notify the user.
	 * 
	 * @param e
	 */
	public void addScreening(ActionEvent e){

		String startDate = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
		String startTime = startTimeHour.getValue()+":"+startTimeMinute.getValue();
		String filmName = "";
		String filmDescription = "";
		int filmDuration = 0;
		filmName = chooseFilmChoiceBox.getValue();
		String successMessage = "Screening of " + filmName + " added at " + startTime + " on " + startDate;


		FilmsDatabase databaseFilms = new FilmsDatabase();
		ScreeningsDatabase databaseScreenings = new ScreeningsDatabase();
		try {
			if(filmTypeChoiceBox.getValue().equals("New Film")){//if film has not been screened before
				filmName = title.getText();
				filmDescription = description.getText();
				filmDuration = Integer.parseInt(duration.getText());

				boolean overlap = checkForOverlapDuration(filmName, startDate, startTime, filmDuration);

				if (!overlap) {
					databaseFilms.addFilm(filmName,  filmDescription, filmDuration, filePath);
					databaseScreenings.addScreening(filmName, startTime, startDate);
					screeningAlreadyInProgress.setText(successMessage);	
				}
			} else /*if film has been screened before*/{
				filmName = chooseFilmChoiceBox.getValue();
				boolean overlap = checkForOverlap(filmName, startDate, startTime);
				if (!overlap) {
					databaseScreenings.addScreening(filmName, startTime, startDate);
					screeningAlreadyInProgress.setText(successMessage);
				}		
			}

		}catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		setTodayScreenings(startDate);
	}

	/**
	 * On press of 'Add Image' button a 'file-chooser' box is opened.  
	 * The user selects a file and a message is displayed confirming that selection.
	 * @param e
	 */
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

	
	/**
	 * Takes parameters for two films.  The start (as String) and end (as Date) for one film and the the start (as String)
	 * and duration (as int) of another film.  The method uses these parameters to output a boolean which will be true if the two
	 * films overlap and false if not. 
	 * 
	 * @param currentStartTime
	 * @param currentDuration
	 * @param attemptStartTime
	 * @param attemptDuration
	 */
	public boolean compareTimes(String startCurrent, Date endCurrent, String attemptStart, int attemptDuration) {

		boolean conflict = false;
		
		Date startCurrentDate = convertToDateObject(startCurrent);		
		Date startAttemptDate = convertToDateObject(attemptStart);
		Date endAttemptDate = findEndTime(attemptStart, attemptDuration);


		
		if ((startAttemptDate.before(endCurrent) && startAttemptDate.after(startCurrentDate)) 
				|| (endAttemptDate.before(endCurrent) && endAttemptDate.after(startCurrentDate))) {

			conflict = true;
			// if the being suggested shares a start time or an end time with another film
		} else if (startAttemptDate.equals(startCurrentDate) || startAttemptDate.equals(endCurrent) 
				|| (endAttemptDate.equals(endCurrent) && endAttemptDate.equals(startCurrentDate))) {

			conflict = true;
		} else if(startAttemptDate.before(startCurrentDate)  && endAttemptDate.after(endCurrent)) {
			
			conflict = true;
		}

		return conflict;
		
	
	}


	/**
	 * Takes the 'film name', 'date' and 'time' of the screening the user is trying to add.  The method calls up the 'FilmsDatabase' 
	 * in order to get the duration of the film.  It then iterates over the other screenings taking places on this day and using 
	 * the method 'compareTimes' outputs a boolean which will return true if the screening will overlap and false if not.  If the 
	 * output is false an error message will also be displayed 
	 * to the user. 
	 * 
	 * @param filmNameAttempt
	 * @param dateAttempt
	 * @param timeAttempt
	 * @return
	 */
	public boolean checkForOverlap(String filmNameAttempt, String dateAttempt, String timeAttempt) {
		screeningAlreadyInProgress.setText("");

		boolean isOverlap = false;
		int attemptFilmDuration = 0;

		FilmsDatabase fd = new FilmsDatabase();

		
			ResultSet res2;
			try {
				res2 = fd.displayRow(filmNameAttempt);
				
				if (res2 != null) {
					attemptFilmDuration = res2.getInt("filmDuration");
					res2.close();
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			for (AddDataToTable item : todaysScreenings) {
				String filmName = item.getFilmName();
				String currentFilmTime = item.getFilmTime();
				String endTimeString = item.getEndTime();
				Date endTimeDate = item.getDateObjectEnd();
				
				String errorMessage = "sorry, " + filmName + " is starting at " + currentFilmTime +
						" and will go on until " + endTimeString + " please try another time";
				
				if (compareTimes(currentFilmTime, endTimeDate, timeAttempt, attemptFilmDuration)) {
					
					screeningAlreadyInProgress.setText(errorMessage);	
					isOverlap = true;
					break;										
				}
			}

		return isOverlap;
	}
	
	/**
	 *
	 * Takes the 'film name', 'date', 'duration' and 'time' of the screening the user is trying to add.  The method iterates over the other
	 * screenings taking places on this day and using the method 'compareTimes' outputs a boolean which will return true 
	 * if the screening will overlap and false if not.  If the output is false an error message will also be displayed 
	 * to the user. 
	 * 
	 * @param filmNameAttempt
	 * @param dateAttempt
	 * @param timeAttempt
	 * @param attemptDuration
	 * @return
	 */
	public boolean checkForOverlapDuration(String filmNameAttempt, String dateAttempt, String timeAttempt, int attemptDuration) {

		boolean isOverlap = false;


		for (AddDataToTable item : todaysScreenings) {
			String filmName = item.getFilmName();
			String currentFilmTime = item.getFilmTime();
			String endTimeString = item.getEndTime();
			Date currentEndTime = item.getDateObjectEnd();

			int attemptFilmDuration = attemptDuration;

			String errorMessage = "sorry, " + filmName + " is starting at " + currentFilmTime +
					" and will go on until " + endTimeString + " please try another time";

			if (compareTimes(currentFilmTime, currentEndTime, dateAttempt, attemptFilmDuration)) {

				screeningAlreadyInProgress.setText(errorMessage);	
				isOverlap = true;
				break;										
			}

		}

		return isOverlap;
	}

	/**
	 * Takes the date the user has selected, calls up the 'ScreeningsDatabase' and adds the screenings taking place on that day 
	 * to an Observable List called 'todaysScreenings'.  Each time it is called the list is first cleared to ensure it only 
	 * ever contains the screenings for the chosen day.   
	 * @param dateAttempt
	 */
	public void setTodayScreenings(String dateAttempt) {	
		
		todaysScreenings.clear();

		ScreeningsDatabase sd = new ScreeningsDatabase();

		ResultSet res1;
		
			try {
				res1 = sd.durationAndTime(dateAttempt);
				
				while (res1.next()) {

					String filmName = res1.getString("filmName");
					String currentFilmTime = res1.getString("time");
					int currentDuration = res1.getInt("filmDuration");

					Date endTime = findEndTime(currentFilmTime, currentDuration);
					String stringDate = dateToString(endTime);
					
					AddDataToTable nextFilm = new AddDataToTable(filmName, currentFilmTime, currentDuration, stringDate, endTime);
					todaysScreenings.add(nextFilm);
				}
				
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	
	/**
	 * Takes, as the parameters, the 'start' and 'duration' of a film and outputs the end of the film as a Date object
	 * @param start
	 * @param duration
	 * @return
	 */
	public Date findEndTime(String start, int duration) {
		Date filmStartTime = convertToDateObject(start);		
		long sum1 = filmStartTime.getTime() + (duration * ONE_MINUTE_IN_MILLIS);
		
		Date endTime = new Date(sum1);
		return endTime;
	}
	
	
	/**
	 * Takes a Date object as its parameter and converts it to a string
	 * @param date
	 * @return
	 */
	public String dateToString(Date date) {
		String DATE_FORMAT_NOW = "HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		String stringDate = sdf.format(date);
		
		return stringDate;
	}
	
	/**
	 * Takes a date as a String and converts it to a Date object. 
	 * @param time
	 * @return
	 */
	public Date convertToDateObject(String time) {

		DateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = sdf.parse(time);		
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;				
	}

}
