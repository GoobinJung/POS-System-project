package posPd;
/** Local demo validation only; no payment gateway is contacted. */
public abstract class AuthorizedPayment extends Payment {
    private static final long serialVersionUID = 1L;
    public abstract Boolean isAuthorized();
    @Override public Boolean countsAsCash() { return false; }
}
