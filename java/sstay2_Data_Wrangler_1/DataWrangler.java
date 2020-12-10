// --== CS400 File Header Information ==--
// Name: Sui Jiet Tay
// Email: sstay2@wisc.edu
// Team: IA
// Role: Data Wrangler 1
// TA: Mu Cai
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * This class implements the DataWranglerInterface
 * 
 * @author Sui Jiet Tay
 *
 */
public class DataWrangler implements DataWranglerInterface {

	// instance fields
//	protected static long latestUsedTransactionNumber = 0;
//	protected static long latestUsedUserNumber = 0;

	// Fields used to navigate the .csv files
	private static String transactionFile = "";
	private static String userInformationFile = "";
	private Path transactionCSVPath = Paths.get(".", "res/transaction.csv");
	private Path userInformationCSVPath = Paths.get(".", "res/userInformation.csv");

	// Data structures used to store added or removed data
	protected static Hashtable<String, UserAccount> userAccounts;
	protected static LinkedList<Transaction> transactions;

	/**
	 * This constructor initializes the data required for the application
	 */
	public DataWrangler() {
		
		// Instantiates new data structures and purges existing data
		userAccounts = new Hashtable<>();
		transactions = new LinkedList<>();

		// Loads data from all both .csv files
		this.loadDataFromCSV();
	}

	// Instance methods
	/**
	 * This helper method reads the .csv file in the application
	 * 
	 * @return true if .csv file is successfully read
	 */
	private boolean readCSVFile() {

		// Checks if transaction.csv can be read
		try {
			transactionFile = Files.readString(transactionCSVPath, StandardCharsets.UTF_8);

		} catch (IOException e) {

			return false;
		}

		// Checks of userInformation.csv can be read
		try {
			userInformationFile = Files.readString(userInformationCSVPath, StandardCharsets.UTF_8);

		} catch (IOException e) {

			return false;
		}

		return true;
	}

	/**
	 * This method allows the backend to save the changes into the .csv file
	 * 
	 * @return true if data is successfully saved into .csv file
	 */
	public boolean saveDataToCSV() {

		// Creates a StringJoiner object to join userInformation.csv
		StringJoiner join1 = new StringJoiner("\n");

		// Adds header of the .csv file to the string joiner
		join1.add("userID,name,hashedPassword,dateOfBirth,nationality,address,budgetCapPerDay,balance");

		// Iterates through all user accounts in the application to be written back into
		// the .csv file
		DataWrangler.userAccounts.values().forEach(account -> {

			String individualAccount = "\""+account.getUserID()+"\"" + "," + "\""+account.getName()+"\"" + "," + "\""+account.getHashedPassword()+ "\""
					+ "," + "\""+account.getDate()+"\"" + "," + "\""+account.getNationality()+"\"" + "," + "\""+account.getAddress()+"\"" + ","
					+ "\""+account.getBudgetCapPerDay()+"\"" + "," + "\""+account.getBalance()+"\"";

			join1.add(individualAccount);

		});

		// Checks if a string of concatenated csv can be written into the
		// userInformation.csv
		try {
			Files.writeString(this.userInformationCSVPath, join1.toString(), StandardCharsets.UTF_8);

		} catch (IOException e) {

			return false;
		}

		// Creates a StringJoiner object to join transaction.csv
		StringJoiner join2 = new StringJoiner("\n");

		// Adds header of the .csv file to the string joiner
		join2.add(
				"transactionStatus,userID,dateAndTime,transactionID,transactionDesctiption,amount,merchantName,paymentMethod,locationOfSpending");

		// Iterates through all transactions in the application to be written back into
		// the .csv file
		DataWrangler.transactions.forEach(transaction -> {

			String singleTransaction = "\""+transaction.getTransactionStatus()+"\"" + "," + "\""+transaction.getUserID()+"\"" + ","
					+ "\""+transaction.getDateTime()+"\"" + "," + "\""+transaction.getTransactionID() +"\""+ ","
					+ "\""+transaction.getTransactionDescription()+"\"" + "," + "\""+transaction.getAmount()+"\"" + ","
					+ "\""+transaction.getMerchantName()+"\"" + "," + "\""+transaction.getPaymentMethod()+"\"" + ","
					+ "\""+transaction.getLocationOfSpending()+"\"";

			join2.add(singleTransaction);

		});

		// Checks if a string of concatenated csv can be written into the
		// transaction.csv
		try {
			Files.writeString(this.transactionCSVPath, join2.toString(), StandardCharsets.UTF_8);

		} catch (IOException e) {

			return false;
		}

		return true;
	}

