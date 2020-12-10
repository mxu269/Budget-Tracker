// --== CS400 File Header Information ==--
// Name: Zhantao Yang
// Email: zyang484@wisc.edu
// Team: IA
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: None

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.time.DayOfWeek.MONDAY; // beginning of the week
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * backend class that make use of data structures and pass data from data wrangler to frontend as needed
 */
public class BackEnd implements BackEndInterface {
	private final DataWrangler dataWrangler;
	private final HashTableMap<String, User> hashTableMap;

	/**
	 * default constructor that initializes fields and data
	 */
	public BackEnd() throws IOException {
		dataWrangler = new DataWrangler();
		hashTableMap = new HashTableMap<>();
		for (UserAccount userAccount : dataWrangler.getAllUserAccounts()) {
			hashTableMap.put(userAccount.getUserID(), new User(userAccount));
		}

		if (hashTableMap.size() == 0) throw new IOException("data file unreadable");
	}


	/**
	 * an inner class that contains a userAccount object and a corresponding 2-3-4 tree that stores transactions
	 * Although this is public static, it will only be used after BackEnd is initialized
	 */
	public static class User {
		public UserAccount userAccount;
		public TwoThreeFourTree<Transaction> transactionsTree;

		/**
		 * default constructor that initializes a user object with user account and tree
		 *
		 * @param userAccount that is stored in the User object
		 */
		public User(UserAccount userAccount) {
			this.userAccount = userAccount;
			this.transactionsTree = new TwoThreeFourTree<>(); // initialize empty tree

			for (Transaction transaction : userAccount.allTransactions()) {
				transactionsTree.insert(transaction);
			}

		}

		/**
		 * calls insert to the transaction tree in this object
		 *
		 * @param transaction to insert into the tree
		 * @return true if transaction added successfully, false otherwise(transaction already exist)
		 */
		public boolean add(Transaction transaction) {
			if (transaction == null) return false;

			this.userAccount.addTransaction(transaction);
			return this.transactionsTree.insert(transaction);
		}

		/**
		 * calls remove to the transaction tree in this object
		 *
		 * @param transaction to remove from the tree
		 * @return true if transaction deleted successfully, false otherwise(transaction does not exist)
		 */
		public boolean remove(Transaction transaction) {
			if (transaction == null) return false;

			boolean result = this.userAccount.removeTransaction(transaction);
			if (!result) return false;

			return transactionsTree.remove(transaction);
		}
	}

	/**
	 * get the userAccount object that has been logged in now
	 *
	 * @param username of the user to retrieve from
	 * @return the UserAccount Object of current user
	 */
	@Override
	public UserAccount getUser(String username) {
		return hashTableMap.get(username).userAccount;
	}

	/**
	 * get an array of transactions with the given limit by transaction amount ascending order
	 * example array: {1, 22, 220, 1000}
	 *
	 * @param username of the user to retrieve from
	 * @param max      the maximum number of transactions to return, negative for all
	 * @return array of transactions
	 */
	@Override
	public Transaction[] getAscendAmountTransactions(String username, int max) {
		Transaction[] transactions;
		LinkedList<Transaction> transactionLinkedList = hashTableMap.get(username).transactionsTree.toLinkedList();
		// sublist can also be used, but since sublist does not accept index out of bound, doing like this is better

		if (max < 0) transactions = new Transaction[transactionLinkedList.size()];
		else if (max > transactionLinkedList.size()) transactions = new Transaction[transactionLinkedList.size()];
		else transactions = new Transaction[max];

		ListIterator<Transaction> transactionListIter = transactionLinkedList.listIterator();
		for (int i = 0; i < transactions.length; i++) {
			transactions[i] = transactionListIter.next();
		}
		return transactions;
	}

