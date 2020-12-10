// --== CS400 File Header Information ==--
// Name: Jerry Xu
// Email: mxu269@wisc.edu
// Team: IA
// Role: Front End 2
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import io.jsonwebtoken.*;
import io.jsonwebtoken.gson.io.*;
import io.jsonwebtoken.io.Deserializer;
import io.jsonwebtoken.io.Serializer;
import io.jsonwebtoken.security.Keys;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.SecretKey;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//JSON library - https://github.com/stleary/JSON-java
//JWT library - https://github.com/jwtk/jjwt

/**
 * HTTP Server
 */
public class Server {

    public static final int SOCKET = 8000;
    public static final long ttlMillis = 604800000l; //7days
    public static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final Serializer<Map<String,?>> serializer = new GsonSerializer();
    public static final Deserializer<Map<String,?>> deserializer = new GsonDeserializer();
    private static BackEnd backend;

    public void start() {

        try {
            backend = new BackEnd();
            HttpServer server = HttpServer.create(new InetSocketAddress(SOCKET), 0);
            server.createContext("/test", new TestHandler());
            server.createContext("/api/auth/signin", new POSTSigninHandler());
            server.createContext("/api/auth/signup", new POSTSignupHandler());
            server.createContext("/api/user/new-transaction", new POSTNewTransactionHandler());
            server.createContext("/api/user/delete-transaction", new POSTDeleteTransactionHandler());
            server.createContext("/api/user/transaction", new GETTransactionHandler());
            server.createContext("/api/user/search-transaction", new GETSearchTransactionsHandler());
            server.createContext("/api/user/week-summary", new GET7DayAmountHandler());
            server.createContext("/api/user/balance", new GETBalanceHandler());
            server.createContext("/api/user/recent-transaction", new GETRecentTransactionsHandler());
            server.start();

            System.out.println("java.src.Server listening on: " + SOCKET);
            System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate JWS
     * @param subject username
     * @param ttlMillis time to live
     * @return jwt in string
     */
    public static String createJWS(String subject, long ttlMillis) {

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + ttlMillis;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);


        JwtBuilder builder = Jwts.builder()
                .serializeToJsonWith(serializer)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key);

        String jws = builder.compact();


        System.out.println(jws);
        return jws;
    }

