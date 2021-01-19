package pl.bartekbak.vetclinicreservations.manager;

import org.springframework.stereotype.Service;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.repository.VisitRepository;

import java.util.List;

@Service
public class VisitManager {
    private VisitRepository repository;

    public VisitManager(VisitRepository repository) {
        this.repository = repository;
    }

    public Visit findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Visit> findAll() {
        return repository.findAll();
    }

    public void addVisit(Visit visit) {
        //check if date is available

        //validate id and pin

        //add to Vet

        //add to Customer

        repository.save(visit);
    }

    public void deleteVisitById(Long id) {
        //check if exist

        //validate id and pin

        //delete from Vet

        //delete from Customer

        repository.deleteById(id);
    }

    //always true, as neither authentication nor authorization is required
    public boolean validate(int id, int pin) {
        return true;
    }

    /*@EventListener(ApplicationReadyEvent.class)
    private void fillDatabase() {
        Vet firstVet = Vet.builder()
                .firstName("Martin")
                .lastName("Vet")
                .build();
        Vet secondVet = Vet.builder()
                .firstName("Paula")
                .lastName("Vet")
                .build();

        addVisit(Visit.builder()
                .id(1L)
                .vet(firstVet)
                .visitStart(LocalDateTime.of(2021, 5, 15, 11, 30))
                .build());
        addVisit(Visit.builder()
                .id(2L)
                .vet(secondVet)
                .visitStart(LocalDateTime.of(2021, 5, 15, 11, 30))
                .build());
        addVisit(Visit.builder()
                .id(3L)
                .vet(firstVet)
                .visitStart(LocalDateTime.of(2021, 5, 15, 12, 30))
                .build());
        addVisit(Visit.builder()
                .id(4L)
                .vet(secondVet)
                .visitStart(LocalDateTime.of(2021, 5, 15, 12, 30))
                .build());
    }*/
}
