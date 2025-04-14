Intro Data Science
Lab 4: Unsupervised Collective Learning System with Tic Tac Toe

This lab is coded using Python in Lab4.ipynb. The file can be opened by typing 'jupyter notebook' in the terminal and then navigating to the file using the interface that appears in the browser.

There are 7 code cells in the Python notebook, which should be run in order.

Code cell 1: This cell imports all the libraries used in the program.

Code cell 2: This cell defines global variables that are referenced throughout the program. Many of them have their values changed or reassigned in different cells.
 - spaceMapping: a dictionary mapping the visual board spaces to the logical state space
 - state: the current state of the current game, represented as a 9-char String
 - turn: the number of moves that have been made by both sides in the current game
 - winStates: a dictionary of regex's representing states with 3-in-a-row for 'x' or 'o'
 - stateJournal: a log of encountered states and corresponding decisions by the program for the current game

Code cell 3: This cell defines all the functions for the program, which get called multiple times in other cells.
 - assignSpaces(): maps the visual board spaces to the logical state space by updating spaceMapping; called once per game when the first non-center move is made
 - showBoard(): prints the board with the current moves in an easily readable visual format; called each time the person has to make a move and at the end of the game
 - move(): depending on whose turn it is, prompts the player or the program to alter the game state accordingly; calls assignSpaces() when necessary; updates the stateJournal on the program's turn
 - decide(): the program uses the STM to randomly select its move according to the probabilities it has learned; except on the last turn, in which case it just fills in the single remaining empty space
 - learn(): the program updates the probabilities in the STM by applying Algedonic compensation with the appropriate beta value, depending on whether it won/tied or lost
 - branch(): takes a hypothetical game state and returns all states that could result one move in the future; used to build the STM in cell 4

Code cell 4: This cell builds the STM, one of few global variables not defined in cell 2. You can rerun this cell at any time to reset the program's learning by replacing its STM with a blank slate.
 - The STM is a list of dictionaries of dictionaries. It is split into a list of 4 so that it can be searched by what turn the game is on (between 0, 2, 4, and 6); there are so many possible states that narrowing the search in any way possible seems reasonable.
 - Each state the program might encounter is a key with its value being a dictionary of the possible resulting states as keys, with their values being the probability of each one being selected.
 - It is likely that some redundant or unnecessary states get built into the STM, though I took basic measures to keep it from branching on terminal states and states that get ruled out by the early-game state orientation with assignSpaces().
 - Building the STM actually doesn't take as long as I would have expected, but there is a print statement right at the end so you can be sure when it's done.
 * See screen_shot_cell4finished for what it looks like when this cell is finished executing.

Code cell 5: This cell defines the beta values for reward and punishment. It is separate from cell 2 so that you can easily isolate and modify the beta values as desired. Both values should be positive decimals between 0 and 1. These values are used when the program performs Algedonic compensation in learn().
 - betaReward: for reward (used when the program wins or ties the game)
 - betaPunish: for punishment (used when the person beats the program)

Code cell 6: This cell facilitates a single game of Tic Tac Toe between you and the program. You can run it repeatedly to play many times; the program will learn continuously over the course of many games.
 - See further instructions below.

Code cell 7: This cell simply prints the current STM. You can run it at any time with no consequence to the values being stored.
 - Two variables at the top of this cell can optionally be changed prior to running it. If you make showForJustOneState True and make specificState a certain state that the program has encountered or might encounter (it must have an equal number of x's and o's and not have only one empty space, since those penultimate states are not kept track of in the STM), then the cell will only print the STM for that particular state. This feature is recommended if you're only looking for a certain state, because if you print the whole thing, it's so big that the desired state can be very hard to find by scrolling through.
 * See screen_shot_cell7false for an example of what it looks like when you leave showForJustOneState False and let the cell print the entire STM; notice the tiny scroll bar on the right indicating that the output is massive.
 * See screen_shot_cell7true for an example of what it looks like when you make showForJustOneState True; this example has the empty state '---------' for specificState.

=======================================================
Playing Tic Tac Toe with the program:

Upon running cell 6, you will first be asked whether the program should print the String states on each turn (see screen_shot_cell6start). If your answer starts with a 'y' or 'Y', it will be interpreted as 'yes'.

The states do not take up very much space, but they slightly clutter the feed (see screen_shot_cell6with), so you might want to play without them (see screen_shot_cell6without).

The reason to play with the states displayed in this manner is so you can copy/paste them into cell 7 (specificState) after the game is over to see how the STM probabilities were affected for the exact states that were encountered. (Remember that states with only one blank space remaining are not stored in the STM because there is no decision involved in acting on those states.)

After you answer yes or no, the game starts.

The program always moves first as 'x'.

When it's your turn, you will be shown the current board, with the spaces labeled by letter, and you will be prompted to input the letter of the space you want to put your 'o' in. If your answer is not a valid move, you will be asked to try again until you input a valid move. As soon as you move, the program will have its turn, and then the board will be printed again and you will be prompted again.

This will continue until someone wins the game, or the board fills up (meaning a tie), at which point the program will print the final board, announce the winner, and terminate.

