package com.dev.memberblog.role.mapper;
import com.dev.memberblog.role.dto.RoleDTO;
import com.dev.memberblog.role.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    RoleDTO toDTO(Role entity);
    Role toEntity(RoleDTO dto);
}
