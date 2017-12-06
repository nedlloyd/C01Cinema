package application;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sqlitedatabases.UsersDatabase;
import user.UserMainController;

public class LoginController {

	@FXML
	private Label lblstatus;
	@FXML 
	private Label lbl2;
	@FXML
	private TextField txtUserName;
	@FXML
	private TextField txtPassword;
	@FXML
	private TextField newUserName;
	@FXML
	private TextField newPassword;

	private String usernameAttempt;
	private String passwordAttempt;

	/**
	 * Adds a new user to users database when they sign up
	 * @param e
	 */
	public void signUp(ActionEvent e){
		String username = newUserName.getText();
		String password = newPassword.getText();

		UsersDatabase users = new UsersDatabase();
		try {
			users.createUser(username, password, "customer");
			
			lblstatus.getScene().getWindow().hide();
			//Launch user/customer portal after sign up
			Stage userStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/user/UserMain.fxml").openStream());
			
			//calls userMainController in order to pass variables
			UserMainController userMain = (UserMainController)loader.getController();
			//passes usernameAttempt to userMainController
			userMain.setUser(username);
			
			Scene scene = new Scene(root,600,600);
			userStage.setScene(scene);
			userStage.setTitle("Cinema Booking");
			userStage.show();
			lblstatus.getScene().getWindow().hide();

		} catch (ClassNotFoundException | SQLException e1) {
			System.out.println("Error with sign up process. Error adding to users database.");
			e1.printStackTrace();
		} catch(IOException e2){
			System.out.println("Error with launching customer portal.");
			e2.printStackTrace();
		}

	}

	public void login(ActionEvent e) throws SQLException{

		usernameAttempt = txtUserName.getText();
		passwordAttempt = txtPassword.getText();

		UsersDatabase users = new UsersDatabase();
		ResultSet res;
		try {
			res = users.displayRow(usernameAttempt); //Fetches the user details from database
			String pw = res.getString("password");
			String role = res.getString("role");

			//Check to see if password matched
			if(passwordAttempt.equals(pw)){
				//Check to see whether the user is an employee/admin or a customer
				if(role.equals("employee")||role.equals("admin")){

					//Launch admin portal
					try{
						Stage adminStage = new Stage();
						Parent root = FXMLLoader.load(getClass().getResource("/employee/AdminMain.fxml"));
						Scene scene = new Scene(root,600,400);
						adminStage.setScene(scene);
						adminStage.setTitle("Cinema Employee Portal");
						adminStage.show();
						lblstatus.getScene().getWindow().hide();
					}
					catch(Exception ex){
						ex.printStackTrace();
						System.out.println("Launching employee portal failed: "+ex);
					}
				}
				else /*if user is a customer*/{

					//Launch user/customer portal
					try{
						Stage userStage = new Stage();
						FXMLLoader loader = new FXMLLoader();
						Parent root = loader.load(getClass().getResource("/user/UserMain.fxml").openStream());
						
						//calls userMainController in order to pass variables
						UserMainController userMain = (UserMainController)loader.getController();
						//passes usernameAttempt to userMainController
						userMain.setUser(usernameAttempt);
						Scene scene = new Scene(root,700,600);
						userStage.setScene(scene);
						userStage.setTitle("Cinema Booking");
						userStage.show();
						lblstatus.getScene().getWindow().hide();
					}
					catch(Exception ex){
						ex.printStackTrace();
						System.out.println("Launching Customer Scene failed: "+ex);
					}
				}

			} else{
				lblstatus.setText("Incorrect password");
			}

		} catch (ClassNotFoundException ex) {
			lblstatus.setText("User name not recognised.");
		}
		catch (SQLException ex) {
			lblstatus.setText("User name not recognised.");
		}
	}





}
