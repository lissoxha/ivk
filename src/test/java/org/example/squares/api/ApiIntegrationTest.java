package org.example.squares.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiIntegrationTest {

    @LocalServerPort
    int port;

    @Test
    void statusEndpointWorks() {
        WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        String body = "{\"size\":3,\"rows\":[\"...\",\"...\",\"...\"],\"turn\":\"WHITE\"}";
        client.post().uri("/api/status")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.result").isEqualTo("IN_PROGRESS");
    }

    @Test
    void nextMoveReturnsCoordinates() {
        WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        String body = "{\"size\":3,\"rows\":[\"...\",\"...\",\"...\"],\"turn\":\"BLACK\"}";
        client.post().uri("/api/next-move")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.x").exists()
                .jsonPath("$.y").exists();
    }
}
