package ch.dsa;

import ch.dsa.service.HardwareService;
import ch.dsa.service.OSService;
import ch.dsa.service.PropertyService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

public class DashboardHandler implements HttpHandler {
    private final Config cfg;
    private final HardwareService hardware = new HardwareService();
    private final OSService os = new OSService();
    private final PropertyService property = new PropertyService();
    private final Gson gson = new Gson();

    public DashboardHandler(Config cfg) {
        this.cfg = cfg;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        try {
            if (!isAuthorized(ex)) {
                sendJson(ex, 401, Map.of("error", "unauthorized"));
                return;
            }
            String path = ex.getRequestURI().getPath();
            switch (path) {
                case "/api/hardware"    -> sendJson(ex, 200, hardware.collect());
                case "/api/os"          -> sendJson(ex, 200, os.collect());
                case "/api/property"    -> sendJson(ex, 200, property.collect());
                default                 -> sendJson(ex, 404, Map.of("error", "not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJson(ex, 500, Map.of("error", String.valueOf(e.getMessage())));
        } finally {
            ex.close();
        }
    }

    private boolean isAuthorized(HttpExchange ex) {
        String given = ex.getRequestHeaders().getFirst("X-Token");
        if (given == null) {
            String query = ex.getRequestURI().getQuery();
            if (query != null) {
                for (String pair : query.split("&")) {
                    if (pair.startsWith("token=")) given = pair.substring(6);
                }
            }
        }
        if (given == null) return false;
        return MessageDigest.isEqual(
                given.getBytes(StandardCharsets.UTF_8),
                cfg.get("token").getBytes(StandardCharsets.UTF_8));
    }

    private void sendJson(HttpExchange ex, int code, Object data) throws IOException {
        byte[] body = gson.toJson(data).getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, body.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(body);
        }
    }
}