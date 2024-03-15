/*
 * Copyright (c) 2023-2024 - present Florian Sauer
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

package de.florianemanuelsauer.hka_timetable_ical_extender;

import de.florianemanuelsauer.hka_timetable_ical_extender.repository.RaumzeitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;

public interface RaumzeitClientConfig {

    int RAUMZEIT_DEFAULT_PORT = 443;
    String RAUMZEIT_DEFAULT_SCHEMA = "https";
    String RAUMZEIT_DEFAULT_HOST = "raumzeit.hka-iwi.de";
    Logger LOGGER = LoggerFactory.getLogger(RaumzeitClientConfig.class);

    @Bean
    default UriComponentsBuilder uriComponentsBuilder() {
        final var host = RAUMZEIT_DEFAULT_HOST;
        final int port = RAUMZEIT_DEFAULT_PORT;

        LOGGER.debug("Raumzeit: host={}, port={}", host, port);
        return UriComponentsBuilder.newInstance()
                .scheme(RAUMZEIT_DEFAULT_SCHEMA)
                .host(host)
                .port(port);
    }

    @Bean
    default RaumzeitRepository raumzeitRepository(final UriComponentsBuilder uriBuilder,
                                                  final RestClient.Builder restClientBuilder,
                                                  final RestClientSsl ssl
    ) {
        final var baseUrl = uriComponentsBuilder().build().toUriString();
        LOGGER.info("REST-Client: baseUrl={}", baseUrl);
        final var restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        final var clientAdapter = RestClientAdapter.create(restClient);
        final var proxyFactory = HttpServiceProxyFactory.builderFor(clientAdapter).build();
        return proxyFactory.createClient(RaumzeitRepository.class);
    }

}
