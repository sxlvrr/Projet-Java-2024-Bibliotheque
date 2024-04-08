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
		Connection connection = null;
		try {
			connection = Database.getConnection();
			List<Livre> livres = new ArrayList<>();
			livres = Livre.fetchBooksFromDatabase(connection);
			Database.closeConnection(connection);
			
			Livre livre = livres.get(0);
			
			User user = User.verifyPassword("client@client.fr", "client");
			
			
			Emprunter emprunt = new Emprunter(livre, user);
			System.out.println(emprunt);
			//emprunt.emprunterUnLivre();
			emprunt.rendreUnLivre();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            }
        });
        
        /*
		Connection connection = null;
		try {
			connection = Database.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//List<Livre> listLivre = Livre.fetchBooksFromDatabase(connection);
		//System.out.println(listLivre);
		
		//Auteur auteur = Auteur.fetchAuthorById(1, connection);
		//System.out.println(auteur);
		
		//Stock stock = Stock.fetchStockById(1, connection);
		//System.out.println(stock);
	}
}
