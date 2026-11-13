package finalproject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.File;                  
import java.io.FileNotFoundException; 
import java.util.Scanner; 

public class Owner extends User {

    private ArrayList<Customer> customers = new ArrayList();
    private BookStore bookstore;
    private boolean loaded = false;

    public Owner(String usr, String pwd) {
        super(usr, pwd);
        this.bookstore = BookStore.getInstance();
    }

    public BookStore getBookStore() {
        return bookstore;
    }

    public void addCustomer(String usr, String pwd) {
        customers.add(new Customer(usr, pwd));

    }

    public void deleteCustomer(String usr) {
        for(int i = 0; i < this.getCustomers().size(); i++){
            if(this.getCustomers().get(i).getUsername().equals(usr)){
                this.getCustomers().remove(i);
                break;
            }        
        }
    }
    
    public void addBook(Book book) {
        bookstore.addBook(book);
    }

    public void deleteBook(Book book) {
        bookstore.deleteBook(book);
    }

    public void saveData() {
        bookstore.saveBooksToFile();
        this.saveCustomersToFile();
    }
    
    public void loadData(){
        if (this.loaded == false){
        loadCustomers();
        loadBooks();
        this.loaded = true;
       }
    }
    
   public void loadCustomers(){
        File file = new File("customers.txt");

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                int indexA = data.indexOf(",");
                String usrC = data.substring(0,indexA);
                
                data = data.substring(indexA + 1);
                indexA = data.indexOf(",");
                String pwdC = data.substring(0, indexA);
                
                data = data.substring(indexA + 1);
                int pts = Integer.parseInt(data);
                
                this.addCustomer(usrC, pwdC);
                this.getCustomers().get(this.getCustomers().size() - 1).addPoints(pts);
                if (pts > 999) this.getCustomers().get(this.getCustomers().size() - 1).setState(new GoldState());
                else this.getCustomers().get(this.getCustomers().size() - 1).setState(new SilverState());
            }
        } catch (FileNotFoundException e) {
        }
    }
    
    public void loadBooks(){
        File file = new File("books.txt");
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                int index = data.indexOf(",");
                String name = data.substring(0,index);
                String priceString = data.substring(index + 1);
                double price = Double.parseDouble(priceString);
                this.addBook(new Book(name,price));
            }
        } catch (FileNotFoundException e) {
        }

    }
    //Save file format: usr,pwd,pts
     public void saveCustomersToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("customers.txt"));
            for (Customer x : this.getCustomers()) {
                writer.println(x.getUsername() + "," + x.getPassword() + "," + x.getPoints());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}
