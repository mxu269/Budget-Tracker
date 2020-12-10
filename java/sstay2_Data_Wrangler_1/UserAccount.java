// --== CS400 File Header Information ==--
// Name: Sui Jiet Tay
// Email: sstay2@wisc.edu
// Team: IA
// Role: Data Wrangler 1
// TA: Mu Cai
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * This class defines a user account
 * 
 * @author Sui Jiet Tay
 *
 */
public class UserAccount {

// userID,name,hashedPassword,dateOfBirth,nationality,address,budgetCapPerDay,balance

	// instance variable
	private String userID;
	private String name;
	private String hashedPassword;
	private LocalDate dateOfBirth;
	private String nationality;
	private String address;
	private BigDecimal budgetCapPerDay;
	private BigDecimal balance;

	private LinkedList<Transaction> userTransactions = new LinkedList<>();

	/**
	 * This constructor initializes the fields in the UserAccount object
	 * ----------------------- Warning:--------------------------------------------
	 * - Any class that is defined outside the date wrangler package should not use
	 * this constructor
	 * 
	 * - Constructing a new user account should be done from the addUserAccount()
	 * method in DataWrangler.java
	 * ----------------------------------------------------------------------------
	 * 
	 * @param userID          the userID of the user
	 * @param name            the name of the user
	 * @param hashedPassword  the password of the user
	 * @param dateOfBirth     the date of birth of the user
	 * @param nationality     the nationality of the user
	 * @param address         the home address of the user
	 * @param budgetCapPerDay the budget ceiling of the user per day
	 * @param balance         the balance in the user account
	 */
	public UserAccount(String userID, String name, String hashedPassword, LocalDate dateOfBirth, String nationality,
			String address, BigDecimal budgetCapPerDay, BigDecimal balance) {

		this.userID = userID;
		this.name = name;
		this.hashedPassword = hashedPassword;
		this.dateOfBirth = dateOfBirth;
		this.nationality = nationality;
		this.address = address;
		this.budgetCapPerDay = budgetCapPerDay;
		this.balance = balance;

	}

	// Instance methods
	/**
	 * This method adds a new transaction made by the user account
	 * 
	 * @param transaction Transaction object
	 */
	public void addTransaction(Transaction transaction) {

		// Adds the transaction to the user account
		this.userTransactions.add(transaction);

		// Adds the new transaction globally to the application
		DataWrangler.transactions.add(transaction);
	}

//	/**
//	 * This method adds a new transaction made by the user account
//	 * 
//	 * @param transactionStatus      the transaction status of the transaction
//	 * @param dateTime               the date and time of the transaction
//	 * @param transactionDescription the transaction description of the transaction
//	 * @param amount                 the amount of the made transaction
//	 * @param merchantName           the name of the merchant of the transaction
//	 * @param paymentMethod          the payment method used in the transaction
//	 * @param locationOfSpending     the location of spending of the transaction
//	 * @throws Exception
//	 */
//	public void addTransaction(String transactionStatus, LocalDateTime dateTime, String transactionDescription,
//			BigDecimal amount, String merchantName, DataWranglerInterface.PaymentMethods paymentMethod,
//			String locationOfSpending) throws Exception {
//
//		// Increments static transactionID for a new transaction number
//		DataWrangler.latestUsedTransactionNumber++;
//
//		// Appends the new transaction number into the transactionID
//		String transactionID = String.valueOf(DataWrangler.latestUsedTransactionNumber);
//
//		// Checks the number of leading zero that is left to append to the transactionID
//		int numberOfLeadingZeroLeft = 6 - transactionID.length();
//
//		// Throws an exception if the 6 digit transaction number within the transactionID is full
//		if (numberOfLeadingZeroLeft < 0) {
//			throw new Exception("You have exceeded the 6 digit userID number limit");
//		}
//
//		// Appends the leading zeros the the transactionID
//		String leadingZeros = "";
//		for (int i = 0; i < numberOfLeadingZeroLeft; i++) {
//
//			leadingZeros += "0";
//		}
//
//		// Appends all components to make the transactionID
//		transactionID = "T" + leadingZeros + transactionID;
//
//		// Creates a new transaction object
//		Transaction transaction = new Transaction(transactionStatus, this.userID, dateTime, transactionID,
//				transactionDescription, amount, merchantName, paymentMethod, locationOfSpending);
//
//		// Adds the transaction object into the user account data structure
//		this.userTransactions.add(transaction);
//
//		// Adds the transaction object globally into the application data structure
//		DataWrangler.transactions.add(transaction);
//	}

	/**
	 * This method removes a transaction made by the user account
	 * 
	 * @param transactionID the transactionID of the transaction
	 * @return true if the transaction in the user account is successfully removed
	 */
	public boolean removeTransaction(String transactionID) {

		// Iterates through all transactions in the user account to find a match to be removed
		for (Transaction t : this.userTransactions) {

			if (t.getTransactionID().equals(transactionID)) {

				// Removes the transaction object into the user account data structure
				this.userTransactions.remove(t);
				
				// Removes the transaction object globally into the application data structure
				DataWrangler.transactions.remove(t);

				return true;
			}

		}

		return false;
	}

