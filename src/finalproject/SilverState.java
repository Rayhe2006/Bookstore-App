package finalproject;

public class SilverState implements State {
    @Override
    public double handlePurchase(Customer customer, double cost, boolean redeem) {
        double finalPrice = cost;

        if (redeem) {
            double discount = customer.getPoints() / 100.0; //Number of points to the cost
            if (discount > cost) {
                discount = cost;
            }
            customer.removePoints((int) (discount * 100));
            finalPrice = finalPrice - discount;
        }
        else customer.addPoints((int) (finalPrice * 10));
        updateState(customer);
        return finalPrice;
    }

    @Override
    public void updateState(Customer customer) {
        if (customer.getPoints() >= 1000) {
            customer.setState(new GoldState());
        }
    }

    @Override
    public String getStatusName() {
        return "Silver";
    }
}