	/**
	 * get an array of transactions with the given limit by transaction date descending order
	 * example order: {9/20/2020 13:20, 2/28/2020 1:20, 12/30/2019 19:20}
	 *
	 * @param username of the user to retrieve from
	 * @param max      the maximum number of transactions to return
	 * @return array of transactions
	 */
	@Override
	public Transaction[] getDateTimeTransactions(String username, int max) {
		Transaction[] transactions = hashTableMap.get(username).userAccount.allTransactions();
		// sort:
		// t1 t2 are reversed because LocalDateTime comparator is from earliest to latest(ascend), so reverse
		Arrays.sort(transactions, (t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));

		if (max < 0) return transactions; // return directly
		if (max > transactions.length) max = transactions.length;
		// if max has exceeded the length number of transactions, reassign with total length
		return Arrays.copyOfRange(transactions, 0, max);
	}

	/**
	 * get an array of transactions with the given limit by transaction name ascending order
	 * example order: {Amazon,Steam, Uber}, the first letter will ALWAYS be upper case(no lower) or number or signs etc.
	 * <p>
	 * notice unlike the getTypeTransactions and getLocationTransactions,
	 * this does NOT take a merchant name as a parameter, because
	 *
	 * @param username     of the user to retrieve from
	 * @param merchantName of the transaction to limit the result
	 * @param max          the maximum number of transactions to return
	 * @return array of transactions
	 */
	@Override
	public Transaction[] getNameTransactions(String username, String merchantName, int max) {
		Transaction[] transactions = hashTableMap.get(username).userAccount.allTransactions();
		if (merchantName != null) {
			Filter tempFilter = new Filter();
			tempFilter.setMerchantName(merchantName);
			transactions = tempFilter.getTransactions(transactions, null);
		}

		// sort:
		// will prioritize on sorting name of merchant, if merchant name equals, it will sort on amount/ID
		Arrays.sort(transactions, (t1, t2) -> {
			int cmp = t1.getMerchantName().compareToIgnoreCase(t2.getMerchantName());
			if (cmp != 0) return cmp;
			else return t1.compareTo(t2);
		});

		if (max < 0) return transactions; // return directly
		if (max > transactions.length) max = transactions.length;
		// if max has exceeded the length number of transactions, reassign with total length
		return Arrays.copyOfRange(transactions, 0, max);
	}

	/**
	 * get an array of transactions with the given limit of given transaction type ascending order
	 * if type is null, this will be sorted based on enum Type within max, which is provided by data wrangler
	 *
	 * @param username       of the user to retrieve from
	 * @param paymentMethods of transaction (payment method), if this is null, all type will be returned in type order
	 * @param max            the maximum number of transactions to return
	 * @return array of transactions
	 */
	@Override
	public Transaction[] getTypeTransactions(String username,
	                                         DataWranglerInterface.PaymentMethods paymentMethods, int max) {
		Transaction[] transactions = hashTableMap.get(username).userAccount.allTransactions();
		if (paymentMethods != null) {
			Filter tempFilter = new Filter();
			tempFilter.setPaymentMethod(paymentMethods);
			transactions = tempFilter.getTransactions(transactions, null);
		}

		// sort:
		Arrays.sort(transactions, (t1, t2) -> {
			int cmp = t1.getPaymentMethod().compareTo(t2.getPaymentMethod());
			if (cmp != 0) return cmp;
			else return t1.compareTo(t2);
		});

		if (max < 0) return transactions; // return directly
		if (max > transactions.length) max = transactions.length;
		// if max has exceeded the length number of transactions, reassign with total length
		return Arrays.copyOfRange(transactions, 0, max);
	}

	/**
	 * get an array of transactions with the given limit of given transaction location ascending order
	 * if location is null, this will be sorted based on location within max
	 *
	 * @param username of the user to retrieve from
	 * @param location of the transaction
	 * @param max      the maximum number of transactions to return
	 * @return array of transactions
	 */
	@Override
	public Transaction[] getLocationTransactions(String username, String location, int max) {
		Transaction[] transactions = hashTableMap.get(username).userAccount.allTransactions();
		if (location != null) {
			Filter tempFilter = new Filter();
			tempFilter.setLocationOfSpending(location);
			transactions = tempFilter.getTransactions(transactions, null);
		}

		// sort:
		Arrays.sort(transactions, (t1, t2) -> {
			int cmp = t1.getLocationOfSpending().compareToIgnoreCase(t2.getLocationOfSpending());
			if (cmp != 0) return cmp;
			else return t1.compareTo(t2);
		});

		if (max < 0) return transactions; // return directly
		if (max > transactions.length) max = transactions.length;
		// if max has exceeded the length number of transactions, reassign with total length
		return Arrays.copyOfRange(transactions, 0, max);
	}

