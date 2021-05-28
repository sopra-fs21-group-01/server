package ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperInstancesTest;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserTokenDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;


public class UserTokenDTOTest {

    @Mock
    private UserTokenDTO userTokenDTO;

    @Test
    public void test_getters_and_setter(){
        userTokenDTO = new UserTokenDTO();
        userTokenDTO.setToken("newToken");

        assertSame("newToken", userTokenDTO.getToken());
    }
}
