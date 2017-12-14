package employee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import sqlitedatabases.ReservationsDatabase;
import sqlitedatabases.ScreeningsDatabase;
import user.AddDataToTable;
import user.AddImageToTable;
import user.MakeReservationController;

/**
 * Controller class responsible for updating the content of the employee window (Adminmain.fxml). 
 * Contains methods for displaying films on a selected date, logging out and exporting data in a text file 
 * and opening windows to add new employee and add new viewing to schedule. 
 *
 */

public class AdminController {

	@FXML private Label viewingsLbl;
	@FXML private Label titleLbl; 

	@FXML private Label chooseDateLbl;
	@FXML private DatePicker datePicker;
	@FXML private TableView<AddDataToTable> tableView;
	@FXML private TableColumn<AddDataToTable, ImageView> pictureColumn;
	@FXML private TableColumn<AddDataToTable, String> filmNameColumn;
	@FXML private TableColumn<AddDataToTable, String> filmDescriptionColumn;
	@FXML private TableColumn<AddDataToTable, String> filmTimeColumn;
	@FXML private TableColumn<AddDataToTable, Integer> availableSeatsColumn;
	@FXML private ImageView filmImage;
	@FXML private Label dataExportSucessLbl;

	@FXML private Button writeData;
	@FXML private Button logOutButton;
	@FXML private Button OpenNewEmployeeWindowBtn;
	@FXML private Button addScreeningButton;

	private ObservableList<AddImageToTable> someImages = FXCollections.observableArrayList();

