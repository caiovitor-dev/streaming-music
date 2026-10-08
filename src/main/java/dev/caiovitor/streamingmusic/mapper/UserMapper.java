package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.UserCreateDTO;
import dev.caiovitor.streamingmusic.dto.UserProfileResponseDTO;
import dev.caiovitor.streamingmusic.dto.UserProfileUpdateDTO;
import dev.caiovitor.streamingmusic.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {


    public User toEntity(UserCreateDTO userDTO);

    UserProfileResponseDTO toDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfile(UserProfileUpdateDTO userUpdate, @MappingTarget User user);
}
