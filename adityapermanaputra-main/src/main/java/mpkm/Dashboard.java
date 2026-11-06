package mpkm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class Dashboard extends JFrame {

    private final TransaksiRepository repository = new TransaksiRepository();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Jenis", "Nominal", "Tanggal", "Kategori", "Keterangan"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final JLabel totalPemasukan = new JLabel("Rp0", SwingConstants.CENTER);
    private final JLabel totalPengeluaran = new JLabel("Rp0", SwingConstants.CENTER);
    private final JLabel saldo = new JLabel("Rp0", SwingConstants.CENTER);
    private List<Transaksi> dataTransaksi;

    public Dashboard(String username) {
        setTitle("ArusKAS - " + username);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel ringkasan = new JPanel(new GridLayout(1, 3, 12, 0));
        ringkasan.add(kartu("Total Pemasukan", totalPemasukan));
        ringkasan.add(kartu("Total Pengeluaran", totalPengeluaran));
        ringkasan.add(kartu("Saldo", saldo));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton tambahPemasukan = new JButton("Tambah Pemasukan");
        JButton tambahPengeluaran = new JButton("Tambah Pengeluaran");
        JButton ubah = new JButton("Ubah");
        JButton hapus = new JButton("Hapus");
        JButton ekspor = new JButton("Ekspor CSV");
        JButton muatUlang = new JButton("Muat Ulang");

        tambahPemasukan.addActionListener(event -> bukaForm(null, "PEMASUKAN"));
        tambahPengeluaran.addActionListener(event -> bukaForm(null, "PENGELUARAN"));
        ubah.addActionListener(event -> ubahPilihan());
        hapus.addActionListener(event -> hapusPilihan());
        ekspor.addActionListener(event -> eksporCsv());
        muatUlang.addActionListener(event -> muatData());

        tombol.add(tambahPemasukan);
        tombol.add(tambahPengeluaran);
        tombol.add(ubah);
        tombol.add(hapus);
        tombol.add(ekspor);
        tombol.add(muatUlang);

        add(ringkasan, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(tombol, BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent event) {
                HibernateUtil.tutup();
            }
        });

        muatData();
    }

    private JPanel kartu(String judul, JLabel nilai) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.add(new JLabel(judul, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(nilai, BorderLayout.CENTER);
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 8, 16, 8));
        return panel;
    }

    private void muatData() {
        try {
            dataTransaksi = repository.semua();
            model.setRowCount(0);
            for (Transaksi transaksi : dataTransaksi) {
                model.addRow(new Object[]{
                    transaksi.getId(),
                    transaksi.getJenis(),
                    transaksi.getNominal(),
                    transaksi.getTanggal(),
                    transaksi.getKategori(),
                    transaksi.getKeterangan()
                });
            }
            totalPemasukan.setText(rupiah(repository.total("PEMASUKAN")));
            totalPengeluaran.setText(rupiah(repository.total("PENGELUARAN")));
            saldo.setText(rupiah(repository.saldo()));
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat transaksi: " + e.getMessage());
        }
    }

    private void bukaForm(Transaksi transaksi, String jenisBaru) {
        boolean modeUbah = transaksi != null;
        JTextField nominal = new JTextField(modeUbah ? transaksi.getNominal().toPlainString() : "");
        JTextField tanggal = new JTextField(modeUbah ? transaksi.getTanggal().toString() : LocalDate.now().toString());
        JTextField kategori = new JTextField(modeUbah ? transaksi.getKategori() : "");
        JTextField keterangan = new JTextField(modeUbah ? transaksi.getKeterangan() : "");

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nominal"));
        form.add(nominal);
        form.add(new JLabel("Tanggal (YYYY-MM-DD)"));
        form.add(tanggal);
        form.add(new JLabel("Kategori"));
        form.add(kategori);
        form.add(new JLabel("Keterangan"));
        form.add(keterangan);

        int pilihan = JOptionPane.showConfirmDialog(
                this, form, modeUbah ? "Ubah Transaksi" : "Tambah " + jenisBaru,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (pilihan != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            BigDecimal nilai = new BigDecimal(nominal.getText().trim());
            LocalDate tanggalTransaksi = LocalDate.parse(tanggal.getText().trim());
            String namaKategori = kategori.getText().trim();
            if (nilai.compareTo(BigDecimal.ZERO) <= 0 || namaKategori.isEmpty()) {
                throw new IllegalArgumentException("Nominal harus lebih dari nol dan kategori wajib diisi.");
            }

            if (modeUbah) {
                transaksi.setNominal(nilai);
                transaksi.setTanggal(tanggalTransaksi);
                transaksi.setKategori(namaKategori);
                transaksi.setKeterangan(keterangan.getText().trim());
                repository.ubah(transaksi);
            } else {
                repository.tambah(new Transaksi(
                        jenisBaru, nilai, tanggalTransaksi, namaKategori, keterangan.getText().trim()));
            }
            muatData();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Periksa nominal, tanggal, dan kategori: " + e.getMessage());
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
        }
    }

    private void ubahPilihan() {
        int baris = table.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin diubah.");
            return;
        }
        int barisModel = table.convertRowIndexToModel(baris);
        Long id = ((Number) model.getValueAt(barisModel, 0)).longValue();
        dataTransaksi.stream().filter(data -> data.getId().equals(id)).findFirst()
                .ifPresentOrElse(data -> bukaForm(data, data.getJenis()),
                        () -> JOptionPane.showMessageDialog(this, "Transaksi tidak ditemukan."));
    }

    private void hapusPilihan() {
        int baris = table.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dihapus.");
            return;
        }
        int barisModel = table.convertRowIndexToModel(baris);
        Long id = ((Number) model.getValueAt(barisModel, 0)).longValue();
        int pilihan = JOptionPane.showConfirmDialog(
                this, "Hapus transaksi terpilih?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (pilihan == JOptionPane.YES_OPTION) {
            try {
                repository.hapus(id);
                muatData();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus transaksi: " + e.getMessage());
            }
        }
    }

    private void eksporCsv() {
        JFileChooser pemilih = new JFileChooser();
        pemilih.setDialogTitle("Simpan Laporan Transaksi");
        pemilih.setFileFilter(new FileNameExtensionFilter("CSV (*.csv)", "csv"));
        pemilih.setSelectedFile(new java.io.File("laporan-aruskas.csv"));
        if (pemilih.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.io.File file = pemilih.getSelectedFile();
        if (!file.getName().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            file = new java.io.File(file.getAbsolutePath() + ".csv");
        }

        StringBuilder csv = new StringBuilder("ID,Jenis,Nominal,Tanggal,Kategori,Keterangan\r\n");
        for (Transaksi transaksi : repository.semua()) {
            csv.append(transaksi.getId()).append(',')
                    .append(kutipCsv(transaksi.getJenis())).append(',')
                    .append(transaksi.getNominal()).append(',')
                    .append(transaksi.getTanggal()).append(',')
                    .append(kutipCsv(transaksi.getKategori())).append(',')
                    .append(kutipCsv(transaksi.getKeterangan() == null ? "" : transaksi.getKeterangan()))
                    .append("\r\n");
        }

        try {
            Files.writeString(file.toPath(), "\uFEFF" + csv, StandardCharsets.UTF_8);
            JOptionPane.showMessageDialog(this, "Laporan berhasil disimpan.");
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengekspor CSV: " + e.getMessage());
        }
    }

    private String kutipCsv(String nilai) {
        return "\"" + nilai.replace("\"", "\"\"") + "\"";
    }

    private String rupiah(BigDecimal nilai) {
        return java.text.NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).format(nilai);
    }
}
