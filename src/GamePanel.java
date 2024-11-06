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
    private Object[][] map;
    private int playerX, playerY;

    public GamePanel() {
        setTitle("Survival Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        int size = 10;
        map = new Object[size][size];
        playerX = size / 2;
        playerY = size / 2;

        player = new Player("Jucator", 10, 5, 100);
        map[playerY][playerX] = player;

        spawnRandomObjects(size);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMap(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(600, 600));
        add(gamePanel, BorderLayout.CENTER);

        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setPreferredSize(new Dimension(200, 600));
        updateStatsPanel();
        add(statsPanel, BorderLayout.EAST);

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

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                spawnRandomObjects(size);
                repaint();
            }
        }, 0, 20000);
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

        if (keyCode == KeyEvent.VK_W) {
            newY--;
        } else if (keyCode == KeyEvent.VK_S) {
            newY++;
        } else if (keyCode == KeyEvent.VK_A) {
            newX--;
        } else if (keyCode == KeyEvent.VK_D) {
            newX++;
        }

        if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length) {
            Object encountered = map[newY][newX];

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
                map[newY][newX] = 0;
            } else if (encountered instanceof Enemy) {
                Enemy enemy = (Enemy) encountered;
                battle(enemy);
                if (!enemy.isAlive) {
                    map[newY][newX] = null;
                    System.out.println("Defeated enemy!");
                }
            }

            map[playerY][playerX] = null;
            playerX = newX;
            playerY = newY;
            map[playerY][playerX] = player;
        }
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this, "Jocul s-a terminat. " + player.name + " a murit!", "Joc terminat!", JOptionPane.ERROR_MESSAGE);
        this.removeKeyListener(this.getKeyListeners()[0]);
        System.exit(0);
    }

    private void battle(Enemy enemy) {
        System.out.println("Battle started with " + enemy.name);

        while (player.isAlive && enemy.isAlive) {
            int playerDamage = Math.max(0, player.attack - enemy.defense);
            enemy.takeDamage(playerDamage);
            System.out.println("Player deals " + playerDamage + " damage to the enemy.");

            if (!enemy.isAlive) {
                System.out.println("Enemy defeated!");
                return;
            }

            int enemyDamage = Math.max(0, enemy.attack - player.defense);
            player.takeDamage(enemyDamage);
            System.out.println("Enemy deals " + enemyDamage + " damage to the player.");

            if (!player.isAlive) {
                System.out.println("The game ended, " + player.name + " died!");
                endGame();
                return;
            }
        }
    }

    private void drawMap(Graphics g) {
        int cellSize = 50;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] instanceof Player) {
                    g.setColor(Color.BLUE);
                } else if (map[y][x] instanceof Tree) {
                    g.setColor(Color.GREEN);
                } else if (map[y][x] instanceof Rock) {
                    g.setColor(Color.GRAY);
                } else if (map[y][x] instanceof Grain) {
                    g.setColor(Color.YELLOW);
                } else if (map[y][x] instanceof Enemy) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
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
                + "HP: " + player.health + "<br>"
                + "Attack: " + player.attack + "<br>"
                + "Defense: " + player.defense + "<br>"
                + "Wood: " + player.getWood() + "<br>"
                + "Stone: " + player.getStone() + "<br>"
                + "Food: " + player.getFood() + "</html>");

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
