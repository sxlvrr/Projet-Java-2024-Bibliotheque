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
import javax.swing.RowFilter;

public class LibraryManagementApp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableBooks;
    private JButton btnDetails;
    private JTextField searchField;
    private JLabel roleLabel;
    private User user;

    // Connexion à la base de données
    private Connection connection;

    // Méthode pour récupérer les livres depuis la base de données
    private void fetchBooksFromDatabase() {
        try {
            // Préparation de la requête SQL avec une jointure pour récupérer les informations de l'auteur
            String query = "SELECT livre.titre AS `Titre du Livre`, livre.editeur AS `Éditeur`, " +
                    "auteur.nom AS `Nom de l'Auteur`, auteur.prenom AS `Prénom de l'Auteur`, stock.ISBN AS `ISBN` " +
                    "FROM livre " +
                    "INNER JOIN auteur ON livre.idAuteur = auteur.idAuteur " +
                    "INNER JOIN stock ON livre.idStock = stock.idStock";
            PreparedStatement statement = connection.prepareStatement(query);

            // Exécution de la requête
            ResultSet resultSet = statement.executeQuery();

            // Création du modèle de table
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Titre du Livre");
            model.addColumn("Éditeur");
            model.addColumn("Nom de l'Auteur");
            model.addColumn("Prénom de l'Auteur");
            model.addColumn("ISBN");

            // Remplissage du modèle avec les données de la base de données
            while (resultSet.next()) {
                String title = resultSet.getString("Titre du Livre");
                String publisher = resultSet.getString("Éditeur");
                String authorLastName = resultSet.getString("Nom de l'Auteur");
                String authorFirstName = resultSet.getString("Prénom de l'Auteur");
                String isbn = resultSet.getString("ISBN");
                model.addRow(new Object[]{title, publisher, authorLastName, authorFirstName, isbn});
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
    
    private String fetchBookDetailsFromDatabase(String title) {
        String details = "";
        try {
            // Préparation de la requête SQL pour récupérer les détails supplémentaires du livre
            String query = "SELECT langue, nbPage, datePublication, description, stock.nbTotal, stock.nbDisponible, stock.ISBN " +
                    "FROM livre " +
                    "INNER JOIN stock ON livre.idStock = stock.idStock " +
                    "WHERE titre = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, title);

            // Exécution de la requête
            ResultSet resultSet = statement.executeQuery();

            // Récupération des détails
            if (resultSet.next()) {
                String langue = resultSet.getString("langue");
                int nb_pages = resultSet.getInt("nbPage");
                String date_publication = resultSet.getString("datePublication");
                String description = resultSet.getString("description");
                int nb_total = resultSet.getInt("nbTotal");
                int nb_disponible = resultSet.getInt("nbDisponible");
                String ISBN = resultSet.getString("ISBN");

                // Construction de la chaîne de détails
                details = "Langue : " + langue + "\n" +
                        "Nombre de pages : " + nb_pages + "\n" +
                        "Date de publication : " + date_publication + "\n" +
                        "Description : " + description + "\n" +
                        "Nombre total d'exemplaires : " + nb_total + "\n" +
                        "Nombre d'exemplaires disponibles : " + nb_disponible + "\n" +
                        "ISBN : " + ISBN;
            }

            // Fermeture des ressources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    // Méthode pour mettre à jour le filtre de recherche
    private void updateFilter1(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tableBooks.getRowSorter();
        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null); // Pas de filtre si la recherche est vide
        } else {
            // Création du filtre basé sur la recherche
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText);
            sorter.setRowFilter(filter);
        }
    }

    // Méthode pour initialiser l'interface graphique
    private void initializeUI() {
        // Création de la barre de recherche
        searchField = new JTextField();
        searchField.setBounds(30, 340, 300, 30);
        contentPane.add(searchField);

        // Ajout d'un écouteur de saisie pour la recherche en temps réel
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                updateFilter1(searchText);
            }
        });
    }

    public LibraryManagementApp(User user) {
    	this.user = user;
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 1200, 600);
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

        // Ajout de la barre de recherche
        searchField = new JTextField();
        searchField.setBounds(30, 340, 300, 30);
        contentPane.add(searchField);
        
        // Création et ajout du JLabel pour afficher le rôle
        roleLabel = new JLabel("Rôle de l'utilisateur : " + user.getRole());
        contentPane.add(roleLabel, BorderLayout.NORTH);
        
        displayUserRole();

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
                    // Récupérer le titre du livre à partir de la ligne sélectionnée
                    String title = (String) tableBooks.getValueAt(selectedRow, 0);
                    // Récupérer les détails supplémentaires du livre à partir de la base de données
                    String details = fetchBookDetailsFromDatabase(title);
                    // Afficher les détails dans une boîte de dialogue
                    JOptionPane.showMessageDialog(LibraryManagementApp.this, details, "Détails du Livre", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Ajout d'un écouteur pour la barre de recherche en temps réel
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                updateFilter1(searchText);
            }
        });
    }

    // Méthode pour mettre à jour le filtre de recherche
    private void updateFilter(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tableBooks.getRowSorter();
        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null); // Pas de filtre si la recherche est vide
        } else {
            // Création du filtre basé sur la recherche
            RowFilter<DefaultTableModel, Object> titleFilter = RowFilter.regexFilter("(?i)" + searchText, 0);
            RowFilter<DefaultTableModel, Object> authorFilter = RowFilter.regexFilter("(?i)" + searchText, 2);
            RowFilter<DefaultTableModel, Object> publisherFilter = RowFilter.regexFilter("(?i)" + searchText, 1);
            RowFilter<DefaultTableModel, Object> isbnFilter = RowFilter.regexFilter("(?i)" + searchText, 4);
            
            RowFilter<DefaultTableModel, Object> compoundRowFilter = RowFilter.orFilter(
                RowFilter.regexFilter(titleFilter, authorFilter),
                RowFilter.regexFilter(publisherFilter, isbnFilter)
            );9
            sorter.setRowFilter(compoundRowFilter);
        }
    }
    
 // Méthode pour afficher le rôle de l'utilisateur
    private void displayUserRole() {
        if (user != null) {
            System.out.println("Rôle de l'utilisateur : " + user.getRole()); // Supposons que getRole() renvoie le rôle de l'utilisateur
        }
    }

}