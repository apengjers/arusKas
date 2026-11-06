package mpkm;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            login frame = new login();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}