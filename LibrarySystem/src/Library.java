import java.sql.*;
import java.util.*;

public class Library {
    private Connection conn;

    public Library() {
        try {
            // Establish database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_system", "root", "12345678910");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: Database connection failed.");
        }
    }

    // Add a new book to the database
    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, is_issued) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setBoolean(3, book.isIssued());
        stmt.executeUpdate();
    }

    // Retrieve all books from the database
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            int bookId = rs.getInt("book_id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            boolean isIssued = rs.getBoolean("is_issued");

            books.add(new Book(bookId, title, author, isIssued));
        }

        return books;
    }

    // Issue a book to a member
    public void issueBook(int bookId, int memberId) throws SQLException {
        String issueQuery = "INSERT INTO issued_books (book_id, member_id, issue_date) VALUES (?, ?, NOW())";
        PreparedStatement issueStmt = conn.prepareStatement(issueQuery);
        issueStmt.setInt(1, bookId);
        issueStmt.setInt(2, memberId);
        issueStmt.executeUpdate();

        String updateQuery = "UPDATE books SET is_issued = true WHERE book_id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
        updateStmt.setInt(1, bookId);
        updateStmt.executeUpdate();
    }

    // Return a book
    public void returnBook(int bookId) throws SQLException {
        String deleteIssueQuery = "DELETE FROM issued_books WHERE book_id = ?";
        PreparedStatement deleteStmt = conn.prepareStatement(deleteIssueQuery);
        deleteStmt.setInt(1, bookId);
        deleteStmt.executeUpdate();

        String updateQuery = "UPDATE books SET is_issued = false WHERE book_id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
        updateStmt.setInt(1, bookId);
        updateStmt.executeUpdate();
    }

    // Add a new member to the database
    public void addMember(Member member) throws SQLException {
        String query = "INSERT INTO members (name, contact_number, address_line1, address_line2) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, member.getName());
        stmt.setString(2, member.getContactNumber());
        stmt.setString(3, member.getAddressLine1());
        stmt.setString(4, member.getAddressLine2());
        stmt.executeUpdate();
    }

    // Get all members from the database
    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            int memberId = rs.getInt("member_id");
            String name = rs.getString("name");
            String contactNumber = rs.getString("contact_number");
            String addressLine1 = rs.getString("address_line1");
            String addressLine2 = rs.getString("address_line2");

            members.add(new Member(memberId, name, contactNumber, addressLine1, addressLine2));
        }

        return members;
    }
}
