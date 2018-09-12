package com.polytech.edt.model;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class IniReport {

    private Map<String, Map<String, String>> map;

    public IniReport() {
        map = new HashMap<>();
    }

    public void addCategory(String category) {
        if (!map.containsKey(category)) {
            map.put(category, new HashMap<String, String>());
        }
    }

    public Map<String, String> getCategory(String category) {
        if (!map.containsKey(category)) {
            return null;
        }
        return map.get(category);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (String category : new TreeSet<>(map.keySet())) {
            builder.append("[" + category + "]\n");
            for (String key : new TreeSet<>(map.get(category).keySet())) {
                if (map.get(category).get(key).length() < 15) {
                    builder.append(key + ": " + map.get(category).get(key));
                }
                else {
                    builder.append(key + ":\n" + map.get(category).get(key));
                }
                builder.append("\n");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
