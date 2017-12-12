package user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sqlitedatabases.ReservationsDatabase;
import sqlitedatabases.ScreeningsDatabase;
import sqlitedatabases.UsersDatabase;

public class ViewProfileController {

	@FXML private TableView<AddDataToTable> tableView;
	@FXML private TableColumn<AddDataToTable, String> filmNameColumn;
	@FXML private TableColumn<AddDataToTable, Integer> reservationIDColumn;
	@FXML private TableColumn<AddDataToTable, String> filmTimeColumn;
	@FXML private TableColumn<AddDataToTable, String> filmDateColumn;
	@FXML private TableColumn<AddDataToTable, Integer> seatIDColumn;
	@FXML private ImageView filmImage;
	@FXML private Button deleteReservation;
	@FXML private Button editLogin;
	@FXML private Button allReservations;
	@FXML private Label name;
	@FXML private Label email;
	@FXML private Label filmDescription;
	@FXML private Label filmDuration;
	@FXML private DatePicker datePickerUser; 

	private ObservableList<AddDataToTable> films = FXCollections.observableArrayList();
	private ObservableList<AddImageToTable> someImages = FXCollections.observableArrayList();
	private String currentFilm;
	private String currentDescription;
	private String currentDate;
	private String currentUser;
	private String currentEmail;
	private int currentID;
	private int userID;


