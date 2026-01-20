package com.myLibrary.app;

import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Book class
class Book {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;
    private LocalDate dueDate;
    private String borrowerId;

    public Book(String id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
        this.dueDate = null;
        this.borrowerId = null;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return isAvailable; }
    public LocalDate getDueDate() { return dueDate; }
    public String getBorrowerId() { return borrowerId; }

    public void setAvailable(boolean available) { isAvailable = available; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setBorrowerId(String borrowerId) { this.borrowerId = borrowerId; }

    @Override
    public String toString() {
        return String.format("ID: %s | Title: %-25s | Author: %-20s | ISBN: %s | Available: %s%s",
                id, title, author, isbn, isAvailable ? "Yes" : "No",
                !isAvailable ? " (Due: " + dueDate + ")" : "");
    }
}

// Member class
class Member {
    private String id;
    private String name;
    private String email;
    private String phone;
    private List<Book> borrowedBooks;
    private double fines;

    public Member(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.borrowedBooks = new ArrayList<>();
        this.fines = 0.0;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }
    public double getFines() { return fines; }
    
    public void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }
    
    public void removeBorrowedBook(Book book) {
        borrowedBooks.remove(book);
    }
    
    public void addFine(double amount) {
        fines += amount;
    }
    
    public void payFine(double amount) {
        fines = Math.max(0, fines - amount);
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %-20s | Email: %-25s | Phone: %s | Books Borrowed: %d | Fines: $%.2f",
                id, name, email, phone, borrowedBooks.size(), fines);
    }
}

// Library Management System
class Library {
    private Map<String, Book> books;
    private Map<String, Member> members;
    private int bookCounter;
    private int memberCounter;
    private static final int MAX_BORROW_DAYS = 14;
    private static final double DAILY_FINE = 0.50;

    public Library() {
        books = new HashMap<>();
        members = new HashMap<>();
        bookCounter = 1;
        memberCounter = 1;
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Add some sample books
        addBook("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565");
        addBook("To Kill a Mockingbird", "Harper Lee", "9780446310789");
        addBook("1984", "George Orwell", "9780451524935");
        addBook("Pride and Prejudice", "Jane Austen", "9780141439518");
        addBook("The Hobbit", "J.R.R. Tolkien", "9780547928227");

        // Add some sample members
        registerMember("John Smith", "john@email.com", "555-0101");
        registerMember("Emma Johnson", "emma@email.com", "555-0102");
        registerMember("Robert Brown", "robert@email.com", "555-0103");
    }

    // Book Management
    public void addBook(String title, String author, String isbn) {
        String id = "B" + String.format("%03d", bookCounter++);
        Book book = new Book(id, title, author, isbn);
        books.put(id, book);
        System.out.println("✓ Book added successfully: " + title);
    }

    public Book findBook(String searchTerm) {
        for (Book book : books.values()) {
            if (book.getId().equalsIgnoreCase(searchTerm) ||
                book.getTitle().equalsIgnoreCase(searchTerm) ||
                book.getIsbn().equals(searchTerm)) {
                return book;
            }
        }
        return null;
    }

