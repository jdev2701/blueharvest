package hussam.blueharvest.account.controller;

import java.util.List;

import javax.validation.Valid;

import hussam.blueharvest.account.model.AccountTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import hussam.blueharvest.account.constant.ErrorMessage;
import hussam.blueharvest.account.exception.NotFoundException;
import hussam.blueharvest.account.exception.ServiceDownException;
import hussam.blueharvest.account.model.Account;
import hussam.blueharvest.account.model.Customer;
import hussam.blueharvest.account.model.Transaction;
import hussam.blueharvest.account.service.AccountService;
import hussam.blueharvest.account.service.CustomerService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private AccountService accountService;
	private CustomerService customerService;

	@Autowired
	public AccountController(AccountService accountService, CustomerService customerService) {
		this.accountService = accountService;
		this.customerService = customerService;
	}

	/**
	 * Returns transaction history of the account
	 * 
	 * @param accountId
	 *            id of the account
	 * @return transaction history of the account
	 */
	@GetMapping("/{accountId}/transactions")
	public List<Transaction> getAccountTransactions(@PathVariable int accountId) {
		return accountService.getAccountTransactions(accountId);
	}

	@RequestMapping(value = "addTransaction")
	@ResponseBody
	public Transaction addNewTransaction(@RequestParam int accountId,@RequestParam long amount) throws NotFoundException, ServiceDownException {
		Transaction transaction = accountService.addTransaction(accountId, amount);
		if (transaction == null) {
			throw new ServiceDownException(ErrorMessage.SERVICE_NOT_AVAILABLE);
		}
		return transaction;
	}

	@RequestMapping(value = "createAccount")
	@ResponseBody
	public Account createAccount(@RequestParam int customerId,@RequestParam long initBalance, @RequestParam AccountTypes type) throws NotFoundException, ServiceDownException {
		Account input=new Account(customerId,initBalance,type);
		Customer customer = customerService.findCustomerById(input.getCustomerId());
		if (customer == null) {
			throw new NotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND);
		}

		Account account = accountService.createAccount(input);
		if (account == null) {
			throw new ServiceDownException(ErrorMessage.SERVICE_NOT_AVAILABLE);
		}
		return account;
	}
}
