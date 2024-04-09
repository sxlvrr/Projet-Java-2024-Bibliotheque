import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Application de gestion de bibliothèque avec interface graphique Swing.
 */
public class LibraryManagementApp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableBooks;
    private JButton btnDetails;
    private JButton btnEmprunter;
    private JTextField searchField;
    private JLabel roleLabel;
    private User user;
    private boolean isAdmin;
    private List<Livre> listLivre;
    private Connection connection;

    /**
     * Affiche les livres provenant de la base de données dans la table.
     */
    private void showBooksFromDatabase() {
        try {
            connection = Database.getConnection();
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Titre du Livre");
            model.addColumn("Éditeur");
            model.addColumn("Auteur");
            model.addColumn("Genre");

            listLivre = Livre.fetchBooksFromDatabase(connection);

            for (Livre livre : listLivre) {
                model.addRow(new Object[]{livre.getTitre(), livre.getEditeur(), livre.getAuteur().getPrenom() + ' ' + livre.getAuteur().getNom(), livre.getGenre()});
            }

            tableBooks.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère les détails d'un livre à partir de son titre.
     * @param title Titre du livre
     * @return Détails du livre sous forme de chaîne de caractères
     */
    private String fetchBookDetailsFromDatabase(String title) {
        String details = "";
        for (Livre livre : listLivre) {
            if (livre.getTitre().equals(title)) {
                details = "Langue : " + livre.getLangue() + "\n" +
                        "Nombre de pages : " + livre.getNbPage() + "\n" +
                        "Date de publication : " + livre.getDatePublication() + "\n" +
                        "Description : " + livre.getDescription() + "\n" +
                        "Nombre total d'exemplaires : " + livre.getStock().getNbTotal() + "\n" +
                        "Nombre d'exemplaires disponibles : " + livre.getStock().getNbDisponible() + "\n" +
                        "ISBN : " + livre.getISBN();
            }
        }
        return details;
    }

    /**
     * Met à jour le filtre de recherche pour la table des livres.
     * @param searchText Texte de recherche
     */
    private void updateFilter(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tableBooks.getRowSorter();
        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null); // Supprime le filtre si la recherche est vide
        } else {
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText);
            sorter.setRowFilter(filter);
        }
    }

    /**
     * Initialise l'interface graphique de l'application.
     */
    private void initializeUI() {
        searchField = new JTextField();
        searchField.setBounds(30, 340, 300, 30);
        contentPane.add(searchField);

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                updateFilter(searchText);
            }
        });
    }

    /**
     * Constructeur de l'application de gestion de bibliothèque.
     * @param user Utilisateur connecté
     */
    public LibraryManagementApp(User user) {
        this.user = user;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 1200, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        tableBooks = new JTable();
        tableBooks.setBounds(30, 30, 700, 300);
        contentPane.add(tableBooks);

        JScrollPane scrollPane = new JScrollPane(tableBooks);
        scrollPane.setBounds(30, 30, 700, 300);
        contentPane.add(scrollPane);

        searchField = new JTextField();
        searchField.setBounds(30, 340, 300, 30);
        contentPane.add(searchField);

        roleLabel = new JLabel("Rôle de l'utilisateur : " + user.getRole());
        contentPane.add(roleLabel, BorderLayout.NORTH);
        roleLabel.setBounds(30, 380, 200, 30);
        contentPane.add(roleLabel);

        if (user != null && user.getRole() == 1) {
            isAdmin = true;
            addAdminFeatures();
        } else {
            isAdmin = false;
        }

        showBooksFromDatabase();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tableBooks.getModel());
        tableBooks.setRowSorter(sorter);

        tableBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableBooks.rowAtPoint(e.getPoint());
                int col = tableBooks.columnAtPoint(e.getPoint());
                if (row >= 0 && col >= 0) {
                    btnDetails.setEnabled(true);
                    btnEmprunter.setEnabled(true);
                }
            }
        });

        btnDetails = new JButton("Détails");
        btnDetails.setBounds(750, 100, 100, 30);
        btnDetails.setEnabled(false);
        contentPane.add(btnDetails);

        btnEmprunter = new JButton("Emprunter");
        btnEmprunter.setBounds(750, 50, 100, 30);
        btnEmprunter.setEnabled(false);
        contentPane.add(btnEmprunter);

        btnDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableBooks.getSelectedRow();
                if (selectedRow != -1) {
                    String title = (String) tableBooks.getValueAt(selectedRow, 0);
                    String details = fetchBookDetailsFromDatabase(title);
                    JOptionPane.showMessageDialog(LibraryManagementApp.this, details, "Détails du Livre", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnEmprunter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableBooks.getSelectedRow();
                if (selectedRow != -1 && !listLivre.isEmpty()) {
                    String title = (String) tableBooks.getValueAt(selectedRow, 0);
                    for (Livre livre : listLivre) {
                        if (livre.getTitre().equals(title)) {
                            Emprunter emprunt = new Emprunter(livre.getISBN(), user);
                            if (emprunt.emprunterUnLivre()) {
                                JOptionPane.showMessageDialog(LibraryManagementApp.this, "Livre emprunté !");
                            } else {
                                JOptionPane.showMessageDialog(LibraryManagementApp.this, "Vous ne pouvez pas emprunter ce livre");
                            }
                        }
                    }
                }
            }
        });

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                updateFilter(searchText);
            }
        });

        JButton btnRetour = new JButton("Retour");
        btnRetour.setBounds(750, 150, 100, 30);
        contentPane.add(btnRetour);

        btnRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuAccueil menuAccueil = new MenuAccueil(user);
                menuAccueil.setVisible(true);
                dispose();
            }
        });

        initializeUI();
    }

    /**
     * Ajoute les fonctionnalités d'administration à l'interface graphique.
     */
    private void addAdminFeatures() {
        JButton editButton = new JButton("Éditer");
        editButton.setBounds(750, 200, 100, 30);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ajouter la logique pour l'édition des livres
            }
        });
        contentPane.add(editButton);

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.setBounds(750, 250, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ajouter la logique pour la suppression des livres
            }
        });
        contentPane.add(deleteButton);

        JButton createButton = new JButton("Créer");
        createButton.setBounds(750, 300, 100, 30);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ajouter la logique pour la création de nouveaux livres
            }
        });
        contentPane.add(createButton);
    }
}
