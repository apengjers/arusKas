package mpkm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String jenis;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal nominal;

    @Column(nullable = false)
    private LocalDate tanggal;

    @Column(nullable = false, length = 100)
    private String kategori;

    @Column(length = 255)
    private String keterangan;

    public Transaksi() {
    }

    public Transaksi(String jenis, BigDecimal nominal, LocalDate tanggal, String kategori, String keterangan) {
        this.jenis = jenis;
        this.nominal = nominal;
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.keterangan = keterangan;
    }

    public Long getId() {
        return id;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public BigDecimal getNominal() {
        return nominal;
    }

    public void setNominal(BigDecimal nominal) {
        this.nominal = nominal;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
