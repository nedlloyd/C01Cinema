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
import java.util.ArrayList;
import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import sqlitedatabases.ScreeningsDatabase;

/**
 * Controller class responsible for updating the content on the customer window (UserMain.fxml). 
 * Contains methods for displaying films on a selected date, and launches new windows
 * for making reservation and viewing user info. 
 */
public class UserMainController {

	private String currentFilm;
	private int screeningID;
	private String user;
	private int dayTracker;

	@FXML private Label titleLbl;  @FXML private Label lbl1;
	@FXML public Label helloMessage;

	//Day of the week buttons provide an alternative way of selecting date for screenings. Instead of 
	//having to click the DatePicker, the user can click a button representing a day in the next week. 
	@FXML private Button todayBtn; 	@FXML private Button dayOfWeekBtn1; 
	@FXML private Button dayOfWeekBtn2; @FXML private Button dayOfWeekBtn3;
	@FXML private Button dayOfWeekBtn4; @FXML private Button dayOfWeekBtn5;
	@FXML private Button dayOfWeekBtn6;
	DayOfTheWeekButton[] dayButtonArray = new DayOfTheWeekButton[7];

	@FXML private Button seeScreenings;
	@FXML private Label viewingsUser; 
	@FXML private DatePicker datePickerUser; 
	@FXML private Button viewProfile;
	@FXML private Button logOutBtn;

	@FXML private ScrollPane scrollPane;
	@FXML private VBox filmDisplayVbox;
	@FXML private Label noScreeningsLbl;

	//Contains all the data for the films 
	private ObservableList<AddDataToTable> films = FXCollections.observableArrayList();

