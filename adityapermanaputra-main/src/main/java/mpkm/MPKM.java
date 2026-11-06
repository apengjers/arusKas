/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mpkm;
public class MPKM {
    static class Pelanggan {
        private int id;
        private String nama;
        private String alamat;
        private String telp;
        
        public Pelanggan(int id, String nama, String alamat, String telp){
            this.id = id;
            this.nama = nama;
            this.alamat = alamat;
            this.telp = telp;
        }
        
        public String getNama(){
            return nama;
        }
        
        public void tampilkanData(){
            System.out.println("\n=== DATA PELANGGAN ===");
            System.out.println("ïd           : " + id);
            System.out.println("nama         : " + nama);
            System.out.println("alamat       : " + alamat);
            System.out.println("telp         : " + telp);
        }
    }
    
    static class Layanan {
        private int id;
        private String namaLayanan;
        private double harga;
        
        public Layanan(int id, String namaLayanan, double harga){
            this.id = id;
            this.namaLayanan = namaLayanan;
            this.harga = harga;
        }
        
        public String getNamaLayanan(){
            return namaLayanan;
        }
        
        public double getHarga(){
            return harga;
        }
        
        public void tampilkanLayanan(){
            System.out.println("\n=== Data Layanan ==");
            System.out.println("ïd           : " + id);
            System.out.println("layanan      : " + namaLayanan);
            System.out.println("harga        : " + harga);
        }
    }
    
    static class Pesanan{
        private int idPesanan;
        private Pelanggan pelanggan;
        private Layanan layanan;
        private String deskripsi;
        
        public Pesanan(int idPesanan, Pelanggan pelanggan, Layanan layanan, String deskripsi){
            this.idPesanan = idPesanan;
            this.pelanggan = pelanggan;
            this.layanan = layanan;
            this.deskripsi = deskripsi;
        }
        
        public double hitungTotal(){
            return layanan.getHarga();
        }
        
        public void tampilkanPesanan(){
            System.out.println("\n=== Data Pesanan ==");
            System.out.println("ïd pesanan         : " + idPesanan);
            System.out.println("pelanggan          : " + pelanggan.getNama());
            System.out.println("layanan            : " + layanan.getNamaLayanan());     
            System.out.println("deskripsi          : " + deskripsi);
            System.out.println("total              : Rp" + hitungTotal());            
        }
    }
    
    static class Pembayaran{
            private int idPembayaran;
            private double jumlahBayar;
            
            public Pembayaran(int idPembayaran, double jumlahBayar){
                this.idPembayaran = idPembayaran;
                this.jumlahBayar = jumlahBayar;
            }
            
            public void tampilkanPembayaran(){
                System.out.println("\n=== Data Pembayaran ==");
                System.out.println("ïd pembayaran         : " + idPembayaran);
                System.out.println("jumlah bayar          : Rp" + jumlahBayar);
            }
    }
    
    public static void main(String[] args){
        Pelanggan pelanggan = new Pelanggan(1, "Ahmad", "Bekasi", "081234567890");
        
        Layanan layanan = new Layanan(1, "Jasa las", 50000);
        
        Pesanan pesanan = new Pesanan(1, pelanggan, layanan, "Pembuatan Pagar");
        
        Pembayaran pembayaran = new Pembayaran(1, pesanan.hitungTotal());
        
        pelanggan.tampilkanData();
        layanan.tampilkanLayanan();
        pesanan.tampilkanPesanan();
        pembayaran.tampilkanPembayaran();
    }
}