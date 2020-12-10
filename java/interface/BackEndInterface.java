// Name: Zhantao Yang, Xin Su
// Email: zyang484@wisc.edu, xsu57@wisc.edu
// Team: IA
// TA: Mu Cai
// Lecturer: Florian Heimerl, Gary Dahl
// Notes to Grader: Completed and used by both backends to avoid ambiguity and inconsistency,
// BackEnd code will be implemented independently
// also note that this is called FrontEndInterface on proposal,
// but we decided to call it BackEndInterface since it is implemented by backend

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface that is implemented by backend to expose methods to frontend
 */
public interface BackEndInterface {
	/**
	 * get the userAccount object that has been logged in now
	 *
	 * @return the UserAccount Object of current user
	 */
	UserAccount getUser(String username);

	/**
	 * get an array of transactions with the given limit by transaction amount ascending order
	 * example array: {1, 22, 220, 1000}
	 *
	 * @param max the maximum number of transactions to return
	 * @return array of transactions
	 */
	Transaction[] getAscendAmountTransactions(String username, int max);

	/**
	 * get an array of transactions with the given limit by transaction date descending order
	 * example order: {9/20/2020 13:20, 2/28/2020 1:20, 12/30/2019 19:20}
	 *
	 * @param max the maximum number of transactions to return
	 * @return array of transactions
	 */
	Transaction[] getDateTimeTransactions(String username, int max);

	/**
	 * get an array of transactions with the given limit by transaction name ascending order
	 * example order: {Amazon,Steam, Uber}, the first letter will ALWAYS be upper case(no lower) or number or signs etc.
	 *
	 * @param max          the maximum number of transactions to return
	 * @param merchantName of the transaction to limit the result
	 * @return array of transactions
	 */
	Transaction[] getNameTransactions(String username, String merchantName, int max);

	/**
	 * get an array of transactions with the given limit of given transaction type ascending order
	 * if type is null, this will be sorted based on enum Type within max, which is provided by data wrangler
	 *
	 * @param paymentMethods of transaction
	 * @param max            the maximum number of transactions to return
	 * @return array of transactions
	 */
	Transaction[] getTypeTransactions(String username, DataWranglerInterface.PaymentMethods paymentMethods, int max);

	/**
	 * get an array of transactions with the given limit of given transaction location ascending order
	 * if location is null, this will be sorted based on location within max
	 *
	 * @param location of the transaction
	 * @param max      the maximum number of transactions to return
	 * @return array of transactions
	 */
	Transaction[] getLocationTransactions(String username, String location, int max);

	/**
	 * get the actual name of current user
	 *
	 * @return the name of current user
	 */
	String getName(String username);

	/**
	 * get one single transaction object, if passed in amount is null, this method will still work, but less efficient,
	 * passed in with amount preferred
	 *
	 * @param amount        of the transaction
	 * @param transactionID of the transaction to retrieve
	 * @return the transaction with given ID
	 */
	Transaction getTransaction(String username, BigDecimal amount, String transactionID);

	/**
	 * search the transaction by given keyword AND filter,
	 * if filter if null, this will ONLY search by keyword
	 *
	 * @param keywords to search in the transaction name
	 * @param filter   to filter the fields of transaction
	 * @return array of transaction that fits given keywords and filter
	 */
	Transaction[] search(String username, String keywords, Filter filter);

	/**
	 * search the transaction by given keyword AND filter,
	 * if filter if null, this will ONLY search by keyword
	 *
	 * @param keywords to search in the transaction name
	 * @param json   that contains the info of a filter, the amount can be both positive and negative
	 * @return array of transaction that fits given keywords and filter
	 */
	Transaction[] searchJson(String username, String keywords, String json);

	/**
	 * search the transaction by given filter
	 *
	 * @param filter to filter the fields of transaction
	 * @return array of transactions that fits given filter
	 */
	Transaction[] filter(String username, Filter filter);

	/**
	 * remove the transaction based on ID
	 *
	 * @param transactID unique ID of transaction
	 * @return true if removal successful, false if transaction does not exist
	 */
	boolean removeTransaction(String username, String transactID);

	/**
	 * remove the given transaction
	 *
	 * @param transaction that needs to be removed
	 * @return true if removal successful, false if transaction does not exist
	 */
	boolean removeTransaction(String username, Transaction transaction);

	/**
	 * insert a new transaction as provided, if the date field is not filled, it will be filled with system time
	 *
	 * @param transaction transaction that wants to be inserted
	 * @return true if inserted, false if it already exist
	 * @throws java.util.NoSuchElementException if transaction is refused due to exceeding budget
	 * @throws IllegalArgumentException         if total spending exceeds 80% of budget,
	 *                                          notice that it will still be inserted while giving such warning
	 */
	boolean insertTransaction(String username, Transaction transaction) throws IOException;

	/**
	 * remove the user based on ID
	 *
	 * @return true if removal successful, false if does not exist
	 */
	boolean removeUser(String username);

	/**
	 * remove the given user
	 *
	 * @param userAccount that needs to be removed
	 * @return true if removal successful, false if does not exist
	 */
	boolean removeUser(UserAccount userAccount);

	/**
	 * insert a new user as provided, if the date field is not filled, it will be filled with 2000/01/01
	 *
	 * @return true if added successfully, false if it already exist
	 */
	public boolean addUser(String userID, String name, String password, LocalDate dateOfBirth,
	                       String nationality, String address, BigDecimal budgetCapPerDay, BigDecimal balance) throws IOException;

	/**
	 * insert a new user as provided, all other fields will be set to default:
	 * DOB=2000/01/01, nationality=unknown, address=unknown, budget=-1, balance=0
	 *
	 * @param username of the account
	 * @param password of the account
	 * @param name actual name of the user
	 * @return true if added successfully, false if it already exist
	 */
	boolean addUser(String username, String password, String name) throws IOException;

	/**
	 * check if the transaction by given keyword AND filter exist,
	 * if filter if null, this will ONLY check by keyword
	 *
	 * @param keywords to search in the transaction name
	 * @param filter   to filter the fields of transaction
	 * @return true if exist
	 */
	boolean containsTransaction(String username, String keywords, Filter filter);

	/**
	 * check if the transaction by given filter exist,
	 *
	 * @param filter to filter the fields of transaction
	 * @return array of transactions that fits given filter
	 */
	boolean containsFilterTransaction(String username, Filter filter);

	/**
	 * set the budget from current month, -1 when not set(no limit)
	 *
	 * @param amount of maximum spending
	 */
	void setBudget(String username, BigDecimal amount) throws IOException;

	/**
	 * get the percent of spending in month of given DATE, notice ONLY spending will be counted (income does not count)
	 *
	 * @param date that the month it is in
	 * @return percent of spending over budget
	 */
	double getPercentSpent(String username, LocalDate date);

	/**
	 * get the total spending of the month of given date
	 *
	 * @param date that the month it is in
	 * @return the total amount of spending
	 */
	BigDecimal getAmount(String username, LocalDate date);

	/**
	 * get the amount of spending for each of 7 days
	 *
	 * @return an array of size 7 that contains daily summary of current week
	 */
	BigDecimal[] getDailySumPerWeek(String username);

	/**
	 * checks that the username matches with the password
	 *
	 * @param username of the user
	 * @param password of the user
	 * @return true if information correctly matches, false otherwise
	 */
	boolean validate(String username, String password);

	/**
	 * forcefully save data to csv without checking time
	 *
	 * @return true if saved, false if an unfixable IOException occurred
	 */
	boolean forceSave();
}
