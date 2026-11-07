package ch.dsa.service;

import ch.dsa.annotation.FindCollect;

import java.util.Map;

public interface IService {
    default Map<String, Object> collect() {
        return FindCollect.collect(this);
    }
}
