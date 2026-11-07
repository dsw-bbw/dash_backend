package ch.dsa.service;

import ch.dsa.utility.FileUtility;

import java.util.Map;

public class OSService implements IService {
    @Override
    public Map<String, Object> collect() {
        map.put("uptime", getUptime());
        return map;
    }

    private double getUptime() {
        return Double.parseDouble(FileUtility.readLines("/proc/uptime"));
    }
}
