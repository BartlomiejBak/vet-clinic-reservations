package pl.bartekbak.vetclinicreservations.manager;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.bartekbak.vetclinicreservations.entity.Customer;
import pl.bartekbak.vetclinicreservations.entity.Vet;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.repository.VisitRepository;

import java.time.LocalDateTime;
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

    public Visit addVisit(Visit visit) {
        //check if available

        //add to Vet

        //add to Customer

        return repository.save(visit);
    }

    public void deleteVisitById(Long id) {
        repository.deleteById(id);
    }

    /*@EventListener(ApplicationReadyEvent.class)
    private void fillDatabase() {
        Customer firstCustomer = Customer.builder()
                .id(1234)
                .firstName("John")
                .lastName("Doe")
                .pin(4321)
                .build();
        Customer secondCustomer = Customer.builder()
                .id(2468)
                .firstName("Jan")
                .lastName("Nowak")
                .pin(2222)
                .build();

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
                .customer(firstCustomer)
                .vet(firstVet)
                .visitStart(LocalDateTime.of(2021, 5, 15, 11, 30))
                .build());
        addVisit(Visit.builder()
                .id(2L)
                .customer(secondCustomer)
                .vet(secondVet)
                .visitStart(LocalDateTime.of(2021, 5, 15, 11, 30))
                .build());
        addVisit(Visit.builder()
                .id(3L)
                .customer(secondCustomer)
                .vet(firstVet)
                .visitStart(LocalDateTime.of(2021, 5, 15, 12, 30))
                .build());
        addVisit(Visit.builder()
                .id(4L)
                .customer(firstCustomer)
                .vet(secondVet)
                .visitStart(LocalDateTime.of(2021, 5, 15, 12, 30))
                .build());
    }*/
}
