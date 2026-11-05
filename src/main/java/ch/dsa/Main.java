package ch.dsa;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Config cfg = new Config(Path.of("config.properties"));

        ScheduledExecutorService jobs = Executors.newScheduledThreadPool(2);

        StatsSampler sampler = new StatsSampler();
        sampler.start(jobs);

        HttpServer server = HttpServer.create(
                new InetSocketAddress(cfg.get("bind"), cfg.getInt("port")), 0);
        server.createContext("/", new DashboardHandler(cfg, sampler));
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("Dashboard läuft: http://localhost:" + cfg.get("port") + "/");
        System.out.println("Token:          " + cfg.get("token") + "   (steht auch in " + cfg.getPath() + ")");
        System.out.println("Minecraft-Ordner: " + cfg.get("mc_dir"));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Beende Dashboard …");
            server.stop(1);
            jobs.shutdownNow();
        }));
    }
}