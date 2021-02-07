public class StoreStateMessage {
    String n_lineUp;
    String n_bookings;
    String n_customers;

    public StoreStateMessage(String n_lineUp, String n_bookings, String n_customers) {
        this.n_lineUp = n_lineUp;
        this.n_bookings = n_bookings;
        this.n_customers = n_customers;
    }

    public String getN_lineUp() {
        return n_lineUp;
    }

    public String getN_bookings() {
        return n_bookings;
    }

    public String getN_customers() {
        return n_customers;
    }
}
