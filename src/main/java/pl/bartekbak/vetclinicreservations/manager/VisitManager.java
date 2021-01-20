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
                .filter(visit -> visit.getDate().isEqual(date))
                .collect(Collectors.toList());
    }

    public String addVisit(Visit visit) {
        //check if data is complete
        String dataCompletion = validateVisitData(visit, false);
        if (!dataCompletion.isEmpty()) return dataCompletion;

        //check if date is available
        if (checkAvailability(visit)) {
            vetManager.addVisit(visit);
            repository.save(visit);
            return "Successfully created";
        } else {
            return "Date is unavailable";
        }
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
        LocalTime startOfVisit = visit.getTime();
        int duration = visit.getVisitDurationMinutes();

        List<Visit> visits = findVisitsOfVetInDate(vet.getId(), date)
                .stream()
                .filter(visit1 ->
                        periodIsColliding(startOfVisit, duration, visit1.getTime(), visit1.getVisitDurationMinutes()))
                .collect(Collectors.toList());
        return visits.isEmpty();
    }

    private boolean pointOfTimeIsBeetween(LocalTime point, LocalTime start, LocalTime end) {
        return start.isBefore(point) && end.isAfter(point);
    }

    private boolean periodIsColliding(LocalTime begin, int duration, LocalTime reference, int refDuration) {
        return pointOfTimeIsBeetween(begin, reference, reference.plusMinutes(refDuration))
                || pointOfTimeIsBeetween(begin.plusMinutes(duration), reference, reference.plusMinutes(refDuration));
    }

    private String validateVisitData(Visit visit, boolean isUpdate) {
        if (visit.getVet() == null) return "You need to choose Vet";
        if (visit.getDate() == null) return "You need to specify date";
        if (visit.getTime() == null) return "You need to specify Time";
        if (isUpdate && !validatePin(visit)) return "Invalid Pin";
        if (!CustomerIdExist(visit)) return "Invalid user Id";
        if (!checkIfVetExist(visit)) return "No such vet in database";

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
        if (visit.getVet() == null || visit.getVet().getId() == null) return false;
        Long id = visit.getVet().getId();
        return vetManager.findById(id) != null;
    }
}
