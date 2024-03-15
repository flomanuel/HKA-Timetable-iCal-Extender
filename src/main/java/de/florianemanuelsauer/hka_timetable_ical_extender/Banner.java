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

package de.florianemanuelsauer.hka_timetable_ical_extender;

import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Objects;

/**
 * Banner as String-constant for the server start.
 */
final class Banner {

    // http://patorjk.com/software/taag/#p=display&f=Slant&t=HKA%20Timetable%20iCal%20Extender
    private static final String FIGLET = """
              _    _ _  __             _______ _                _        _     _         _  _____      _    ______      _                 _
             | |  | | |/ /    /\\      |__   __(_)              | |      | |   | |       (_)/ ____|    | |  |  ____|    | |               | |
             | |__| | ' /    /  \\        | |   _ _ __ ___   ___| |_ __ _| |__ | | ___    _| |     __ _| |  | |__  __  _| |_ ___ _ __   __| | ___ _ __
             |  __  |  <    / /\\ \\       | |  | | '_ ` _ \\ / _ \\ __/ _` | '_ \\| |/ _ \\  | | |    / _` | |  |  __| \\ \\/ / __/ _ \\ '_ \\ / _` |/ _ \\ '__|
             | |  | | . \\  / ____ \\      | |  | | | | | | |  __/ || (_| | |_) | |  __/  | | |___| (_| | |  | |____ >  <| ||  __/ | | | (_| |  __/ |
             |_|  |_|_|\\_\\/_/    \\_\\     |_|  |_|_| |_| |_|\\___|\\__\\__,_|_.__/|_|\\___|  |_|\\_____\\__,_|_|  |______/_/\\_\\\\__\\___|_| |_|\\__,_|\\___|_|
            """;
    private static final String JAVA = Runtime.version() + "-" + System.getProperty("java.vendor");
    private static final String OS_VERSION = System.getProperty("os.name");
    private static final InetAddress LOCALHOST = getLocalhost();
    private static final long MEGABYTE = 1024L * 1024L;
    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static final String USERNAME = System.getProperty("user.name");

    /**
     * Banner für den Server-Start.
     */
    static final String TEXT = """

            $figlet
            (C) Florian Sauer
            Version             1
            Spring Boot         $springBoot
            Spring Framework    $spring
            Java                $java
            Operating system    $os
            Computer name       $computerName
            IP-Address          $ip
            Heap: Size          $heapSize MiB
            Heap: Free          $heapFree MiB
            Username            $username
            JVM Locale          $locale
            """
            .replace("$figlet", FIGLET)
            .replace("$springBoot", SpringBootVersion.getVersion())
            .replace("$spring", Objects.requireNonNull(SpringVersion.getVersion()))
            .replace("$java", JAVA)
            .replace("$os", OS_VERSION)
            .replace("$computerName", LOCALHOST.getHostName())
            .replace("$ip", LOCALHOST.getHostAddress())
            .replace("$heapSize", String.valueOf(RUNTIME.totalMemory() / MEGABYTE))
            .replace("$heapFree", String.valueOf(RUNTIME.freeMemory() / MEGABYTE))
            .replace("$username", USERNAME)
            .replace("$locale", Locale.getDefault().toString());

    @SuppressWarnings("ImplicitCallToSuper")
    private Banner() {
    }

    private static InetAddress getLocalhost() {
        try {
            return InetAddress.getLocalHost();
        } catch (final UnknownHostException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
