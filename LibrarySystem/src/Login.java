import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Login {
    JFrame frame = new JFrame();
    JPanel header = new JPanel(new GridLayout(1, 1, 10, 30));
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                // Load image from project directory
                Image backgroundImage = ImageIO.read(new File("book.jpg"));
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to load background image.");
            }
        }
    };

    JLabel logIn = new JLabel("Library Login", JLabel.CENTER);
    JLabel username = new JLabel("Username:");
    JLabel password = new JLabel("Password:");
    JTextField txtUsername = new JTextField();
    JPasswordField txtPassword = new JPasswordField();
    JButton btnLog = new JButton("Login");
    JLabel togglePasswordLabel = new JLabel("Show Password", JLabel.RIGHT);
    boolean isPasswordVisible = false;

    // Database connection variables
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public void createGUI() {
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Font toggleLabelFont = new Font("Arial", Font.BOLD, 14);

        logIn.setFont(new Font("Arial", Font.BOLD, 40));
        logIn.setForeground(Color.BLACK);
        header.setBackground(Color.DARK_GRAY);
        header.add(logIn);

        panel.setLayout(null);

        username.setFont(labelFont);
        username.setForeground(Color.BLACK);
        username.setBounds(50, 100, 150, 30);
        panel.add(username);

        txtUsername.setFont(labelFont);
        txtUsername.setBounds(200, 100, 250, 30);
        panel.add(txtUsername);

        password.setFont(labelFont);
        password.setForeground(Color.BLACK);
        password.setBounds(50, 160, 150, 30);
        panel.add(password);

        txtPassword.setFont(labelFont);
        txtPassword.setBounds(200, 160, 250, 30);
        panel.add(txtPassword);

        togglePasswordLabel.setFont(toggleLabelFont);
        togglePasswordLabel.setForeground(Color.BLACK);
        togglePasswordLabel.setBounds(180, 190, 130, 20);
        togglePasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(togglePasswordLabel);

        togglePasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPasswordVisible) {
                    txtPassword.setEchoChar('*');
                    togglePasswordLabel.setText("Show Password");
                } else {
                    txtPassword.setEchoChar((char) 0);
                    togglePasswordLabel.setText("Hide Password");
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

        btnLog.setFont(buttonFont);
        btnLog.setForeground(Color.BLACK);
        btnLog.setBounds(200, 240, 150, 40);
        btnLog.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(btnLog);

        // Button action listener
        btnLog.addActionListener(e -> {
            String usernameInput = txtUsername.getText();
            String passwordInput = new String(txtPassword.getPassword());

            if (validateLogin(usernameInput, passwordInput)) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                frame.setVisible(false);
                new LibraryManagementPage();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.getContentPane().add(header, BorderLayout.NORTH);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("library.png").getImage());
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public boolean validateLogin(String username, String password) {
        if (!"admin".equals(username)) {
            // Username must be "admin"
            JOptionPane.showMessageDialog(frame, "Username must be 'admin'.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_system", "root", "12345678910");

            String query = "SELECT * FROM librarians WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Login log = new Login();
        log.createGUI();
    }
}
