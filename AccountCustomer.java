package LM10;

class AccountCustomer {
    private int accountId;
    private int customerId;
    private String cardNo;
    private String atmPin;
    private String accountType;
    private double balance;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String checkbookStatus;

    // Getters and Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }

    public String getAtmPin() { return atmPin; }
    public void setAtmPin(String atmPin) { this.atmPin = atmPin; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCheckbookStatus() {
        return checkbookStatus;
    }

    public void setCheckbookStatus(String checkbookStatus) {
        this.checkbookStatus = checkbookStatus;
    }
}
