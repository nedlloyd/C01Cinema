package user;

import java.time.LocalDate;

import javafx.scene.control.Button;

/**
 * Class used for the day selector buttons in the user portal. 
 * Each day in the next 7 days has a corresponding DayOfTheWeekButton. 
 *
 */
public class DayOfTheWeekButton {
	
	Button button;
	LocalDate date;
	
	public DayOfTheWeekButton(Button b, LocalDate d){
		this.button = b;
		this.date = d;
	}
	
	public Button getButton(){
		return this.button;
	}
	
	public LocalDate getDate(){
		return this.date;
	}
	
	
	
	
	

}
