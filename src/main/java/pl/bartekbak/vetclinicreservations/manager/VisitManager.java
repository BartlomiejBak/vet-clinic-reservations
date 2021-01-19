package pl.bartekbak.vetclinicreservations.manager;

import org.springframework.stereotype.Service;
import pl.bartekbak.vetclinicreservations.entity.Vet;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.repository.VisitRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitManager {
    private VisitRepository repository;
    private VetManager vetManager;

    public VisitManager(VisitRepository repository, VetManager vetManager) {
        this.repository = repository;
        this.vetManager = vetManager;
    }

    public Visit findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Visit> findAll() {
        return repository.findAll();
    }

    public List<Visit> findVisitsOfVetInDate(Long vetId, LocalDate date) {
        return repository.findAll().stream()
                .filter(visit -> visit.getVet().getId() == vetId)
                .filter(visit -> visit.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public String addVisit(Visit visit) {
        //check if data is complete

        //check if date is available

        //create pin

        //add to Vet

        //add to Customer

        repository.save(visit);
        return "Successfully created";
    }

    public String updateVisit(Visit visit) {
        //check if data is complete

        //check if date is available

        //validate id and pin

        //add to Vet

        //add to Customer

        repository.save(visit);
        return "Successfully updated";
    }

    public String deleteVisitById(Long id) {
        //check if exist

        //validate id and pin

        //delete from Vet

        //delete from Customer

        repository.deleteById(id);
        return "Successfully deleted";
    }

    private boolean checkAvailability(Visit visit) {
        Vet vet = visit.getVet();
        LocalDate date = visit.getDate();
        LocalTime time = visit.getTime();
        LocalTime endOfVisit = time.plusMinutes(visit.getEstimatedVisitDurationMinutes());
        List<Visit> visits = findVisitsOfVetInDate(vet.getId(), date)
                .stream()
                .filter(visit1 -> visit1.getTime().isAfter(endOfVisit)
                && visit1.getTime().plusMinutes(visit.getEstimatedVisitDurationMinutes()).isBefore(time))
                .collect(Collectors.toList());
        return visits.isEmpty();
    }

    private String validateVisitData(Visit visit, boolean isUpdate) {
        if (visit.getVet() == null) return "You need to choose Vet";
        if (!checkIfVetExist(visit)) return "No such vet in database";
        if (visit.getDate() == null) return "You need to specify date";
        if (visit.getTime() == null) return "You need to specify Time";
        if (isUpdate && visit.getPin() == null) return "Invalid Pin";
        if (!CustomerIdExist(visit)) return "Invalid user Id";

        return "";
    }

    private boolean validatePin(Visit visit) {
        return visit.getPin().matches("^[0-9]{4}$");
    }

    private boolean CustomerIdExist(Visit visit) {
        return visit.getCustomerId().matches("^[0-9]{4}$");
    }

    private boolean checkIfVisitIdExist(Visit visit) {
        return findById(visit.getId()) != null;
    }

    private boolean checkIfVetExist(Visit visit) {
        Long id = visit.getVet().getId();
        return vetManager.findById(id) != null;
    }
}
