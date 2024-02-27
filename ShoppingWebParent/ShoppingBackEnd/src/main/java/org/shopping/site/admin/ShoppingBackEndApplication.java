package org.shopping.site.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan({"org.shopping.entity", "org.shopping.site.admin.user"})
public class ShoppingBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingBackEndApplication.class, args);
    }

}
