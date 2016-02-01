import java.io.*;
import java.util.*;

public class Console {
    // IO stuff
    private static File data;
    private static Scanner sc;
    private static int temp = 0;
    // Player related stuff
    private static boolean hasPlayer;
    private static String name;
    private static int level;
    private static int[] stats = new int[3];
    // stats is an array of size 3, format is {atk, def, maxHP}
    private static int xp;
    private static String lastCommand;
    // Random other stuff
    private static int[] xpTable;

    public static void main(String args[]) throws IOException {
        hasPlayer = false;
        sc = new Scanner(System.in);
        getXP();
        System.out.println("Enter Command");
        String str = "";
        do {
            str = sc.nextLine();
            if (str.contains("test")) {
                test();
            } else {
                parseCommand(str);
            }
        } while (!str.equals("quit"));
    }

    // random battle
    public static void tutorial() {
        System.out.println("Welcome to the tutorial. In here you will learn how to battle.");
        Enemy tutorial = new Enemy("tutorial");// rite
        battle(tutorial);
    }

    // starts a new game with lvl 0 player
    public static void newGame() {
        data = null;
        // we have to instantiate stats here...
        stats = new int[] { 2, 2, 10 };
        xp = 0;
        level = 0;
        name = "";
        System.out.print("What is your name? ==> ");
        name = sc.next();
        sc.nextLine();
        hasPlayer = true;
        tutorial();
    }

    public static void save(String name) throws IOException {
        if (hasPlayer) {
            File playerData = new File(name);
            FileWriter fileWriter = new FileWriter(playerData, false);
            fileWriter.write(xp + "\n");
            for (int sn : stats) {
                fileWriter.write(sn + " ");
            }
            System.out.println("Saved");
            fileWriter.close();
        } else {
            System.out.println("Error 404 player not found");
        }
    }

    public static void loadGame(String Name) throws FileNotFoundException {

        // Stores a backup copy of stuff in case FileNotFound
        String tempName = name;
        name = Name;
        int tempXp = xp;

        // Reads file
        data = new File(name);
        if (!data.exists())
            return;
        Scanner Sc = new Scanner(data);
        try {
            xp = Sc.nextInt();
        } catch (Exception e) {
            Sc.close();
            return;
        }
        Sc.nextLine();
        String[] tempStatsArray = Sc.nextLine().split(" ");

        // Displays Stats
        System.out.println("Player: " + name);
        System.out.println("Total XP: " + xp);
        System.out.println("Stats: " + Arrays.toString(tempStatsArray));

        // Asks if player is correct
        System.out.println("Are you sure you want to load " + name + "? (y/n)");
        String temp = sc.next();
        if (temp.toLowerCase().charAt(0) == 'y') {
            try {
                stats[0] = Integer.parseInt(tempStatsArray[0]);
                stats[1] = Integer.parseInt(tempStatsArray[1]);
                stats[2] = Integer.parseInt(tempStatsArray[2]);
                System.out.println("Player " + name + " loaded");
                hasPlayer = true;
                levelUp();
            } catch (Exception e) {
                Sc.close();
                return;
            }
        } else {
            name = tempName;
            xp = tempXp;
        }

        // Finalizes the load
        Sc.close();
    }

    // creates xp table for level up purposes
    private static void getXP() {
        xpTable = new int[61];
        for (int w = 0; w < 61; w++) {
            xpTable[w] = (int) Math.pow(w, 3);
        }
    }

    private static String dispStats() {
        if (hasPlayer) {
            String temp = "Level: " + level + " HP: " + stats[2] + " Atk: " + stats[0] + " Def: " + stats[1]
                    + " XP to next level: " + (xp < 60 ? xpTable[level + 1] - xp : 0);
            System.out.println(temp);
            return "" + name + " " + level + " " + xp + temp;
        } else {
            System.out.println("No player found");
            return "";
        }
    }

    public static void levelUp() {
        while (xp >= xpTable[level]) {
            if (level != 60) {
                xp -= xpTable[level];
                System.out.println("Level up: " + level + " to " + ++level);
                stats[2] += 3; // increasing hp/atk/def
                stats[0] += 1;
                stats[1] += 1;
            } else {
                System.out.println("LEVEL MAXED");
                break;
            }
        }
    }

