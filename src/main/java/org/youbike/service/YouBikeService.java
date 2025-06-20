package org.youbike.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.youbike.client.YouBikeApiClient;
import org.youbike.model.dto.YouBikeDto;
import org.youbike.model.entity.StationChange;
import org.youbike.model.entity.YouBike;
import org.youbike.model.mapper.YouBikeMapper;
import org.youbike.repository.StationChangeRepo;
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
    StationChangeRepo stationChangeRepo;

    @Inject
    YouBikeMapper youBikeMapper;

    @Channel("station-change-out")
    Emitter<List<StationChange>> stationChangeEmitter;

    @Channel("youbike-cache-update-out")
    Emitter<List<YouBikeDto>> youBikeEmitter;

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

    //每1分鐘執行一次，系統啟動後延遲10秒開始執行
    @Scheduled(identity = "import-stations-task", every = "5m",delayed = "10s")
    @CacheInvalidateAll(cacheName = "stations")
    @CacheInvalidateAll(cacheName = "stations-change")
    public void importData() {
        List<YouBikeDto> youBikeListFromApi = youBikeApiClient.getYouBikeList();

        Map<String, YouBike> youBikeMapFromDb = youBikeRepo.findAll().stream()
                .collect(Collectors.toMap(YouBike::getSno, Function.identity()));

        List<StationChange> stationChanges = new ArrayList<>();
        List<YouBikeDto> youBikesChanges = new ArrayList<>();

        for (YouBikeDto youBikeDto : youBikeListFromApi) {
            YouBike youBike = youBikeMapFromDb.get(youBikeDto.getSno());

            //如果DB也有相同站點資料和可借車數量不一致
            if (isStationChange(youBike, youBikeDto)) {
                StationChange stationChange = createStationChange(youBike, youBikeDto);
                stationChanges.add(stationChange);
                youBikesChanges.add(youBikeDto);
            }
        }
        stationChangeEmitter.send(stationChanges);
        youBikeEmitter.send(youBikesChanges);

        youBikeMapper.toEntityList(youBikeListFromApi).forEach(youBikeRepo::persistOrUpdate);
        log.info("Imported YouBike data successfully.");
    }



    @CacheResult(cacheName = "stations-change")
    public List<StationChange> getYouBikeChangeRecent(@CacheKey String recent10) {
        return stationChangeRepo.findRecent();
    }

    private boolean isStationChange(YouBike youBike, YouBikeDto youBikeDto) {
        return youBike != null &&
                !Objects.equals(youBike.getAvailable_rent_bikes(), youBikeDto.getAvailable_rent_bikes());
    }

    private StationChange createStationChange(YouBike youBike, YouBikeDto youBikeDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StationChange stationChange = new StationChange();
        stationChange.setStationId(youBike.getSno());
        stationChange.setChangeTime(
                LocalDateTime.parse(youBikeDto.getUpdateTime(), formatter));
        stationChange.setBefore(youBike.getAvailable_rent_bikes());
        stationChange.setAfter(youBikeDto.getAvailable_rent_bikes());
        stationChange.setDelta(stationChange.getAfter() - stationChange.getBefore());
        return stationChange;
    }
}
