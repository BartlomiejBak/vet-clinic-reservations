package pl.bartekbak.vetclinicreservations.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.manager.VisitManager;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitApi {

    private final VisitManager manager;

    public VisitApi(VisitManager manager) {
        this.manager = manager;
    }

    @ApiOperation(value = "Find all visits")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/all")
    public List<Visit> getVisits() {
        return manager.findAll();
    }

    @ApiOperation(value = "Find visit by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping
    public Visit getVisitById(@RequestParam Long id) {
        return manager.findById(id);
    }

    @ApiOperation(value = "Find visits of vet in date")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incomplete data"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/vet/{vetId}")
    public List<Visit> getVetVisitsForDate(@PathVariable Long vetId, @RequestBody LocalDate date) {
        return manager.findVisitsOfVetInDate(vetId, date);
    }

    @ApiOperation(value = "Add visit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created"),
            @ApiResponse(code = 400, message = "Incomplete data"),
            @ApiResponse(code = 401, message = "Invalid credentials"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 409, message = "Data collision"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping
    public String addVisit(@RequestBody Visit visit) {
        visit.setId(0L);
        return manager.addVisit(visit);
    }

    @ApiOperation(value = "Update visit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "Incomplete data"),
            @ApiResponse(code = 401, message = "Invalid credentials"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 409, message = "Data collision"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PutMapping
    public String updateVisit(@RequestBody Visit visit) {
        return manager.updateVisit(visit);
    }

    @ApiOperation(value = "Delete visit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 400, message = "Incomplete data"),
            @ApiResponse(code = 401, message = "Invalid credentials"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @DeleteMapping
    public String deleteVisit(@RequestBody Visit visit) {
        return manager.deleteVisit(visit);
    }


}
