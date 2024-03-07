    package prueba14.sqldriver.controllersTest;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String CONTROLLER_BASE_URL = "/players";
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

    /////////GET
    @Test
    public void getPlayerByIdTest() throws Exception {

        String url = CONTROLLER_BASE_URL + "/get/getById/1";

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("player1"));

    }

    @Test
    public void getAllPlayersTest() throws Exception {

        String url = CONTROLLER_BASE_URL + "/get/findAll";

        mockMvc.perform(get(url))
                .andExpect(status().isFound())
                .andDo(print())
                .andExpect(jsonPath("$[0].username").value("player1"))
                .andExpect(jsonPath("$[1].username").value("player2"))
                .andExpect(jsonPath("$[2].username").value("player3"))
                .andExpect(jsonPath("$[3].username").value("playerAdmin"));
    }

    @Test
    public void getByUserNameTest() throws Exception {

        String url = CONTROLLER_BASE_URL + "/get/getByUsername/player2";

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username").value("player2"));
    }

    /////////PUT

    @Test
    @Transactional //evita que se realicen cambios en la base de datos
    public void updatePlayerTest() throws Exception {

        token = authenticate("playerAdmin", "playerAdmin");

        String url = CONTROLLER_BASE_URL + "/updatePlayer/4";

        String playerUpdated = "{\"username\":\"playerAdminUpdated\",\"password\":\"playerAdminUpdated\"}";

        mockMvc.perform(put(url)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerUpdated))
                .andExpect(status().isOk())
                .andDo(print());

        //COMPROBAR UN USUARIO NO PUEDE ACTUALIZAR A OTRO USUARIO

        // Obtener un nuevo token con el nombre de usuario actualizado
        token = authenticate("playerAdminUpdated", "playerAdminUpdated");

        String urlNotAuthorized = CONTROLLER_BASE_URL + "/updatePlayer/2";

        String playerNotAuthorizedToUpdate = "{\"username\":\"player2updated\",\"password\":\"player2updated\"}";

        mockMvc.perform(put(urlNotAuthorized)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerNotAuthorizedToUpdate))
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }




    /////////DADOS

    @Test
    @Transactional
    public void playerThrowDiceTest() throws Exception {

        token = authenticate("player1", "password1");

        String url = CONTROLLER_BASE_URL + "/dice/throw/1";

        mockMvc.perform(post(url)
                        .header("Authorization", "Bearer " + token)
                        .content("{\"dado1\":1, \"dado2\":2, \"resultado_tirada\":3}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());


        //comprobar que se ha registrado la tirada en el jugador

        String urlGetPlayerThrows = CONTROLLER_BASE_URL + "/dice/get/1";

        MvcResult result = mockMvc.perform(get(urlGetPlayerThrows)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        Assertions.assertFalse(responseContent.isEmpty());

        //comprobar que un usuario no puede lanzar los dados de otro usuario

        token = authenticate("player2", "password2");

        String urlNotAuthorized = CONTROLLER_BASE_URL + "/dice/throw/1";

        mockMvc.perform(post(urlNotAuthorized)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @Transactional
    public void deleteThrowsTest() throws Exception {

        token = authenticate("player1", "password1");

        String url = CONTROLLER_BASE_URL + "/dice/throw/1";

        //lanzar los dados
        mockMvc.perform(post(url)
                        .header("Authorization", "Bearer " + token)
                        .content("{\"dado1\":1, \"dado2\":2, \"resultado_tirada\":3}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        //eliminar las tiradas
        String urlToDelete = CONTROLLER_BASE_URL + "/dice/deleteThrows/1";

        MvcResult result = mockMvc.perform(delete(urlToDelete)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andReturn();

        //comprobar que se han eliminado las tiradas
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Throws deleted for player 1"));


        //comprobar que un usuario no puede eliminar las tiradas de otro usuario

        token = authenticate("player2", "password2");

        String urlNotAuthorized = CONTROLLER_BASE_URL + "/dice/deleteThrows/1";

        mockMvc.perform(delete(urlNotAuthorized)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}
