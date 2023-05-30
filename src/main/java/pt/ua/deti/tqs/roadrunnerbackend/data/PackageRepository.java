package pt.ua.deti.tqs.roadrunnerbackend.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID> {

    Optional<Package> findById(UUID id);

    List<Package> findByStatus(Status status);
    List<Package> findByPickUpLocation(PickUpLocation pickUpLocation);

    List<Package> findAllByPickUpLocationId(UUID partnerId);

    List<Package> findAllByPickUpLocationIdAndStatus(UUID partnerId, Status status);

    List<Package> findAllByShopId(UUID shopId);

    List<Package> findAllByStatus(Status status);
}
