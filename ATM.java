package atm;
 import java.util.*;
 import java.sql.*;
public class ATM {
     String dbURL = "jdbc:mysql://localhost:3306/atm_java";
     String username = "root";
     String password = "";
       int num;
       String lastname;
       String firstname;
       String middlename;
       int pin1;
       int bal;
      //constructor   
     public ATM(int num, String lastname, String firstname, String middlename, int pin1, int bal)
    {
        this.num=num;
        this.lastname=lastname;
        this.firstname=firstname;
        this.middlename=middlename;
        this.pin1=pin1;
        this.bal=bal;
    }
    public static void main(String[] args) {
		int choice=0;
                int choice1=0;
                Scanner scn= new Scanner(System.in);
                Scanner scn3= new Scanner(System.in);
                boolean exit=false;
                int temp;
                
                //constructor
                 ATM atmm=new ATM(87096, "Corpuz","Dianne","Mendoza",1289, 5000);
         do
       {
        System.out.println("");
        System.out.println("======MENU=======");
        System.out.println("[1] ADD NEW ACCOUNT");
        System.out.println("[2] TRANSACTION");
        System.out.println("[3] VIEW ALL");
        System.out.println("[4] Exit");
        System.out.print("Enter choice:  "); 
        choice= scn.nextInt();
        switch (choice)
            {
                case 1:
                  atmm.insert();
                break;
                //transactions
                case 2:
                     boolean exit1=false;
                       do
                    {
                    System.out.println("");
                    System.out.println("======TRANSACTIONS=======");
                    System.out.println("[1] CHECK BALANCE");
                    System.out.println("[2] DEPOSIT");
                    System.out.println("[3] WITHDRAW");
                    System.out.println("[4] BACK");
                    System.out.print("Enter choice:  "); 
                    choice1= scn3.nextInt();
                    switch (choice1)
                        {
                        case 1://checkbalance
                        atmm.checkbalance();
                        break;
                        case 2://deposit
                        atmm.deposit();
                        break;
                        case 3: //withdraw
                        atmm.withdraw();
                        break;
                        case 4://back to main menu
                        System.out.println("Back to main menu"); 
                        exit1=true;
                        break;
                        }
                     }while(!exit1);
               break; 
                case 3: //viewall
                      atmm.viewAll();
                break;
                
                case 4://exit
                  System.out.println("System Exit"); 
                    exit=true;
                break;
       }
         }while(!exit);
    }