    /**
     * decode and verify jws
     * @param jws the jws to verify
     * @return claims
     */
    public static Jws<Claims> decodeJWS(String jws) {
        try {

            Jws<Claims> claims = Jwts.parserBuilder()
                    .deserializeJsonWith(deserializer)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jws);
            return claims;

        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * Test handler
     */
    static class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println(exchange.getRequestMethod());
            System.out.println(exchange.getRequestHeaders().entrySet());
            System.out.println(exchange.getRequestHeaders().getFirst("Authorization"));
            System.out.println(new String(exchange.getRequestBody().readAllBytes()));

            }
    }

    /**
     * POST /signin handler
     */
    static class POSTSigninHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            HttpConnection conn = new HttpConnection(exchange);

            try {
                conn.require("POST");
                JSONObject body = conn.getJSONBody();
                String username = body.getString("username");
                String password = body.getString("password");
                System.out.println("request to login: " + username + " " + password);

                //boolean success = username.equals("jappleseed") && password.equals("password");
                boolean success = backend.validate(username, password);
                if (success) {
                    String jwt = createJWS(username, ttlMillis);
                    String data = new JSONObject()
                            .put("name", backend.getUser(username).getName())
                            .put ("access_token", jwt)
                            .toString();
                    conn.sendResponse(200, data);
                } else {
                    conn.sendResponse(401, "username/password incorrect");
                }
            } catch (Exception e) {
                conn.sendResponse(400, e.getLocalizedMessage());
            }

        }
    }

    /**
     * POST /signup handler
     */
    static class POSTSignupHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            HttpConnection conn = new HttpConnection(exchange);

            try {
                conn.require("POST");
                JSONObject body = conn.getJSONBody();
                String name = body.getString("name");
                String username = body.getString("username");
                String password = body.getString("password");
                System.out.println("request to signup: " + name + " " + username + " " + password);

                boolean success = backend.addUser(username, password, name);
                success = success && backend.forceSave();
                if (success) {
                    String jwt = createJWS(username, ttlMillis);
                    String data = new JSONObject()
                            .put("name", backend.getUser(username).getName())
                            .put ("access_token", jwt)
                            .toString();
                    conn.sendResponse(200, data);
                } else {
                    conn.sendResponse(401, "cannot create account");
                }
            } catch (Exception e) {
                System.out.println(e);
                conn.sendResponse(400, e.getLocalizedMessage());
            }

        }
    }

    /**
     * POST /new-transaction handler
     */
    static class POSTNewTransactionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            HttpConnection conn = new HttpConnection(exchange);

            try {
                conn.require("POST");
                Jws<Claims> verifiedJWT = decodeJWS(conn.getJWT());

                if (verifiedJWT == null) {
                    String data = new JSONObject()
                            .put ("msg", "unable to add transaction, user authentication failed")
                            .toString();
                    conn.sendResponse(401, data);
                }

                String username = verifiedJWT.getBody().getSubject();

                JSONObject body = conn.getJSONBody();
                String merchantName = body.getString("merchantName");
                String location = body.getString("location");
                String amount = body.getString("amount");
                String status = body.getString("status");
                String date = body.getString("date");
                String type = body.getString("type");
                String description = body.getString("description");

                System.out.println("request to add new transaction: " + body);

                LocalDateTime datetime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
                BigDecimal amountBD = new BigDecimal(amount);
                DataWranglerInterface.PaymentMethods method;
                switch (type) {
                    case "cash":
                        method = DataWranglerInterface.PaymentMethods.CASH_OUT;
                        break;
                    case "credit":
                        method = DataWranglerInterface.PaymentMethods.DEBIT;
                        break;
                    case "payment":
                        method = DataWranglerInterface.PaymentMethods.PAYMENT;
                        break;
                    case "transfer":
                        method = DataWranglerInterface.PaymentMethods.TRANSFER;
                        break;
                    default:
                        method = DataWranglerInterface.PaymentMethods.CASH_OUT;
                }

                Transaction t = new Transaction(status, username, datetime, UUID.randomUUID().toString(), description, amountBD, merchantName, method, location);

                boolean success = backend.insertTransaction(username, t);
                success = success && backend.forceSave();
                if (success) {
                    String data = new JSONObject()
                            .put ("msg", "transaction successfully added")
                            .toString();
                    conn.sendResponse(200, data);
                } else {
                    String data = new JSONObject()
                            .put ("msg", "unable to add transaction")
                            .toString();
                    conn.sendResponse(500, data);
                }
            } catch (IllegalArgumentException e){
                String data = new JSONObject()
                        .put ("msg", e.getLocalizedMessage())
                        .toString();
                conn.sendResponse(200, data);
            } catch (NoSuchElementException e){
                String data = new JSONObject()
                        .put ("msg", e.getLocalizedMessage())
                        .toString();
                conn.sendResponse(200, data);
            }
            catch (Exception e) {
                String data = new JSONObject()
                        .put ("msg", "unable to add transaction")
                        .toString();
                conn.sendResponse(400, data);
            }

        }
    }

    /**
     * GET transaction?id= handler
     */
    static class GETTransactionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            HttpConnection conn = new HttpConnection(exchange);

            System.out.println("trying to get trans");

            try {
                conn.require("GET");
                String jwt = conn.getJWT();
                Jws<Claims> verifiedJWT = decodeJWS(jwt);

                if (verifiedJWT == null) {
                    String data = new JSONObject()
                            .put ("msg", "user authentication failed")
                            .toString();
                    conn.sendResponse(401, data);
                }

                String username = verifiedJWT.getBody().getSubject();

                JSONObject params = conn.getJSONParams();
                String id = params.getString("id");

                System.out.println("request to get transaction: " + id);

                Transaction t = backend.getTransaction(username, null, id);

                DataWranglerInterface.PaymentMethods method = t.getPaymentMethod();
                String type;
                switch (method) {
                    case CASH_OUT:
                        type = "cash";
                        break;
                    case PAYMENT:
                        type = "payment";
                        break;
                    case TRANSFER:
                        type = "transfer";
                        break;
                    case DEBIT:
                        type = "credit";
                        break;
                    default:
                        type = "cash";
                        break;
                }

                String data = new JSONObject()
                        .put("id", t.getTransactionID())
                        .put("merchantName", t.getMerchantName())
                        .put("location", t.getLocationOfSpending())
                        .put("amount", t.getAmount())
                        .put("status", t.getTransactionStatus())
                        .put("date", t.getDateTime().format(DateTimeFormatter.ISO_DATE))
                        .put("type", type)
                        .put("description", t.getTransactionDescription())
                        .toString();
                conn.sendResponse(200, data);

            } catch (Exception e) {
                String data = new JSONObject()
                        .put ("msg", "cannot get transaction")
                        .toString();
                conn.sendResponse(400, data);
            }

        }
    }

    /**
     * GET /transactions?key= handler
     */
    static class GETSearchTransactionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            HttpConnection conn = new HttpConnection(exchange);

            System.out.println("trying to search transactions");

            try {
                conn.require("GET");
                String jwt = conn.getJWT();
                Jws<Claims> verifiedJWT = decodeJWS(jwt);

                if (verifiedJWT == null) {
                    String data = new JSONObject()
                            .put ("msg", "user authentication failed")
                            .toString();
                    conn.sendResponse(401, data);
                }

                String username = verifiedJWT.getBody().getSubject();

                JSONObject params = conn.getJSONParams();
                String keywords = params.getString("key");

                System.out.println("request to search transactions: " + keywords);

                Transaction[] transactions = backend.search(username, keywords, (Filter) null);

                JSONArray transactionsJSON = new JSONArray();

                for (Transaction t: transactions){

                    String type;
                    switch (t.getPaymentMethod()) {
                        case CASH_OUT:
                            type = "cash";
                            break;
                        case PAYMENT:
                            type = "payment";
                            break;
                        case TRANSFER:
                            type = "transfer";
                            break;
                        case DEBIT:
                            type = "credit";
                            break;
                        default:
                            type = "cash";
                            break;
                    }
                    JSONObject tJSON = new JSONObject()
                            .put("id", t.getTransactionID())
                            .put("merchantName", t.getMerchantName())
                            .put("location", t.getLocationOfSpending())
                            .put("amount", t.getAmount())
                            .put("status", t.getTransactionStatus())
                            .put("date", t.getDateTime().format(DateTimeFormatter.ISO_DATE))
                            .put("type", type)
                            .put("description", t.getTransactionDescription());
                    transactionsJSON.put(tJSON);
                }
                String result = new JSONObject()
                        .put("transactions", transactionsJSON)
                        .toString();
                conn.sendResponse(200, result);

            } catch (Exception e) {
                String data = new JSONObject()
                        .put ("msg", "cannot get transaction")
                        .toString();
                conn.sendResponse(400, data);
            }

        }
    }

    /**
     * POST /delete-transaction?id= handler
     */
    static class POSTDeleteTransactionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            HttpConnection conn = new HttpConnection(exchange);

            try {
                conn.require("POST");
                Jws<Claims> verifiedJWT = decodeJWS(conn.getJWT());

                if (verifiedJWT == null) {
                    String data = new JSONObject()
                            .put ("msg", "unable to add transaction, user authentication failed")
                            .toString();
                    conn.sendResponse(401, data);
                }

                String username = verifiedJWT.getBody().getSubject();

                JSONObject body = conn.getJSONBody();
                String id = body.getString("id");

                System.out.println("request to delete transaction: " + id);

                boolean success = backend.removeTransaction(username, id);
                success = success && backend.forceSave();
                if (success) {
                    String data = new JSONObject()
                            .put ("msg", "transaction successfully deleted")
                            .toString();
                    conn.sendResponse(200, data);
                } else {
                    String data = new JSONObject()
                            .put ("msg", "unable to delete transaction")
                            .toString();
                    conn.sendResponse(500, data);
                }
            } catch (Exception e) {
                String data = new JSONObject()
                        .put ("msg", "unable to delete transaction")
                        .toString();
                conn.sendResponse(400, data);
            }

        }
    }

    /**
     * GET /week-summary handler
     */
    static class GET7DayAmountHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            HttpConnection conn = new HttpConnection(exchange);

            System.out.println("trying to get weekly amount summary");

            try {
                conn.require("GET");
                String jwt = conn.getJWT();
                Jws<Claims> verifiedJWT = decodeJWS(jwt);

                if (verifiedJWT == null) {
                    String data = new JSONObject()
                            .put ("msg", "user authentication failed")
                            .toString();
                    conn.sendResponse(401, data);
                }

                String username = verifiedJWT.getBody().getSubject();

                System.out.println("request to get last 7 days amount: " + username);

                BigDecimal[] nums = backend.getDailySumPerWeek(username);
                System.out.println(nums);
//                int[] nums = {1,2,3,4,5,6,7};

                JSONArray numsJSON = new JSONArray(nums);

                String result = new JSONObject()
                        .put("amounts", numsJSON)
                        .toString();
                conn.sendResponse(200, result);

            } catch (Exception e) {
                String data = new JSONObject()
                        .put ("msg", "cannot get weekly summary")
                        .toString();
                conn.sendResponse(400, data);
            }

        }
    }

    /**
     * GET /balance handler
     */
    static class GETBalanceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            HttpConnection conn = new HttpConnection(exchange);

            System.out.println("trying to get balance");

            try {
                conn.require("GET");
                String jwt = conn.getJWT();
                Jws<Claims> verifiedJWT = decodeJWS(jwt);

                if (verifiedJWT == null) {
                    String data = new JSONObject()
                            .put ("msg", "user authentication failed")
                            .toString();
                    conn.sendResponse(401, data);
                }

                String username = verifiedJWT.getBody().getSubject();

                System.out.println("request to get balance: " + username);

                BigDecimal balance = backend.getUser(username).getBalance();


                String result = new JSONObject()
                        .put("balance", balance)
                        .toString();
                conn.sendResponse(200, result);

            } catch (Exception e) {
                String data = new JSONObject()
                        .put ("msg", "cannot get balance")
                        .toString();
                conn.sendResponse(400, data);
            }

        }
    }

    /**
     * GET /recent-transaction handler
     */
    static class GETRecentTransactionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            HttpConnection conn = new HttpConnection(exchange);

            System.out.println("trying to get recent transactions");

            try {
                conn.require("GET");
                String jwt = conn.getJWT();
                Jws<Claims> verifiedJWT = decodeJWS(jwt);

                if (verifiedJWT == null) {
                    String data = new JSONObject()
                            .put ("msg", "user authentication failed")
                            .toString();
                    conn.sendResponse(401, data);
                }

                String username = verifiedJWT.getBody().getSubject();

                System.out.println("request to get recent transactions: " + username);

                Transaction[] transactions = backend.getDateTimeTransactions(username, 5);

                JSONArray transactionsJSON = new JSONArray();

                for (Transaction t: transactions){

                    String type;
                    switch (t.getPaymentMethod()) {
                        case CASH_OUT:
                            type = "cash";
                            break;
                        case PAYMENT:
                            type = "payment";
                            break;
                        case TRANSFER:
                            type = "transfer";
                            break;
                        case DEBIT:
                            type = "credit";
                            break;
                        default:
                            type = "cash";
                            break;
                    }
                    JSONObject tJSON = new JSONObject()
                            .put("id", t.getTransactionID())
                            .put("merchantName", t.getMerchantName())
                            .put("location", t.getLocationOfSpending())
                            .put("amount", t.getAmount())
                            .put("status", t.getTransactionStatus())
                            .put("date", t.getDateTime().format(DateTimeFormatter.ISO_DATE))
                            .put("type", type)
                            .put("description", t.getTransactionDescription());
                    transactionsJSON.put(tJSON);
                }
                String result = new JSONObject()
                        .put("transactions", transactionsJSON)
                        .toString();
                conn.sendResponse(200, result);

            } catch (Exception e) {
                String data = new JSONObject()
                        .put ("msg", "cannot get transaction")
                        .toString();
                conn.sendResponse(400, data);
            }

        }
    }

}