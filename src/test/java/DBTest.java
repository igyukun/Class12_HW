import java.sql.*;

public class DBTest {

    private static final String USER_NAME = "I2j7L7VaSN";
    private static final String DATABASE_NAME = "I2j7L7VaSN";
    private static final String PASSWORD = "p8FNdS5KJh";
    private static final String PORT = "3306";
    private static final String SERVER = "remotemysql.com";

    private static Connection con; //Global connection object

    public static void main(String[] args) throws SQLException {
        //Establish a connection to the DB using JDBC
        con = DriverManager.getConnection("jdbc:mysql://"+SERVER+":"+PORT, USER_NAME, PASSWORD);

        //Delete the table before program execution
        try{
            dropTable();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        //Create the table and show that it is empty
        System.out.println("1. Create 'dogs' table.");
        createTable();
        getTableContent();
        //Insert three records into the table and show them
        System.out.printf("%n2. Insert three records.%n");
        insertData("Rexy", 5, "German shepherd");
        insertData("King", 3, "Husky");
        insertData("Lady", 6, "Collie");
        getTableContent();
        //Update the second record and delete the third. Show the final table content
        System.out.printf("%n3. Update the second record 'age' field value and delete the third record.%n");
        updateAgeValue("King",7);
        deleteRecord("Lady");
        getTableContent();
        //close connection
        con.close();
    }

    /**
     * Creates a new table 'dogs' with three fields and no primary key
     * @throws SQLException
     */
    public static void createTable() throws SQLException{
        String statementToExecute = "CREATE TABLE " + DATABASE_NAME +
                ".dogs(name VARCHAR(40) NOT NULL,age INT NOT NULL,breed VARCHAR(30) NOT NULL);";
        con.createStatement().execute(statementToExecute);
    }

    /**
     * Inserts a single record to the 'dogs' table
     * @param name - a dog's name
     * @param age - a dog's age
     * @param breed - a dog's breed
     * @throws SQLException
     */
    public static void insertData (String name, int age, String breed) throws SQLException{
        String statementToExecute = "Insert into " + DATABASE_NAME + ".dogs(name, age, breed) " +
                "Values ('" + name + "','" + age + "','" + breed + "');";
        con.createStatement().execute(statementToExecute);
    }

    /**
     * Updates the age field of a record selected, using the dogs.name field. It is possible that multiple records will be selected if there are
     * more than one record with the same value in the 'name' field.
     * @param name - dog's name to search a record by
     * @param newAge - a new Age value
     * @throws SQLException
     */
    public static void updateAgeValue(String name, int newAge) throws SQLException{
        String statementToExecute = "UPDATE `" + DATABASE_NAME + "`.`dogs` SET `age`='"+newAge+
                "' WHERE `name`='" + name + "';";
        con.createStatement().executeUpdate(statementToExecute);
    }

    /**
     * Deletes a record selected, using a dogs.name field. It is possible that multiple records will be selected if there are
     * more than one record with the same value in the 'name' field
     * @param name - dog's name to search a record by
     * @throws SQLException
     */
    public static void deleteRecord(String name) throws SQLException{
        String statementToExecute = "DELETE FROM `" + DATABASE_NAME + "`.`dogs` WHERE `name`='"+name+"';";
        con.createStatement().execute(statementToExecute);
    }

    /**
     * Selects the entire contents of the 'dogs' table and print it as a formatted table
     * @throws SQLException
     */
    private static void getTableContent() throws SQLException {
        String statementToExecute = "SELECT * FROM " + DATABASE_NAME + ".dogs;";
        printHeader();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(statementToExecute);
        while (rs.next()) {
            //Retrieve by column name
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String breed = rs.getString("breed");
            //Display values
            printFormattedRecord(name, String.valueOf(age), breed);
        }
        printSeparatorLine();
        rs.close();
    }

    /**
     * Deletes the table 'dogs' from the database
     * @throws SQLException
     */
    private static void dropTable() throws SQLException{
        String statementToExecute = "DROP TABLE " + DATABASE_NAME + ".dogs;";
        con.createStatement().execute(statementToExecute);
    }

    /**
     * Prints the formatted table separator line
     */
    private static void printSeparatorLine(){
        System.out.println("|===============|====|====================|");
    }

    /**
     * Prints the formatted data line
     */
    private static void printFormattedRecord(String s1, String s2, String s3){
        System.out.printf("|%-15s|%-4s|%-20s|%n",s1, s2, s3);

    }

    /**
     * Print the header of the formatted table
     */
    private static void printHeader(){
        printSeparatorLine();
        printFormattedRecord("NAME","AGE","BREED");
        printSeparatorLine();
    }
}
