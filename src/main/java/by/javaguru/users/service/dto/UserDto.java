package by.javaguru.users.service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AlbumDto> albums;

}
