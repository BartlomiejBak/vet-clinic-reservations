package pl.bartekbak.vetclinicreservations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartekbak.vetclinicreservations.entity.Vet;

@Repository
public interface VetRepository extends JpaRepository <Vet, Long> {
}
