package pl.bartekbak.vetclinicreservations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartekbak.vetclinicreservations.entity.Visit;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
}
