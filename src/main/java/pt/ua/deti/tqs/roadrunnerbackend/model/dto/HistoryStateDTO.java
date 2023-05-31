package pt.ua.deti.tqs.roadrunnerbackend.model.dto;

import lombok.Data;
import pt.ua.deti.tqs.roadrunnerbackend.model.State;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class HistoryStateDTO {
    private Map<UUID, List<State>> statuses = new HashMap<>();
}
