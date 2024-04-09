import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Représente le stock d'un livre dans la base de données.
 */
public class Stock {
    private int idStock;
    private int nbTotal;
    private int nbDisponible;
    private String ISBN;

    /**
     * Constructeur pour créer un objet Stock avec les informations fournies.
     * @param idStock Identifiant du stock
     * @param nbTotal Nombre total d'exemplaires
     * @param nbDisponible Nombre d'exemplaires disponibles
     * @param ISBN ISBN du livre associé à ce stock
     */
    public Stock(int idStock, int nbTotal, int nbDisponible, String ISBN) {
        this.idStock = idStock;
        this.nbTotal = nbTotal;
        this.nbDisponible = nbDisponible;
        this.ISBN = ISBN;
    }

    // Getters et setters pour les attributs de l'objet Stock

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
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Méthode pour obtenir les informations sur un stock à partir de son identifiant.
     * @param idStock Identifiant du stock à récupérer
     * @param connection Connexion à la base de données
     * @return Objet Stock correspondant à l'identifiant spécifié, ou null si non trouvé
     */
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

    /**
     * Méthode privée pour récupérer les informations sur un stock à partir de son ISBN.
     * @param ISBN ISBN du livre associé au stock à récupérer
     * @param connection Connexion à la base de données
     * @return Objet Stock correspondant à l'ISBN spécifié, ou null si non trouvé
     */
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

                // Création de l'objet Stock avec les informations récupérées
                stock = new Stock(idStock, nbTotal, nbDisponible, ISBN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    /**
     * Méthode statique pour vérifier si un livre (par son ISBN) est en stock.
     * @param ISBN ISBN du livre à vérifier
     * @param connection Connexion à la base de données
     * @return true si des exemplaires sont disponibles en stock, sinon false
     */
    public static Boolean checkStock(String ISBN, Connection connection) {
        Stock stock = Stock.fetchStockByISBN(ISBN, connection);
        if (stock != null) {
            return (stock.getNbDisponible() > 0);
        }
        return false;
    }

    /**
     * Méthode statique pour mettre à jour le stock d'un livre (par son ISBN).
     * @param ISBN ISBN du livre à mettre à jour
     * @param add true pour ajouter un exemplaire, false pour en retirer
     * @param connection Connexion à la base de données
     * @return true si la mise à jour du stock a réussi, sinon false
     */
    public static Boolean updateStock(String ISBN, Boolean add, Connection connection) {
        Stock stock = Stock.fetchStockByISBN(ISBN, connection);
        if (stock != null) {
            stock.nbDisponible += (add) ? 1 : -1;
            try {
                String query = "UPDATE stock SET nbDisponible = ? WHERE ISBN = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, stock.nbDisponible);
                statement.setString(2, ISBN);
                int rowsUpdated = statement.executeUpdate();
                return rowsUpdated > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
