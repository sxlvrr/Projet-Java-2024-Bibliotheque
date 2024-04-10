import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class GestionMembre extends JFrame {

    private JTable table;
    private JButton retourButton;
    private JButton modifierRoleButton;
    private JButton supprimerButton;
    private JButton supprimerPenalitesButton; // Bouton pour supprimer les pénalités
    private User user;

    public GestionMembre(User user) {
        this.user = user;

        setTitle("Liste des Membres");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        setupActions();
        setLocationAndDisplay();
    }

    private void initComponents() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nom");
        model.addColumn("Prénom");
        model.addColumn("Email");
        model.addColumn("Rôle");

        Connection connection = null;
        try {
            connection = Database.getConnection();
            List<User> users = User.getAllUsers(connection);
            for (User user : users) {
                model.addRow(new Object[]{user.getNom(), user.getPrenom(), user.getEmail(), user.getRole()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(connection);
        }

        table = new JTable(model);
        table.setRowHeight(25);

        retourButton = new JButton("Retour");
        retourButton.setFocusPainted(false);

        modifierRoleButton = new JButton("Modifier le rôle");
        modifierRoleButton.setFocusPainted(false);
        modifierRoleButton.setEnabled(false);

        supprimerButton = new JButton("Supprimer");
        supprimerButton.setFocusPainted(false);
        supprimerButton.setEnabled(false);

        supprimerPenalitesButton = new JButton("Supprimer Pénalités");
        supprimerPenalitesButton.setFocusPainted(false);
        supprimerPenalitesButton.setEnabled(false); // Désactiver initialement

        table.getSelectionModel().addListSelectionListener(event -> {
            boolean rowSelected = table.getSelectedRow() != -1;
            modifierRoleButton.setEnabled(rowSelected);
            supprimerButton.setEnabled(rowSelected);
            supprimerPenalitesButton.setEnabled(rowSelected); // Activer le bouton si une ligne est sélectionnée
        });

        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
    }

    private void setupActions() {
        retourButton.addActionListener(e -> {
            MenuAccueil menuAccueil = new MenuAccueil(user);
            menuAccueil.setVisible(true);
            dispose();
        });

        modifierRoleButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String email = (String) table.getValueAt(selectedRow, 2);
                int currentRole = (int) table.getValueAt(selectedRow, 3);

                // Code pour modifier le rôle de l'utilisateur...
            }
        });

        supprimerButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String email = (String) table.getValueAt(selectedRow, 2);
                int option = JOptionPane.showConfirmDialog(GestionMembre.this,
                        "Voulez-vous vraiment supprimer cet utilisateur ?", "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    // Code pour supprimer l'utilisateur...
                }
            }
        });

        supprimerPenalitesButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String email = (String) table.getValueAt(selectedRow, 2);
                int option = JOptionPane.showConfirmDialog(GestionMembre.this,
                        "Voulez-vous vraiment supprimer les pénalités de cet utilisateur ?", "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    boolean deleted = Penaliter.deletePenalitesByEmail(email);
                    if (deleted) {
                        JOptionPane.showMessageDialog(GestionMembre.this, "Pénalités supprimées avec succès !");
                    } else {
                        JOptionPane.showMessageDialog(GestionMembre.this,
                                "Échec de la suppression des pénalités de l'utilisateur.", "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void setLocationAndDisplay() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.add(retourButton);
        buttonPanel.add(modifierRoleButton);
        buttonPanel.add(supprimerButton);
        buttonPanel.add(supprimerPenalitesButton); // Ajouter le bouton supprimer pénalités

        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createTitledBorder("Liste des Membres"));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(titlePanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
