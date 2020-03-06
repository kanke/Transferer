# Transferer

<img src="https://i.imgur.com/Kqog97Y.jpg" width="300" height="200" />

#  :bowtie: About this application #
A RESTful API (including data model and the backing implementation) for money transfers between accounts.

##  Original Problem Statement ##
Explicit requirements:
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense

##  To run this project ##

> :computer: -  Clone to computer

>  🧭 -  Navigate to root project directory of project i.e `cd /{path-to-project}`

> 🧹 -  Run `mvn clean install`

> :package: - Run  `mvn package`

> :runner: -  Run `java -jar target/Transferer-1.0-SNAPSHOT.jar server`

> :bangbang: **NOTE** -  You might have to install lombok plugin for your IDE if viewing this locally

###  To run Tests ###

> 🧹 -  Run `mvn verify` <- Runs unit and integration tests

##  Tools ##
- Used concurrentHashMap as a  threadsafe data store but this can be switched to an in memory database in future eg Redis, h2 by providing a different implementation
- Dropwizard as a lightweight package to build application
- Lombok as a library that automatically plugs into your editor and build tool for less boiler plate
- Guice for dependency injection and dropwizard guicey for auto configuration
- Dropwizard Testing for integration test to start up entire application and hit it with real HTTP requests during. Starts and stops the Dropwizard application containing the test doubles.
- Mockito and JUnit to write unit test

##  API Support ##
`POST /transfer`
Transfer funds between accounts of same currency
```
{
"debitAccountId":"2",
"creditAccountId":"2",
"amount":"2",
"reference":"bruv"
}
```
\
`POST /account`
Create new bank account
```
{
"accountName":"test",
"balance":"50",
"currencyCode":"GBP"
}
```

##  Future Improvements ##
Due to time constraints, I was unable to implement a lot but in future I will love to support the below;
1. Currency conversions and multi-currency operations
2. Add Api documentation like Swagger
3. Expand the api to include other requests like getting account, getting all transactions for account etcetera
4. Version API - Format versions should go in headers—Accept-Version and Content-Version—in the request and response
5. Access policies - Restrictions from certain networks, rate limits, guidance of caching of API call content
   etcetera
6. Add Dropwizard config file to add different config such as different port for server and running integration tests
7. Add terms & conditions and legal obligations




