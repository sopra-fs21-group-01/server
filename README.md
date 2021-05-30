# UNO card game

## Introduction

In this project we wanted to replicate the UNO card game. UNO is a popular family game, who can be played from a young to an old age. It consists of 108 cards with cards of 4 different colors from the number 0 to 9 and some special cards which are explained as follows:
- **Reverse card**: This card reverses direction of play. Play to the left now passes to the right, and vice versa.
- **Skip card**: The next person in line to play after this card is played loses his/her turn and is "skipped".
- **Wild card**: When you play this card, you may change the color being played to any color (including the current color) to continue play. You may play a Wild card even if you have another playable card in hand.
- **Wild four card**: This card allows you to call the next color played and requires the next player to pick 4 cards from the DRAW pile.
### Game play
The Host of the Lobby first has to decide which gamemode is played and based upon that, each player becomes the selected amount of cards in the beginning of the game.
Now the player who begins has to look at the discard pile if there is for example a Green 7, you must play a Green card or any color with the number 7. Or, you may play any Wild card or a Wild Draw 4 card. If you don't have anything that matches, you must pick a card from the DRAW pile and the next player's turn begins. In case that you are playing your second last card, you have to push the UNO button until your next turn, if you don't press the button you receive a extra card. As soon as you have put down all cards xou win the game. 

## Technologies
For this Project we used the client/server archictecture. And in order to connect both components we used the standardized REST interface.



## High-level components
Our main components from the projects are the Game, Chat, User and Lobby components.
- **Game**: Is our main component where the whole game logic is implented. There exists a game component in the [client](https://github.com/sopra-fs21-group-01/client/blob/master/src/components/game/Game.js)
 where the game is displayed and from which RESTrequests are sent to the game component in the [server](https://github.com/sopra-fs21-group-01/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/controller/GameController.java)  where the game logic is implemented. 
- **Chat**: Important component to enhance the user experience of the game. It is also the component with the integrated external API. On Client side chat messages are shown and in the backend the messages are saved in the database
- **User**: This component is needed to save and identify the players so that lobbies have the right amount of players and e.g. to display the name of the players during the game. 
- **Lobby**: This component defines how players can join lobbies and which gamemode is selected. It is a crucial preprocessing part for the game component.

## Launch & Deployment
In case someone wants to distribute to this project there are different steps to do for the server and client
### Server
In order to run the server someone needs to have installed Gradle a
#### Build

```bash
./gradlew build
```

#### Run

```bash
./gradlew bootRun
```

#### Test

```bash
./gradlew test
```

## Illustrations
## Roadmap
Some features that could be integrated is to implement a music player which contains files either from a local storage or e.g. from spotify. Another one could be to create more complicated gamemodes as there exists many different rules that could be applied. Last most challenging feature to implement could be to integrate a voice chat, which may cause to extend the current architecture.

## Authors and Acknowledgement
Last we want to thank all the Authors who contributed to this great project and also our TA Jan Willi who helped us in situation where we needed help.
- Claudius Knecht (claudius.knecht@uzh.ch)
- Dean Heizmann (dean.heizmann@uzh.ch)
- Tony Bang (tony.bang@uzh.ch)
- Marco Heiniger (marco.heiniger@uzh.ch)
## License
This Project is licensed under the terms of the [MIT](https://choosealicense.com/licenses/mit/) license and is available for free



### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing

### Postman

-   We highly recommend to use [Postman](https://www.getpostman.com) in order to test your API Endpoints.

## Debugging

If something is not working and/or you don't know what is going on. We highly recommend that you use a debugger and step
through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command),
do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug"Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing

Have a look here: https://www.baeldung.com/spring-boot-testing
