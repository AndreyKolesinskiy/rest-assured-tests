package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

public class ReqresTests {

    @Test
    void checkNameOfSingleUser() {
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.first_name", is("Janet"));
    }

    @Test
    void checkNameAndJobOfCreatedUser() {
        given()
                .contentType(JSON)
                .body("{\"name\": \"morpheus\",\"job\": \"leader\"}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void checkJobName() {
        String response = given()
                .contentType(JSON)
                .body("{\"name\": \"morpheus\",\"job\": \"zion resident\"}")
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .extract().response().asString();
        assertThat(response).contains("zion resident");
    }

    @Test
    void checkLoginNegative() {
        String response = given()
                .contentType(JSON)
                .body("{\"email\": \"peter@klaven\"}")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .extract().response().asString();
        System.out.println(response);
        assertThat(response).contains("{\"error\":\"Missing password\"}");
    }

    @Test
    void checkEmail() {
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("data[0].email", is("michael.lawson@reqres.in"));
    }
}
