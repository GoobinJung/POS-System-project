package posPd;

public abstract class AuthorizedPayment extends Payment { 

    private String authorizationCode;

    public Boolean isAuthorized() {
        return false; 
    }

    public Boolean countsAsCash() {
        return false;
    }
}