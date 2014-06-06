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

/**
 * Pac4j constants for Vert.x binding.
 * 
 * @author Michael Remond
 * @since 1.0.0
 *
 */
public interface Constants {

    public static final String HOST_HEADER = "Host";

    public static final String CONTENT_TYPE_HEADER = "Content-Type";

    public final static String SESSION_ID = "pac4jSessionId";

    public final static String USER_PROFILE = "pac4jUserProfile";

    public final static String REQUESTED_URL = "pac4jRequestedUrl";

    public final static String FORM_ATTRIBUTES = "pac4jFormAttributes";

    public final static String HTML_CONTENT_TYPE = "text/html; charset=utf-8";

    public final static String FORM_URLENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";

    public final static String SEPARATOR = "$";

}
