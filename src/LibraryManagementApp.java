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

import java.util.Iterator;
import java.util.List;

public class LibraryManagementApp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableBooks;
    private JButton btnDetails;
    private JTextField searchField;
    private JLabel roleLabel;
    private User user;
    private boolean isAdmin;
    private List<Livre> listLivre;

    // Connexion à la base de données
    private Connection connection;
    
    
    private void showBooksFromDatabase() {
        try {
        	System.out.println("DEGUG [LibraryManagementApp] [showBooksFromDatabase] [START]");
            // Récupération de la connexion à la base de données
            connection = Database.getConnection();
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Titre du Livre");
            model.addColumn("Éditeur");
            model.addColumn("Auteur");
            model.addColumn("Genre");
            
            // Utilisation de la méthode fetchBooksFromDatabase de la classe Livre pour récupérer les livres
            listLivre = Livre.fetchBooksFromDatabase(connection);
            System.out.println("DEGUG [LibraryManagementApp] [showBooksFromDatabase] [listLivre] : "+listLivre);
            
            for (Livre livre : listLivre) {
                model.addRow(new Object[]{livre.getTitre(), livre.getEditeur(), livre.getAuteur().getPrenom() +' '+ livre.getAuteur().getNom(), livre.getGenre()});
    		}
            
            // Affichage des livres dans la table
            tableBooks.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String fetchBookDetailsFromDatabase(String title) {
        String details = "";
        // Récupération des détails
        if (!listLivre.isEmpty()) {
        	for (Livre livre : listLivre) {
        		if (livre.getTitre().contentEquals(title)) {
        			details = "Langue : " + livre.getLangue() + "\n" +
                            "Nombre de pages : " + livre.getNbPage() + "\n" +
                            "Date de publication : " + livre.getDatePublication() + "\n" +
                            "Description : " + livre.getDescription() + "\n" +
                            "Nombre total d'exemplaires : " + livre.getStock().getNbTotal() + "\n" +
                            "Nombre d'exemplaires disponibles : " + livre.getStock().getNbDisponible() + "\n" +
                            "ISBN : " + livre.getISBN();			
        		}
        	}           
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
        roleLabel.setBounds(30, 380, 200, 30);
        contentPane.add(roleLabel);

        if (user != null && user.getRole() == 1) {
            isAdmin = true;
            // Ajouter des fonctionnalités d'administration
            addAdminFeatures();
        } else {
            isAdmin = false;
        }

        // Charger les livres depuis la base de données au démarrage de l'application
        showBooksFromDatabase();

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

    private void addAdminFeatures() {
        // Ajouter des fonctionnalités d'édition, de suppression et de création de livres
        JButton editButton = new JButton("Éditer");
        editButton.setBounds(750, 150, 100, 30);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ajouter la logique pour l'édition des livres
            }
        });
        contentPane.add(editButton);

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.setBounds(750, 200, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ajouter la logique pour la suppression des livres
            }
        });
        contentPane.add(deleteButton);

        JButton createButton = new JButton("Créer");
        createButton.setBounds(750, 250, 100, 30);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ajouter la logique pour la création de nouveaux livres
            }
        });
        contentPane.add(createButton);
    }
}
