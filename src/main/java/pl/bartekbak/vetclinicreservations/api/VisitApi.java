package pl.bartekbak.vetclinicreservations.api;

import org.springframework.web.bind.annotation.*;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.manager.VisitManager;

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

    @PostMapping
    public Visit addVisit(@RequestBody Visit visit) {
        return manager.addVisit(visit);
    }

    @PutMapping
    public Visit updateVisit(@RequestBody Visit visit) {
        return manager.addVisit(visit);
    }

    @DeleteMapping
    public void deleteVisit(@RequestParam Long id) {
        manager.deleteVisitById(id);
    }
}
