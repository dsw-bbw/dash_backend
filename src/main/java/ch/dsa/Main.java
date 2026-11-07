package ch.dsa;

import com.sun.net.httpserver.HttpServer;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Config cfg = new Config(Path.of("config.properties"));

        HttpServer server = HttpServer.create(
                new InetSocketAddress(cfg.get("bind"), cfg.getInt("port")), 0);
        server.createContext("/", new DashboardHandler(cfg));
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("Dashboard läuft: http://" + localIp() + ":" + cfg.get("port") + "/");
        System.out.println("Token:          " + cfg.get("token") + "   (steht auch in " + cfg.getPath() + ")");
        System.out.println("Minecraft-Ordner: " + cfg.get("mc_dir"));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Beende Dashboard …");
            server.stop(1);
        }));
    }

    private static String localIp() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 80);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            return "localhost";
        }
    }
}