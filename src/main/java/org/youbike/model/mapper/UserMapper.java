package org.youbike.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.youbike.model.dto.UserDto;
import org.youbike.model.entity.User;
import org.youbike.resource.UserResource.UserRequest;

@Mapper(componentModel = "cdi",
        //如果來源是null，則忽略目標屬性
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserDto toDto(User entity);
    User toEntity(UserDto dto);

    User registerRequestToUser(UserRequest userRequest);

    User updateRequestToUser(UserRequest userRequest, @MappingTarget User beforeUser);
}
