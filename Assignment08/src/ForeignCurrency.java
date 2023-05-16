public class ForeignCurrency {
    private String ISO;
    private double rate;
    private double balance;

    public ForeignCurrency(String ISO, double rate) {
        this.ISO = ISO;
        this.rate = rate;
    }
    public double getConversionToUSD() {
        double toUSD = (balance) * (rate);
        return toUSD;
    }


    public String getISO() {
        return ISO;
    }

    public double getRate() {
        return rate;
    }

    public String toString() {
        return (String.format("%s %.4f", this.ISO, this.rate));
    }

}
