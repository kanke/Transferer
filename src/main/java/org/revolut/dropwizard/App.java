package org.revolut.dropwizard;


import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.revolut.resource.TransferResource;

@Slf4j
public class App extends Application<Configuration> {
    public void run(Configuration configuration, Environment environment) {

        //****** Dropwizard REST Endpoints ***********//
        log.info("Registering REST resources");
        final TransferResource transferResource = new TransferResource();
        environment.jersey().register(transferResource);

    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}
