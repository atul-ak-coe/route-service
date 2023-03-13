package com.fleet.management.route.service.impl;

import com.fleet.management.route.dto.RouteDetailDTO;
import com.fleet.management.route.model.RouteDetail;
import com.fleet.management.route.respository.RouteRepository;
import com.fleet.management.route.service.Delegate.ProducerService;
import com.fleet.management.route.service.RouteDetailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class RouteDetailDetailServiceImpl implements RouteDetailService {

    @NotNull
    private final RouteRepository routeRepository;

    private final ProducerService producerService;

    @NotNull
    private final ModelMapper modelMapper;

    @Override
    public Mono<RouteDetailDTO> addRouteDetail(RouteDetailDTO routeDetailDTO) {
        return Mono.just(modelMapper.map(routeDetailDTO, RouteDetail.class))
                .flatMap(routeRepository::save)
                .map(this::publishNotification)
                .map(rd -> modelMapper.map(rd, RouteDetailDTO.class));
    }

    @Override
    public Mono<RouteDetailDTO> getRouteDetailById(String routeId) {
        Long actualRouteId = Long.parseLong(routeId);
        return routeRepository.findById(actualRouteId)
                .map(rd -> modelMapper.map(rd, RouteDetailDTO.class));
    }

    @Override
    public Mono<Void> deleteRouteDetailById(String routeId) {
        Long actualRouteId = Long.parseLong(routeId);
        return routeRepository.deleteById(actualRouteId);
    }

    private RouteDetail publishNotification(RouteDetail routeDetail) {
        producerService.send(routeDetail);

        return routeDetail;
    }
}
