public class Transactions{
    private String type;
    private int id;
    private double amount;
    public Transactions(String type, int id, double amount){
        super();
        this.type = type;
        this.id = id;
        this.amount = amount;
    }
    public String getType(){
        return (this.type);
    }
    public double getTransactionAmount(){
        return (this.amount);
    }
    //Methods
    public String toString(){
        String transactionInfo = "";
        transactionInfo += String.format("%d : %s : %.2f%n", id, type, amount);
        return (transactionInfo);
    }
}