package services;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
@WebListener
public class StartUpListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		
        System.out.println("Shutting down!");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		
		System.out.println("Starting up!");
		try {
			StartUpRoutine.setUpDb();
			StartUpRoutine.setUpAdmin();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Start up error, check listener");
		}
	}

}
