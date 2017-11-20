package application;

public class User {

		//member variables
		String userName;
		private String password;
		boolean isAdmin;

		//constructor
		User(String userName, String password, boolean isAdmin){
		 this.userName = userName;
		 this.password = password;
		 this.isAdmin = isAdmin;
		}

		 public String toString(){
		 return "Username: "+this.userName+"\n"+
		   "Is admin?: "+this.isAdmin;
		}

	
}
