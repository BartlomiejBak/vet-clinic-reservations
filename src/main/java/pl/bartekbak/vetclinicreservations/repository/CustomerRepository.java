package pl.bartekbak.vetclinicreservations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartekbak.vetclinicreservations.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Integer> {
}
