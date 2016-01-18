package textRpg;

import java.io.*;
import java.util.*;

public class Console {
    // IO stuff
    private static File data;
    private static String fileName;
    private static Scanner sc;

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
        fileName = "player";
        System.out.print("What is your name? ==> ");
        sc.next();
        name = sc.nextLine();
        hasPlayer = true;
        tutorial();
    }

    public static void save(String savename) throws IOException {
        File playerData = new File(savename);
        FileWriter fileWriter = new FileWriter(playerData, false);
        fileWriter.write(name + " " + xp + "\n");
        for (int sn : stats) {
            fileWriter.write(sn + " ");
        }
        fileWriter.close();
    }

    public static void loadGame(String Name) throws FileNotFoundException {
        String tempName = name;
        int tempXp = xp;
        fileName = Name;
        data = new File(fileName);
        Scanner Sc = new Scanner(data);
        name = Sc.next();
        System.out.println("Player: " + name);
        xp = Sc.nextInt();
        System.out.println("Total XP: " + xp);
        Sc.nextLine();
        String[] tempStatsArray = Sc.nextLine().split(" ");
        System.out.println("Stats: " + Arrays.toString(tempStatsArray));
        System.out.println("Are you sure you want to load " + name + "? (y/n)");
        String temp = sc.next();
        if (temp.toLowerCase().charAt(0) == 'y') {
            stats[0] = Integer.parseInt(tempStatsArray[0]);
            stats[1] = Integer.parseInt(tempStatsArray[1]);
            stats[2] = Integer.parseInt(tempStatsArray[2]);
            System.out.println("Player " + name + " loaded");
        } else {
            name = tempName;
            xp = tempXp;
            System.out.println("Player was not loaded");
        }
        Sc.close();
        hasPlayer = true;
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
            String temp = "HP: " + stats[2] + " Atk: " + stats[0] + " Def: " + stats[1];
            System.out.println(temp);
            return "" + name + " " + level + " " + xp + temp;
        } else {
            System.out.println("No player found");
            return "";
        }
    }

    public static void levelUp() {
        while (xp >= xpTable[level]) {
            xp -= xpTable[level];
            System.out.println("Level up: " + level + " to " + ++level);
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
                    System.out.println("Enemy deals " + plhit + " damage!");
                    stats[2] -= plhit;
                    System.out.print("Player stats:");
                    dispStats();
                    System.out.println();
                    if (stats[2] <= 0) {
                        System.out.println("player " + name + " has died!");
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
        if(str.length() == 0)
        	str = lastCommand;
        else
        	lastCommand = str;
    	String[] args = str.split(" ");
        switch (args[0]) {
        case "new":
            newGame();
            break;
        case "load":
            System.out.println("What is the filename? (It's usually player.dat)");
            loadGame(sc.next());
            break;
        case "quit":
            System.out.println("Saving...");
            save(fileName);
            System.exit(0);
            break;
        case "save":
            save(fileName);
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
                Enemy slime = new Enemy("slime", level);
                battle(slime);
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
    private ArrayList<String> script;
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
            script = new ArrayList<String>();
            script.add("Tutorial enemy found. Initiating battle.");
            script.add(dispStats());
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
        script = new ArrayList<String>();
        script.add(dispStats());

    }

    public Enemy(String name, int baseHP, int baseATK, int baseDEF, int baseCRIT, double baseCRITD, boolean spec,
            ArrayList<String> messages) {
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
        return level + (atk * 3) + (def * 3) + (hp);
    }

    public int changeHP(int change) {
        return hp += change;
    }

    public boolean isSpecial() {
        return special;
    }

    public void runScript() {
        for (String str : script)
            System.out.println(str);
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
