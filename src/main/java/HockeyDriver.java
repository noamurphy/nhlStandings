import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Handle the assignment command line arguments
 */
public class HockeyDriver {

    /**
     * main program entry point
     * @param args command line arguments (see output below)
     */
    public static void main(String [] args) {
        //parse the args and take action
        if (args.length < 2) {
            System.out.println("Please provide 2 program arguments");
            System.out.println("Example: -t 11 (to display the current standings for team with ID = 11");
            System.out.println("Example: -f nhl-game-data" + File.separator + "games-nov.csv (to load new game results and display resulting standings");
            System.exit(0);
        }
        //Identify first arg
        switch(args[0]){
            case "-t":
                //Scan for team ID
                Scanner tArgScan = new Scanner(args[1]);
                Team team;
                if(tArgScan.hasNextInt()) {
                    team = new Team(tArgScan.nextInt());
                    System.out.println(team.getStanding());
                }
                else {
                    System.out.println("Invalid argument");
                }
                break;
            case "-f":
                //Process all filePath args
                for(int i = 1; i < args.length; i++){
                    Scanner fArgScan = new Scanner(args[i]);
                    String filePath = fArgScan.next();
                    if(Files.exists(Paths.get(filePath))){
                        League.updateStandings(filePath);
                    }
                }
                League.printStandings();
                break;
            default:
                System.out.println("First argument must be -t or -f");
                break;
        }
    }
}
