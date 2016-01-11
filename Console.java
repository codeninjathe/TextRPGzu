import java.io.*;
import java.util.*;

public class Console {
    private static File data;
    private static Scanner sc;
    private static int[] stats; //stats is an array of size 3, format is {atk, def, maxHP}
    private static int[] xpTable;
    public static String playerName;
    private static int xp;
    private static int level;
    private static String name;
    private static String fileName;
    
    public static void main(String args[]) throws IOException {
        sc = new Scanner(System.in);
        getXP();
        System.out.println("Entry Command");
        parseCommand(sc.nextLine());
    }

    private static void getXP() {
        xpTable = new int[61];
        for (int w = 0; w < 61; w++) {
            xpTable[w] = (int) Math.pow(w, 3);
        }
    }

    public static void newGame() {
        data = null;
        stats = new int[0];
        xp = 0;
        level = 0;
        name = "";
        fileName = "player";
        System.out.print("What is your name? ==> ");
        sc.next();
        name = sc.nextLine();
        tutorial();
    }
    
    public static void tutorial(){
        System.out.println("Welcome to the tutorial. In here you will learn how to battle.");
        Enemy tutorial = new Enemy("tutorial");//rite
        battle(tutorial);
    }
    
    public static void save(String savename) throws IOException{
        File playerData= new File(savename);
        FileWriter fileWriter = new FileWriter(playerData, false);                                             
        fileWriter.write(name+" "+xp+ "\n");
        for(int sn:stats){
            fileWriter.write(sn + " ");
        }
        fileWriter.close();
    }
    
    public static void loadGame(String fileName) throws FileNotFoundException {
        data = new File(fileName);
        Scanner sc = new Scanner(data);
        playerName = sc.next();
        xp = sc.nextInt();
        sc.nextLine();
        stats[0] = sc.nextInt();
        stats[1] = sc.nextInt(); 
        stats[2] = sc.nextInt();
    }
    
    public static void inBattle(){
       String cmd = sc.nextLine();
       while(!cmd.equals("exit")){
           cmd = sc.nextLine();
       }
    }
    
    public static void parseCommand(String str) throws IOException {
        String[] args = str.split(" ");
        switch (args[0]) {
        case "new":
            newGame();
            break;
        case "load":
            System.out.println("What is the filename? (It's usually player.dat)");
            break;
        case "quit":
            System.out.println("Saving...");
            save(fileName);
            System.out.println("Saved!\nPress any key to quit");
            sc.next();
            System.exit(0);
            break;
        case "save":
            //save();
            break;
        default:
            System.out.println();
        }
    }
    
    public static void battle(Enemy en){
        if(en.isSpecial()){
             en.runScript();
        }
    }
}

class Enemy{
    private String name; 
    private int hp, atk, def;
    private boolean special;
    private ArrayList<String> script;
    private Scanner sc;
    
    public Enemy(String name){
        this.name = name;
        if(name.equals("Tutorial")){
            hp = 10;
            atk = 1;
            def = 0;
            special = true;
            script = new ArrayList<String>();
            script.add("Tutorial enemy found. Initiating battle.");
            script.add(dispStats());
        }        
    }
    
    private String dispStats(){
        return "HP: " + hp + " ATK: " + atk + " DEF: " + def;    
    }
    
    public boolean isSpecial(){
        return special;
    }
    
    public void runScript(){
        for(String str: script)
            System.out.println(str);
    }
}
