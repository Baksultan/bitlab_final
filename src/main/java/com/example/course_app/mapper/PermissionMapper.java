package com.example.course_app.mapper;

import com.example.course_app.dto.PermissionDTO;
import com.example.course_app.model.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionDTO toDto(Permission permission);

    Permission toModel(PermissionDTO permissionDTO);

    List<PermissionDTO> toDtoList(List<Permission> permissionList);

    List<Permission> toModelList(List<PermissionDTO> permissionDTOList);
}
