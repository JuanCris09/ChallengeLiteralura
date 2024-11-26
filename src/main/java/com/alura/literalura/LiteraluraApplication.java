package com.alura.literalura;

import com.alura.literalura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
//        (exclude = {DataSourceAutoConfiguration.class})
public class LiteraluraApplication implements CommandLineRunner {
    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {

        SpringApplication.run(LiteraluraApplication.class, args);
    }

    public void run(String... args) throws Exception {
        Principal principal = context.getBean(Principal.class);
        principal.muestraMenu();
    }

}
