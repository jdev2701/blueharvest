package hussam.blueharvest.account.proxy;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import hussam.blueharvest.account.model.Transaction;

@FeignClient(name = "transaction-service", fallback = TransactionServiceFallback.class)
@RibbonClient(name = "transaction-service")
public interface TransactionServiceProxy {
	@GetMapping("/api/account/{accountId}/transactions")
	List<Transaction> getAccountTransactions(@PathVariable("accountId") int accountId);

	@PostMapping("/api/transactions")
	Transaction createTransaction(@RequestBody Transaction transaction);

	@DeleteMapping("/api/transactions")
	void deleteAllTransactions();
}
