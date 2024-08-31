package de.florianemanuelsauer.hka_timetable_ical_extender.rest;

import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Tag("integration")
@Tag("rest")
@Tag("rest-get")
@DisplayName("REST-Api for GET-Requests")
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class IcalExtenderGetRestTest {

    public static final String URL = "http://localhost";
    public static final String FILE_PATH = "/";

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;

    private IcalExtenderGetRestTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    @DisplayName("Always successful")
    void alwaysSuccessful() {
        assertThat(true).isTrue();
    }

    @Test
    void testMissingPathAndQueryParamsResultsIn404() {
        var result = this.restTemplate.getForEntity(
                String.format("%s:%s%s", URL, port, FILE_PATH), String.class
        ).getStatusCode();

        assertThat(result).isEqualTo(NOT_FOUND);
    }

}
