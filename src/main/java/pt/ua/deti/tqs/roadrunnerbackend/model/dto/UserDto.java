package pt.ua.deti.tqs.roadrunnerbackend.model.dto;

import lombok.Data;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;

import java.util.UUID;

@Data
public class UserDto {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private PickUpLocation pickUpLocation;
    private String role;

}