	/**
	 * get the actual name of current user
	 *
	 * @param username of the user to retrieve from
	 * @return the name of current user
	 */
	@Override
	public String getName(String username) {
		return hashTableMap.get(username).userAccount.getName();
	}

	/**
	 * get one single transaction object, if passed in amount is null, this method will still work, but less efficient,
	 * passed in with amount preferred
	 *
	 * @param username      of the user to retrieve from
	 * @param amount        of the transaction
	 * @param transactionID of the transaction to retrieve from
	 * @return the transaction with given ID, null will be returned if no such transaction
	 */
	@Override
	public Transaction getTransaction(String username, BigDecimal amount, String transactionID) {
		UserAccount target = hashTableMap.get(username).userAccount;
		if (amount == null) { // if amount is not given, use linear search since it does not allow using tree search
			for (Transaction transaction : target.allTransactions()) {
				if (transaction.getTransactionID().compareTo(transactionID) == 0) {
					return transaction;
				}
			}
			return null;
		}

		// get the transaction with a dummy/fake transaction of same key (amount and transactionID)
		return hashTableMap.get(username).transactionsTree.search(new Transaction(null, null, null,
				transactionID, null, amount, null, null, null));
	}

	/**
	 * search the transaction by given keyword AND filter,
	 * if filter if null, this will ONLY search by keyword
	 *
	 * @param username of the user to retrieve from
	 * @param keywords to search in the transaction name
	 * @param filter   to filter the fields of transaction
	 * @return array of transaction that fits given keywords and filter
	 */
	@Override
	public Transaction[] search(String username, String keywords, Filter filter) {
		if (filter == null) filter = new Filter(); // create an empty filter
		return filter.getTransactions(getAscendAmountTransactions(username, -1), keywords);
	}

	/**
	 * search the transaction by given keyword AND filter,
	 * if filter if null, this will ONLY search by keyword
	 *
	 * @param username of the user to retrieve from
	 * @param keywords to search in the transaction name
	 * @param json     that contains the info of a filter, the amount can be both positive and negative
	 * @return array of transaction that fits given keywords and filter
	 */
	@Override
	public Transaction[] searchJson(String username, String keywords, String json) {
		Filter filter = new Filter(json);
		Transaction[] unfilteredTransactions = getAscendAmountTransactions(username, -1);
		String[] jsonSplitArray = json.substring(1, json.length() - 1).split(",");
		// split json into individual fields, further split into pairs
		for (String jsonComp : jsonSplitArray) {
			String[] temp = jsonComp.split(":");
			temp[0] = temp[0].replace("\"", "").strip();
			temp[1] = temp[1].replace("\"", "").strip();

			// if both are positive, no range for amount expense
			if (temp[0].compareToIgnoreCase("minAmount") == 0) {
				BigDecimal bigDecimalTemp = new BigDecimal(temp[1]);
				if (bigDecimalTemp.compareTo(new BigDecimal(0)) < 0) { // is negative
					filter.setMaxAmount(bigDecimalTemp.abs());
				} else filter.setMaxAmount(null);

			} else if (temp[0].compareToIgnoreCase("maxAmount") == 0) {
				BigDecimal bigDecimalTemp = new BigDecimal(temp[1]);
				if (bigDecimalTemp.compareTo(new BigDecimal(0)) < 0) { // is negative
					filter.setMinAmount(new BigDecimal(temp[1]).abs());
				} else filter.setMinAmount(null);
			}
			filter.setTransactionStatus("expense");
		}
		Transaction[] expenses = filter.getTransactions(unfilteredTransactions, null);

		// similarly, income
		filter = new Filter(json);  // reset filter
		for (String jsonComp : jsonSplitArray) {
			String[] temp = jsonComp.split(":");
			temp[0] = temp[0].replace("\"", "").strip();
			temp[1] = temp[1].replace("\"", "").strip();

			// if both are positive, no range for amount expense
			if (temp[0].compareToIgnoreCase("minAmount") == 0) {
				BigDecimal bigDecimalTemp = new BigDecimal(temp[1]);
				if (bigDecimalTemp.compareTo(new BigDecimal(0)) > 0) { // is positive
					filter.setMinAmount(bigDecimalTemp.abs());
				} else filter.setMinAmount(null);
			} else if (temp[0].compareToIgnoreCase("maxAmount") == 0) {
				BigDecimal bigDecimalTemp = new BigDecimal(temp[1]);
				if (bigDecimalTemp.compareTo(new BigDecimal(0)) > 0) { // is positive
					filter.setMaxAmount(new BigDecimal(temp[1]).abs());
				} else filter.setMaxAmount(null);
			}
			filter.setTransactionStatus("income");
		}
		Transaction[] incomes = filter.getTransactions(unfilteredTransactions, null);

		// concat transaction arrays in stream
		Stream<Transaction> allTransactions =
				Stream.concat(Arrays.stream(expenses).sorted(Collections.reverseOrder()), Arrays.stream(incomes));
		return allTransactions.toArray(Transaction[]::new); // transform back to transaction array
	}

