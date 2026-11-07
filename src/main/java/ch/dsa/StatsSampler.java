package ch.dsa;

import ch.dsa.utility.FileUtility;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class StatsSampler {
    private final AtomicReference<Map<String, Object>> latest = new AtomicReference<>(Map.of());

    public void start(ScheduledExecutorService executor) {
        executor.scheduleAtFixedRate(this::sample, 0, 5, TimeUnit.SECONDS);
    }

    private void sample() {
        try {
            Map<String, Object> s = new LinkedHashMap<>();
            s.put("time", Instant.now().toString());
            s.put("load", ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
            s.putAll(readMemInfo());
            latest.set(s);
        } catch (Exception e) {
            System.err.println("Sampler-Fehler: " + e.getMessage());
        }
    }

    private Map<String, Object> readMemInfo() throws IOException {
        long total = 0, available = 0;
        for (String line : Files.readAllLines(Path.of("/proc/meminfo"))) {
            String[] parts = FileUtility.splitString(line);
            if (parts[0].equals("MemTotal:"))     total = Long.parseLong(parts[1]);
            if (parts[0].equals("MemAvailable:")) available = Long.parseLong(parts[1]);
        }
        return Map.of(
                "ram_total_mb", total / 1024,
                "ram_used_mb", (total - available) / 1024
        );
    }

    public Map<String, Object> latest() {
        return latest.get();
    }
}
