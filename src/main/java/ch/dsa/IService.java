package ch.dsa;

import java.util.LinkedHashMap;
import java.util.Map;

public interface IService {
    Map<String, Object> map = new LinkedHashMap<>();

    default Map<String, Object> collect() {
        return map;
    }
}
