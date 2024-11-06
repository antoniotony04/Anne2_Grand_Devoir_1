import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JFrame {
    private JPanel gamePanel;
    private JPanel upgradePanel;
    private Player player;
    private int[][] map;
    private int playerX, playerY; // Poziția jucătorului pe hartă

    public GamePanel() {
        setTitle("Survival Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        // Initializează harta
        int size = 10; // Dimensiunea matricei
        map = new int[size][size];
        playerX = size / 2;
        playerY = size / 2;

        // Adaugă player-ul pe hartă
        map[playerY][playerX] = 1; // 1 reprezintă jucătorul

        // Game panel
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMap(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(600, 600));
        add(gamePanel, BorderLayout.CENTER);

        // Upgrade panel
        upgradePanel = new JPanel();
        upgradePanel.setLayout(new BoxLayout(upgradePanel, BoxLayout.Y_AXIS));
        upgradePanel.setPreferredSize(new Dimension(200, 600));
        JLabel upgradeLabel = new JLabel("Upgrade-uri");
        upgradePanel.add(upgradeLabel);

        JButton upgradeButton = new JButton("Upgrade Attack");
        upgradeButton.addActionListener(e -> player.attack += 1);
        upgradePanel.add(upgradeButton);

        add(upgradePanel, BorderLayout.EAST);

        // Initializează player-ul
        player = new Player("Jucator", 10, 5, 100);

        // Adaugă key listener pentru mișcare
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                handleMovement(e.getKeyCode());
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    private void handleMovement(int keyCode) {
        int newX = playerX;
        int newY = playerY;

        // Verificăm input-ul
        if (keyCode == KeyEvent.VK_W) { // Sus
            newY--;
        } else if (keyCode == KeyEvent.VK_S) { // Jos
            newY++;
        } else if (keyCode == KeyEvent.VK_A) { // Stânga
            newX--;
        } else if (keyCode == KeyEvent.VK_D) { // Dreapta
            newX++;
        }

        // Verificăm limitele matricei
        if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length) {
            // Actualizăm harta
            map[playerY][playerX] = 0; // Lăsăm celula goală
            playerX = newX;
            playerY = newY;
            map[playerY][playerX] = 1; // Mutăm jucătorul
        }
    }

    private void drawMap(Graphics g) {
        int cellSize = 50; // Dimensiunea unei celule
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 1) {
                    g.setColor(Color.BLUE); // Jucătorul
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Celule goale
                }
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GamePanel game = new GamePanel();
            game.setVisible(true);
        });
    }
}
