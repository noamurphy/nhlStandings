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

                int idBytes = Integer.BYTES;
                int winsBytes = Long.BYTES;
                int lossesBytes = Long.BYTES;
                int lossesOTBytes = Long.BYTES;

                final int STANDING_BYTES = idBytes + winsBytes + lossesBytes + lossesOTBytes;
                long position = STANDING_BYTES*(id-1);
                raf.seek(position);

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
         * @param win whether to add a win
         * @param oT whether the game was a loss in OT
         */
        public void modifyStanding(int id, boolean win, boolean oT) {
            readStanding(id);
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

        public Long getPoints(){
            Long points = (wins*League.getWinPoints()) + (lossesOT*League.getOtLossPoints());
            return points;
        }

        public String toString(){
            return (wins + " " + losses + " " +
                    lossesOT + " " + getPoints());
        }
    }
}
