
import java.sql.*;
import java.text.NumberFormat;


public class Library {
	public static void main(String args[]) {

        // Load the database driver
        // NOTE: This block is necessary for Oracle 10g (JDBC 3.0) or earlier,
        // but not for Oracle 11g (JDBC 4.0) or later
//        try {
//            Class.forName("oracle.jdbc.OracleDriver");
//        } catch (ClassNotFoundException e) {
//            System.out.println(e);
//        }

        // define common JDBC objects
        Connection connection = null;
        Statement statement1 = null;
        Statement statement2 = null;
        Statement statement3 = null;
        Statement statement4 = null;
        Statement statement5 = null;
        Statement statement6 = null;
        Statement statement7 = null;
        Statement statement8 = null;
        Statement statement9 = null;
        Statement statement10 = null;
        
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        ResultSet rs6 = null;
        ResultSet rs7 = null;
        ResultSet rs8 = null;
        ResultSet rs9 = null;
        
        try {
            // Connect to the database
        	String dbUrl = "jdbc:mysql://10.184.200.177:3306/BTE423FALL15_bpoon";
            String username = "bpoon";
            String password = "c02898";
            connection = DriverManager.getConnection(dbUrl, username, password);

            // Execute a SELECT statement
            statement1 = connection.createStatement();
            String query = "SELECT COUNT(*) AS Number_of_Copies FROM Availability WHERE Book_ISBN='9780756404079';";
            rs1 = statement1.executeQuery(query);

            // Display the results of a SELECT statement
            while (rs1.next()) {
                String Number_of_Copies = rs1.getString("Number_of_Copies");
                System.out.println(
                	"1. List the number of copies of a particular libary item: \n"
                    + "\t Number_of_Copies: " + Number_of_Copies + "\n"
                    + "\t ISBN: 9780756404079" + "\n");
            }
            
//            ===========================================
//            2 Query
//            =======================================
            // Execute a SELECT statement
            statement2 = connection.createStatement();
            String query2 = "SELECT DISTINCT patron.*"
            		+ " FROM LibraryPatrons patron, loanEvent l, Availability a, Category c"
            		+ " WHERE patron.cardNo = l.cardNo"
            		+ " AND l.itemNo = a.itemNo"
            		+ " AND a.materialType = c.materialType"
            		+ " AND dateCheckedIn IS NULL"
            		+ " AND checkoutPeriod < DATEDIFF(NOW(), dateCheckedOut)"
            		+ " AND (NOT EXISTS (SELECT r.*"
            		+ " FROM loanEvent l, Renewal r"
            		+ " WHERE l.loanEventNo = r.loanEventNo)"
            		+ " OR checkoutPeriod < ANY(SELECT DATEDIFF(NOW(), dateRenewed)"
            		+ " FROM loanEvent l, Renewal r"
            		+ " WHERE l.loanEventNo = r.loanEventNo))";
            rs2 = statement2.executeQuery(query2);

            // Display the results of a SELECT statement
            System.out.println(
                	"2. List the details of the patrons who have at least an overdue library item today \n");
            while (rs2.next()) {
                String cardNo = rs2.getString("cardNo");
                String lastName = rs2.getString("lastName");
                String firstName = rs2.getString("firstName");
                String phone = rs2.getString("phone");
                String street = rs2.getString("street");
                String city = rs2.getString("city");
                String state = rs2.getString("state");
                String zip = rs2.getString("zip");
                String email = rs2.getString("email");
                
                System.out.println(
                    	"\t cardNo: " + cardNo + "\n"
                        + "\t lastName: " + lastName + "\n"
                        + "\t firstName: " + firstName + "\n"
                        + "\t phone: " + phone + "\n"
                        + "\t street: " + street + "\n"
                        + "\t city: " + city + "\n"
                        + "\t state: " + state + "\n"
                        + "\t zip: " + zip + "\n"
                        + "\t email: " + email + "\n"
                
                        );
            }
            
//          ===========================================
//          3 Query
//          =======================================
          // Execute a SELECT statement
          statement3 = connection.createStatement();
          String query3 = "SELECT patron.cardNo, patron.lastName, patron.firstName, SUM((DATEDIFF(now(),last_loan_date) - checkoutPeriod) * 0.10) AS OverDue_Fine"
          		+ " FROM CheckedOut co, LibraryPatrons patron, Category, Availability a"
          		+ " WHERE co.cardNo = patron.cardNo"
          		+ " AND a.itemNo = co.itemNo"
          		+ " AND Category.materialType = a.materialType"
          		+ " AND patron.firstName = 'Benedict'"
          		+ " AND checkoutPeriod < DATEDIFF(now(),last_loan_date)"
          		+ " GROUP BY patron.cardNo;"
          		;
          rs3 = statement3.executeQuery(query3);

          System.out.println(
              	"3.	Identify the total fines owed by a patron (by his/her library card number) currently in the system");
          // Display the results of a SELECT statement
          while (rs3.next()) {
              double OverDue_Fine = rs3.getDouble("OverDue_Fine");
              String cardNo = rs3.getString("cardNo");
              String lastName = rs3.getString("lastName");
              String firstName = rs3.getString("firstName");
//
              NumberFormat currency = NumberFormat.getCurrencyInstance();
              String fine = currency.format(OverDue_Fine);
//
              System.out.println(
            		  "\t cardNo: " + cardNo + "\n"
                        + "\t lastName: " + lastName + "\n"
                       + "\t firstName: " + firstName + "\n"
                    + "\t Over Due Fine: " + fine + "\n");
                
          }
//        ===========================================
//        4 Query
//        =======================================
        // Execute a SELECT statement
        statement4 = connection.createStatement();
        String query4 = "SELECT  patron.cardNo, patron.firstName, patron.lastName, p.*"
        		+ "  FROM Payment p, loanEvent l, LibraryPatrons patron"
        		+ " WHERE p.loanEventNo = l.loanEventNo"
        		+ " AND l.cardNo = patron.cardNo"
        		+ " AND patron.firstName = 'Yunho'";
        rs4 = statement4.executeQuery(query4);

        // Display the results of a SELECT statement
        System.out.println(
         "4. List the details (e.g., damage, lost, amount, etc) of the payment made by a patron"
        		);
        while (rs4.next()) {
            String cardNo = rs4.getString("cardNo");
            String lastName = rs4.getString("lastName");
            String firstName = rs4.getString("firstName");
            String paymentNo = rs4.getString("paymentNo");
            String loanEventNo = rs4.getString("loanEventNo");
            String Reason = rs4.getString("Reason");
            String datePaid = rs4.getString("datePaid");
            Double amountPaid = rs4.getDouble("amountPaid");
            
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            String amount = currency.format(amountPaid);
            
            System.out.println(
                	"\t cardNo: " + cardNo + "\n"
                    + "\t lastName: " + lastName + "\n"
                    + "\t firstName: " + firstName + "\n"
                    + "\t paymentNo: " + paymentNo + "\n"
                    + "\t loanEventNo: " + loanEventNo + "\n"
                    + "\t Reason: " + Reason + "\n"
                    + "\t datePaid: " + datePaid + "\n"
                    + "\t amountPaid: " + amount + "\n"

            
                    );
          }
//      ===========================================
//      5 Query
//      =======================================
      // Execute a SELECT statement
      statement5 = connection.createStatement();
      String query5 = "SELECT a.*"
      		+ " FROM CheckedOut co, Category, Availability a"
      		+ " WHERE co.itemNo = a.itemNo"
      		+ " AND Category.materialType = a.materialType"
      		+ " AND 180 < DATEDIFF(now(), last_loan_date);";
      rs5 = statement5.executeQuery(query5);

      System.out.println(
    	         "5. List the copies of library items that are grossly overdue	"
    	        		);
      
      // Display the results of a SELECT statement
      while (rs5.next()) {
    	  String itemNo = rs5.getString("itemNo");
          String shelfNumber = rs5.getString("shelfNumber");
          String status = rs5.getString("status");
          String condition = rs5.getString("condition");
          String materialType = rs5.getString("materialType");
          String Book_ISBN = rs5.getString("Book_ISBN");
          String Movie_UPC = rs5.getString("Movie_UPC");
          String Magazine_UPC = rs5.getString("Magazine_UPC");
          String compactDisc_UPC = rs5.getString("compactDisc_UPC");
          String audioBook_ISBN = rs5.getString("audioBook_ISBN");
          
         
          System.out.println(
              	"\t itemNo: " + itemNo + "\n"
                  + "\t shelfNumber: " + shelfNumber + "\n"
                  + "\t status: " + status + "\n"
                  + "\t condition: " + condition + "\n"
                  + "\t materialType: " + materialType + "\n"
                  + "\t Book_ISBN: " + Book_ISBN + "\n"
                  + "\t Movie_UPC: " + Movie_UPC + "\n"
                  + "\t Magazine_UPC: " + Magazine_UPC + "\n"
                  + "\t compactDisc_UPC: " + compactDisc_UPC + "\n"
                  + "\t audioBook_ISBN: " + audioBook_ISBN + "\n"
                  
        		  );

      }
//    ===========================================
//    6 Query
//    =======================================
    // Execute a SELECT statement
    statement6 = connection.createStatement();
    String query6 = "SELECT pending.*"
    		+ " From pendingRequest pending"
    		+ " WHERE Active = 'Active';";

    		
    rs6 = statement6.executeQuery(query6);

    System.out.println(
  	         "6. List the details of the current pending requests in the system."
  	        		);
    
    // Display the results of a SELECT statement
    while (rs6.next()) {
    	String itemNo = rs6.getString("itemNo");
        String requestNo = rs6.getString("requestNo");
        String cardNo = rs6.getString("cardNo");
        String dateRequested = rs6.getString("dateRequested");
        String holdStatus = rs6.getString("holdStatus");
        String dateHold = rs6.getString("dateHold");
        String Active = rs6.getString("Active");
        
        System.out.println(
              	"\t itemNo: " + itemNo + "\n"
                  + "\t requestNo: " + requestNo + "\n"
                  + "\t cardNo: " + cardNo + "\n"
                  + "\t dateRequested: " + dateRequested + "\n"
                  + "\t holdStatus: " + holdStatus + "\n"
                  + "\t dateHold: " + dateHold + "\n"
                  + "\t Active: " + Active + "\n"
               
        		  );
    }
//  ===========================================
//  7 Query
//  =======================================
  // Execute a SELECT statement
  statement7 = connection.createStatement();
  String query7 = "SELECT ROUND(SUM(amountPaid),2) AS TOTAL_REVENUE"
  		+ " FROM Payment"
  		+ " WHERE datePaid BETWEEN '2014-04-01' AND '2014-10-01';";
  		
  		
  rs7 = statement7.executeQuery(query7);

  System.out.println(
	         "7. Identify the total fines revenue to the library between April 1, 2014 to October 1, 2014."
	        		);
 
  // Display the results of a SELECT statement
  while (rs7.next()) {
	  Double TOTAL_REVENUE = rs7.getDouble("TOTAL_REVENUE");
	    
	    NumberFormat currency = NumberFormat.getCurrencyInstance();
	    String amount = currency.format(TOTAL_REVENUE);
	    
	    System.out.println(
	        "\t Total Revenue: 	" + amount + "\n");

  }
//===========================================
//8 Query
//=======================================
// Execute a SELECT statement
statement8 = connection.createStatement();
String query8 = "SELECT c.*"
		+ " FROM Category c;";
		
		
rs8 = statement8.executeQuery(query8);

System.out.println(
	         "8. List the details of the checkout periods and the numbers of renewals for all the categories of library items."
	        		);

// Display the results of a SELECT statement
while (rs8.next()) {
    String materialType = rs8.getString("materialType");
    String checkoutPeriod = rs8.getString("checkoutPeriod");
    String numberOfRenewals = rs8.getString("numberOfRenewals");
    System.out.println(
          	"\t materialType: " + materialType + "\n"
              + "\t checkoutPeriod: " + checkoutPeriod + "\n"
              + "\t numberOfRenewals: " + numberOfRenewals + "\n"
           
    		  );
}

//===========================================
//9 Query
//=======================================
// Execute a SELECT statement
statement9 = connection.createStatement();
String query9 = "SELECT co.cardNo, COUNT(*) AS COUNT, a.*"
		+ " From CheckedOut co, Availability a"
		+ " WHERE co.cardNo = 'A000000003'"
		+ " AND co.itemNo = a.itemNo"
		+ " GROUP BY co.cardNo;";
		
		
rs9 = statement9.executeQuery(query9);

System.out.println(
         "9. List the total number and the details of library items that are checked out and renewed by a patron."
        		);

// Display the results of a SELECT statement
while (rs9.next()) {
	String cardNo = rs9.getString("cardNo");
	String COUNT = rs9.getString("COUNT");
	String itemNo = rs9.getString("itemNo");
    String shelfNumber = rs9.getString("shelfNumber");
    String status = rs9.getString("status");
    String condition = rs9.getString("condition");
    String materialType = rs9.getString("materialType");
    String Book_ISBN = rs9.getString("Book_ISBN");
    String Movie_UPC = rs9.getString("Movie_UPC");
    String Magazine_UPC = rs9.getString("Magazine_UPC");
    String compactDisc_UPC = rs9.getString("compactDisc_UPC");
    String audioBook_ISBN = rs9.getString("audioBook_ISBN");
    
   
    System.out.println(
    		"\t cardNo: " + cardNo + "\n"
    		+ "\t COUNT: " + COUNT + "\n"
        	+ "\t itemNo: " + itemNo + "\n"
            + "\t shelfNumber: " + shelfNumber + "\n"
            + "\t status: " + status + "\n"
            + "\t condition: " + condition + "\n"
            + "\t materialType: " + materialType + "\n"
            + "\t Book_ISBN: " + Book_ISBN + "\n"
            + "\t Movie_UPC: " + Movie_UPC + "\n"
            + "\t Magazine_UPC: " + Magazine_UPC + "\n"
            + "\t compactDisc_UPC: " + compactDisc_UPC + "\n"
            + "\t audioBook_ISBN: " + audioBook_ISBN + "\n"
            
  		  );
}  
//===========================================
//10 Query
//=======================================
//Execute a SELECT statement
		statement10 = connection.createStatement();
		String sql10 = "INSERT INTO `pendingRequest` (`requestNo`,`itemNo`,`cardNo`,`dateRequested`,`holdStatus`,`dateHold`,`Active`)"
				+ " VALUES ('R0000000002','C487357280','A000000002','2015-12-01','ON HOLD','2015-12-01','Active');";
		System.out.println(
		         "10. Insert a new pending request made by a patron. ");
		    		
		statement10.executeUpdate(sql10);

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
        	try {
                if (statement10 != null) {
                    statement10.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        	try {
                if (statement9 != null) {
                    statement9.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        	try {
                if (statement8 != null) {
                    statement8.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        	try {
                if (rs7 != null) {
                    rs7.close();
                }
                if (statement7 != null) {
                    statement7.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        	try {
                if (rs6 != null) {
                    rs6.close();
                }
                if (statement6 != null) {
                    statement6.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        	try {
                if (rs5 != null) {
                    rs5.close();
                }
                if (statement5 != null) {
                    statement5.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        	try {
                if (rs4 != null) {
                    rs4.close();
                }
                if (statement4 != null) {
                    statement4.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        	try {
                if (rs3 != null) {
                    rs3.close();
                }
                if (statement3 != null) {
                    statement3.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
            try {
                if (rs2 != null) {
                    rs2.close();
                }
                if (statement2 != null) {
                    statement2.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
            try {
                if (rs1 != null) {
                    rs1.close();
                }
                if (statement1 != null) {
                    statement1.close();
                }
                
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

    }
}

