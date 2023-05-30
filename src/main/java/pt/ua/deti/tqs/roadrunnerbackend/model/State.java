package pt.ua.deti.tqs.roadrunnerbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "states")
public class State {

    @Id
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "pt.ua.deti.tqs.roadrunnerbackend.config.CustomUUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private long timestamp;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}
