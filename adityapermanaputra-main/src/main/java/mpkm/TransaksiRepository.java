package mpkm;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {

    List<Transaksi> findAllByOrderByTanggalDesc();

    @Query("select sum(t.nominal) from Transaksi t where t.jenis = :jenis")
    BigDecimal jumlahUntukJenis(@Param("jenis") String jenis);

    default BigDecimal total(String jenis) {
        BigDecimal hasil = jumlahUntukJenis(jenis);
        return hasil == null ? BigDecimal.ZERO : hasil;
    }

    default BigDecimal saldo() {
        return total("PEMASUKAN").subtract(total("PENGELUARAN"));
    }
}
