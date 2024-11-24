import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
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
    public int enemyType = 0;

    public GamePanel() throws IOException {
        setTitle("Jocul lui Tony si Daria");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        int size = 10;
        map = new Object[size][size];
        playerX = size / 2;
        playerY = size / 2;

        FileReader fr = new FileReader("src/datePlayer.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        while((line = br.readLine())!=null){
            String[] datePlayer = line.split("/");
            String nume = datePlayer[0];
            int attack = Integer.parseInt(datePlayer[1]);
            int defense = Integer.parseInt(datePlayer[2]);
            int health = Integer.parseInt(datePlayer[3]);
            player = new Player(nume, attack, defense, health);
        }
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
                        e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D ||
                        e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    handleMovement(e.getKeyCode());
                } else if(e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_2) {
                    handleBuildingCreation(e.getKeyCode());
                }
                else if(e.getKeyCode() == KeyEvent.VK_F) {
                    useFood(e.getKeyCode());
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
        }, 0, 10000);
    }

    public void useFood(int keyCode){
        if(keyCode == KeyEvent.VK_F){

            if(player.health < 100){
                player.health = player.health+1;
                player.setFood(player.getFood()-10);
                System.out.println("Punctele de viata au crescut cu o unitate.");
            }
            else{
                System.out.println("Viata este deja maxima.");
            }

        }
    }
    public void handleBuildingCreation(int keyCode) {
        Building building = null;

        if (keyCode == KeyEvent.VK_1) {
            if(player.health==100){
                System.out.println("Viata este deja maxima");
            }
            else {
                building = new Building("Fantana Vietii", 10, 15);
            }
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
                    System.out.println("Ai construit Monumentul Sabiei! Atacul a crescut cu 10%.");
                }
            } else {
                System.out.println("Nu ai suficiente resurse pentru a construi " + building.getName());
            }
        }
    }

    /*public void changeEnemyType(Enemy main){
        ArrayList<Enemy> inamici = new ArrayList<Enemy>();
        //enemyType = 0 => Caine salbatic
        inamici.add(new Enemy("Caine salbatic", 15, 3, 50));
        inamici.add(new Enemy("Lup Salbatic", 20, 5, 70));
        inamici.add(new Enemy("Lup Albastru", 30, 5, 80));
        main = inamici.get(enemyType);
        if(enemyType<3){
            enemyType++;
        }

    }*/
    //Idee de viitor de facut sa se spawneze inamici mai grei deoarece itemele ofera bonusuri cu ajutorul carora inamicii devin prea slabi pentru player

    public void spawnRandomObjects(int size) {
        Random random = new Random();
        Enemy main = new Enemy("Caine salbatic", 15, 3, 50);
        for (int i = 0; i < 3; i++) {
            placeRandomObject(size, main);
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
        } else if (chance < 0.9) {
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

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            newY--;
        } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            newY++;
        } else if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            newX--;
        } else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
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
                Item dropat = enemy.dropItem();
                if (dropat != null) {
                    System.out.println("Itemul dropat: " + dropat.toString());

                    valoareAtacComparator a = new valoareAtacComparator();
                    aparareComparator b = new aparareComparator();

                    if (dropat.type == 1) {
                        if (player.getArma() != dropat) {
                            if (a.compare(player.getArma(), dropat) < 0) {
//                                player.addItemToInventory(player.getArma());
                                player.setArma(dropat);
                                System.out.println("Arma echipata: " + dropat.name);
//                                System.out.println("Arma veche in inventar.");
                                updateStatsPanel();
                            } else {
                                System.out.println("Arma existenta este mai buna decat itemul dropat.");
                                player.addItemToInventory(dropat);
                                updateStatsPanel();

                            }
                        }
                    } else if (dropat.type == 2) {
                        if (player.getCasca() != dropat) {
                            if (b.compare(player.getCasca(), dropat) < 0) {
//                                player.addItemToInventory(player.getCasca());
                                player.setCasca(dropat);
                                System.out.println("Casca echipata: " + dropat.name);
//                                System.out.println("Casca veche in inventar");
                                updateStatsPanel();
                            } else {
                                System.out.println("Casca existenta este mai buna decat itemul dropat.");
                                player.addItemToInventory(dropat);
                                updateStatsPanel();
                            }
                        }
                    } else if (dropat.type == 3) {
                        if (player.getArmura() != dropat) {
                            if (b.compare(player.getArmura(), dropat) < 0) {
//                                player.addItemToInventory(player.getArmura());
                                player.setArmura(dropat);
                                System.out.println("Armura echipata: " + dropat.name);
//                                System.out.println("Armura veche in inventar.");
                                updateStatsPanel();
                            } else {
                                System.out.println("Armura existenta este mai buna decat itemul dropat.");
                                player.addItemToInventory(dropat);
                                updateStatsPanel();
                            }
                        }
                    }
                    updateStatsPanel();
                } else {
                    System.out.println("Inamicul nu a dropat nimic.");
                    updateStatsPanel();
                }
            }

            int enemyDamage = Math.max(0, enemy.attack - player.defense);
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
        //HTML folosit cu JLabel https://stackoverflow.com/questions/6635730/how-do-i-put-html-in-a-jlabel-in-java
        String inventory = "Inventar: <br>";
        for (Item item : player.getInventar()) {
            inventory += "->" + item.name + "<br>";
        }


        JLabel statsLabel = new JLabel("<html><font size=5>"
                + "Viata: " + player.health + "<br>"
                + "Atac: " + player.attack + "<br>"
                + "Aparare: " + player.defense + "<br>"
                + "Lemn: " + player.getWood() + "<br>"
                + "Piatra: " + player.getStone() + "<br>"
                + "Mancare: " + player.getFood() + "<br><br>"
                + inventory.toString()
                + "Pentru constructii apasa tasta:<br>"
                + "1. Fountain Of Life<br>"
                + "2. Sword Monument"
                + "</font></html>");

        statsPanel.add(statsLabel);
        statsPanel.revalidate();
        statsPanel.repaint();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GamePanel game = null;
            try {
                game = new GamePanel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            game.setVisible(true);
        });
    }
}
