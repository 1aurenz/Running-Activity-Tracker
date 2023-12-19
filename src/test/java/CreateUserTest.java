import com.codecool.runningactivitytracker.RunningActivityTrackerApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RunningActivityTrackerApplication.class})
class CreateUserTest {

    private static final String DEFAULT_REQUEST_BODY = "{\"username\":\"my-user\", \"password\":\"p\"}";

    @LocalServerPort
    private Integer port;

    private RequestSpecification given() {
        return RestAssured.given().baseUri("http://localhost").port(port);
    }

    @Test
    void createUserSuccess() {
        given()
                .header("Authorization", "Bearer MY-TOKEN")
                .contentType(ContentType.JSON)
                .body(DEFAULT_REQUEST_BODY)
                .post("/internal/user")
                .then()
                .statusCode(201);
    }

    @Test
    void createUserWrongToken() {
        given()
                .header("Authorization", "Bearer WRONG-TOKEN")
                .body(DEFAULT_REQUEST_BODY)
                .post("/internal/user")
                .then()
                .statusCode(401);
    }

    @Test
    void createUserMissingToken() {
        given().body(DEFAULT_REQUEST_BODY)
                .post("/internal/user")
                .then()
                .statusCode(401);
    }
}
