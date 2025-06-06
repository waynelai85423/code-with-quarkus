package org.youbike.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.youbike.model.entity.YouBike;

@ApplicationScoped
public class YouBikeRepo implements PanacheRepository<YouBike> {

}
