package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.DuplicatedUserException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        testUser.setEmail("test@uzh.ch");
        testUser.setPassword("Test1234");
        testUser.setStatus(UserStatus.OFFLINE);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);

    }

    // test for valid user creation
    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    // test invalid creation with already existing name
    @Test
    public void createUser_duplicateName_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(DuplicatedUserException.class, () -> userService.createUser(testUser));
    }

    // attempt to create user with all duplicats, throws exeption
    @Test
    public void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(DuplicatedUserException.class, () -> userService.createUser(testUser));
    }

    // find User by Id, succesfull
    @Test
    public void getuserById_succesfully_returnsUser(){

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));

        User userByID = userService.getUseryById(testUser.getId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());
        assertSame(userByID, testUser);
    }

    // find User by Id, unsuccessful, throws exception
    @Test
    public void getuserById_unSuccesfully_throwException(){

        when(userRepository.save(Mockito.any())).thenReturn(Optional.of(testUser));
        assertThrows(ResponseStatusException.class, () -> userService.getUseryById(testUser.getId()+1));
    }

    // edit user successfully
    @Test
    public void editUser_succesfully_returnsUser(){

        // given
        User testUser_edited = new User();
        testUser_edited.setId(1L);
        testUser_edited.setUsername("testUsername_edited");
        testUser_edited.setEmail("test@uzh.ch");
        testUser_edited.setPassword("Test1234_new");
        testUser_edited.setStatus(UserStatus.OFFLINE);

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));

        User editedUser = userService.editUser(testUser_edited);
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).flush();
        assertSame(editedUser.getId(), testUser_edited.getId());
        assertSame(editedUser.getUsername(), testUser_edited.getUsername());
        assertSame(editedUser.getEmail(), testUser_edited.getEmail());
        assertSame(editedUser.getPassword(), testUser_edited.getPassword());

    }

    // edit user but username is taken, throws exception
    @Test
    public void editUser_userNameTaken_throwException(){

        // given
        User testUser_edited = new User();
        testUser_edited.setId(1L);
        testUser_edited.setUsername("testUsername_edited");
        testUser_edited.setEmail("test@uzh.ch");
        testUser_edited.setPassword("Test1234_new");
        testUser_edited.setStatus(UserStatus.OFFLINE);

        // userropi will find an user that already has this name
        when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser_edited);
        assertThrows(ResponseStatusException.class, () -> userService.editUser(testUser_edited));
    }

    // edit user successfully, but no name given
    @Test
    public void editUser_noNameGiven_succesfully_returnsUser(){

        // given
        User testUser_edited = new User();
        testUser_edited.setId(1L);
        testUser_edited.setUsername(null);
        testUser_edited.setEmail("test@uzh.ch");
        testUser_edited.setPassword("Test1234_new");
        testUser_edited.setStatus(UserStatus.OFFLINE);

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));

        User editedUser = userService.editUser(testUser_edited);
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).flush();
        assertSame(editedUser.getId(), testUser_edited.getId());
        assertSame(editedUser.getEmail(), testUser_edited.getEmail());
        assertSame(editedUser.getPassword(), testUser_edited.getPassword());

        // the function takes the old name (testUser) in this case
        assertSame(editedUser.getUsername(), testUser.getUsername());
    }

    // edit user successfully, but no password given
    @Test
    public void editUser_noPaswordGiven_succesfully_returnsUser(){

        // given
        User testUser_edited = new User();
        testUser_edited.setId(1L);
        testUser_edited.setUsername("testUsername_edited");
        testUser_edited.setEmail("test@uzh.ch");
        testUser_edited.setPassword(null);
        testUser_edited.setStatus(UserStatus.OFFLINE);

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));

        User editedUser = userService.editUser(testUser_edited);
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).flush();
        assertSame(editedUser.getId(), testUser_edited.getId());
        assertSame(editedUser.getEmail(), testUser_edited.getEmail());
        assertSame(editedUser.getUsername(), testUser_edited.getUsername());

        // the function takes the old password (testUser) in this case
        assertSame(editedUser.getPassword(), testUser.getPassword());
    }

    // test got valid login
    @Test
    public void login_Test_valid(){

        userService.createUser(testUser);


        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));
       given(userService.getUser(Mockito.any())).willReturn(testUser);
       given(userService.getUser(Mockito.any())).willReturn(testUser);
       given(userService.getUser(Mockito.any())).willReturn(testUser);

        userService.login(testUser);

        assertSame(testUser.getStatus(), UserStatus.ONLINE);}

    // test for valid email input
    @Test
    public void isValidTest_valid(){
        String email = "I.am@perfectlyfi.ne";
        assertTrue(UserService.isValid(email));
    }

    // test for mail with invalid (no @)
    @Test
    public void isValidTest_Invalid_noAT(){
        String email = "IdonthaveAnAt.ch";
        assertFalse(UserService.isValid(email));
    }

    // test for mail with invalid (no domain)
    @Test
    public void isValidTest_Invalid_notValidEnding(){
        String email = "Ihavenotgoot@Ending.c";
        assertFalse(UserService.isValid(email));
    }


}
