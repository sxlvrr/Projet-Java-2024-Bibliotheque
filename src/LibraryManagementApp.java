import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class LibraryManagementApp extends JFrame {

    private JPanel contentPane;
    private JTable tableBooks;
    private JButton btnDetails;
    private JButton btnEmprunter;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField searchField;
    private List<Livre> listLivre;
    private Connection connection;
    private User user;
    private boolean isAdmin;

    public LibraryManagementApp(User user) {
        this.user = user;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 1200, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        tableBooks = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableBooks);
        scrollPane.setBounds(30, 30, 700, 300);
        contentPane.add(scrollPane);

        searchField = new JTextField();
        searchField.setBounds(30, 340, 300, 30);
        contentPane.add(searchField);

        JLabel roleLabel = new JLabel("Rôle de l'utilisateur : " + user.getRole());
        roleLabel.setBounds(30, 380, 200, 30);
        contentPane.add(roleLabel);

        showBooksFromDatabase();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tableBooks.getModel());
        tableBooks.setRowSorter(sorter);

        tableBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableBooks.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    btnDetails.setEnabled(true);
                    btnEmprunter.setEnabled(true);
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    btnDetails.setEnabled(false);
                    btnEmprunter.setEnabled(false);
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
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

        editButton = new JButton("Éditer");
        editButton.setBounds(750, 200, 100, 30);
        editButton.setEnabled(false);
        

        deleteButton = new JButton("Supprimer");
        deleteButton.setBounds(750, 250, 100, 30);
        deleteButton.setEnabled(false);
        
        if (user.getRole() != 1) {
        	contentPane.add(btnEmprunter);
        }else if (user.getRole() == 1){
        	contentPane.add(deleteButton);
        	contentPane.add(editButton);
        }

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

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableBooks.getSelectedRow();
                if (selectedRow != -1) {
                    Livre selectedLivre = listLivre.get(selectedRow);
                    editBook(selectedLivre);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableBooks.getSelectedRow();
                if (selectedRow != -1) {
                    Livre selectedLivre = listLivre.get(selectedRow);
                    deleteBook(selectedLivre);
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

        if (user != null && user.getRole() == 1) {
            isAdmin = true;
            addAdminFeatures();
        } else {
            isAdmin = false;
        }

        setVisible(true);
    }

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

    private void updateFilter(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tableBooks.getRowSorter();
        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText);
            sorter.setRowFilter(filter);
        }
    }

    private void addAdminFeatures() {
        JButton createButton = new JButton("Créer");
        createButton.setBounds(750, 300, 100, 30);
        //contentPane.add(createButton);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = JOptionPane.showInputDialog(contentPane, "Titre du livre :");
                String author = JOptionPane.showInputDialog(contentPane, "Auteur :");
                String publisher = JOptionPane.showInputDialog(contentPane, "Éditeur :");
                String genre = JOptionPane.showInputDialog(contentPane, "Genre :");
                String langue = JOptionPane.showInputDialog(contentPane, "Langue :");
                String nbPage = JOptionPane.showInputDialog(contentPane, "Nombre de pages :");
                String datePublication = JOptionPane.showInputDialog(contentPane, "Date de publication :");
                String description = JOptionPane.showInputDialog(contentPane, "Description :");
                String isbn = JOptionPane.showInputDialog(contentPane, "ISBN :");

                createBook(title, author, publisher, genre, langue, nbPage, datePublication, description, isbn);
            }
        });
    }

    private void editBook(Livre livre) {
        String newTitle = JOptionPane.showInputDialog(contentPane, "Nouveau titre du livre :", livre.getTitre());
        String newPublisher = JOptionPane.showInputDialog(contentPane, "Nouvel éditeur :", livre.getEditeur());
        String newGenre = JOptionPane.showInputDialog(contentPane, "Nouveau genre :", livre.getGenre());

        try {
            String updateQuery = "UPDATE livre SET titre = ?, editeur = ?, genre = ? WHERE ISBN = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newTitle);
            preparedStatement.setString(2, newPublisher);
            preparedStatement.setString(3, newGenre);
            preparedStatement.setString(4, livre.getISBN());
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(contentPane, "Détails du livre mis à jour avec succès.");
            showBooksFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(contentPane, "Erreur lors de la mise à jour du livre : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook(Livre livre) {
        int confirmResult = JOptionPane.showConfirmDialog(contentPane, "Êtes-vous sûr de vouloir supprimer ce livre ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                // Check for associated records in emprunter table
                boolean hasAssociatedRecords = checkForAssociatedRecords(livre.getISBN());

                if (!hasAssociatedRecords) {
                    String deleteQuery = "DELETE FROM livre WHERE titre = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                    preparedStatement.setString(1, livre.getTitre());
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(contentPane, "Livre supprimé avec succès.");
                    showBooksFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Impossible de supprimer ce livre car il a des emprunts associés.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(contentPane, "Erreur lors de la suppression du livre : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean checkForAssociatedRecords(String isbn) throws SQLException {
        String query = "SELECT COUNT(*) FROM emprunter WHERE ISBN = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, isbn);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }

    private void createBook(String title, String author, String publisher, String genre, String langue, String nbPage, String datePublication, String description, String isbn) {
        try {
            // Parse the datePublication string to a java.sql.Date object
            java.util.Date parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(datePublication);
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            String insertQuery = "INSERT INTO livre (titre, editeur, genre, nbPage, langue, datePublication, description, ISBN) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, publisher);
            preparedStatement.setString(3, genre);
            preparedStatement.setInt(4, Integer.parseInt(nbPage));
            preparedStatement.setString(5, langue);
            preparedStatement.setDate(6, sqlDate); // Set the datePublication as java.sql.Date
            preparedStatement.setString(7, description);
            preparedStatement.setString(8, isbn);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(contentPane, "Livre créé avec succès.");
            showBooksFromDatabase();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(contentPane, "Nombre de pages invalide. Veuillez saisir un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException | SQLException e) {
            JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du livre : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
