import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A couple of tests to ensure basic program functionality
 */
public class HockeyStandingsTests {

    private final ByteArrayOutputStream outData = new ByteArrayOutputStream();
    private final PrintStream origOutput = System.out;

    private final String BASE_DIR = "nhl-game-data";

    private final String MINUS_T = "-t";
    private final String MINUS_F = "-f";
    private final String GAMES_OCT = BASE_DIR + File.separator + "games-oct.csv";
    private final String GAMES_NOV = BASE_DIR + File.separator + "games-nov.csv";
    private final String GAMES_DEC = BASE_DIR + File.separator + "games-dec.csv";
    private final String GAMES_JAN = BASE_DIR + File.separator + "games-jan.csv";
    private final String GAMES_FEB = BASE_DIR + File.separator + "games-feb.csv";
    private final String GAMES_MAR = BASE_DIR + File.separator + "games-mar.csv";
    private final String GAMES_APR = BASE_DIR + File.separator + "games-apr.csv";

    private final String BACKUP = BASE_DIR + File.separator + "standings.backup";
    private final String ORIG = BASE_DIR + File.separator + "standings.bin";

    @Before
    public void init() {

        //the backup should be the original state of the file
        //load the backup before each test
        //because just like we are working for the weekend
        //we better start from the start
        Path backup = Paths.get(BACKUP);
        Path current = Paths.get(ORIG);
        try {
            Files.copy(backup, current, StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e) {
            e.printStackTrace();
        }
        System.setOut(new PrintStream(outData));
    }

    @After
    public void restore() {
        System.setOut(origOutput);
        System.out.println(outData.toString());
    }

    @Test
    public void testLoadNovThruApr() {
        String[] args = {MINUS_F, GAMES_NOV, GAMES_DEC, GAMES_JAN, GAMES_FEB, GAMES_MAR, GAMES_APR};
        HockeyDriver.main(args);
        String allOutput = outData.toString();

        Scanner scan = new Scanner(allOutput);
        int count = 0;

        while(scan.hasNextLine())  {
            String line = scan.nextLine();
            //pick 3 random team totals for the whole season
            //but never pick the Boston Bruins we don't need to revisit that
            if (line.contains("New Jersey DEVILS")) {
                assertTrue(line.contains("88"));
                count++;
            }
            else if(line.contains("Toronto MAPLE LEAFS")) {
                assertTrue(line.contains("84"));
                count++;
            }
            else if (line.contains("Calgary FLAMES")) {
                assertTrue(line.contains("77"));
                count++;
            }
        }
        assertEquals(3, count);
    }

    @Test
    public void printWinnipegOctStandings() {
        String[] args = {MINUS_T, "11"};
        HockeyDriver.main(args);
        String allOutput = outData.toString();
        Scanner scan = new Scanner(allOutput);
        int count = 0;
        while(scan.hasNextLine())  {
            String line = scan.nextLine();
            //Winnipeg accumulated 12 points in October 2013
            if (line.contains("Winnipeg JETS")) {
                count++;
                assertTrue(line.contains("12"));
            }
        }
        assertTrue(count > 0);

    }


}
