package com.swapnil.signicat;

import com.swapnil.signicat.dto.response.AuthResponseDTO;
import com.swapnil.signicat.dto.UserGroupDTO;
import com.swapnil.signicat.model.Subject;
import com.swapnil.signicat.model.UserGroup;
import com.swapnil.signicat.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

import static com.swapnil.signicat.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class AuthControllerTests {
    @Autowired
    private UserService userService;

    @LocalServerPort
    private Integer localServerPort;

    @BeforeEach
    public void init() {}

    @Test
    void requestWithoutJWT_ToUnsecuredAPIs_ShouldWork() {
        ResponseEntity<Subject> responseEntity = registerADemoUser();
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        // Clean up the user
        deleteDemoUser();
    }

    @Test
    void requestWithoutJWT_ToSecuredAPIs_ShouldFail() {
        String URL = String.format(GET_GROUP_API_URL, localServerPort);

        assertThrows(HttpClientErrorException.Forbidden.class,
                () -> new RestTemplate().getForEntity(URL, UserGroupDTO[].class));
    }

    @Test
    void requestWithJwt_ToSecuredAPIs_ShouldWork() {
        registerADemoUser();
        ResponseEntity<AuthResponseDTO> response = loginWithTheDemoUser();

        String URL = String.format(GET_GROUP_API_URL, localServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(response.getBody().getToken());

        ResponseEntity<UserGroup[]> responseEntity = new RestTemplate().exchange(URL, HttpMethod.GET, new HttpEntity<>(headers), UserGroup[].class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        // Clean up the user
        deleteDemoUser();
    }

    @Transactional(rollbackOn = Exception.class)
    ResponseEntity<AuthResponseDTO> loginWithTheDemoUser() {
        String LOGIN_URL = String.format(LOGIN_API_URL, localServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Subject> requestEntity = new HttpEntity<>(getDemoUser(), headers);
        return new RestTemplate().postForEntity(LOGIN_URL, requestEntity, AuthResponseDTO.class);
    }

    @Transactional(rollbackOn = Exception.class)
    ResponseEntity<Subject> registerADemoUser() {
        String REGISTER_URL = String.format(REGISTER_API_URL, localServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Subject> requestEntity = new HttpEntity<Subject>(getDemoUser(), headers);
        return new RestTemplate().postForEntity(REGISTER_URL, requestEntity, Subject.class);
    }

    @Transactional(rollbackOn = Exception.class)
    void deleteDemoUser() {
        userService.deleteUserByUsername(DEMO_USER_NAME);
    }
}
