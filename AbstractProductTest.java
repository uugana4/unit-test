import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AbstractProductTest {

    @Test
    void testNameGetterSetter() {
        Product product = new Product("Bread", 2.0, 30);
        product.setName("Butter");
        assertEquals("Butter", product.getName());
    }
}
