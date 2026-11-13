package mpkm;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class TestHibernate {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class)
                .web(WebApplicationType.NONE)
                .headless(true)
                .run(args)) {
            TransaksiRepository repo = context.getBean(TransaksiRepository.class);

            Transaksi data = new Transaksi(
                "PEMASUKAN",
                new BigDecimal("150000"),
                LocalDate.now(),
                "Gaji",
                "Tes Hibernate"
            );

            repo.save(data);

            System.out.println("Daftar transaksi: " + repo.count());
            System.out.println("Total pemasukan: " + repo.total("PEMASUKAN"));
            System.out.println("Saldo: " + repo.saldo());
        }
    }
}