    public static int hit(Enemy enemy) {
        int dmg = stats[0] - enemy.getDef() / 2;
        return dmg;
    }

    public static void battle(Enemy en) {
        System.out.println("Enemy " + en.getName() + " found. Initating battle.");

        if (en.isSpecial()) {
            en.runScript();
        }
        {
            while (en.getHP() > 0 && stats[2] > 0) {

                int enhit = hit(en);
                int plhit = en.hit(level, stats[1]);

                en.changeHP(-1 * enhit);
                if (en.getHP() <= 0) {
                    System.out.println("You have defeated enemy " + en.getName());
                    xp += en.getXp();
                    System.out.println("You have gained: " + en.getXp() + " xp.");
                    levelUp();
                    break;
                } else {
                    if (plhit <= 0)
                        plhit = 0;
                    System.out.println("Enemy deals " + plhit + " damage!");
                    stats[2] -= plhit;
                    System.out.print("Player stats:");
                    dispStats();
                    System.out.println();
                    if (stats[2] <= 0) {
                        System.out.println("player " + name + " has died!");
                        hasPlayer = false;
                        break;
                    } else {
                        System.out.println("Player deals " + enhit + "damage!");
                        System.out.print("Enemy health:" + en.getHP());
                        System.out.println();
                    }
                }

            }
        }
    }

    public static void inBattle() {
        String cmd = sc.nextLine();
        while (!cmd.equals("exit")) {
            cmd = sc.nextLine();
        }
    }

    public static void parseCommand(String str) throws IOException {
        if (str.length() == 0) {
            str = lastCommand;
            if (!(lastCommand.equalsIgnoreCase("explore")))
                return;
        } else
            lastCommand = str;
        String[] args = str.split(" ");
        switch (args[0]) {
        case "new":
            newGame();
            break;
        case "load":
            System.out.println("What is the player's name?");
            loadGame(sc.next());
            System.out.println("Player was " + (hasPlayer == true ? "" : "not ") + "loaded");
            break;
        case "quit":
            System.out.println("Saving...");
            save(name);
            System.out.println("Saved!");
            System.exit(0);
            break;
        case "save":
            save(name);
            break;
        case "stats":
            dispStats();
            break;
        case "explore":
            explore();
            break;
        case "help":
            System.out.println(
                    "type \"new\" to start a new game\ntype \"load\" to load a game\ntype \"quit\" to save and quit game\ntype \"explore\" to look for an enemy\ntype \"help\" for help");
            break;
        default:
            System.out.println();
        }
    }

    public static void explore() {
        if (hasPlayer && stats[2] > 0) {
            if (num(100) > 50) {
                Enemy enemy = new Enemy(level);
                battle(enemy);
            } else {
                System.out.println("You explored the area, but nothing was found");
            }
        } else {
            System.out.println("Player is dead. Sorry m8");
        }
    }

    public static int num(int n) {
        return (int) (Math.random() * n) + 1;
    }

    // test() is for testing methods
    public static void test() {
        Enemy t = new Enemy("testdummy", 70, 35, 20, 10, 1.5, false, null);
        System.out.println(t.hit(60, 60));
    }

}

@SuppressWarnings("all")
class Enemy {
    private String name;
    private int level, maxHp, maxAtk, maxDef, hp, atk, def;
    private boolean special;
    // script <Threshold,Text>
    private Map<Float, String> script;
    private int baseCrit, crit;
    private double baseCritD, critD;

    public Enemy(String name) {
        this.name = name;
        if (name.equals("tutorial")) {
            // level = (int) Math.random()
            maxHp = hp = 10;
            maxAtk = atk = 1;
            maxDef = def = 0;
            baseCrit = crit = 10;
            baseCritD = critD = 1.5;
            special = true;
            script = new TreeMap<Float, String>();
            script.put(1.1f, "Tutorial enemy found. Initiating battle.");
            script.put(1f, dispStats());
        }

    }

