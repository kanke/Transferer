package org.revolut.dropwizard;


import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.revolut.exception.AccountTransactionException;
import org.revolut.resource.AccountResource;
import org.revolut.resource.TransactionResource;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import javax.security.auth.login.AccountException;

@Slf4j
public class App extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        log.info("Registering REST resources");
        environment.jersey().register(TransactionResource.class);
        environment.jersey().register(AccountResource.class);
        environment.jersey().register(AccountException.class);
        environment.jersey().register(AccountTransactionException.class);
        environment.admin().addTask(new ShutdownTask());
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(new AppGuiceModule())
                .build());
    }

}
