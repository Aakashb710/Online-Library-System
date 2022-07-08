package aakashProject;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;
class PrintingObject
{
    public static void print(Object O)
    {
        System.out.print(O);
    }
    public static void println(Object o)
    {
        System.out.println(o);
    }
}

public class Library
{

    static void addBook(PreparedStatement ps)throws Exception
    {
        InputStreamReader r=new InputStreamReader(System.in);
        BufferedReader sc =new BufferedReader(r);
        try {
            do {
                System.out.println("Enter book type");
                String type = sc.readLine();
                System.out.println("Enter book name");
                String bookName = sc.readLine();
                System.out.println("Enter book_author's name");
                String bookAuthor = sc.readLine();
                System.out.println("Enter book_id");
                String id=sc.readLine();
                System.out.println("Enter status");
                String status=sc.readLine();

                ps.setString(1,type);
                ps.setString(2,bookName);
                ps.setString(3,bookAuthor);
                ps.setString(4,id);
                ps.setString(5,status);

                int ex = ps.executeUpdate();
                System.out.println(ex+"Books Added");

                System.out.println("Do you want to continue:\ty / n");
                String s = sc.readLine();
                if(s.equalsIgnoreCase("n"))
                {
                    break;
                }
            }while (true);
        }catch (SQLException sqlException)
        {
            PrintingObject.println(sqlException);
        }
        catch (InputMismatchException inputMismatchException)
        {
            PrintingObject.print("Please check your input & try again");
        }

    }
    static void showAvailableBooks(PreparedStatement psh)
    {
        try {
            ResultSet rs=null;
            if (psh!=null)
            {
                //if prepared statement is not null then it will execute the mentioned query.
                rs=rs = psh.executeQuery("Select * from Library");
            }
            PrintingObject.println("Book_Type"+"   "+"Book_Name"+"   "+"Book_Author"+"   "+"Book_Id"+"   "+"Book_Status");
            PrintingObject.println("------------------------------------------------------------------------------------");
            while (rs.next())
            {
                PrintingObject.println(rs.getString(1)+"   "+rs.getString(2)+"   "+rs.getString(3)+"   "+rs.getString(4)+"   "+rs.getString(5));
            }
        }catch (SQLException sqlException) {
            PrintingObject.print(sqlException);
        }
    }

    static void returnBook(PreparedStatement pd) throws Exception
    {
        InputStreamReader r=new InputStreamReader(System.in);
        BufferedReader sc =new BufferedReader(r);
        try {
            PrintingObject.println("Enter which book you want to return");
            String book = sc.readLine();
            PrintingObject.println("Enter the book status");
            String status=sc.readLine();
            pd.setString(1,status);
            pd.setString(2,book);
            int d = pd.executeUpdate();
            PrintingObject.println(d+"Book has been returned");
        }catch (SQLException sqlException)
        {
            PrintingObject.print(sqlException);
        }

    }

    static void issueBook(PreparedStatement pt) throws Exception
    {
        InputStreamReader r=new InputStreamReader(System.in);
        BufferedReader sc =new BufferedReader(r);
        try {
            PrintingObject.println("Enter the book name to issue");
            String book=sc.readLine();
            PrintingObject.println("Enter the book status");
            String status=sc.readLine();
            pt.setString(1,status);
            pt.setString(2,book);
            int i1 = pt.executeUpdate();
            System.out.println(i1+"Book has been issued");
        }catch (SQLException sqlException)
        {
            PrintingObject.println(sqlException);
        }
    }

