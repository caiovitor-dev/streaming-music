package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.UserCreateDTO;
import dev.caiovitor.streamingmusic.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {


    public User toEntity(UserCreateDTO userDTO);
}
