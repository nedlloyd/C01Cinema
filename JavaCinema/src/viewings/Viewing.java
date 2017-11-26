package viewings;

import java.time.LocalDate;

public class Viewing {
	
	byte startTime;//HHmm
	LocalDate startDate; 
	Film film;
	
	public Viewing(Film film, byte startTime, LocalDate date){
		this.film = film;
		this.startTime = startTime;
		this.startDate = date;
			
	}
	

}