    //insert 
     public void insert()
    {
        Scanner scn2 = new Scanner(System.in);
        int temp;
       try (Connection conn = DriverManager.getConnection(dbURL, username, password)) {
                        System.out.println("Enter first name: ");
                        this.firstname =scn2.nextLine();
                        
                        System.out.println("Enter last name: ");
                        this.lastname =scn2.nextLine();
                        
                        System.out.println("Enter middle name: ");
                       this.middlename =scn2.nextLine();
                        
                         System.out.println("Enter account number: ");
                       this.num =scn2.nextInt();
                       String accnum=Integer.toString(this.num);
                        
                        System.out.println("Enter pincode: ");
                       this.pin1 =scn2.nextInt();
                       String pin=Integer.toString(this.pin1);
                        
                      this.bal=0;
                       String bal1=Integer.toString(this.bal);
			String sql = "INSERT INTO accounts(acc_firstname, acc_lastname, acc_middlename, "
                                + "acc_num, pincode, balance) VALUES (?, ?, ?, ?, ?, ?)";
			
			PreparedStatement statement = conn.prepareStatement(sql);
	 		
                        
			statement.setString(1, this.firstname);
                        statement.setString(2, this.lastname);
			statement.setString(3, this.middlename);
                        statement.setString(4, accnum);
                        statement.setString(5, pin);
                        statement.setString(6, bal1);
			
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new user was inserted successfully!");
			}

		} catch (SQLException ex) {
                     System.out.println("There is something wrong");
		}
    }
    
    public void viewAll()
    {
      try (Connection conn = DriverManager.getConnection(dbURL, username, password)) {
			
                     	String sql = "SELECT * FROM accounts";
			
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			int count = 0;
                        
			System.out.println("User Number-Account Number-Lastname-Firstname-Middlename-Pincode-Balance");
                        
                        
			while (result.next()){
                                 this.num=result.getInt("acc_num");
                                 this.lastname = result.getString("acc_lastname");
                                 this.firstname = result.getString("acc_firstname");
                                 this.middlename = result.getString("acc_middlename");
                                 this.pin1=result.getInt("pincode");
                                 this.bal=result.getInt("balance");
                                     
				String output = "Account #%d:     %s       %s    %s    %s    %s      %s";
		System.out.println(String.format(output, ++count, this.num, this.lastname, this.firstname, this.middlename, this.pin1, this.bal));}
			
		} catch (SQLException ex) {
                    System.out.println("WRONG");
		}		
    }  
    
     public void withdraw()
    {
        Scanner scn3 = new Scanner(System.in); 
    try (Connection conn = DriverManager.getConnection(dbURL, username, password)) {
			double currentbalance=0;
                        
                      System.out.println("Enter your account number: ");
                       this.num =scn3.nextInt();
                       
                       System.out.println("Enter your pincode: ");
                       this.pin1 =scn3.nextInt();
                       
                        System.out.println("Enter the amount you will withdraw: ");
                       double withdraw =scn3.nextDouble();
                       
                     	String sql = "SELECT * FROM accounts WHERE acc_num=? AND pincode=?";
                        
                        PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, this.num);
                        statement.setInt(2, this.pin1);
                        
			ResultSet result = statement.executeQuery();
			
                        
			while (result.next()){
                                 this.bal=result.getInt("balance");
                                 
                                    if(this.bal==0)
                                    {
                                    System.out.println("You can't withdraw because your balance is 0");
                                    }
                                    else if(this.bal<withdraw)
                                    {
                                    System.out.println("You can't withdraw because your current balance is only "+this.bal);
                                    }
                                     else
                                    {
                                        currentbalance=this.bal-withdraw;
                                         String sqlupdate = "UPDATE accounts SET balance=?  WHERE acc_num=? AND pincode=?";
                                        PreparedStatement statementup = conn.prepareStatement(sqlupdate);
                       
                                        statementup.setDouble(1, currentbalance);
                                        statementup.setInt(2, this.num);
                                        statementup.setInt(3, this.pin1);
			
                                        int rowsUpdated = statementup.executeUpdate();
                                            if (rowsUpdated > 0 && currentbalance>0) {
                                                System.out.println("You have successfully withdraw!"); }
                                     }
                                 }
                        } catch (SQLException ex) {
                            System.out.println("There is something wrong");
                        }
    }
    
      public void deposit()
    {
       Scanner scn3 = new Scanner(System.in);  
    try (Connection conn = DriverManager.getConnection(dbURL, username, password)) {
			double currentbalance=0;
                        
                      System.out.println("Enter your account number: ");
                       this.num =scn3.nextInt();
                       
                       System.out.println("Enter your pincode: ");
                      this.pin1 =scn3.nextInt();
                       
                        System.out.println("Enter the amount you will deposit: ");
                       double dep =scn3.nextDouble();
                       
                     	String sql = "SELECT * FROM accounts WHERE acc_num=? AND pincode=?";
                        PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, this.num);
                        statement.setInt(2, this.pin1);
                        
                        
                        
                        
			ResultSet result = statement.executeQuery();
			
			
			while (result.next()){
                                 this.bal=result.getInt("balance");
                                 
                                 currentbalance=this.bal+dep;
                                 }
                       
                       String sqlupdate = "UPDATE accounts SET balance=?  WHERE acc_num=? AND pincode=?";
                       PreparedStatement statementup = conn.prepareStatement(sqlupdate);
                       
                        statementup.setDouble(1, currentbalance);
                        statementup.setInt(2, this.num);
                        statementup.setInt(3, this.pin1);
			
			int rowsUpdated = statementup.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("You have successfully deposited!");
			}
                     } catch (SQLException ex) {
                     System.out.println("There is something wrong");
                     }
    }
     
      
      
      
      
      
      
       public void checkbalance()
    {
        Scanner scn3 = new Scanner(System.in); 
     try (Connection conn = DriverManager.getConnection(dbURL, username, password)) {
			
                      System.out.println("Enter your account number: ");
                       this.num =scn3.nextInt();
                       
                       System.out.println("Enter your pincode: ");
                      this.pin1 =scn3.nextInt();
                       
                     	String sql = "SELECT * FROM accounts WHERE acc_num=? AND pincode=?";
                        
                        PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, this.num);
                        statement.setInt(2, this.pin1);
                        
			ResultSet result = statement.executeQuery();
			while (result.next()){
                                this.bal=result.getInt("balance");
				String output = "%s";
                                System.out.println(String.format("Your Balance is "+output,this.bal));}
                   
                        } catch (SQLException ex) {
                            System.out.println("There is something wrong");
                        }
    }
}
