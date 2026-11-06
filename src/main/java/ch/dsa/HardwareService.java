package ch.dsa;

import ch.dsa.utility.FileUtility;

import java.util.List;
import java.util.Map;

public class HardwareService implements IService {
    @Override
    public Map<String, Object> collect() {
        map.put("totalMem", getTotalMemory());

        return map;
    }

    private double getTotalMemory() {
        for (String line : getMemoryList()) {
            if (line.startsWith("MemTotal")) {
                String totalMem = line.substring(line.indexOf(":") + 1).trim();
                return Double.parseDouble(totalMem);
            }
        }
        return -1;
    }

    private List<String> getMemoryList() {
        return FileUtility.readFile("/proc/meminfo");
    }
}
