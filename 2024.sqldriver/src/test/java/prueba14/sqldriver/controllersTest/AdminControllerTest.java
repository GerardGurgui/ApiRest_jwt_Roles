package prueba14.sqldriver.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String CONTROLLER_BASE_URL = "/admin";
    private final String CONTROLLER_AUTH_URL = "/api/auth";

    private String token;


    // Autenticar al usuario administrador para obtener el token y poder acceder a los endpoints
    private String authenticate(String username, String password) throws Exception {

        String JsonToken = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        MvcResult result = mockMvc.perform(post(CONTROLLER_AUTH_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToken))
                .andExpect(status().isOk())
                .andReturn();

        // Extraer el token de la respuesta
        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    @Transactional
    public void addRoleTest() throws Exception {

        token = authenticate("playerAdmin", "playerAdmin");

        //modificar el rol del usuario con id 2
        String url = CONTROLLER_BASE_URL + "/addRole/2/ADMIN";

        mockMvc.perform(post(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    public void onlyAdminCanAddRoleTest() throws Exception {

        token = authenticate("player1", "password1");

        String urlNotAuthorized = CONTROLLER_BASE_URL + "/addRole/2/ADMIN";

        mockMvc.perform(post(urlNotAuthorized)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @Transactional
    public void deleteRoleTest() throws Exception {

        token = authenticate("playerAdmin", "playerAdmin");

        //eliminar el rol admin del usuario con id 2
        String url = CONTROLLER_BASE_URL + "/deleteRole/2/ADMIN";

        mockMvc.perform(delete(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @Transactional
    public void deletePlayerTest() throws Exception {

        token = authenticate("playerAdmin", "playerAdmin");

        //eliminar el usuario con id 1
        String url = CONTROLLER_BASE_URL + "/deleteUser/1";

        mockMvc.perform(delete(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

    }


    @Test
    @Transactional
    public void onlyAdminCanDeletePlayerTest() throws Exception {

        token = authenticate("player2", "password2");

        String urlNotAuthorized = CONTROLLER_BASE_URL + "/deleteUser/3";

        mockMvc.perform(delete(urlNotAuthorized)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

}