	@FXML
	public void initialize() throws ClassNotFoundException, SQLException{	

		filmDisplayVbox.setSpacing(50);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		noScreeningsLbl.setVisible(false);

		//LINK UP TO DATEPICKER SO DATEPICKER CHANGES COLOR OF DAY TAGS
		dayButtonArray[0] =  new DayOfTheWeekButton( todayBtn,LocalDate.now());
		dayButtonArray[1] =  new DayOfTheWeekButton( dayOfWeekBtn1,LocalDate.now().plusDays(1));
		dayButtonArray[2] =  new DayOfTheWeekButton( dayOfWeekBtn2,LocalDate.now().plusDays(2));
		dayButtonArray[3] =  new DayOfTheWeekButton( dayOfWeekBtn3,LocalDate.now().plusDays(3));
		dayButtonArray[4] =  new DayOfTheWeekButton( dayOfWeekBtn4,LocalDate.now().plusDays(4));
		dayButtonArray[5] =  new DayOfTheWeekButton( dayOfWeekBtn5,LocalDate.now().plusDays(5));
		dayButtonArray[6] =  new DayOfTheWeekButton( dayOfWeekBtn6,LocalDate.now().plusDays(6));

		LocalDate todaysDate = LocalDate.now(); 
		DayOfWeek day = todaysDate.getDayOfWeek();

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

		//Disables past dates
		final Callback<DatePicker, DateCell> dayCellFactory = 
				new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (item.isBefore(todaysDate)){
							setDisable(true);
							setStyle("-fx-background-color: gray;");
						}

					}
				};
			}
		};
		datePickerUser.setDayCellFactory(dayCellFactory);

		// event listener for datePicker when date is changes films outputted are changed 
		datePickerUser.valueProperty().addListener((ov, oldValue, newValue) -> {
			try {
				String theDate = datePickerUser.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));

				getFilms(theDate);

				displayFilms();

				if(films.isEmpty()){
					noScreeningsLbl.setVisible(true);
					filmDisplayVbox.getChildren().add(noScreeningsLbl);
				}else{
					noScreeningsLbl.setVisible(false);
				}

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

		//Set datePicker value to today
		datePickerUser.setValue(todaysDate);

	}//End of initialize() method

	/**
	 * Queries the database for films booked on a certain date.  For each film on the date selected the method then 
	 * creates an AddDataToTable object. The variables 'filmName', 'filmTime', 'filmDescription' and 'binaryStream' 
	 * (after being converted to an image) are assigned to it.  The object is then added to the ObservableList 'films'.
	 * 
	 * @param date
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void getFilms(String date) throws ClassNotFoundException, SQLException, IOException{	

		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();

		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet res = screeningDatabase.getDataFromTwoTables("films", "screenings", date);
		films.clear();

		while (res.next()) {

			//creates variables for each field that we need for the Observable list and then the table
			String filmName = res.getString("filmName");
			String filmTime = res.getString("time");
			String filmDescription = res.getString("filmDescription");
			int id = res.getInt("screeningID");
			InputStream binaryStream = res.getBinaryStream("image");

			//initializes AddDataToTable object with constructor that takes the variables name, time and description and id
			AddDataToTable nextObject = new AddDataToTable(filmName,filmDescription, filmTime, id);			

			//converts binaryStream into Image
			if (binaryStream != null) {
				//creates file 
				OutputStream os = new FileOutputStream (new File("photo1.jpg"));
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
				Image image = new Image("file:photo1.jpg", 100, 150, true, true);
				ImageView imageView = new ImageView();
				imageView.setImage(image);

				nextObject.setFilmImage(imageView);

				//adds AddDataToTable objects to observable list 
				films.add(nextObject);
			}
		}
	}

	/**
	 *  When a screening button is pressed, the make-a-reservation window is opened and
	 *  the variables of filmName, screeningId and user are passed onto the next 'MakeReservationController'.
	 * @param e
	 */
	public void makeReservation() {
		try {
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

				//Passing data onto reservationController window
				reservationController.setDate(date);
				reservationController.setTime(time);
				reservationController.setFilmName(filmName);
				reservationController.filmLabel.setText(filmName);
				reservationController.timeLabel.setText(time+" "+date);

				Scene scene = new Scene(root,500,500);
				newReservationStage.setScene(scene);
				newReservationStage.setTitle("Select a seat: "+filmName+" "+time+" "+date);
				newReservationStage.show(); 

			} catch (SQLException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}

		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Method in order to receive variable username from login controller class and
	 * set it accordingly. 
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
	 * Changed the value of the datePicker when button is clicked
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

	/**
	 * Opens a new window with user details
	 * @param e
	 */
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
			newProfileController.setScene(scene);
			newProfileController.setTitle("User Info");
			newProfileController.show(); 	

		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * For every film stored as an AddDataToTable object in the 'films' ArrayList, 
	 * we create a childpane and add to to a VBox. When the datepicker is actioned, 
	 * displayFilms() is triggered, and the films are displayed for that date. 
	 * changes.
	 */
	public void displayFilms(){

		filmDisplayVbox.getChildren().clear();

		//ArrayList to keep track of whether there are multiple screenings of one film on one date
		ArrayList<Pane> filmPanes = new ArrayList<Pane>();

		for(int i = 0; i < films.size();i++){

			Pane sameFilmPane = new Pane();
			boolean repeatedScreening = false;

			String filmTitle = films.get(i).getFilmName();


			for(int j = 0; j < filmPanes.size(); j++){
				if(filmPanes.get(j).getId().equals(filmTitle)){
					repeatedScreening = true;
					sameFilmPane = filmPanes.get(j);
					break;
				}
			}

			//If the film is a repeated screening, instead of creating a whole new pane for it, just add an 
			//additional button the the existing pane for that film.
			if(repeatedScreening){

				String currentfilm = sameFilmPane.getId();

				Button screeningTimebutton = new Button();
				screeningTimebutton.setText(films.get(i).getFilmTime());
				screeningTimebutton.setId(Integer.toString(films.get(i).getScreeningID()));

				//The button is the last item in the Pane's getChildren() list.
				int buttonIndex = sameFilmPane.getChildren().size() - 1;
				double position = sameFilmPane.getChildren().get(buttonIndex).getLayoutX();
				//Place the position of the new button to the right of the other button.
				screeningTimebutton.setLayoutX(position+60);

				screeningTimebutton.setLayoutY(120);
				screeningTimebutton.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

				//The booking buttons trigger the makeReservation() method: 
				screeningTimebutton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						screeningID = Integer.parseInt(screeningTimebutton.getId());
						currentFilm = currentfilm;
						makeReservation();
					}
				});

				sameFilmPane.getChildren().addAll(screeningTimebutton);

			}else/*if the film is the first in the list of screenings for that film on the selected date*/{

				//Create a new Pane and add it to the VBox for each film in the 'films' 
				//ArrayList on that date. Within that pane there is a title label, 
				//a description text area, and booking buttons.
				Pane filmPane = new Pane();
				filmPane.setPrefSize(631, 150);
				filmPane.setId(filmTitle);
				filmPanes.add(filmPane);

				//Film title label
				Label titleLbl = new Label();
				titleLbl.setText(filmTitle);
				titleLbl.setLayoutX(120);
				titleLbl.setStyle("	-fx-text-fill:white;"
						+ "-fx-font-family:Impact;"
						+ "-fx-font-size: 22px");

				//Film description text area
				TextArea descriptionArea = new TextArea();
				descriptionArea.setWrapText(true);
				descriptionArea.setEditable(false);
				descriptionArea.setMaxHeight(90);
				descriptionArea.setMaxWidth(500);
				descriptionArea.setText(films.get(i).getFilmDescription());
				descriptionArea.setLayoutX(120);
				descriptionArea.setLayoutY(30);
				descriptionArea.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

				//Film image
				ImageView pic = films.get(i).getFilmImage();

				//Booking button for screening
				Button screeningTimebutton = new Button();
				screeningTimebutton.setText(films.get(i).getFilmTime());
				screeningTimebutton.setId(Integer.toString( films.get(i).getScreeningID()));
				screeningTimebutton.setLayoutX(120);
				screeningTimebutton.setLayoutY(120);
				screeningTimebutton.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				//The booking buttons trigger the makeReservation() method: 
				screeningTimebutton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						screeningID = Integer.parseInt(screeningTimebutton.getId());
						currentFilm = filmPane.getId();
						makeReservation();
					}
				});

				//It is important that the screeningTimebutton is added to filmPane LAST in next line:
				filmPane.getChildren().addAll(titleLbl, descriptionArea, pic, screeningTimebutton);
				filmDisplayVbox.getChildren().add(filmPane);

			}	
		}
	}

	/**
	 *  Logs out of user portal (i.e. closes user portal window)
	 *  and re-opens login window. 
	 *  Triggered when Log out button is pressed.
	 */
	public void logOut(ActionEvent e){
		try {
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
			Scene scene = new Scene(root,800,500);
			primaryStage.setScene(scene);
			primaryStage.setTitle("NEAM-Arts-Cinema Login");
			primaryStage.show();
			logOutBtn.getScene().getWindow().hide(); //Close employee portal

		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}



}
