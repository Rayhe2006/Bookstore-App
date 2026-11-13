package finalproject;

interface State {
    
    public double handlePurchase(Customer customer, double cost, boolean redeem);
    
    public void updateState(Customer customer);
    
    public String getStatusName();
}

