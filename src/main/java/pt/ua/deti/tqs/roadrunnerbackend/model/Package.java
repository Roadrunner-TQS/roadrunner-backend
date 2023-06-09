package pt.ua.deti.tqs.roadrunnerbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.*;

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

    public void sort() {
        states.sort(Comparator.comparing(State::getTimestamp));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Package aPackage = (Package) o;
        return id != null && Objects.equals(id, aPackage.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
