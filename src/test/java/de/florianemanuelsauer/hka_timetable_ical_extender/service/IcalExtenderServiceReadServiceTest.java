package de.florianemanuelsauer.hka_timetable_ical_extender.service;

import biweekly.component.VEvent;
import de.florianemanuelsauer.hka_timetable_ical_extender.repository.RaumzeitRepository;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static de.florianemanuelsauer.hka_timetable_ical_extender.service.IcalExtenderService.RECLAIM_DESCRIPTION_TRAVEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("unit")
@Tag("service-read")
@DisplayName("Application-core for reading. ")
@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
public class IcalExtenderServiceReadServiceTest {

    public static final String ACADEMIC_COURSE = "WIIB";
    public static final String MAIN_TERM = "4.B";
    public static final String ACADEMIC_COURSE_AND_TERM = String.format("%s.%s", ACADEMIC_COURSE, MAIN_TERM);
    public static final String UNEDITED_ICAL_FILE_WITHOUT_PROPERTY_DESCRIPTION = """
            BEGIN:VCALENDAR
            VERSION:2.0
            PRODID:-//hacksw/handcal//NONSGML v1.0//EN
            BEGIN:VEVENT
            UID:uid1@example.com
            ORGANIZER;CN=John Doe:MAILTO:john.doe@example.com
            DTSTART:19970714T170000Z
            DTEND:19970715T040000Z
            SUMMARY:Bastille Day Party
            GEO:48.85299;2.36885
            END:VEVENT
            END:VCALENDAR
            """;
    public static final String UNEDITED_ICAL_FILE_WITH_GIVEN_PROPERTY_DESCRIPTION = """
            BEGIN:VCALENDAR
            VERSION:2.0
            PRODID:-//hacksw/handcal//NONSGML v1.0//EN
            BEGIN:VEVENT
            UID:uid1@example.com
            ORGANIZER;CN=John Doe:MAILTO:john.doe@example.com
            DTSTART:19970714T170000Z
            DTEND:19970715T040000Z
            SUMMARY:Bastille Day Party
            DESCRIPTION:Bastille Day Party
            GEO:48.85299;2.36885
            END:VEVENT
            END:VCALENDAR
            """;

    @Mock
    private RaumzeitRepository repository;

    private IcalExtenderService icalExtenderService;

    @InjectSoftAssertions
    private SoftAssertions softly;

    @BeforeEach
    void beforeEach() {
        icalExtenderService = new IcalExtenderService(repository);
    }

    @Test
    @DisplayName("Always successful")
    void alwaysSuccessful() {
        assertThat(true).isTrue();
    }

    // todo: maybe consolidate both tests into one parametrized test
    @Test
    @DisplayName("Get iCal-file with new description")
    void testGetExtendedIcalFileFromEmptyDescription() {
        // arrange
        final var responseEntity = ResponseEntity.of(Optional.of(UNEDITED_ICAL_FILE_WITHOUT_PROPERTY_DESCRIPTION));
        when(repository.getIcalForCourseAndTerm(ACADEMIC_COURSE_AND_TERM)).thenReturn(responseEntity);
        final MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        // act
        final var result = icalExtenderService.getExtendedIcalFile(ACADEMIC_COURSE, MAIN_TERM, requestParams);

        // assert
        assertThat(result).isNotNull();
        result.getEvents().stream()
                .map(VEvent::getDescription)
                .map(description -> description.getValue())
                .forEach(
                        description -> softly.assertThat(description).isEqualToIgnoringCase(RECLAIM_DESCRIPTION_TRAVEL)
                );
    }

    @Test
    @DisplayName("Get iCal-file with extended description")
    void testGetExtendedIcalFileFromExistingDescription() {
        // arrange
        final var responseEntity = ResponseEntity.of(Optional.of(UNEDITED_ICAL_FILE_WITH_GIVEN_PROPERTY_DESCRIPTION));
        when(repository.getIcalForCourseAndTerm(ACADEMIC_COURSE_AND_TERM)).thenReturn(responseEntity);
        final MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        // act
        final var result = icalExtenderService.getExtendedIcalFile(ACADEMIC_COURSE, MAIN_TERM, requestParams);

        // assert
        assertThat(result).isNotNull();
        result.getEvents().stream()
                .map(VEvent::getDescription)
                .map(description -> description.getValue())
                .forEach(
                        description -> softly.assertThat(description).containsIgnoringCase(RECLAIM_DESCRIPTION_TRAVEL)
                );
    }
}
