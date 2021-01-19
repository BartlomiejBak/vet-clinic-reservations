package pl.bartekbak.vetclinicreservations.manager;

import org.springframework.stereotype.Service;
import pl.bartekbak.vetclinicreservations.entity.Vet;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.repository.VetRepository;

import java.util.List;

@Service
public class VetManager {

    private VetRepository repository;

    public VetManager(VetRepository repository) {
        this.repository = repository;
    }

    public Vet findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Vet> findAll() {
        return repository.findAll();
    }

    public Vet addVet(Vet vet) {
        return repository.save(vet);
    }

    public void deleteVetById(Long id) {
        repository.deleteById(id);
    }

    //todo
    public boolean checkAvailability(Visit visit) {
        return false;
    }

    //todo
    public void addVisit(Visit visit) {

    }
}
