package cmpe275Project;

//remember to set port to 9000 in application.properies
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//enable redis http session store for the backend app also
//@EnableRedisHttpSession
public class Cmpe275ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(Cmpe275ProjectApplication.class, args);
    }
}
