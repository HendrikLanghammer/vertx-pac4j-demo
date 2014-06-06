/*
  Copyright 2014 - 2014 Michael Remond

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.vertx;

import org.pac4j.core.client.Clients;

/**
 * Holder for configuration values.
 * 
 * @author Michael Remond
 * @since 1.0.0
 *
 */
public final class Config {

    private final static String DEFAULT_URL = "/";

    private static String defaultSuccessUrl = DEFAULT_URL;

    private static String defaultLogoutUrl = DEFAULT_URL;

    // all the clients
    private static Clients clients;

    private static String errorPage401 = "authentication required";

    private static String errorPage403 = "forbidden";

    public static String getDefaultSuccessUrl() {
        return defaultSuccessUrl;
    }

    public static void setDefaultSuccessUrl(final String defaultSuccessUrl) {
        Config.defaultSuccessUrl = defaultSuccessUrl;
    }

    public static String getDefaultLogoutUrl() {
        return defaultLogoutUrl;
    }

    public static void setDefaultLogoutUrl(final String defaultLogoutUrl) {
        Config.defaultLogoutUrl = defaultLogoutUrl;
    }

    public static Clients getClients() {
        return clients;
    }

    public static void setClients(final Clients clients) {
        Config.clients = clients;
    }

    public static String getErrorPage401() {
        return errorPage401;
    }

    public static void setErrorPage401(final String errorPage401) {
        Config.errorPage401 = errorPage401;
    }

    public static String getErrorPage403() {
        return errorPage403;
    }

    public static void setErrorPage403(final String errorPage403) {
        Config.errorPage403 = errorPage403;
    }

}
