import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * A team that can contain and handle standing data
 */
public class Team {
    private int id;
    private String name;

    /**
     * Team constructor
     * @param id ID for team of interest (as indicated in team_ids.csv)
     */
    public Team(int id){
        this.id = id;
    }

    /**
     * Reference team_ids.csv to get a team name from an ID.
     * @param id the team ID.
     */
    private void getName(int id){
        List<String> teamName = new ArrayList<String>();
        try {
            teamName = Files.lines(Paths.get("nhl-game-data\\team_ids.csv"))
                    .filter(line -> line.startsWith(Integer.toString(id)))
                    .collect(Collectors.toList());
            Scanner scan = new Scanner(teamName.get(0));
            scan.useDelimiter(",");
            scan.next();
            this.name = scan.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates a new standing object and fills it from binary standing file by calling readStanding method.
     * @return A string with team name and current standing data.
     */
    public String getStanding(){
        Standing standing = new Standing();
        standing.readStanding(id);
        this.getName(id);
        return (name + " " + standing.toString());
    }

    /**
     * creates an updated standing object and calls modify standing to update the binary standing file
     * @param win set as true to add a win
     * @param oT set as true to make a loss an OT loss
     */
    public void updateStanding(boolean win, boolean oT){
        Standing standing = new Standing();
        standing.modifyStanding(id, win, oT);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Internal class for handling input from and output to binary standings file
     */
    private class Standing{
        long wins, losses, lossesOT;

        /**
         * reads the standings for a team from a binary standings file
         * @param id the id of the team
         */
        public void readStanding(int id) {
            try(RandomAccessFile raf = new RandomAccessFile("nhl-game-data\\standings.bin", "r")){
                if(id < 1 || id > League.getNumTeams()){
                    throw new IllegalArgumentException("Team ID is not valid");
                }

                //Calculate the size of a standing in bytes
                int idBytes = Integer.BYTES;
                int winsBytes = Long.BYTES;
                int lossesBytes = Long.BYTES;
                int lossesOTBytes = Long.BYTES;

                //Move pointer to id specific standing position
                final int STANDING_BYTES = idBytes + winsBytes + lossesBytes + lossesOTBytes;
                long position = STANDING_BYTES*(id-1);
                raf.seek(position);

                //Set Standing variables to values on standings file
                if(raf.readInt() == id){
                    wins = raf.readLong();
                    losses = raf.readLong();
                    lossesOT = raf.readLong();
                }
                else{
                    System.out.println("Pointer is at incorrect position");
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        /**
         * Modifies the standings of a team in a binary standings file
         * @param id id of the team
         * @param win true adds a win
         * @param oT true makes a loss an OT loss
         */
        public void modifyStanding(int id, boolean win, boolean oT) {
            //Get current standing data
            readStanding(id);

            //Update standing data
            if (win) {
                this.wins++;
            }
            else {
                if (oT) {
                    this.lossesOT++;
                } else {
                    this.losses++;
                }
            }

            //Write modified standing data back to standings file (See readStanding method for breakdown)
            try (RandomAccessFile raf = new RandomAccessFile("nhl-game-data\\standings.bin", "rw")) {
                if(id < 1 || id > League.getNumTeams()){
                    throw new IllegalArgumentException("Team ID is not valid");
                }

                int idBytes = Integer.BYTES;
                int winsBytes = Long.BYTES;
                int lossesBytes = Long.BYTES;
                int lossesOTBytes = Long.BYTES;

                final int STANDING_BYTES = idBytes + winsBytes + lossesBytes + lossesOTBytes;
                long position = STANDING_BYTES*(id-1);
                raf.seek(position);

                if(raf.readInt() == id){
                    raf.writeLong(wins);
                    raf.writeLong(losses);
                    raf.writeLong(lossesOT);
                }
                else{
                    System.out.println("Pointer is at incorrect position");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Calculates a teams current points from their wins and losses in OT
         * @return the points of a teams current standing
         */
        public Long getPoints(){
            Long points = (wins*League.getWinPoints()) + (lossesOT*League.getOtLossPoints());
            return points;
        }

        /**
         * Converts a standings data to String
         * @return the standing data
         */
        public String toString(){
            return (wins + " " + losses + " " +
                    lossesOT + " " + getPoints());
        }
    }
}
