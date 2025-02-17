package io.github.wouterbauweraerts.samples.springpetadoption;

import org.springframework.boot.SpringApplication;

public class TestSpringPetAdoptionApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringPetAdoptionApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
