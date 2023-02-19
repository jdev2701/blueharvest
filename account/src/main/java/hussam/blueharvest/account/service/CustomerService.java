package hussam.blueharvest.account.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hussam.blueharvest.account.model.Customer;
import hussam.blueharvest.account.repository.CustomerRepository;

@Service
public class CustomerService {

	private CustomerRepository customerRepository;

	@Autowired
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public List<Customer> getAllCustomers() {
		List<Customer> customers = new ArrayList<>();
		customerRepository.findAll().forEach(customers::add);
		return customers;
	}

	public Customer findCustomerById(int id) {
		return customerRepository.findById(id).orElse(null);
	}

	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}
}
