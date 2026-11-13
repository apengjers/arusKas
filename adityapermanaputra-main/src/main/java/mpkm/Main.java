package mpkm;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run(args);

        TransaksiRepository repository = context.getBean(TransaksiRepository.class);
        String databasePassword = context.getEnvironment().getProperty("spring.datasource.password", "");
        Runtime.getRuntime().addShutdownHook(new Thread(context::close));

        javax.swing.SwingUtilities.invokeLater(() -> {
            login frame = new login(repository, databasePassword);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
