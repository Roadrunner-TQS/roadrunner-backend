package pt.ua.deti.tqs.roadrunnerbackend.model.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;

    @JsonCreator
    public LoginResponse(@JsonProperty("token") String token) {
        this.token = token;
    }
}
