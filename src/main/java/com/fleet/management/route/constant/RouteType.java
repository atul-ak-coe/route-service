package com.fleet.management.route.constant;

public enum RouteType {
    ROUTE_PICKUP("PICKUP"),
    ROUTE_DELIVERY("DELIVERY");

    private String name;

    RouteType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
