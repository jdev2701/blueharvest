package hussam.blueharvest.transaction.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hussam.blueharvest.transaction.model.Transaction;
import hussam.blueharvest.transaction.repository.TransactionRepository;

@Service
public class TransactionService {

	private TransactionRepository transactionRepository;

	@Autowired
	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public Transaction save(Transaction account) {
		account.setTransactionDate(new Date());
		return transactionRepository.save(account);
	}

	public List<Transaction> findTransactionsByAccountId(int accountId) {
		return transactionRepository.findByAccountId(accountId);
	}

	public void deleteAll() {
		transactionRepository.deleteAll();
	}
}
