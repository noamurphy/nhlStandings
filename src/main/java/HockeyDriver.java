import java.io.File;

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
    }
}
