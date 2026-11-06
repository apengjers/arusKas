package mpkm;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestHibernate {
    public static void main(String[] args) {
        try {
            TransaksiRepository repo = new TransaksiRepository();

            Transaksi data = new Transaksi(
                "PEMASUKAN",
                new BigDecimal("150000"),
                LocalDate.now(),
                "Gaji",
                "Tes Hibernate"
            );

            repo.tambah(data);

            System.out.println("Daftar transaksi: " + repo.semua().size());
            System.out.println("Total pemasukan: " + repo.total("PEMASUKAN"));
            System.out.println("Saldo: " + repo.saldo());
        } finally {
            HibernateUtil.tutup();
        }
    }
}