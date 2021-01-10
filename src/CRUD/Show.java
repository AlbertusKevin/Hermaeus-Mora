package CRUD;

import java.util.StringTokenizer;

public class Show {
    public static void tampilkanMenu(){
        System.out.println("======== Menu Database Perpustakaan ========");
        System.out.println("1. Tampilkan Data Buku");
        System.out.println("2. Cari Data Buku");
        System.out.println("3. Tambah Data Buku");
        System.out.println("4. Update Data Buku");
        System.out.println("5. Hapus Data Buku");
        System.out.println("=============================================");
        System.out.print("Pilih Menu: ");
    }

    public static void tampilkanHeaderTabel(){
        System.out.println("========================================================================== Daftar Data Buku ================================================================================");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("|\t%2s\t|\t%-60s\t|\t%-20s\t|\t%-20s\t|\t%-20s\t|\tTAHUN\n","NO","KEY","JUDUL BUKU","PENULIS","PENERBIT");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public static void tampilkanDataBuku(String data, int i){
        StringTokenizer buku = new StringTokenizer(data, ",");
        System.out.printf("|\t%2d\t", i);
        System.out.printf("|\t%-60s\t",buku.nextToken());
        System.out.printf("|\t%-20s\t", buku.nextToken());
        System.out.printf("|\t%-20s\t",buku.nextToken());
        System.out.printf("|\t%-20s\t",buku.nextToken());
        System.out.printf("|\t%s\n",buku.nextToken());
    }
}
