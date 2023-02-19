package hussam.blueharvest.account.proxy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import hussam.blueharvest.account.model.Transaction;

@Component
public class TransactionServiceFallback implements TransactionServiceProxy {
	@Override
	public List<Transaction> getAccountTransactions(int accountId) {
		return new ArrayList<>();
	}

	@Override
	public Transaction createTransaction(Transaction transaction) {
		return null;
	}

	@Override
	public void deleteAllTransactions() {
	}
}
