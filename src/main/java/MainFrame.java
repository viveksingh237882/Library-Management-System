/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author NARENDER KESWANI
 */
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;

public class MainFrame {

    public static Connection connect() {
        //Making Database Connection once & using multiple times whenenver required.
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3308/library", "root", "Narend-10");
            return con;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void loginFn() {

        //Creating Login Frame
        JFrame loginFrame = new JFrame("Login");

        //Creating label Username
        JLabel l1 = new JLabel("Username", SwingConstants.CENTER);

        //Creating label Password
        JLabel l2 = new JLabel("Password", SwingConstants.CENTER);

        //Setting up opaque so that label component paints every pixel within its bounds.
        l1.setOpaque(true);

        //Setting up background color of label.
        l1.setBackground(new Color(51, 35, 85));

        //Setting up foreground color of label.
        l1.setForeground(Color.white);

        //Setting up opaque so that label component paints every pixel within its bounds.
        l2.setOpaque(true);

        //Setting up background color of label.
        l2.setBackground(new Color(51, 35, 85));

        //Setting up foreground color of label.
        l2.setForeground(Color.white);

        //Create textfield Username
        JTextField usernameTF = new JTextField();

        //Setting up background color of textfield.
        usernameTF.setBackground(new Color(51, 35, 85));

        //Setting up foreground color of textfield.
        usernameTF.setForeground(Color.white);

        //Create textfield Password
        JPasswordField passwordTF = new JPasswordField();

        //Setting up background color of textfield.
        passwordTF.setBackground(new Color(51, 35, 85));

        //Setting up foreground color of textfield.
        passwordTF.setForeground(Color.white);

        //Create button Login
        JButton loginBtn = new JButton("Login");

        //Setting up background color of button.
        loginBtn.setBackground(new Color(124, 85, 227));

        //Setting up foreground color of button.
        loginBtn.setForeground(Color.white);

        //Create button cancel
        JButton cancelBtn = new JButton("Cancel");

        //Setting up background color of button.
        cancelBtn.setBackground(new Color(124, 85, 227));

        //Setting up foreground color of button.
        cancelBtn.setForeground(Color.white);

        //Performing action on button.
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameTF.getText();
                String password = passwordTF.getText();

                //If username is empty
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter username"); //Display dialog box with the message
                } //If password is empty
                else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter password"); //Display dialog box with the message
                } //If both the fields are present then to login the user, check wether the user exists already
                else {
                    //Connect to the database
                    Connection connection = connect();
                    try {
                        Statement stmt = connection.createStatement();
                        String st = ("SELECT * FROM USERS WHERE USERNAME='" + username + "' AND PASSWORD='" + password + "'"); //Retreive username and passwords from users
                        ResultSet rs = stmt.executeQuery(st); //Execute query
                        if (rs.next() == false) { //Move pointer below
                            JOptionPane.showMessageDialog(null, "Invalid Username/Password!"); //Display Message
                        } else {
                            loginFrame.dispose();
                            rs.beforeFirst();  //Move the pointer above
                            while (rs.next()) {
                                String admin = rs.getString("user_type"); //user is admin
                                System.out.println(admin);
                                String UID = rs.getString("UID"); //Get user ID of the user
                                if (admin.equals("1")) { //If boolean value 1
                                    //Redirecting to Librarian Frame
                                    librarian_frame();
                                } else {
                                    //Redirecting to User Frame for that user ID
                                    user_frame(UID);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();
            }
        });

        //Adding all login components in login frame. 
        loginFrame.add(l1);
        loginFrame.add(usernameTF);
        loginFrame.add(l2);
        loginFrame.add(passwordTF);
        loginFrame.add(loginBtn);
        loginFrame.add(cancelBtn);

        //Setting size of frame (width, height)
        loginFrame.setSize(330, 180);//400 width and 500 height 

        //Setting layout of the frame
        loginFrame.setLayout(new GridLayout(3, 2));

        //Setting frame visible to the user
        loginFrame.setVisible(true);

        //Setting frame non-resizable
        loginFrame.setResizable(false);

    }

    public static void user_frame(String UID) {

        //Creating Frame for Student
        JFrame studentFrame = new JFrame("Student Functions");

        //Creating button
        JButton view_books_btn = new JButton("View Books");

        //Setting Background Colour of the button.
        view_books_btn.setBackground(new Color(51, 35, 85));

        //Setting Foreground Colour of the button.
        view_books_btn.setForeground(Color.white);

        view_books_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating Frame.
                JFrame viewBooksUserFrame = new JFrame("Books Available");

                //Connection to database.
                Connection connection = connect();

                //Query for retreiving data from database.
                String sql = "select * from books";
                try {
                    //Creating Statement.
                    Statement stmt = connection.createStatement();

                    //Executing query.
                    ResultSet rs = stmt.executeQuery(sql);

                    //Creating Table for to data will be in table format.
                    JTable book_list = new JTable();
                    String[] bookColumnNames = {"Book ID", "Book ISBN", "Book Name", "Book Publisher", "Book Edition", "Book Genre", "Book price", "Book Pages"};

                    //Creating model for the table.
                    DefaultTableModel bookModel = new DefaultTableModel();

                    //Setting up the columns names of the model.
                    bookModel.setColumnIdentifiers(bookColumnNames);

                    //Adding model to the table component.
                    book_list.setModel(bookModel);

                    //Setting background colour of the table.
                    book_list.setBackground(new Color(51, 35, 85));

                    //Setting foreground colour of the table.
                    book_list.setForeground(Color.white);

                    //Setting up table auto-resizable.
                    book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    book_list.setFillsViewportHeight(true);
                    book_list.setFocusable(false);

                    //Creating scrollbars for table.
                    JScrollPane scrollBook = new JScrollPane(book_list);
                    scrollBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                    while (rs.next()) {
                        //Fetching the data from mysql database
                        int book_id = rs.getInt(1);
                        String book_isbn = rs.getString(2);
                        String book_name = rs.getString(3);
                        String book_publisher = rs.getString(4);
                        String book_edition = rs.getString(5);
                        String book_genre = rs.getString(6);
                        int book_price = rs.getInt(7);
                        int book_pages = rs.getInt(8);
                        //Adding fetched data in model
                        bookModel.addRow(new Object[]{book_id, book_isbn, book_name, book_publisher, book_edition, book_genre, book_price, book_pages});
                    }

                    //Adding scrollbars in the frame.
                    viewBooksUserFrame.add(scrollBook);

                    //Setting up the size of the frame. (width,height)
                    viewBooksUserFrame.setSize(800, 400);

                    //Setting up frame visible for user.
                    viewBooksUserFrame.setVisible(true);

                } catch (Exception e1) {
                    //Creating Dialog box to show any error if occured!
                    JOptionPane.showMessageDialog(null, e1);
                }
            }
        }
        );

        //Creating Button.
        JButton view_user_issued_books_btn = new JButton("Issued Books");

        //Setting Background color of the button.
        view_user_issued_books_btn.setBackground(new Color(51, 35, 85));

        //Setting Foreground color of the button.
        view_user_issued_books_btn.setForeground(Color.white);

        //Performing action on the button.
        view_user_issued_books_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating frame
                JFrame viewUserIssuedBooksFrame = new JFrame("My Issued Books");

                //Storing userid
                int userid = Integer.parseInt(UID);

                //Connection to database
                Connection connection = connect();

                //Database Query
                String sql = "select issued_books.iid as iid, issued_books.bid as bid, issued_books.uid as uid,"
                        + " books.book_isbn as book_isbn, books.book_name as book_name, books.book_publisher as book_publisher, "
                        + "books.book_edition as book_edition, books.book_genre as book_genre, books.book_price as book_price,"
                        + " books.book_pages as book_pages, issued_books.issued_date as issued_date, issued_books.period as period from books,"
                        + "issued_books where books.bid=issued_books.bid and issued_books.uid=" + userid;

                try {
                    //Creating statement
                    Statement stmt = connection.createStatement();

                    //Executing query
                    ResultSet rs = stmt.executeQuery(sql);

                    //Creating Table for to data will be in table format
                    JTable issued_book_list = new JTable();

                    String[] issuedBookColumnNames = {"Issue ID", "Book ID", "User ID", "Book ISBN", "Book Name", "Book Publisher", "Book Edition", "Book Genre", "Book Price", "Book Pages", "Issued Date", "Period"};

                    //Creating model for the table
                    DefaultTableModel bookModel = new DefaultTableModel();

                    //Setting up the columns names of the model
                    bookModel.setColumnIdentifiers(issuedBookColumnNames);

                    //Adding model to the table component
                    issued_book_list.setModel(bookModel);

                    //Setting background colour of the table
                    issued_book_list.setBackground(new Color(51, 35, 85));

                    //Setting foreground colour of the table
                    issued_book_list.setForeground(Color.white);

                    //Setting up table auto-resizable
                    issued_book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    issued_book_list.setFillsViewportHeight(true);
                    issued_book_list.setFocusable(false);

                    //Creating scrollbars for table
                    JScrollPane scrollIssuedBook = new JScrollPane(issued_book_list);
                    scrollIssuedBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollIssuedBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                    while (rs.next()) {
                        //Fetching the data from mysql database
                        int iid = rs.getInt(1);
                        int bid = rs.getInt(2);
                        int uid = rs.getInt(3);
                        String book_isbn = rs.getString(4);
                        String book_name = rs.getString(5);
                        String book_publisher = rs.getString(6);
                        String book_edition = rs.getString(7);
                        String book_genre = rs.getString(8);
                        int book_price = rs.getInt(9);
                        int book_pages = rs.getInt(10);
                        String issued_date = rs.getString(11);
                        int period = rs.getInt(12);
                        //Adding fetched data in model
                        bookModel.addRow(new Object[]{iid, bid, uid, book_isbn, book_name, book_publisher, book_edition, book_genre, book_price, book_pages, issued_date, period});
                    }

                    //Adding scrollbars.
                    viewUserIssuedBooksFrame.add(scrollIssuedBook);

                    //Setting up the dimensions of the frame.
                    viewUserIssuedBooksFrame.setSize(1200, 600);

                    //Setting up the frame visible.
                    viewUserIssuedBooksFrame.setVisible(true);

                    //Setting up the frame non-resizable.
                    viewUserIssuedBooksFrame.setResizable(false);
                } catch (Exception e1) {
                    //Creating Dialog box to show any error if occured!
                    JOptionPane.showMessageDialog(null, e1);
                }
            }
        });

        //Creating Button
        JButton view_user_returned_books_btn = new JButton("My Returned Books");

        //Setting Background color of the button.
        view_user_returned_books_btn.setBackground(new Color(51, 35, 85));

        //Setting Foreground color of the button.
        view_user_returned_books_btn.setForeground(Color.white);

        //Performing action on the button.
        view_user_returned_books_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating frame
                JFrame viewUserReturnedBooksFrame = new JFrame("My Returned Books");

                //Storing userid
                int userid = Integer.parseInt(UID);

                //Connection to database
                Connection connection = connect();

                //Query for retreiving data from database
                String sql = "select returned_books.rid as rid, returned_books.bid as bid, returned_books.uid as uid,"
                        + "books.book_isbn as book_isbn, books.book_name as book_name, books.book_publisher as book_publisher,"
                        + "books.book_edition as book_edition, books.book_genre as book_genre, books.book_price as book_price,"
                        + "books.book_pages as book_pages, returned_books.return_date as return_date, returned_books.fine as fine "
                        + "from books, returned_books where books.bid=returned_books.bid and returned_books.uid=" + userid;

                try {
                    //Creating Statement
                    Statement stmt = connection.createStatement();

                    //Executing query
                    ResultSet rs = stmt.executeQuery(sql);

                    //Creating Table for to data will be in table format
                    JTable returned_book_list = new JTable();
                    String[] returnedBookColumnNames = {"Return ID", "Book ID", "User ID", "Book ISBN", "Book Name", "Book Publisher", "Book Edition", "Book Genre", "Book Price", "Book Pages", "Returned Date", "Fine"};

                    //Creating model for the table
                    DefaultTableModel bookModel = new DefaultTableModel();

                    //Setting up the columns names of the model
                    bookModel.setColumnIdentifiers(returnedBookColumnNames);

                    //Adding model to the table component
                    returned_book_list.setModel(bookModel);

                    //Setting background colour of the table
                    returned_book_list.setBackground(new Color(51, 35, 85));

                    //Setting foreground colour of the table
                    returned_book_list.setForeground(Color.white);

                    //Setting up table auto-resizable
                    returned_book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    returned_book_list.setFillsViewportHeight(true);
                    returned_book_list.setFocusable(false);

                    //Creating scrollbars for table
                    JScrollPane scrollIssuedBook = new JScrollPane(returned_book_list);
                    scrollIssuedBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollIssuedBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                    while (rs.next()) {
                        //Fetching the data from mysql database
                        int rid = rs.getInt(1);
                        int bid = rs.getInt(2);
                        int uid = rs.getInt(3);
                        String book_isbn = rs.getString(4);
                        String book_name = rs.getString(5);
                        String book_publisher = rs.getString(6);
                        String book_edition = rs.getString(7);
                        String book_genre = rs.getString(8);
                        int book_price = rs.getInt(9);
                        int book_pages = rs.getInt(10);
                        String returned_date = rs.getString(11);
                        int fine = rs.getInt(12);
                        //Adding fetched data in model
                        bookModel.addRow(new Object[]{rid, bid, uid, book_isbn, book_name, book_publisher, book_edition, book_genre, book_price, book_pages, returned_date, fine});
                    }

                    //Adding scrollbars.
                    viewUserReturnedBooksFrame.add(scrollIssuedBook);

                    //Setting up the dimensions of the frame. Params:(width,height)
                    viewUserReturnedBooksFrame.setSize(1200, 600);

                    //Setting up the frame visible.
                    viewUserReturnedBooksFrame.setVisible(true);

                    //Setting up the frame non-resizable.
                    viewUserReturnedBooksFrame.setResizable(false);

                } catch (Exception e1) {
                    //Creating Dialog box to show any error if occured!
                    JOptionPane.showMessageDialog(null, e1);
                }
            }
        });

        //Setting Layout of the student frame.
        studentFrame.setLayout(new GridLayout(3, 1));

        //Adding all the components in the student frame.
        studentFrame.add(view_books_btn);
        studentFrame.add(view_user_issued_books_btn);
        studentFrame.add(view_user_returned_books_btn);

        //Setting size of the student frame (width,height)
        studentFrame.setSize(500, 500);

        //Setting up the frame visible
        studentFrame.setVisible(true);

        //Setting up frame non-resizable
        studentFrame.setResizable(false);
    }

    public static void librarian_frame() {

        //Creating Librarian Frame
        JFrame librarianFrame = new JFrame("Librarian Functions");

        //Creating Button
        JButton view_books_btn = new JButton("View Books");

        //Setting up background color of button.
        view_books_btn.setBackground(new Color(51, 35, 85));

        //Setting up foreground color of button.
        view_books_btn.setForeground(Color.white);

        //Performing actions on button.
        view_books_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating frame.
                JFrame viewBooksFrame = new JFrame("Books Available");

                //Connection to Database
                Connection connection = connect();

                //Query for retreiving data from database
                String sql = "select * from BOOKS";
                try {
                    //Creating Statement
                    Statement stmt = connection.createStatement();

                    //Executing query
                    ResultSet rs = stmt.executeQuery(sql);

                    //Creating Table for to data will be in table format
                    JTable book_list = new JTable();
                    String[] bookColumnNames = {"Book ID", "Book ISBN", "Book Name", "Book Publisher", "Book Edition", "Book Genre", "Book price", "Book Pages"};

                    //Creating model for the table
                    DefaultTableModel bookModel = new DefaultTableModel();

                    //Setting up the columns names of the model
                    bookModel.setColumnIdentifiers(bookColumnNames);

                    //Adding model to the table component
                    book_list.setModel(bookModel);

                    //Setting background colour of the table
                    book_list.setBackground(new Color(51, 35, 85));

                    //Setting foreground colour of the table
                    book_list.setForeground(Color.white);

                    //Setting up table auto-resizable
                    book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    book_list.setFillsViewportHeight(true);
                    book_list.setFocusable(false);

                    //Creating scrollbars for table
                    JScrollPane scrollBook = new JScrollPane(book_list);

                    scrollBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                    while (rs.next()) {
                        //Fetching the data from mysql database
                        int book_id = rs.getInt(1);
                        String book_isbn = rs.getString(2);
                        String book_name = rs.getString(3);
                        String book_publisher = rs.getString(4);
                        String book_edition = rs.getString(5);
                        String book_genre = rs.getString(6);
                        int book_price = rs.getInt(7);
                        int book_pages = rs.getInt(8);
                        //Adding fetched data in model
                        bookModel.addRow(new Object[]{book_id, book_isbn, book_name, book_publisher, book_edition, book_genre, book_price, book_pages});
                    }

                    //Adding scrollbars in the frame
                    viewBooksFrame.add(scrollBook);

                    //Setting up the size of the frame (width,height)
                    viewBooksFrame.setSize(800, 400);

                    //Setting up frame visible for user
                    viewBooksFrame.setVisible(true);
                } catch (Exception e1) {
                    //Creating Dialog box to show any error if occured!
                    JOptionPane.showMessageDialog(null, e1);
                }
            }
        });

        //Creating button
        JButton view_users_btn = new JButton("View Users");

        //Setting Background color of the button.
        view_users_btn.setBackground(new Color(51, 35, 85));

        //Setting Foreground color of the button.
        view_users_btn.setForeground(Color.white);

        //Performing actions on the button.
        view_users_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating frame.
                JFrame viewUsersFrame = new JFrame("Users List");

                //Connection to database
                Connection connection = connect();

                //Query for retreiving data from database
                String sql = "select * from users";
                try {
                    //Creating Statement
                    Statement stmt = connection.createStatement();

                    //Executing query
                    ResultSet rs = stmt.executeQuery(sql);

                    //Creating Table for to data will be in table format
                    JTable users_list = new JTable();
                    String[] userColumnNames = {"User ID", "User Name", "User Type"};

                    //Creating model for the table
                    DefaultTableModel userModel = new DefaultTableModel();

                    //Setting up the columns names of the model
                    userModel.setColumnIdentifiers(userColumnNames);

                    //Adding model to the table component
                    users_list.setModel(userModel);

                    //Setting up table auto-resizable
                    users_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    users_list.setFillsViewportHeight(true);

                    //Setting background colour of the table.
                    users_list.setBackground(new Color(51, 35, 85));

                    //Setting foreground colour of the table.
                    users_list.setForeground(Color.white);

                    //Creating scrollbars for table
                    JScrollPane scrollUser = new JScrollPane(users_list);

                    scrollUser.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollUser.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                    while (rs.next()) {
                        //Fetching the data from mysql database
                        int uid = rs.getInt(1);
                        String user_name = rs.getString(2);
                        int user_type = rs.getInt(4);
                        if (user_type == 1) {
                            //Checking if it is 1 then it is admin
                            userModel.addRow(new Object[]{uid, user_name, "ADMIN"});
                        } else {
                            //Else it will be user
                            userModel.addRow(new Object[]{uid, user_name, "USER"});
                        }
                    }

                    //Adding scrollbars in the frame
                    viewUsersFrame.add(scrollUser);

                    //Setting up the size of the frame (width,height)
                    viewUsersFrame.setSize(800, 400);

                    //Setting up frame visible for user
                    viewUsersFrame.setVisible(true);

                } catch (Exception e1) {
                    //Creating Dialog box to show any error if occured!
                    JOptionPane.showMessageDialog(null, e1);
                }

            }
        });

        //Creating button
        JButton view_issued_books_btn = new JButton("View Issued Books");

        //Setting background colour of the button.
        view_issued_books_btn.setBackground(new Color(51, 35, 85));

        //Setting foreground colour of the button.
        view_issued_books_btn.setForeground(Color.white);

        //Performing actions on button.
        view_issued_books_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating button
                JFrame issuedBooksFrame = new JFrame("Issued Books List");

                //Connection to database
                Connection connection = connect();

                //Query for retreiving data from database
                String sql = "select * from issued_books";
                try {
                    //Creating Statement
                    Statement stmt = connection.createStatement();

                    //Executing query
                    ResultSet rs = stmt.executeQuery(sql);

                    //Creating Table for to data will be in table format
                    JTable issue_book_list = new JTable();
                    String[] issueBookColumnNames = {"Issue ID", "User ID", "Book ID", "Issue Date", "Period"};

                    //Creating model for the table
                    DefaultTableModel issuedBookModel = new DefaultTableModel();

                    //Setting up the columns names of the model
                    issuedBookModel.setColumnIdentifiers(issueBookColumnNames);

                    //Adding model to the table component
                    issue_book_list.setModel(issuedBookModel);

                    //Setting up table auto-resizable
                    issue_book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    issue_book_list.setFillsViewportHeight(true);
                    issue_book_list.setFocusable(false);

                    //Setting background colour of the table
                    issue_book_list.setBackground(new Color(51, 35, 85));

                    //Setting foreground colour of the table
                    issue_book_list.setForeground(Color.white);

                    //Creating scrollbars for table
                    JScrollPane scrollIssuedBook = new JScrollPane(issue_book_list);
                    scrollIssuedBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollIssuedBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                    while (rs.next()) {
                        //Fetching the data from mysql database
                        int iid = rs.getInt(1);
                        int uid = rs.getInt(2);
                        int bid = rs.getInt(3);
                        String issue_date = rs.getString(4);
                        int period = rs.getInt(5);
                        //Adding fetched data in model
                        issuedBookModel.addRow(new Object[]{iid, uid, bid, issue_date, period});
                    }

                    //Adding scrollbars in the frame
                    issuedBooksFrame.add(scrollIssuedBook);

                    //Setting up the size of the frame (width,height)
                    issuedBooksFrame.setSize(800, 400);

                    //Setting up frame visible for user
                    issuedBooksFrame.setVisible(true);

                } catch (Exception e1) {
                    //Creating Dialog box to show any error if occured!
                    JOptionPane.showMessageDialog(null, e1);
                }
            }
        });

        //Creating button
        JButton view_returned_books_btn = new JButton("View Returned Books");

        //Setting Background Colour of the button.
        view_returned_books_btn.setBackground(new Color(51, 35, 85));

        //Setting Foreground Colour of the button.
        view_returned_books_btn.setForeground(Color.white);

        //Performing actions on the button.
        view_returned_books_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating button.
                JFrame returnedBooksFrame = new JFrame("Returned Books List");

                //Connection to database.
                Connection connection = connect();

                //Query for retreiving data from database.
                String sql = "select * from returned_books";
                try {
                    //Creating Statement.
                    Statement stmt = connection.createStatement();

                    //Executing query.
                    ResultSet rs = stmt.executeQuery(sql);

                    //Creating Table for to data will be in table format.
                    JTable returned_book_list = new JTable();

                    String[] returnBookColumnNames = {"Return ID", "Book ID", "User ID", "Return Date", "Fine"};

                    //Creating model for the table.
                    DefaultTableModel returnBookModel = new DefaultTableModel();

                    //Setting up the columns names of the model.
                    returnBookModel.setColumnIdentifiers(returnBookColumnNames);

                    //Adding model to the table component.
                    returned_book_list.setModel(returnBookModel);

                    //Setting up table auto-resizable.
                    returned_book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    returned_book_list.setFillsViewportHeight(true);
                    returned_book_list.setFocusable(false);

                    //Setting background colour of the table.
                    returned_book_list.setBackground(new Color(51, 35, 85));

                    //Setting foreground colour of the table.
                    returned_book_list.setForeground(Color.white);

                    //Creating scrollbars for table.
                    JScrollPane scrollReturnedBook = new JScrollPane(returned_book_list);
                    scrollReturnedBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollReturnedBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                    while (rs.next()) {
                        //Fetching the data from mysql database.
                        int rid = rs.getInt(1);
                        int bid = rs.getInt(2);
                        int uid = rs.getInt(3);
                        String returned_date = rs.getString(4);
                        int fine = rs.getInt(5);

                        //Adding fetched data in model.
                        returnBookModel.addRow(new Object[]{rid, bid, uid, returned_date, fine});
                    }

                    //Adding scrollbars in the frame.
                    returnedBooksFrame.add(scrollReturnedBook);

                    //Setting up the size of the frame (width,height)
                    returnedBooksFrame.setSize(800, 400);

                    //Setting up frame visible for user.
                    returnedBooksFrame.setVisible(true);

                } catch (Exception e1) {
                    //Creating Dialog box to show any error if occured!
                    JOptionPane.showMessageDialog(null, e1);
                }
            }
        });

        //Creating button
        JButton add_user_btn = new JButton("Add User");

        //Setting Background Colour of the button.
        add_user_btn.setBackground(new Color(51, 35, 85));

        //Setting Foreground Colour of the button.
        add_user_btn.setForeground(Color.white);

        //Performing actions on button.
        add_user_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Craeting frame
                JFrame add_user_frame = new JFrame("Enter User Details"); //Frame to enter user details

                //Creating label 
                JLabel l1 = new JLabel("Username", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l1.setOpaque(true);

                //Setting Background Colour of the label.
                l1.setBackground(new Color(51, 35, 85));

                //Setting Foreground Colour of the label.
                l1.setForeground(Color.white);

                //Creating label 
                JLabel l2 = new JLabel("Password", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l2.setOpaque(true);

                //Setting Background Colour of the label.
                l2.setBackground(new Color(51, 35, 85));

                //Setting Foreground Colour of the label.
                l2.setForeground(Color.white);

                //Creating textfield 
                JTextField add_username_tf = new JTextField();

                //Setting Background Colour of the textfield.
                add_username_tf.setBackground(new Color(51, 35, 85));

                //Setting Foreground Colour of the textfield.
                add_username_tf.setForeground(Color.white);

                //Creating textfield 
                JPasswordField add_password_tf = new JPasswordField();

                //Setting Background Colour of the textfield.
                add_password_tf.setBackground(new Color(51, 35, 85));

                //Setting Foreground Colour of the textfield.
                add_password_tf.setForeground(Color.white);

                //Creating radiobutton
                JRadioButton user_type_radio1 = new JRadioButton("Admin");

                //Aligning center
                user_type_radio1.setHorizontalAlignment(SwingConstants.CENTER);

                //Setting Background Colour of the radiobutton.
                user_type_radio1.setBackground(new Color(51, 35, 85));

                //Setting Foreground Colour of the radiobutton.
                user_type_radio1.setForeground(Color.white);

                //Creating radiobutton
                JRadioButton user_type_radio2 = new JRadioButton("User");

                //Aligning center
                user_type_radio2.setHorizontalAlignment(SwingConstants.CENTER);

                //Setting Background Colour of the radiobutton.
                user_type_radio2.setBackground(new Color(51, 35, 85));

                //Setting Foreground Colour of the radiobutton.
                user_type_radio2.setForeground(Color.white);

                //Adding radiobuttons in buttongroup
                ButtonGroup user_type_btn_grp = new ButtonGroup();
                user_type_btn_grp.add(user_type_radio1);
                user_type_btn_grp.add(user_type_radio2);

                //Creating button.
                JButton create_btn = new JButton("Create");

                //Setting Background Colour of the button.
                create_btn.setBackground(new Color(124, 85, 227));

                //Setting Foreground Colour of the button.
                create_btn.setForeground(Color.white);

                //Creating button.
                JButton user_entry_cancel_btn = new JButton("Cancel");

                //Setting Background Colour of the button.
                user_entry_cancel_btn.setBackground(new Color(124, 85, 227));

                //Setting Foreground Colour of the button.
                user_entry_cancel_btn.setForeground(Color.white);

                //Performing actions on the button.
                create_btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //Getting data from the textfield.
                        String username = add_username_tf.getText();
                        String password = add_password_tf.getText();

                        //Connection to database.
                        Connection connection = connect();

                        try {
                            //Creating statement
                            Statement stmt = connection.createStatement();

                            //Check if radio1 is click or not
                            //If radio1 is click then it is added as admin, else normal student
                            if (user_type_radio1.isSelected()) {
                                //Query to insert inside in the table
                                stmt.executeUpdate("INSERT INTO USERS(USERNAME,PASSWORD,USER_TYPE) VALUES ('" + username + "','" + password + "','" + "1" + "')");
                                //Creating Dialogbox to display meassage.
                                JOptionPane.showMessageDialog(null, "Admin added!");
                                add_user_frame.dispose();
                            } else {
                                //Query to insert inside in the table.
                                stmt.executeUpdate("INSERT INTO USERS(USERNAME,PASSWORD,USER_TYPE) VALUES ('" + username + "','" + password + "','" + "0" + "')");
                                //Creating Dialogbox to display meassage.
                                JOptionPane.showMessageDialog(null, "User added!");
                                add_user_frame.dispose();
                            }

                        } catch (Exception e1) {
                            //Creating Dialog box to show any error if occured!
                            JOptionPane.showMessageDialog(null, e1);
                        }
                    }
                });

                //Performing actions on button.
                user_entry_cancel_btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        add_user_frame.dispose();
                    }
                });

                //Adding components in frame.
                add_user_frame.add(l1);
                add_user_frame.add(add_username_tf);
                add_user_frame.add(l2);
                add_user_frame.add(add_password_tf);
                add_user_frame.add(user_type_radio1);
                add_user_frame.add(user_type_radio2);
                add_user_frame.add(create_btn);
                add_user_frame.add(user_entry_cancel_btn);

                //Setting up the size of the frame (width,height)
                add_user_frame.setSize(350, 200);

                //Setting up layout of the frame
                add_user_frame.setLayout(new GridLayout(4, 2));

                //Setting up the frame visible
                add_user_frame.setVisible(true);

                //Setting up table auto-resizable.
                add_user_frame.setResizable(false);
            }
        });

        //Creating button.
        JButton add_book_btn = new JButton("Add Book");

        //Setting Background Colour of the button.
        add_book_btn.setBackground(new Color(51, 35, 85));

        //Setting Foreground Colour of the button.
        add_book_btn.setForeground(Color.white);

        //Performing actions on button.
        add_book_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating Frame.
                JFrame book_frame = new JFrame("Enter Book Details");

                //Creating labels
                JLabel l1, l2, l3, l4, l5, l6, l7;

                l1 = new JLabel("ISBN", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l1.setOpaque(true);

                //Setting background colour of the label.
                l1.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l1.setForeground(Color.white);

                l2 = new JLabel("Name", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l2.setOpaque(true);

                //Setting background colour of the label.
                l2.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l2.setForeground(Color.white);

                l3 = new JLabel("Publisher", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l3.setOpaque(true);

                //Setting background colour of the label.
                l3.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l3.setForeground(Color.white);

                l4 = new JLabel("Edition", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l4.setOpaque(true);

                //Setting background colour of the label.
                l4.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l4.setForeground(Color.white);

                l5 = new JLabel("Genre", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l5.setOpaque(true);

                //Setting background colour of the label.
                l5.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l5.setForeground(Color.white);

                l6 = new JLabel("Price", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l6.setOpaque(true);

                //Setting background colour of the label.
                l6.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l6.setForeground(Color.white);

                l7 = new JLabel("Pages", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l7.setOpaque(true);

                //Setting background colour of the label.
                l7.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l7.setForeground(Color.white);

                //Creating textfield.
                JTextField book_isbn_tf = new JTextField();

                //Setting background colour of the textfield.
                book_isbn_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                book_isbn_tf.setForeground(Color.white);

                //Creating textfield
                JTextField book_name_tf = new JTextField();

                //Setting background colour of the textfield.
                book_name_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                book_name_tf.setForeground(Color.white);

                //Creating textfield.
                JTextField book_publisher_tf = new JTextField();

                //Setting background colour of the textfield.
                book_publisher_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                book_publisher_tf.setForeground(Color.white);

                //Creating textfield.
                JTextField book_edition_tf = new JTextField();

                //Setting background colour of the textfield.
                book_edition_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                book_edition_tf.setForeground(Color.white);

                //Creating textfield.
                JTextField book_genre_tf = new JTextField();

                //Setting background colour of the textfield.
                book_genre_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                book_genre_tf.setForeground(Color.white);

                //Creating textfield.
                JTextField book_price_tf = new JTextField();

                //Setting background colour of the textfield.
                book_price_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                book_price_tf.setForeground(Color.white);

                //Creating textfield.
                JTextField book_pages_tf = new JTextField();

                //Setting background colour of the textfield.
                book_pages_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                book_pages_tf.setForeground(Color.white);

                //Creating button.
                JButton create_btn = new JButton("Submit");

                //Setting background colour of the button.
                create_btn.setBackground(new Color(124, 85, 227));

                //Setting foreground colour of the button.
                create_btn.setForeground(Color.white);

                //Creating button.
                JButton add_book_cancel_btn = new JButton("Cancel");

                //Setting background colour of the button.
                add_book_cancel_btn.setBackground(new Color(124, 85, 227));

                //Setting foreground colour of the button.
                add_book_cancel_btn.setForeground(Color.white);

                //Performing actions on the button.
                create_btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //Getting data from the textfield.
                        String book_isbn = book_isbn_tf.getText();
                        String book_name = book_name_tf.getText();
                        String book_publisher = book_publisher_tf.getText();
                        String book_edition = book_edition_tf.getText();
                        String book_genre = book_genre_tf.getText();

                        //Converting bookprice and bookpages to integer from string.
                        int book_price = Integer.parseInt(book_price_tf.getText());
                        int book_pages = Integer.parseInt(book_pages_tf.getText());

                        //Connection to database.
                        Connection connection = connect();

                        try {
                            //Creating statement
                            Statement stmt = connection.createStatement();

                            //Query to insert inside in the table.
                            stmt.executeUpdate("INSERT INTO BOOKS(book_isbn,book_name,book_publisher,book_edition,book_genre,book_price,book_pages)"
                                    + " VALUES ('" + book_isbn + "','" + book_name + "','" + book_publisher + "','" + book_edition + "','" + book_genre + "','" + book_price + "'," + book_pages + ")");

                            //Creating Dialogbox to diaplay meassage.
                            JOptionPane.showMessageDialog(null, "Book added!");
                            book_frame.dispose();
                        } catch (Exception e1) {
                            //Creating Dialog box to show any error if occured!
                            JOptionPane.showMessageDialog(null, e1);
                        }
                    }
                });

                //Performing actions on the button.
                add_book_cancel_btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        book_frame.dispose();
                    }
                });

                //Adding components in the frame.
                book_frame.add(l1);
                book_frame.add(book_isbn_tf);
                book_frame.add(l2);
                book_frame.add(book_name_tf);
                book_frame.add(l3);
                book_frame.add(book_publisher_tf);
                book_frame.add(l4);
                book_frame.add(book_edition_tf);
                book_frame.add(l5);
                book_frame.add(book_genre_tf);
                book_frame.add(l6);
                book_frame.add(book_price_tf);
                book_frame.add(l7);
                book_frame.add(book_pages_tf);
                book_frame.add(create_btn);
                book_frame.add(add_book_cancel_btn);

                //Setting up the size of the frame (width,height)
                book_frame.setSize(800, 500);

                //Setting up layout of the frame
                book_frame.setLayout(new GridLayout(8, 2));

                //Setting up the frame visible
                book_frame.setVisible(true);

                //Setting up table auto-resizable.
                book_frame.setResizable(false);
            }
        });

        //Creating button
        JButton add_issue_book_btn = new JButton("Issue Book");

        //Setting background colour of the button.
        add_issue_book_btn.setBackground(new Color(51, 35, 85));

        //Setting foreground colour of the button.
        add_issue_book_btn.setForeground(Color.white);

        //Performing actions on the button.
        add_issue_book_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating frame.
                JFrame issue_book_frame = new JFrame("Enter Details");

                //Creating panel.
                JPanel pickerPanel = new JPanel();

                //Creating datepicker.
                JXDatePicker picker = new JXDatePicker();

                //Setting up current date in datepicker
                picker.setDate(Calendar.getInstance().getTime());

                //Formating datepicker.
                picker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

                //Adding datepicker in the panel.
                pickerPanel.add(picker);

                //Setting background colour of the panel.
                pickerPanel.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the panel.
                pickerPanel.setForeground(Color.white);

                //Creating labels
                JLabel l1, l2, l3, l4;
                l1 = new JLabel("Book ID", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l1.setOpaque(true);

                //Setting background colour of the label.
                l1.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l1.setForeground(Color.white);

                l2 = new JLabel("User/Student ID", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l2.setOpaque(true);

                //Setting background colour of the label.
                l2.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l2.setForeground(Color.white);

                l3 = new JLabel("Period(days)", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l3.setOpaque(true);

                //Setting background colour of the label.
                l3.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l3.setForeground(Color.white);

                l4 = new JLabel("Issued Date(DD-MM-YYYY)", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l4.setOpaque(true);

                //Setting background colour of the label.
                l4.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l4.setForeground(Color.white);

                //Creating textfield.
                JTextField bid_tf = new JTextField();

                //Setting background colour of the textfield.
                bid_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                bid_tf.setForeground(Color.white);

                //Creating textfield.
                JTextField uid_tf = new JTextField();

                //Setting background colour of the textfield.
                uid_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                uid_tf.setForeground(Color.white);

                //Creating textfield.
                JTextField period_tf = new JTextField();

                //Setting background colour of the textfield.
                period_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                period_tf.setForeground(Color.white);

                //Creating button.
                JButton create_btn = new JButton("Submit");

                //Setting background colour of the button.
                create_btn.setBackground(new Color(124, 85, 227));

                //Setting foreground colour of the button.
                create_btn.setForeground(Color.white);

                //Creating button.
                JButton issue_book_cancel_btn = new JButton("Cancel");

                //Setting background colour of the button.
                issue_book_cancel_btn.setBackground(new Color(124, 85, 227));

                //Setting foreground colour of the button.
                issue_book_cancel_btn.setForeground(Color.white);

                //Performing actions on the button.
                create_btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //Getting data from textfield.
                        int uid = Integer.parseInt(uid_tf.getText());
                        int bid = Integer.parseInt(bid_tf.getText());
                        String period = period_tf.getText();
                        Date oDate = picker.getDate();

                        //Formatting date.
                        DateFormat oDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String issued_date = oDateFormat.format(oDate);

                        //Converting period from string to integer.
                        int period_int = Integer.parseInt(period);

                        //Connection to the database
                        Connection connection = connect();

                        try {
                            //Creating Statement
                            Statement stmt = connection.createStatement();

                            //Query to insert data in the table.
                            stmt.executeUpdate("INSERT INTO issued_books(UID,BID,ISSUED_DATE,PERIOD) VALUES ('" + uid + "','" + bid + "','" + issued_date + "'," + period_int + ")");

                            //Creating Dialogbox to diaplay message.
                            JOptionPane.showMessageDialog(null, "Book Issued!");
                            issue_book_frame.dispose();

                        } catch (Exception e1) {
                            //Creating Dialog box to show any error if occured!
                            JOptionPane.showMessageDialog(null, e1);
                        }
                    }
                });

                issue_book_cancel_btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        issue_book_frame.dispose();
                    }
                });

                //Adding components in the frame
                issue_book_frame.add(l1);
                issue_book_frame.add(bid_tf);
                issue_book_frame.add(l2);
                issue_book_frame.add(uid_tf);
                issue_book_frame.add(l3);
                issue_book_frame.add(period_tf);
                issue_book_frame.add(l4);
                issue_book_frame.getContentPane().add(pickerPanel);
                issue_book_frame.add(create_btn);
                issue_book_frame.add(issue_book_cancel_btn);

                //Setting up the size of the frame (width,height)
                issue_book_frame.setSize(600, 300);

                //Setting up frame layout
                issue_book_frame.setLayout(new GridLayout(5, 2));

                //Setting up the frame visible
                issue_book_frame.setVisible(true);

                //Setting up table auto-resizable.
                issue_book_frame.setResizable(false);
            }
        });

        //Creating button.
        JButton add_return_book_btn = new JButton("Return Book");

        //Setting background colour of the button.
        add_return_book_btn.setBackground(new Color(51, 35, 85));

        //Setting foreground colour of the button.
        add_return_book_btn.setForeground(Color.white);

        //Performing actions on the button.
        add_return_book_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Creating frame.
                JFrame returnBookFrame = new JFrame("Enter Details");

                //Creating the labels.
                JLabel l1 = new JLabel("Book ID", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l1.setOpaque(true);

                //Setting background colour of the label.
                l1.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l1.setForeground(Color.white);

                JLabel l2 = new JLabel("User ID", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l2.setOpaque(true);

                //Setting background colour of the label.
                l2.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l2.setForeground(Color.white);

                //Creating label.
                JLabel l3 = new JLabel("Return Date(DD-MM-YYYY)", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l3.setOpaque(true);

                //Setting background colour of the label.
                l3.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l3.setForeground(Color.white);

                JLabel l4 = new JLabel("Fine", SwingConstants.CENTER);

                //Setting up opaque so that label component paints every pixel within its bounds.
                l4.setOpaque(true);

                //Setting background colour of the label.
                l4.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the label.
                l4.setForeground(Color.white);

                //Creating textfield.
                JTextField bid_tf = new JTextField();

                //Setting background colour of the textfield.
                bid_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                bid_tf.setForeground(Color.white);

                //Creating textfield.
                JTextField uid_tf = new JTextField();

                //Setting background colour of the textfield.
                uid_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                uid_tf.setForeground(Color.white);

                //Creating panel for date.
                JPanel pickerPanel = new JPanel();

                //Creating datepicker.
                JXDatePicker picker = new JXDatePicker();

                //Getting and Setting up the current time of the date.
                picker.setDate(Calendar.getInstance().getTime());

                //Setting up the format of the date picker.
                picker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

                //Creating textfield.
                JTextField fine_tf = new JTextField();

                //Setting background colour of the textfield.
                fine_tf.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the textfield.
                fine_tf.setForeground(Color.white);

                //Adding datepixker in panel.
                pickerPanel.add(picker);

                //Setting background colour of the panel.
                pickerPanel.setBackground(new Color(51, 35, 85));

                //Setting foreground colour of the panel.
                pickerPanel.setForeground(Color.white);

                //Creating button.
                JButton return_book_btn = new JButton("Return");

                //Setting background colour of the button.
                return_book_btn.setBackground(new Color(124, 85, 227));

                //Setting foreground colour of the button.
                return_book_btn.setForeground(Color.white);

                //Creating button.
                JButton cancel_book_btn = new JButton("Cancel");

                //Setting background colour of the button.
                cancel_book_btn.setBackground(new Color(124, 85, 227));

                //Setting foreground colour of the button.
                cancel_book_btn.setForeground(Color.white);

                //Performing actions on button.
                return_book_btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //Getting data from textfields.
                        int bid = Integer.parseInt(bid_tf.getText());
                        int uid = Integer.parseInt(uid_tf.getText());
                        int fine = Integer.parseInt(fine_tf.getText());
                        Date oDate = picker.getDate();
                        //Formatting Date.
                        DateFormat oDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String return_date = oDateFormat.format(oDate);

                        try {
                            //Connection to database
                            Connection connection = connect();

                            //Creating Statement
                            Statement stmt = connection.createStatement();

                            //Querying to insert in the table.
                            stmt.executeUpdate("INSERT INTO returned_books(bid,uid,return_date,fine) VALUES ('" + bid + "','" + uid + "','" + return_date + "'," + fine + ")");

                            //Creating Dialogbox to display message.
                            JOptionPane.showMessageDialog(null, "Book Returned!");
                            returnBookFrame.dispose();

                        } catch (Exception e1) {
                            //Creating Dialog box to show any error if occured!
                            JOptionPane.showMessageDialog(null, e1);
                        }
                    }
                });

                //Performing actions on the button.
                cancel_book_btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        returnBookFrame.dispose();
                    }
                });

                //Adding all return book components in the frame
                returnBookFrame.add(l1);
                returnBookFrame.add(bid_tf);
                returnBookFrame.add(l2);
                returnBookFrame.add(uid_tf);
                returnBookFrame.add(l3);
                returnBookFrame.getContentPane().add(pickerPanel);
                returnBookFrame.add(l4);
                returnBookFrame.add(fine_tf);
                returnBookFrame.add(return_book_btn);
                returnBookFrame.add(cancel_book_btn);

                //Setting up the size of the frame
                returnBookFrame.setSize(600, 300);

                //Setting up the layout of the frame
                returnBookFrame.setLayout(new GridLayout(5, 2));

                //Setting up the frame visible
                returnBookFrame.setVisible(true);

                //Setting up frame non-resizable
                returnBookFrame.setResizable(false);
            }
        });

        //Setting the layout of Librarian Frame
        librarianFrame.setLayout(new GridLayout(2, 4));

        //Adding Librarian components in the Librarian Frame
        librarianFrame.add(add_user_btn);
        librarianFrame.add(add_book_btn);
        librarianFrame.add(add_issue_book_btn);
        librarianFrame.add(add_return_book_btn);
        librarianFrame.add(view_users_btn);
        librarianFrame.add(view_books_btn);
        librarianFrame.add(view_issued_books_btn);
        librarianFrame.add(view_returned_books_btn);

        //Setting size of the frame (width,height)
        librarianFrame.setSize(800, 200);

        //Setting up the frame visible to the user
        librarianFrame.setVisible(true);

        //Setting up frame non-resizable
        librarianFrame.setResizable(false);

    }

    public static void main(String[] args) {

        //Creating object of MainFrame class.
        MainFrame mf = new MainFrame();

        //Calling initially login function.
        mf.loginFn();
    }
}
