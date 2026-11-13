package finalproject;

import java.awt.Dimension;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.awt.*;     


//An object containing the main method of the application. Handles all GUI.
public class FinalProject extends JFrame {

    private Owner owner = new Owner("admin", "admin");

    public FinalProject() {
        setTitle("Bookstore");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Add the listener to catch the close event
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                owner.saveData();//Save the data
                dispose();//Close the window
                System.exit(0);//Kill the process
            }
        });
        setSize(450, 250);

        loginScreen();
        setLocationRelativeTo(null);//Center window on screen
        setVisible(true);
    }

    //clean off all content on screen
    private void cleanup() {
        getContentPane().removeAll();
        revalidate(); //Refresh layout
        repaint(); //Redraw everything
    }

    //Add panel and package to make visible
    private void reassemble(JPanel panel) {
        add(panel);
        revalidate();
        repaint();
    }

    //Login Window
    private void loginScreen() {
        cleanup();
        owner.loadData();
        JPanel panel = new JPanel(); //Main panel of login screen
        JPanel labels = new JPanel();
        JPanel fields = new JPanel();
        JLabel usrLabel = new JLabel("Username:  ");
        JLabel pwdLabel = new JLabel("Password:  ");

        JTextField usrEntry = new JTextField(10);
        JTextField pwdEntry = new JTextField(10);
        JLabel welcome = new JLabel("Welcome to the BookStore App");

        //Authentication Logic
        JButton submit = new JButton("Login");
        submit.addActionListener(e -> {
            String usr = usrEntry.getText();
            String pwd = pwdEntry.getText();
            //Admin Login
            if (usr.equals("admin") && pwd.equals("admin")) {
                ownerOptions();
            }
            //Customer Login
            ArrayList<Customer> x = owner.getCustomers();
            for (int i = 0; i < x.size(); i++) {
                if (usr.equals(x.get(i).getUsername()) && pwd.equals(x.get(i).getPassword())) {
                    customerOptions(x.get(i));
                }
            }
        });
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));

        labels.add(welcome);
        labels.add(usrLabel);
        labels.add(pwdLabel);

        fields.add(Box.createRigidArea(new Dimension(0, 40)));//40px vertical space
        fields.add(usrEntry);
        fields.add(pwdEntry);
        fields.add(submit);
        panel.add(labels);
        panel.add(fields);

        reassemble(panel);
    }

    //Options window for Owners, Customers, Books and Logout buttons
    private void ownerOptions() {
        cleanup();
        JPanel panel = new JPanel();

        JButton customers = new JButton("Customers");
        customers.addActionListener(e -> customerEdit()); 

        JButton books = new JButton("Books");
        books.addActionListener(e -> bookEdit()); 

        JButton logout = new JButton("Log Out"); 
        logout.addActionListener(e -> loginScreen());

        //Center horizontally
        customers.setAlignmentX(Component.CENTER_ALIGNMENT);
        books.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // 10px vertical space
        panel.add(customers);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 
        panel.add(books);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 
        panel.add(logout);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 
        reassemble(panel);
    }

    //The owners-customers-screen
    private void customerEdit() {
        cleanup();
        JPanel panel = new JPanel();

        //Table of customers
        String col[] = {"Username", "Password", "Points"};
        DefaultTableModel tableModel = new DefaultTableModel(col, 0);

        //Filling up the table
        for (int i = 0; i < owner.getCustomers().size(); i++) {
            String usr = owner.getCustomers().get(i).getUsername();
            String pwd = owner.getCustomers().get(i).getPassword();
            int pts = owner.getCustomers().get(i).getPoints();
            Object data[] = {usr, pwd, pts};
            tableModel.addRow(data);
        }
        //Assemble Table
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 100));//width, height

        //Adding new Customer
        JLabel usrLabel = new JLabel("Username:  ");
        JLabel pwdLabel = new JLabel("Password:  ");
        JTextField usrEntry = new JTextField(10);
        JTextField pwdEntry = new JTextField(10);

        //Add Customer
        JButton add = new JButton("add");
        add.addActionListener(e -> {
            String usr = usrEntry.getText();
            String pwd = pwdEntry.getText();
            //Check if there is a customer with the same username
            for (int i = 0; i < owner.getCustomers().size(); i++) {
                Customer existingCustomer = owner.getCustomers().get(i);
                if (existingCustomer.getUsername().equalsIgnoreCase(usr)) {
                    return;
                }
            }
            //Checks for invalid usernames/passwords
            if ((usr.isEmpty() || pwd.isEmpty() || usr.trim().isEmpty() || pwd.trim().isEmpty() || usr.contains(",") || pwd.contains(",")) != true) {
                owner.addCustomer(usr, pwd);
                int size = owner.getCustomers().size();
                Object newData[] = {usr, pwd, owner.getCustomers().get(size - 1).getPoints()};
                tableModel.addRow(newData);
            }
        });
        //Delete Customer
        JButton delete = new JButton("delete");
        delete.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                String usr = (String) tableModel.getValueAt(selected, 0);
                owner.deleteCustomer(usr);
                tableModel.removeRow(selected);
            }
        });

        //back
        JButton back = new JButton("Back");
        back.addActionListener(e -> ownerOptions());

        panel.add(scrollPane);
        panel.add(usrLabel);
        panel.add(usrEntry);
        panel.add(pwdLabel);
        panel.add(pwdEntry);
        panel.add(add);
        panel.add(delete);
        panel.add(back);
        reassemble(panel);
    }

    //The owners-books-screen
    private void bookEdit() {
        cleanup();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1)); //3 equal parts

        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        JPanel bottom = new JPanel();

        //top part (Table of books)
        String[] headings = {"Book Name", "Book Price"};
        DefaultTableModel model = new DefaultTableModel(headings, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        top.setLayout(new BorderLayout());
        top.add(scroll, BorderLayout.CENTER);

        //middle part
        JTextField nameField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JLabel nameLabel = new JLabel("Name: ");
        JLabel priceLabel = new JLabel("Price: ");
        JButton add = new JButton("Add Book");
        //Retrieve books and write to an array
        for (int i = 0; i < owner.getBookStore().getBooks().size(); i++) {
            String bookName = owner.getBookStore().getBooks().get(i).getName();
            double bookPrice = owner.getBookStore().getBooks().get(i).getPrice();
            Object data[] = {bookName, bookPrice};
            model.addRow(data);
        }
        add.addActionListener(e -> {
            String name = nameField.getText();
            String priceText = priceField.getText();
            try {
                double price = Double.parseDouble(priceText); 
                boolean duplicate = false;
                //Loops through array list of books to see if there is a book with the same name
                for (Book x : owner.getBookStore().getBooks()) {
                    if (x.getName().equals(name)) {
                        duplicate = true; 
                    }
                }
                if (duplicate != true) { //If there is no duplicate
                    owner.addBook(new Book(name, price)); 
                    model.addRow(new Object[]{name, price}); 
                }
            } catch (NumberFormatException ex) {
            }
        });

        middle.add(nameLabel);
        middle.add(nameField);
        middle.add(priceLabel);
        middle.add(priceField);
        middle.add(add);

        //bottom
        JButton delete = new JButton("Delete");
        JButton back = new JButton("Back");

        delete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) { 
                
                String name = (String) model.getValueAt(selectedRow, 0);
                double price = Double.parseDouble(model.getValueAt(selectedRow, 1).toString());

                //Remove from BookStore
                owner.getBookStore().getBooks().removeIf(c -> c.getName().equals(name) && c.getPrice() == price);
                //Remove from table
                model.removeRow(selectedRow);
            }
        });

        back.addActionListener(e -> {
            ownerOptions();
        });

        bottom.add(delete);
        bottom.add(back);

        panel.add(top);
        panel.add(middle);
        panel.add(bottom);

        reassemble(panel);

    }

    //The customer-start-screen
    private void customerOptions(Customer customer) {
        cleanup();
        JPanel panel = new JPanel();
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        JPanel bottom = new JPanel();

        //top
        JLabel welcome = new JLabel("Welcome " + customer.getUsername()
                + ". You have " + customer.getPoints() + " points. "
                + "Your status is " + customer.getStatus() + ".");
        top.add(welcome);
        String[] cols = {"Book Name", "Book Price", "Select"};
        
        //middle
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                //Column 0 = Book Name, Column 1 = Price, Column 2 = Select
                if (columnIndex == 2) {
                    return Boolean.class;//Forces Swing to draw a Checkbox because when boolean it draws checkbox
                }
                
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; //Sets only column 2 (the checkbox) to editable
            }
        };

        //Retrieve books and write to an array
        for (int i = 0; i < owner.getBookStore().getBooks().size(); i++) {
            String bookName = owner.getBookStore().getBooks().get(i).getName();
            double bookPrice = owner.getBookStore().getBooks().get(i).getPrice();
            Object data[] = {bookName, bookPrice, false};
            model.addRow(data);
        }
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        middle.setLayout(new BorderLayout());
        middle.add(scroll, BorderLayout.CENTER);

        //Bottom
        JButton buy = new JButton("Buy");
        JButton redeemPointsAndBuy = new JButton("Redeem Points and Buy");
        JButton logout = new JButton("Logout");

        bottom.add(buy);
        bottom.add(redeemPointsAndBuy);
        bottom.add(logout);

        buy.addActionListener(e -> {
            processPurchase(customer, model, false);
        });
        redeemPointsAndBuy.addActionListener(e -> {
            processPurchase(customer, model, true);
        });
        logout.addActionListener(e -> {
            loginScreen();

        });
        panel.setLayout(new GridLayout(3, 1)); // 3 equal parts
        panel.add(top);
        panel.add(middle);
        panel.add(bottom);

        reassemble(panel);
    }

    //The customer-cost-screen
    private void customerCost(Customer customer, double totalCost) {
        cleanup();
        JPanel panel = new JPanel();
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        JPanel bottom = new JPanel();
        //top
        JLabel totalCostLabel = new JLabel("Total Cost: " + String.valueOf(totalCost));
        top.add(totalCostLabel);

        //middle
        JLabel pointsAndStatus = new JLabel("Points: " + customer.getPoints() + " Status: " + customer.getStatus());
        middle.add(pointsAndStatus);

        //bottom
        JButton logout = new JButton("Logout");

        bottom.add(logout);
        logout.addActionListener(e -> {
            loginScreen();
        });
        panel.setLayout(new GridLayout(3, 1)); // 3 equal parts
        panel.add(top);
        panel.add(middle);
        panel.add(bottom);
        reassemble(panel);
    }

    //Used to handle purchases 
    public void processPurchase(Customer customer, DefaultTableModel model, boolean redeem) {
        boolean nothing = true;
        double totalCost = 0;
        // Loop through table to find checked boxes
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean isChecked = (boolean) model.getValueAt(i, 2);
            if (isChecked) {
                nothing = false;
                totalCost += (double) model.getValueAt(i, 1);
                String bookName = (String) model.getValueAt(i, 0);
                double bookPrice = (double) model.getValueAt(i, 1);
                owner.deleteBook(new Book(bookName, bookPrice));
            }
        }
        if (nothing == false) {
            double finalPrice = customer.getState().handlePurchase(customer, totalCost, redeem);
            customerCost(customer, finalPrice);
        }
    }

    public static void main(String[] args) {

        //The GUI works by creating a final project object which initializes the screen.
        //This is done because non static methods (methods containing the screens) cannot be instantated from this static main class.
        javax.swing.SwingUtilities.invokeLater(() -> {
            FinalProject a = new FinalProject();
        });

    }

}
