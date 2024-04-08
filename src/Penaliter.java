import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Penaliter {
	private String ISBN;
	private int IdUser;
	private LocalDate datePenalite;
	
	/**
	 * @param iSBN
	 * @param idUser
	 * @param datePenalite
	 */
	public Penaliter(Livre livre, User user) {
		ISBN = livre.getISBN();
		IdUser = user.getIdUser();
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
	
}