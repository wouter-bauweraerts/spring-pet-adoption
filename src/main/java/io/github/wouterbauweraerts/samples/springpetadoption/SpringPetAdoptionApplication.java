package io.github.wouterbauweraerts.samples.springpetadoption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringPetAdoptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPetAdoptionApplication.class, args);
    }

}
