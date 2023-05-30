package pt.ua.deti.tqs.roadrunnerbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessDTO<T> {
    private T message;

}
