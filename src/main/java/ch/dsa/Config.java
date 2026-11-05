package ch.dsa;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Properties;

public class Config {
    private final Properties props = new Properties();
    private final Path path;

    public Config(Path path) throws IOException {
        this.path = path;
        if (Files.exists(path)) {
            try (Reader r = Files.newBufferedReader(path)) {
                props.load(r);
            }
            setDefault("bind", "0.0.0.0");
            setDefault("port", "8765");
            setDefault("token", newToken());
            setDefault("mc_dir", System.getProperty("user.home") + "/minecraft");
            save();
        }
    }

    private void setDefault(String key, String value) {
        props.putIfAbsent(key, value);
    }

    private static String newToken() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    public void save() throws IOException {
        try (Writer w = Files.newBufferedWriter(path)) {
            props.store(w, "Minecraft-Dashboard Konfiguration");
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public Path getPath() {
        return path;
    }
}
