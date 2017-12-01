package user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sqlitedatabases.FilmsDatabase;
import sqlitedatabases.ScreeningsDatabase;

public class UserMainController {
	
	@FXML
	private Label helloMessage;
	
	@FXML
	private Button seeScreenings;
	
	@FXML
	private Label viewingsUser;
	
	@FXML
	private DatePicker datePickerUser; 
	
	@FXML 
	private TableView<AddDataToTable> tableView;
	
	@FXML 
	private TableColumn<AddDataToTable, String> filmNameColumn;
	
	@FXML 
	private TableColumn<AddDataToTable, String> filmDescriptionColumn;
	
	@FXML 
	private TableColumn<AddDataToTable, String> filmTimeColumn;
	

	@FXML
	void initialize() throws ClassNotFoundException, SQLException{	
		
		//Display current time
		datePickerUser.setValue(LocalDate.now());
		
		//set up columns in the table
		filmNameColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmName"));
        filmDescriptionColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmDescription"));
        filmTimeColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmTime"));
        
        
	}
	
	
	// creates and returns an observable of the films on a certain date this can then be added to the table
	public ObservableList<AddDataToTable>  getFilms() throws ClassNotFoundException, SQLException
	{	
		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();

		//initialises observable list 
		ObservableList<AddDataToTable> films = FXCollections.observableArrayList();

		//gets the date from the date picker in the correct format
		String date = datePickerUser.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
		

		// creates a result set by calling  the detDataFromTwoTables method present in ScreeningDatabase
		ResultSet res = screeningDatabase.getDataFromTwoTables("films", "screenings", date);

		try {
			while (res.next()) {

				//creates variables for each field that we need for the Observable list and then the table
				String name = res.getString("filmName");
				String description = res.getString("filmDescription");
				String time = res.getString("time");

				//initialises AddDataToTable object with constructor that takes the variables we just intialised
				AddDataToTable i = new AddDataToTable(name, description, time);

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

	
	
	// when get screenings button is pressed the table is updated with the films on that day
	public void getScreenings(ActionEvent e) throws ClassNotFoundException, SQLException{
		
		tableView.setItems(getFilms());
	}
	
}
