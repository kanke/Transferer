package org.revolut.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.revolut.resource.AccountResource;
import org.revolut.resource.TransactionResource;
import ru.vyarus.dropwizard.guice.GuiceBundle;

@Slf4j
public class App extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) {

        //****** Dropwizard REST Endpoints ***********//
        log.info("Registering REST resources");
//        final TransactionResource transactionResource = new TransactionResource();
//        final AccountResource accountResource = new AccountResource();
//        environment.jersey().register(transactionResource);
//        environment.jersey().register(accountResource);
        environment.jersey().register(TransactionResource.class);
        environment.jersey().register(AccountResource.class);

        //Guice.createInjector(new AppGuiceModule());
//        AccountResource accountResource = guice.getInstance(AccountResource.class);
//        TransactionResource transactionResource = guice.getInstance(TransactionResource.class);
//        TransactionService transactionService = guice.getInstance(TransactionService.class);
//        AccountService accountService = guice.getInstance(AccountService.class);
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
//        guiceBundle = GuiceBundle.builder()
//                //.enableAutoConfig("org.revolut.service")
//                .enableAutoConfig(getClass().getPackage().getName())
//                .build();
//        bootstrap.addBundle(guiceBundle);
//
//        GuiceBundle<Configuration> guiceBundle = GuiceBundle.defaultBuilder(Configuration.class)
//                .modules(new AppModule())
//                .build();
//
//        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(new AppGuiceModule())
                .build());
    }

}
