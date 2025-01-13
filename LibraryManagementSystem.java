import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

public class LibraryManagementSystem {

    // Book class
    static class Book {
        private int id;
        private String title;
        private String author;
        private String publisher;
        private boolean isAvailable = true; // Default to available
        private Date issueDate, returnDate;

        public Book(int id, String title, String author, String publisher) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.publisher = publisher;
        }

        public int getId() { 
            return id;
         }
        public String getTitle() { 
            return title;
         }
        public String getAuthor() { 
            return author;
         }
        public String getPublisher() { 
            return publisher;
         }
        public boolean isAvailable() { 
            return isAvailable;
         }

        public void issueBook() {
            if (isAvailable) {
                isAvailable = false;
                issueDate = new Date();
                returnDate = null;
            }
        }

        public void returnBook() {
            if (!isAvailable) {
                isAvailable = true;
                returnDate = new Date();
            }
        }

        @Override
        public String toString() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "ID: " + id +
                   "\nTitle: " + title +
                   "\nAuthor: " + author +
                   "\nPublisher: " + publisher +
                   "\nAvailable: " + isAvailable +
                   "\nIssue Date: " + (issueDate != null ? sdf.format(issueDate) : "N/A") +
                   "\nReturn Date: " + (returnDate != null ? sdf.format(returnDate) : "N/A") + "\n";
        }
    }

    // Member class
    static class Member {
        private int id;
        private String name;

        public Member(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { 
            return id; 
        }
        public String getName() { 
            return name;
         }

        @Override
        public String toString() {
            return "ID: " + id + "\nName: " + name + "\n";
        }
    }

    // Library class
    static class Library {
        private ArrayList<Book> books = new ArrayList<>();
        private ArrayList<Member> members = new ArrayList<>();
        private static final String BOOKS_FILE = "library_books.notes";
        private static final String MEMBERS_FILE = "library_members.notes";

        public void addBook(Book book) {
            books.add(book);
        }

        public void addMember(Member member) {
            members.add(member);
        }

        public ArrayList<Book> getBooks() { 
            return books;
         }
        public ArrayList<Member> getMembers() { 
            return members;
         }

        public Book getBookById(int id) {
            return books.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
        }

        public void saveData() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
                for (Book book : books) {
                    writer.write(book.toString());
                    writer.newLine();
                }
                System.out.println("Books saved successfully.");
            } catch (IOException e) {
                System.out.println("Error saving books: " + e.getMessage());
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBERS_FILE))) {
                for (Member member : members) {
                    writer.write(member.toString());
                    writer.newLine();
                }
                System.out.println("Members saved successfully.");
            } catch (IOException e) {
                System.out.println("Error saving members: " + e.getMessage());
            }
        }

        public void loadData() {
            try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");
                    int id = Integer.parseInt(parts[0].split(": ")[1]);
                    String title = parts[1].split(": ")[1];
                    String author = parts[2].split(": ")[1];
                    String publisher = parts[3].split(": ")[1];
                    books.add(new Book(id, title, author, publisher));
                }
                System.out.println("Books loaded successfully.");
            } catch (IOException e) {
                System.out.println("Error loading books: " + e.getMessage());
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(MEMBERS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");
                    int id = Integer.parseInt(parts[0].split(": ")[1]);
                    String name = parts[1].split(": ")[1];
                    members.add(new Member(id, name));
                }
                System.out.println("Members loaded successfully.");
            } catch (IOException e) {
                System.out.println("Error loading members: " + e.getMessage());
            }
        }
    }

    // GUI Class
    public static class LibraryGUI {
        private JFrame frame;
        private Library library;

        public LibraryGUI(Library library) {
            this.library = library;
            initialize();
        }

        private void initialize() {
            frame = new JFrame("Library Management System");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JLabel welcomeLabel = new JLabel("Welcome to the Library Management System", JLabel.CENTER);
            frame.add(welcomeLabel, BorderLayout.NORTH);

            JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));

            JButton viewBooksButton = new JButton("View All Books");
            JButton issueBookButton = new JButton("Issue a Book");
            JButton returnBookButton = new JButton("Return a Book");
            JButton searchBooksButton = new JButton("Search Books");
            JButton searchMembersButton = new JButton("Search Members");
            JButton saveDataButton = new JButton("Save Data");
            JButton loadDataButton = new JButton("Load Data");

            viewBooksButton.setBackground(new Color(255,218,185));
            issueBookButton.setBackground(new Color(255,218,185));
            returnBookButton.setBackground(new Color(255,218,185));
            searchBooksButton.setBackground(new Color(255,218,185));
            searchMembersButton.setBackground(new Color(255,218,185));
            saveDataButton.setBackground(new Color(255,218,185));
            loadDataButton.setBackground(new Color(255,218,185));
            
            panel.add(viewBooksButton);
            panel.add(issueBookButton);
            panel.add(returnBookButton);
            panel.add(searchBooksButton);
            panel.add(searchMembersButton);
            panel.add(saveDataButton);
            panel.add(loadDataButton);

            frame.add(panel, BorderLayout.CENTER);

            // Button listeners
            viewBooksButton.addActionListener(e -> viewBooks());
            issueBookButton.addActionListener(e -> issueBook());
            returnBookButton.addActionListener(e -> returnBook());
            searchBooksButton.addActionListener(e -> searchBooks());
            searchMembersButton.addActionListener(e -> searchMembers());
            saveDataButton.addActionListener(e -> library.saveData());
            loadDataButton.addActionListener(e -> library.loadData());

            frame.setVisible(true);
        }

        private void viewBooks() {
            StringBuilder booksText = new StringBuilder("Books in the Library:\n");
            for (Book book : library.getBooks()) {
                booksText.append(book.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, booksText.toString());
        }

        private void issueBook() {
            String bookIdStr = JOptionPane.showInputDialog(frame, "Enter Book ID to Issue:");
            try {
                int bookId = Integer.parseInt(bookIdStr);
                Book book = library.getBookById(bookId);
                if (book != null && book.isAvailable()) {
                    book.issueBook();
                    JOptionPane.showMessageDialog(frame, "Book issued successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Book is not available or already issued.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input!");
            }
        }

        private void returnBook() {
            String bookIdStr = JOptionPane.showInputDialog(frame, "Enter Book ID to Return:");
            try {
                int bookId = Integer.parseInt(bookIdStr);
                Book book = library.getBookById(bookId);
                if (book != null && !book.isAvailable()) {
                    book.returnBook();
                    JOptionPane.showMessageDialog(frame, "Book returned successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Book is not currently issued.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input!");
            }
        }

        private void searchBooks() {
            String keyword = JOptionPane.showInputDialog(frame, "Enter a keyword or Book ID to search:");
            try {
                int bookId = Integer.parseInt(keyword);
                Book book = library.getBookById(bookId);
                if (book != null) {
                    JOptionPane.showMessageDialog(frame, book.toString());
                } else {
                    JOptionPane.showMessageDialog(frame, "No book found with ID: " + bookId);
                }
            } catch (NumberFormatException e) {
                StringBuilder results = new StringBuilder("Search Results:\n");
                for (Book book : library.getBooks()) {
                   
                    if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                        book.getPublisher().toLowerCase().contains(keyword.toLowerCase())) {
                        results.append(book.toString()).append("\n");
                    }
                }
                if (results.length() > 0) {
                    JOptionPane.showMessageDialog(frame, results.toString());
                } else {
                    JOptionPane.showMessageDialog(frame, "No books found matching the keyword: " + keyword);
                }
            }
        }

        private void searchMembers() {
            String keyword = JOptionPane.showInputDialog(frame, "Enter a keyword or Member ID to search:");
            try {
                int memberId = Integer.parseInt(keyword);
                Member member = library.getMembers().stream()
                    .filter(m -> m.getId() == memberId)
                    .findFirst()
                    .orElse(null);
                if (member != null) {
                    JOptionPane.showMessageDialog(frame, member.toString());
                } else {
                    JOptionPane.showMessageDialog(frame, "No member found with ID: " + memberId);
                }
            } catch (NumberFormatException e) {
                StringBuilder results = new StringBuilder("Search Results:\n");
                for (Member member : library.getMembers()) {
                    if (member.getName().toLowerCase().contains(keyword.toLowerCase())) {
                        results.append(member.toString()).append("\n");
                    }
                }
                if (results.length() > 0) {
                    JOptionPane.showMessageDialog(frame, results.toString());
                } else {
                    JOptionPane.showMessageDialog(frame, "No members found matching the keyword: " + keyword);
                }
            }
        }
    }

    // Main function
    public static void main(String[] args) {
        Library library = new Library();

        // Add sample members
        library.addMember(new Member(1, "Admin"));
        library.addMember(new Member(2, "Ali"));

        // Add sample books
        library.addBook(new Book(1, "Java Programming", "Author Ali", "Publisher Abdullah"));
        library.addBook(new Book(2, "Python Programming", "Author Ahmad", "Publisher Haris"));
        library.addBook(new Book(3, "C++ Programming", "Author Saim", "Publisher Zohaib"));

        // Launch the GUI
        new LibraryGUI(library);
    }
}
