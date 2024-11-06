import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JFrame {
    public JPanel gamePanel;
    public JPanel statsPanel;
    public Player player;
    public Object[][] map;
    public int playerX, playerY;
    public int kc = 0;

    public GamePanel() {
        setTitle("Jocul lui Tony si Daria");
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
            public void paintComponent(Graphics g) {
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
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S ||
                        e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
                    handleMovement(e.getKeyCode());
                } else {
                    handleBuildingCreation(e.getKeyCode());
                }
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
    public void handleBuildingCreation(int keyCode) {
        Building building = null;

        if (keyCode == KeyEvent.VK_1) {
            building = new Building("Fantana Vietii", 10, 15);
        } else if (keyCode == KeyEvent.VK_2) {
            building = new Building("Monumentul Sabiei", 10, 15);
        }

        if (building != null) {
            if (player.getWood() >= building.getWoodCost() && player.getStone() >= building.getStoneCost()) {
                player.collectWood(-building.getWoodCost());
                player.collectStone(-building.getStoneCost());

                if (building.getName().equals("Fantana Vietii")) {
                    player.health = 100;
                    System.out.println("Ai construit Fantana Vietii! Viata ta a fost restaurata complet.");
                } else if (building.getName().equals("Monumentul Sabiei")) {
                    player.attack += player.attack * 0.1;
                    System.out.println("Ai construit Monumentul Sabiei! Atacul tau a crescut cu 10%.");
                }
            } else {
                System.out.println("Nu ai suficiente resurse pentru a construi " + building.getName());
            }
        }
    }
    public void spawnRandomObjects(int size) {
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            placeRandomObject(size, new Enemy("Caine salbatic", 8, 3, 50));
        }

        for (int i = 0; i < 3; i++) {
            Gatherable.Quality quality = randomQuality(random);
            placeRandomObject(size, new Tree(getQuantityByQuality(5, quality), quality));
        }

        for (int i = 0; i < 3; i++) {
            Gatherable.Quality quality = randomQuality(random);
            placeRandomObject(size, new Rock(getQuantityByQuality(5, quality), quality));
        }

        for (int i = 0; i < 3; i++) {
            Gatherable.Quality quality = randomQuality(random);
            placeRandomObject(size, new Grain(getQuantityByQuality(3, quality), quality));
        }
    }

    public void placeRandomObject(int size, Object obj) {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (map[y][x] != null);
        map[y][x] = obj;
    }
    public Gatherable.Quality randomQuality(Random random) {
        double chance = random.nextDouble();
        if (chance < 0.5) {
            return Gatherable.Quality.COMMON;
        } else if (chance < 0.8) {
            return Gatherable.Quality.RARE;
        } else {
            return Gatherable.Quality.EPIC;
        }
    }
    public int getQuantityByQuality(int base, Gatherable.Quality quality) {
        switch (quality) {
            case COMMON:
                return base * 2;
            case RARE:
                return base * 3;
            case EPIC:
                return base * 5;
            default:
                return base;
        }
    }

    public void handleMovement(int keyCode) {
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
                String quality = gatherable.quality.name();

                if (gatherable instanceof Tree) {
                    player.collectWood(gatherable.quantity);
                    System.out.println("Ai colectat lemn " + quality + ": " + gatherable.quantity);
                } else if (gatherable instanceof Rock) {
                    player.collectStone(gatherable.quantity);
                    System.out.println("Ai colectat piatra " + quality + ": " + gatherable.quantity);
                } else if (gatherable instanceof Grain) {
                    player.collectFood(gatherable.quantity);
                    System.out.println("Ai colectat mancare " + quality + ": " + gatherable.quantity);
                }
                map[newY][newX] = null;
            } else if (encountered instanceof Enemy) {
                Enemy enemy = (Enemy) encountered;
                battle(enemy);
                if (!enemy.isAlive) {
                    map[newY][newX] = null;
                    System.out.println("Ai invins un inamic!");
                    kc++;
                }
            }

            map[playerY][playerX] = null;
            playerX = newX;
            playerY = newY;
            map[playerY][playerX] = player;
        }
    }

    public void endGame() {
        JOptionPane.showMessageDialog(this, "Jocul s-a terminat. " + player.name + " a murit cu scorul "+ kc+"!", "Joc terminat!", JOptionPane.ERROR_MESSAGE);
        this.removeKeyListener(this.getKeyListeners()[0]);
        System.exit(0);
    }

    public void battle(Enemy enemy) {
        System.out.println("Batalia a inceput cu " + enemy.name);

        while (player.isAlive && enemy.isAlive) {
            int playerDamage = player.attack - enemy.defense;
            enemy.takeDamage(playerDamage);
            System.out.println("Jucatorul da " + playerDamage + " daune inamicului.");

            if (!enemy.isAlive) {
                System.out.println("Inamicul a fost invins!");
                return;
            }

            int enemyDamage = enemy.attack - player.defense;
            player.takeDamage(enemyDamage);
            System.out.println("Inamicul da " + enemyDamage + " daune jucatorului.");

            if (!player.isAlive) {
                System.out.println("Jocul s-a terminat, " + player.name + " a murit!");
                endGame();
                return;
            }
        }
    }

    public void drawMap(Graphics g) {
        int cellSize = 50;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] instanceof Player) {
                    g.setColor(Color.PINK); //ideea dariei
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

    public void updateStatsPanel() {
        statsPanel.removeAll();

        JLabel statsLabel = new JLabel("<html><font size=8>"
                + "Viata: " + player.health + "<br>"
                + "Atac: " + player.attack + "<br>"
                + "Aparare: " + player.defense + "<br>"
                + "Lemn: " + player.getWood() + "<br>"
                + "Piatra: " + player.getStone() + "<br>"
                + "Mancare: " + player.getFood() + "<br></font>"
                + "Pentru constructii apasa tasta:" + "<br>"
                + "1. Fountain Of Life" + "<br>"
                + "2. Sword Monument" + "</html>");


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
