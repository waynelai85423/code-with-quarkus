package org.youbike.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.youbike.model.dto.YouBikeDto;
import org.youbike.model.entity.StationChange;
import org.youbike.service.YouBikeService;


@Path("/you-bike")
public class YouBikeResource  {

    @Inject
    YouBikeService youBikeService;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<YouBikeDto> getYouBikeList(){
        return youBikeService.getYouBikeList();
    }

    @GET
    @Path("/station/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public YouBikeDto getStationById(@PathParam("id") String id) {
        return youBikeService.getStationById(id);
    }

    @GET
    @Path("/changes/recent")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StationChange> getYouBikeChangeRecent() {
        return youBikeService.getYouBikeChangeRecent("recent10");
    }

    @POST
    @Path("/import")
    @Produces(MediaType.APPLICATION_JSON)
    public void importData() {
        youBikeService.importData();
    }

}
