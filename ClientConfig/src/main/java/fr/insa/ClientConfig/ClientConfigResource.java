package fr.insa.ClientConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientConfigResource {

    @Value("${db.uri}")
    private String dbURI;
    @Value("${db.name}")
    private String dbName;
    @Value("${db.login}")
    private String dbLogin;
    @Value("${db.password}")
    private String dbPassword;

    @GetMapping("/db")
    public String getDbConfig() {
        return "URI : " + dbURI + ", name : " + dbName + ", login : " + dbLogin + ", password : " + dbPassword;
    }
}
