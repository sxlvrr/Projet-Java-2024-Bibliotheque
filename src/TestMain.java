import java.time.LocalDate;

import javax.swing.SwingUtilities;

/**
 * Classe de Test permettant de lancer l'application
 */
public class TestMain {
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		//TEST ENTIER
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            }
		});
	}
}
