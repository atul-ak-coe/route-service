package com.fleet.management.route.model;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
public class Coordinate {

    private String latitude;

    private String longitude;

    private Integer stepNum;
}
