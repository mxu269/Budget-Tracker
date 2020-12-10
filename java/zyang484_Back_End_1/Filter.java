// --== CS400 File Header Information ==--
// Name: Zhantao Yang
// Email: zyang484@wisc.edu
// Team: IA
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: None


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/**
 * a filter that helps to search results that matches the desired content,
 * note that ANY field can be set to null if it has no limit
 * Also notice that you can set max to be smaller then min,
 * but it will be meaningless since there will be no result matches
 */
public class Filter implements FilterInterface {
	private LocalDate minDate;
	private LocalDate maxDate;

	private LocalTime minTime;
	// note that only hour, minutes and second stored will be compared, date will NOT be compared for this field
	private LocalTime maxTime;

	private String minTransactionID;
	private String maxTransactionID;

	private String transactionDescription; // find description that CONTAINS given key

	private BigDecimal minAmount;
	private BigDecimal maxAmount;

	private String merchantName; // find merchant name that CONTAINS given key

	private DataWranglerInterface.PaymentMethods paymentMethod; // find the payment method that MATCHES EXACTLY

	private String locationOfSpending; // find the location that CONTAINS given key

	private String transactionStatus; // find the payment method that MATCHES EXACTLY ignore cases

	/**
	 * a default constructor that sets all value to null, this is an empty filter
	 */
	public Filter() {
		minDate = null;
		maxDate = null;
		minTime = null;
		maxTime = null;
		minTransactionID = null;
		maxTransactionID = null;
		transactionDescription = null;
		minAmount = null;
		maxAmount = null;
		merchantName = null;
		paymentMethod = null;
		locationOfSpending = null;
		transactionStatus = null;
	}

