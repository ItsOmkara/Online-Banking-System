package LM10;

public class ChangePinRequest{
    private String cardNo;
    private String oldPin;
    private String newPin;

    // Getters and Setters
    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }

    public String getOldPin() { return oldPin; }
    public void setOldPin(String oldPin) { this.oldPin = oldPin; }

    public String getNewPin() { return newPin; }
    public void setNewPin(String newPin) { this.newPin = newPin; }
}

class ChangePinResponse {
    private String message;

    // Constructor
    public ChangePinResponse(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
