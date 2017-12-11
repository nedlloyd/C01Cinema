package user;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sqlitedatabases.UsersDatabase;

public class EditUserProfileController {
	@FXML private Button edit1;
	@FXML private Button edit2;
	@FXML private Button edit3;
	@FXML private Button confirm;

	
	@FXML private TextField username; 
	@FXML private TextField email; 
	@FXML private TextField passwoord; 
	
	@FXML private Label updatedUsername; 
	@FXML private Label updatedEmail; 
	@FXML private Label updatedPasswoord; 
	
	private String currentUser;

	
	public void initialize() {
		setPassword();
	}
	
	public void setUsername(String username) {
		this.username.setText(username);
		this.currentUser = username;
	}

	public void setEmail(String email) {
		this.email.setText(email);
	}

	public void setPassword() {
		
		String currentPassword = "";
		UsersDatabase ud = new UsersDatabase();
		try {	
			ResultSet res = ud.displayRow(currentUser);
			System.out.println(currentPassword);
			if(res != null) {
				currentPassword = res.getString("password");
				System.out.println(currentPassword);
			}
			res.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.passwoord.setText(currentPassword);
	}

}
