package org.youbike.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.youbike.model.dto.YouBikeDto;
import org.youbike.model.entity.StationChange;
import org.youbike.repository.StationChangeRepo;

@Slf4j
@ApplicationScoped
@Transactional
public class StationChangeConsumer {
    @Inject
    ObjectMapper objectMapper;

    @Inject
    StationChangeRepo stationChangeRepo;

    @Incoming("station-change-in")
    public void receiveStationChange(String stationChange) throws JsonProcessingException {
        List<StationChange> stationChanges = objectMapper.readValue(stationChange, new TypeReference<>() {});
        stationChangeRepo.persist(stationChanges);
        log.info("stationChange insert success: {} items", stationChanges.size());
    }

    @Incoming("youbike-cache-update-in")
    public void receiveYouBikeUpdateCache(String youBikeDto) throws JsonProcessingException {
        List<YouBikeDto> youBikeDtos = objectMapper.readValue(youBikeDto, new TypeReference<>() {});
        for (YouBikeDto dto : youBikeDtos) {
            saveCache(dto.getSno(), dto);
        }
        log.info("youBike cache update success: {} items", youBikeDtos.size());
    }

    @CacheResult(cacheName = "stations")
    public YouBikeDto saveCache(@CacheKey String id , YouBikeDto youBikeDto) {
        return youBikeDto;
    }

}