	/**
	 * This helper method checks if the data is successfully loaded from the .csv file
	 * 
	 * @return true if data is successfully loaded from the .csv file to the
	 *         application
	 */
	private boolean loadDataFromCSV() {

		// Checks if .csv files can be read
		if (!readCSVFile())
			return false;

		// Reads each line of csv from userInformation.csv into a Stream
		Stream<String> userInformationStream = DataWrangler.userInformationFile.lines();

		// Iterates through every line of csv to be loaded into application data
		// structure
		userInformationStream.forEach(singleLine -> {

			// Checks if the csv line is a header
			if (singleLine.startsWith("userID"))
				return;
			
			try {

				this.loadUserInformationDataToDataStructure(singleLine);
				
			}catch(Exception e) {
				
			}
			
		});

		// Reads each line of csv from transaction.csv into a Stream
		Stream<String> transactionStream = DataWrangler.transactionFile.lines();

		// Iterates through every line of csv to be loaded into application data
		// structure
		transactionStream.forEach(singleLine -> {

			// Checks if the csv line is a header
			if (singleLine.startsWith("transactionStatus"))
				return;

			try {
				this.loadTransactionDataToDataStructure(singleLine);
				
			}catch(Exception e) {
				
			}

		});

		return false;

	}


	/**
	 * This helper method checks if data from the transaction.csv file is successfully
	 * retrieved to be used in the application
	 * 
	 * @param singleLine a line of string read from a single row of transaction.csv
	 *                   file
	 * @return true if transaction data from transaction.csv is successfully
	 *         retrieved
	 */
	private boolean loadTransactionDataToDataStructure(String singleLine) {
		
		// parse data into an array
		String[] arrayData = this.splitCSVLine(singleLine, 9);
		
			LocalDateTime localDateTime = LocalDateTime.parse(arrayData[2], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			
			DataWranglerInterface.PaymentMethods paymentMethod = DataWranglerInterface.PaymentMethods
			.valueOf(arrayData[7]);
			
			// Creates a new transaction
			Transaction existingTransaction = new Transaction(arrayData[0],arrayData[1],localDateTime ,arrayData[3],arrayData[4],new BigDecimal(arrayData[5]),arrayData[6],paymentMethod,arrayData[8]);
			
			
			
			
			
			
			// Adds the new transaction object into the data structure of the respective
			// user account
			UserAccount user = DataWrangler.userAccounts.get(arrayData[1]);
			user.addTransaction(existingTransaction);

			// Updates the latest used transactionID that is being loaded into the
			// application
//			this.latestUsedTransactionID(existingTransaction.getTransactionID());

			// To perform GC by compiler
			existingTransaction = null;

		

		return true;
	}

	/**
	 * This method checks if data from the userInformation.csv file is successfully
	 * retrieved to be used in the application
	 * 
	 * @param singleLine a line of string read from a single row of
	 *                   userInformation.csv file
	 * @return true if data from the userInformation.csv file is successfully
	 *         retrieved
	 */
	private boolean loadUserInformationDataToDataStructure(String singleLine) {

		// Parse data into an array		
		String[] arrayData = this.splitCSVLine(singleLine, 8);
		
		
		LocalDate parsedDate = LocalDate.parse(arrayData[3], DateTimeFormatter.ISO_LOCAL_DATE);
		
		UserAccount existingUserAccount = new UserAccount(arrayData[0], arrayData[1],arrayData[2], parsedDate,arrayData[4],arrayData[5],new BigDecimal(arrayData[6]),new BigDecimal(arrayData[7]) );

		// Adds the new user information as a key value pair into the data structure
		DataWrangler.userAccounts.put(arrayData[0], existingUserAccount);

		// To perform GC by compiler
		existingUserAccount = null;

		return true;
	}
	
	/**
	 * split CSV line into n elements
	 *  - Code provided by Jerry Xu - 
	 * @param line line to split
	 * @param n number of elements
	 * @return
	 */
	private String[] splitCSVLine(String line, int n) {
	    String[] data = new String[n];
	    String[] quotes = line.split("\"");
	    //System.out.println(Arrays.toString(quotes));
	    int di = 0;
	    for (int i = 0; i < quotes.length; i++) {
	        int r = 1;  //line.charAt(0) == '\"' ? 0 : 1;
	        if (i % 2 == r) {
	            // This is a quote
	            data[di] = quotes[i];
	            di++;
	        } else {
	            // This is NOT a quote
	            String[] fields = quotes[i].split(",");
	            //System.out.println(Arrays.toString(fields));
	            for (String e:fields) {
	                if (!e.equals("")) {
	                    data[di] = e;
	                    di++;
	                }
	            }
	        }
	    }
	    return data;
	}

	/**
	 * This method adds a new user account into the application
	 * 
	 * @param userID          the userID of the user
	 * @param name            the name of the user
	 * @param password        the password of the user
	 * @param dateOfBirth     the date of birth of the user
	 * @param nationality     the nationality of the user
	 * @param address         the home address of the user
	 * @param budgetCapPerDay the budget ceiling of the user per day
	 * @param balance         the balance in the user's account
	 * @return true if a new user account is successfully added into the application
	 */
	@Override
	public boolean addUserAccount(String userID, String name, String password, LocalDate dateOfBirth,
			String nationality, String address, BigDecimal budgetCapPerDay, BigDecimal balance) {


		// Creates a userAccount object using passed parameters
		UserAccount userAccount = new UserAccount(userID, name, this.hashPassword(password), dateOfBirth, nationality,
				address, budgetCapPerDay, balance);

		// Checks if userID is already present in the application
		if (!DataWrangler.userAccounts.containsKey(userID)) {

			DataWrangler.userAccounts.put(userID, userAccount);
		}

		return true;
	}

	/**
	 * This method hashes the password of the user
	 * 
	 * @param password the password that is to be hashed
	 * @return concatenatedComplex the hashed password
	 */
	public String hashPassword(String password) {

		// Converts a password into a hashed complex
		String concatenatedComplex = "";
		try {
			byte[] hash = password.getBytes("UTF-8");
			for (byte x : hash) {

				if (x - 100 < 0) {
					char c = (char) (x);
					if (String.valueOf(c).equals("\'") || String.valueOf(c).equals("\""))
						c = 'c';
					concatenatedComplex += c;
				}

				concatenatedComplex += String.valueOf((int) x);
			}

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		String additionalStringComplex = "#ac";

		concatenatedComplex += additionalStringComplex;

		return concatenatedComplex;
	}

	/**
	 * This method removes the user account from the application
	 * 
	 * @param userID the userID of the user account
	 * @return true if the user account is removed from the application
	 */
	@Override
	public boolean removeUserAccount(String userID) {

		// Checks if userID is already present in the application
		if (DataWrangler.userAccounts.containsKey(userID)) {

			// Removes the userAccount object from the hash table
			DataWrangler.userAccounts.remove(userID);

			return true;
		}

		return false;

	}

	/**
	 * This method removes the user account from the application
	 * 
	 * @param user the userAccount object
	 * @return true if the user account is removed from the application
	 */
	@Override
	public boolean removeUserAccount(UserAccount user) {

		// Checks if userID is already present in the application
		if (DataWrangler.userAccounts.containsKey(user.getUserID())) {

			// Removes the userAccount object from the hash table
			DataWrangler.userAccounts.remove(user.getUserID());

			return true;
		}

		return false;
	}

	/**
	 * This method removes the user account and the user-related transactions from
	 * the application
	 * 
	 * @param userID the userID of the removed user account and user-related
	 *               transactions
	 * @return true if the user account and its user-related transactions are
	 *         successfully removed
	 */
	@Override
	public boolean removeUserAccountAndTransactions(String userID) {

		// Checks if userAccount object is present in the application
		if (!this.removeUserAccount(userID)) {

			return false;
		}

		// Iterates through all transactions in the application
		// Checks if the current userID matches the requested userID
		// Removes transaction from the linked list data structure
		DataWrangler.transactions.removeIf(transaction -> transaction.getUserID().equals(userID));

		return true;
	}

	/**
	 * This method removes the user account and the user-related transactions from
	 * the application
	 * 
	 * @param user the userAccount object
	 * @return true if the user account and its user-related transactions are
	 *         successfully removed
	 */
	@Override
	public boolean removeUserAccountAndTransactions(UserAccount user) {

		// Checks if userAccount object is present in the application
		if (!this.removeUserAccount(user)) {

			return false;
		}

		// Iterates through all transactions in the application
		// Checks if the current userID matches the requested userID
		// Removes transaction from the linked list data structure	
		DataWrangler.transactions.removeIf(transaction -> transaction.getUserID().equals(user.getUserID()));
			
		

		return true;
		

	}

	/**
	 * This method retrieves the user account of the user using their userID and
	 * password
	 * 
	 * @param userID   the userID of the user account
	 * @param password the password of the user account
	 * @return DataWrangler.userAccounts.get(userID) if user credentials are a
	 *         match, and if user account is found
	 * @return null if user credentials are invalid or user account is not found
	 */
	@Override
	public UserAccount getUserAccount(String userID, String password) {

		// Converts the inputed password to its hashed format
		String hashedPasswordMatcher = this.hashPassword(password);

		// Checks if userID is already present in the application
		if (!DataWrangler.userAccounts.containsKey(userID)) {

			return null;
		}

		// Checks if the userAccount has the same password credential
		if (DataWrangler.userAccounts.get(userID).getHashedPassword().equals(hashedPasswordMatcher)) {

			// Retrieve the userAccount object from the hash table
			return DataWrangler.userAccounts.get(userID);
		}

		return null;

	}

	/**
	 * This method checks if the userAccount can be found from the application
	 * 
	 * @param userAccountID userID of the user account
	 * @return true if the user account is found in the application
	 */
	@Override
	public boolean containsUserAccount(String userAccountID) {

		// Checks if the userAccount is present in the application
		return DataWrangler.userAccounts.containsKey(userAccountID);

	}

	/**
	 * This method checks if the userAccoount can be found from the application
	 * 
	 * @param user the userAcccount object
	 * @return true if the user account is found in the application
	 */
	@Override
	public boolean containsUserAccount(UserAccount user) {

		// Checks if the userAccount object is present in the application
		return DataWrangler.userAccounts.containsKey(user.getUserID());
	}

	/**
	 * This method checks if a transaction can be found from the application
	 * 
	 * @param transactionID the transactionID of the transaction
	 * @return true if the transaction is found in the application
	 */
	@Override
	public boolean containsTransaction(String transactionID) {

		// Iterates through all the transactions in the application
		for (Transaction t : DataWrangler.transactions) {

			// Checks if transactionID matches the searched transactionID
			if (t.getTransactionID().equals(transactionID)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This method checks if a transaction can be found from the application
	 * 
	 * @param transaction the transaction object
	 * @return true if the transaction is found in the application
	 */
	@Override
	public boolean containsTransaction(Transaction transaction) {

		// Checks if the transaction object is present in the application
		return DataWrangler.transactions.contains(transaction);
	}

	/**
	 * This method retrieves all user accounts in the application
	 * 
	 * @return userAccounts an array of all user accounts in the application
	 */
	@Override
	public UserAccount[] getAllUserAccounts() {

		// Converts all userAccounts into an array
		Object[] collect = DataWrangler.userAccounts.values().toArray();
		UserAccount[] userAccounts = Arrays.copyOf(collect, collect.length, UserAccount[].class);

		return userAccounts;

	}

	/**
	 * This method retrieves all transactions in the application
	 * 
	 * @return arrayTransactions an array of all transactions that is found in the
	 *         application
	 */
	@Override
	public Transaction[] getAllTransactions() {

		// Creates an ArrayList to append all transactions from the linked list
		ArrayList<Transaction> listTransactions = new ArrayList<>();

		// Stores all elements in the linked list into an ArrayList
		for (Transaction t : DataWrangler.transactions) {

			listTransactions.add(t);
		}

		// Converts all transactions into an array
		Transaction[] arrayTransactions = new Transaction[listTransactions.size()];
		arrayTransactions = listTransactions.toArray(arrayTransactions);

		return arrayTransactions;

	}

	public static void main(String args[]) {

		DataWrangler dw = new DataWrangler();

		dw.loadDataFromCSV();

//		
//		System.out.println(dw.hashPassword("thisPasswordMayNotBeStrong100"));
//		System.out.println(dw.hashPassword("Superman's Cape"));
//		System.out.println(dw.hashPassword("lazyMan100"));
//		System.out.println(dw.hashPassword("zebraOnTheGo7000"));
//		System.out.println(dw.hashPassword("LLonepp$$"));
//		System.out.println(dw.hashPassword("$$%TTO"));
//		System.out.println(dw.hashPassword("HumanZ!"));
//		System.out.println(dw.hashPassword("iAmHighInTheSky"));
//		System.out.println(dw.hashPassword("SamIsReal"));
//		System.out.println(dw.hashPassword("SamuelJackson"));
//		System.out.println(dw.hashPassword("Mr.MadMan"));
//		System.out.println(dw.hashPassword("IAMATHOME"));
//		System.out.println(dw.hashPassword("ThisIsNotATest"));
//		System.out.println(dw.hashPassword("33445566"));
//		System.out.println(dw.hashPassword("100020"));
//		System.out.println(dw.hashPassword("7676767676"));
//		System.out.println(dw.hashPassword("8888888888"));
//		System.out.println(dw.hashPassword("version?"));
//		System.out.println(dw.hashPassword("testing"));
//		System.out.println(dw.hashPassword("999999"));

//		dw.latestUsedUserID("C000167");
//		dw.latestUsedTransactionID("T000178");
//		dw.latestUsedTransactionID("T000177");

//		LocalDate date = LocalDate.of(1999, 1, 2);
//
//		try {
//			dw.addUserAccount("userIDxxx123", "sam", "xxxxwfwe", date, "American", "102N Orchard",
//					new BigDecimal("100.00"), new BigDecimal("1030.10"));
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//
//		LocalDateTime dateTime = LocalDateTime.of(1999, 1, 2, 0, 0);
//
//		UserAccount acc = dw.getUserAccount("C000001", "thisPasswordMayNotBeStrong100");
//
//		try {
//			acc.addTransaction("income",dateTime, "yolo", new BigDecimal("100.00"), "maxis",
//					DataWranglerInterface.PaymentMethods.DEBIT, "American");
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//
//		System.out.println(DataWranglerInterface.PaymentMethods.DEBIT);
//
//		DataWranglerInterface.PaymentMethods paymentMethod = DataWranglerInterface.PaymentMethods.valueOf("CASH_OUT");
//
//		System.out.println(paymentMethod);
//		
//
		

		
	}

}
