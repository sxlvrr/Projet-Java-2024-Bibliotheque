import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Noter {
	private String ISBN;
	private int IdUser;
	private int note;
	private String commentaire;
	
	/**
	 * @param iSBN
	 * @param idUser
	 * @param datePenalite
	 */
	public Noter(String ISBN, User user, Integer note, String commentaire) {
		this.ISBN = ISBN;
		this.IdUser = user.getIdUser();
		this.note = note;
		this.commentaire = commentaire;
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
	public int getNote() {
		return note;
	}
	public void setNote(int note) {
		this.note = note;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	
	@Override
	public String toString() {
		return "Noter [ISBN=" + ISBN + ", IdUser=" + IdUser + ", note=" + note + ", commentaire=" + commentaire + "]";
	}
	
	private ResultSet getNoter(String ISBN, Integer idUser, Connection connection) {
		try {
			String query = "SELECT * FROM noter WHERE ISBN = ? AND idUser = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1, ISBN);
	        statement.setInt(2, idUser);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	        	return resultSet;
	        } else {
	            return null;
	        }
		}catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DEBUG : CHECK 6 (Error SQL)");
            return null;
        } 
	}
	
	private Boolean insertNote(Connection connection) {
		System.out.println("DEBUG : CHECK 7 (insertNote)");
		try {
			String query = "INSERT INTO noter (ISBN, idUser, note, commentaire) VALUES (?, ?, ?, ?)";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1, this.getISBN());
	        statement.setInt(2, this.getIdUser());
	        statement.setInt(3, this.getNote());
	        statement.setString(4, this.getCommentaire());
	        int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
	                
		}catch (SQLException e) {
            e.printStackTrace();
            return false;
        }   
	}
	
	private Boolean updateNote(Connection connection) {
		System.out.println("DEBUG : CHECK 7 (updateNote)");
		try {
			String query = "UPDATE noter SET note = ? , commentaire = ? WHERE ISBN = ? AND idUser = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, this.getNote());
	        statement.setString(2, this.getCommentaire());
	        statement.setString(3, this.getISBN());
	        statement.setInt(4, this.getIdUser());
	        int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
	                
		}catch (SQLException e) {
            e.printStackTrace();
            return false;
        }   
	}
	
	public Boolean noterUnLivre(){
		Connection connection = null;
		Boolean res = false;
		try {
			connection = Database.getConnection();
			ResultSet resultSet = this.getNoter(this.ISBN, this.getIdUser(), connection);
	        if (resultSet == null && this.insertNote(connection)) {
	        	res = true;
	        }
		}catch (SQLException e) {
            e.printStackTrace();
        }
		Database.closeConnection(connection);
		return res;
	}
	
	public Boolean modifierUneNote(){
		Connection connection = null;
		Boolean res = false;
		try {
			connection = Database.getConnection();
			ResultSet resultSet = this.getNoter(this.ISBN, this.getIdUser(), connection);
	        if (resultSet != null && this.updateNote(connection)) {
	        	res = true;
	        }
		}catch (SQLException e) {
            e.printStackTrace();
        }
		Database.closeConnection(connection);
		return res;
	}
}