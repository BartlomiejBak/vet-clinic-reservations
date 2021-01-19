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

    public String addVisit(Visit visit) {
        //check if data is complete

        //check if date is available

        //create pin

        //add to Vet

        //add to Customer

        repository.save(visit);
        return "pin is: ";
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
        return "";
    }

    //always true, as neither authentication nor authorization is required
    public boolean validate(int id, int pin) {
        return true;
    }

    private int createPin(int id) {
        return 1234;
    }

    private String validateVisitData(Visit visit, boolean isUpdate) {

        return "false";
    }
}
