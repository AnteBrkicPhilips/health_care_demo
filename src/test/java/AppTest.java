import com.example.healthcare.HealthcareSystem;
import org.junit.Test;

public class AppTest {

    private HealthcareSystem sut = new HealthcareSystem();

    @Test
    public void testApp(){
        sut.run();
    }
}
