package viewings;

import java.time.LocalDate;

public class Viewing {
	
	int startTimeHour;
	int startTimeMin;
	LocalDate startDate; 
	Film film;
	
	public Viewing(Film film, int startTimeHour, int startTimeMin, LocalDate date){
		this.film = film;
		this.startTimeHour = startTimeHour;
		this.startTimeMin = startTimeMin;
		this.startDate = date;
			
	}
	

}
