package pl.bartekbak.vetclinicreservations.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.bartekbak.vetclinicreservations.entity.Vet;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.manager.VetManager;
import pl.bartekbak.vetclinicreservations.manager.VisitManager;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final VetManager vetManager;
    private final VisitManager visitManager;

    public DataLoader(VetManager vetManager, VisitManager visitManager) {
        this.vetManager = vetManager;
        this.visitManager = visitManager;
    }

    @Override
    public void run(String... args) throws Exception {
        int count = vetManager.findAll().size();
        if (count == 0) loadData();
    }

    private void loadData() {
        Vet firstVet = Vet.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
        Vet secondVet = Vet.builder()
                .id(2L)
                .firstName("Jan")
                .lastName("Nowak")
                .build();

        Visit aVisit = Visit.builder()
                .id(100L)
                .vet(firstVet)
                .customerId("5000")
                .pin("6000")
                .date(LocalDate.of(2021, 02, 11))
                .time(LocalTime.of(12, 00))
                .build();
        Visit bVisit = Visit.builder()
                .id(101L)
                .vet(firstVet)
                .customerId("5001")
                .pin("6001")
                .date(LocalDate.of(2021, 02, 11))
                .time(LocalTime.of(12, 20))
                .build();
        Visit cVisit = Visit.builder()
                .id(102L)
                .vet(firstVet)
                .customerId("5002")
                .pin("6002")
                .date(LocalDate.of(2021, 02, 11))
                .time(LocalTime.of(12, 40))
                .build();
        Visit dVisit = Visit.builder()
                .id(103L)
                .vet(secondVet)
                .customerId("5003")
                .pin("6003")
                .date(LocalDate.of(2021, 02, 11))
                .time(LocalTime.of(12, 00))
                .build();
        Visit eVisit = Visit.builder()
                .id(104L)
                .vet(secondVet)
                .customerId("5004")
                .pin("6004")
                .date(LocalDate.of(2021, 02, 11))
                .time(LocalTime.of(12, 20))
                .build();
        Visit fVisit = Visit.builder()
                .id(105L)
                .vet(secondVet)
                .customerId("5005")
                .pin("6005")
                .date(LocalDate.of(2021, 02, 11))
                .time(LocalTime.of(12, 40))
                .build();

        vetManager.addVet(firstVet);
        vetManager.addVet(secondVet);
        System.out.println("Loaded Vets");

        visitManager.addVisit(aVisit);
        visitManager.addVisit(bVisit);
        visitManager.addVisit(cVisit);
        visitManager.addVisit(dVisit);
        visitManager.addVisit(eVisit);
        visitManager.addVisit(fVisit);
        System.out.println("Loaded Visits");
    }
}
