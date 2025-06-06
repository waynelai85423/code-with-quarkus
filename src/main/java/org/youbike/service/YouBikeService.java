package org.youbike.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.youbike.client.YouBikeApiClient;
import org.youbike.model.dto.YouBikeDto;

@ApplicationScoped
public class YouBikeService {
    @Inject
    @RestClient
    YouBikeApiClient youBikeApiClient;

    public List<YouBikeDto> getYouBikeList() {
        return youBikeApiClient.getYouBikeList();
    }

    public YouBikeDto getStationById(String id) {
        return youBikeApiClient.getYouBikeList().stream()
                .filter(youBike -> youBike.getSno().equals(id)).findFirst().orElse(null);
    }
}
