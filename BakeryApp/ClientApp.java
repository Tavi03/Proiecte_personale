import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClientApp extends JFrame {
    private DefaultListModel<String> cakeListModel;
    private JList<String> cakeList;
    private JLabel quantityLabel;
    private JTextField quantityTextField;
    private JButton buyButton;
    private JButton closeButton;
    private Double totalPrice;

    public ClientApp() {
        setTitle("Cofetarie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 540);
        setLocationRelativeTo(null);

        // Initializam datele
        cakeListModel = new DefaultListModel<>();
        cakeList = new JList<>(cakeListModel);
        quantityLabel = new JLabel("Cantitate (multiplu de 100):");
        quantityTextField = new JTextField(10);
        buyButton = new JButton("Cumpara");
        closeButton = new JButton("Iesire");
        totalPrice = 0.0;

        // Adaugam produsele din fisierul cakes.txt
        addCakesFromFile();

        // Aranjarea datelor in JFrame
        setLayout(new BorderLayout());

        JPanel cakePanel = new JPanel();
        cakePanel.setLayout(new BorderLayout());
        cakePanel.add(new JScrollPane(cakeList), BorderLayout.CENTER);

        JPanel buyPanel = new JPanel();
        buyPanel.add(quantityLabel);
        buyPanel.add(quantityTextField);
        buyPanel.add(buyButton);
        buyPanel.add(closeButton);

        add(cakePanel, BorderLayout.CENTER);
        add(buyPanel, BorderLayout.SOUTH);


        // Cream ActionListeners pentru cele 2 butoare
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buySelectedCake();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = "C:\\Users\\Tavi\\Documents\\GitHub\\Proiecte_personale_neterminate\\Cofetarie\\src\\cakes.txt";
                updateCakeFile(cakeListModel, filePath);
                dispose();
            }
        });
        setVisible(true);
    }

    // Metoda pentru adaugarea datelor unui produs in lista
    private void addCake(String name, int quantity, double pricePerKg) {
        String cakeInfo = name + " - " + quantity + " grame disponibile - " + pricePerKg + " RON/kg";
        cakeListModel.addElement(cakeInfo);
    }

    // Metoda pentru obtinerea datelor fiecarui produs din fisierul cakes.txt
    private void addCakesFromFile() {
        String filePath = "C:\\Users\\Tavi\\Documents\\GitHub\\Proiecte_personale_neterminate\\Cofetarie\\src\\cakes.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String name = parts[0];
                int grams = Integer.parseInt(parts[1]);
                double price = Double.parseDouble(parts[2]);
                addCake(name, grams, price);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda pentru genstionarea datelor unui produs selectat atunci cand se apasa butonul 'Cumpara'
    private void buySelectedCake() {
        int selectedIndex = cakeList.getSelectedIndex();
        if (selectedIndex != -1) {
            try {
                int quantityToBuy = Integer.parseInt(quantityTextField.getText());
                if (quantityToBuy % 100 != 0) {
                    JOptionPane.showMessageDialog(this, "Introdu cantitate ce este multiplu de 100!", "Eroare!", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    // Obtine informatiile despre produsul selectat
                    String selectedCakeInfo = cakeListModel.getElementAt(selectedIndex);
                    String[] parts = selectedCakeInfo.split(" - ");
                    String cakeName = parts[0];
                    int availableQuantity = Integer.parseInt(parts[1].split(" ")[0]);
                    double pricePerKg = Double.parseDouble(parts[2].substring(0, parts[2].indexOf("R")));

                    if (quantityToBuy > availableQuantity) {
                        JOptionPane.showMessageDialog(this, "Stoc insuficient!", "Eroare!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Modificam cantitatea disponibila
                        int newQuantity = availableQuantity - quantityToBuy;

                        // Actualizam cantitatea in lista
                        cakeListModel.setElementAt(cakeName + " - " + newQuantity + " grame disponibile - " + pricePerKg + " RON/kg", selectedIndex);
                        if (newQuantity == 0) {
                            cakeListModel.removeElementAt(selectedIndex);
                        }
                        totalPrice = totalPrice + quantityToBuy * (pricePerKg/1000);
                        JOptionPane.showMessageDialog(this, "Ati cumparat " + quantityToBuy + " grame de  " + cakeName + ". Multumim!\n Pretul total va fi: " + totalPrice + " RON.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Te rog introdu o cantitate valida.", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "Te rog selecteaza un produs!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metoda pentru actualizarea fisierului cu produse la iesirea clientului din aplcatie
    private void updateCakeFile(DefaultListModel<String> cakeListModel, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < cakeListModel.size(); i++) {
                String cakeInfo = cakeListModel.getElementAt(i);
                String[] parts = cakeInfo.split(" - ");
                String cakeName = parts[0];
                int availableQuantity = Integer.parseInt(parts[1].split(" ")[0]);
                double pricePerKg = Double.parseDouble(parts[2].substring(0, parts[2].indexOf("RON")));

                if (availableQuantity > 0){
                    writer.write(cakeName + ", " + availableQuantity + ", " + pricePerKg + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
