package com.fleet.management.route.handler;

import com.fleet.management.route.constant.APIConstant;
import com.fleet.management.route.dto.RouteDetailDTO;
import com.fleet.management.route.service.RouteDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class RouteDetailHandler {

    @NotNull
    private final RouteDetailService routeDetailService;

    public Mono<ServerResponse> addRouteDetail(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RouteDetailDTO.class)
                .flatMap(routeDetailService::addRouteDetail)
                .flatMap(trackingDetailsDTO -> ServerResponse.created(URI.create(APIConstant.API_ADD_ROUTE))
                        .body(Mono.just(trackingDetailsDTO), RouteDetailDTO.class)
                );
    }

    public Mono<ServerResponse> getRouteDetail(ServerRequest serverRequest) {
        String routeId = serverRequest.pathVariable(APIConstant.API_PATH_PARAM_ROUTE_ID);

        return routeDetailService.getRouteDetailById(routeId)
                .flatMap(routeDetailDTO -> ServerResponse.ok()
                        .body(Mono.just(routeDetailDTO), RouteDetailDTO.class)
                );
    }
}
