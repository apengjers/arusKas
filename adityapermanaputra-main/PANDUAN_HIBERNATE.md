# Hibernate untuk Transaksi ArusKAS

Hibernate menyimpan objek `Transaksi` ke tabel `transaksi`. Database `aruskas` dan tabelnya dibuat atau disesuaikan otomatis saat aplikasi pertama kali terhubung.

Konfigurasi koneksi ada di `src/main/resources/hibernate.cfg.xml`. Sesuaikan username dan password MySQL dengan pengaturan di komputer.

Contoh pemakaian:

```java
TransaksiRepository repository = new TransaksiRepository();

Transaksi data = new Transaksi(
    "PEMASUKAN",
    new BigDecimal("150000"),
    LocalDate.now(),
    "Gaji",
    "Uang saku bulanan"
);

repository.tambah(data);
System.out.println(repository.saldo());

HibernateUtil.tutup();
```

Gunakan `"PENGELUARAN"` untuk transaksi pengeluaran. Method repository yang tersedia adalah `tambah`, `semua`, `cari`, `ubah`, `hapus`, `total`, dan `saldo`. Panggil `HibernateUtil.tutup()` saat aplikasi selesai.
