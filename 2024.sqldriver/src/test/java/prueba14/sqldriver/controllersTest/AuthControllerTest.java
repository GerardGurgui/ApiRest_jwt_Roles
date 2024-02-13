package prueba14.sqldriver.controllersTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String
            CONTROLLER_BASE_URL = "/api/auth";

    @Test
    public void registerAndLoginTest() throws Exception {

        String playerJson = "{\"username\":\"testUser\",\"password\":\"testUser\",\"email\":\"testUser@test.com\"}";

        // Register
        mockMvc.perform(post(CONTROLLER_BASE_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Login
        mockMvc.perform(post(CONTROLLER_BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
