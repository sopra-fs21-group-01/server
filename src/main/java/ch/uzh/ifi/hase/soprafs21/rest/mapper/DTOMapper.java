package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);
    @Mapping(target  = "id", ignore = true)
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "token", ignore = true)
    @Mapping(target  = "status", ignore = true)
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "status", target = "status")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source  = "username", target = "username")
    @Mapping(target  = "email", ignore = true)
    @Mapping(source  = "password", target = "password")   
    @Mapping(target = "token", ignore = true)
    @Mapping(target  = "status", ignore = true)
    User convertUserToEditDTOtoEntity(UserEditDTO userEditDTO);


        // Mapper for when Lobby gets posted
    @Mapping(source = "host", target = "host")
    // @Mapping(source = "name", target = "name")
    // @Mapping(source = "password", target = "password")
    @Mapping(source = "playerList", target = "playerList")
    Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

    @Mapping(source = "host", target = "host")
   // @Mapping(source = "name", target = "name")
   // @Mapping(source = "password", target = "password")
    @Mapping(source = "playerList", target = "playerList")
    @Mapping(source = "id", target = "id")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

    @Mapping(target  = "id", ignore = true)
    @Mapping(target  = "username", ignore = true)
    @Mapping(target  = "email", ignore = true)
    @Mapping(target  = "password", ignore = true)
    @Mapping(source = "token", target = "token")
    @Mapping(target  = "status", ignore = true)
    User convertUserTokenDTOtoEntity(UserTokenDTO userTokenDTO);

    // Game mapping
    @Mapping(source = "host", target = "host")
    @Mapping(source = "id", target = "id")
    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

    @Mapping(source = "host", target = "host")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "cardStack", target = "cardStack")
   // @Mapping(source = "playerList", target = "playerList")
   //  @Mapping(source = "gamemode", target = "gamemode")
    GameGetDTO convertEntityToGameGetDTO(Game game);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "message", target = "message")
    Chat convertChatPostDTOtoEntity(ChatPostDTO chatPostDTO);

    @Mapping(source = "message", target = "message")
    @Mapping(source = "id", target = "id")
    ChatGetDTO convertEntityToChatGetDTO(Chat chat);

}
