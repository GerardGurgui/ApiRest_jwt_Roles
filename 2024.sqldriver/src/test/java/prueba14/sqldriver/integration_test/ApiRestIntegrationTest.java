package prueba14.sqldriver.integration_test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.entities.Roles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static Player playerTest;
    private static Player playerTestUpdated;
    private static Player playerToDelete;
    private static Set<Roles> roles;
    private static Roles roleUser;
    private String token;
    private final String PLAYER_CONTROLLER_URL = "/players";
    private final String AUTH_CONTROLLER_URL = "/api/auth";
    private final String ADMIN_CONTROLLER_URL = "/admin";


    @BeforeAll
    public static void setUp() {

        playerTest = new Player();
        playerTest.setId(5L);
        playerTest.setUsername("userTest");
        playerTest.setPassword("userTest");
        playerTest.setEmail("userTest@test.com");

        playerTestUpdated = new Player();
        playerTestUpdated.setId(playerTest.getId());
        playerTestUpdated.setUsername("userTestUpdated");
        playerTestUpdated.setPassword("userTestUpdated");
        playerTestUpdated.setEmail("userTestUpdated@test.com");

        playerToDelete = new Player();
        playerToDelete.setId(6L);
        playerToDelete.setUsername("userToDelete");
        playerToDelete.setPassword("userToDelete");
        playerToDelete.setEmail("userToDelete@test.com");

        //Roles
        roles = new HashSet<>();
        roleUser = new Roles();

        roleUser.setName("USER");
        roles.add(roleUser);

        playerTest.setRoles(roles);
        playerTestUpdated.setRoles(roles);

    }


    private String getToken(String username, String password) throws Exception {

        String urlLoginPlayer = "/login";

        String playerJson = "{\n" +
                "    \"username\":\"" + username + "\",\n" +
                "    \"password\": \"" + password + "\"\n" +
                "}";

        MvcResult result = mockMvc.perform
                (MockMvcRequestBuilders.post(AUTH_CONTROLLER_URL + urlLoginPlayer)
                        .contentType("application/json")
                        .content(playerJson))
                .andExpect(status().isOk())
                .andReturn();

        // Extraer el token de la respuesta
        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }


    @Test
    @Order(1)
    public void registerPlayerTest() throws Exception {

        String urlRegisterPlayer = "/register";

        String playerJson = "{\n" +
                "    \"username\":\""+playerTest.getUsername()+"\",\n" +
                "    \"password\": \""+playerTest.getPassword()+"\",\n" +
                "    \"email\": \""+playerTest.getEmail()+"\"\n" +
                "}";

        mockMvc.perform
                (MockMvcRequestBuilders.post(AUTH_CONTROLLER_URL + urlRegisterPlayer)
                        .contentType("application/json")
                        .content(playerJson))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @Order(2)
    public void loginPlayerTest() throws Exception {

        String urlLoginPlayer = "/login";

        String playerJson = "{\n" +
                "    \"username\":\""+playerTest.getUsername()+"\",\n" +
                "    \"password\": \""+playerTest.getPassword()+"\"\n" +
                "}";

        MvcResult result = mockMvc.perform
                (MockMvcRequestBuilders.post(AUTH_CONTROLLER_URL + urlLoginPlayer)
                        .contentType("application/json")
                        .content(playerJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode playerResponse = objectMapper.readTree(responseBody);

        System.out.println(playerResponse);

        //comprobar que genera un token unico cuando se hace el login
        String token = getToken(playerTest.getUsername(), playerTest.getPassword());
        assertNotNull(token);
    }



    @Test
    @Order(3)
    public void testGetPlayerById() throws Exception {

        //buscamos el jugador con id 5 creado en el test anterior
        String urlGetPlayerById = "/get/getById/5";

        MvcResult result = mockMvc.perform
                (MockMvcRequestBuilders.get(PLAYER_CONTROLLER_URL + urlGetPlayerById))
                .andExpect(status().isFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode playerResponse = objectMapper.readTree(responseBody);

        System.out.println(playerResponse);
    }

    @Test
    @Order(4)
    public void updatePlayerTest() throws Exception {

        //obtenemos el token del jugador para evitar el error 401 unauthorized
        token = getToken(playerTest.getUsername(), playerTest.getPassword());

        String urlUpdatePlayer = "/updatePlayer/5";

        String playerJson = "{\n" +
                "    \"username\":\""+playerTestUpdated.getUsername()+"\",\n" +
                "    \"password\": \""+playerTestUpdated.getPassword()+"\",\n" +
                "    \"email\": \""+playerTestUpdated.getEmail()+"\"\n" +
                "}";

        MvcResult result = mockMvc.perform
                        (MockMvcRequestBuilders.put(PLAYER_CONTROLLER_URL + urlUpdatePlayer)
                                .contentType("application/json")
                                .content(playerJson)
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode playerResponse = objectMapper.readTree(responseBody);

        System.out.println(playerResponse);
    }

    @Test
    @Order(4)
    public void testGetAllPlayers() throws Exception {

        String urlGetAllPlayers = "/get/findAll";

        MvcResult result = mockMvc.perform
                (MockMvcRequestBuilders.get(PLAYER_CONTROLLER_URL + urlGetAllPlayers))
                .andExpect(status().isFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode playersResponse = objectMapper.readTree(responseBody);

        for (JsonNode player : playersResponse) {

            System.out.println(player);
        }

    }

    /////-----DADOS
    @Test
    @Order(5)
    public void throwDiceTest() throws Exception {

        //token del jugador actualizado
        token = getToken(playerTestUpdated.getUsername(), playerTestUpdated.getPassword());

        assertNull(playerTestUpdated.getThrowsDices());

        String urlPlayerThrowDice = "/dice/throw/5";

        mockMvc.perform
                (MockMvcRequestBuilders.post(PLAYER_CONTROLLER_URL + urlPlayerThrowDice)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String urlGetById = "/get/getById/5";

        MvcResult result = mockMvc.perform
                        (MockMvcRequestBuilders.get(PLAYER_CONTROLLER_URL + urlGetById)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode playerResponse = objectMapper.readTree(responseBody);

        assertFalse(playerResponse.get("throwsDices").isEmpty());
    }

    @Test
    @Order(6)
    public void deleteThrowsTest() throws Exception {

        token = getToken("player1", "password1");

        String urlPlayerThrowDice = "/dice/throw/1";

        mockMvc.perform
                    (MockMvcRequestBuilders.post(PLAYER_CONTROLLER_URL + urlPlayerThrowDice)
                    .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();


        String urlDeleteThrows = "/dice/deleteThrows/1";

        mockMvc.perform
                (MockMvcRequestBuilders.delete(PLAYER_CONTROLLER_URL + urlDeleteThrows)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isAccepted())
                .andReturn();

        String urlGetById = "/get/getById/1";

        MvcResult result = mockMvc.perform
                (MockMvcRequestBuilders.get(PLAYER_CONTROLLER_URL + urlGetById)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode playerResponse = objectMapper.readTree(responseBody);

        assertTrue(playerResponse.get("throwsDices").isEmpty());
    }

    ////----> ADMIN
    @Test
    @Order(7)
    public void addRoleTest() throws Exception {

        token = getToken("playerAdmin","playerAdmin");

        assertFalse(playerTestUpdated.getRoles().toString().contains("ADMIN"));

        //añadir el rol ADMIN del player1 de data.sql
        String urlModifyRol = "/1/ADMIN";

        mockMvc.perform
                (MockMvcRequestBuilders.post(ADMIN_CONTROLLER_URL + urlModifyRol)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();


        String urlGetById = "/get/getById/1";

        MvcResult result = mockMvc.perform
                (MockMvcRequestBuilders.get(PLAYER_CONTROLLER_URL + urlGetById)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isFound())
                .andReturn();

        //comprobar que el rol se ha añadido correctamente al mismo jugador
        String responseBody = result.getResponse().getContentAsString();
        JsonNode playerResponse = objectMapper.readTree(responseBody);


        assertTrue(playerResponse.get("roles").toString().contains("ADMIN"));
    }


    @Test
    @Order(8)
    public void deletePlayerTest() throws Exception {


        //REGISTRAR UN JUGADOR PARA ELIMINARLO
        String urlRegisterPlayer = "/register";

        String playerJson = "{\n" +
                "    \"username\":\""+playerToDelete.getUsername()+"\",\n" +
                "    \"password\": \""+playerToDelete.getPassword()+"\",\n" +
                "    \"email\": \""+playerToDelete.getEmail()+"\"\n" +
                "}";

        mockMvc.perform
                (MockMvcRequestBuilders.post(AUTH_CONTROLLER_URL + urlRegisterPlayer)
                .contentType("application/json")
                .content(playerJson))
                .andExpect(status().isOk())
                .andReturn();

        //token del admin con autorizacion para eliminar jugadores
        token = getToken("playerAdmin","playerAdmin");

        String urlDeletePlayer = "/deleteUser/6";

        mockMvc.perform
                (MockMvcRequestBuilders.delete(ADMIN_CONTROLLER_URL + urlDeletePlayer)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

//      comprobar que el jugador ha sido eliminado
        String getById = "/get/getById/6";

        mockMvc.perform
                (MockMvcRequestBuilders.get(PLAYER_CONTROLLER_URL + getById)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andReturn();

    }


}
