package hussam.blueharvest.account.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import hussam.blueharvest.account.exception.NotFoundException;
import hussam.blueharvest.account.model.Account;
import hussam.blueharvest.account.model.Customer;
import hussam.blueharvest.account.service.AccountService;
import hussam.blueharvest.account.service.CustomerService;

@Component
@Profile("default")
public class DataLoader implements ApplicationRunner {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AccountService accountService;

	/**
	 * Insert some initial dummy data
	 */
	public void run(ApplicationArguments args) throws NotFoundException {
		accountService.deleteAllTransactions();
		Customer customer = customerService.save(new Customer("Hussam", "Genady"));
		Account account = accountService.createAccount(new Account(customer.getId(), 1000L));
		if (account != null) {
			accountService.addTransaction(account.getId(), -400);
		}
	}
}
