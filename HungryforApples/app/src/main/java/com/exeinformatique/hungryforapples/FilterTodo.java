package com.exeinformatique.hungryforapples;

import java.util.Date;

public class FilterTodo {
    int rangeKm;
    int serviceId;
    String serviceName;

    public FilterTodo(int rangeKm, int serviceId, String serviceName) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.rangeKm = rangeKm;
    }

    public int getRangeKm() {
        return rangeKm;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setRangeKm(int rangeKm) {
        this.rangeKm = rangeKm;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
