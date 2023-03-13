package com.fleet.management.route.service;

import com.fleet.management.route.dto.RouteDetailDTO;
import reactor.core.publisher.Mono;

public interface RouteDetailService {

    Mono<RouteDetailDTO> addRouteDetail(RouteDetailDTO routeDetailDTO);

    Mono<RouteDetailDTO> getRouteDetailById(String routeId);

    Mono<Void> deleteRouteDetailById(String routeId);
}
