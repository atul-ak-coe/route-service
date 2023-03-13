package com.fleet.management.route.respository;

import com.fleet.management.route.model.RouteDetail;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends ReactiveCrudRepository<RouteDetail, Long> {
}
