package hussam.blueharvest.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import hussam.blueharvest.account.model.Account;
import hussam.blueharvest.account.model.Customer;
import hussam.blueharvest.account.service.AccountService;
import hussam.blueharvest.account.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private AccountService accountService;
	private CustomerService customerService;

	@Autowired
	public CustomerController(AccountService accountService, CustomerService customerService) {
		this.accountService = accountService;
		this.customerService = customerService;
	}

	/**
	 * Lists all existing customers
	 * 
	 * @return all existing customers
	 */
	@GetMapping
	public List<Customer> getAllCustomers() {
		return customerService.getAllCustomers();
	}

	/**
	 * Returns a specific customer
	 * 
	 * @param id
	 *            of the customer
	 * @return requested customer
	 */
	@GetMapping("/{id}")
	public Customer getCustomer(@PathVariable int id) {
		return customerService.findCustomerById(id);
	}

	/**
	 * Displays accounts of given customer
	 * 
	 * @param id
	 *            of the customer
	 * @return accounts of given customer
	 */
	@GetMapping("/{id}/accounts")
	public List<Account> getAccounts(@PathVariable int id) {
		return accountService.findAccountsByCustomerId(id);
	}

	@RequestMapping(value = "save")
	@ResponseBody
	public Customer saveCustomer(@RequestParam String name,@RequestParam String surname) {
		Customer customer=new Customer(name,surname);
		return customerService.save(customer);
	}
}
