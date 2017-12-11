package employee;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sqlitedatabases.UsersDatabase;

public class AddNewEmployeeController {
	
	@FXML private TextField newEmployeeUsername;
	@FXML private TextField newEmployeeEmail;
	@FXML private TextField newEmployeePassword;
	@FXML private Button createEmployeeBtn;
	
	/**
	 * Adds a new employee account to the database. This method is accessed through
	 * the NewEmployee.fxml file when the createEmployeeBtn button is pressed. 
	 * @param e
	 */
	public void createEmployeeAccount(ActionEvent e){
		String username = newEmployeeUsername.getText();
		String email = newEmployeeEmail.getText();
		String pw = newEmployeePassword.getText();

		UsersDatabase users = new UsersDatabase();

		try {
			if(username.isEmpty()==false && email.isEmpty()==false && pw.isEmpty() == false){
				users.createUser(username, pw, email, "employee");
				newEmployeeUsername.getScene().getWindow().hide();
			}

		} catch (ClassNotFoundException | SQLException e1) {
			System.out.println("Error adding new employee to database."
					+ "Error thrown by createEmployeeAccount() method in AdminController class");
			e1.printStackTrace();
		}

	}

}
