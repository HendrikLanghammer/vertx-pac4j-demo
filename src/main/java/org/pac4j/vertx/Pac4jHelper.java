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

import java.util.List;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * This helper class is usefull for sending messages on the event bus to the pac4j manager module.<br>
 * The three basic messages are:
 * <ul>
 * <li>redirect</li> 
 * <li>redirectUrls</li> 
 * <li>authenticate</li>
 * </ul>
 * 
 * @author Michael Remond
 * @since 1.0.0
 *
 */
public class Pac4jHelper {

    private Vertx vertx;

    private String address;

    public Pac4jHelper(Vertx vertx) {
        this(vertx, "vertx.pac4jmodule");
    }

    public Pac4jHelper(Vertx vertx, String address) {
        this.vertx = vertx;
        this.address = address;
    }

    /**
     * Send a redirect message to the pac4j manager. The response will contain the http information to send back to the user.
     * 
     * @param req
     * @param sessionAttributes
     * @param clientName
     * @param protectedResource
     * @param isAjax
     * @param handler
     */
    public void redirect(HttpServerRequest req, JsonObject sessionAttributes, String clientName,
            boolean protectedResource, Boolean isAjax, final Handler<Message<JsonObject>> handler) {

        JsonObject webContext = buildWebContext(req, sessionAttributes);
        JsonObject msg = new JsonObject().putString("clientName", clientName)
                .putBoolean("protected", protectedResource).putBoolean("isAjax", isAjax)
                .putObject("webContext", webContext);

        vertx.eventBus().send(address + ".redirect", msg, handler);

    }

    /**
     * Get the redirection urls for the given client names.
     * 
     * @param req
     * @param sessionAttributes
     * @param handler
     * @param clients
     */
    public void getRedirectUrls(HttpServerRequest req, JsonObject sessionAttributes,
            Handler<Message<JsonObject>> handler, String... clients) {
        JsonObject webContext = buildWebContext(req, sessionAttributes);
        JsonObject msg = new JsonObject().putObject("webContext", webContext).putArray("clients",
                new JsonArray(clients));

        vertx.eventBus().send(address + ".redirectUrls", msg, handler);
    }

    /**
     * Send an authenticate message to the pac4j manager. The response will contain the user profile if the authentication succeeds or the
     * http information to send back to the user otherwise.
     * 
     * @param req
     * @param sessionAttributes
     * @param handler
     */
    public void authenticate(HttpServerRequest req, JsonObject sessionAttributes,
            final Handler<Message<JsonObject>> handler) {
        JsonObject webContext = buildWebContext(req, sessionAttributes);
        JsonObject msg = new JsonObject().putObject("webContext", webContext);

        vertx.eventBus().send(address + ".authenticate", msg, handler);

    }

    /**
     * Generate the Vertx response based on the pac4j manager response.
     * 
     * @param response
     * @param event
     */
    public void sendResponse(HttpServerResponse response, JsonObject event) {
        response.setStatusCode(event.getInteger("code"));
        for (String name : event.getObject("headers").getFieldNames()) {
            response.putHeader(name, event.getObject("headers").getString(name));
        }
        String content = event.getString("content");
        response.end(content);
    }

    /**
     * Get the full url from the given request.
     * 
     * @param request
     * @return
     */
    public String getFullRequestURL(HttpServerRequest request) {
        String scheme = request.absoluteURI().getScheme();
        String hostHeader = request.headers().get(Constants.HOST_HEADER);
        return scheme + "://" + hostHeader + request.uri();
    }

    private JsonObject buildWebContext(HttpServerRequest request, JsonObject sessionAttributes) {

        final JsonObject parameters = new JsonObject();
        if (request.params().get(Constants.FORM_ATTRIBUTES) != null) {
            for (String name : request.formAttributes().names()) {
                List<String> values = request.formAttributes().getAll(name);
                JsonArray fixed = new JsonArray();
                for (String val : values) {
                    // FIX for Vertx
                    fixed.addString(val.replaceAll("\\s", "+"));
                }
                parameters.putArray(name, fixed);
            }
        }
        for (String name : request.params().names()) {
            parameters.putArray(name, new JsonArray(request.params().getAll(name).toArray()));
        }
        final JsonObject headers = new JsonObject();
        for (String name : request.headers().names()) {
            headers.putString(name, request.headers().get(name));
        }
        String method = request.method();
        String scheme = request.absoluteURI().getScheme();
        String hostHeader = headers.getString(Constants.HOST_HEADER);
        String serverName = hostHeader.split(":")[0];
        int serverPort = -1;
        String[] tab = hostHeader.split(":");
        if (tab.length > 1) {
            serverPort = Integer.parseInt(tab[1]);
        }
        if ("http".equals(scheme)) {
            serverPort = 80;
        } else {
            serverPort = 443;
        }
        String fullUrl = scheme + "://" + hostHeader + request.uri();

        return new JsonObject().putString("method", method).putString("serverName", serverName)
                .putNumber("serverPort", serverPort).putString("fullUrl", fullUrl).putString("scheme", scheme)
                .putObject("headers", headers).putObject("parameters", parameters)
                .putObject("sessionAttributes", sessionAttributes);

    }

    public Object getUserProfile(JsonObject response) {
        return response.getValue("userProfile");
    }

    public JsonObject getSessionAttributes(JsonObject response) {
        return response.getObject("sessionAttributes");
    }

}
