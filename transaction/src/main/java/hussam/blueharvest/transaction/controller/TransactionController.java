package hussam.blueharvest.transaction.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hussam.blueharvest.transaction.model.Transaction;
import hussam.blueharvest.transaction.service.TransactionService;

/**
 * Endpoint for inserting new transaction
 */
@RestController
@RequestMapping("/api")
public class TransactionController {
	private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

	private TransactionService transactionService;

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	/**
	 * Returns transactions for the given customer account
	 * 
	 * @param accountId
	 *            id of the customer account
	 * @return transactions for the given account
	 */
	@GetMapping("/account/{accountId}/transactions")
	public List<Transaction> getAccountTransactions(@PathVariable int accountId) {
		log.info("Request received for geting transactions for account: " + accountId);
		return transactionService.findTransactionsByAccountId(accountId);
	}

	/**
	 * Inserts a new transaction. Does not validate if account exists
	 * 
	 * @param transaction
	 *            model object
	 * @return saved transaction model object
	 */
	@PostMapping("/transactions")
	private Transaction createTransaction(@RequestBody Transaction transaction) {
		log.info("Request received for creating transaction");
		return transactionService.save(transaction);
	}

	/**
	 * Removes all transaction history for all accounts
	 */
	@DeleteMapping("/transactions")
	private void createTransaction() {
		log.info("Request received for deleting transactions");
		transactionService.deleteAll();
	}
}
