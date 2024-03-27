import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryManagementApp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableBooks;
    private JButton btnDetails;

    // Connexion à la base de données
    private Connection connection;

    // Méthode pour récupérer les livres depuis la base de données
    private void fetchBooksFromDatabase() {
        try {
            // Préparation de la requête SQL avec une jointure pour récupérer les informations de l'auteur
            String query = "SELECT livre.titre AS `Titre du Livre`, livre.editeur AS `Éditeur`, " +
                    "auteur.nom AS `Nom de l'Auteur`, auteur.prenom AS `Prénom de l'Auteur` " +
                    "FROM livre " +
                    "INNER JOIN auteur ON livre.idAuteur = auteur.idAuteur";
            PreparedStatement statement = connection.prepareStatement(query);

            // Exécution de la requête
            ResultSet resultSet = statement.executeQuery();

            // Création du modèle de table
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Titre du Livre");
            model.addColumn("Éditeur");
            model.addColumn("Nom de l'Auteur");
            model.addColumn("Prénom de l'Auteur");

            // Remplissage du modèle avec les données de la base de données
            while (resultSet.next()) {
                String title = resultSet.getString("Titre du Livre");
                String publisher = resultSet.getString("Éditeur");
                String authorLastName = resultSet.getString("Nom de l'Auteur");
                String authorFirstName = resultSet.getString("Prénom de l'Auteur");
                model.addRow(new Object[]{title, publisher, authorLastName, authorFirstName});
            }

            // Création de la table avec le modèle de données
            tableBooks.setModel(model);

            // Fermeture des ressources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LibraryManagementApp frame = new LibraryManagementApp();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LibraryManagementApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Création de la table
        tableBooks = new JTable();
        tableBooks.setBounds(30, 30, 700, 300);
        contentPane.add(tableBooks);

        // Ajout d'une barre de défilement pour la table
        JScrollPane scrollPane = new JScrollPane(tableBooks);
        scrollPane.setBounds(30, 30, 700, 300);
        contentPane.add(scrollPane);

        // Établir une connexion à la base de données
        try {
            connection = DatabaseConnector.getConnection();
            // Charger les livres depuis la base de données au démarrage de l'application
            fetchBooksFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Activer le tri en cliquant sur les en-têtes de colonnes
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tableBooks.getModel());
        tableBooks.setRowSorter(sorter);

        // Ajout d'un MouseListener pour détecter les clics sur les lignes
        tableBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableBooks.rowAtPoint(e.getPoint());
                int col = tableBooks.columnAtPoint(e.getPoint());
                if (row >= 0 && col >= 0) {
                    // Activer le bouton de détails lorsque l'utilisateur clique sur une ligne
                    btnDetails.setEnabled(true);
                }
            }
        });

        // Création et ajout du bouton de détails
        btnDetails = new JButton("Détails");
        btnDetails.setBounds(750, 100, 100, 30);
        btnDetails.setEnabled(false); // Désactiver le bouton au début
        contentPane.add(btnDetails);

        // Définir l'action du bouton de détails
        btnDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer la ligne sélectionnée
                int selectedRow = tableBooks.getSelectedRow();
                if (selectedRow != -1) {
                    // Récupérer les informations sur le livre à partir de la ligne sélectionnée
                    String title = (String) tableBooks.getValueAt(selectedRow, 0);
                    String publisher = (String) tableBooks.getValueAt(selectedRow, 1);
                    String authorLastName = (String) tableBooks.getValueAt(selectedRow, 2);
                    String authorFirstName = (String) tableBooks.getValueAt(selectedRow, 3);

                    // Afficher les détails dans une boîte de dialogue
                    String detailsMessage = "Titre : " + title + "\n" +
                            "Éditeur : " + publisher + "\n" +
                            "Nom de l'Auteur : " + authorLastName + "\n" +
                            "Prénom de l'Auteur : " + authorFirstName;
                    JOptionPane.showMessageDialog(LibraryManagementApp.this, detailsMessage, "Détails du Livre", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}
