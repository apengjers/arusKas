package mpkm;

import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TransaksiRepository {

    public void tambah(Transaksi data) {
        jalankan(session -> session.persist(data));
    }

    public List<Transaksi> semua() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Transaksi order by tanggal desc", Transaksi.class).list();
        }
    }

    public Transaksi cari(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Transaksi.class, id);
        }
    }

    public void ubah(Transaksi data) {
        jalankan(session -> session.merge(data));
    }

    public void hapus(long id) {
        jalankan(session -> {
            Transaksi data = session.get(Transaksi.class, id);
            if (data != null) {
                session.remove(data);
            }
        });
    }

    public BigDecimal total(String jenis) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            BigDecimal hasil = session.createQuery(
                    "select sum(t.nominal) from Transaksi t where t.jenis = :jenis", BigDecimal.class)
                    .setParameter("jenis", jenis)
                    .uniqueResult();
            return hasil == null ? BigDecimal.ZERO : hasil;
        }
    }

    public BigDecimal saldo() {
        return total("PEMASUKAN").subtract(total("PENGELUARAN"));
    }

    private void jalankan(PerintahTransaksi perintah) {
        Transaction transaksi = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaksi = session.beginTransaction();
            perintah.jalankan(session);
            transaksi.commit();
        } catch (RuntimeException e) {
            if (transaksi != null && transaksi.isActive()) {
                transaksi.rollback();
            }
            throw e;
        }
    }

    @FunctionalInterface
    private interface PerintahTransaksi {
        void jalankan(Session session);
    }
}