	/**
	 * Controller initialized.  The table columns are set, event listeners for the date and the film selected 
	 * (in order to dispplay the films on the current date and the film poster for the selected film) and number
	 * of available seats is calculated and displayed on a label.  Variables are also passed to the reservations 
	 * controller
	 * 
	 */
	@FXML 
	void initialize(){

		tableView.getStylesheets().add(getClass().getResource("/employee/tableview.css").toExternalForm());

		//Setting up columns in screenings table
		filmNameColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmName"));
		filmDescriptionColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmDescription"));
		filmTimeColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmTime"));
		availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, Integer>("availableSeats"));
		pictureColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, ImageView>("filmImage"));

		//Wrapping text in description cell so it will always fit inside the cell.
		//Found at https://stackoverflow.com/questions/22732013/javafx-tablecolumn-text-wrapping
		filmDescriptionColumn.setCellFactory(new Callback<TableColumn<AddDataToTable, String>, TableCell<AddDataToTable, String>>() {
			@Override
			public TableCell<AddDataToTable, String> call(TableColumn<AddDataToTable, String> param) {

				TableCell<AddDataToTable, String> cell = new TableCell<>();
				Text text = new Text();
				cell.setGraphic(text);
				cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
				text.wrappingWidthProperty().bind(cell.widthProperty());
				text.textProperty().bind(cell.itemProperty());

				text.setFill(Color.WHITE);
				cell.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
				return cell ;
			}
		});


		// event listener for datePicker when date is changes films displayed are changed 
		datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
			try {

				String theDate = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
				tableView.setItems(getFilms(theDate));

			} catch (IOException | ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} 
		});

		//Set the current date to now
		datePicker.setValue(LocalDate.now());	
		viewingsLbl.setText("Viewing Screenings on "+datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"))+":");

		filmImage.setImage(new Image("images/cinemaCurtains.png"));

		//create even listener on table so by selecting row you can view the film poster
		tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {      	
			try {
				//sets image which is returned from the getImageFromTableMethod
				filmImage.setImage(getImageFromTable()); 

				Stage screeningDataStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Parent root = loader.load(getClass().getResource("/employee/ScreeningData.fxml").openStream());

				//calls up reservation controller allowing variables to be set from current controller
				MakeReservationController reservationController = (MakeReservationController)loader.getController();
				// uses the setScreening method from reservationController in order to pass the variable screeningID and set the seats bases on whether they are reserved
				int screeningID = tableView.getSelectionModel().getSelectedItem().getScreeningID();
				reservationController.setSeats(screeningID);
				//does the same but with user;

				//Update label with film title and viewing time/date on new window
				try {
					ScreeningsDatabase sdb = new ScreeningsDatabase();
					ResultSet screeningResult = sdb.displayRow(screeningID);
					String filmName = screeningResult.getString("filmName");
					String time = screeningResult.getString("time");
					String date = screeningResult.getString("date");

					ReservationsDatabase reservationsDatabase = new ReservationsDatabase();
					ResultSet reservationsResultSet = reservationsDatabase.displayRows(screeningID);
					int reservationCount = reservationsDatabase.countRowsInResultSet(reservationsResultSet);
					int availableSeats = 50 - reservationCount;

					reservationController.filmLabel.setText(filmName);
					reservationController.timeLabel.setText(date+" "+time);
					reservationController.availableSeatsLbl.setText(Integer.toString(availableSeats));
					reservationController.bookedSeatsLbl.setText(Integer.toString(reservationCount));

					Scene scene = new Scene(root,500,500);
					scene.getStylesheets().add(getClass().getResource("tableview.css").toExternalForm());
					screeningDataStage.setScene(scene);
					screeningDataStage.setTitle(filmName+" "+time+" "+date+" Booking data");
					screeningDataStage.show(); 	

				} catch (SQLException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

	}

	/**
	 * When date on datePicker is changed the method sets text on the viewingsLbl to display the selected date.
	 * 
	 * Triggered by datePicker
	 * @param e
	 */
	public void changeViewingDayListings(ActionEvent e){
		viewingsLbl.setText("Viewings on "+datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"))+":");
		//Change to viewings on the given day
	}

	/**
	 * 
	 * Triggered by addScreeningButton. Opens window with form for adding films to schedule
	 * @param e
	 */
	public void openAddNewScreeningWindow(ActionEvent e){

		try {
			Stage newScreeningStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/employee/AddScreeningForm.fxml"));
			Scene scene = new Scene(root,750,520);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newScreeningStage.setScene(scene);
			newScreeningStage.setTitle("New Screening");
			newScreeningStage.show(); 	

		} catch(Exception exc) {
			exc.printStackTrace();
		}


	}

	/**
	 * Executes joint query to 'FilmsDatabase' and 'screeningsDatabase'in order to obtain the 'filmName', 'filmDescription', 'time' 
	 * and 'screeningId' for films on the date selected on the datePicker.  For each film on the selected date an AddDataToTable 
	 * object is created with these fields.  Each of these objects is then added to an ObservableList called 'films'.
	 * This list is then returned.   
	 * 
	 * @return ObservableList of films screening on the day selected  
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ObservableList<AddDataToTable>  getFilms() throws ClassNotFoundException, SQLException
	{	
		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();

		//initialises observable list 
		ObservableList<AddDataToTable> films = FXCollections.observableArrayList();

		//gets the date from the date picker in the correct format
		String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));

		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet res = screeningDatabase.getDataFromTwoTables("films", "screenings", date);

		try {
			while (res.next()) {

				//creates variables for each field that we need for the Observable list and then the table
				String name = res.getString("filmName");
				String description = res.getString("filmDescription");
				String time = res.getString("time");
				int screeningId = res.getInt("screeningID");

				//initialises AddDataToTable object with constructor that takes the variables we just intialised
				AddDataToTable i = new AddDataToTable(name, description, time, screeningId);

				//adds AddDataToTable objects to observable list 
				films.add(i);


			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error with populating table form database");
		}

		//returns the observable list 
		return films;
	}


	/**
	 * Executes joint query to 'FilmsDatabase' and 'screeningsDatabase'in order to obtain the 'filmName', 'filmDescription', 'time', 
	 * 'screeningId' and 'image' (of the poster) for films on the date selected.  
	 * 
	 * For each film on the selected date an AddDataToTable object is created with the first four of these parameters.  The variables 
	 * are also passed to 'calculateAndSetAvailableSeatsCount' method which counts available seats.  For 'image' the 'Input Stream' 
	 * obtained from the database is converted into an Image object.  An AddImageToTable object is 
	 * created with the film name and the image.  
	 * 
	 * The AddImageToTable is added to an ObservableList called 'someImages' and the AddDataToTable object is added to an 
	 * ObservableList called 'films'.  Finally the ObservableList 'films' is returned.
	 * 
	 * 
	 * @param date
	 * @return ObservableList films
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public ObservableList<AddDataToTable>  getFilms(String date) throws ClassNotFoundException, SQLException, IOException{	
		ObservableList<AddDataToTable> films = FXCollections.observableArrayList();

		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();
		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet screeningsResultSet = screeningDatabase.getDataFromTwoTables("films", "screenings", date);

		ReservationsDatabase reservationsDatabase = new ReservationsDatabase();

		// populates two observableLists one with film data the other with film names and posters 
		try {
			while (screeningsResultSet.next()) {

				//creates variables for each field that we need for the AddDataToTable and the AddImageToTable objects
				String name = screeningsResultSet.getString("filmName");
				String description = screeningsResultSet.getString("filmDescription");
				String time = screeningsResultSet.getString("time");
				int screeningId = screeningsResultSet.getInt("screeningID");

				InputStream binaryStream = screeningsResultSet.getBinaryStream("image");

				//initializes AddDataToTable object with constructor that takes the variables name, time, description and screeningID
				AddDataToTable nextObject = new AddDataToTable(name, description, time, screeningId);

				//available seats calculated
				nextObject.calculateAndSetAvailableSeatsCount(reservationsDatabase, screeningId, 50);

				//converts binaryStream to Image
				if (binaryStream != null) {
					OutputStream os = new FileOutputStream (new File("photo.jpg"));
					byte[] content = new byte[1024];
					int size = 0;
					while ((size = binaryStream.read(content)) != -1) {
						os.write(content, 0, size);
					}
					os.close();
					binaryStream.close();

					//image variable becomes the file we just wrote
					Image image = new Image("file:photo.jpg", 100, 150, true, true);
					ImageView imageView = new ImageView();
					imageView.setImage(image);

					nextObject.setFilmImage(imageView);

					//image becomes an AddImageToTable
					AddImageToTable nextImage = new AddImageToTable(image, name);
					//it is then added to an observableList 
					someImages.add(nextImage);
				}				
				//adds AddDataToTable objects to observable list 
				films.add(nextObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("error with populating table form database");
		}
		//ObservableList returned
		return films;
	}

	// finds out what row of table is selected, gets the name of the film and returns the correct poster
	/**
	 * Image object is first initialized as a picture of curtains.  Then the index of the row selected is found
	 * and used to find the name of the film selected from the ObservableList 'films'.  
	 * 
	 * The AddImageToTable objects in the ObservableList 'someImages' is iterated over.  If the film selected 
	 * from the table shares a name with an AddImageToTable object then the curtains image is changed to poster 
	 * for that film.  
	 * 
	 * Finally, if the image has been changed that image is returned.  If not the image of curtains is returned. 
	 * 
	 * @return returns Image Selected on table
	 * @throws IOException
	 */
	public Image getImageFromTable() throws IOException {

		// image is initialised to curtains in case film does not contain a photo
		Image theImage = new Image("images/cinemaCurtains.png");

		if(tableView.getSelectionModel().getSelectedIndex() >= 0){
			TablePosition pos = tableView.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();

			// gets film name of object on that row 
			String fn = tableView.getItems().get(row).getFilmName();

			//returns the poster that matches the film name 
			for (AddImageToTable item : someImages) {
				if (item.getFilmName() == fn) {
					theImage = item.getFilmImage();
				} 
			}
		}
		
		return theImage;
	}

	/**
	 * Triggered when writeData Button is pressed in the AdminMain window.
	 * Exports screenings data to text file in format: film name, date, time, seats booked, available seats. 
	 * 
	 * The file is saved in the user's home directory, and confirmation is printed to the console. 
	 * @param e
	 */
	public void writeDataToFile(ActionEvent e) {

		//Gets the user's home-directory to save the file in.
		String userHomeFolder = System.getProperty("user.home");

		File fileName = new File (userHomeFolder, "screening_data.txt");
		ScreeningsDatabase sd = new ScreeningsDatabase();
		ReservationsDatabase rd = new ReservationsDatabase();
		ResultSet res1 = null;
		ResultSet res2 = null;		

		try {
			res1 = sd.getAll();
			PrintWriter outputStream = new PrintWriter(fileName);
			while(res1.next()) {

				String name = res1.getString("filmName");
				String time = res1.getString("time");
				String date = res1.getString("date");
				int screeningID = res1.getInt("screeningID");

				res2 = rd.displayRows(screeningID);
				int reservationCount = rd.countRowsInResultSet(res2);
				int availableSeats = 50 - reservationCount;

				outputStream.println(name + " " + date + " " + time + " seatsbooked: " + reservationCount + 
						" availableSeats: " + availableSeats);				
			}			
			outputStream.close();

			System.out.println("New file created: "+fileName.toString());

			dataExportSucessLbl.setText("New file "+fileName.toString() + " created.");

			writeData.setStyle("-fx-background-color:green;"
					+ "-fx-font-color:white;"
					+ "-fx-border-color:white;"
					+ "-fx-border-radius:4px;");


		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}


	/**
	 *  Logs out of employee portal (i.e. closes employee portal window)
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
			logOutButton.getScene().getWindow().hide(); //Close employee portal

		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Opens a new window for creating a new employee account and adding it to database.
	 * @param e
	 */
	public void openAddEmployeeWindow(ActionEvent e){
		try {
			Stage addEmployeeStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/employee/AddNewEmployee2.fxml"));
			Scene scene = new Scene(root,400,250);
			addEmployeeStage.setScene(scene);
			addEmployeeStage.setTitle("Create New Employee Account");
			addEmployeeStage.show();

		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}






}
