package ch.dsa.service;

import ch.dsa.utility.FileUtility;

import java.util.LinkedHashMap;
import java.util.Map;

public class OSService implements IService {
    @Override
    public Map<String, Object> collect() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("uptime", getUptime());
        return map;
    }

    private double getUptime() {
        String[] uptime = FileUtility.readSplitLines("/proc/uptime");
        return Double.parseDouble(uptime[0]);
    }
}
