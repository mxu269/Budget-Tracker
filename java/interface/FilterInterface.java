// Name: Zhantao Yang, Xin Su
// Email: zyang484@wisc.edu, xsu57@wisc.edu
// Team: IA
// TA: Mu Cai
// Lecturer: Florian Heimerl, Gary Dahl
// Notes to Grader: Completed and used by both backends to avoid ambiguity and inconsistency,
// Filter code will be implemented independently


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public interface FilterInterface {
	/**
	 * Get transactionStatus.
	 *
	 * @return private variable of transactionStatus
	 */
	String getTransactionStatus();

	/**
	 * Sets transactionStatus.
	 *
	 * @param transactionStatus private field of transactionStatus
	 */
	void setTransactionStatus(String transactionStatus);

	/**
	 * Get minDate.
	 *
	 * @return private variable of minDate
	 */
	LocalDate getMinDate();

	/**
	 * Sets minDate.
	 *
	 * @param minDate private field of minDate
	 */
	void setMinDate(LocalDate minDate);

	/**
	 * Get maxDate.
	 *
	 * @return private variable of maxDate
	 */
	LocalDate getMaxDate();

	/**
	 * Sets maxDate.
	 *
	 * @param maxDate private field of maxDate
	 */
	void setMaxDate(LocalDate maxDate);

	/**
	 * Get minTime.
	 *
	 * @return private variable of minTime
	 */
	LocalTime getMinTime();

	/**
	 * Sets minTime.
	 *
	 * @param minTime private field of minTime
	 */
	void setMinTime(LocalTime minTime);

	/**
	 * Get maxTime.
	 *
	 * @return private variable of maxTime
	 */
	LocalTime getMaxTime();

	/**
	 * Sets maxTime.
	 *
	 * @param maxTime private field of maxTime
	 */
	void setMaxTime(LocalTime maxTime);

	/**
	 * Get minTransactionID.
	 *
	 * @return private variable of minTransactionID
	 */
	String getMinTransactionID();

	/**
	 * Sets minTransactionID.
	 *
	 * @param minTransactionID private field of minTransactionID
	 */
	void setMinTransactionID(String minTransactionID);

	/**
	 * Get maxTransactionID.
	 *
	 * @return private variable of maxTransactionID
	 */
	String getMaxTransactionID();

	/**
	 * Sets maxTransactionID.
	 *
	 * @param maxTransactionID private field of maxTransactionID
	 */
	void setMaxTransactionID(String maxTransactionID);

	/**
	 * Get transactionDescription.
	 *
	 * @return private variable of transactionDescription
	 */
	String getTransactionDescription();

	/**
	 * Sets transactionDescription.
	 *
	 * @param transactionDescription private field of transactionDescription
	 */
	void setTransactionDescription(String transactionDescription);

	/**
	 * Get minAmount.
	 *
	 * @return private variable of minAmount
	 */
	BigDecimal getMinAmount();

	/**
	 * Sets minAmount.
	 *
	 * @param minAmount private field of minAmount
	 */
	void setMinAmount(BigDecimal minAmount);

	/**
	 * Get maxAmount.
	 *
	 * @return private variable of maxAmount
	 */
	BigDecimal getMaxAmount();

	/**
	 * Sets maxAmount.
	 *
	 * @param maxAmount private field of maxAmount
	 */
	void setMaxAmount(BigDecimal maxAmount);

	/**
	 * Get merchantName.
	 *
	 * @return private variable of merchantName
	 */
	String getMerchantName();

	/**
	 * Sets merchantName.
	 *
	 * @param merchantName private field of merchantName
	 */
	void setMerchantName(String merchantName);

	/**
	 * Get paymentMethod.
	 *
	 * @return private variable of paymentMethod
	 */
	DataWranglerInterface.PaymentMethods getPaymentMethod();

	/**
	 * Sets paymentMethod.
	 *
	 * @param paymentMethod private field of paymentMethod
	 */
	void setPaymentMethod(DataWranglerInterface.PaymentMethods paymentMethod);

	/**
	 * Sets locationOfSpending.
	 *
	 * @param locationOfSpending private field of locationOfSpending
	 */
	void setLocationOfSpending(String locationOfSpending);

	/**
	 * Get locationOfSpending.
	 *
	 * @return private variable of locationOfSpending
	 */
	String getLocationOfSpending();

	/**
	 * gets the transaction after being filtered
	 *
	 * @param transactions to be filtered
	 * @param keyword that needs to be found in the transaction
	 * @return filtered transactions
	 */
	Transaction[] getTransactions(Transaction[] transactions, String keyword);
}
