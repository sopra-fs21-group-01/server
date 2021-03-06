package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;



@RestController
@Qualifier("gameController")
public class GameController {

    private final GameService gameService;
    private final LobbyService lobbyService;
    private final UserService userService;

    GameController(GameService gameService, LobbyService lobbyService, UserService userService) {
        this.gameService = gameService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    // post a gme
    @PostMapping("/game/{id}/kickOff")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createGame(@PathVariable(value = "id") Long id, @RequestBody GamePostDTO gamePostDTO) {
        // convert API Game to internal representation
        Game newGame = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        // creates and sets the list of userIDs
        newGame.setPlayerList(gameService.convertUserNamesToIds(id));

        Game createdGame = gameService.createGame(newGame);

        // set the lobby to "isInGame" and create a Game
        lobbyService.changeIsInGameStat(id);

        // return URL of where to find the User
        String url = "game/" + createdGame.getId() + "/kickOff";

        return url;
    }

    // delete the game by its ID
    @DeleteMapping("/game/{id}/deletion")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteGame(@PathVariable(value = "id") Long id) {
        //delete the game and say the lobby is not in a game anymore
        gameService.deleteGame(id);
        lobbyService.getLobbyById(id).setInGame(false);
   }

    // Put mapping when a player plays a card and this card is put on top of the cardstack
    @PutMapping("/game/{id}/playerTurn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playCard(@PathVariable(value = "id") Long id, @RequestBody PlayerMoveDTO playerMoveDTO) {

        PlayerMove playerMove = DTOMapper.INSTANCE.convertPlayerMoveDTOToEntity(playerMoveDTO);
        Game gameOfId = gameService.getGameById(id);
        User playerOfMove = userService.getUseryById(playerMove.getPlayerId());

        Card cardToPlay = new Card(playerMove.getColor(), playerMove.getValue());
        String cardName = cardToPlay.getCardName();

        Game updatedGame = gameService.playCard(gameOfId, playerOfMove, cardName);

        if(playerMoveDTO.getWishedColor() != null){
             gameService.wishColor(playerMoveDTO.getWishedColor(), gameOfId);
        }
        System.out.println("Set the the current color to" + playerMoveDTO.getWishedColor() + "In the controller");
    }

    // Put mapping when a player has finished his hand and is removed from the player pool
    @PutMapping("/game/{id}/wins")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playerFinishesHand(@PathVariable(value = "id") Long id, @RequestBody PlayerMoveDTO playerMoveDTO) {
        PlayerMove playerMove = DTOMapper.INSTANCE.convertPlayerMoveDTOToEntity(playerMoveDTO);
        Game gameOfId = gameService.getGameById(id);

        gameService.addWinner(gameOfId,playerMove.getPlayerId());

        gameService.removePlayerFromPlayerList(gameOfId, playerMove.getPlayerId());


        }

    @PutMapping("/game/{id}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playerLeavesGame(@PathVariable(value = "id") Long id, @RequestBody PlayerMoveDTO playerMoveDTO) {
        PlayerMove playerMove = DTOMapper.INSTANCE.convertPlayerMoveDTOToEntity(playerMoveDTO);
        Game gameOfId = gameService.getGameById(id);


        if (userService.getUseryById(playerMove.getPlayerId()).getUsername().equals(gameOfId.getHost())){

            gameService.removePlayerFromPlayerList(gameOfId, playerMove.getPlayerId());
            gameService.changeHost(gameOfId);


        } else if (gameOfId.getPlayerList().size()==1){

            gameService.removePlayerFromPlayerList(gameOfId, playerMove.getPlayerId());

        }

        else {
            long currentPlayer = gameOfId.getCurrentPlayerId();

            gameService.removePlayerFromPlayerList(gameOfId, playerMove.getPlayerId());

            //if persons leaves an is the currentPLayer: currentplayer +1
            if (playerMoveDTO.getPlayerId() == currentPlayer){
                gameOfId.setCurrentPlayerPlusOne();
            }
        }
    }

    @PutMapping("/game/{id}/drawCard")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void drawCard(@PathVariable(value = "id") Long id) {
        Game gameOfId = gameService.getGameById(id);

        gameService.cantPlayDrawCard(gameOfId);

    }

    @PutMapping("/game/{id}/sayUno")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void sayUno(@PathVariable(value = "id") Long id, @RequestBody PlayerMoveDTO playerMoveDTO) {

        PlayerMove playerMove = DTOMapper.INSTANCE.convertPlayerMoveDTOToEntity(playerMoveDTO);
        Game gameOfId = gameService.getGameById(id);
        Long playerId = playerMove.getPlayerId();

        gameService.sayUno(gameOfId, playerId);
        }

    // GetMapping for receiving the players
    @GetMapping("/game/{id}/kickOff")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGamePlayers(@PathVariable(value = "id") Long id) {

        Game gameOfId = gameService.getGameById(id);

        List<Long> opponentList = gameOfId.getPlayerList();
        List<Integer> opponentHandSize = new ArrayList<>();

        try {

        for (int i = 0; i < opponentList.size(); i++) {
            int handSize = gameService.getHandById(opponentList.get(i)).getHandSize();
            opponentHandSize.add(handSize);
        }

        System.out.println("opli:"+opponentList.size());
        System.out.println("oh"+opponentHandSize.size());

        List<String> userNameHandSize = new ArrayList<>();
        for (int j = 0; j < opponentList.size(); j++) {
            String usernameHand = opponentList.get(j).toString() + ","
                    + userService.getUseryById(opponentList.get(j)).getUsername()
                    + "," + opponentHandSize.get(j).toString()
                    + "," +gameService.getHandById(opponentList.get(j)).getUnoStatus();
            userNameHandSize.add(usernameHand);
        }

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(gameOfId);
        gameGetDTO.setOpponentListHands(userNameHandSize);

        return gameGetDTO;
    } catch (Exception e){ GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(gameOfId);
            gameGetDTO.setOpponentListHands(null);
            return gameGetDTO;}}

    // GetMapping for getting the current player of a running game, returns single Id
    @GetMapping("/game/{id}/kickOff/currentPlayerIds")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public long getCurrentPlayer(@PathVariable(value = "id") Long id) {

        return gameService.getGameById(id).getCurrentPlayerId();
    }
}
