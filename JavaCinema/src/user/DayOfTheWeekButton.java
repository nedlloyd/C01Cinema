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
	
	/**
	 * initializes DayOfTheWeekObject with variables button and date
	 * 
	 * @param b
	 * @param d
	 */
	public DayOfTheWeekButton(Button b, LocalDate d){
		this.button = b;
		this.date = d;
	}
	
	/**
	 * returns button variable as Button
	 * 
	 * @return button
	 */
	public Button getButton(){
		return this.button;
	}
	
	/**
	 * returns Date variable as Date
	 * @return
	 */
	public LocalDate getDate(){
		return this.date;
	}
	
	
	
	
	

}
