package user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sqlitedatabases.ScreeningsDatabase;


public class UserMainController {

	private String currentFilm;
	private int screeningID;
	private String user;
	private int dayTracker;

	@FXML private Label titleLbl;  @FXML private Label lbl1;
	@FXML public Label helloMessage;

	@FXML private Button todayBtn; 	@FXML private Button dayOfWeekBtn1; 
	@FXML private Button dayOfWeekBtn2; @FXML private Button dayOfWeekBtn3;
	@FXML private Button dayOfWeekBtn4; @FXML private Button dayOfWeekBtn5;
	@FXML private Button dayOfWeekBtn6;

	DayOfTheWeekButton[] dayButtonArray = new DayOfTheWeekButton[7];

	@FXML private Button seeScreenings;
	@FXML private Label viewingsUser; 
	@FXML private DatePicker datePickerUser; 
	@FXML private TableView<AddDataToTable> tableView;
	@FXML private TableColumn<AddDataToTable, String> filmNameColumn;
	@FXML private TableColumn<AddDataToTable, String> filmDescriptionColumn;
	@FXML private TableColumn<AddDataToTable, String> filmTimeColumn;
	@FXML private ImageView filmImage;
	@FXML private Button makeReservation;
	@FXML private Button viewProfile;
	@FXML private Label selectFilm;
	@FXML private Label filmDescription;





	private ObservableList<AddImageToTable> someImages = FXCollections.observableArrayList();
	private ObservableList<AddDataToTable> films = FXCollections.observableArrayList();
	private String currentDescription;


