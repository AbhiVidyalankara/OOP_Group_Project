package Medicare;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Datastore.store = Datastore.load();
            } catch (Exception e) {
                Datastore.store = new Datastore();
                Datastore.store.seedSampleData();
            }
            new UI();
        });
    }
}