    public void displayAllBooks() {
        System.out.println("\n=== ALL BOOKS IN LIBRARY ===");
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
        } else {
            books.values().forEach(System.out::println);
        }
        System.out.println("Total Books: " + books.size());
    }

    public void displayAvailableBooks() {
        System.out.println("\n=== AVAILABLE BOOKS ===");
        boolean found = false;
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books available at the moment.");
        }
    }

    // Member Management
    public void registerMember(String name, String email, String phone) {
        String id = "M" + String.format("%03d", memberCounter++);
        Member member = new Member(id, name, email, phone);
        members.put(id, member);
        System.out.println("✓ Member registered successfully: " + name + " (ID: " + id + ")");
    }

    public Member findMember(String searchTerm) {
        for (Member member : members.values()) {
            if (member.getId().equalsIgnoreCase(searchTerm) ||
                member.getName().equalsIgnoreCase(searchTerm) ||
                member.getEmail().equalsIgnoreCase(searchTerm)) {
                return member;
            }
        }
        return null;
    }

    public void displayAllMembers() {
        System.out.println("\n=== ALL MEMBERS ===");
        if (members.isEmpty()) {
            System.out.println("No members registered.");
        } else {
            members.values().forEach(System.out::println);
        }
        System.out.println("Total Members: " + members.size());
    }

    // Borrow and Return Operations
    public void borrowBook(String memberId, String bookId) {
        Member member = members.get(memberId);
        Book book = books.get(bookId);

        if (member == null) {
            System.out.println("✗ Error: Member not found!");
            return;
        }
        if (book == null) {
            System.out.println("✗ Error: Book not found!");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("✗ Error: Book is already borrowed!");
            return;
        }
        if (member.getBorrowedBooks().size() >= 5) {
            System.out.println("✗ Error: Member has reached the maximum borrowing limit (5 books)!");
            return;
        }
        if (member.getFines() > 0) {
            System.out.println("✗ Error: Member has outstanding fines! Please pay fines first.");
            return;
        }

        // Process borrowing
        book.setAvailable(false);
        book.setDueDate(LocalDate.now().plusDays(MAX_BORROW_DAYS));
        book.setBorrowerId(memberId);
        member.addBorrowedBook(book);

        System.out.println("\n✓ Book borrowed successfully!");
        System.out.println("Book: " + book.getTitle());
        System.out.println("Borrower: " + member.getName());
        System.out.println("Due Date: " + book.getDueDate());
    }

    public void returnBook(String bookId) {
        Book book = books.get(bookId);
        
        if (book == null) {
            System.out.println("✗ Error: Book not found!");
            return;
        }
        if (book.isAvailable()) {
            System.out.println("✗ Error: This book is not currently borrowed!");
            return;
        }

        Member member = members.get(book.getBorrowerId());
        LocalDate returnDate = LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(book.getDueDate(), returnDate);
        double fine = 0;

        if (daysOverdue > 0) {
            fine = daysOverdue * DAILY_FINE;
            member.addFine(fine);
            System.out.println("⚠ Book is overdue by " + daysOverdue + " days!");
            System.out.println("Fine imposed: $" + String.format("%.2f", fine));
        }

        // Process return
        book.setAvailable(true);
        book.setDueDate(null);
        book.setBorrowerId(null);
        member.removeBorrowedBook(book);

        System.out.println("\n✓ Book returned successfully!");
        System.out.println("Book: " + book.getTitle());
        System.out.println("Returned by: " + member.getName());
        if (fine > 0) {
            System.out.println("Current total fines for member: $" + String.format("%.2f", member.getFines()));
        }
    }

    public void payFine(String memberId, double amount) {
        Member member = members.get(memberId);
        if (member == null) {
            System.out.println("✗ Error: Member not found!");
            return;
        }
        
        double oldFine = member.getFines();
        member.payFine(amount);
        double newFine = member.getFines();
        
        System.out.println("✓ Payment processed successfully!");
        System.out.println("Amount paid: $" + String.format("%.2f", amount));
        System.out.println("Previous fine: $" + String.format("%.2f", oldFine));
        System.out.println("Remaining fine: $" + String.format("%.2f", newFine));
    }

    public void searchBook(String query) {
        System.out.println("\n=== SEARCH RESULTS FOR: '" + query + "' ===");
        boolean found = false;
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                book.getIsbn().contains(query)) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found matching your search.");
        }
    }

    public void displayOverdueBooks() {
        System.out.println("\n=== OVERDUE BOOKS ===");
        boolean found = false;
        LocalDate today = LocalDate.now();
        
        for (Book book : books.values()) {
            if (!book.isAvailable() && book.getDueDate() != null && 
                book.getDueDate().isBefore(today)) {
                Member member = members.get(book.getBorrowerId());
                long daysOverdue = ChronoUnit.DAYS.between(book.getDueDate(), today);
                double fine = daysOverdue * DAILY_FINE;
                
                System.out.println("Book: " + book.getTitle());
                System.out.println("  Borrower: " + member.getName());
                System.out.println("  Due Date: " + book.getDueDate());
                System.out.println("  Days Overdue: " + daysOverdue);
                System.out.println("  Fine Amount: $" + String.format("%.2f", fine));
                System.out.println("-----------------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No overdue books at the moment.");
        }
    }
}

// Main Application
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        
        System.out.println("=========================================");
        System.out.println("    LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=========================================");
        
        while (true) {
            displayMenu();
            System.out.print("\nEnter your choice (1-10): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        library.displayAllBooks();
                        break;
                    case 2:
                        library.displayAvailableBooks();
                        break;
                    case 3:
                        System.out.print("\nEnter book title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter author: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter ISBN: ");
                        String isbn = scanner.nextLine();
                        library.addBook(title, author, isbn);
                        break;
                    case 4:
                        System.out.print("\nEnter member name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter phone: ");
                        String phone = scanner.nextLine();
                        library.registerMember(name, email, phone);
                        break;
                    case 5:
                        System.out.print("\nEnter member ID: ");
                        String memberId = scanner.nextLine();
                        System.out.print("Enter book ID: ");
                        String bookId = scanner.nextLine();
                        library.borrowBook(memberId, bookId);
                        break;
                    case 6:
                        System.out.print("\nEnter book ID to return: ");
                        String returnBookId = scanner.nextLine();
                        library.returnBook(returnBookId);
                        break;
                    case 7:
                        System.out.print("\nEnter search term (title/author/ISBN): ");
                        String searchTerm = scanner.nextLine();
                        library.searchBook(searchTerm);
                        break;
                    case 8:
                        library.displayOverdueBooks();
                        break;
                    case 9:
                        System.out.print("\nEnter member ID: ");
                        String fineMemberId = scanner.nextLine();
                        System.out.print("Enter payment amount: $");
                        double amount = scanner.nextDouble();
                        library.payFine(fineMemberId, amount);
                        break;
                    case 10:
                        library.displayAllMembers();
                        break;
                    case 0:
                        System.out.println("\nThank you for using Library Management System!");
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 0-10.");
                }
                
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. View All Books");
        System.out.println("2. View Available Books");
        System.out.println("3. Add New Book");
        System.out.println("4. Register New Member");
        System.out.println("5. Borrow a Book");
        System.out.println("6. Return a Book");
        System.out.println("7. Search Books");
        System.out.println("8. View Overdue Books");
        System.out.println("9. Pay Fines");
        System.out.println("10. View All Members");
        System.out.println("0. Exit");
        System.out.println("================================");
    }
}