package com.flamingo.qa.helpers;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ThreadVarsHashMap {

    private final InheritableThreadLocal<HashMap<Enum, Object>> tlHashMapList = new InheritableThreadLocal<>();

    public void put(Enum key, Object value) {
        if (tlHashMapList.get() == null) tlHashMapList.set(new HashMap<>());
        tlHashMapList.get().put(key, value);
    }

    public Object get(Enum key) {
        if (tlHashMapList.get() == null) return null;
        else return tlHashMapList.get().getOrDefault(key, null);
    }

    public String getString(Enum key) {
        if (tlHashMapList.get() == null) return null;
        else return tlHashMapList.get().get(key) != null ? tlHashMapList.get().get(key).toString() : null;
    }

    public void clear() {
        if (tlHashMapList.get() != null) tlHashMapList.get().clear();
    }

    @Override
    public String toString() {
        return tlHashMapList.get().entrySet().toString();
    }
}