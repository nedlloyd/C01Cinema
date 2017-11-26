package viewings;

public class Film {
	
	String title;
	String description;
	//image?
	int duration; //Duration of film in minutes
	
	//Constructor
	public Film(String title, String description, int duration /*, image*/){
		this.title = title;
		this.description = description;
		this.duration = duration;
	}
	
	Film(){}
	
	int getDuration(){
		return duration;
	}
	

}
