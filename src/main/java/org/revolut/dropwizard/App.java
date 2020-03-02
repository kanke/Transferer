package org.revolut.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
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
        final TransactionResource transactionResource = new TransactionResource();
        environment.jersey().register(transactionResource);

    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .build());
    }

}
