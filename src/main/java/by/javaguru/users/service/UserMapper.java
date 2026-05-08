package by.javaguru.users.service;

import by.javaguru.users.data.UserEntity;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
    builder = @Builder(disableBuilder = true))
public interface UserMapper {

    UserEntity createUserDtoToUserEntity(CreateUserDto createUserDto);

    UserDto userEntityToUserDto(UserEntity userEntity);

}
