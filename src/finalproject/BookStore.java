package finalproject;

import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

//Object representation of a bookstore. Contains book storage and management functionality.
public class BookStore {

    private static BookStore instance = null;
    private ArrayList<Book> books = new ArrayList<>();

    private BookStore() {
    }

    public static BookStore getInstance() {
        if (instance == null) {
            instance = new BookStore();
        }
        return instance;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void deleteBook(Book book) {
        books.removeIf(b -> b.getName().equals(book.getName()) && b.getPrice() == book.getPrice());
    }

    public void saveBooksToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("books.txt"));
            
            for (Book b : books) {
                writer.println(b.getName() + "," + b.getPrice());
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
