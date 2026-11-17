# Library Management System

A desktop application for managing a small library's books, members and loans. Librarians sign in, add books and members, issue books to members, record returns and view which books are currently out on loan.

This project was built as Project Task 1 for the HexSoftwares Java internship.

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Design](#database-design)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Demo](#demo)
- [Known Limitations](#known-limitations)
- [Author](#author)

## About the Project

Small libraries often track their stock and loans on paper or in spreadsheets, which makes it hard to see what is available and who has borrowed what. This application replaces that with a single desktop tool backed by a MySQL database.

A librarian logs in with a username and password, and is then taken to a main screen where every day-to-day task is one button away. Book and member records are stored in the database, so the data persists between sessions.

## Features

- **Librarian login.** Access is restricted to librarians whose details are stored in the database. The password field includes a show/hide toggle.
- **Add books.** Record a new book by title and author. New books are marked as available by default.
- **Add members.** Register a member with a name, contact number and up to two address lines.
- **Issue books.** Issue a book to a member using the book ID and member ID. The loan date is recorded and the book is marked as issued.
- **Return books.** Record a return by book ID, which clears the loan and marks the book as available again.
- **View issued books.** See a table of every book currently on loan, along with the borrowing member's name and the issue date.
- **Input validation.** Book titles, author names and member names accept letters and spaces only. Contact numbers must be exactly ten digits.
- **Parameterised queries.** All data entry uses prepared statements to guard against SQL injection.

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Java |
| User interface | Java Swing (`javax.swing`, `java.awt`) |
| Database | MySQL |
| Database connectivity | JDBC with MySQL Connector/J 9.0.0 |
| IDE / project format | IntelliJ IDEA |

## Project Structure

```
HexSoftwares_Library_Management_System/
├── Database(MYSQL)                  SQL script to create the schema and seed data
├── README.md
└── LibrarySystem/
    ├── lib/
    │   └── mysql-connector-j-9.0.0.jar
    ├── src/
    │   ├── Login.java               Login screen and entry point
    │   ├── LibraryManagementPage.java   Main window and button actions
    │   ├── Library.java             Database operations for books, members and loans
    │   ├── Book.java                Book model
    │   └── Member.java              Member model
    ├── book.jpg                     Login screen background
    ├── centerPanel.jpg              Main screen background
    ├── library.png                  Window icon
    └── LibrarySystem.iml
```

## Database Design

The database is called `library_system` and contains four tables.

| Table | Purpose | Key columns |
| --- | --- | --- |
| `books` | Every book in the library | `book_id`, `title`, `author`, `is_issued` |
| `members` | Registered library members | `member_id`, `name`, `contact_number`, `address_line1`, `address_line2` |
| `librarians` | Staff accounts that can sign in | `id`, `full_names`, `contact_number`, `username`, `password` |
| `issued_books` | Books currently on loan | `issue_id`, `book_id`, `member_id`, `issue_date`, `return_date` |

`issued_books` links to `books` and `members` through foreign keys.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or later
- MySQL Server 8 or later
- IntelliJ IDEA (recommended), or any other way of compiling and running Java

### 1. Clone the repository

```bash
git clone https://github.com/sellwane7/HexSoftwares_Library_Management_System.git
cd HexSoftwares_Library_Management_System
```

### 2. Set up the database

Run the script in the file named `Database(MYSQL)` against your MySQL server. For example, from the MySQL command line:

```sql
SOURCE Database(MYSQL);
```

You can also open the file in MySQL Workbench and execute it. The script creates the `library_system` database, its four tables and two librarian accounts.

### 3. Configure the database connection

The connection details are set directly in the source code. Update the JDBC URL, username and password to match your own MySQL setup in each of these files:

- `LibrarySystem/src/Login.java`
- `LibrarySystem/src/Library.java`
- `LibrarySystem/src/LibraryManagementPage.java`

The connection string looks like this:

```java
DriverManager.getConnection("jdbc:mysql://localhost:3306/library_system", "your_mysql_user", "your_mysql_password");
```

### 4. Run the application

**Using IntelliJ IDEA**

1. Open the `LibrarySystem` folder as a project.
2. Confirm that `lib/mysql-connector-j-9.0.0.jar` is on the project's classpath. It is already declared in `LibrarySystem.iml`.
3. Set the working directory of the run configuration to the `LibrarySystem` folder, so that the image files can be found.
4. Run the `Login` class.

**Using the command line**

From inside the `LibrarySystem` folder:

```bash
# Compile
javac -cp "lib/mysql-connector-j-9.0.0.jar" -d out/production/LibrarySystem src/*.java

# Run (use a semicolon instead of a colon on Windows)
java -cp "out/production/LibrarySystem:lib/mysql-connector-j-9.0.0.jar" Login
```

## Usage

1. Start the application. The login window opens.
2. Sign in with the username `admin` and the password of one of the librarian accounts created by the database script.
3. From the main window, use the buttons on the left:
   - **Add New Book** asks for a title and an author.
   - **Add New Member** asks for a name, a ten-digit contact number and the address lines.
   - **Issue Book to Member** asks for a book ID and a member ID.
   - **Return Book** asks for the ID of the book being returned.
   - **Display Issued Books** shows all books currently on loan.
4. Results are shown in the table in the centre of the window.

## Demo

_A demo of the application will be added here._

<!-- Suggested content for later: a short screen recording or GIF of the login and main screens, plus screenshots of issuing and returning a book. -->

## Known Limitations

These are the areas planned for improvement:

- Database credentials are written directly in the source code. They should be moved to a configuration file or environment variables.
- Librarian passwords are stored and compared as plain text. They should be hashed, for example with bcrypt.
- The login check only accepts the username `admin`.
- Returning a book deletes the loan record, so there is no loan history. The `return_date` column exists in the schema but is not yet used.
- Issuing a book does not check whether the book is already on loan.
- Book titles and author names accept letters and spaces only, so titles containing numbers or punctuation are rejected.
- There is no search, edit or delete function for books and members.

## Author

**Joyce Sellwane Mosia**

- GitHub: [github.com/sellwane7](https://github.com/sellwane7)
- Portfolio: [sellwane7.github.io/personalportfolio](https://sellwane7.github.io/personalportfolio)