The final message printed will indicate that the program has learned (updated the STM) according to the result of the game, with a smily face :) if it was rewarded or a frowny face :( if it was punished.

=======================================================
How the program was created and how it works:


* Eliminating redundant states

The first problem I tackled for Lab 4 is how to represent equivalent states as one. The solution I implemented is not perfect (some equivalent state duplicates are still possible), but it eliminates a very large number of redundant states.

I differentiate the "physical" or visual game space, fixed as

  A B C
  D E F
  G H I

from the logical state space, represented as a String with indices 0-8. I then have the program assign the logical state indices to the visual space letters after the game has started. The "E" space will always map to index 4 (the center), but the first non-center move made by either side will determine how the logical state space will be "oriented" relative to the visual space. There are four orientations possible; the first non-center move will have its space mapped to index 0 if it's a corner, or 1 if it's a side.

Possible state space orientations, depending on letter of first non-center move:

 A or B:  C or F:  I or H:  G or D:
  0 1 2    6 3 0    8 7 6    2 5 8
  3 4 5    7 4 1    5 4 3    1 4 7
  6 7 8    8 5 2    2 1 0    0 3 6

This mapping is set as soon as the first non-center move is made. The mapping is then referenced for the rest of the game to determine how moves in letter spaces translate to the logical state and vice versa.

The result of this measure is that all logical states where the first two indices are both '-', besides '---------' and '----x----', are eliminated entirely.


* Detecting terminal states

Next, I created the simple interface for playing against the machine, and worked on how to detect when the game is over.

Before each move is made, the program first checks if the current state is a terminal state in two steps:

1. It checks for a "win" state, which is a terminal state where either side actually wins with 3 in a row. In winStates (cell 2), I define a set of 16 simple regular expressions, 8 for the 'x' win states and a matching 8 for the 'o' win states. These simply outline where the x's or o's would need to be in the logical state to make a 3-in-a-row, filling the other spaces with '.', meaning anything could go there. If the current game state matches one of these regex's, the game is over and the program takes note of who won.

2. If the current state is not a "win" state for either side, then it checks the turn counter, which increments with every turn (total turns = 'x' turns + 'o' turns). If the counter has reached 9, then it must be true that the board is full and no more moves can be made. If this happens and no win state was identified, the game is considered a tie. This outcome is treated as if the program won for the purpose of learning (it is rewarded the same amount as if it had won).


* Building the STM

Finally, I faced the problem of actually making the program learn using an STM as shown in class.

I worked out how the STM should be structured pretty easily, but initially made the mistake of assuming I could hardcode it. I approached this task very methodically, but after a couple of hours working on it I started to suspect that it would take much longer than I had assumed. When I finally paused to do the math, I realized there was no way I would finish within any reasonable amount of time, and anyway life is too short. So, I wrote branch() and cell 4 to do it automatically, which was both faster and honestly way more fun to do.

Once I had the STM, I wrote cell 7 to print out the STM so I could verify that it was correct, and then made the program decide its move using the STM (picking randomly between the options by probability using the 'cumulative' algorithm shown in class). When writing decide() in cell 3, I accounted for the case where the probabilities add up to less than 1 and the cumulative value never gets as high as the random value. I didn't think I would ever encounter this case, except perhaps by rounding error, but it's always a good idea to be cautious.


* Algedonic compensation and early testing

Finally, I implemented Algedonic compensation so the program could learn at the end of each game. At this point I added the stateJournal and made it get updated in all the right places, and spent some time overthinking how to parse it out in learn().

Once this was finally done, I tested my work, only to realize I had forgotten to set a beta value. Since the assignment asks for the possibility of using different beta values for reward and punishment, I went back and added two, and reworked learn() accordingly.

After working out a few other minor things, the program finally seemed to be working. I played several games against it, winning every time, and seeing the STM update, until something strange happened...


* The misunderstanding (and how it was discovered)

I started a new game and the very first move tripped the error detector I had set up in decide(). I looked at the STM for the empty state and realized that the probabilities were indeed much too small to add up to 1 as they should.

I reset the STM, went through a few more games, and carefully observed the STM values for the empty state after each game. Then, I reproduced what was happening-- which values were going into the Algedonic compensation equations-- on paper. Working the equations by hand, I came up with the same answers my program was producing.

This is when I sent that lengthy email asking about Algedonic compensation. I knew I probably misunderstood something about the equations, because it seems I had succeeded in implementing them exactly as I understood them. The email response I received indeed exposed a major misunderstanding I had about the values used in part of each equation.

In the code of learn(), the Algedonic compensation equations are implemented in the inner 'for' loop, using two local variables 'beta' and 'term' which are set depending on whether the program is being rewarded or punished. The 'term' variable is the part I had wrong in the earlier iteration of the code. Instead of using the probability of the decision made (STM[i][encountered][decided]), 'term' had been based on the probability of the possibility being looked at each loop (STM[i][encountered][possibility], defined inside the loop). This resulted in no problem upon the first learning experience, since all the probabilities were the same anyway, but would cause the problem I observed upon subsequent learning experiences involving the same encountered states.

I guess I had a hard time seeing the difference in the variables used to express this in the slides because the variables had so many repetitive letters in them that I automatically ignored them and assumed they all represented the same thing. That's my mistake. In any case I'm glad I was able to get help understanding this part of the algorithm.


* Finishing the program

After fixing my implementation of Algedonic compensation, I did a bit more testing to ensure the issue was fixed and no new ones arose. Then I worked on the part of the assignment that asks for a report on the performance of different beta values. This can be found in report.txt.

