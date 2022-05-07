import core.BaseTest;
import org.testng.annotations.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CountriesQueryTest extends BaseTest{

    @Test
    public void shouldReturnAllCountries(){
        given()
                .body("{\"query\":\"query {\\r\\n  countries\\r\\n  {\\r\\n    code\\r\\n    name\\r\\n  }\\r\\n}\",\"variables\":{}}")
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("data.countries", instanceOf(List.class))
                .body("data.countries", hasSize(250));
    }

    @Test
    public void shouldReturnAllCountriesUsingUSDCurrencyThatAreNotUSA() {
        given()
                .body("{\"query\":\"query{\\r\\n    countries(filter:{\\r\\n        currency:{\\r\\n            in:[\\\"USD\\\"]\\r\\n        }\\r\\n        code:{\\r\\n            nin:[\\\"US\\\"]\\r\\n        }\\r\\n    }){\\r\\n        code\\r\\n        name\\r\\n        native\\r\\n        currency\\r\\n        capital\\r\\n    }\\r\\n}\",\"variables\":{}}")
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("data.countries", instanceOf(List.class))
                .body("data.countries", hasSize(15))
                .body("data.countries.name", not(hasItem("United States")));

    }

    @Test
    public void shouldReturnAllCountriesInSouthAmericaThatUsesEurCurrency(){
        given()
                .body("{\"query\":\"query{\\r\\n  countries(filter:{\\r\\n    continent:{\\r\\n      in:[\\\"SA\\\"]\\r\\n    }\\r\\n    currency:{\\r\\n    \\tin:[\\\"EUR\\\"]\\r\\n    }\\r\\n  })\\r\\n  {\\r\\n    code\\r\\n    name\\r\\n    currency\\r\\n  }\\r\\n}\",\"variables\":{}}")
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("data.countries", instanceOf(List.class))
                .body("data.countries", hasSize(1))
                .body("data.countries.code[0]", is("GF"))
                .body("data.countries.name[0]", is("French Guiana"))
                .body("data.countries.currency[0]", is("EUR"));
    }
}
