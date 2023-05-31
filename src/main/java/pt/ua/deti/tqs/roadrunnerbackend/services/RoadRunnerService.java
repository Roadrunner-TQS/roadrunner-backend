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
        log.info("RoadRunnerService -- Get pick up location by id -- request received");
        if(Boolean.TRUE.equals(authService.isAdmin(token)) ) {
            Object response = adminService.getPickUpLocationById(pickUpid);
            if (response instanceof ErrorDTO) {
                log.error("RoadRunnerService -- Get pick up location by id -- " + ((ErrorDTO) response).getMessage());
                return response;
            }
            log.info("RoadRunnerService -- Get pick up location by id -- Pick Up Location found");
            return response;
        }
        if (Boolean.TRUE.equals(authService.isPartner(token)) && authService.getCurrentUser(token).getPickUpLocation().getId().equals(pickUpid)) {
            log.info("RoadRunnerService -- Get pick up location by id -- Pick Up Location found");
            return authService.getCurrentUser(token).getPickUpLocation();
        }
            log.error("RoadRunnerService -- Get pick up location by id -- Unauthorized");
        return UNAUTHORIZED;
    }

    public Object getPackageById(UUID packageId, String token) {
        log.info("RoadRunnerService -- Get package by id -- request received");
        if(Boolean.TRUE.equals(authService.isAdmin(token)) ) {
            Object response = adminService.getPackageById(packageId);
            if (response instanceof ErrorDTO) {
                log.error("RoadRunnerService -- Get package by id -- " + ((ErrorDTO) response).getMessage());
                return response;
            }
            log.info("RoadRunnerService -- Get package by id -- Package found");
            return response;
        }
        if (Boolean.TRUE.equals(authService.isPartner(token)))
        {
            Object response = partnerService.getPackageById(authService.getCurrentUser(token).getPickUpLocation().getId(), packageId);
            if (response instanceof ErrorDTO) {
                log.error("RoadRunnerService -- Get package by id -- " + ((ErrorDTO) response).getMessage());
                return response;
            }
            log.info("RoadRunnerService -- Get package by id -- Package found");
            return response;
        }
        log.error("RoadRunnerService -- Get package by id -- Unauthorized");
        return UNAUTHORIZED;
    }

    public Object getAllPackages(String state, String token) {
        log.info("RoadRunnerService -- Get all packages -- request received");
        if(Boolean.TRUE.equals(authService.isAdmin(token)) ) {
            Object response = adminService.getPackages(state);
            if (response instanceof ErrorDTO) {
                log.error("RoadRunnerService -- Get all packages -- " + ((ErrorDTO) response).getMessage());
                return response;
            }
            log.info("RoadRunnerService -- Get all packages -- Packages found");
            return response;
        }
        if (Boolean.TRUE.equals(authService.isPartner(token)))
        {
            Object response = partnerService.getAllPackages(authService.getCurrentUser(token).getPickUpLocation().getId(), state);
            if (response instanceof ErrorDTO) {
                log.error("RoadRunnerService -- Get all packages -- " + ((ErrorDTO) response).getMessage());
                return response;
            }
            log.info("RoadRunnerService -- Get all packages -- Packages found");
            return response;
        }
        log.error("RoadRunnerService -- Get all packages -- Unauthorized");
        return UNAUTHORIZED;
    }

}
