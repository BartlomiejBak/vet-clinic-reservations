package pl.bartekbak.vetclinicreservations.manager;

import org.springframework.stereotype.Service;
import pl.bartekbak.vetclinicreservations.entity.Vet;
import pl.bartekbak.vetclinicreservations.exceptions.ResourceNotFoundException;
import pl.bartekbak.vetclinicreservations.repository.VetRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VetManager {

    private final VetRepository repository;

    public VetManager(VetRepository repository) {
        this.repository = repository;
    }

    public Vet findById(Long id) {
        Optional<Vet> result = repository.findById(id);
        Vet vet;
        if (result.isPresent()) {
            vet = result.get();
        } else {
            throw new ResourceNotFoundException("Id not found");
        }
        return vet;
    }

    public List<Vet> findAll() {
        return repository.findAll();
    }

    public Vet addVet(Vet vet) {
        return repository.save(vet);
    }

}
