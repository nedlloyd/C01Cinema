package employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
import sqlitedatabases.ScreeningsDatabase;
import user.AddDataToTable;

public class AdminController {
	
	@FXML 
	private Button addScreeningButton;
	
	@FXML
	private Label viewingsLbl;
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private Button showScreenings;
	
	@FXML 
	private TableView<AddDataToTable> tableView;
	
	@FXML 
	private TableColumn<AddDataToTable, String> filmNameColumn;
	
	@FXML 
	private TableColumn<AddDataToTable, String> filmDescriptionColumn;
	
	@FXML 
	private TableColumn<AddDataToTable, String> filmTimeColumn;
	
	@FXML 
	void initialize(){
		datePicker.setValue(LocalDate.now());	
		//viewingsLbl.setText("Viewings on "+datePicker.getValue().toString());
		//Display viewings on that date
		
		filmNameColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmName"));
        filmDescriptionColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmDescription"));
        filmTimeColumn.setCellValueFactory(new PropertyValueFactory<AddDataToTable, String>("filmTime"));
	}
	
	public void changeViewingDayListings(ActionEvent e){
		viewingsLbl.setText("Viewings on "+datePicker.getValue().toString());
		//Change to viewings on the given day
	}

	public void openAddNewScreeningWindow(ActionEvent e){
	
		try {
			Stage newScreeningStage = new Stage();
		    Parent root = FXMLLoader.load(getClass().getResource("/employee/AddScreeningForm.fxml"));
			Scene scene = new Scene(root,500,500);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			newScreeningStage.setScene(scene);
			newScreeningStage.setTitle("New Screening");
			newScreeningStage.show(); 	
			
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		
		
	}
	
	public ObservableList<AddDataToTable>  getFilms() throws ClassNotFoundException, SQLException
	{	
		ScreeningsDatabase screeningDatabase = new ScreeningsDatabase();

		//initialises observable list 
		ObservableList<AddDataToTable> films = FXCollections.observableArrayList();

		//gets the date from the date picker in the correct format
		String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
		System.out.print(date);

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
	
	public void getScreenings(ActionEvent e) throws ClassNotFoundException, SQLException {
		tableView.setItems(getFilms());
	}
	
	

}
