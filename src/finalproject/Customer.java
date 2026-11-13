package finalproject;


public class Customer extends User {

    private int points = 0;
    private State customerState;

    public Customer(String pwd, String usr) {
        super(pwd, usr);
        this.customerState = new SilverState();

    }

    public String getStatus() {
        return customerState.getStatusName(); 
    }

    public State getState() {
        return this.customerState;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points = this.points + points;
    }

    public void removePoints(int points) {
        this.points = this.points - points;
    }

    public void setState(State state) {
        this.customerState = state;
    }
}
