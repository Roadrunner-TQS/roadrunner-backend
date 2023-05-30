package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadRunnerService {
    private final AdminService adminService;
    private final AuthService authService;
    private final PartnerService partnerService;

    private static final ErrorDTO UNAUTHORIZED = new ErrorDTO("Unauthorized");
    public Object getPickUpLocationById(UUID pickUpid, String token) {
        return null;
    }

    public Object getPackageById(UUID packageId, String token) {
        return null;
    }

    public Object getAllPackages(String state, String token) {
       return null;
    }

}
