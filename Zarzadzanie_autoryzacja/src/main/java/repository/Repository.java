package repository;

import domain.User;

public interface Repository {
	String addUser(User User);
	String removeUser(User User);
	String updateUser(User User); 
	String selectUser(User User);
   // void createTable(User User);
}
