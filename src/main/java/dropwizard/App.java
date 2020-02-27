package dropwizard;


import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application<Configuration> {
    public void run(Configuration configuration, Environment environment) {

        //****** Dropwizard REST Endpoints ***********//
        log.info("Registering REST resources");

    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}
