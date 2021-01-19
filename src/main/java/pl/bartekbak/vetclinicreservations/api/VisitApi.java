package pl.bartekbak.vetclinicreservations.api;

import org.springframework.web.bind.annotation.*;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.manager.VisitManager;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitApi {

    private VisitManager manager;

    public VisitApi(VisitManager manager) {
        this.manager = manager;
    }

    @GetMapping("/all")
    public List<Visit> getVisits() {
        return manager.findAll();
    }

    @GetMapping
    public Visit getVisitById(@RequestParam Long id) {
        return manager.findById(id);
    }

    @GetMapping("/vet/{vetId}")
    public List<Visit> getVetVisitsForDate(@PathVariable Long vetId, @RequestBody LocalDate date) {
        return manager.findVisitsOfVetInDate(vetId, date);
    }

    @PostMapping
    public Visit addVisit(@RequestBody Visit visit) {
        visit.setId(0L);
        manager.addVisit(visit);
        return visit;
    }

    @PutMapping
    public void updateVisit(@RequestBody Visit visit) {
        manager.addVisit(visit);
    }

    @DeleteMapping
    public void deleteVisit(@RequestParam Long id) {
        manager.deleteVisitById(id);
    }


}
