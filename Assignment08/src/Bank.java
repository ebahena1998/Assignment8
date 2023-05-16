import ExtraExceptions.AccountClosedException;
import ExtraExceptions.InsufficientBalanceException;
import ExtraExceptions.NoSuchAccountException;

import java.io.*;
import java.util.*;

public class Bank{
    private static Map<Integer, Account> ACCOUNT_LIST = new TreeMap<Integer, Account>();
    private static Map<String, Double> exchangeList = new HashMap<String, Double>();
    private static int accountId = 100;
    private static File foreignExchangeFile;
    public static boolean canRead;
    public static Account createCheckingAccount(Person customer, double overdraft, String ISO){
        if(!canRead) {
            Account bankCheckingAccount = new CheckingAccount(customer, accountId++, overdraft);
            ACCOUNT_LIST.putIfAbsent(bankCheckingAccount.getId(), bankCheckingAccount);
            return bankCheckingAccount;
        }

        Account bankCheckingAccount = new CheckingAccount(customer, accountId++, overdraft, ISO, exchangeList.get(ISO));
        ACCOUNT_LIST.putIfAbsent(bankCheckingAccount.getId(), bankCheckingAccount);
        return bankCheckingAccount;
    }

    public static Account createSavingsAccount(Person customer, String ISO){
        if(!canRead) {
            Account bankSavingsAccount = new SavingsAccount(customer, accountId++);
            ACCOUNT_LIST.putIfAbsent(bankSavingsAccount.getId(), bankSavingsAccount);
            return bankSavingsAccount;
        }
        Account bankSavingsAccount = new SavingsAccount(customer, accountId++, ISO, exchangeList.get(ISO));
        ACCOUNT_LIST.putIfAbsent(bankSavingsAccount.getId(), bankSavingsAccount);
        return bankSavingsAccount;

    }

    public static Account findAccount(int userInput) throws NoSuchAccountException {
        if(!ACCOUNT_LIST.containsKey(userInput)) {
            throw new NoSuchAccountException("Account not found!");
        }
        return ACCOUNT_LIST.get(userInput);
    }

    public static boolean getIsoCode(String userISOCODE) {
        if(!exchangeList.containsKey(userISOCODE)) {
            return false;
        }
        return true;
    }

    public static void listAccountsFromList(OutputStream out) throws IOException {
        out.write((byte) 10);

        for(Account temp : ACCOUNT_LIST.values()) {
            out.write(temp.toString().getBytes());
            out.write((byte) 10);
        }

        out.write((byte) 10);
        out.flush();
    }

    public static void getAccountStatements(int userInput, OutputStream out) throws NoSuchAccountException, IOException{
        findAccount(userInput).listOfAccountTransactions(out);

    }

    public static void getAccountInfo(int userInput, OutputStream out) throws NoSuchAccountException, IOException{
        findAccount(userInput).listOfAccountCurrencyInfo(out);

    }


    public static String depositIntoAccount(int userInput, double amount) throws NoSuchAccountException,
            InsufficientBalanceException, AccountClosedException {

        findAccount(userInput).deposit(amount);
        return (String.format("Deposit successful, the new balance is: %.2f%n%n", findAccount(userInput).getBalance()));
    }

    public static String withdrawFromAccount(int userInput, double amount) throws NoSuchAccountException,
            AccountClosedException, InsufficientBalanceException {

        findAccount(userInput).withdraw(amount);
        return (String.format("Withdraw successful, the new balance is: %.2f%n%n", findAccount(userInput).getBalance()));
    }

    public static String closeAccountInList(int userInput) throws NoSuchAccountException{

        findAccount(userInput).setStatus(false);
        return (String.format("Account closed, current balance is %.2f, deposits are no longer possible!%n%n",
                findAccount(userInput).getBalance()));
    }

    public static void storeExchangeRateFile(String fileName) throws FileNotFoundException,  IOException {
        File file = new File(fileName);
        if(!file.exists() || !file.canRead()) {

            throw new FileNotFoundException("**Currency file could not be loaded, Currency Conversion Service and " +
                    "Foreign Currency accounts are not available**");
        }

        String line;
        foreignExchangeFile = file;
        canRead = true;
        BufferedReader bReader = new BufferedReader(new FileReader(fileName));
        while((line = bReader.readLine()) != null) {

            String[] temp = line.split(",");
            exchangeList.put(temp[0], Double.parseDouble(temp[2]));

        }

        bReader.close();

    }

    public static void convertToUSD() {

    }


}


