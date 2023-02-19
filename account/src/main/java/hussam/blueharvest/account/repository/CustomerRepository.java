package hussam.blueharvest.account.repository;

import org.springframework.data.repository.CrudRepository;

import hussam.blueharvest.account.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
