import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    void testRoleValues() {
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.USER, Role.valueOf("USER"));
        assertEquals(2, Role.values().length);
    }
}