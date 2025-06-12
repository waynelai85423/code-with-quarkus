package org.youbike.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.youbike.client.YouBikeApiClient;
import org.youbike.model.dto.YouBikeDto;
import org.youbike.model.entity.YouBike;
import org.youbike.model.mapper.YouBikeMapper;
import org.youbike.repository.YouBikeRepo;

@Slf4j
@ApplicationScoped
public class YouBikeService {
    @Inject
    @RestClient
    YouBikeApiClient youBikeApiClient;

    @Inject
    YouBikeRepo youBikeRepo;
    
    @Inject
    YouBikeMapper youBikeMapper;

    public List<YouBikeDto> getYouBikeList() {
        return youBikeApiClient.getYouBikeList();
    }

    @CacheResult(cacheName = "stations")
    public YouBikeDto getStationById(String id) {
        YouBike youBike = youBikeRepo.findById(id);
        if (youBike != null) {
            return youBikeMapper.toDto(youBike);
        }
        throw new NoSuchElementException("YouBike station not found with id: " + id);
    }

    //每5分鐘執行一次，系統啟動後延遲10秒開始執行
    @Scheduled(identity = "import-stations-task", every = "5m",delayed = "10s")
    @CacheInvalidateAll(cacheName = "stations")
    public void importData() {
        youBikeMapper.toEntityList(youBikeApiClient.getYouBikeList())
                .forEach(youBikeRepo::persistOrUpdate);
        log.info("Imported YouBike data successfully.");
    }

}
