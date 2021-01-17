package pl.bartekbak.vetclinicreservations.manager;

import org.springframework.stereotype.Service;
import pl.bartekbak.vetclinicreservations.entity.Customer;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.repository.CustomerRepository;

@Service
public class CustomerManager {
    CustomerRepository repository;

    public CustomerManager(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer findById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Customer addCustomer(Customer customer) {
        return repository.save(customer);
    }

    //todo
    public boolean checkAvailability(Visit visit) {
        return false;
    }

    //todo
    public void addVisit(Visit visit) {

    }

}
