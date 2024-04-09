import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class ListeEmprunt extends JFrame {

    private JTable tableEmpruntsEnCours;
    private JTable tableHistorique;
    private List<Emprunter> emprunts;
    private List<Livre> livres = new ArrayList<>();
    private User user;

    public ListeEmprunt(User user) {
        this.user = user;
        this.emprunts = Emprunter.mesEmprunts(user);

        Connection connection = null;
        try {
            connection = Database.getConnection();
            this.livres = Livre.fetchBooksFromDatabase(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Database.closeConnection(connection);

        setTitle("Liste des Emprunts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Création des modèles de table
        DefaultTableModel modelEmpruntsEnCours = new DefaultTableModel();
        modelEmpruntsEnCours.addColumn("ISBN");
        modelEmpruntsEnCours.addColumn("Titre");
        modelEmpruntsEnCours.addColumn("Date d'Emprunt");

        DefaultTableModel modelHistorique = new DefaultTableModel();
        modelHistorique.addColumn("ISBN");
        modelHistorique.addColumn("Titre");
        modelHistorique.addColumn("Date d'Emprunt");
        modelHistorique.addColumn("Date de Retour");

        // Remplissage des modèles
        for (Emprunter emprunt : emprunts) {
            String ISBN = emprunt.getISBN();
            String dateEmprunt = emprunt.getDateEmprunt().toString();
            String dateRetour = (emprunt.getDateRetour() != null) ? emprunt.getDateRetour().toString() : "Non retourné";
            String titre = "";
            for (Livre livre : livres) {
                if (livre.getISBN().equals(ISBN)) {
                    titre = (livre != null) ? livre.getTitre() : "Titre inconnu";
                }
            }

            if (emprunt.getDateRetour() == null) {
                modelEmpruntsEnCours.addRow(new Object[]{ISBN, titre, dateEmprunt});
            } else {
                modelHistorique.addRow(new Object[]{ISBN, titre, dateEmprunt, dateRetour});
            }
        }

        // Création des tables
        tableEmpruntsEnCours = new JTable(modelEmpruntsEnCours);
        tableHistorique = new JTable(modelHistorique);
        tableHistorique.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tableEmpruntsEnCours.getTableHeader().setBackground(Color.LIGHT_GRAY);

        // Création des boutons
        JButton rendreButton = new JButton("Rendre");
        rendreButton.setEnabled(false);

        JButton noterButton = new JButton("Noter");
        noterButton.setEnabled(false);

        JButton modifierNoteButton = new JButton("Modifier Note");
        modifierNoteButton.setEnabled(false);

        // Ajout des écouteurs d'événements
        rendreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableEmpruntsEnCours.getSelectedRow();
                if (selectedRow != -1) {
                    String ISBN = (String) tableEmpruntsEnCours.getValueAt(selectedRow, 0);
                    Emprunter emprunt = findEmpruntByISBN(ISBN);
                    if (emprunt != null && emprunt.rendreUnLivre()) {
                        JOptionPane.showMessageDialog(ListeEmprunt.this, "Livre rendue !");
                        // Mettre à jour le modèle après le rendu
                        modelHistorique.addRow(new Object[]{ISBN, tableEmpruntsEnCours.getValueAt(selectedRow, 1), emprunt.getDateEmprunt().toString(), LocalDate.now().toString()});
                        modelEmpruntsEnCours.removeRow(selectedRow);
                    }else {
                    	JOptionPane.showMessageDialog(ListeEmprunt.this, "Erreur pour rendre le livre");
                    }
                }
            }
        });
        
        noterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableHistorique.getSelectedRow();
                if (selectedRow != -1) {
                	String ISBN = (String) tableHistorique.getValueAt(selectedRow, 0);
                    String title = (String) tableHistorique.getValueAt(selectedRow, 1);
                    
                    // Création et affichage de la boîte de dialogue NoterDialog
                    NoterDialog noterDialog = new NoterDialog(ListeEmprunt.this, title);
                    Object[] result = noterDialog.showNoteDialog();

                    if (result != null) {
                        Integer note = (Integer) result[0];
                        String commentaire = (String) result[1];
                        Noter noter = new Noter(ISBN, user, note, commentaire);
                        if (noter.noterUnLivre()) {
                        	JOptionPane.showMessageDialog(ListeEmprunt.this, "Livre noté !");
                        }
                    }
                }
            }
        });


        modifierNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int selectedRow = tableHistorique.getSelectedRow();
                if (selectedRow != -1) {
                	String ISBN = (String) tableHistorique.getValueAt(selectedRow, 0);
                    String title = (String) tableHistorique.getValueAt(selectedRow, 1);
                    
                    // Création et affichage de la boîte de dialogue NoterDialog
                    NoterDialog noterDialog = new NoterDialog(ListeEmprunt.this, title);
                    Object[] result = noterDialog.showNoteDialog();
                    if (result != null) {
                        Integer note = (Integer) result[0];
                        String commentaire = (String) result[1];
                        Noter noter = new Noter(ISBN, user, note, commentaire);
                        if (noter.modifierUneNote()) {
                        	JOptionPane.showMessageDialog(ListeEmprunt.this, "Note Modifié !");
                        }
                    }
                }
            }
        });

        // Écouteur de sélection pour activer/désactiver le bouton "Rendre"
        ListSelectionModel selectionModel = tableEmpruntsEnCours.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean enableButton = !selectionModel.isSelectionEmpty();
                rendreButton.setEnabled(enableButton);
            }
        });

        // Écouteur de sélection pour activer/désactiver le bouton "Noter" et "Modifier Note"
        ListSelectionModel selectionModel2 = tableHistorique.getSelectionModel();
        selectionModel2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean enableButton = !selectionModel2.isSelectionEmpty();
                noterButton.setEnabled(enableButton);
                modifierNoteButton.setEnabled(enableButton);
            }
        });

        // Création des panneaux principaux
        JPanel empruntsPanel = new JPanel(new BorderLayout());
        empruntsPanel.setBorder(BorderFactory.createTitledBorder("Mes emprunts en cours"));
        empruntsPanel.add(new JScrollPane(tableEmpruntsEnCours), BorderLayout.CENTER);

        // Création du panneau pour les boutons "Rendre" sous le tableau "Mes emprunts"
        JPanel rendrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        rendrePanel.add(rendreButton);
        empruntsPanel.add(rendrePanel, BorderLayout.SOUTH);

        JPanel historiquePanel = new JPanel(new BorderLayout());
        historiquePanel.setBorder(BorderFactory.createTitledBorder("Historique"));
        historiquePanel.add(new JScrollPane(tableHistorique), BorderLayout.CENTER);

        // Panneau pour les boutons "Noter" et "Modifier Note" sous le tableau "Historique"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(noterButton);
        buttonPanel.add(modifierNoteButton);
        historiquePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajout du bouton "Retour" au panneau principal
        JButton retourButton = new JButton("Retour");
        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre actuelle
                MenuAccueil menu = new MenuAccueil(user);
                menu.setVisible(true); // Affiche le menu d'accueil
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(retourButton);

        // Création du séparateur horizontal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, empruntsPanel, historiquePanel);
        splitPane.setResizeWeight(0.5); // Répartition égale de l'espace

        // Création du panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Définir le contenu de la fenêtre principale
        setContentPane(mainPanel);

        // Ajuster la taille de la fenêtre en fonction du contenu
        pack();
        // Centrer la fenêtre sur l'écran
        setLocationRelativeTo(null);
        // Rendre la fenêtre visible
        setVisible(true);
    }

    // Méthode utilitaire pour trouver un emprunt par ISBN
    private Emprunter findEmpruntByISBN(String ISBN) {
        for (Emprunter em : emprunts) {
            if (em.getISBN().equals(ISBN)) {
                return em;
            }
        }
        return null;
    }
}
