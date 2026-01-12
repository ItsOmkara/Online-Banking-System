package LM10;

class ApplyForCheckbookRequest {
    private String cardNo;

    // Getters and Setters
    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }
}

class ApplyForCheckbookResponse {
    private String message;

    // Constructor
    public ApplyForCheckbookResponse(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
