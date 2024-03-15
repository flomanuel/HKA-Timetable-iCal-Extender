/*
 * Copyright (c) 2024 - present Florian Sauer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.florianemanuelsauer.hka_timetable_ical_extender.controller;

import biweekly.Biweekly;
import biweekly.ICalendar;
import de.florianemanuelsauer.hka_timetable_ical_extender.service.IcalExtenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import static de.florianemanuelsauer.hka_timetable_ical_extender.controller.Controller.ICAL_PATH;

/**
 * Controller for accepting GET requests to parse the iCal file.
 */
@RestController
@RequestMapping(ICAL_PATH)
@RequiredArgsConstructor
@Slf4j
public class Controller {

    /**
     * Service for manipulating the given iCal file.
     */
    private final IcalExtenderService icalExtenderService;

    /**
     * MIME type for calendar files.
     */
    public static final String TEXT_CALENDAR = "text/calendar";
    public static final String ICAL_PATH = "/ical";

    /**
     * Pattern describing the format of a term at HKA university.
     */
    public static final String TERM_PATTERN = "[1-8]{1}.[a-fA-F]{1}";

    /**
     * Pattern describing the format of an academic course at HKA university.
     */
    public static final String ACADEMIC_COURSE_PATTERN = "[a-zA-Z]{1,4}";

    /**
     * Method for handling GET request with a given academic course and term.
     *
     * @param academicCourse The academic course defining the main calendar.
     * @param mainTerm       The main term defining the main calendar.
     * @param requestParams  Optional parameters defining modules that are to be omitted and additional modules from other terms.
     * @return The final iCal file as a string.
     */
    @GetMapping(
            path = "{academicCourse:" + ACADEMIC_COURSE_PATTERN + "}/{mainTerm:" + TERM_PATTERN + "}"
            , produces = TEXT_CALENDAR
    )
    public String getIcalFile(
            @PathVariable final String academicCourse,
            @PathVariable final String mainTerm,
            @RequestParam final MultiValueMap<String, String> requestParams
    ) {
        log.debug("getIcalFile academicCourse={} mainTerm={}, requestParams={}", academicCourse, mainTerm, requestParams);
        ICalendar mainCalendar = icalExtenderService.getExtendedIcalFile(academicCourse, mainTerm, requestParams);
        return Biweekly.write(mainCalendar).go();
    }

}
