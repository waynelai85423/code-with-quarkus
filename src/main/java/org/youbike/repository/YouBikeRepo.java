package org.youbike.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.youbike.model.entity.YouBike;

@ApplicationScoped
public class YouBikeRepo implements PanacheMongoRepositoryBase<YouBike,String> {

}
