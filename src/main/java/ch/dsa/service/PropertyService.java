package ch.dsa.service;

import ch.dsa.annotation.Collect;
import ch.dsa.utility.FileUtility;

import java.util.List;
import java.util.Objects;

public class PropertyService implements IService {
    @Collect("difficulty")
    private String getDifficulty() {
        String value = getValueFromKey("difficulty");
        return Objects.requireNonNull(value);
    }

    @Collect("levelName")
    private String getLevelName() {
        String value = getValueFromKey("level-name");
        return Objects.requireNonNull(value);
    }

    @Collect("maxPlayers")
    private int getMaxPlayers() {
        String value = getValueFromKey("max-players");
        return Integer.parseInt(Objects.requireNonNull(value));
    }

    @Collect("serverDescription")
    private String getServerDescription() {
        String value = getValueFromKey("motd");
        return Objects.requireNonNull(value);
    }

    @Collect("port")
    private int getPort() {
        String value = getValueFromKey("server-port");
        return Integer.parseInt(Objects.requireNonNull(value));
    }

    @Collect("simulationDistance")
    private int getSimulationDistance() {
        String value = getValueFromKey("simulation-distance");
        return Integer.parseInt(Objects.requireNonNull(value));
    }

    @Collect("viewDistance")
    private int getViewDistance() {
        String value = getValueFromKey("view-distance");
        return Integer.parseInt(Objects.requireNonNull(value));
    }

    private String getValueFromKey(String key) {
        for (String line : getPropertyFile()) {
            if (line.contains(key)) {
                return line.split("=")[1];
            }
        }
        return null;
    }

    private List<String> getPropertyFile() {
        return FileUtility.readFile("/home/damian/server/server.properties");
    }
}