	/**
	 * search the transaction by given filter, if filter is null, ALL UNSORTED transactions will be retrieve from
	 *
	 * @param username of the user to retrieve from
	 * @param filter   to filter the fields of transaction
	 * @return array of transactions that fits given filter
	 */
	@Override
	public Transaction[] filter(String username, Filter filter) {
		if (filter == null) return hashTableMap.get(username).userAccount.allTransactions();
		return filter.getTransactions(getAscendAmountTransactions(username, -1), null);
	}

	/**
	 * remove the transaction based on ID
	 *
	 * @param username   of the user to retrieve from
	 * @param transactID unique ID of transaction
	 * @return true if removal successful, false if transaction does not exist
	 */
	@Override
	public boolean removeTransaction(String username, String transactID) {
		Transaction transaction = getTransaction(username, null, transactID);
		return removeTransaction(username, transaction);
	}

	/**
	 * remove the given transaction
	 *
	 * @param username    of the user to retrieve from
	 * @param transaction that needs to be removed
	 * @return true if removal successful, false if transaction does not exist
	 */
	@Override
	public boolean removeTransaction(String username, Transaction transaction) {
		User user = hashTableMap.get(username);
		boolean result = user.remove(transaction);
		if (result) user.userAccount.setBalance(user.userAccount.getBalance().
				add(new BigDecimal(transaction.getTransactionStatus().compareToIgnoreCase("EXPENSE") == 0 ? 1 : -1).
						multiply(transaction.getAmount()))); // update balance
		return result;
	}

	/**
	 * insert a new transaction as provided, if the date field is not filled, it will be filled with system time
	 *
	 * @param username    of the user to retrieve from
	 * @param transaction transaction that wants to be inserted
	 * @return true if inserted, false if it already exist
	 * @throws NoSuchElementException   if transaction is exceeding budget
	 * @throws IllegalArgumentException if total spending exceeds 80% of budget,
	 *                                  notice that it will still be inserted while giving such warning
	 */
	@Override
	public boolean insertTransaction(String username, Transaction transaction) throws IOException {
		if (transaction.getDateTime() == null) transaction.setDateTime(LocalDateTime.now());
		transaction.setDateTime(transaction.getDateTime().withNano(0));
		transaction.setDateTime(transaction.getDateTime().withSecond(0));
		User user = hashTableMap.get(username);
		// check if warning needs to be casted
		boolean result = user.add(transaction);
		if (result) user.userAccount.setBalance(user.userAccount.getBalance().
				add(new BigDecimal(transaction.getTransactionStatus().compareToIgnoreCase("EXPENSE") == 0 ? -1 : 1).
						multiply(transaction.getAmount()))); // update balance
		if (result && getPercentSpent(username, transaction.getDateTime().toLocalDate()) >= 1)
			// multiply by number of days in month
			throw new NoSuchElementException("transaction exceeding budget");
		if (result && getPercentSpent(username, transaction.getDateTime().toLocalDate()) >= .8)
			throw new IllegalArgumentException("inserted successfully, but spending exceeded 80 percent of budget");
		save();
		return result;
	}

