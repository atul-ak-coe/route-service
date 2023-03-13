package com.fleet.management.route.router;

import com.fleet.management.route.constant.APIConstant;
import com.fleet.management.route.handler.RouteDetailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.validation.constraints.NotNull;

@Configuration
@RequiredArgsConstructor
public class RouteDetailRouter {

    @Value("${server.context-path}")
    private String contextPath;

    @NotNull
    private final RouteDetailHandler routeDetailHandler;

    @Bean
    public RouterFunction<ServerResponse> trackingService() {
        RouterFunction<ServerResponse> routeDetailFunction = initAddRoute().and(initGetRoute());

        return RouterFunctions.nest(RequestPredicates.path(contextPath), routeDetailFunction);
    }

    private RouterFunction<ServerResponse> initAddRoute() {
        return RouterFunctions.route(
                RequestPredicates.POST(APIConstant.API_ADD_ROUTE), routeDetailHandler::addRouteDetail
        );
    }

    private RouterFunction<ServerResponse> initGetRoute() {
        return RouterFunctions.route(
                RequestPredicates.POST(APIConstant.API_GET_ROUTE), routeDetailHandler::getRouteDetail
        );
    }
}
