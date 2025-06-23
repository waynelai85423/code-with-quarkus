package org.youbike.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import org.youbike.model.entity.StationChange;

@ApplicationScoped
public class StationChangeRepo implements PanacheRepositoryBase<StationChange, Long> {
    public List<StationChange> findRecent() {
        return find("ORDER BY changeTime DESC").page(0, 10).list();
    }
}
