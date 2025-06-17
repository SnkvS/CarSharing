package com.senkiv.carsharing.mapper;

import com.senkiv.carsharing.config.MapperConfig;
import com.senkiv.carsharing.dto.UserProfileUpdateRequestDto;
import com.senkiv.carsharing.dto.UserRegistrationRequestDto;
import com.senkiv.carsharing.dto.UserResponseDto;
import com.senkiv.carsharing.model.Role;
import com.senkiv.carsharing.model.Role.RoleName;
import com.senkiv.carsharing.model.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto dto);

    User toModel(UserProfileUpdateRequestDto dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToRoleNames")
    UserResponseDto toDto(User user);

    @Named("rolesToRoleNames")
    default Set<RoleName> rolesToRoleNames(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
    }
}
