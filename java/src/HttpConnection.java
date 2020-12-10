// --== CS400 File Header Information ==--
// Name: Jerry Xu
// Email: mxu269@wisc.edu
// Team: IA
// Role: Front End 2
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP Connection helper
 */
public class HttpConnection {
    private final HttpExchange exchange;
    private boolean allowCORS = true;
    private boolean closed = false;

    public HttpConnection(HttpExchange exchange) {
        this.exchange = exchange;
        if (allowCORS) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        }
    }

    public String getMethod() { return exchange.getRequestMethod(); }

    public Headers getHeaders() {
        return exchange.getRequestHeaders();
    }

    public String getHeader(String key) {
        return getHeaders().getFirst(key);
    }

    public String getJWT() {
        return getHeader("Authorization").split(" ")[1];
    }

    public String getBody() throws IOException {
        return new String(exchange.getRequestBody().readAllBytes());
    }

    public String getQuery() {
        return exchange.getRequestURI().getQuery();
    }

    /**
     * Query parser implemented from https://stackoverflow.com/questions/11640025/how-to-obtain-the-query-string-in-a-get-with-java-httpserver-httpexchange
     * @param query String query
     * @return map of params
     */
    public Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public JSONObject getJSONParams() {
        return new JSONObject(queryToMap(getQuery()));
    }

    public JSONObject getJSONBody () throws IOException {
        return new JSONObject(getBody());
    }

    public void sendResponse(int code, JSONObject json) throws IOException {
        if (!closed) {
            String response = json.toString();
            exchange.sendResponseHeaders(code, 0);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
        closed = true;
    }

    public void sendResponse(int code, String response) throws IOException {
        if (!closed) {
            exchange.sendResponseHeaders(code, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
        closed = true;
    }


    public void require(String method) throws Exception {
        if (getMethod().equals("OPTIONS")) {
            //System.out.println("CORS OPTIONS");
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
            closed = true;
        }

        if (!method.equals(getMethod())) {
            throw new Exception("Bad request method");
        }
    }
}
