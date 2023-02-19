package hussam.blueharvest.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hussam.blueharvest.account.constant.ErrorMessage;
import hussam.blueharvest.account.exception.NotFoundException;
import hussam.blueharvest.account.model.Account;
import hussam.blueharvest.account.model.Transaction;
import hussam.blueharvest.account.proxy.TransactionServiceProxy;
import hussam.blueharvest.account.repository.AccountRepository;

@Service
public class AccountService {

	private AccountRepository accountRepository;
	private TransactionServiceProxy transactionServiceProxy;

	@Autowired
	public AccountService(AccountRepository accountRepository, TransactionServiceProxy transactionServiceProxy) {
		this.accountRepository = accountRepository;
		this.transactionServiceProxy = transactionServiceProxy;
	}

	public List<Account> findAccountsByCustomerId(int customerId) {
		return accountRepository.findByCustomerId(customerId);
	}

	public Account findAccountById(int accountId) {
		return accountRepository.findById(accountId).orElse(null);
	}

	public Account createAccount(Account account) {
		account.setId(0);
		account.setBalance(0);
		account = accountRepository.save(account);

		if (account.getInitialBalance() != 0) {
			try {
				Transaction transaction = addTransaction(account.getId(), account.getInitialBalance());
				if (transaction == null) {
					accountRepository.deleteById(account.getId());
					return null;
				}
			} catch (NotFoundException ignored) {
				// This can never happen
			}
		}

		return findAccountById(account.getId());
	}

	public Transaction addTransaction(int accountId, long transactionAmount) throws NotFoundException {
		Account account = findAccountById(accountId);
		if (account == null) {
			throw new NotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND);
		}

		Transaction transaction = transactionServiceProxy.createTransaction(new Transaction(account.getId(), transactionAmount));
		if (transaction != null) {
			account.addBalance(transaction.getTransactionAmount());
			accountRepository.save(account);
		}

		return transaction;
	}

	public void deleteAllTransactions() {
		transactionServiceProxy.deleteAllTransactions();
	}

	public List<Transaction> getAccountTransactions(int accountId) {
		return transactionServiceProxy.getAccountTransactions(accountId);
	}
}
