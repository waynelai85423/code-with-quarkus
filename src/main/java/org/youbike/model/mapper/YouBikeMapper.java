package org.youbike.model.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.youbike.model.dto.YouBikeDto;
import org.youbike.model.entity.YouBike;

@Mapper(componentModel = "cdi",
        //如果來源是null，則忽略目標屬性
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface YouBikeMapper {
    YouBikeDto toDto(YouBike entity);
    YouBike toEntity(YouBikeDto dto);
    List<YouBikeDto> toDtoList(List<YouBike> entities);
    List<YouBike> toEntityList(List<YouBikeDto> dtos);
}