	@FXML
	public void initialize() throws ClassNotFoundException, SQLException{	

		//LINK UP TO DATEPICKER SO DATEPICKER CHANGES COLOR OF DAY TAGS
		dayButtonArray[0] =  new DayOfTheWeekButton(todayBtn,LocalDate.now());
		dayButtonArray[1] =  new DayOfTheWeekButton( dayOfWeekBtn1,LocalDate.now().plusDays(1));
		dayButtonArray[2] =  new DayOfTheWeekButton( dayOfWeekBtn2,LocalDate.now().plusDays(2));
		dayButtonArray[3] =  new DayOfTheWeekButton( dayOfWeekBtn3,LocalDate.now().plusDays(3));
		dayButtonArray[4] =  new DayOfTheWeekButton( dayOfWeekBtn4,LocalDate.now().plusDays(4));
		dayButtonArray[5] =  new DayOfTheWeekButton( dayOfWeekBtn5,LocalDate.now().plusDays(5));
		dayButtonArray[6] =  new DayOfTheWeekButton( dayOfWeekBtn6,LocalDate.now().plusDays(6));


		LocalDate todaysDate = LocalDate.now(); 
		DayOfWeek day = todaysDate.getDayOfWeek();

		//Set datePicker value to today
		datePickerUser.setValue(todaysDate);

		//Set day of week buttons accordingly
		dayTracker = day.getValue();
		changeDatePickerAction(todayBtn, 0);

		dayTracker++;
		loopDayTracker(dayTracker);
		dayOfWeekBtn1.setText("Tomorrow");
		changeDatePickerAction(dayOfWeekBtn1, 1);

		dayTracker++;
		loopDayTracker(dayTracker);
		String twoDaysAway = DayOfWeek.of(dayTracker).getDisplayName(TextStyle.FULL, Locale.UK);
		dayOfWeekBtn2.setText(twoDaysAway);
		changeDatePickerAction(dayOfWeekBtn2, 2);

		dayTracker++;
		loopDayTracker(dayTracker);
		String threeDaysAway = DayOfWeek.of(dayTracker).getDisplayName(TextStyle.FULL, Locale.UK);
		dayOfWeekBtn3.setText(threeDaysAway);
		changeDatePickerAction(dayOfWeekBtn3, 3);

		dayTracker++;
		loopDayTracker(dayTracker);
		String fourDaysAway = DayOfWeek.of(dayTracker).getDisplayName(TextStyle.FULL, Locale.UK);
		dayOfWeekBtn4.setText(fourDaysAway);
		changeDatePickerAction(dayOfWeekBtn4, 4);

		dayTracker++;
		loopDayTracker(dayTracker);
		String fiveDaysAway = DayOfWeek.of(dayTracker).getDisplayName(TextStyle.FULL, Locale.UK);
		dayOfWeekBtn5.setText(fiveDaysAway);
		changeDatePickerAction(dayOfWeekBtn5, 5);

		dayTracker++;
		loopDayTracker(dayTracker);
		String sixDaysAway = DayOfWeek.of(dayTracker).getDisplayName(TextStyle.FULL, Locale.UK);
		dayOfWeekBtn6.setText(sixDaysAway);
		changeDatePickerAction(dayOfWeekBtn6, 6);

		//set up columns in the table
		filmNameColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmName"));
		filmDescriptionColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmDescription"));
		filmTimeColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmTime"));

		// event listener for datePicker when date is changes films outputted are changed 
		datePickerUser.valueProperty().addListener((ov, oldValue, newValue) -> {
			try {
				String theDate = datePickerUser.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
				getFilms(theDate);
				tableView.setItems(films);
				//Selecting day of datePicker changes which day button is highlighted: 
				for(int i = 0 ; i < dayButtonArray.length ; i++){
					//Selected day has yellow text
					if(datePickerUser.getValue().equals(dayButtonArray[i].getDate())){
						dayButtonArray[i].getButton().setStyle("-fx-text-fill: yellow;"+
								"-fx-background-color: none;");
					}else{ 	//Non selected days have white text 
						dayButtonArray[i].getButton().setStyle("-fx-text-fill: white;"+
								"-fx-background-color: none;");
					}
				}

			} catch (IOException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		});

		//event listener setting 'currentFilm' to film selected, then using this to set the correct poster 
		tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {      	
			try {
				//sets image which is returned from the getImageFromTableMethod
				TablePosition pos = tableView.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				// sets variable current film to the film selected 
				currentFilm = tableView.getItems().get(row).getFilmName();
				//sets image based on film selected
				setImageAndDescription(); 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});  

	}
	
	/**
	 * queries the database for films booked on a certain date and by certain user (and therfore userID).  
	 * then calls the setVriablesFromQuery method to set the variables 'films' and 'someImages' to the current query
	 * @param date
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void getFilms(String date) throws ClassNotFoundException, SQLException, IOException
	{	
		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();

		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet res = screeningDatabase.getDataFromTwoTables("films", "screenings", date);
		films.clear();
		
		// populates two observableLists on with film data the other with film names and posters 		
		setVariablesFromQuery(res);		

	}
	
	/**
	 * takes variables from query and asigns them to two observable lists, 1 for 
	 * images and description the other for the table 
	 * @param res
	 * @throws SQLException
	 * @throws IOException
	 */
	public void setVariablesFromQuery(ResultSet res) throws SQLException, IOException {
		
		films.clear();
		
		while (res.next()) {

			//creates variables for each field that we need for the Observable list and then the table
			String filmName = res.getString("filmName");
			String filmDate = res.getString("date");
			String filmTime = res.getString("time");
			String filmDescription = res.getString("filmDescription");
			String id = res.getString("screeningID");
			InputStream binaryStream = res.getBinaryStream("image");
			
			
			if (binaryStream != null) {
				addImageToList(binaryStream, filmName, filmDescription);
			} else {
				addImageToList(filmName, filmDescription);
			}
								
			//initialises AddDataToTable object with constructor that takes the variables name, time and description and id
			AddDataToTable nextObject = new AddDataToTable(filmName, filmTime, id);						
			//adds AddDataToTable objects to observable list 
			films.add(nextObject);
		}
	}
	
	/**
	 * converts binary stream to image, creates an addImageToTable object and adds object to observableList
	 * @param binaryStream
	 * @param filmName
	 * @param description
	 * @throws IOException
	 */
	public void addImageToList(InputStream binaryStream, String filmName, String description) throws IOException {
		//ensures that there is a photo for a certain film 
		if (binaryStream != null) {
			//creates file 
			OutputStream os = new FileOutputStream (new File("photo.jpg"));
			byte[] content = new byte[1024];
			int size = 0;
			//while binary Stream holds more than -1
			while ((size = binaryStream.read(content)) != -1) {
				//os becomes an image
				os.write(content, 0, size);
			}
			os.close();
			binaryStream.close();

			//image variable becomes the file we just wrote
			Image image = new Image("file:photo.jpg", 100, 150, true, true);
			
			//image becomes an AddImageToTable
			AddImageToTable nextImage = new AddImageToTable(image, filmName, description);
			//it is then added to an observableList 
			someImages.add(nextImage);
		}				
	}
	
	/**
	 * if film does not have an image in the database, creates an addImageToTable object and adds object to observableList
	 * @param filmName
	 * @param description
	 * @throws IOException
	 */
	public void addImageToList(String filmName, String description) throws IOException {

		//image becomes an AddImageToTable
		AddImageToTable nextImage = new AddImageToTable(filmName, description);
		//it is then added to an observableList 
		someImages.add(nextImage);
	}	
	
	/**
	 * sets image and description based upon current film selected
	 * @throws IOException
	 */
	public void setImageAndDescription() throws IOException {
		
		//clears the previous film and description
		filmImage.setImage(null);

		// image is initialised to curtains in case film does not contain a photo
		Image theImage = new Image("images/cinema_curtains.png", 100, 150, true, true);

		//returns the poster that matches the film name 
		for (AddImageToTable item : someImages) {
			if (item.getFilmName() == currentFilm) {
				if (item.getFilmImage() != null) {
					theImage = item.getFilmImage();
				}					
				currentDescription = item.getFilmDescription();
			} 			
			filmImage.setImage(theImage);
			filmDescription.setText(currentDescription);
		}
	}
			
	/**
	 *  when the make a reservation button is pressed the make a reservation window is opened
	 * 	variables of filmName, screeningid and user are also passed onto the next controller
	 * @param e
	 */
	public void makeReservation(ActionEvent e) {
		String theDate = datePickerUser.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
		try {
			// variable screeningID set to currently selected films screening id
			setScreeningID();
			if (screeningID != 0) {
				Stage newReservationStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Parent root = loader.load(getClass().getResource("/user/MakeReservation.fxml").openStream());

				//calls up reservation controller allowing variables to be set from current controller
				MakeReservationController reservationController = (MakeReservationController)loader.getController();
				// uses the setScreening method from reservationController in order to pass the variable screeningID and set the seats bases on whether they are reserved
				reservationController.setSeats(screeningID);
				//does the same but with user
				reservationController.setUser(user);

				//Update label with film title and viewing time/date on new window
				try {
					ScreeningsDatabase sdb = new ScreeningsDatabase();
					ResultSet screeningResult = sdb.displayRow(screeningID);
					String filmName = screeningResult.getString("filmName");
					String time = screeningResult.getString("time");
					String date = screeningResult.getString("date");

					reservationController.filmLabel.setText(filmName);
					reservationController.timeLabel.setText(date+" "+time);

				} catch (SQLException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}


				Scene scene = new Scene(root,500,500);
				//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				newReservationStage.setScene(scene);
				newReservationStage.setTitle("Select a seat");
				newReservationStage.show(); 	
			} else {
				selectFilm.setText("please select a screening");
			}

		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}


	/**
	 *  Sets the screeningID variable of the class corresponding to the film selected
	 */
	public void setScreeningID() {

		for (AddDataToTable item : films) {
			if (item.getFilmName() == currentFilm) {
				Integer si = Integer.parseInt(item.getScreeningID());
				screeningID = si;
			} 
		}
	}

	/**
	 * method in order to receive variable username from login controller and
	 * set it as a variable 
	 * @param user
	 */
	public void setUser(String user) throws ClassNotFoundException, SQLException{
		this.user = user;
	}

	/**
	 * If the day tracker exceeds 7 (i.e. goes beyond Sunday), 
	 * return it to the start of the week (back to Monday)
	 * @param dayTracker
	 */
	private void loopDayTracker(int dayTracker){
		if(dayTracker>7){
			this.dayTracker = 1;
		}
	}

	/**
	 * Changed the value of th datePicker when button is clicked
	 * @param b
	 * @param value
	 */
	private void changeDatePickerAction(Button b, int value){
		b.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				datePickerUser.setValue(LocalDate.now().plusDays(value));
				for(int i = 0; i < dayButtonArray.length; i++){
					dayButtonArray[i].getButton().setStyle("-fx-text-fill: white;"+
							"-fx-background-color: none;");
				}
				b.setStyle("-fx-text-fill: yellow;"+
						"-fx-background-color: none;");
			}
		});	
	}
	
	
	public void viewProfile(ActionEvent e) {
		
		try {
			// variable screeningID set to currently selected films screening id
				Stage newProfileController = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Parent root = loader.load(getClass().getResource("/user/ViewProfile.fxml").openStream());

				//calls up reservation controller allowing variables to be set from current controller
				ViewProfileController vp  = (ViewProfileController)loader.getController();
				// uses the setScreening method from reservationController in order to pass the variable screeningID and set the seats bases on whether they are reserved
				vp.setUserEmailName(user);
				vp.viewAllReservations();

				
				Scene scene = new Scene(root,500,500);
				//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				newProfileController.setScene(scene);
				newProfileController.setTitle("User Info");
				newProfileController.show(); 	

		} catch(Exception exc) {
			exc.printStackTrace();
		}
				
			
	}
	





}