    static void batchProcessing(PreparedStatement ps) throws Exception
    {
        InputStreamReader r=new InputStreamReader(System.in);
        BufferedReader sc =new BufferedReader(r);
        // method to insert data using batch for better performance
        try {
            do {
                PrintingObject.println("Enter the Book type");
                String bookType= sc.readLine();

                PrintingObject.println("Enter Book_Name");
                String bookName = sc.readLine();
                PrintingObject.println("");
                PrintingObject.println("Enter Book Author");
                String bookAuthor=sc.readLine();
                PrintingObject.println("");
                PrintingObject.println("Enter Book_Id");
                String bookId = sc.readLine();

                PrintingObject.println("Enter Available Status");
                String status = sc.readLine();

                ps.setString(1, bookType);
                ps.setString(2, bookName);
                ps.setString(3, bookAuthor);
                ps.setString(4, bookId);
                ps.setString(5, status);

                ps.addBatch();
                PrintingObject.println("Do you want to continue : y/n");
                String enterChoice = sc.readLine();
                if (enterChoice.equalsIgnoreCase("n"))
                {
                    break;
                }
            }while (true);
            ps.executeBatch();//for executing the batch
            System.out.println("record successfully saved");
        }catch (SQLException sqlException)
        {
            PrintingObject.println(sqlException);
        }
        catch (InputMismatchException inputMismatchException)
        {
            PrintingObject.println(inputMismatchException);
        }

    }
    public static void main(String[] args) throws SQLException
    {
        InputStreamReader r=new InputStreamReader(System.in);
        BufferedReader sc =new BufferedReader(r);
        String url = "jdbc:oracle:thin:@localhost:1521:xe";//this is the connection url of oracle database
        String username = "aakashb";
        String password = "abcd69";
        String query = "Select * from Library";
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = null;
            PreparedStatement pt = null;
            PreparedStatement pd = null;
            PreparedStatement psh = null;
            if (con != null)//if con isn's null then it will call the methods to perform those mentioned operations
            {
                ps = con.prepareStatement("insert into Library values(?,?,?,?,?)");//preparedstatement pt to add new books.
                pt =  con.prepareStatement("update Library  set book_status = '?' where book_name='?'");//preparedstatement pd to issue a book having menthioned name
                pd = con.prepareStatement("update Library set book_status = '?' where book_name='?'");//preparedstatement pds to update the status having mentioned condition
                psh = con.prepareStatement("select * from Library");//preparedstatement ps to show the available books in library.
            }
            con.setAutoCommit(false);
            int choice=1;
            while (choice!=0)
            {
                PrintingObject.println("1. to show table");
                PrintingObject.println("2. to insert new data");
                PrintingObject.println("3. to Issue an exixting book");
                PrintingObject.println("4. to return an existing book");
                PrintingObject.println("5. to commit or roll back");
                PrintingObject.println("6. to set auto commit on");
                PrintingObject.println("7. to close the system");
                PrintingObject.print("Enter choice : ");
                choice=Integer.parseInt(sc.readLine());

                switch (choice)
                {
                    case 1:
                        Library.showAvailableBooks(psh);
                        PrintingObject.println("");
                        break;
                    case 2:
                        Library.addBook(ps);
                        break;
                    case 3:
                        Library.issueBook(pt);
                        break;
                    case 4:
                        Library.returnBook(pd);
                        break;
                    case 5:
                        PrintingObject.println("Enter your choice between rollback/commit");
                        String st = sc.readLine();
                        if(st.equalsIgnoreCase("rollback"))
                        {
                            con.rollback();
                        } else if (st.equalsIgnoreCase("commit")) {
                            con.commit();
                        }
                        break;
                    case 6:
                        PrintingObject.println("Set auto-commit on");
                        con.setAutoCommit(true);
                        break;
                    case 7:
                        PrintingObject.println("Closing:-------");
                        break;


                    default:
                        PrintingObject.println("Wrong choice, click on to the right one");
                }
            }
        } catch (ClassNotFoundException classNotFoundException) {
            System.out.println(classNotFoundException);// prints the parameterised exception
        } catch (SQLException sqlException) {
            System.out.println(sqlException);// prints the parameterised exception
        } catch (Exception exp) {
            System.out.println(exp);
        } finally {
            con.close();//finally block to close the connection at the end.
        }
    }
}

