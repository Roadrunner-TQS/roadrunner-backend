package pt.ua.deti.tqs.roadrunnerbackend.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDto {
    private String firstName;
    private String lastName;
    private String email;
    private long phone;
    private UUID pickUpLocationId;
    private UUID shopId;
    private long date;
}