	/**
	 * This method removes a transaction made by the user account
	 * 
	 * @param transaction transaction object
	 * @return true if the transaction in the user account is successfully removed
	 */
	public boolean removeTransaction(Transaction transaction) {

		// Removes the transaction object into the user account data structure
		this.userTransactions.remove(transaction);

		// Removes the transaction object globally into the application data structure
		return DataWrangler.transactions.remove(transaction);

	}

	/**
	 * This method retrieves all transactions made by the user
	 * 
	 * @return arrayTransactions an array of all transactions made by the user
	 */
	public Transaction[] allTransactions() {

		// Creates an ArrayList to store a list of transactions
		ArrayList<Transaction> listTransactions = new ArrayList<>();

		// Iterates through all transactions in the user account to be appended to a list
		for (Transaction t : this.userTransactions) {

			listTransactions.add(t);
		}

		// Converts a list of transactions into an array
		Transaction[] arrayTransactions = new Transaction[listTransactions.size()];
		arrayTransactions = listTransactions.toArray(arrayTransactions);

		// Returns the array of transactions
		return arrayTransactions;

	}

	/**
	 * This method provides backend the option to retrieve a filtered Stream of
	 * transaction objects
	 * 
	 * @param filtered the stream object of the filtered transactions
	 * @return arrayTransactions an array of the filtered transactions
	 */
	public Transaction[] getSearchedTransaction(Stream<Transaction> filtered) {

		// Converts the filtered stream of transactions into an array
		Object[] array = filtered.toArray();
		Transaction[] arrayTransactions = Arrays.copyOf(array, array.length, Transaction[].class);

		// Returns the array of filtered transactions
		return arrayTransactions;

	}

	/**
	 * This method retrieves a transaction from the existing dataset using a
	 * transactionID as parameter
	 * 
	 * @param transactionID the transactionID of the transaction
	 * @return t the transaction the is searched
	 * @return null if the transaction is not found in the user account
	 */
	public Transaction getSearchedTransaction(String transactionID) {

		// Iterates through all the transactions to find a match to be returned
		for (Transaction t : this.userTransactions) {

			if (t.getTransactionID().equals(transactionID)) {

				return t;
			}
		}

		return null;

	}

	/**
	 * This method retrieves the userID of the user account
	 * 
	 * @return this.userID the userID of the user account
	 */
	public String getUserID() {

		return this.userID;
	}

	/**
	 * This method retrieves the name of the user account
	 * 
	 * @return this.name the name of the user
	 */
	public String getName() {

		return this.name;
	}

	/**
	 * This method retrieves the hashed password of the user account
	 * 
	 * @return this.hashedPassword the hashed password of the user's password
	 */
	public String getHashedPassword() {

		return this.hashedPassword;
	}

	/**
	 * This method retrieves the date of birth of the user
	 * 
	 * @return this.dateOfBirth the date of birth of the user
	 */
	public LocalDate getDate() {

		return this.dateOfBirth;
	}

	/**
	 * This method retrieves the nationality of the user
	 * 
	 * @return this.nationality the nationality of the user
	 */
	public String getNationality() {

		return this.nationality;
	}

	/**
	 * This method retrieves the home address of the user
	 * 
	 * @return this.address the home address of the user
	 */
	public String getAddress() {
		return this.address;

	}

	/**
	 * This method retrieves the budget ceiling of the user per day
	 * 
	 * @return this.budgetCapPerDay the budget ceiling of the user per day
	 */
	public BigDecimal getBudgetCapPerDay() {
		return this.budgetCapPerDay;

	}

	/**
	 * This method retrieves the balance of the user account
	 * 
	 * @return this.balance the balance in the user's account
	 */
	public BigDecimal getBalance() {
		return this.balance;

	}
	
	
	// Setter methods
	/**
	 * This method sets the userID of the user account
	 * 
	 * @return this.userID the userID of the user account
	 */
	public void setUserID(String userID) {

		this.userID = userID;
	}

	/**
	 * This method sets the name of the user account
	 * 
	 * @return this.name the name of the user
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * This method sets the hashed password of the user account
	 * 
	 * @return this.hashedPassword the hashed password of the user's password
	 */
	public void setHashedPassword(String hashedPassword) {

		this.hashedPassword = hashedPassword;
	}

	/**
	 * This method sets the date of birth of the user
	 * 
	 * @return this.dateOfBirth the date of birth of the user
	 */
	public void setDate(LocalDate dateOfBirth) {

		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * This method sets the nationality of the user
	 * 
	 * @return this.nationality the nationality of the user
	 */
	public void setNationality(String nationality) {

		this.nationality = nationality;
	}

	/**
	 * This method sets the home address of the user
	 * 
	 * @return this.address the home address of the user
	 */
	public void setAddress(String address) {
		
		this.address = address;

	}

	/**
	 * This method sets the budget ceiling of the user per day
	 * 
	 * @return this.budgetCapPerDay the budget ceiling of the user per day
	 */
	public void setBudgetCapPerDay(BigDecimal budgetCapPerDay) {
		
		this.budgetCapPerDay = budgetCapPerDay;

	}

	/**
	 * This method sets the balance of the user account
	 * 
	 * @return this.balance the balance in the user's account
	 */
	public void setBalance(BigDecimal balance) {
		
		this.balance = balance;

	}

}
