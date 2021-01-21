package pl.bartekbak.vetclinicreservations.manager;

import org.springframework.stereotype.Service;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.exceptions.DataCollisionException;
import pl.bartekbak.vetclinicreservations.exceptions.IncompleteDataException;
import pl.bartekbak.vetclinicreservations.exceptions.InvalidCredentialsException;
import pl.bartekbak.vetclinicreservations.exceptions.ResourceNotFoundException;
import pl.bartekbak.vetclinicreservations.repository.VisitRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitManager {
    private final VisitRepository repository;
    private final VetManager vetManager;

    public VisitManager(VisitRepository repository, VetManager vetManager) {
        this.repository = repository;
        this.vetManager = vetManager;
    }

    public Visit findById(Long id) {
        Optional<Visit> result = repository.findById(id);
        Visit visit;
        if (result.isPresent()) {
            visit = result.get();
        } else {
            throw new ResourceNotFoundException("Id not found");
        }
        return visit;
    }

    public List<Visit> findAll() {
        return repository.findAll();
    }

    public List<Visit> findVisitsOfVetInDate(Long vetId, LocalDate date) {
        List<Visit> visits = findVisitsOfVetInDateRaw(vetId, date);
        if (visits.isEmpty()) {
            throw new ResourceNotFoundException("No visits in date");
        } else {
            return visits;
        }
    }

    private List<Visit> findVisitsOfVetInDateRaw(Long vetId, LocalDate date) {
        return repository.findAll().stream()
                .filter(visit -> visit.getVet().getId().equals(vetId))
                .filter(visit -> visit.getDate().isEqual(date))
                .collect(Collectors.toList());
    }

    public String addVisit(Visit visit) {
        //check if data is complete
        validateVisitData(visit);
        //check if date is available
        if (checkIfBusy(visit)) throw new DataCollisionException("Date is unavailable");
        repository.save(visit);
        return "Successfully created";
    }

    public String updateVisit(Visit visit) {
        //check if data is complete
        validateVisitData(visit);
        //check if visit exist
        if (visitIdNotExist(visit)) throw new ResourceNotFoundException("No such visit in database");
        //check if date is available
        if (checkIfBusy(visit)) throw new DataCollisionException("Date is unavailable");
        repository.save(visit);
        return "Successfully updated";
    }

    public String deleteVisit(Visit visit) {
        //check if credentials are complete
        if (visit.getId() == null) throw new IncompleteDataException("You need to specify visit id");
        checkCredentials(visit);
        //check if exist
        if (visitIdNotExist(visit)) throw new ResourceNotFoundException("No such visit in database");
        repository.delete(visit);
        return "Successfully deleted";
    }

    private boolean checkIfBusy(Visit newVisit) {

        List<Visit> visitsFiltered = findVisitsOfVetInDateRaw(newVisit.getVet().getId(), newVisit.getDate())
                .stream()
                .filter(visit -> timeCollision(newVisit, visit))
                .filter(visit -> !visit.getId().equals(newVisit.getId()))
                .collect(Collectors.toList());

        return !visitsFiltered.isEmpty();
    }

    private boolean timeCollision(Visit newVisit, Visit oldVisit) {
        LocalTime oldStart = oldVisit.getTime();
        LocalTime oldEnd = oldStart.plusMinutes(oldVisit.getVisitDurationMinutes());

        LocalTime newStart = newVisit.getTime();
        LocalTime newEnd = newStart.plusMinutes(newVisit.getVisitDurationMinutes());

        return newStart.isAfter(oldStart) && newStart.isBefore(oldEnd)
                || newEnd.isAfter(oldStart) && newEnd.isBefore(oldEnd)
                || newStart.isBefore(oldStart) && newEnd.isAfter(oldEnd)
                || newStart.equals(oldStart) || newEnd.equals(oldEnd);
    }

    private void validateVisitData(Visit visit) {
        if (visit.getVet() == null) throw new IncompleteDataException("You need to choose vet");
        if (visit.getDate() == null) throw new IncompleteDataException("You need to specify date");
        if (visit.getTime() == null) throw new IncompleteDataException("You need to specify time");
        checkCredentials(visit);
        if (!checkIfVetExist(visit)) throw new ResourceNotFoundException("No such vet in database");
    }

    private void checkCredentials(Visit visit) {
        if (visit.getPin() == null || !pinMatchesPattern(visit)) throw new InvalidCredentialsException("Invalid Pin");
        if (visit.getCustomerId() == null || !customerIdMatchesPattern(visit)) throw new InvalidCredentialsException("Invalid user Id");
    }

    private boolean pinMatchesPattern(Visit visit) {
        return visit.getPin().matches("^[0-9]{4}$");
    }

    private boolean customerIdMatchesPattern(Visit visit) {
        return visit.getCustomerId().matches("^[0-9]{4}$");
    }

    private boolean visitIdNotExist(Visit visit) {
        try {
            findById(visit.getId());
            return false;
        } catch (RuntimeException e) {
            return true;
        }
    }

    private boolean checkIfVetExist(Visit visit) {
        if (visit.getVet() == null || visit.getVet().getId() == null) return false;
        Long id = visit.getVet().getId();
        return vetManager.findById(id) != null;
    }
}
