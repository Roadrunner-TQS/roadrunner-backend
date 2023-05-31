package pt.ua.deti.tqs.roadrunnerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RoadrunnerBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoadrunnerBackendApplication.class, args);
    }
}