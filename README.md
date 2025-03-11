# spring-pet-adoption
Sample application illustrating different test data approaches

## Description
Simple Spring-Boot REST API. Idea inspired by the spring pet clinic sample application.
Built using Spring-Boot and Spring Modulith. Application divided into 4 modules
- common: contains some common code
- pets: contains CRUD operations on pets
- owners: contains CRUD operations on owners
- adoptions: contains logic to allow pets to be adopted using the system

## Branches
- main
- mockaroo
- datafaker
- datafaker-fixtures
- instancio
- instancio-fixtures
- instancio-fixture-builder

### main
- Starting point of the demonstration
- Test data is static and hard coded
- This shows what should be avoided or can be improved

### mockaroo
- Rewritten some of the tests (`@DataJpaTest`)
- Include testcontainers
- Include some sample `.sql` files in test/resources/data
- Use the .sql files in the tests with the `@Sql` annotation

#### datafaker & datafaker-fixtures
- Replace static data by generated data using the datafaker library
- datafaker-fixtures moves the generation to fixture classes to avoid coupling the tests to the datafaker api

### instancio & instancio-fixtures
- Replace static data by data generated using instancio
- instancio-fixtures avoids coupling the tests to the instancio API by using instancio in fixtures

### abstract-fixture-builder
- manually add a builder pattern on top of instancio-fixtures
- only added demo for PetFixtures and OwnerFixtures

### instancio-fixture-builder
- builds on top of the instancio-fixtures branch
- avoids writing (boilerplate) factory methods by adding a builder pattern
- generates the code for the `AbstractFixtureBuilder` implementation