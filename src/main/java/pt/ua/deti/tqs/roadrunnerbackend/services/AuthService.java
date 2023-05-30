package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.UserRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.User;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginResponse;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.RegisterRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.SuccessDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.UserDto;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Roles;

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
        log.info("AuthService -- Login -- request received");
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user != null && user.checkPassword(loginRequest.getPassword())) {
            if (user.getRole().equals(Roles.ROLE_NOT_VERIFIED) || user.getRole().equals(Roles.ROLE_DELETE)) {
                log.error("AuthService -- Login -- Not Authorized");
                return new ErrorDTO("Not Authorized");
            }
            String token = jwtTokenService.generateToken(user);
            whiteList.put(user.getEmail(), token);
            log.info("AuthService -- Login -- successful");
            return new LoginResponse(token);
        }
        log.error("AuthService -- Login -- Invalid credentials");
        return new ErrorDTO("Invalid credentials");
    }

    public Object signup(RegisterRequest userRequest) {
        log.info("AuthService -- Signup -- request received");
        User user = userRepository.findByEmail(userRequest.getEmail()).orElse(null);
        if(user != null){
            log.error("AuthService -- signup -- User already exists");
            return new ErrorDTO("User already exists");
        }
        PickUpLocation pickUpLocation = pickUpLocationRepository.findByAddress(userRequest.getPickUpLocation().getAddress()).orElse(null);
        if(pickUpLocation != null){
            log.error("AuthService -- signup -- PickUpLocation already exists");
            return new ErrorDTO("PickUpLocation already exists");
        }
        ModelMapper modelMapper = new ModelMapper();
        PickUpLocation newPickUpLocation = modelMapper.map(userRequest.getPickUpLocation(), PickUpLocation.class);
        newPickUpLocation.setSlug(userRequest.getPickUpLocation().getAddress().toLowerCase().replace(" ", "-"));
        PickUpLocation pickUpLocationsave = pickUpLocationRepository.save(newPickUpLocation);
        User newUser = modelMapper.map(userRequest, User.class);
        newUser.setRole(Roles.ROLE_NOT_VERIFIED);
        newUser.setPickUpLocation(pickUpLocationsave);
        userRepository.save(newUser);
        log.info("AuthService -- signup -- User created");
        return new SuccessDTO<String>("User created");
    }

    public Boolean logout(String tokenRequest) {
        log.info("AuthService -- Logout -- request received");
        String token = tokenRequest.replace(BEARER, "");
        String email = jwtTokenService.getEmailFromToken(token);
        if (email != null && whiteList.containsKey(email) && whiteList.get(email).equals(token)) {
            log.info("AuthService -- Logout -- Logout successful");
            whiteList.remove(email);
            return true;
        }
        log.error("AuthService -- Logout -- Logout failed");
        return false;
    }

    public Boolean isAuthenticated(String tokenRequest) {
        log.info("AuthService -- IsAuthenticated -- request received");
        String token = tokenRequest.replace(BEARER, "");
        if (whiteList.isEmpty()) {
            log.info("AuthService -- IsAuthenticated -- No tokens in whitelist");
            return false;
        }
        String email = jwtTokenService.getEmailFromToken(token);
        if (email == null || !jwtTokenService.validateToken(token) || !whiteList.containsKey(email)) {
            log.info("AuthService -- IsAuthenticated -- Invalid token");
            return false;
        }
        log.info("AuthService -- IsAuthenticated -- Token is valid");
        return whiteList.get(email).equals(token);
    }

    public Boolean isAdmin(String tokenRequest){
        log.info("AuthService -- IsAdmin -- request");
        if(Boolean.TRUE.equals(isAuthenticated(tokenRequest))){
            String token = tokenRequest.replace(BEARER, "");
            String email = jwtTokenService.getEmailFromToken(token);
            User user = userRepository.findByEmail(email).orElse(null);
            if(user != null && user.getRole().equals(Roles.valueOf("ROLE_ADMIN"))){
                log.info("AuthService -- IsAdmin -- User has ROLE_ADMIN");
                return true;
            }
            log.info("AuthService -- IsAdmin -- User doesn't have ROLE_ADMIN");
            return false;
        }
        log.info("AuthService -- IsAdmin -- User is not authenticated");
        return false;
    }

    public Boolean isPartner(String tokenRequest){
        if(Boolean.TRUE.equals(isAuthenticated(tokenRequest))){
            String token = tokenRequest.replace(BEARER, "");
            String email = jwtTokenService.getEmailFromToken(token);
            User user = userRepository.findByEmail(email).orElse(null);
            if(user != null && user.getRole().equals(Roles.valueOf("ROLE_PARTNER"))){
                log.info("AuthService -- isPartner -- User has ROLE_PARTNER");
                return true;
            }
            log.info("AuthService -- isPartner -- User doesn't have ROLE_PARTNER");
            return false;
        }
        log.info("AuthService -- isPartner -- User is not authenticated");
        return false;
    }

    public UserDto getCurrentUser(String tokenRequest) {
        log.info("AuthService -- getCurrentUser -- Get current user request");
        String token = tokenRequest.replace(BEARER, "");
        String email = jwtTokenService.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            log.info("AuthService -- getCurrentUser -- User found");
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(user, UserDto.class);
        }
        log.error("AuthService -- getCurrentUser -- User not found");
        return null;
    }

    void cleanWhiteList(){
        whiteList.clear();
    }
}
