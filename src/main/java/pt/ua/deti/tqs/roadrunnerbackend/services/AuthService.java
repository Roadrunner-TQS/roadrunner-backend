package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.UserRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.RegisterRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.UserDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthService {
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    private final PickUpLocationRepository pickUpLocationRepository;

    private final JwtTokenService jwtTokenService;

    private static final Map<String, String> whiteList = new HashMap<>();

    public AuthService(UserRepository userRepository, PickUpLocationRepository pickUpLocationRepository, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.pickUpLocationRepository = pickUpLocationRepository;
        this.jwtTokenService = jwtTokenService;
    }

    public Object login(LoginRequest loginRequest) {
        return null;
    }

    public Object signup(RegisterRequest userRequest) {
        return null;
    }

    public Boolean logout(String tokenRequest) {
        return false;
    }

    public Boolean isAuthenticated(String tokenRequest) {
        return false;
    }

    public Boolean isAdmin(String tokenRequest){
        return false;
    }

    public Boolean isPartner(String tokenRequest){
        return false;
    }

    public UserDto getCurrentUser(String tokenRequest) {
        return null;
    }

    void cleanWhiteList(){}
}
