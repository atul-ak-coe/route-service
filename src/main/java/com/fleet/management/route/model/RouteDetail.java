package com.fleet.management.route.model;

import com.fleet.management.route.constant.RouteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.util.Map;

@AllArgsConstructor
@Builder
public class RouteDetail {

    @Id
    private Long routeId;

    private String routeName;

    private String routeDesc;

    private Map<String, Coordinate> coordinates;

    private double distance;

    private RouteType routeType;
}
