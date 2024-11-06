import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        // Crearea ferestrei principale
        JFrame frame = new JFrame("Matrice Display with Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // Crearea unui JPanel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Matricea
        int[][] v = new int[10][10];
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 5; j++) {
                v[i][j] = i + j;
            }
        }

        // Crearea unui JPanel pentru afișarea matricei
        JPanel matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout(6, 6));

        // Adăugarea valorilor matricei ca JLabel-uri în matrixPanel
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 5; j++) {
                JLabel label = new JLabel(String.valueOf(v[i][j]), SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                matrixPanel.add(label);
            }
        }

        // Crearea unui JPanel pentru input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        // ComboBox pentru selecția unei opțiuni
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Opțiunea 1", "Opțiunea 2", "Opțiunea 3"});
        inputPanel.add(new JLabel("Selectați o opțiune:"));
        inputPanel.add(comboBox);

        // TextField pentru introducerea manuală a unui număr
        JTextField inputField = new JTextField(10);
        inputPanel.add(new JLabel("Introduceți un număr:"));
        inputPanel.add(inputField);

        // Buton pentru procesarea input-ului
        JButton processButton = new JButton("Procesează");
        inputPanel.add(processButton);

        // Adăugarea funcționalității butonului
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) comboBox.getSelectedItem();
                String inputText = inputField.getText();
                JOptionPane.showMessageDialog(frame,
                        "Ați selectat: " + selectedOption + "\nValoare introdusă: " + inputText);
            }
        });

        // Adăugarea componentelor în mainPanel
        mainPanel.add(matrixPanel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Configurarea ferestrei principale
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
