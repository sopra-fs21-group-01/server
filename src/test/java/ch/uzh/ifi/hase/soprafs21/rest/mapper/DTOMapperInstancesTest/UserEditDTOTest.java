package ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperInstancesTest;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserEditDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;



public class UserEditDTOTest {

    @Mock
    private UserEditDTO testUserEditDTO;

    @Test
    public void test_getters_and_setter(){


        testUserEditDTO = new UserEditDTO();
        testUserEditDTO.setId(1L);
        testUserEditDTO.setUsername("Hans");
        testUserEditDTO.setPassword("123");


        assertSame(1L, testUserEditDTO.getId());
        assertSame("Hans", testUserEditDTO.getUsername());
        assertSame("123", testUserEditDTO.getPassword());
    }
}