	public void initialize() {

		LocalDate todaysDate = LocalDate.now(); 		
		//Set datePicker value to today
		datePickerUser.setValue(todaysDate);

		//set up columns in the table
		reservationIDColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, Integer>("reservationID"));
		filmNameColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmName"));
		filmDateColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmDate"));
		filmTimeColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmTime"));
		seatIDColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, Integer>("seatID"));

		try {
			getFilms();
			tableView.setItems(films);
		} catch (ClassNotFoundException | SQLException | IOException e1) {
			e1.printStackTrace();
			System.out.println("Error populating table with reservation data. Error with getFilms() method");
		}
		
		
		// event listener for datePicker when date is changes films outputted are changed 
		datePickerUser.valueProperty().addListener((ov, oldValue, newValue) -> {
			allReservations.setVisible(true);
			try {
				currentDate = datePickerUser.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));

				//getFilms(currentDate);
				getFilms();
				tableView.setItems(films);

			} catch (IOException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		});

		//event listener setting 'currentFilm' to film selected, then using this to set the correct poster and description 
		tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			//sets image which is returned from the getImageFromTableMethod
			//TablePosition pos = tableView.getSelectionModel().getSelectedCells().get(0);
			int rowIndex = tableView.getSelectionModel().getSelectedIndex();
			// sets variable current film to the film selected 
			if(rowIndex >=0 && rowIndex < tableView.getItems().size()){
				currentFilm = tableView.getItems().get(rowIndex).getFilmName();
				currentID = tableView.getItems().get(rowIndex).getReservationID();
				try {
					setImageAndDescription();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	public void getFilms(String date) throws ClassNotFoundException, SQLException, IOException {	
		films.clear();
		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();

		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet res = screeningDatabase.queryForProfile(currentUser, date, userID);


		// populates two observableLists on with film data the other with film names and posters 		
		setVariablesFromQuery(res);		

	}


	/**
	 * queries the database for films booked  by certain user.  
	 * then calls the setVriablesFromQuery method to set the variables 'films' and 'someImages' to the current query
	 * @param date
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void  getFilms() throws ClassNotFoundException, SQLException, IOException {	

		films.clear();
		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();

		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet res = screeningDatabase.queryForProfile(userID);

		// populates two observableLists on with film data the other with film names and posters 		
		setVariablesFromQuery(res);		

	}


	/**
	 * takes variables from query and assigns them to two observable lists, 1 for 
	 * images and description the other for the table 
	 * @param res
	 * @throws SQLException
	 * @throws IOException
	 */
	public void setVariablesFromQuery(ResultSet res) throws SQLException, IOException {


		while (res.next()) {

			//creates variables for each field that we need for the Observable list and then the table
			String filmName = res.getString("filmName");
			String filmDate = res.getString("date");
			String filmTime = res.getString("time");
			String filmDescription = res.getString("filmDescription");
			String seatID = res.getString("seatID");
			int filmReservationID = res.getInt("reservationID");
			InputStream binaryStream = res.getBinaryStream("image");

			if (binaryStream != null) {
				addImageToList(binaryStream, filmName, filmDescription);
			} else {
				addImageToList(filmName, filmDescription);
			}

			//initialises AddDataToTable object with constructor that takes the variables name, time and description and id
			AddDataToTable nextObject = new AddDataToTable(filmName, filmTime, filmDate, seatID, filmReservationID);						
			//adds AddDataToTable objects to observable list 
			films.add(nextObject);
		}
		res.close();
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

		//image variable becomes the file we just wrote
		//Image image = new Image("file:photo.jpg", 100, 150, true, true);

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
		filmDescription.setText("");

		// image is initialised to curtains in case film does not contain a photo
		Image theImage = new Image("images/cinema_curtains.png", 100, 150, true, true);

		//returns the poster that matches the film name 
		for (AddImageToTable item : someImages) {
			if (item.getFilmName() == currentFilm) {
				if (item.getFilmImage() != null) {
					theImage = item.getFilmImage();
				}
				currentDescription = item.getFilmDescription();
				System.out.println(currentDescription);
				System.out.println(item.getFilmName());					
			} 

			filmImage.setImage(theImage);
			filmDescription.setText(currentDescription);
		}
	}


	/**
	 * method called in UserMainController to pass variable 'username' to this controller then 
	 * usees it to obtain userID and email both of which are set as variables on this controller
	 * @param user
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void setUserEmailName(String user) throws ClassNotFoundException, SQLException {
		UsersDatabase ud = new UsersDatabase();
		//finds the userID based on the user name
		ResultSet res = ud.displayRow(user);


		int id = 0;
		while (res.next()) {
			id = res.getInt("userID");
			currentEmail = res.getString("email");			
		}

		this.currentUser = user;
		this.userID = id;
		email.setText("Email: " + currentEmail);
		this.name.setText("Name: " + user);			
	}

	/**
	 * sets table to all reservations user has made and makes see all resevations button invisible
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void viewAllReservations() throws ClassNotFoundException, SQLException, IOException {
		//allReservations.setVisible(false);
		getFilms();
		tableView.setItems(films);
	}

	/**
	 * sets table to all reservations user has made and makes see all resevations button invisible
	 * based upon button click
	 * 
	 * @param e
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void viewAllReservations(ActionEvent e) throws ClassNotFoundException, SQLException, IOException {
		//allReservations.setVisible(false);
		getFilms();
		tableView.setItems(films);
	}

	/**
	 * deletes reservation selected on table then resets films without deleted item
	 * @param e
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void deleteReservation(ActionEvent e) throws ClassNotFoundException, SQLException, IOException {

		ReservationsDatabase rd = new ReservationsDatabase();
		rd.delete(currentID);
		getFilms();			
		tableView.setItems(films);

	}

	/**
	 * opens editUserInfoController
	 * @param e
	 */
	public void editUserInfo(ActionEvent e) {

		try {
			// variable screeningID set to currently selected films screening id
			Stage newEditProfileController = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/user/EditUserInfo.fxml").openStream());

			//calls up reservation controller allowing variables to be set from current controller

			EditUserProfileController eupc  = (EditUserProfileController)loader.getController();
			// uses the setScreening method from reservationController in order to pass the variable screeningID and set the seats bases on whether they are reserved

			eupc.setUsername(currentUser);
			eupc.setEmail(currentEmail);
			eupc.setPassword(currentUser);
			Scene scene = new Scene(root,500,320);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newEditProfileController.setScene(scene);
			newEditProfileController.setTitle("User Info");
			newEditProfileController.show(); 	

		} catch(Exception exc) {
			exc.printStackTrace();
		}


	}

}
