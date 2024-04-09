import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Penaliter {
	private String ISBN;
	private int IdUser;
	private LocalDate datePenalite;
	
	/**
	 * @param iSBN
	 * @param idUser
	 * @param datePenalite
	 */
	public Penaliter(String ISBN, User user) {
		this.ISBN = ISBN;
		this.IdUser = user.getIdUser();
		this.datePenalite = LocalDate.now();
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

	public LocalDate getDatePenalite() {
		return datePenalite;
	}

	public void setDatePenalite(LocalDate datePenalite) {
		this.datePenalite = datePenalite;
	}

	@Override
	public String toString() {
		return "Emprunter [ISBN=" + ISBN + ", IdUser=" + IdUser + ", datePenalite=" + datePenalite + "]";
	}
	
	public static Boolean checkPenaliter(Emprunter emprunt, Connection connection) {
		try {
			String query = "SELECT * FROM penaliter WHERE ISBN = ? AND idUser = ? AND datePenalite <= ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1, emprunt.getISBN());
	        statement.setString(2, String.valueOf(emprunt.getIdUser()));
	        statement.setString(3, String.valueOf(LocalDate.now()));
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	        	System.out.println("DEBUG : CHECK 4 (deja une penalité)");
	        	return true; //Il existe une penalité
	        } else {
	        	System.out.println("DEBUG : CHECK 5 (pas de penalité)");
	            return false; //Il n'y a pas de penalité
	        }
		}catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DEBUG : CHECK 6 (Error SQL)");
            return true; // Echec de la requête ne pas créer d'enregistrement.
        } 
	}
	
	public static List<Penaliter> mesPenaliter(User user){
		List<Penaliter> listPenaliter = new ArrayList<>();
		Connection connection = null;
		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM penaliter WHERE idUser = ? ";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, user.getIdUser());
	        ResultSet resultSet = statement.executeQuery();
	        
	        while (resultSet.next()) {
	        	String ISBN = resultSet.getString("ISBN");
	        	LocalDate datePenalite = LocalDate.parse(resultSet.getString("datePenalite"));
	        	
	        	Penaliter penaliter = new Penaliter(ISBN, user);
	        	penaliter.setDatePenalite(datePenalite);
	        	
	        	listPenaliter.add(penaliter);
	        }        
		}catch (SQLException e) {
            e.printStackTrace();
        }
		Database.closeConnection(connection);
		return listPenaliter;
	}	
}