	/**
	 * remove the user based on ID(username)
	 *
	 * @param username of the user to remove
	 * @return true if the user that has been removed, false if user does not exist
	 */
	@Override
	public boolean removeUser(String username) {
		User result = hashTableMap.remove(username);
		if (result == null) return false;
		return dataWrangler.removeUserAccount(username);
	}

	/**
	 * remove the given user
	 *
	 * @param userAccount that needs to be removed
	 * @return true if removal successful, false if user does not exist
	 */
	@Override
	public boolean removeUser(UserAccount userAccount) {
		return removeUser(userAccount.getUserID());
	}

	/**
	 * insert a new user as provided, if the date field is not filled, it will be filled with 2000/01/01
	 *
	 * @return true if added successfully, false if it already exist
	 * @throws NullPointerException if userID or password is null
	 */
	@Override
	public boolean addUser(String userID, String name, String password, LocalDate dateOfBirth, String nationality,
	                       String address, BigDecimal budgetCapPerDay, BigDecimal balance) throws IOException {
		if (userID == null || password == null) throw new NullPointerException("username cannot be null");
		if (dateOfBirth == null) dateOfBirth = LocalDate.of(2000, 1, 1);
		dataWrangler.addUserAccount(
				userID, name, password, dateOfBirth, nationality, address, budgetCapPerDay, balance);
		save();
		return hashTableMap.put(userID, new User(dataWrangler.getUserAccount(userID, password)));
		// by retrieving from data wrangler, it assures they reference to the same object
	}

	/**
	 * insert a new user as provided, all other fields will be set to default:
	 * DOB=2000/01/01, nationality=unknown, address=unknown, budget=-1, balance=0
	 *
	 * @param username of the account
	 * @param password of the account
	 * @param name     actual name of the user
	 * @return true if added successfully, false if it already exist
	 */
	@Override
	public boolean addUser(String username, String password, String name) throws IOException {
		// preset to default value
		return addUser(username, name, password, LocalDate.of(2000, 1, 1),
				"unknown", "unknown", new BigDecimal(-1), new BigDecimal(0));
	}

	/**
	 * check if the transaction by given keyword AND filter exist,
	 * if filter if null, this will ONLY check by keyword
	 *
	 * @param username of the user to retrieve from
	 * @param keywords to search in the transaction name
	 * @param filter   to filter the fields of transaction
	 * @return true if exist
	 */
	@Override
	public boolean containsTransaction(String username, String keywords, Filter filter) {
		return search(username, keywords, filter).length != 0;
		// if the transaction array returned by search is not 0, it contains at least one
	}

	/**
	 * check if the transaction by given filter exist
	 *
	 * @param username of the user to retrieve from
	 * @param filter   to filter the fields of transaction
	 * @return array of transactions that fits given filter
	 */
	@Override
	public boolean containsFilterTransaction(String username, Filter filter) {
		return filter(username, filter).length != 0;
	}

	/**
	 * set the budget from Current month, negative when not set(no limit)
	 * Also notice that in backend the daily budget will be set,
	 * for example, if given date 12/21/2020 and budget 310, daily budget will be 310/31=10,
	 * then Feb. of 2021 will have budget of 10*30=300
	 *
	 * @param username of the user to retrieve from
	 * @param amount   of maximum spending
	 * @throws NoSuchElementException if amount set as budget is 0
	 */
	@Override
	public void setBudget(String username, BigDecimal amount) throws IOException {
		if (amount.compareTo(new BigDecimal(0)) == 0) throw new NoSuchElementException("cannot set with 0");
		hashTableMap.get(username).userAccount.setBudgetCapPerDay(amount.
				divide(new BigDecimal(LocalDate.now().lengthOfMonth()), RoundingMode.HALF_UP));
		// calculate monthly budget to daily
		save();
	}

