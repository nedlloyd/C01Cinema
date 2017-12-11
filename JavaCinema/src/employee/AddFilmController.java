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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import sqlitedatabases.FilmsDatabase;
import sqlitedatabases.ScreeningsDatabase;
import user.AddDataToTable;


public class AddFilmController {

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

	String filePath = "/Users/nedlloyd/Desktop/vertigo.png";

	private Date currentStartTime;
	private Date currentEndTime;
	private Date attemptStartTime;
	private Date attemptEndTime;

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
	 * Takes data entered in AddFilm form and adds a new screening to the screenings database.
	 * @param e
	 */
	public void addScreening(ActionEvent e){

		String startDate = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
		String startTime = startTimeHour.getValue()+":"+startTimeMinute.getValue();
		String filmName = "";
		String filmDescription = "";
		int filmDuration = 0;
		filmName = chooseFilmChoiceBox.getValue();
		System.out.println("filmname afterpicker " + filmName);
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


	/**
	 * method converts date as string object to date as Date Object 
	 * @param time
	 * @return
	 */
	public Date convertToDateObject(String time) {

		DateFormat sdf = new SimpleDateFormat("hh:mm");
		Date date = null;
		try {
			date = sdf.parse(time);		
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;				
	}


	/**
	 * 
	 * sets variables based upon the start and end of the film suggested and a film already in the database
	 * 
	 * @param currentStartTime
	 * @param currentDuration
	 * @param attemptStartTime
	 * @param attemptDuration
	 */
	public void setStartAndEnd(String currentStartTime, int currentDuration, String attemptStartTime, int attemptDuration) {

		this.currentStartTime = convertToDateObject(currentStartTime);		
		long sum1 = this.currentStartTime.getTime() + (currentDuration * ONE_MINUTE_IN_MILLIS);
		this.currentEndTime = new Date(sum1);

		this.attemptStartTime = convertToDateObject(attemptStartTime);
		long sum2 = this.attemptStartTime.getTime() + (attemptDuration * ONE_MINUTE_IN_MILLIS);
		this.attemptEndTime = new Date(sum2);
	}


	/**
	 * checks if the current film being added will overlap with any films already in the database
	 * @param filmNameAttempt
	 * @param dateAttempt
	 * @param timeAttempt
	 * @return
	 */
	public boolean checkForOverlap(String filmNameAttempt, String dateAttempt, String timeAttempt) {

		screeningAlreadyInProgress.setText("");

		boolean isOverlap = false;
		String filmName = "";
		String currentFilmTime = "";
		int currentFilmDuration = 0;
		int attemptFilmDuration = 0;

		ScreeningsDatabase sd = new ScreeningsDatabase();
		FilmsDatabase fd = new FilmsDatabase();

		try {
			ResultSet res2 = fd.displayRow(filmNameAttempt);
			ResultSet res1 = sd.durationAndTime(dateAttempt);

			if (res2 != null) {
				attemptFilmDuration = res2.getInt("filmDuration");
				res2.close();
			}

			while (res1.next()) {

				filmName = res1.getString("filmName");
				currentFilmTime = res1.getString("time");
				currentFilmDuration = res1.getInt("filmDuration");

				setStartAndEnd(currentFilmTime, currentFilmDuration, timeAttempt, attemptFilmDuration);

				@SuppressWarnings("deprecation")
				String filmFinishString = currentEndTime.getHours() + ":" + currentEndTime.getMinutes();

				String errorMessage = "Sorry, " + filmName + " is starting at " + currentFilmTime +
						" and will go on until " + filmFinishString + ". Please try another time.";

				// if the film being suggested end or stats in another film
				if ((attemptStartTime.before(currentEndTime) && attemptStartTime.after(currentStartTime)) 
						|| (attemptEndTime.before(currentEndTime) && attemptEndTime.after(currentStartTime))) {

					screeningAlreadyInProgress.setText(errorMessage);
					isOverlap = true;
					break;
					// if the being suggested shares a start time or an end time with another film
				} else if (attemptStartTime.equals(currentStartTime) || attemptStartTime.equals(currentEndTime) 
						|| (attemptEndTime.equals(currentEndTime) && attemptEndTime.equals(currentStartTime))) {

					screeningAlreadyInProgress.setText(errorMessage);
					isOverlap = true;
					break;
				}
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isOverlap;
	}

	public boolean checkForOverlapDuration(String filmNameAttempt, String dateAttempt, String timeAttempt, int attemptDuration) {

		boolean isOverlap = false;
		String filmName = "";
		String currentFilmTime = "";
		int currentFilmDuration = 0;
		int attemptFilmDuration = 0;

		ScreeningsDatabase sd = new ScreeningsDatabase();

		attemptFilmDuration = attemptDuration;

		ResultSet res1;
		try {
			res1 = sd.durationAndTime(dateAttempt);

			while (res1.next()) {

				filmName = res1.getString("filmName");
				currentFilmTime = res1.getString("time");
				currentFilmDuration = res1.getInt("filmDuration");

				setStartAndEnd(currentFilmTime, currentFilmDuration, timeAttempt, attemptFilmDuration);

				String filmFinishString = currentEndTime.getHours() + ":" + currentEndTime.getMinutes();

				String errorMessage = "sorry, " + filmName + " is starting at " + currentFilmTime +
						" and will go on until " + filmFinishString + " please try another time";

				// if the film being suggested end or stats in another film
				if ((attemptStartTime.before(currentEndTime) && attemptStartTime.after(currentStartTime)) 
						|| (attemptEndTime.before(currentEndTime) && attemptEndTime.after(currentStartTime))) {

					screeningAlreadyInProgress.setText(errorMessage);
					isOverlap = true;
					break;
					// if the being suggested shares a start time or an end time with another film
				} else if (attemptStartTime.equals(currentStartTime) || attemptStartTime.equals(currentEndTime) 
						|| (attemptEndTime.equals(currentEndTime) && attemptEndTime.equals(currentStartTime))) {

					screeningAlreadyInProgress.setText(errorMessage);
					isOverlap = true;
					break;
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isOverlap;
	}


}
