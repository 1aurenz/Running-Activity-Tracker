import com.codecool.runningactivitytracker.RunningActivityTrackerApplication;
import com.codecool.runningactivitytracker.entity.UserEntity;
import com.codecool.runningactivitytracker.repository.TeamRepository;
import com.codecool.runningactivitytracker.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RunningActivityTrackerApplication.class})
public class TeamManagementTest {

private static boolean isInitialized = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @LocalServerPort
    private Integer port;

    private RequestSpecification given() {
        return RestAssured.given().baseUri("http://localhost").port(port).header("Content-Type", "application/json");
    }

    @BeforeEach
    public void initUsers() {
        if (!isInitialized) {
            userRepository.createUser(new UserEntity("my-user", "p@ssw0rd"));
            isInitialized = true;
        }
        teamRepository.clear();
    }

    @Test
    public void postTeamSuccess() {
        given().auth().preemptive().basic("my-user", "p@ssw0rd")
                .body("""
                          {
                              "teamName": "my-team"
                          }
                        """)
                .post("/team")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void postTeam_forbiddenForTeamAdmins() {
        given().auth().preemptive().basic("my-user", "p@ssw0rd")
                .body("""
                          {
                              "teamName": "my-team"
                          }
                        """)
                .post("/team")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.CREATED.value());

        given().auth().preemptive().basic("my-user", "p@ssw0rd")
                .body("""
                          {
                              "teamName": "my-team"
                          }
                        """)
                .post("/team")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
    @Test
    void addTeamMemberSuccess() {
        given().header("Authorization", "Bearer MY-TOKEN")
                .body("""
                      {
                        "username": "my-team-member",
                        "password": "p@ssw0rd"
                      }
                      """)
                .post("/internal/user")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.CREATED.value());

        given().auth().preemptive().basic("my-user", "p@ssw0rd")
                .body("""
                          {
                              "teamName": "my-team"
                          }
                        """)
                .post("/team")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.CREATED.value());

        given().auth().preemptive().basic("my-user", "p@ssw0rd")
                .body("""
                        {
                           "memberName": "my-team-member"
                        }
                        """)
                .put("/team/members")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value());
    }
}
