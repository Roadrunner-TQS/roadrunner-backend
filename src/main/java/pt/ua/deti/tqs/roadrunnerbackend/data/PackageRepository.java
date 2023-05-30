package pt.ua.deti.tqs.roadrunnerbackend.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID> {
}
