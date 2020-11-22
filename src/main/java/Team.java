import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Team {
    private int id;
    private String name;

    public Team(int id){
        this.id = id;
    }

    private void getName(int id){
        List<String> teamName = new ArrayList<String>();
        try {
            teamName = Files.lines(Paths.get("nhl-game-data\\team_ids.csv"))
                    .skip(0)
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

    public String getStanding(){
        Standing standing = new Standing();
        try {
            standing.readStanding(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getName(id);
        return (name + " " + standing.toString());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    private class Standing{
        long wins, losses, lossesOT;
        public void readStanding(int id) throws IOException {
            try(RandomAccessFile raf = new RandomAccessFile("nhl-game-data\\standings.bin", "r")){
                int notesLength = raf.readInt();
                if(id < 1 || id > League.getNumTeams()){
                    throw new IllegalArgumentException("Team ID is not valid");
                }

                int idBytes = Integer.BYTES;
                int winsBytes = Long.BYTES;
                int lossesBytes = Long.BYTES;
                int lossesOTBytes = Long.BYTES;

                final int STANDING_BYTES = idBytes + winsBytes + lossesBytes + lossesOTBytes;
                long position = STANDING_BYTES*id;
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
        public Long getPoints(){
            Long points = (wins*League.getWinPoints()) + (lossesOT*League.getOtLossPoints());
            return points;
        }
        public String toString(){
            return (Long.toString(wins) + " " + Long.toString(losses) + " " +
                    Long.toString(lossesOT) + " " + Long.toString(getPoints()));
        }
    }
}
