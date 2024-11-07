import java.util.Random;

public class Enemy extends Character {
    public Enemy(String name, int attack, int defense, int health) {
        super(name, attack, defense, health);
    }

    public Item dropItem() {
        Random random = new Random();
        double sansa = 1;
        Item[] dropuri = {
                new Item("Sabie de Lemn", 1, 0, 20),
                new Item("Sabie de Fier", 1, 0, 25),
                new Item("Casca de Piele", 2, 5, 0),
                new Item("Casca de fier", 2, 10, 0),
                new Item("Armura de piele", 3, 10, 0),
                new Item("Armura de fier", 3, 20, 0)
        };


        if (random.nextDouble() < sansa) {
            int i = 0;
            while (i == 0) {
                i = random.nextInt(4);
            }
            switch (i) {
                case 1: {
                    double sansaSabieLemn = 0.5;
                    double sansaSabieFier = 0.2;
                    double x = random.nextDouble();
                    if (x > sansaSabieLemn) {
                        return dropuri[0];
                    } else if (x < sansaSabieFier) {
                        return dropuri[1];
                    } else {
                        return null;
                    }

                }

                case 2: {
                    double sansaCascaPiele = 0.5;
                    double sansaCascaFier = 0.2;
                    double x = random.nextDouble();
                    if (x > sansaCascaPiele) {
                        return dropuri[2];
                    } else if (x < sansaCascaFier) {
                        return dropuri[3];
                    } else {
                        return null;
                    }
                }

                case 3: {
                    double sansaArmuraPiele = 0.5;
                    double sansaArmuraFier = 0.2;
                    double x = random.nextDouble();
                    if (x > sansaArmuraPiele) {
                        return dropuri[4];
                    } else if (x < sansaArmuraFier) {
                        return dropuri[5];
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void damage(int amount) {
        int effectiveDamage = amount - defense;
        if (effectiveDamage > 0) {
            health -= effectiveDamage;
            System.out.println(name + " a primit " + effectiveDamage + " daune. Viata ramasa: " + health);
        } else {
            System.out.println(name + " nu a fost ranit datorita apararii.");
        }
        if (health <= 0) {
            health = 0;
            die();
        }
    }

    @Override
    public void die() {
        isAlive = false;
        System.out.println(name + " a murit!");
    }

    @Override
    public void takeDamage(int amount) {
        damage(amount);
    }
}