    public Enemy(int playerLevel) {
        Map<String, Integer> enemyChance = new HashMap<>();
        enemyChance.put("slime", 5);
        if (playerLevel >= 5)
            enemyChance.put("frog", 10);
        if (playerLevel >= 10)
            enemyChance.put("skeleton", 20);
        if (playerLevel >= 15)
            enemyChance.put("killer dog", 25);
        if (playerLevel >= 20)
            enemyChance.put("farthest boss", 30);
        int chance = 0;
        for (int x : enemyChance.values())
            chance += x;
        chance = rInt(chance);
        Object[] ar = enemyChance.values().toArray();
        int index = ar.length - 1;
        while (chance > 0 && index > 0) {
            chance -= (int) ar[index--];
        }
        Object[] names = enemyChance.keySet().toArray();
        String enName = (String) names[index];
        this.name = enName;
        switch (enName) {
        case "slime":
            maxHp = hp = 10;
            maxAtk = atk = 1;
            maxDef = def = 0;
            baseCrit = crit = 10;
            baseCritD = critD = 1.5;
            special = false;
            break;
        case "frog":
            maxHp = hp = 15;
            maxAtk = atk = 3;
            maxDef = def = 3;
            baseCrit = crit = 20;
            baseCritD = critD = 1.75;
            special = false;
            break;
        case "skeleton":
            maxHp = hp = 25;
            maxAtk = atk = 5;
            maxDef = def = 7;
            baseCrit = crit = 30;
            baseCritD = critD = 1.75;
            special = false;
            break;
        case "killer dog":
            maxHp = hp = 40;
            maxAtk = atk = 8;
            maxDef = def = 12;
            baseCrit = crit = 40;
            baseCritD = critD = 2;
            special = false;
            break;
        case "farthest boss":
            maxHp = hp = 100;
            maxAtk = atk = 20;
            maxDef = def = 15;
            baseCrit = crit = 50;
            baseCritD = critD = 2.5;
            special = true;
            script = new TreeMap<Float, String>();
            // put them in the opposite order you want the script to be
            // displayed
            script.put(1.1f, "*lunges at you*");
            script.put(1.2f, "NOW!");
            script.put(1.3f, "Sadly you won't be able to beat me.");
            script.put(1.4f, "So you've finally reached me. Congratulations.");
        }
    }

    public Enemy(String name, int playerLevel) {
        this.name = name;
        maxHp = hp = 10;
        maxAtk = atk = 1;
        maxDef = def = 0;
        baseCrit = crit = 10;
        baseCritD = critD = 1.5;
        special = false;
        script = new TreeMap<Float, String>();
        script.put(1f, dispStats());

    }

    public Enemy(String name, int baseHP, int baseATK, int baseDEF, int baseCRIT, double baseCRITD, boolean spec,
            Map<Float, String> messages) {
        this.name = name;
        maxHp = hp = baseHP;
        maxAtk = atk = baseATK;
        maxDef = def = baseDEF;
        baseCrit = crit = baseCRIT;
        baseCritD = critD = baseCRITD;
        special = spec;
        script = messages;
    }

    public static int getRandPercent() {
        return (int) (Math.random() * 100) + 1;
    }

    public static int rInt(int num) {
        return (int) (Math.random() * num);
    }

    public String dispStats() {
        return "HP: " + hp + " ATK: " + atk + " DEF: " + def;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHP() {
        return hp;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getCrit() {
        return crit;
    }

    public double getCritD() {
        return critD;
    }

    public int getXp() {
        int getXp = level + (atk * 3) + (def * 3) + (hp);
        return getXp > 0 ? getXp : 1;
    }

    public int changeHP(int change) {
        return hp += change;
    }

    public boolean isSpecial() {
        return special;
    }

    public void runScript() {

        Iterator<Map.Entry<Float, String>> entries = script.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Float, String> entry = entries.next();
            if (entry.getKey() >= getPercentHp()) {
                System.out.println(entry.getValue());
                entries.remove();
            }
        }
    }

    private float getPercentHp() {
        return hp / maxHp;
    }

    private void delay(long num) {
        try {
            Thread.sleep(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int hit(int playerLevel, int playerDef) {
        int tempCrit = crit + playerLevel / 2;
        int dmg = atk - playerDef / 2;
        if (getRandPercent() <= tempCrit) {
            System.out.println("Enemy critical hit. Damage multiplier is " + String.format("%.2f", critD));
            dmg = (int) (dmg * critD);
        }
        return dmg;
    }
}
// made by AkaiNoCode and Anonaipai on collabedit.com
