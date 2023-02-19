package hussam.blueharvest.transaction.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Transaction {

	@Id
	@GeneratedValue
	private int id;
	private int accountId;
	private long transactionAmount;
	private Date transactionDate;

	public Transaction() {
	}

	public Transaction(int accountId, long transactionAmount) {
		this.accountId = accountId;
		this.transactionAmount = transactionAmount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public long getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(long transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
}
