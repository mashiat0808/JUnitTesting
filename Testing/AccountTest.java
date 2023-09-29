import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
    }

    @Test
    public void testInitialBalance() {
        assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDeposit() {
        account.deposit(100.0);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawSufficientFunds() {
        account.deposit(200.0);
        account.withdraw(50.0);
        assertEquals(150.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        account.deposit(50.0);
        // Attempt to withdraw more than the balance
        boolean result = account.withdraw(100.0);
        assertFalse(result); // Withdraw should fail
        assertEquals(50.0, account.getBalance(), 0.001); // Balance should remain unchanged
    }

    @Test
    public void testWithdrawNegativeAmount() {
        account.deposit(100.0);
        // Attempt to withdraw a negative amount
        boolean result = account.withdraw(-20.0);
        assertFalse(result); // Withdraw should fail
        assertEquals(100.0, account.getBalance(), 0.001); // Balance should remain unchanged
    }

    @Test
    public void testWithdrawZeroAmount() {
        account.deposit(100.0);
        // Attempt to withdraw zero amount
        boolean result = account.withdraw(0.0);
        assertFalse(result); // Withdraw should fail
        assertEquals(100.0, account.getBalance(), 0.001); // Balance should remain unchanged
    }
}
