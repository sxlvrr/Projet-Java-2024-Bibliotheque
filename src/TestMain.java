import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


public class TestMain {

	public static void main(String[] args) {
		/*
		//TEST ENTIER
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            }
		});
       
       */
	
		//TEST PAGE EMPRUNT
        SwingUtilities.invokeLater(new Runnable() {
        	public void run() {
        	User user = User.verifyPassword("admin@admin.fr", "admin"); 	
        	ListeEmprunt listeEmprunt= new ListeEmprunt(user);
            listeEmprunt.setVisible(true);
        	}
        });
		
	}
}
