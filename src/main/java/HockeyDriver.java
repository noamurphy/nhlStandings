import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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
        if (args[0].equals("-t")){
            Scanner argScan = new Scanner(args[1]);
            Team team;
            if(argScan.hasNextInt()) {
                 team = new Team(argScan.nextInt());
                 System.out.println(team.getStanding());
            }
        }
        //if (args[1].equals("-f")){

        //}
        else{
            System.out.println("First argument must be -t or -f");
        }
    }
}
