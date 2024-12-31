package com.dev.memberblog.user.mapper;

import com.dev.memberblog.user.dto.UserDTO;
import com.dev.memberblog.user.dto.UserDetailsDTO;
import com.dev.memberblog.user.dto.UserDetailsUpdateDTO;
import com.dev.memberblog.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO toDTO(User entity);
    User toEntity(UserDTO dto);
    UserDetailsDTO toUserDetailsDTO(User entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    User updateToUserDetails(UserDetailsUpdateDTO userDetails, @MappingTarget User user);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    User update(UserDetailsDTO userDetails, @MappingTarget User user);
}
