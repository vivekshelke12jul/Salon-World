package com.vivek.userMicroservice.service;

import com.vivek.userMicroservice.payload.dto.KeycloakRole;
import com.vivek.userMicroservice.payload.dto.KeycloakUserDTO;
import com.vivek.userMicroservice.payload.dto.KeycloakUserinfo;
import com.vivek.userMicroservice.payload.dto.SignupDto;
import com.vivek.userMicroservice.payload.request.Credential;
import com.vivek.userMicroservice.payload.request.UserRequest;
import com.vivek.userMicroservice.payload.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakService {

    private static final String KEYCLOAK_BASE_URL="http://localhost:8080";

    private static final String KEYCLOAK_ADMIN_API = KEYCLOAK_BASE_URL+"/admin/realms/master/users";

    private static final String TOKEN_URL = KEYCLOAK_BASE_URL+"/realms/master/protocol/openid-connect/token";
    private static final String CLIENT_ID = "salon-world-client";
    private static final String CLIENT_SECRET = "Tr5NP9cOqB9BBoJNdQ0baEHjgfjXfJv0";
    private static final String GRANT_TYPE = "password";
    private static final String scope = "openid email profile";
    private static final String username = "admin";
    private static final String password = "admin";
    private static  final String clientId = "b01fc2de-fa75-4b9d-96f6-aa19d62648fa";

    @Autowired
    private RestTemplate restTemplate;

    public void createUser(SignupDto signupDto) throws Exception {

        String ACCESS_TOKEN = getAdminAccessToken(
                username,
                password,
                GRANT_TYPE,null).getAccessToken();

        System.out.println("access token: " + ACCESS_TOKEN);

        Credential credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signupDto.getPassword());

        UserRequest userRequest=new UserRequest();
        userRequest.setEmail(signupDto.getEmail());
        userRequest.setEnabled(true);
        userRequest.setUsername(signupDto.getUsername());
        userRequest.getCredentials().add(credential);



        RestTemplate restTemplate = new RestTemplate();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        // Create HTTP entity
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest, headers);


        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    KEYCLOAK_ADMIN_API,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            System.out.println("User created successfully!");
            KeycloakUserDTO user=fetchFirstUserByUsername(
                    signupDto.getUsername(),
                    ACCESS_TOKEN
            );
            KeycloakRole role=getRoleByName(
                    clientId,
                    ACCESS_TOKEN,
                    signupDto.getRole().toString()
            );
            List<KeycloakRole> roles=new ArrayList<>();
            roles.add(role);
            assignRoleToUser(
                    user.getId(),
                    clientId,
                    roles,
                    ACCESS_TOKEN
            );

        } catch (Exception e) {
            // Handle HTTP 4xx errors
            System.err.println("Client error: " + e.getMessage());
            throw new Exception(e.getMessage());

        }


    }

    public TokenResponse getAdminAccessToken(
            String username,
            String password,
            String grantType,
            String refreshToken
    ) throws Exception {


        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);
        requestBody.add("grant_type", grantType);
        requestBody.add("scope", scope);
        requestBody.add("username", username);
        requestBody.add("password", password);
        requestBody.add("refresh_token",refreshToken);

        // Create HTTP entity
        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(requestBody, headers);

        // Send POST request


        try {

            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    requestEntity,
                    TokenResponse.class
            );
            return response.getBody();
        } catch (Exception e) {

            System.err.println("Client error: " + e.getMessage());
            throw new Exception(e.getMessage());

        }

    }

    public KeycloakRole getRoleByName(
            String clientId,
            String token,
            String role
    ) throws Exception {


        // Endpoint URL
        String url=KEYCLOAK_BASE_URL+"/admin/realms/master/clients/{clientId}/roles/{role}";
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/json");

        // Create the HTTP entity
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {

            ResponseEntity<KeycloakRole> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    KeycloakRole.class,
                    clientId,
                    role
            );
            return response.getBody();
        } catch (Exception e) {

            System.err.println("Client error: " + e.getMessage());
            throw new Exception(e.getMessage());

        }
    }

    public KeycloakUserDTO fetchFirstUserByUsername(String username,String token) throws Exception {
        String url = KEYCLOAK_BASE_URL+"/admin/realms/master/users?username=" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Send the GET request
            ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    KeycloakUserDTO[].class
            );

            // Extract and return the first user object
            KeycloakUserDTO[] users = response.getBody();
            if (users != null && users.length > 0) {
                return users[0]; // Return the first user
            } else {
                throw new Exception("No users found for username: " + username);
            }

        } catch (Exception e) {

            throw new Exception("Failed to fetch user details: " + e.getMessage());
        }
    }


    public void assignRoleToUser(
            String userId,
            String clientId,
            List<KeycloakRole> roles,
            String token
    ) throws Exception {
        String url = KEYCLOAK_BASE_URL+"/admin/realms/master/users/" + userId +
                "/role-mappings/clients/" + clientId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);


        HttpEntity<List<KeycloakRole>> entity = new HttpEntity<>(roles, headers);

        try {

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );


            response.getStatusCode();

        } catch (Exception e) {

            throw new Exception("Failed to assign roles: " + e.getMessage());
        }
    }

    public KeycloakUserinfo fetchUserProfileByJwt(String token) throws Exception {
        System.out.println("keycloak profile token "+ token);
        String url = KEYCLOAK_BASE_URL+"/realms/master/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization",  token);


        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Send the GET request
            ResponseEntity<KeycloakUserinfo> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    KeycloakUserinfo.class
            );

            // Extract and return the first user object
            return response.getBody();

        } catch (Exception e) {
            System.out.println("Failed to fetch user details: " + e.getMessage());
            throw new Exception("Failed to fetch user details: " + e.getMessage());
        }
    }

}
