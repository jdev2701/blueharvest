package hussam.blueharvest.account;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.google.gson.Gson;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import hussam.blueharvest.account.exception.NotFoundException;
import hussam.blueharvest.account.exception.ServiceDownException;
import hussam.blueharvest.account.model.Account;
import hussam.blueharvest.account.model.Customer;
import hussam.blueharvest.account.model.Transaction;
import hussam.blueharvest.account.service.AccountService;
import hussam.blueharvest.account.service.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "feign.hystrix.enabled=true" })
@ContextConfiguration(classes = { AccountApplicationTests.LocalRibbonClientConfiguration.class })
@ActiveProfiles("test")
public class AccountApplicationTests {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private AccountService accountService;

	private Gson gson = new Gson();

	@ClassRule
	public static WireMockClassRule wiremock = new WireMockClassRule(wireMockConfig().dynamicPort());

	@TestConfiguration
	public static class LocalRibbonClientConfiguration {
		@Bean
		public ServerList<Server> ribbonServerList() {
			return new StaticServerList<>(new Server("localhost", wiremock.port()));
		}
	}

	@Test
	public void testCustomerService() {
		Customer expected = new Customer("Hussam", "Test");
		Customer saved = customerService.save(expected);
		assertEquals(expected.getName(), saved.getName());
		assertEquals(expected.getSurname(), saved.getSurname());
		assertNotEquals(0, expected.getId());

		List<Customer> customers = customerService.getAllCustomers();
		Customer customer = customers.get(customers.size() - 1);
		assertEquals(saved.getName(), customer.getName());
		assertEquals(saved.getSurname(), customer.getSurname());
		assertEquals(saved.getId(), customer.getId());

		Customer found = customerService.findCustomerById(saved.getId());
		assertEquals(saved.getName(), found.getName());
		assertEquals(saved.getSurname(), found.getSurname());
		assertEquals(saved.getId(), found.getId());
	}

	@Test
	public void testCreatingAccountWithNoBalance() throws NotFoundException, ServiceDownException {
		// Create account
		Customer customer = customerService.save(new Customer("Hussam", "Test2"));
		Account account = accountService.createAccount(new Account(customer.getId(), 0L));
		assertEquals(customer.getId(), account.getCustomerId());
		assertEquals(0, account.getBalance());

		// Register mock
		long transactionAmount = 1000;
		Transaction retval = new Transaction(account.getId(), transactionAmount);
		retval.setId(1);
		stubFor(post(urlMatching("/api/transactions")).willReturn(
				aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON).withBody(gson.toJson(retval))));

		// Test adding transaction
		Transaction transaction = accountService.addTransaction(account.getId(), transactionAmount);
		assertNotNull(transaction);
		assertEquals(account.getId(), transaction.getAccountId());
		assertEquals(transactionAmount, transaction.getTransactionAmount());
		assertEquals(transactionAmount, accountService.findAccountById(account.getId()).getBalance());

		// Register mock for account transactions
		stubFor(get(urlMatching("/api/account/" + account.getId() + "/transactions")).willReturn(aResponse().withStatus(HttpStatus.OK.value())
				.withHeader("Content-Type", MediaType.APPLICATION_JSON).withBody(gson.toJson(Collections.singletonList(retval)))));

		// Check account transactions
		List<Transaction> transactions = accountService.getAccountTransactions(account.getId());
		assertNotNull(transactions);
		assertEquals(1, transactions.size());
		assertEquals(transactionAmount, transactions.get(0).getTransactionAmount());
	}

	@Test
	public void testCreatingAccountWithBalance() throws ServiceDownException {
		long transactionAmount = 1000;

		// Create account
		Customer customer = customerService.save(new Customer("Hussam", "Test2"));
		Account account = new Account(customer.getId(), transactionAmount);
		account.setId(999);

		Transaction retval = new Transaction(account.getId(), transactionAmount);
		retval.setId(111);
		stubFor(post(urlMatching("/api/transactions")).willReturn(
				aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON).withBody(gson.toJson(retval))));

		account = accountService.createAccount(account);
		assertEquals(customer.getId(), account.getCustomerId());
		assertEquals(transactionAmount, account.getBalance());

		// Check via service
		assertEquals(accountService.findAccountById(account.getId()).getBalance(), transactionAmount);

		// Register mock for account transactions
		stubFor(get(urlMatching("/api/account/" + account.getId() + "/transactions"))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON)
						.withBody(gson.toJson(Collections.singletonList(new Transaction(account.getId(), transactionAmount))))));

		// Check account transactions
		List<Transaction> transactions = accountService.getAccountTransactions(account.getId());
		assertNotNull(transactions);
		assertEquals(1, transactions.size());
		assertEquals(transactionAmount, transactions.get(0).getTransactionAmount());
	}
}
