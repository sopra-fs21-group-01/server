package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.DuplicatedUserException;
import ch.uzh.ifi.hase.soprafs21.exceptions.InvalidCredentialsException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);

        if(!isValid(newUser.getEmail())) {
            throw new InvalidCredentialsException("The provided email is invalid");
        }

        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User login(User userInput) {
        User userFound = checkUser(userInput);
        userFound.setStatus(UserStatus.ONLINE);
        return userFound;
    }

    private User checkUser(User userInput) {
        if (getUser(userInput.getUsername()) == null) { // looking if user exists
            throw new UserNotFoundException("Username does not exist!");
        }
        else {
            User user = getUser(userInput.getUsername()); //get user from userRepository
            if (!userInput.getPassword().equals(user.getPassword())){   // check password
                throw new InvalidCredentialsException("Invalid Password!");
            }
            else {
                return user;
            }
        }
    }

    public User getUser(String username) {

        if (this.userRepository.findByUsername(username) == null) {
            throw new UserNotFoundException("No User for this username found:"+username);
        }
        return this.userRepository.findByUsername(username);
    }

    public User getUseryById(Long id){
        User userFoundById = null;

        // check if there is a user with this ID and return it. If no lobby found, throw exception
        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isPresent()){
            userFoundById = optionalUser.get();

            log.debug("Found and returned User with ID: {}", id);
            return userFoundById;
        }
        if (userFoundById == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given ID was not found");}
        return userFoundById;
    }

    public User logout(User user){
        User foundbyToken = userRepository.findByToken(user.getToken());
        foundbyToken.setStatus(UserStatus.OFFLINE);
        return foundbyToken;
    }
    
    public User editUser(User user) {
        User usertoEdit = getUseryById(user.getId());
        if (usertoEdit != null) {

            // handles the case if a user wants to change their username to an existing username
            if (this.userRepository.findByUsername(user.getUsername()) != null) {
                throw new DuplicatedUserException("Username is already taken!");
            }
            if (user.getUsername() != null) {
                usertoEdit.setUsername(user.getUsername());
            }
            if (user.getPassword() != null) {
                usertoEdit.setPassword(user.getPassword());
            }
            userRepository.save(usertoEdit);
            userRepository.flush();
            return usertoEdit;
        }
        // status code 404 if user does not exist
        else {
            throw new UserNotFoundException("User does not exist");
        }   
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        User userByEmail = userRepository.findByEmail(userToBeCreated.getEmail());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new DuplicatedUserException(String.format(baseErrorMessage, "username", "is"));
        }
        else if (userByEmail != null) {
            throw new DuplicatedUserException("This email has already been used");
        }
    }

    // Function to check if email is valid
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                            "[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                            "A-Z]{2,7}$";
                              
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
