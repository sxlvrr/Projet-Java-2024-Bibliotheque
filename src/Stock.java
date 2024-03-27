import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stock {
	private int idStock;
	private int nbTotal;
	private int nbDisponible;
	
	/**
	 * @param idStock
	 * @param nbTotal
	 * @param nbDisponible
	 */
	public Stock(int idStock, int nbTotal, int nbDisponible) {
		super();
		this.idStock = idStock;
		this.nbTotal = nbTotal;
		this.nbDisponible = nbDisponible;
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

	@Override
	public String toString() {
		return "Stock [idStock=" + idStock + ", nbTotal=" + nbTotal + ", nbDisponible=" + nbDisponible +"]";
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

                // Création de l'objet Stock avec les informations récupérées
                stock = new Stock(idStock, nbTotal, nbDisponible);
            }

            // Fermeture des ressources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stock;
    }
}
