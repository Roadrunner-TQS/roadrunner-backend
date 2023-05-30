package pt.ua.deti.tqs.roadrunnerbackend.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PickUpLocationRepository extends JpaRepository<PickUpLocation, UUID> {
    List<PickUpLocation> findByCityAndDisable(String city, Boolean disable);

    List<PickUpLocation> findByAcceptedAndDisable(Boolean accepted, Boolean disable);

    List<PickUpLocation> findByCityAndAcceptedAndDisable(String city, Boolean accepted, Boolean disable);

    List<PickUpLocation> findByDisable(Boolean disable);

    Optional<PickUpLocation> findByIdAndDisable(UUID pickUpLocationId, Boolean disable);
    Optional<PickUpLocation> findByAddress(String address);
}
