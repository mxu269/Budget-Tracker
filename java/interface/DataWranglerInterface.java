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

/**
 * This is an interface for the DataWrangler class
 * 
 * @author Sui Jiet Tay
 *
 */
public interface DataWranglerInterface {

	/**
	 * This enum stores the types of payment methods that can be used
	 * 
	 *
	 */
	public enum PaymentMethods {
		 CASH_OUT, DEBIT, PAYMENT, TRANSFER
		
		

	}

	/**
	 * This method adds a new user account to the dataset
	 * 
	 * @param userID 		  the userID of the user
	 * @param name            the name of the user
	 * @param password        the password of the user
	 * @param dateOfBirth     the date of birth of the user
	 * @param nationality     the nationality of the user
	 * @param address         the home address of the user
	 * @param budgetCapPerDay the budget ceiling of the user per day
	 * @param balance         the account balance of the user
	 * @return true if a new user account is successfully added
	 * 
	 * @throws Exception if userID long data type reaches maximum capacity
	 */
	public boolean addUserAccount(String userID, String name, String password, LocalDate dateOfBirth, String nationality,
			String address, BigDecimal budgetCapPerDay, BigDecimal balance);

	/**
	 * This method removes a userAccount from the dataset
	 * 
	 * @param userID the userID of the user
	 * @return true if the user account is successfully removed
	 */
	public boolean removeUserAccount(String userID);

	/**
	 * This method removes a userAccount from the dataset
	 * 
	 * @param user the user object of the user
	 * @return true if the user account is successfully removed
	 */
	public boolean removeUserAccount(UserAccount user);

	/**
	 * This method removes a userAccount, and its user-related transactions from the
	 * dataset
	 * 
	 * @param userID the userID of the user
	 * @return true if the user account and its transactions are successfully
	 *         removed
	 */
	public boolean removeUserAccountAndTransactions(String userID);

	/**
	 * This method removes a userAccount, and its user-related transactions from the
	 * dataset
	 * 
	 * @param user the user object of the user
	 * @return true if the user account and its transactions are successfully
	 *         removed
	 */
	public boolean removeUserAccountAndTransactions(UserAccount user);

	/**
	 * This method retrieves a userAccount from the exisitng dataset
	 * 
	 * @param userID   the userID of the user
	 * @param password the entered password of the user
	 * @return UserAccount the user account object of the user
	 */
	public UserAccount getUserAccount(String userID, String password);

	/**
	 * This method checks if a userAccount is in the dataset
	 * 
	 * @param userAccountID the userID of the user
	 * @return true if the user account is found in the dataset
	 */
	public boolean containsUserAccount(String userAccountID);

	/**
	 * This method checks if a userAccount is in the dataset
	 * 
	 * @param user the user object of the user
	 * @return true if the user account is found in the dataset
	 */
	public boolean containsUserAccount(UserAccount user);

	/**
	 * This method checks if a transaction is in the dataset
	 * 
	 * @param transactionID the transaction ID of the transaction
	 * @return true if the transaction is found in the dataset
	 */
	public boolean containsTransaction(String transactionID);

	/**
	 * This method checks if a transaction is in the dataset
	 * 
	 * @param transaction
	 * @return true if the transaction is found in the dataset
	 */
	public boolean containsTransaction(Transaction transaction);

	/**
	 * This method retrieves all userAccounts in the dataset
	 * 
	 * @return UserAccount[] an array of all the users in the dataset
	 */
	public UserAccount[] getAllUserAccounts();

	/**
	 * This method retrieves all transactions in the dataset
	 * 
	 * @return Transaction[] an array of all transactions in the dataset
	 */
	public Transaction[] getAllTransactions();

}
