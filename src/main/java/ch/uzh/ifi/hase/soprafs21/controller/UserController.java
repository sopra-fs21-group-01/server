package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;
    private final GameService gameService;

    UserController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }


    // get the handcards of a single user
    @GetMapping("/users/{id}/hands")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public HandGetDto getUserHand(@PathVariable(value = "id") Long id) {
        // get User from repository
        User userOfID = userService.getUseryById(id);
        Hand handOfUser = gameService.getHandById(userOfID.getHandId());

        return DTOMapper.INSTANCE.convertEntitiyTOHandGetDTO(handOfUser);

    }


    // get one User
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUser(@PathVariable(value = "id") Long id) {
        // get User from repository
        User userOfID = userService.getUseryById(id);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userOfID);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUser(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO); // convert DTO to a user
        
        User loginUser = userService.login(userInput);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loginUser); // convert user to DTO
    }

    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO logout(@RequestBody UserTokenDTO userTokenDTO){
        User userInput = DTOMapper.INSTANCE.convertUserTokenDTOtoEntity(userTokenDTO);
        User loggedOutUser = userService.logout(userInput);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedOutUser);
    }

    // profile edit
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDTO editUser(@RequestBody UserEditDTO editDto) {
        User userEdit = DTOMapper.INSTANCE.convertUserToEditDTOtoEntity(editDto);
        User updatedUser = userService.editUser(userEdit);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
    }

}
