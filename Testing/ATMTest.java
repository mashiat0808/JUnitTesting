import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.NumberFormat;

public class ATMTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private Account checking;
    private Account savings;
    private ATM atm;

    @BeforeEach
    public void setUpStreams() {
        // Redirect System.out to capture printed messages
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    public void setUp() {
        checking = new Account();
        checking.setType("Checking");
        checking.setBalance(0.00);
        checking.setRate(0.00);

        savings = new Account();
        savings.setType("Savings");
        savings.setBalance(0.00);
        savings.setRate(2.00);

        atm = new ATM();
    }

    @Test
    public void testDepositToSavings() {
        // Simulate user input to deposit $200.0 into savings
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1\n200.0\n".getBytes());
        System.setIn(in);

        atm.run();

        // Check if the output contains the new savings balance
        assertTrue(outContent.toString().contains("Your Savings balance is now: $200.00"));
    }

    @Test
    public void testWithdrawFromChecking() {
        // Set up initial balance in checking
        checking.setBalance(500.0);

        // Simulate user input to withdraw $200.0 from checking
        ByteArrayInputStream in = new ByteArrayInputStream("2\n2\n200.0\n".getBytes());
        System.setIn(in);

        atm.run();

        // Check if the output contains the new checking balance
        assertTrue(outContent.toString().contains("Your Checking balance is now: $300.00"));
    }

    @Test
    public void testTransferFromSavingsToChecking() {
        // Set up initial balances
        savings.setBalance(1000.0);
        checking.setBalance(500.0);

        // Simulate user input to transfer $200.0 from savings to checking
        ByteArrayInputStream in = new ByteArrayInputStream("3\n1\n200.0\n".getBytes());
        System.setIn(in);

        atm.run();

        // Check if the output contains the new balances
        assertTrue(outContent.toString().contains("You successfully transferred $200.00 from Savings to Checking"));
        assertTrue(outContent.toString().contains("Checking Balance: $700.00"));
        assertTrue(outContent.toString().contains("Savings Balance: $800.00"));
    }

    // Add similar tests for other functionalities (checking balance, end session, invalid input, etc.)

    @Test
    public void testEndSession() {
        // Simulate user input to end the session
        ByteArrayInputStream in = new ByteArrayInputStream("5\n".getBytes());
        System.setIn(in);

        atm.run();

        // Check if the output contains the ending message
        assertTrue(outContent.toString().contains("Thank you for banking with us!"));
    }

    @Test
    public void testInvalidInput() {
        // Simulate user input with an invalid option
        ByteArrayInputStream in = new ByteArrayInputStream("6\n".getBytes());
        System.setIn(in);

        atm.run();

        // Check if the output contains the "Invalid Choice" message
        assertTrue(outContent.toString().contains("Invalid selection. Please choose a valid option."));
    }

    @Test
    public void testInvalidWithdrawalAmount() {
        // Simulate user input with a withdrawal amount greater than balance
        checking.setBalance(200.0); // Set a lower balance

        ByteArrayInputStream in = new ByteArrayInputStream("2\n2\n300.0\n".getBytes());
        System.setIn(in);

        atm.run();

        // Check if the output contains the "Insufficient Funds" message
        assertTrue(outContent.toString().contains("Insufficient funds. Your Checking balance is $200.00"));
    }

    @Test
    public void testNegativeDepositAmount() {
        // Simulate user input with a negative deposit amount
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1\n-50.0\n".getBytes());
        System.setIn(in);

        atm.run();

        // Check if the output contains the "Invalid Deposit Amount" message
        assertTrue(outContent.toString().contains("Invalid deposit amount. Please enter a positive value."));
    }
}