	/**
	 * a json parser that transforms json string to filter instance
	 *
	 * @param json string of json
	 */
	public Filter(String json) {
		String[] jsonSplitArray = json.substring(1, json.length() - 1).split(",");
		// split json into individual fields, further split into pairs
		for (String jsonComp : jsonSplitArray) {
			String[] temp = jsonComp.split(":");
			temp[0] = temp[0].replace("\"", "").strip();
			temp[1] = temp[1].replace("\"", "").strip();
			if (temp[0].compareToIgnoreCase("minDate") == 0) {
				minDate = LocalDate.parse(temp[1].substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} else if (temp[0].compareToIgnoreCase("maxDate") == 0) {
				maxDate = LocalDate.parse(temp[1].substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} else if (temp[0].compareToIgnoreCase("minTransactionID") == 0) {
				minTransactionID = temp[1];
			} else if (temp[0].compareToIgnoreCase("maxTransactionID") == 0) {
				maxTransactionID = temp[1];
			} else if (temp[0].compareToIgnoreCase("transactionDescription") == 0) {
				transactionDescription = temp[1];
			} else if (temp[0].compareToIgnoreCase("minAmount") == 0) {
				minAmount = new BigDecimal(temp[1]);
			} else if (temp[0].compareToIgnoreCase("maxAmount") == 0) {
				maxAmount = new BigDecimal(temp[1]);
			} else if (temp[0].compareToIgnoreCase("merchantName") == 0) {
				merchantName = temp[1];
			} else if (temp[0].compareToIgnoreCase("type") == 0) {
				if (temp[1].compareToIgnoreCase("withdraw") == 0)
					paymentMethod = DataWranglerInterface.PaymentMethods.CASH_OUT;
				else if (temp[1].compareToIgnoreCase("deposit") == 0)
					paymentMethod = DataWranglerInterface.PaymentMethods.DEBIT;
				else if (temp[1].compareToIgnoreCase("payment") == 0)
					paymentMethod = DataWranglerInterface.PaymentMethods.PAYMENT;
				else if (temp[1].compareToIgnoreCase("transfer") == 0)
					paymentMethod = DataWranglerInterface.PaymentMethods.TRANSFER;
			} else if (temp[0].compareToIgnoreCase("locationOfSpending") == 0) {
				locationOfSpending = temp[1];
			} else if (temp[0].compareToIgnoreCase("transactionStatus") == 0) {
				if (transactionStatus.compareToIgnoreCase("income") == 0 ||
						transactionStatus.compareToIgnoreCase("expense") == 0) // only set when it is valid
					this.transactionStatus = temp[1];
			}
		}
	}

	/**
	 * a constructor that initializes all values as desired
	 */
	public Filter(LocalDate minDate, LocalDate maxDate, LocalTime minTime, LocalTime maxTime, String minTransactionID,
	              String maxTransactionID, String transactionDescription, BigDecimal minAmount, BigDecimal maxAmount,
	              String merchantName, DataWranglerInterface.PaymentMethods paymentMethod, String locationOfSpending,
	              String transactionStatus) {
		if (transactionStatus.compareToIgnoreCase("income") == 0 ||
				transactionStatus.compareToIgnoreCase("expense") == 0) // only set when it is valid
			this.transactionStatus = transactionStatus;
		this.minDate = minDate;
		this.maxDate = maxDate;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.minTransactionID = minTransactionID;
		this.maxTransactionID = maxTransactionID;
		this.transactionDescription = transactionDescription;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.merchantName = merchantName;
		this.paymentMethod = paymentMethod;
		this.locationOfSpending = locationOfSpending;
	}

	/**
	 * Get transactionStatus.
	 *
	 * @return private variable of transactionStatus
	 */
	public String getTransactionStatus() {
		return transactionStatus;
	}

	/**
	 * Sets transactionStatus.
	 *
	 * @param transactionStatus private field of transactionStatus
	 */
	public void setTransactionStatus(String transactionStatus) {
		if (transactionStatus.compareToIgnoreCase("income") != 0 &&
				transactionStatus.compareToIgnoreCase("expense") != 0)
			return;
		this.transactionStatus = transactionStatus;
	}

	/**
	 * Get minDate.
	 *
	 * @return private variable of minDate
	 */
	@Override
	public LocalDate getMinDate() {
		return minDate;
	}

	/**
	 * Sets minDate.
	 *
	 * @param minDate private field of minDate
	 */
	@Override
	public void setMinDate(LocalDate minDate) {
		this.minDate = minDate;
	}

	/**
	 * Get maxDate.
	 *
	 * @return private variable of maxDate
	 */
	@Override
	public LocalDate getMaxDate() {
		return maxDate;
	}

	/**
	 * Sets maxDate.
	 *
	 * @param maxDate private field of maxDate
	 */
	@Override
	public void setMaxDate(LocalDate maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * Get minTime.
	 *
	 * @return private variable of minTime
	 */
	@Override
	public LocalTime getMinTime() {
		return minTime;
	}

	/**
	 * Sets minTime.
	 *
	 * @param minTime private field of minTime
	 */
	@Override
	public void setMinTime(LocalTime minTime) {
		this.minTime = minTime;
	}

	/**
	 * Get maxTime.
	 *
	 * @return private variable of maxTime
	 */
	@Override
	public LocalTime getMaxTime() {
		return maxTime;
	}

	/**
	 * Sets maxTime.
	 *
	 * @param maxTime private field of maxTime
	 */
	@Override
	public void setMaxTime(LocalTime maxTime) {
		this.maxTime = maxTime;
	}

	/**
	 * Get minTransactionID.
	 *
	 * @return private variable of minTransactionID
	 */
	@Override
	public String getMinTransactionID() {
		return minTransactionID;
	}

	/**
	 * Sets minTransactionID.
	 *
	 * @param minTransactionID private field of minTransactionID
	 */
	@Override
	public void setMinTransactionID(String minTransactionID) {
		this.minTransactionID = minTransactionID;
	}

	/**
	 * Get maxTransactionID.
	 *
	 * @return private variable of maxTransactionID
	 */
	@Override
	public String getMaxTransactionID() {
		return maxTransactionID;
	}

	/**
	 * Sets maxTransactionID.
	 *
	 * @param maxTransactionID private field of maxTransactionID
	 */
	@Override
	public void setMaxTransactionID(String maxTransactionID) {
		this.maxTransactionID = maxTransactionID;
	}

	/**
	 * Get transactionDescription.
	 *
	 * @return private variable of transactionDescription
	 */
	@Override
	public String getTransactionDescription() {
		return transactionDescription;
	}

	/**
	 * Sets transactionDescription.
	 *
	 * @param transactionDescription private field of transactionDescription
	 */
	@Override
	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	/**
	 * Get minAmount.
	 *
	 * @return private variable of minAmount
	 */
	@Override
	public BigDecimal getMinAmount() {
		return minAmount;
	}

	/**
	 * Sets minAmount.
	 *
	 * @param minAmount private field of minAmount
	 */
	@Override
	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	/**
	 * Get maxAmount.
	 *
	 * @return private variable of maxAmount
	 */
	@Override
	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	/**
	 * Sets maxAmount.
	 *
	 * @param maxAmount private field of maxAmount
	 */
	@Override
	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	/**
	 * Get merchantName.
	 *
	 * @return private variable of merchantName
	 */
	@Override
	public String getMerchantName() {
		return merchantName;
	}

	/**
	 * Sets merchantName.
	 *
	 * @param merchantName private field of merchantName
	 */
	@Override
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/**
	 * Get paymentMethod.
	 *
	 * @return private variable of paymentMethod
	 */
	@Override
	public DataWranglerInterface.PaymentMethods getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Sets paymentMethod.
	 *
	 * @param paymentMethod private field of paymentMethod
	 */
	@Override
	public void setPaymentMethod(DataWranglerInterface.PaymentMethods paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * Sets locationOfSpending.
	 *
	 * @param locationOfSpending private field of locationOfSpending
	 */
	@Override
	public void setLocationOfSpending(String locationOfSpending) {
		this.locationOfSpending = locationOfSpending;
	}

	/**
	 * Get locationOfSpending.
	 *
	 * @return private variable of locationOfSpending
	 */
	@Override
	public String getLocationOfSpending() {
		return locationOfSpending;
	}

	/**
	 * this method is for backend use, but since our group decided not to use packages,
	 * and this will not have subclasses, protected is no difference from public
	 *
	 * @param transactions to be filtered
	 * @param keyword      if any that should be found in result, null if no keyword restricted
	 * @return the transactions that fits the user's need
	 */
	public Transaction[] getTransactions(Transaction[] transactions, String keyword) {
		LinkedList<Transaction> tempList = new LinkedList<>();
		for (Transaction transaction : transactions) {
			// can also be processed with stream, but since there are too many conditions, if statement looks clearer
			LocalTime tempTime = transaction.getDateTime().toLocalTime();
			LocalDate tempDate = transaction.getDateTime().toLocalDate();
			if ((minDate == null || tempDate.compareTo(minDate) >= 0)
					&& (maxDate == null || tempDate.compareTo(maxDate) <= 0)
					&& (minTime == null || tempTime.compareTo(minTime) >= 0)
					&& (maxTime == null || tempTime.compareTo(maxTime) <= 0)
					&& (minTransactionID == null ||
					minTransactionID.compareTo(transaction.getTransactionID()) <= 0)
					&& (maxTransactionID == null ||
					maxTransactionID.compareTo(transaction.getTransactionID()) >= 0)
					&& (transactionDescription == null ||
					transaction.getTransactionDescription().toUpperCase().
							contains(transactionDescription.toUpperCase()))
					&& (minAmount == null || transaction.getAmount().compareTo(minAmount) >= 0)
					&& (maxAmount == null || transaction.getAmount().compareTo(maxAmount) <= 0)
					&& (merchantName == null ||
					transaction.getMerchantName().toUpperCase().contains(merchantName.toUpperCase()))
					&& (paymentMethod == null || transaction.getPaymentMethod() == paymentMethod)
					&& (locationOfSpending == null ||
					transaction.getLocationOfSpending().toUpperCase().contains(locationOfSpending.toUpperCase()))
					&& (keyword == null || transaction.toString().toUpperCase().contains(keyword.toUpperCase()))
					&& (transactionStatus == null ||
					transaction.getTransactionStatus().compareToIgnoreCase(transactionStatus) == 0)) {
				tempList.add(transaction);
			}
		}

		return tempList.toArray(Transaction[]::new);
	}
}
