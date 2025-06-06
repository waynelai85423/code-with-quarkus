package org.youbike.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.youbike.model.dto.YouBikeDto;

@RegisterRestClient(configKey = "you-bike-api")
public interface YouBikeApiClient {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<YouBikeDto> getYouBikeList();
}
