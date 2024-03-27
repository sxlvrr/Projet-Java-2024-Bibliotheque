import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TestMain {

	public static void main(String[] args) {
		
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
