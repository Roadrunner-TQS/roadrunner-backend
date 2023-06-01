package pt.ua.deti.tqs.roadrunnerbackend.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.StateRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class Updater {
    private final PackageRepository packagesRepository;
    private final StateRepository stateRepository;

    public Updater(PackageRepository packagesRepository, StateRepository stateRepository) {
        this.packagesRepository = packagesRepository;
        this.stateRepository = stateRepository;
    }

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void updateAVAILABLE() {
        log.info("Updater -- Update -- request received");
        List<Package> packagesAVAILABLE = packagesRepository.findAllByStatus(Status.AVAILABLE);
        for (Package p : packagesAVAILABLE) {
            long time = System.currentTimeMillis() - p.getTimestamp();
            if (time > 1000 * 30) { // 1 min
                p.setStatus(Status.FORGOTTEN);
                p.setTimestamp(System.currentTimeMillis());
                packagesRepository.save(p);
                State state = new State(UUID.randomUUID(), System.currentTimeMillis(), Status.FORGOTTEN);
                stateRepository.save(state);
                p.getStates().add(state);
                p.sort();
                packagesRepository.save(p);
            }
            log.info("Updater -- Update -- packages AVAILABLE updated");
        }
    }
    @Scheduled(cron = "0 0/2 * 1/1 * ?")
    public void updateSHIPPING() {
        List<Package> packagesSHIPPING = packagesRepository.findAllByStatus(Status.SHIPPING);
        for (Package p : packagesSHIPPING) {
            long time = System.currentTimeMillis() - p.getTimestamp();
            if (time > 1000 * 60 * 2) { // 1 min
                p.setStatus(Status.INTRANSIT);
                p.setTimestamp(System.currentTimeMillis());
                packagesRepository.save(p);
                State state = new State(UUID.randomUUID(), System.currentTimeMillis(), Status.INTRANSIT);
                stateRepository.save(state);
                p.getStates().add(state);
                p.sort();
                packagesRepository.save(p);
            }
            log.info("Updater -- Update -- packages SHIPPING updated");
        }
    }
}
