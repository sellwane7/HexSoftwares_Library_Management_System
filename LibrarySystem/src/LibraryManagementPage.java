import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LibraryManagementPage {

    JFrame frame = new JFrame();
    JButton addBookButton = new JButton("Add New Book");
    JButton issueBookButton = new JButton("Issue Book to Member");
    JButton returnBookButton = new JButton("Return Book");
    JButton displayBooksButton = new JButton("Display Issued Books");
    JButton addMemberButton = new JButton("Add New Member");
    JPanel topPanel = new JPanel();
    JLabel heading = new JLabel("Welcome to the Library Management System");
    JPanel panel = new JPanel();
    JPanel centerPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                // Load image from project directory
                Image backgroundImage = ImageIO.read(new File("centerPanel.jpg"));
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to load background image.");
            }
        }
    };
    private JTable booksTable;
    private final Library library;
    private Connection conn;

    public LibraryManagementPage() {
        library = new Library();  // Initialize the library object

        // Database Connection Setup
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_system", "root", "12345678910");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database connection failed.");
            return;
        }

        // GUI setup
        setupGUI();

        // Add action listeners for buttons
        addBookButton.addActionListener(e -> addBook());
        issueBookButton.addActionListener(e -> issueBook());
        returnBookButton.addActionListener(e -> returnBook());
        displayBooksButton.addActionListener(e -> displayBooks());
        addMemberButton.addActionListener(e -> addMember());

        frame.setVisible(true);
    }

    private void setupGUI() {
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("library.png").getImage());
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());

        // Top panel
        topPanel.setLayout(new GridLayout(1, 1));
        topPanel.setBackground(Color.BLUE);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(Color.BLACK);
        topPanel.add(heading);

        // Side panel with background image
        panel.setLayout(new GridLayout(5, 1, 20, 20));
        panel.setBackground(Color.BLUE);

        // Add buttons to side panel
        addBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        issueBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        displayBooksButton.setFont(new Font("Arial", Font.BOLD, 16));
        addMemberButton.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(addBookButton);
        panel.add(issueBookButton);
        panel.add(returnBookButton);
        panel.add(displayBooksButton);
        panel.add(addMemberButton);

        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(panel, BorderLayout.WEST);

        booksTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(booksTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
    }
    private boolean isValidText(String text) {
        return !text.matches("[a-zA-Z\\s]+");
    }

    // Add Book action
    private void addBook() {
        displayBooksInTable();
        String title = JOptionPane.showInputDialog(frame, "Enter book title:");
        String author = JOptionPane.showInputDialog(frame, "Enter book author:");

        if (title == null || title.isEmpty() || isValidText(title)) {
            JOptionPane.showMessageDialog(frame, "Invalid title. Only letters and spaces are allowed.");
            return;
        }

        if (author == null || author.isEmpty() || isValidText(author)) {
            JOptionPane.showMessageDialog(frame, "Invalid author name. Only letters and spaces are allowed.");
            return;
        }

        try {
            Book newBook = new Book(0, title, author, false);
            library.addBook(newBook);
            JOptionPane.showMessageDialog(frame, "Book added successfully!");
            displayBooksInTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to add book.");
        }
    }

    // Issue Book action
    private void issueBook() {
        String bookId = JOptionPane.showInputDialog(frame, "Enter Book ID to issue:");
        String memberId = JOptionPane.showInputDialog(frame, "Enter Member ID:");

        try {
            library.issueBook(Integer.parseInt(bookId), Integer.parseInt(memberId));
            JOptionPane.showMessageDialog(frame, "Book issued successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to issue book.");
        }
    }

    // Return Book action
    private void returnBook() {
        displayBooksInTable();
        String bookId = JOptionPane.showInputDialog(frame, "Enter Book ID to return:");

        try {
            library.returnBook(Integer.parseInt(bookId));
            JOptionPane.showMessageDialog(frame, "Book returned successfully!");
            displayBooksInTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to return book.");
        }
    }

    // Display Books in table
    private void displayBooks() {
        try {
            displayIssuedBooksInTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error displaying issued books.");
        }
    }

    // Add Member action
    private void addMember() {
        displayMembersInTable();
        String name = JOptionPane.showInputDialog(frame, "Enter member's name:");
        String contactNumber = JOptionPane.showInputDialog(frame, "Enter member's contact number:");
        String addressLine1 = JOptionPane.showInputDialog(frame, "Enter member's address line1:");
        String addressLine2 = JOptionPane.showInputDialog(frame, "Enter member's address line2:");

        if (name == null || name.isEmpty() || isValidText(name)) {
            JOptionPane.showMessageDialog(frame, "Invalid name. Only letters and spaces are allowed.");
            return;
        }

        if (contactNumber == null || contactNumber.length() != 10 || !contactNumber.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(frame, "Invalid contact number. It must be exactly 10 digits.");
            return;
        }

        try {
            Member newMember = new Member(0, name, contactNumber, addressLine1, addressLine2);
            library.addMember(newMember);
            JOptionPane.showMessageDialog(frame, "Member added successfully!");
            displayMembersInTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to add member.");
        }
    }
    private void displayBooksInTable() {
        try {
            List<Book> books = library.getAllBooks();
            String[] columnNames = {"Book ID", "Title", "Author", "Issued"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (Book book : books) {
                model.addRow(new Object[] {
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.isIssued() ? "Yes" : "No"
                });
            }

            booksTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error displaying books.");
        }
    }
    private void displayIssuedBooksInTable() throws SQLException {
        try {
            String query = "SELECT b.book_id AS BookID, b.title AS Title, b.author AS Author, m.name AS MemberName, ib.issue_date AS IssueDate " +
                    "FROM books b " +
                    "JOIN issued_books ib ON b.book_id = ib.book_id " +
                    "JOIN members m ON ib.member_id = m.member_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            String[] columnNames = {"Book ID", "Title", "Author", "Member Name", "Issue Date"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("BookID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("MemberName"),
                        rs.getDate("IssueDate")
                });
            }

            booksTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error displaying issued books.");
        }
    }
    private void displayMembersInTable() {
        try {
            String query = "SELECT * FROM members";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            String[] columnNames = {"Member ID", "Name", "Contact Number", "Address Line 1", "Address Line 2"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                int memberId = rs.getInt("member_id");
                String name = rs.getString("name");
                String contactNumber = rs.getString("contact_number");
                String addressLine1 = rs.getString("address_line1");
                String addressLine2 = rs.getString("address_line2");

                model.addRow(new Object[]{memberId, name, contactNumber, addressLine1, addressLine2});
            }

            booksTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error displaying members.");
        }
    }

    public static void main(String[] args) {
        new LibraryManagementPage();
    }
}
