package ch.dsa.service;

import ch.dsa.annotation.Collect;
import ch.dsa.utility.FileUtility;

public class OSService implements IService {
    @Collect("uptime")
    private double getUptime() {
        String[] uptime = FileUtility.readSplitLines("/proc/uptime");
        return Double.parseDouble(uptime[0]);
    }
}
