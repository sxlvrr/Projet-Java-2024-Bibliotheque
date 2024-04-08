import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stock {
	private int idStock;
	private int nbTotal;
	private int nbDisponible;
	private String ISBN;
	
	/**
	 * @param idStock
	 * @param nbTotal
	 * @param nbDisponible
	 * @param ISBN
	 */
	public Stock(int idStock, int nbTotal, int nbDisponible, String ISBN) {
		super();
		this.idStock = idStock;
		this.nbTotal = nbTotal;
		this.nbDisponible = nbDisponible;
		this.ISBN = ISBN;
	}
	
	public int getIdStock() {
		return idStock;
	}
	public void setIdStock(int idStock) {
		this.idStock = idStock;
	}
	public int getNbTotal() {
		return nbTotal;
	}
	public void setNbTotal(int nbTotal) {
		this.nbTotal = nbTotal;
	}
	public int getNbDisponible() {
		return nbDisponible;
	}
	public void setNbDisponible(int nbDisponible) {
		this.nbDisponible = nbDisponible;
	}
	public String getISBN() {
		return this.ISBN;
	}
	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

	@Override
	public String toString() {
		return "Stock [idStock=" + idStock + ", nbTotal=" + nbTotal + ", nbDisponible=" + nbDisponible + "ISBN=" + ISBN +"]";
	}
	
	public static Stock fetchStockById(int idStock, Connection connection) {
		Stock stock = null;
        
        try {
            // Préparation de la requête SQL pour récupérer les informations sur le stock
            String query = "SELECT * FROM stock WHERE idStock = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idStock);

            // Exécution de la requête
            ResultSet resultSet = statement.executeQuery();

            // Vérification s'il y a un résultat
            if (resultSet.next()) {
                // Récupération des données du stock
                int nbTotal = resultSet.getInt("nbTotal");
                int nbDisponible = resultSet.getInt("nbDisponible");
                String ISBN = resultSet.getString("ISBN");

                // Création de l'objet Stock avec les informations récupérées
                stock = new Stock(idStock, nbTotal, nbDisponible, ISBN);
            }

            // Fermeture des ressources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stock;
    }
	
	private static Stock fetchStockByISBN(String ISBN, Connection connection) {
		Stock stock = null;
		try {
            // Préparation de la requête SQL pour récupérer les informations sur le stock
            String query = "SELECT * FROM stock WHERE ISBN = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ISBN);

            // Exécution de la requête
            ResultSet resultSet = statement.executeQuery();

            // Vérification s'il y a un résultat
            if (resultSet.next()) {
                // Récupération des données du stock
            	int idStock = resultSet.getInt("idStock");
            	int nbTotal = resultSet.getInt("nbTotal");
                int nbDisponible = resultSet.getInt("nbDisponible");
                
                stock = new Stock(idStock, nbTotal, nbDisponible, ISBN);
                return stock;
            }
	 } catch (SQLException e) {
            e.printStackTrace();        
        }
		return stock;
	}
	
	public static Boolean checkStock(String ISBN, Connection connection) {
		Stock stock = Stock.fetchStockByISBN(ISBN, connection);
		Boolean res = (stock.getNbDisponible() > 0) ? true : false;
        return res;
	}
	
	public static Boolean updateStock(String ISBN, Boolean add, Connection connection) {
		Stock stock = Stock.fetchStockByISBN(ISBN, connection);
		stock.nbDisponible += (add) ? 1 : -1;
		
		try {
			String query = "UPDATE stock SET nbDisponible = ? WHERE ISBN = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, stock.nbDisponible);
			statement.setString(2, ISBN);
			int rowsInserted = statement.executeUpdate();
			
			return rowsInserted > 0;

		} catch (SQLException e) {
            e.printStackTrace();        
        }
		return false;
	}
}
