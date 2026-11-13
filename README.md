# Bookstore App

A single-window desktop bookstore application written in **Java** with a **Swing** graphical interface. An owner manages the book catalogue and the registered customers, and customers log in to buy books and earn loyalty points that move them between **Silver** and **Gold** status. Customer status is modelled with the **State design pattern**.

Built for COE528 (Object-Oriented Analysis and Design) at Toronto Metropolitan University, Winter 2026.

## Features

- **One window only**: every screen replaces the previous one in the same window, like a web browser, and no extra windows pop up
- **Owner and customer logins** from a single login screen
- **Owner screens**: add and delete books (name and price), and add and delete customers (username and password, starting at 0 points)
- **Customer screen**: a table of available books with checkboxes, a welcome message showing the customer's points and status, and two ways to pay
- **Loyalty points**: paying with **Buy** earns 10 points for every CAD spent, and **Redeem points and Buy** uses points instead, with every 100 points redeemed taking 1 CAD off the cost (the cost never goes below 0)
- **Status levels**: under 1000 points is **Silver** and 1000 points or more is **Gold**
- **Saved data**: books and customers are written to `books.txt` and `customers.txt` when the window is closed, and loaded again the next time the app starts

## How to Use

1. Start the app (see below) and log in.
2. **Owner**: username `admin`, password `admin`. From the owner start screen, open **Books** or **Customers** to add or delete entries, then use **Back** or **Logout**.
3. **Customer**: log in with an account the owner created. The included sample data has a customer `john` with password `123`.
4. Tick the books to buy and click **Buy** or **Redeem points and Buy**. The cost screen shows the total cost, the points, and the status.

### Points example

With books priced 50, 100, 200, and 500:

| Step | Action | Total cost | Points after | Status |
| --- | --- | --- | --- | --- |
| 1 | Buy the 200 and 500 books | 700 | 7000 | Gold |
| 2 | Redeem points and buy the 50 book | 0 | 2000 | Gold |
| 3 | Redeem points and buy the 100 book | 80 | 800 | Silver |

## Getting Started

### Requirements

- Java Development Kit (JDK) 8 or newer. The app uses only the standard library (Swing/AWT), so there is nothing else to install.

### Run from the command line

Run these from the repository root, because the app reads and writes `books.txt` and `customers.txt` in the current folder:

```bash
mkdir out
javac -d out src/finalproject/*.java
java -cp out finalproject.FinalProject
```

### Run in NetBeans

Open the repository folder as a NetBeans project (an Ant project, not Maven or Gradle) and run `FinalProject`.

## Design

### State design pattern

A customer's status (Silver or Gold) decides how a purchase is handled, so each status is its own class instead of an `if (points >= 1000)` check inside `Customer`.

| Class | Role |
| --- | --- |
| `State` | Interface with `handlePurchase`, `updateState`, and `getStatusName` |
| `SilverState` | Handles purchases for customers under 1000 points and moves them to Gold when they reach 1000 |
| `GoldState` | Handles purchases for customers at 1000 points or more and moves them back to Silver when they drop below 1000 |
| `Customer` | Holds the current `State` and delegates purchases to it |

Adding a new status, such as Diamond, only needs a new class that implements `State`, with no changes to `Customer`.

### Other classes

| Class | Role |
| --- | --- |
| `FinalProject` | The Swing window and all screens (login, owner books and customers, customer purchase, cost) |
| `User` | Base class holding a username and password |
| `Owner` | The store owner, who manages customers and books and loads and saves the data files |
| `Customer` | A registered customer with points and a status |
| `BookStore` | Singleton that holds the catalogue of books and saves it to `books.txt` |
| `Book` | A book with a name and a price |

### UML diagrams

Use case diagram:

![Use case diagram](docs/use-case-diagram.png)

Class diagram (the `Customer`, `State`, `SilverState`, and `GoldState` classes take part in the State pattern):

![Class diagram](docs/class-diagram.png)

## Data Files

Both files are plain text with one record per line:

- `books.txt`: `name,price`
- `customers.txt`: `username,password,points`

The files in this repository are sample data.

## Project Structure

```text
src/finalproject/   Java source code
docs/               UML diagrams (PNG)
nbproject/          NetBeans (Ant) project configuration
books.txt           Sample book data
customers.txt       Sample customer data
```
