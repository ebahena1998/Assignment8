import ExtraExceptions.AccountClosedException;
import ExtraExceptions.InsufficientBalanceException;

public class SavingsAccount extends Account{
    public SavingsAccount(Person customer, int id){
        super(customer, id, "(Savings)");
    }

    public SavingsAccount(Person customer, int id, String ISO, double rate) {
        super(customer, id, "(Savings)", ISO, rate);
    }

    public void withdraw(double amount) throws InsufficientBalanceException, AccountClosedException {
        if(getBalance() - amount < 0) {
            throw new InsufficientBalanceException(String.format("Withdraw failed, the balance is: %.2f%n%n",
                    super.getBalance()));
        }

        super.withdraw(amount);
    }
}