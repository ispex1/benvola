package fr.insa.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootApplication
public class UserServiceApplication {

	@Value("${db.uri}")
	private String dbURI;
	@Value("${db.name}")
	private String dbName;
	@Value("${db.login}")
	private String dbLogin;
	@Value("${db.password}")
	private String dbPassword;

	@Bean
	public Connection connection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(dbURI, dbLogin, dbPassword);
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
