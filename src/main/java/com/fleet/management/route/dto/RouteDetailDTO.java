package com.fleet.management.route.dto;

import com.fleet.management.route.constant.RouteType;
import com.fleet.management.route.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@Builder
public class RouteDetailDTO {
    private String routeName;

    private String routeDesc;

    private List<Coordinate> coordinates;

    private double distance;

    private RouteType routeType;
}
