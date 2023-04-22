# CS 2910 - Assignment 3

You are taking over a job to build software to keeps track of the standings for the National Hockey League (NHL). Standings refers to how many games hockey teams have won or lost (or in the case of the NHL where their are no ties) also lost in overtime.

The person who was previously tasked with this job has quit, and the hockey season is already underway and so there are already some files left behind by this person that contain the standings up until this current point.

In the NHL a hockey game involves 2 teams and for each team there 3 possible results:

 1. A team may win the game in regulation (REG), which contributes 2 points towards the winning team's point total for the season and 0 points towards the losing team's point total.

 2. A team may win the game in overtime (OT), which contributes 2 points towards the winning team's point total and 1 point towards the losing team's point total.

 3. A team may win the game in a shoot-out (SO), which contributes 2 points towards the winning team's point total and 1 point towards the losing team's point total.

 As a point of terminology the NHL considers an OT loss to be any loss in either OT or SO.

 As an example consider that the New Jersey Devils (an NHL team) in the 2013-2014 NHL season had 35 wins, 29 losses (in regulation) and 18 over-time losses (either in shoot-out or over-time). Their point-total for that season is shown below:

|TEAM |  Wins | Losses | OT Losses | Points |
|---|---|---|---|---|
|New Jersey DEVILS|	35|	29|	18| 88 |

Every month the NHL will send you the results of recent NHL games (see the provided .csv files, which simulate this) and so your task will be to interpret the results and store them into the same binary standings files that your predecessor created (as well as print them out to screen).

Unfortunately your predecessor (who had this job before you) left on unpleasant terms and took all of their source code with them, leaving you only the binary file that contains the wins, losses and over-time losses for each team (up to the end of October - i.e., they loaded the game-oct.csv file into their binary format before they quit).

You have to write a program that is capable of updating this binary file as new NHL games occur.

## Binary File Format:

The binary file that contains the standings has the following format. For each of the X teams in the NHL they have an entry in the binary file `standings.bin` as follows:

|Field |  Size in Bytes | Description |
|---|---|---|
|TeamID |	4| 4 byte integer representing the ID of the team for this record |
| WINS | 8 | 8 byte long representing the the number of wins for this team |
| LOSSES | 8 | 8 byte long representing the number of losses for this team |
| OT-LOSSES | 8 | 8 byte long representing the number of over-time losses for this team|

The teams appear in the binary file in the same order as they appear in the nhl-game-data/team_ids.csv file.

For example: the New Jersey DEVILS data is stored in the first record in the binary file that your predecessor has created.

## CSV Files:

The TeamID can be reconciled from the `team_ids.csv` CSV file containing the associations of int TeamID : String TeamName

shown below a sample from `team_ids.csv` (in the `nhl-game-data` dir):

```csv
id,team_name
1,New Jersey DEVILS
2,NY Islanders ISLANDERS
3,NY Rangers RANGERS
4,Philadelphia FLYERS
...
```

From the above we see that the id for NEW JERSEY Devils is 1.

The games are given in other csv files all starting with games-X.csv where X is a month prefix from the NHL season.

For example: `game-oct.csv` contains all of the games played in October 2013 in the NHL.

(Those extreme NHL hockey fans among us will notice our database doesn't exactly align with the actual NHL schedule from 2013-2014 since some west coast timezone games that finished after midnight are listed on the next day - non-extreme NHL fans don't need to worry about this. Our assignment has all of the actual NHL games from 2013-2014 so the final results you obtain after loading all months should match:
https://www.nhl.com/standings/2013/league
)

The game csv files are all formatted as follows:
date (not important),away team id,home team id, away team goals (not important), home team goals (not important), result

Where most of the data fields are self explanatory except the result which is formatted as follows:
`[home, away] [win, lose] [REG, SO, OT]`

for example, a result equal to: `home win REG` implies the home team should get 2 points and the away team gets 0 points added to their cumulative point total. A result of `away win SO` implies the away team gets 2 points and the home team 1 points (for over-time loss), etc.

## Driver Program

### Driver Program Name

The Driver must be in a file called: `HockeyDriver.java`

As part of your assignment you should include a driver program. The driver program takes as input command line arguments of two possible types.

The first is of the form `-t X` where X is a valid teamID, for example 1 which is the New Jersey DEVILS. On receiving command line arguments `-t 1` your program should print the current standings for team X (in this case New Jerset DEVILS).

Your standings output for each team should appear all on one line and contain the teams name, wins, losses, OT-Losses and points total. In the example below we also show a header line to aid readability. The unit-tests tests however require the second line to contain the team name **exactly** as shown in the `team_ids.csv` and the integer values for wins, losses. ot losses and points (all on the same line).

Example:
```txt
         TEAM      WINS    LOSSES OT LOSSES    POINTS
Winnipeg JETS         5         7         2        12
```

The second possible command line argument is `-f filepath1 filepath2 ...`

That is a `-f` followed by some number of ordered filepaths to nhl-game-data csv files.

Upon receiving a `-f` your program should read the next argument which will be a game data file (e.g., nhl-game-data/games/nov.csv). Your program should read the game data and add into the `standing.bin` the new data. Now `standings.bin` contains update to date game results. Your program should do this for each of the arguments that follow `-f`.

For example: upon argument input: `-f nhl-game-data/nov.csv nhl-game-data/dec.csv` Your binary standings.bin file should contain 31 records (one for each nhl team) with each containing the combined original data (from your predecessor the october csv data) plus the november and december data.

If all of the csv data files (except oct) are passed to your program (after the -f flag) then your program should contain the final NHL standings for 2013-2014 and match the following:
https://www.nhl.com/standings/2013/league

After your program has combined all of the data files after the `-f` flag your program should print the standings to the screen. The standings should print in order of point totals (with teams with highest point totals at the top)

example:

```txt
          TEAM      WINS    LOSSES OT LOSSES    POINTS
 Boston BRUINS        54        19         9       117
 Anaheim DUCKS        54        20         8       116
Colorado AVALANCHE    52        22         8       112
St Louis BLUES        52        23         7       111
San Jose SHARKS       51        22         9       111
```

**IMPORTANT**

When printing the standings your program must match the above formatting. One team per line, the team name must match exactly as it is found in the team_ids.csv file and it must contain on the same line as the team name the integer values for wins, losses, OT losses and points.

You may print the above header as well if you want *marginally* nicer output.

Notice in the tests directory some sample unit-tests to help you out.

Deliverables:

A well designed program that achieves the above description. Note that for this assignment we ask you to use Java Streams as part of your solution. It is worth 1/10 points and is up to you where you use them.

**Due Date: Friday November 20th 11:59pm**

## Grading

| Gradable| Value | Comments |
| --- | --- | --- |
| Readability | 3 Points | proper variable names<br>proper class names <br>comments for every method and class<br>readable coding
| Code Design | 3 points | classes and methods are specific to a task<br>proper separation of duties<br>efficient code and algorithms<br>proper use of instance fields and local vars
| Streams | 1 Point | Have you used streams in various or obvious points of your solution
| Functionality | 3 Points | pass all the tests<br>robust to avoid crashes|

## Comments on Code Design  

A few additional comments on code design and separation of duties. Classes should be fairly specific to their task. For example the class that writes data to your binary file likely should not be aware of how to print standings out to the console.
