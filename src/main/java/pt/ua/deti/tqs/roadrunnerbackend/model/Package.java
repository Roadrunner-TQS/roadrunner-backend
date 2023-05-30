package pt.ua.deti.tqs.roadrunnerbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "packages")
public class Package {

    @Id
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "pt.ua.deti.tqs.roadrunnerbackend.config.CustomUUIDGenerator")
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<State> states = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "pickuplocation_id", nullable = true)
    private PickUpLocation pickUpLocation;

    @ManyToOne
    private Shop shop;

    private long timestamp = System.currentTimeMillis();

    private Status status = Status.PENDING;

    public boolean canBeUpdated() {
        return !this.status.equals(Status.CANCELLED) && !this.status.equals(Status.RETURNED) &&
                !this.status.equals(Status.DENIED) && !this.status.equals(Status.FORGOTTEN);
    }

}
