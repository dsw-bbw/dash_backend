package ch.dsa.service;

import ch.dsa.utility.FileUtility;

import java.util.List;
import java.util.Map;

public class HardwareService implements IService {
    @Override
    public Map<String, Object> collect() {
        map.put("totalMem", getTotalMemory());
        map.put("availableMem", getAvailableMemory());
        map.put("diskStorage", getTotalDiskStorage());
        return map;
    }

    private double getTotalMemory() {
        for (String line : getMemoryList()) {
            if (line.startsWith("MemTotal")) {
                String[] totalMem = FileUtility.splitString(line);
                return Double.parseDouble(totalMem[1]) / 1000;
            }
        }
        return -1;
    }

    private double getAvailableMemory() {
        for (String line : getMemoryList()) {
            if (line.startsWith("MemAvailable")) {
                String[] availableMem = FileUtility.splitString(line);
                return Double.parseDouble(availableMem[1]) / 1000;
            }
        }
        return -1;
    }

    private double getTotalDiskStorage() {
        String sections = FileUtility.readLines("/sys/block/sdb/size").trim();
        long bytes = Long.parseLong(sections) * 512;
        return (double) bytes / (1024 * 1024);
    }

    private List<String> getMemoryList() {
        return FileUtility.readFile("/proc/meminfo");
    }
}
