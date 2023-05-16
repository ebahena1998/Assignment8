import ExtraExceptions.AccountClosedException;
import ExtraExceptions.InsufficientBalanceException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Account{
    //Bank settings
    private Person customer;
    private int Id;
    //Account settings
    private String type;
    private boolean status;
    //Transactions settings

    private ArrayList<Transactions> accountTransactions;
    private int counter = 1;

    //Currency Settings
    private ForeignCurrency fCurrency;
    //Account details below
    public Account(Person customer, int Id, String type){
        this.customer = customer;
        this.Id = Id;
        this.type = type;
        this.status = true;
        this.accountTransactions = new ArrayList<Transactions>();
    }

    public Account(Person customer, int Id, String type, String ISO, double rate) {
        this.customer = customer;
        this.Id = Id;
        this.type = type;
        this.status = true;
        this.accountTransactions = new ArrayList<Transactions>();
        this.fCurrency = new ForeignCurrency(ISO, rate);
    }


    public void setStatus(boolean status){
        this.status = status;
    }

    //Getters
    public int getId(){
        return (this.Id);
    }

    public double getBalance(){
        double balance = 0.00;
        for(Transactions temp : accountTransactions){
            if(temp.getType().equals("Credit")){
                balance += temp.getTransactionAmount();
            }
            else {
                balance -= temp.getTransactionAmount();
            }
        }

        return balance;
    }

    //METHODS
    public void withdraw(double amount) throws AccountClosedException, InsufficientBalanceException {
        if(!status && (getBalance() <= 0.00)){
            throw new AccountClosedException(String.format("Account is closed, withdrawals are not allowed!%n"));
        }

        accountTransactions.add(new Transactions("Debit", counter++, amount));
    }

    public void deposit(double amount) throws InsufficientBalanceException {
        if(amount < 0) {
            throw new InsufficientBalanceException(String.format("Deposit failed, the balance is: %.2f%n",
                    getBalance()));
        }

        if(!status && (getBalance() >= 0.00)){
            throw new InsufficientBalanceException(String.format("Account is closed, deposits are not allowed!%n"));
        }

        accountTransactions.add(new Transactions("Credit", counter++, amount));
    }

    public void listOfAccountCurrencyInfo(OutputStream out) throws IOException {
        String temp = "";
        temp = "Account Number: " + getId() + "\n";
        out.write((byte) 10);
        out.write(temp.getBytes());
        temp = String.format("Name: %s %s\nSSN: %s\nCurrency: %s\nCurrency Balance: %s %.2f\nUSD Balance: %s %.2f",
                customer.getFirstName(), customer.getLastName(), customer.getSSN(), fCurrency.getISO(),fCurrency.getISO(),
                convertToUSD(), "USD", getBalance());
        out.write(temp.getBytes());
        out.write((byte) 10);

    }

    public void listOfAccountTransactions(OutputStream out) throws IOException{
        for(Transactions temp : accountTransactions) {
            out.write(temp.toString().getBytes());
        }

        out.write(String.format("Balance: %.2f%n",getBalance()).getBytes());
        out.write((byte) 10);
        out.write((byte) 13);
        out.flush();
    }

    public String getForeignRate() {
        return (this.fCurrency.toString());
    }

    public double convertToUSD() {
        double amount = 0;
        if(fCurrency.getISO().equalsIgnoreCase("USD")) {
            amount = getBalance();
        }
        else
        {
            amount = getBalance();
            amount = fCurrency.getRate() * amount;
        }
        return amount;

    }

    public String toString(){
        if(!Bank.canRead) {
            if(status){
                return String.format("%d %s : %s %.2f : Account Open ", getId(), type, customer.toString(), getBalance());
            }
            return String.format("%d %s : %s %.2f : Account Closed ", getId(), type, customer.toString(), getBalance());
        }

        if(status) {
            return String.format("%d %s : %s %s : %.2f : %.4f: Account Open", getId(), type, customer.toString(), fCurrency.getISO(),
                    getBalance(), convertToUSD());
        }
        return String.format("%d %s : %s %s : %.2f : %.4f: Account Closed", getId(), type, customer.toString(), fCurrency.getISO(),
                getBalance(), convertToUSD());


    }


}