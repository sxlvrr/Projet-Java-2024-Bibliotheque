import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Emprunter {
	private String ISBN;
	private int IdUser;
	private LocalDate dateEmprunt;
	private LocalDate dateRetour;
	
	/**
	 * @param iSBN
	 * @param idUser
	 * @param dateEmprunt
	 * @param dateRetour
	 */
	public Emprunter(String ISBN, User user) {
		this.ISBN = ISBN;
		this.IdUser = user.getIdUser();
		this.dateEmprunt = LocalDate.now();
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public int getIdUser() {
		return IdUser;
	}

	public void setIdUser(int idUser) {
		IdUser = idUser;
	}

	public LocalDate getDateEmprunt() {
		return dateEmprunt;
	}

	public void setDateEmprunt(LocalDate dateEmprunt) {
		this.dateEmprunt = dateEmprunt;
	}

	public LocalDate getDateRetour() {
		return dateRetour;
	}

	public void setDateRetour(LocalDate dateRetour) {
		this.dateRetour = dateRetour;
	}

	@Override
	public String toString() {
		return "Emprunter [ISBN=" + ISBN + ", IdUser=" + IdUser + ", dateEmprunt=" + dateEmprunt + ", dateRetour="
				+ dateRetour + "]";
	}
	
	public Boolean rendreUnLivre() {
		Boolean res = false;
		Connection connection = null;
		System.out.println("DEBUG : CHECK 0 (rendreUnLivre)");
		try {
			connection = Database.getConnection();
			if (this.checkDejaEmprunter(connection)) {
				String query = "UPDATE emprunter SET dateRetour = ? WHERE ISBN = ? AND idUser = ? AND dateRetour IS NULL";
		        PreparedStatement statement = connection.prepareStatement(query);
		        statement.setString(1, String.valueOf(LocalDate.now()));
		        statement.setString(2, this.getISBN());
		        statement.setInt(3, this.getIdUser());
		        int rowsInserted = statement.executeUpdate();
		        
		        if (rowsInserted > 0) {
		        	Stock.updateStock(this.getISBN(), true, connection);
		        	System.out.println("DEBUG : CHECK 9 (livre rendu + stock modifié)");
		        	res = true;
		        } 
			}
		}catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DEBUG : CHECK 3 (SQL Error)"); 
        }
		Database.closeConnection(connection);
		return res;
	}
	
	public Boolean emprunterUnLivre() {
		System.out.println("DEBUG : CHECK 0 (emprunterUnLivre)");
		Boolean res = false;
		Connection connection = null;
		try {
			connection = Database.getConnection();
			if (!this.checkDejaEmprunter(connection) && Stock.checkStock(this.getISBN(), connection)) {
				
				if (createEmprunter(connection)) {
					Stock.updateStock(this.getISBN(), false, connection);
					res = true;
				}
			}
		}catch (SQLException e) {
            e.printStackTrace();
        }
		Database.closeConnection(connection);
		return res;
	}
	
	private Boolean checkDejaEmprunter(Connection connection) {
		try {
			String query = "SELECT * FROM emprunter WHERE ISBN = ? AND idUser = ? AND dateRetour IS NULL";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1, this.getISBN());
	        statement.setString(2, String.valueOf(this.getIdUser()));
	        ResultSet resultSet = statement.executeQuery();
	        
	        if (resultSet.next()) {
	        	System.out.println("DEBUG : CHECK 1 (deja un emprunt)");
	        	return true; //Il y a déjà un emprunt
	        } else {
	        	System.out.println("DEBUG : CHECK 2 (pas d'emprunt)");
	            return Penaliter.checkPenaliter(this, connection); // Il n'y a pas d'emprunt ou l'emprunt est terminé et verif penaliter de retour
	        }
		}catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DEBUG : CHECK 3 (SQL Error)");
            return true; // Echec de la requête ne pas créer d'enregistrement.
        }   
	}
	
	private Boolean createEmprunter(Connection connection) {
		System.out.println("DEBUG : CHECK 7 (createEmprunter)");
		try {
			String query = "INSERT INTO emprunter (ISBN, idUser, dateEmprunt) VALUES (?, ?, ?)";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1, this.getISBN());
	        statement.setString(2, String.valueOf(this.getIdUser()));
	        statement.setString(3, String.valueOf(this.getDateEmprunt()));
	        int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
	                
		}catch (SQLException e) {
            e.printStackTrace();
            return false;
        }   
	}
	
	public static List<Emprunter> mesEmprunts(User user){
		List<Emprunter> listEmprunt = new ArrayList<Emprunter>();
		Connection connection = null;
		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM emprunter WHERE idUser = ? ";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, user.getIdUser());
	        ResultSet resultSet = statement.executeQuery();
	        
	        while (resultSet.next()) {
	        	String ISBN = resultSet.getString("ISBN");
	        	LocalDate dateEmprunt = LocalDate.parse(resultSet.getString("dateEmprunt"));
	        	LocalDate dateRetour = (resultSet.getString("dateRetour") != null) ? LocalDate.parse(resultSet.getString("dateRetour")) : null;
	        	
	        	Emprunter emprunt = new Emprunter(ISBN, user);
	        	emprunt.setDateEmprunt(dateEmprunt);
	        	emprunt.setDateRetour(dateRetour);
	        	
	        	listEmprunt.add(emprunt);
	        }        
		}catch (SQLException e) {
            e.printStackTrace();
        }
		Database.closeConnection(connection);
		return listEmprunt;
	}
}