	/**
	 * get the percent of spending in month of given month, notice ONLY spending will be counted (income does not count)
	 * 0 will be returned if NO budget set (negative)
	 *
	 * @param username of the user to retrieve from
	 * @param date     that the month it is in
	 * @return percent of spending over budget
	 */
	@Override
	public double getPercentSpent(String username, LocalDate date) {
		BigDecimal budgetPerDay = hashTableMap.get(username).userAccount.getBudgetCapPerDay();
		if (budgetPerDay.compareTo(new BigDecimal(0)) < 0) return 0; // no budget set
		// get the amount of spending this month, divide by the budget of the month
		// budget of this month is given by getBudgetCapPerDay times number of days in this month
		// finally, round and return as double
		return getAmount(username, date).divide(budgetPerDay.
				multiply(new BigDecimal(date.lengthOfMonth())), RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * get the total spending of the month of given date
	 *
	 * @param username of the user to retrieve from
	 * @param date     that the month it is in
	 * @return the total amount of spending
	 */
	@Override
	public BigDecimal getAmount(String username, LocalDate date) {
		Filter filter = new Filter();
		filter.setMinDate(LocalDate.of(date.getYear(), date.getMonthValue(), 1));
		filter.setMaxDate(LocalDate.of(date.getYear(), date.getMonthValue(), date.lengthOfMonth()));
		// set range of filter

		Transaction[] filteredTransactions =
				filter.getTransactions(hashTableMap.get(username).userAccount.allTransactions(), null);
		BigDecimal totalTransactions = new BigDecimal(0);

		// iterate through ALL transactions in given range and search for spending
		for (Transaction filteredTransaction : filteredTransactions) {
			if (filteredTransaction.getTransactionStatus().compareToIgnoreCase("Expense") == 0) {
				// check if is spending
				totalTransactions = totalTransactions.add(filteredTransaction.getAmount());
			}
		}
		return totalTransactions;
	}

	/**
	 * get the amount of spending for each of 7 days
	 *
	 * @param username of the user to retrieve from
	 * @return an array of size 7 that contains daily summary of current week
	 */
	@Override
	public BigDecimal[] getDailySumPerWeek(String username) {
		BigDecimal[] dailySum = new BigDecimal[7];
		Filter filter = new Filter();
		LocalDate tempDate = LocalDate.now().with(previousOrSame(MONDAY));
		// adjust date to the monday of the current week
		for (int i = 0; i < 7; i++) {
			filter.setMinDate(tempDate.plusDays(i));
			filter.setMaxDate(tempDate.plusDays(i));
			// add days of the week
			BigDecimal tempSum = new BigDecimal(0);
			for (Transaction transaction :
					filter.getTransactions(hashTableMap.get(username).userAccount.allTransactions(), null)) {
				if (transaction.getTransactionStatus().compareToIgnoreCase("expense") == 0)
					tempSum = tempSum.add(transaction.getAmount());
			} // iterate through all transactions and add the spending only
			dailySum[i] = tempSum;

		}
		return dailySum;
	}

	/**
	 * checks that the username matches with the password
	 *
	 * @param username of the user to retrieve from
	 * @param password of the user
	 * @return true if information correctly matches, false otherwise
	 */
	@Override
	public boolean validate(String username, String password) {
		try {
			return hashTableMap.get(username).userAccount.getHashedPassword().
					compareTo(dataWrangler.hashPassword(password)) == 0;
		} catch (NoSuchElementException n) { // user not found
			return false;
		}

	}

	/**
	 * checks time and save to csv, by restricting save time, it improves efficiency of program, since save method is
	 * not very efficient
	 *
	 * @throws IOException when saving unsuccessfully (will ONLY be caused by IOException, which is usually unfixable)
	 */
	private void save() throws IOException {
		Boolean result = null;
		// save only between 6 am and noon
		if (LocalTime.now().isAfter(LocalTime.MIDNIGHT.plusHours(6)) && LocalTime.now().isBefore(LocalTime.NOON))
			result = dataWrangler.saveDataToCSV();
		if (result != null && !result) throw new IOException("Unsolvable problem occurs while interacting with data");
		// if save occurs but is unsuccessful, the only possibility is IOException as stated by DataWrangler
	}

	/**
	 * forcefully save regardless of time
	 *
	 * @return true if saved, false if an unfixable IOException occurred
	 */
	public boolean forceSave() {
		return dataWrangler.saveDataToCSV();
	}
}
