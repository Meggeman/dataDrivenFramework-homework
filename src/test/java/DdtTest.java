import Mysql.MySqlJDBC;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import java.security.Security;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DdtTest {

    static {
        // Add TLS 1.0 to the enabled protocols
        Security.setProperty("jdk.tls.client.protocols", "TLSv1, TLSv1.1, TLSv1.2, TLSv1.3");
    }

    @BeforeTest
    public void setUp() {
        try (Connection connection = MySqlJDBC.getConnection()) {
            System.out.println(String.format("Connected to database: %s", connection.getCatalog()));
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void dbTest() throws SQLException {
        open("https://demoqa.com/automation-practice-form");
        SelenideElement firstName = $(By.id("firstName"));
        SelenideElement lastName = $(By.id("lastName"));
        SelenideElement phone = $(By.id("userNumber"));

        String sql = "SELECT firstName, lastName, phone FROM dbo.students";
        try (Connection conn = MySqlJDBC.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                firstName.setValue(rs.getString("firstName"));
                lastName.setValue(rs.getString("lastName"));
                phone.setValue(rs.getString("phone"));

                firstName.clear();
                lastName.clear();
                phone.clear();
            }
        }
    }
}
