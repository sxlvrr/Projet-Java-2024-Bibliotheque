import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

/**
 * Boîte de dialogue permettant de saisir une note et un commentaire.
 */
public class NoterDialog extends JDialog {
    private JSpinner noteSpinner;
    private JTextArea commentaireArea;
    private JButton validerButton;
    private JButton annulerButton;
    private Integer note;
    private String commentaire;

    /**
     * Constructeur de la boîte de dialogue de notation.
     * @param parent Fenêtre parente de la boîte de dialogue
     * @param title Titre de la boîte de dialogue
     */
    public NoterDialog(JFrame parent, String title) {
        super(parent, "Noter " + title, true); // Titre de la boîte de dialogue, modalité

        // Création du spinner pour la note
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 10, 1);
        noteSpinner = new JSpinner(spinnerModel);

        // Création des composants
        commentaireArea = new JTextArea(4, 20);
        validerButton = new JButton("Valider");
        annulerButton = new JButton("Annuler");

        // Ajout des écouteurs d'événements sur les boutons
        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                note = (Integer) noteSpinner.getValue(); // Récupération de la note sélectionnée
                commentaire = commentaireArea.getText(); // Récupération du commentaire saisi
                dispose(); // Ferme la boîte de dialogue
            }
        });

        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                note = null; // Réinitialisation de la note
                commentaire = null; // Réinitialisation du commentaire
                dispose(); // Ferme la boîte de dialogue
            }
        });

        // Création du panneau de contenu
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ajout des composants au panneau de contenu
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        inputPanel.add(new JLabel("Note : "));
        inputPanel.add(noteSpinner);

        JPanel commentairePanel = new JPanel(new BorderLayout());
        commentairePanel.add(new JLabel("Commentaire : "), BorderLayout.NORTH);
        commentairePanel.add(new JScrollPane(commentaireArea), BorderLayout.CENTER);

        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(commentairePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(validerButton);
        buttonPanel.add(annulerButton);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Définition du panneau de contenu pour la boîte de dialogue
        setContentPane(contentPane);

        // Ajustement automatique de la taille en fonction des composants
        pack();

        // Positionnement au centre de la fenêtre parent
        setLocationRelativeTo(parent);
    }

    /**
     * Affiche la boîte de dialogue de notation et récupère la note et le commentaire saisis.
     * @return Tableau d'objets contenant la note et le commentaire, ou null si annulé
     */
    public Object[] showNoteDialog() {
        setVisible(true); // Affiche la boîte de dialogue
        if (note != null && commentaire != null) {
            return new Object[]{note, commentaire}; // Retourne la note et le commentaire saisis
        } else {
            return null; // Retourne null si annulé
        }
    }
}
