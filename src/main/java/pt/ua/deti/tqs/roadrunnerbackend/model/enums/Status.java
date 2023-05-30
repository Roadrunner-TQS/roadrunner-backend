package pt.ua.deti.tqs.roadrunnerbackend.model.enums;

public enum Status {
    PENDING,
    SHIPPING,
    INTRANSIT,
    AVAILABLE,
    DELIVERED,
    RETURNED,
    DENIED,
    FORGOTTEN,
    CANCELLED;

    public static boolean contains(String state) {
        for (Status status : Status.values()) {
            if (status.name().equals(state)) {
                return true;
            }
        }
        return false;
    }
}
