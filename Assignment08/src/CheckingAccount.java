import ExtraExceptions.AccountClosedException;
import ExtraExceptions.InsufficientBalanceException;

public class CheckingAccount extends Account{
    //Allows customer to withdraw more than the account balance
    //Negative balance is the overdraft limit;
    private double overdraft;
    public CheckingAccount(Person customer, int Id, double overdraft){
        super(customer, Id, "(Checking)");
        this.overdraft = overdraft;
    }
    public CheckingAccount(Person customer, int Id, double overdraft, String ISO, double rate) {
        super(customer, Id, "(Checking)", ISO, rate);
        this.overdraft = overdraft;
    }

    public void withdraw(double amount) throws InsufficientBalanceException, AccountClosedException {
        if(getBalance() + overdraft - amount < 0) {
            throw new InsufficientBalanceException(String.format("Withdraw failed, the balance is: %.2f%n%n",
                    super.getBalance()));
        }

        super.withdraw(amount);
    }


}