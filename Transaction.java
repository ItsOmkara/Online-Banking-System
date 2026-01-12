package LM10;

//public class Transaction {
//
//	private int transactionId;
//    private double amount;
//    private String transactionType;
//    private String transactionDate;
//
//    // Getters and Setters
//    public int getTransactionId() {
//        return transactionId;
//    }
//
//    public void setTransactionId(int transactionId) {
//        this.transactionId = transactionId;
//    }
//
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public String getTransactionType() {
//        return transactionType;
//    }
//
//    public void setTransactionType(String transactionType) {
//        this.transactionType = transactionType;
//    }
//
//    public String getTransactionDate() {
//        return transactionDate;
//    }
//
//    public void setTransactionDate(String transactionDate) {
//        this.transactionDate = transactionDate;
//    }
//}
class Transaction {
    private int transactionId;
    private double amount;
    private String cardNo;
    private String transactionType;
    private String transactionDate;

    public Transaction(int transactionId, double amount, String cardNo, String transactionType, String transactionDate) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.cardNo = cardNo;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    // Getters (required for Gson serialization)
    public int getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }
}