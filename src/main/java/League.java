import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.*;

/**
 * A class for variables and methods which affect every team in the league
 */
public class League {
    static final private int NUM_TEAMS = 30;
    static final private int WIN_POINTS = 2;
    static final private int OT_LOSS_POINTS = 1;

    /**
     * Updates standings.bin with a games data from a .csv file.
     * @param filePath a path to a file of monthly games data to be updated to the binary standings file
     */
    public static void updateStandings(String filePath) {
        try {
            //Add each line of the games file (each game) to a list of games
            List<String> lines = Files.lines(Paths.get(filePath))
                    .skip(1)
                    .collect(Collectors.toList());

            //Update each games data to the standings
            for (String line : lines) {

                //Separate the each line (game) into an array of game data
                String[] data = line.split(",");

                //Update relevant game data to standings of involved teams
                int id;
                boolean oT = false;
                if (data[5].substring(9).equals("OT")||data[5].substring(9).equals("SO")){
                    oT = true;
                }

                //if away wins
                if (data[5].startsWith("away")) {
                    Team away = new Team(Integer.parseInt(data[1]));
                    away.updateStanding(true, oT);

                    Team home = new Team(Integer.parseInt(data[2]));
                    home.updateStanding(false, oT);
                }

                //if home wins
                else{
                    Team home = new Team(Integer.parseInt(data[2]));
                    home.updateStanding(true, oT);

                    Team away = new Team(Integer.parseInt(data[1]));
                    away.updateStanding(false, oT);
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Print the standings for all teams in the league
     */
    public static void printStandings(){
        System.out.println("TEAM, WINS, LOSSES, OT LOSSES, POINTS");
        for(int i = 1; i <= NUM_TEAMS; i++){
            Team team = new Team(i);
            System.out.println(team.getStanding());
        }
    }

    public static int getWinPoints(){
        return WIN_POINTS;
    }

    public static int getOtLossPoints(){
        return OT_LOSS_POINTS;
    }

    public static int getNumTeams() {
        return NUM_TEAMS;
    }
}
