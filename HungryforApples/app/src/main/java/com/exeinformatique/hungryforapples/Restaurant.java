package com.exeinformatique.hungryforapples;

import java.util.List;
import java.util.Map;

public class Restaurant {
    String Name;
    Map<String,Double> Position;
    List<String> Services;
    String Type;
    String heureOuverture;
    String heureFermeture;

    public Restaurant(String name, Map<String, Double> position, List<String> services, String type,
                      String heureOuverture, String heureFermeture) {
        Name = name;
        Position = position;
        Services = services;
        Type = type;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
    }

    public String getName() {
        return Name;
    }

    public Map<String, Double> getPosition() {
        return Position;
    }

    public List<String> getServices() {
        return Services;
    }

    public String getType() {
        return Type;
    }

    public String getHeureOuverture() {
        return heureOuverture;
    }

    public String getHeureFermeture() {
        return heureFermeture;
    }
}
