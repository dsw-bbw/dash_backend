package ch.dsa.service;

import ch.dsa.annotation.Collect;
import ch.dsa.utility.FileUtility;

import java.util.List;

public class HardwareService implements IService {
    private long prevIdle = -1;
    private long prevTotal = -1;

    @Collect("cpuPercent")
    private double getCpuUsage() {
        String firstLine = FileUtility.readFile("/proc/stat").get(0);
        String[] f = FileUtility.splitString(firstLine.trim());

        long idle = Long.parseLong(f[4]) + Long.parseLong(f[5]);
        long total = 0;
        for (int i = 1; i <= 8; i++) {
            total += Long.parseLong(f[i]);
        }

        double usage = -1;
        if (prevTotal >= 0 && total > prevTotal) {
            usage = 100.00 * (1 -(double) (idle - prevIdle) / (total - prevTotal));
        }

        prevIdle = idle;
        prevTotal = total;
        return Math.round(usage * 10) / 10.0;
    }

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
