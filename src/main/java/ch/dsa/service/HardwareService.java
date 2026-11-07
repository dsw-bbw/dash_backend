package ch.dsa.service;

import ch.dsa.annotation.Collect;
import ch.dsa.utility.FileUtility;

import java.util.List;

public class HardwareService implements IService {
    @Collect("totalMem")
    private double getTotalMemory() {
        for (String line : getMemoryList()) {
            if (line.startsWith("MemTotal")) {
                String[] totalMem = FileUtility.splitString(line);
                return Double.parseDouble(totalMem[1]) / 1000;
            }
        }
        return -1;
    }

    @Collect("availableMem")
    private double getAvailableMemory() {
        for (String line : getMemoryList()) {
            if (line.startsWith("MemAvailable")) {
                String[] availableMem = FileUtility.splitString(line);
                return Double.parseDouble(availableMem[1]) / 1000;
            }
        }
        return -1;
    }

    @Collect("diskStorage")
    private double getTotalDiskStorage() {
        String sections = FileUtility.readLines("/sys/block/sdb/size").trim();
        long bytes = Long.parseLong(sections) * 512;
        return (double) bytes / (1024 * 1024);
    }

    private List<String> getMemoryList() {
        return FileUtility.readFile("/proc/meminfo");
    }
}
