// --== CS400 File Header Information ==--
// Name: Tao Pang
// Email: tpang4@wisc.edu
// Team: IA
// Role: Test Engineer 1
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: Front End parts are not tested due to the use of dynamic pages.

import org.junit.jupiter.api.*;

import static java.time.DayOfWeek.MONDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * JUnit test class for Data Wrangler and Back End.
 */
class Tester {
    // Path for data file.
    private static final String dataFilePath = "res";
    // Path for backup file.
    private static final String backupFilePath = "res";
    // Read-in csv data.
    private static CSVFileView transactions, userInformation;

    /**
     * Init the data and backup the original file.
     */
    @BeforeAll
    public static void init() {
        try {
            transactions = new CSVFileView(Path.of(dataFilePath, "transaction.csv"));
            userInformation = new CSVFileView(Path.of(dataFilePath, "userInformation.csv"));
            Files.copy(Path.of(dataFilePath, "transaction.csv"), Path.of(backupFilePath, "transaction_bu.csv"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(dataFilePath, "userInformation.csv"), Path.of(backupFilePath, "userInformation_bu.csv"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Failed for tester to read data files.");
            System.exit(1);
        }
    }

    /**
     * Restore the backup file after each test case.
     */
    @AfterEach
    public void recover() {
        try {
            Files.copy(Path.of(backupFilePath, "transaction_bu.csv"), Path.of(dataFilePath, "transaction.csv"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(backupFilePath, "userInformation_bu.csv"), Path.of(dataFilePath, "userInformation.csv"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Failed for tester to recover data files.");
            System.exit(1);
        }
    }

    /**
     * Clean out backup files after all tests.
     */
    @AfterAll
    public static void clean() {
        try {
            Files.deleteIfExists(Path.of(backupFilePath, "transaction_bu.csv"));
            Files.deleteIfExists(Path.of(backupFilePath, "userInformation_bu.csv"));
        } catch (IOException e) {
            System.out.println("Failed for tester to clean data files.");
            System.exit(1);
        }
    }

    /**
     * Test whether the getters of Transaction works properly.
     */
    @Test
    public void testTransactionGetters() {
        LocalDateTime time = LocalDateTime.now();
        Transaction transaction = new Transaction("TEST_STATUS", "TEST_USERID", time, "TEST_TRANSACTION_ID",
                "TEST_TRANSACTION_DESC", BigDecimal.valueOf(699.99), "TEST_MERCHANT",
                DataWranglerInterface.PaymentMethods.CASH_OUT, "TEST_LOCATION");
        assertEquals("TEST_STATUS", transaction.getTransactionStatus());
        assertEquals("TEST_USERID", transaction.getUserID());
        assertEquals(time, transaction.getDateTime());
        assertEquals("TEST_TRANSACTION_ID", transaction.getTransactionID());
        assertEquals("TEST_TRANSACTION_DESC", transaction.getTransactionDescription());
        assertEquals(BigDecimal.valueOf(699.99), transaction.getAmount());
        assertEquals("TEST_MERCHANT", transaction.getMerchantName());
        assertEquals(DataWranglerInterface.PaymentMethods.CASH_OUT, transaction.getPaymentMethod());
        assertEquals("TEST_LOCATION", transaction.getLocationOfSpending());
    }

    /**
     * Test whether the setters of Transaction works properly.
     */
    @Test
    public void testTransactionSetters() {
        LocalDateTime time = LocalDateTime.now();
        Transaction transaction = new Transaction("TEST_STATUS", "TEST_USERID", time, "TEST_TRANSACTION_ID",
                "TEST_TRANSACTION_DESC", BigDecimal.valueOf(699.99), "TEST_MERCHANT",
                DataWranglerInterface.PaymentMethods.CASH_OUT, "TEST_LOCATION");
        transaction.setTransactionStatus("NEW_STATUS");
        assertEquals("NEW_STATUS", transaction.getTransactionStatus());
        transaction.setUserID("NEW_USERID");
        assertEquals("NEW_USERID", transaction.getUserID());
        transaction.setDateTime(LocalDateTime.of(1999, 1, 1, 1, 1));
        assertEquals(LocalDateTime.of(1999, 1, 1, 1, 1), transaction.getDateTime());
        transaction.setTransactionID("NEW_TRANSACTION_ID");
        assertEquals("NEW_TRANSACTION_ID", transaction.getTransactionID());
        transaction.setTransactionDescription("NEW_TRANSACTION_DESC");
        assertEquals("NEW_TRANSACTION_DESC", transaction.getTransactionDescription());
        transaction.setAmount(BigDecimal.valueOf(0.00));
        assertEquals(BigDecimal.valueOf(0.00), transaction.getAmount());
        transaction.setMerchantName("NEW_MERCHANT");
        assertEquals("NEW_MERCHANT", transaction.getMerchantName());
        transaction.setPaymentMethod("DEBIT");
        assertEquals(DataWranglerInterface.PaymentMethods.DEBIT, transaction.getPaymentMethod());
        transaction.setLocationOfSpending("NEW_LOCATION");
        assertEquals("NEW_LOCATION", transaction.getLocationOfSpending());
    }

    /**
     * Test whether the compareTo method of Transaction works properly.
     */
    @Test
    public void testTransactionCompareTo() {
        LocalDateTime time = LocalDateTime.now();
        Transaction transaction1 = new Transaction("TEST_STATUS", "TEST_USERID", time, "TEST_TRANSACTION_ID_0",
                "TEST_TRANSACTION_DESC", BigDecimal.valueOf(699.99), "TEST_MERCHANT",
                DataWranglerInterface.PaymentMethods.CASH_OUT, "TEST_LOCATION");
        Transaction transaction2 = new Transaction("TEST_STATUS", "TEST_USERID", time, "TEST_TRANSACTION_ID_1",
                "TEST_TRANSACTION_DESC", BigDecimal.valueOf(599.99), "TEST_MERCHANT",
                DataWranglerInterface.PaymentMethods.CASH_OUT, "TEST_LOCATION");
        Transaction transaction3 = new Transaction("TEST_STATUS", "TEST_USERID", time, "TEST_TRANSACTION_ID_2",
                "TEST_TRANSACTION_DESC", BigDecimal.valueOf(699.99), "TEST_MERCHANT",
                DataWranglerInterface.PaymentMethods.CASH_OUT, "TEST_LOCATION");
        Transaction transaction4 = new Transaction("TEST_STATUS", "TEST_USERID", time, "TEST_TRANSACTION_ID_0",
                "TEST_TRANSACTION_DESC", BigDecimal.valueOf(699.99), "TEST_MERCHANT",
                DataWranglerInterface.PaymentMethods.CASH_OUT, "TEST_LOCATION");
        // Test case for comparing two transactions with different amounts.
        assertTrue(transaction1.compareTo(transaction2) > 0);
        // Test case for comparing two transactions with same amount but different ids.
        assertTrue(transaction1.compareTo(transaction3) < 0);
        // Test case for comparing two transactions with exactly same amounts and ids.
        assertEquals(transaction1.compareTo(transaction4), 0);
    }

    /**
     * Test whether the getters of UserAccount works properly.
     */
    @Test
    public void testUserAccountGetters() {
        LocalDate time = LocalDate.now();
        UserAccount account = new UserAccount("TEST_USERID", "TEST_NAME", encryptPassword("TEST_PASSWORD"), time,
                "TEST_NATIONALITY", "TEST_ADDRESS", BigDecimal.valueOf(999.99), BigDecimal.valueOf(15999.99));
        assertEquals("TEST_USERID", account.getUserID());
        assertEquals("TEST_NAME", account.getName());
        assertEquals(encryptPassword("TEST_PASSWORD"), account.getHashedPassword());
        assertEquals(time, account.getDate());
        assertEquals("TEST_NATIONALITY", account.getNationality());
        assertEquals("TEST_ADDRESS", account.getAddress());
        assertEquals(BigDecimal.valueOf(999.99), account.getBudgetCapPerDay());
        assertEquals(BigDecimal.valueOf(15999.99), account.getBalance());
    }

    /**
     * Test whether the Transaction related operations of UserAccount works properly.
     */
    @Test
    public void testUserAccountTransactionOperations() {
        LocalDate time = LocalDate.now();
        UserAccount account = new UserAccount("TEST_USERID", "TEST_NAME", encryptPassword("TEST_PASSWORD"), time,
                "TEST_NATIONALITY", "TEST_ADDRESS", BigDecimal.valueOf(999.99), BigDecimal.valueOf(15999.99));
        // Test case for checking whether the allTransactions() returns empty array for a totally new user account.
        assertEquals(0, account.allTransactions().length);
        // Test case for adding one existed transaction.
        Transaction transaction = new Transaction("TEST_STATUS", "TEST_USERID", time.atStartOfDay(),
                "TEST_TRANSACTION_ID", "TEST_TRANSACTION_DESC", BigDecimal.valueOf(699.99), "TEST_MERCHANT",
                DataWranglerInterface.PaymentMethods.CASH_OUT, "TEST_LOCATION");
        account.addTransaction(transaction);
        assertEquals(1, account.allTransactions().length);
        assertEquals(transaction, account.allTransactions()[0]);
        // Test case for removing transaction from an invalid id.
        assertFalse(account.removeTransaction("INVALID_TRANSACTION"));
        assertEquals(1, account.allTransactions().length);
        // Test case for removing transaction from a valid id.
        assertTrue(account.removeTransaction("TEST_TRANSACTION_ID"));
        assertEquals(0, account.allTransactions().length);
        // Test case for adding multiple transactions.
        account.addTransaction(transaction);
        account.addTransaction(new Transaction("NEW_STATUS", account.getUserID(), time.atStartOfDay(),
                "NEW_TRANSACTION_ID", "NEW_DESC", BigDecimal.valueOf(99.99), "TEST_MERCHANT",
                DataWranglerInterface.PaymentMethods.TRANSFER, "NEW_LOCATION"));
        assertEquals(2, account.allTransactions().length);
        assertEquals("NEW_STATUS", account.allTransactions()[1].getTransactionStatus());
        // Test case for removing a transaction from a valid id.
        Transaction addedTransaction = account.allTransactions()[1];
        assertTrue(account.removeTransaction(addedTransaction.getTransactionID()));
        assertEquals(1, account.allTransactions().length);
        // Test case for removing a transaction from a new created invalid transaction object.
        Transaction invalidTransaction = new Transaction("INVALID", "INVALID", time.atTime(1, 1), "INVALID", "INVALID",
                BigDecimal.ZERO, "INVALID", DataWranglerInterface.PaymentMethods.DEBIT, "INVALID");
        assertFalse(account.removeTransaction(invalidTransaction));
        assertEquals(1, account.allTransactions().length);
        // Test case for searching a valid transaction id.
        assertEquals(transaction, account.getSearchedTransaction("TEST_TRANSACTION_ID"));
        // Test case for removing a transaction from a existed valid transaction object.
        assertTrue(account.removeTransaction(transaction));
        assertEquals(0, account.allTransactions().length);
        // Test case for searching an already removed transaction from its id.
        assertNull(account.getSearchedTransaction("TEST_TRANSACTION_ID"));
    }

    /**
     * Test whether the setters of UserAccount works properly.
     */
    @Test
    public void testUserAccountSetters() {
        LocalDate time = LocalDate.now();
        UserAccount account = new UserAccount("TEST_USERID", "TEST_NAME", encryptPassword("TEST_PASSWORD"), time,
                "TEST_NATIONALITY", "TEST_ADDRESS", BigDecimal.valueOf(999.99), BigDecimal.valueOf(15999.99));
        account.setUserID("NEW_USERID");
        assertEquals("NEW_USERID", account.getUserID());
        account.setName("NEW_NAME");
        assertEquals("NEW_NAME", account.getName());
        account.setHashedPassword(encryptPassword("NEW_PASSWORD"));
        assertEquals(encryptPassword("NEW_PASSWORD"), account.getHashedPassword());
        account.setDate(LocalDate.of(2015, 5, 5));
        assertEquals(LocalDate.of(2015, 5, 5), account.getDate());
        account.setNationality("NEW_NATIONALITY");
        assertEquals("NEW_NATIONALITY", account.getNationality());
        account.setAddress("NEW_ADDRESS");
        assertEquals("NEW_ADDRESS", account.getAddress());
        account.setBudgetCapPerDay(BigDecimal.valueOf(69.99));
        assertEquals(BigDecimal.valueOf(69.99), account.getBudgetCapPerDay());
        account.setBalance(BigDecimal.valueOf(599.99));
        assertEquals(BigDecimal.valueOf(599.99), account.getBalance());
    }

    /**
     * Test whether the UserAccount related operations of DataWrangler works properly.
     */
    @Test
    public void testDataWranglerUserAccountOperations() {
        DataWranglerInterface wrangler = new DataWrangler();
        // Test case for checking whether getAllUserAccounts() returns the right size array.
        assertEquals(userInformation.getRowCount(), wrangler.getAllUserAccounts().length);
        for (CSVFileView.RowEntry row : userInformation) {
            String userID = row.getColumn("userID");
            String password = decryptPassword(row.getColumn("hashedPassword"));
            // Test case for checking whether getUserAccount returns null for an invalid certificate.
            assertNull(wrangler.getUserAccount(password + "INVALID", userID));
            // Test case for checking whether getUserAccount returns the right account object.
            UserAccount account = wrangler.getUserAccount(userID, password);
            assertNotNull(account);
            assertEquals(userID, account.getUserID());
            assertEquals(row.getColumn("name"), account.getName());
            // Test case for checking whether containsUserAccount returns True for valid account object.
            assertTrue(wrangler.containsUserAccount(account));
            // Test case for checking whether containsUserAccount returns True for valid user id.
            assertTrue(wrangler.containsUserAccount(userID));
        }
        // Test case for checking whether containsUserAccount returns False for invalid user id.
        assertFalse(wrangler.containsTransaction("DEFINITELY AN INVALID ID HERE"));
        assertFalse(wrangler.containsTransaction("@@@ANOTHER CHAOTIC ID@@@@"));
        // Test case for checking whether getUserAccount returns null for an invalid certificate.
        assertNull(wrangler.getUserAccount("INVALID_USER_ID", "INVALID_USER_PASSWORD"));
        // Test case for adding an user account from given arguments.
        assertTrue(wrangler.addUserAccount("TEST_USERID", "TEST_NAME", "TEST_PASSWORD", LocalDate.now(),
                "TEST_NATIONALITY", "TEST_ADDRESS", BigDecimal.valueOf(999.99), BigDecimal.valueOf(15999.99)));
        assertEquals(userInformation.getRowCount() + 1, wrangler.getAllUserAccounts().length);
        assertTrue(wrangler.containsUserAccount("TEST_USERID"));
        assertNotNull(wrangler.getUserAccount("TEST_USERID", "TEST_PASSWORD"));
        assertEquals("TEST_NAME", wrangler.getUserAccount("TEST_USERID", "TEST_PASSWORD").getName());
        // Test case for removing an existed user from its id.
        assertTrue(wrangler.removeUserAccount("TEST_USERID"));
        assertNull(wrangler.getUserAccount("TEST_USED_ID", "TEST_PASSWORD"));
        // Test case for removing all user accounts.
        int originalSize = wrangler.getAllUserAccounts().length;

        for (CSVFileView.RowEntry row : userInformation) {
            String userID = row.getColumn("userID");
            String password = decryptPassword(row.getColumn("hashedPassword"));
            UserAccount account = wrangler.getUserAccount(userID, password);
            assertNotNull(account);
            wrangler.removeUserAccount(account);
        }
        assertEquals(0, wrangler.getAllUserAccounts().length);
        // Test case for re-adding all user accounts.
        for (CSVFileView.RowEntry row : userInformation) {
            String userID = row.getColumn("userID");
            String name = row.getColumn("name");
            String password = decryptPassword(row.getColumn("hashedPassword"));
            LocalDate date = row.getColumn("dateOfBirth", LocalDate.class);
            String nationality = row.getColumn("nationality");
            String address = row.getColumn("address");
            BigDecimal budget = BigDecimal.valueOf(row.getColumn("budgetCapPerDay", Double.class));
            BigDecimal balance = BigDecimal.valueOf(row.getColumn("balance", Double.class));
            assertTrue(wrangler.addUserAccount(userID, name, password, date, nationality, address, budget, balance));
        }

        assertEquals(originalSize, wrangler.getAllUserAccounts().length);
        // Test case for checking whether all accounts are added successfully.
        for (CSVFileView.RowEntry row : userInformation)
            assertTrue(wrangler.containsUserAccount(row.getColumn("userID")));
    }

    /**
     * Test whether the Transaction related operations of DataWrangler works properly.
     */
    @Test
    public void testDataWranglerTransactionOperations() {
        DataWranglerInterface wrangler = new DataWrangler();
        assertEquals(transactions.getRowCount(), wrangler.getAllTransactions().length);
        // Test case for checking whether containsTransaction gives expected result when receiving the ID transaction.
        for (CSVFileView.RowEntry row : transactions) {
            String transactionID = row.getColumn("transactionID");
            assertTrue(wrangler.containsTransaction(transactionID));
        }
        // Test case for checking whether containsTransaction gives expected result when receiving transaction reference.
        for (CSVFileView.RowEntry row : userInformation) {
            String userID = row.getColumn("userID");
            String password = decryptPassword(row.getColumn("hashedPassword"));
            UserAccount account = wrangler.getUserAccount(userID, password);
            for (Transaction transaction : account.allTransactions())
                assertTrue(wrangler.containsTransaction(transaction));
        }
        // Test case for checking whether removeUserAccountAndTransactions removes the data as expected.
        Random random = new Random();
        for (CSVFileView.RowEntry row : userInformation) {
            String userID = row.getColumn("userID");
            String password = decryptPassword(row.getColumn("hashedPassword"));
            UserAccount account = wrangler.getUserAccount(userID, password);
            Transaction[] transactions = account.allTransactions();
            assertTrue(wrangler.containsUserAccount(userID));
            if (random.nextBoolean())
                wrangler.removeUserAccountAndTransactions(userID);
            else
                wrangler.removeUserAccountAndTransactions(account);
            // UserAccount can not exist after removing.
            assertFalse(wrangler.containsUserAccount(account));
            assertFalse(wrangler.containsUserAccount(userID));
            // Related transaction can not exist after removing.
            for (Transaction transaction : transactions) {
                assertFalse(wrangler.containsTransaction(transaction));
                assertFalse(wrangler.containsTransaction(transaction.getTransactionID()));
            }
        }
    }

    /**
     * Test whether the getters of BackEnd works properly.
     */
    @Test
    public void testBackEndGetters() {
        BackEndInterface backend = null;
        try {
            backend = new BackEnd();
        } catch (IOException e) {
            fail("Failed to create BackEnd due to IOError");
        }
        // Test case for checking whether getUser and getName returns correct results.
        for (CSVFileView.RowEntry row : userInformation) {
            String userID = row.getColumn("userID");
            String name = row.getColumn("name");
            UserAccount account = backend.getUser(userID);
            assertNotNull(account);
            assertEquals(name, account.getName());
            assertEquals(name, backend.getName(userID));
        }
        // Test case for checking whether getTransaction returns correct results.
        for (CSVFileView.RowEntry row : transactions) {
            String userID = row.getColumn("userID");
            String transactionID = row.getColumn("transactionID");
            BigDecimal amount = new BigDecimal(row.getColumn("amount"));
            Transaction transaction = backend.getTransaction(userID, amount, transactionID);
            assertNotNull(transaction);
            assertEquals(transactionID, transaction.getTransactionID());
            assertEquals(amount, transaction.getAmount());
        }
        // Test case for checking whether get methods work.
        for (String userID : Arrays.stream(transactions.getColumn("userID")).collect(Collectors.toSet())) {
            CSVFileView userTrans = transactions.getFilteredRowsView(r -> r.getColumn("userID").equals(userID));
            Stream<LocalDateTime> dateTimes = Arrays.stream(userTrans.getColumn("dateAndTime", LocalDateTime.class));
            List<LocalDateTime> sortedDateTimes = dateTimes.sorted().collect(Collectors.toList());
            // Reverse the sorted results to get descending order.
            Collections.reverse(sortedDateTimes);
            // Check getDateTimeTransaction method.
            Transaction[] dateTimeTransactions = backend.getDateTimeTransactions(userID, -1);
            for (int i = 0; i < sortedDateTimes.size(); i++)
                assertEquals(sortedDateTimes.get(i), dateTimeTransactions[i].getDateTime());
            dateTimes = Arrays.stream(userTrans.getColumn("dateAndTime", LocalDateTime.class));
            // Check getAmount method.
            for (LocalDateTime date : dateTimes.collect(Collectors.toSet())) {
                double sum = Arrays.stream(userTrans.getFilteredRows(r ->
                        r.getColumn("dateAndTime", LocalDate.class).getMonthValue() == date.getMonthValue() &&
                                r.getColumn("dateAndTime", LocalDate.class).getYear() == date.getYear() &&
                                r.getColumn("transactionStatus").compareToIgnoreCase("Expense") == 0))
                        .mapToDouble(r -> r.getColumn("amount", Double.class)).sum();
                assertEquals(BigDecimal.valueOf(sum).compareTo(backend.getAmount(userID, date.toLocalDate())), 0);
            }
            dateTimes = Arrays.stream(userTrans.getColumn("dateAndTime", LocalDateTime.class));
            // Check getPercentSpent method.
            for (LocalDateTime date : dateTimes.collect(Collectors.toSet())) {
                BigDecimal budgetCap = backend.getUser(userID).getBudgetCapPerDay().multiply(
                        BigDecimal.valueOf(date.toLocalDate().lengthOfMonth()));
                if (budgetCap.compareTo(BigDecimal.ZERO) <= 0) continue;
                assertEquals(backend.getAmount(userID, date.toLocalDate()).divide(budgetCap, RoundingMode.HALF_UP)
                        .doubleValue(), backend.getPercentSpent(userID, date.toLocalDate()));
            }
            // Check getAscendAmountTransactions method.
            Stream<Double> amounts = Arrays.stream(userTrans.getColumn("amount", Double.class));
            List<Double> sortedAmounts = amounts.sorted().collect(Collectors.toList());
            Transaction[] amountTransactions = backend.getAscendAmountTransactions(userID, -1);
            for (int i = 0; i < sortedAmounts.size(); i++)
                assertEquals(0, BigDecimal.valueOf(sortedAmounts.get(i)).compareTo(amountTransactions[i].getAmount()));
            // Check getDailySumPerWeek method.
            LocalDate startDate = LocalDate.now().with(previousOrSame(MONDAY));
            BigDecimal[] dailySum = IntStream.range(0, 7).mapToDouble(i -> Arrays.stream(userTrans.getFilteredRows(r ->
                    r.getColumn("dateAndTime", LocalDate.class).isEqual(startDate.plusDays(i)) &&
                            r.getColumn("transactionStatus").compareToIgnoreCase("Expense") == 0))
                    .mapToDouble(r -> r.getColumn("amount", Double.class)).sum()).mapToObj(BigDecimal::valueOf)
                    .toArray(BigDecimal[]::new);
            BigDecimal[] backEndDailySum = backend.getDailySumPerWeek(userID);
            for (int i = 0; i < backEndDailySum.length; i++)
                assertEquals(0, dailySum[i].compareTo(backEndDailySum[i]));
            // Check getTypeTransactions method.
            for (String payment : Arrays.stream(userTrans.getColumn("paymentMethod")).collect(Collectors.toSet())) {
                DataWranglerInterface.PaymentMethods method = DataWranglerInterface.PaymentMethods.valueOf(payment);
                assertEquals(userTrans.getFilteredRows(r -> r.getColumn("paymentMethod").equals(payment)).length,
                        backend.getTypeTransactions(userID, method, -1).length);
            }
            // Check getLocationTransactions method.
            for (String location : Arrays.stream(userTrans.getColumn("locationOfSpending")).collect(Collectors.toSet())) {
                assertEquals(userTrans.getFilteredRows(r -> r.getColumn("locationOfSpending").toLowerCase()
                                .contains(location.toLowerCase())).length,
                        backend.getLocationTransactions(userID, location, -1).length);
            }
            // Check getNameTransactions method.
            for (String merchant : Arrays.stream(userTrans.getColumn("merchantName")).collect(Collectors.toSet())) {
                assertEquals(userTrans.getFilteredRows(r -> r.getColumn("merchantName").contains(merchant)).length,
                        backend.getNameTransactions(userID, merchant, -1).length);
            }
        }
    }

    /**
     * Test whether the UserAccount related operations of BackEnd works properly.
     */
    @Test
    public void testBackEndUserAccountOperations() {
        BackEndInterface backend = null;
        try {
            backend = new BackEnd();
        } catch (IOException e) {
            fail("Failed to create BackEnd due to IOError");
        }
        // Test case for checking whether validate method returns right result for valid/invalid info.
        for (CSVFileView.RowEntry row : userInformation) {
            String userID = row.getColumn("userID");
            String password = decryptPassword(row.getColumn("hashedPassword"));
            // Check valid user info.
            assertTrue(backend.validate(userID, password));
            // Check invalid user info.
            assertFalse(backend.validate(userID, userID + password));
        }
        ArrayList<String> userIDs = new ArrayList<>();
        // Test case for addUser method with only basic information.
        for (int i = 0; i < 10; i++) {
            String userID = getRandomString(10, 15);
            userIDs.add(userID);
            String password = getRandomString(8, 20);
            String name = getRandomString(5, 10);
            try {
                assertTrue(backend.addUser(userID, password, name));
            } catch (IOException e) {
                fail("BackEnd.addUser failed due to IOError.");
            }
            UserAccount account = backend.getUser(userID);
            assertNotNull(account);
            // Check whether fields are set correctly.
            assertEquals(userID, account.getUserID());
            assertEquals(name, account.getName());
            assertEquals(password, decryptPassword(account.getHashedPassword()));
        }
        ArrayList<UserAccount> accounts = new ArrayList<>();
        // Test case for addUser method with all need information.
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String userID = getRandomString(10, 15);
            String password = getRandomString(8, 20);
            String name = getRandomString(5, 10);
            LocalDate date = LocalDate.of(random.nextInt(30) + 1990, random.nextInt(12) + 1, random.nextInt(25) + 1);
            String nationality = getRandomString(5, 10);
            String address = getRandomString(15, 30);
            BigDecimal budget = BigDecimal.valueOf(random.nextDouble() * random.nextInt(500));
            BigDecimal balance = BigDecimal.valueOf(random.nextDouble() * random.nextInt(50000));
            try {
                assertTrue(backend.addUser(userID, name, password, date, nationality, address, budget, balance));
            } catch (IOException e) {
                fail("BackEnd.addUser failed due to IOError.");
            }
            UserAccount account = backend.getUser(userID);
            accounts.add(account);
            assertNotNull(account);
            // Check whether fields are set correctly.
            assertEquals(userID, account.getUserID());
            assertEquals(password, decryptPassword(account.getHashedPassword()));
            assertEquals(name, account.getName());
            assertEquals(date, account.getDate());
            assertEquals(nationality, account.getNationality());
            assertEquals(address, account.getAddress());
            assertEquals(budget, account.getBudgetCapPerDay());
            assertEquals(balance, account.getBalance());
        }
        // Test case for removing user from its id.
        for (String userID : userIDs) {
            assertTrue(backend.removeUser(userID));
            try {
                backend.getUser(userID);
                fail("BackEnd removeUser() failed since the user still exists after removing.");
            } catch (NoSuchElementException ignore) {
            }
        }
        // Test case from removing user by its reference.
        for (UserAccount account : accounts) {
            assertTrue(backend.removeUser(account));
            try {
                backend.getUser(account.getUserID());
                fail("BackEnd removeUser() failed since the user still exists after removing.");
            } catch (NoSuchElementException ignore) {
            }
        }
    }

    /**
     * Test whether the Transaction related operations of BackEnd works properly.
     */
    @Test
    public void testBackEndTransactionOperations() {
        BackEndInterface backend = null;
        try {
            backend = new BackEnd();
        } catch (IOException e) {
            fail("Failed to create BackEnd due to IOError");
        }
        Random random = new Random();
        // Test the insertTransaction method by randomly generating Transaction for insertion.
        ArrayList<Transaction> addedTransactions = new ArrayList<>();
        for (CSVFileView.RowEntry row : userInformation) {
            String userID = row.getColumn("userID");
            for (int i = 0; i < 5; i++) {
                String transactionStatus = new String[]{"Income", "Expense"}[random.nextInt(2)];
                LocalDateTime dateTime = LocalDateTime.of(random.nextInt(2) + 2019, random.nextInt(12) + 1,
                        random.nextInt(25) + 1, random.nextInt(24), random.nextInt(60));
                String transactionID = getRandomString(15, 20);
                String transactionDesc = getRandomString(15, 20);
                BigDecimal amount = BigDecimal.valueOf(random.nextDouble() * random.nextInt(500));
                String merchant = getRandomString(10, 15);
                String payment = new String[]{"PAYMENT", "TRANSFER", "DEBIT", "CASH_OUT"}[random.nextInt(4)];
                String location = getRandomString(10, 20);
                Transaction transaction = new Transaction(transactionStatus, userID, dateTime, transactionID,
                        transactionDesc, amount, merchant, DataWranglerInterface.PaymentMethods.valueOf(payment), location);
                // The transaction can not exist before inserting.
                assertFalse(backend.containsTransaction(userID, transactionID, null));
                try {
                    backend.insertTransaction(userID, transaction);
                } catch (IOException e) {
                    fail("BackEnd insertTransaction failed due to IOError.");
                }
                // The transaction must exist after inserting.
                assertTrue(backend.containsTransaction(userID, transactionID, null));
                // Check whether the inserted Transaction has correct data.
                Transaction resultTransaction = backend.getTransaction(userID, amount, transactionID);
                assertNotNull(resultTransaction);
                assertEquals(transactionID, resultTransaction.getTransactionID());
                assertEquals(amount, resultTransaction.getAmount());
                assertEquals(location, resultTransaction.getLocationOfSpending());
                addedTransactions.add(transaction);
            }
        }
        // Remove previously inserted Transactions to test removeTransaction method.
        for (Transaction transaction : addedTransactions) {
            String userID = transaction.getUserID();
            BigDecimal amount = transaction.getAmount();
            String transactionID = transaction.getTransactionID();
            // The transaction must exists before removing.
            assertNotNull(backend.getTransaction(userID, amount, transactionID));
            if (random.nextBoolean())
                assertTrue(backend.removeTransaction(userID, transactionID));
            else
                assertTrue(backend.removeTransaction(userID, transaction));
            // The transaction can not exist after removing.
            assertFalse(backend.containsTransaction(userID, transactionID, null));
            assertNull(backend.getTransaction(userID, amount, transactionID));
        }
    }

    /**
     * Get a randomly generated string.
     *
     * @param minLength The minimal length of the result string.
     * @param maxLength The maximal length of the result string.
     * @return The randomly generated string.
     */
    public static String getRandomString(int minLength, int maxLength) {
        Random random = new Random();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++)
            builder.append((char) (random.nextInt(59) + 64));
        return builder.toString();
    }

    /**
     * Decrypt the password stored in the CSV file.
     *
     * @param encrypted The encrypted password read from file.
     * @return The decrypted password.
     */
    public static String decryptPassword(String encrypted) {
        encrypted = encrypted.substring(0, encrypted.length() - 3);
        ArrayList<Character> chars = new ArrayList<>();
        for (int tokenIndex = 0; tokenIndex < encrypted.length(); tokenIndex++) {
            char token = encrypted.charAt(tokenIndex);
            if (token < 100) {
                if (encrypted.substring(tokenIndex + 1, tokenIndex + 3).equals("" + (int) token))
                    chars.add(token);
                else if (token == 'c')
                    chars.add((char) Integer.parseInt(encrypted.substring(tokenIndex + 1, tokenIndex + 3)));
                else
                    chars.add((char) Integer.parseInt(encrypted.substring(tokenIndex, tokenIndex + 3)));
                tokenIndex += 2;
            }
        }
        return chars.stream().map(String::valueOf).collect(Collectors.joining());
    }

    /**
     * Encrypt the password.
     *
     * @param plain The plain password before encrypting.
     * @return The encrypted password.
     */
    public static String encryptPassword(String plain) {
        StringBuilder builder = new StringBuilder();
        for (byte token : plain.getBytes(StandardCharsets.UTF_8)) {
            if (token < 100)
                builder.append(token == 34 || token == 39 ? 'c' : (char) token);
            builder.append(token);
        }
        builder.append("#ac");
        return builder.toString();
    }
}