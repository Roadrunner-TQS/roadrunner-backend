package pt.ua.deti.tqs.roadrunnerbackend.model.auth;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;


@Data
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private PickUpLocation pickUpLocation;
    @Nullable
    private String role = "ROLE_NOT_VERIFIED";
}
