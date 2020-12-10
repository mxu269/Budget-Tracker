// --== CS400 File Header Information ==--
// Name: Sui Jiet Tay
// Email: sstay2@wisc.edu
// Team: IA
// Role: Data Wrangler 1
// TA: Mu Cai
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Transaction implements Comparable<Transaction> {

	// transactionStatus,userID,dateAndTime,transactionID,transactionDesctiption,amount,merchantName,paymentMethod,locationOfSpending

	// Instance field
	private String transactionStatus;
	private String userID;
	private LocalDateTime dateTime;
	private String transactionID;
	private String transactionDescription;
	private BigDecimal amount;
	private String merchantName;
	private DataWranglerInterface.PaymentMethods paymentMethod;
	private String locationOfSpending;

	/**
	 * This constructor initializes fields of a new Transaction object
	 * 
	 * ----------------------- Warning:--------------------------------------------
	 * - Any class that is defined outside the date wrangler package should not use
	 * this constructor
	 * 
	 * - Constructing a new transaction should be done from the addTransaction()
	 * method in UserAccount.java
	 * ----------------------------------------------------------------------------
	 * 
	 * @param transactionStatus      the transaction status of the transaction
	 * @param userID                 the userID that made the transaction
	 * @param dateTime               the date and time of the made transaction
	 * @param transactionID          the transactionID of the transaction
	 * @param transactionDescription the transaction description of the transaction
	 * @param amount                 the amount made in the transaction
	 * @param merchantName           the name of the merchant of the transaction
	 * @param paymentMethod          the payment method used in the transaction
	 * @param locationOfSpending     the location of spending of the transaction
	 */
	public Transaction(String transactionStatus, String userID, LocalDateTime dateTime, String transactionID,
			String transactionDescription, BigDecimal amount, String merchantName,
			DataWranglerInterface.PaymentMethods paymentMethod, String locationOfSpending) {

		this.transactionStatus = transactionStatus;
		this.userID = userID;
		this.dateTime = dateTime;
		this.transactionID = transactionID;
		this.transactionDescription = transactionDescription;
		this.amount = amount;
		this.merchantName = merchantName;
		this.paymentMethod = paymentMethod;
		this.locationOfSpending = locationOfSpending;

	}

	// Getter methods
	/**
	 * This method retrieves the transaction status of the transaction
	 * 
	 * @return this.transactionStatus the transaction status in String
	 */
	public String getTransactionStatus() {

		return this.transactionStatus;

	}

	/**
	 * This method retrieves the userID that made the transaction
	 * 
	 * @return this.userID the userID in String
	 */
	public String getUserID() {

		return this.userID;

	}

	/**
	 * This method retrieves the date and time of the transaction
	 * 
	 * @return this.dateTime the date and time field represented by the
	 *         LocalDateTime class
	 */
	public LocalDateTime getDateTime() {

		return this.dateTime;
	}

	/**
	 * This method retrieves the transactionID of the transaction
	 * 
	 * @return this.transactionID the transactionID in String
	 */
	public String getTransactionID() {

		return this.transactionID;
	}

	/**
	 * This method retrieves the transaction description of the transaction
	 * 
	 * @return this.transactionDescription the transaction description in String
	 */
	public String getTransactionDescription() {

		return this.transactionDescription;

	}

	/**
	 * This method retrieves the amount of the made transaction
	 * 
	 * @return this.amount the amount in BigDecimal
	 */
	public BigDecimal getAmount() {

		return this.amount;

	}

	/**
	 * This method retrieves the name of the merchant of the made transaction
	 * 
	 * @return this.merchantName the name of the merchant in String
	 */
	public String getMerchantName() {

		return this.merchantName;

	}

	/**
	 * This method retrieves the payment method constant of the enum collection
	 * 
	 * @return this.paymentMethod the payment method as an enum constant
	 */
	public DataWranglerInterface.PaymentMethods getPaymentMethod() {

		return this.paymentMethod;

	}

	/**
	 * This method retrieves the locations of spending of the transaction
	 * 
	 * @return this.locationOfSpending the location of spending in String
	 */
	public String getLocationOfSpending() {

		return this.locationOfSpending;

	}

	// Setter methods
	/**
	 * This method sets the transaction status of the transaction
	 * 
	 * @return this.transactionStatus the transaction status in String
	 */
	public void setTransactionStatus(String transactionStatus) {

		this.transactionStatus = transactionStatus;

	}

	/**
	 * This method sets the userID that made the transaction
	 * 
	 * @return this.userID the userID in String
	 */
	public void setUserID(String userID) {

		this.userID = userID;

	}

	/**
	 * This method sets the date and time of the transaction
	 * 
	 * @return this.dateTime the date and time field represented by the
	 *         LocalDateTime class
	 */
	public void setDateTime(LocalDateTime dateTime) {

		this.dateTime = dateTime;
	}

	/**
	 * This method sets the transactionID of the transaction
	 * 
	 * @return this.transactionID the transactionID in String
	 */
	public void setTransactionID(String transactionID) {

		this.transactionID = transactionID;
	}

	/**
	 * This method sets the transaction description of the transaction
	 * 
	 * @return this.transactionDescription the transaction description in String
	 */
	public void setTransactionDescription(String transactionDescription) {

		this.transactionDescription = transactionDescription;

	}

	/**
	 * This method sets the amount of the made transaction
	 * 
	 * @return this.amount the amount in BigDecimal
	 */
	public void setAmount(BigDecimal amount) {

		this.amount = amount;

	}

	/**
	 * This method sets the name of the merchant of the made transaction
	 * 
	 * @return this.merchantName the name of the merchant in String
	 */
	public void setMerchantName(String merchantName) {

		this.merchantName = merchantName;

	}

	/**
	 * This method sets the payment method constant of the enum collection
	 * 
	 * @return this.paymentMethod the payment method as an enum constant
	 */
	public void setPaymentMethod(String paymentMethod) {

		this.paymentMethod = DataWranglerInterface.PaymentMethods.valueOf(paymentMethod);
		;

	}

	/**
	 * This method sets the locations of spending of the transaction
	 * 
	 * @return this.locationOfSpending the location of spending in String
	 */
	public void setLocationOfSpending(String locationSpending) {

		this.locationOfSpending = locationSpending;

	}

	/**
	 * This method overrides the toString method
	 * 
	 * @return this.transactionStatus + "," + this.userID + "," +
	 *         this.dateTime.toString() + "," + this.transactionID + "," +
	 *         this.transactionDescription + "," + this.amount.toString() + "," +
	 *         this.merchantName + "," + this.paymentMethod + "," +
	 *         this.locationOfSpending the string representation of a transaction
	 *         class
	 */
	@Override
	public String toString() {

		return this.transactionStatus + "," + this.userID + "," + this.dateTime.toString() + "," + this.transactionID
				+ "," + this.transactionDescription + "," + this.amount.toString() + "," + this.merchantName + ","
				+ this.paymentMethod + "," + this.locationOfSpending;
	}

	/**
	 * This method overrides the compareTo method
	 * 
	 * @param t the transaction object that is being compared
	 * @return this.transactionID.compareTo(t.getTransactionID()) the returned value
	 *         from the compareTo method in string class
	 * @return this.amount.compareTo(t.getAmount()) the returned value from the
	 *         compareTo method in BigDecimal class
	 */
	@Override
	public int compareTo(Transaction t) {

		// Checks if the account amount between two transactions are equal
		if (this.amount.compareTo(t.getAmount()) == 0) {

			return this.transactionID.compareTo(t.getTransactionID());
			
			// Checks if the account number between two transactions are not equal
		} else {

			return this.amount.compareTo(t.getAmount());
		}

	}

	public static void main(String args[]) {
//		String zoneId = "America/Chicago";
//		ZoneId zone = ZoneId.of(zoneId);
//		LocalDateTime x = LocalDateTime.now(zone);

//		LocalDateTime y = LocalDateTime.of(2021, 5, 1, 20, 20);
//		
//		System.out.println(x);
//		System.out.println(y);
//		
//	
//		LocalDateTime minusNanos = x.withNano(0).withSecond(0).withMinute(0).withHour(0);
//		LocalDate date = x.toLocalDate();
//		System.out.println(minusNanos);
//		System.out.println(date);

	}

}
