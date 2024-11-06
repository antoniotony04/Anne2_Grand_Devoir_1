import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JFrame {
    private JPanel gamePanel;
    private JPanel statsPanel;
    private Player player;
    private Object[][] map; // Harta cu obiecte si inamici
    private int playerX, playerY; // Poziția jucatorului pe hartă

    public GamePanel() {
        setTitle("Survival Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        // Initializeaza harta
        int size = 10; // Dimensiunea matricei
        map = new Object[size][size];
        playerX = size / 2;
        playerY = size / 2;

        // Initializează jucătorul
        player = new Player("Jucator", 10, 5, 100);
        map[playerY][playerX] = player;

        // Populează harta inițial
        spawnRandomObjects(size);

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

        // Stats panel
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setPreferredSize(new Dimension(200, 600));
        updateStatsPanel();
        add(statsPanel, BorderLayout.EAST);

        // Adaugă key listener pentru mișcare
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                handleMovement(e.getKeyCode());
                updateStatsPanel();
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Spawnează obiecte periodic
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                spawnRandomObjects(size);
                repaint();
            }
        }, 0, 20000); // La fiecare 20 de secunde
    }

    private void spawnRandomObjects(int size) {
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);

            if (map[y][x] == null) {
                if (random.nextDouble() < 0.2) {
                    map[y][x] = new Enemy("Caine salbatic", 8, 3, 50);
                } else if (random.nextDouble() < 0.5) {
                    map[y][x] = new Tree(5, Gatherable.Quality.COMMON);
                } else if (random.nextDouble() < 0.3) {
                    map[y][x] = new Rock(5, Gatherable.Quality.RARE);
                } else {
                    map[y][x] = new Grain(3, Gatherable.Quality.EPIC);
                }
            }
        }
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
            Object encountered = map[newY][newX];

            // Gestionăm interacțiunile
            if (encountered instanceof Gatherable) {
                Gatherable gatherable = (Gatherable) encountered;
                if (gatherable instanceof Tree) {
                    player.collectWood(gatherable.quantity);
                    System.out.println("Collected wood: " + gatherable.quantity);
                } else if (gatherable instanceof Rock) {
                    player.collectStone(gatherable.quantity);
                    System.out.println("Collected stone: " + gatherable.quantity);
                } else if (gatherable instanceof Grain) {
                    player.collectFood(gatherable.quantity);
                    System.out.println("Collected food: " + gatherable.quantity);
                }
                map[newY][newX] = 0; // Golim locul
            } else if (encountered instanceof Enemy) {
                Enemy enemy = (Enemy) encountered;
                battle(enemy);
                if (!enemy.isAlive) {
                    map[newY][newX] = null; // Inamicul a murit
                    System.out.println("Defeated enemy!");
                }
            }

            // Mutăm jucătorul
            map[playerY][playerX] = null; // Lăsăm celula goală
            playerX = newX;
            playerY = newY;
            map[playerY][playerX] = player;
        }
    }
    private void endGame() {
        // Show a message dialog
        JOptionPane.showMessageDialog(this, "Jocul s-a terminat. " + player.name + " a murit!", "Joc terminat!", JOptionPane.ERROR_MESSAGE);

        // Disable further input
        this.removeKeyListener(this.getKeyListeners()[0]); // Removes the key listener

        // Optionally, you can stop object spawning
        // (Assuming the Timer is declared as a class-level variable)
        // timer.cancel();

        // Exit the game after showing the message (optional)
        System.exit(0); // Terminate the program
    }

    private void battle(Enemy enemy) {
        System.out.println("Battle started with " + enemy.name);

        while (player.isAlive && enemy.isAlive) {
            // Player attacks first
            int playerDamage = Math.max(0, player.attack - enemy.defense);
            enemy.takeDamage(playerDamage);
            System.out.println("Player deals " + playerDamage + " damage to the enemy.");

            if (!enemy.isAlive) {
                System.out.println("Enemy defeated!");
                return; // Exit battle if the enemy dies
            }

            // Enemy attacks back
            int enemyDamage = Math.max(0, enemy.attack - player.defense);
            player.takeDamage(enemyDamage);
            System.out.println("Enemy deals " + enemyDamage + " damage to the player.");

            if (!player.isAlive) {
                System.out.println("The game ended, " + player.name + " died!");
                endGame(); // Call method to end the game
                return;
            }
        }
    }



    private void drawMap(Graphics g) {
        int cellSize = 50; // Dimensiunea unei celule
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] instanceof Player) {
                    g.setColor(Color.BLUE); // Jucătorul
                } else if (map[y][x] instanceof Tree) {
                    g.setColor(Color.GREEN); // Copac
                } else if (map[y][x] instanceof Rock) {
                    g.setColor(Color.GRAY); // Rocă
                } else if (map[y][x] instanceof Grain) {
                    g.setColor(Color.YELLOW); // Cereale
                } else if (map[y][x] instanceof Enemy) {
                    g.setColor(Color.RED); // Inamic
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Celule goale
                }
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    private void updateStatsPanel() {
        statsPanel.removeAll();

        JLabel statsLabel = new JLabel("<html>"
                + "<font size=8>HP: " + player.health + "<br>"
                + "Attack: " + player.attack + "<br>"
                + "Defense: " + player.defense + "<br>"
                + "Wood: " + player.getWood() + "<br>"
                + "Stone: " + player.getStone() + "<br>"
                + "Food: " + player.getFood() + "</font></html>");

        statsPanel.add(statsLabel);
        statsPanel.revalidate();
        statsPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GamePanel game = new GamePanel();
            game.setVisible(true);
        });
    }
}
