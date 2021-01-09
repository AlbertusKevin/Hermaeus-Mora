package id.vin;

import java.io.*;
import java.time.Year;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args){
        char menu;
        char pilihan = 'y';
        boolean error;
        Scanner inputTerminal = new Scanner(System.in);

        while(pilihan == 'y') {
            tampilkanMenu();
            menu = ambilPilihanUser(inputTerminal);
            error = true;
            while (error) {
                switch (menu) {
                    case '1':
                        error = false;

                        try{
                            retrieveBuku();
                        }catch (IOException ex){
                            System.err.println("\nTidak dapat membaca file daftar buku! Database belum ada.\nSilahkan tambah file terlebih dahulu di menu 3.");
                        }

                        break;
                    case '2':
                        error = false;
                        mencariBuku();
                        break;
                    case '3':
                        error = false;

                        try {
                            tambahBuku();
                            retrieveBuku();
                        }catch (IOException ex){
                            System.err.println("\nTidak dapat membaca file daftar buku! File belum ada.\nSilahkan tambah file terlebih dahulu di menu 3.");
                        }

                        break;
                    case '4':
                        System.out.println("===== Update Data Buku ====");
                        error = false;

                        break;
                    case '5':
                        error = false;

                        try{
                            hapusBuku(inputTerminal);
                        }catch (IOException ex){
                            System.err.println("\nTidak dapat membaca file daftar buku! Database belum ada.\nSilahkan tambah file terlebih dahulu di menu 3.");
                        }

                        break;
                    default:
                        System.out.println("Pilihan tidak tersedia\n");
                        tampilkanMenu();
                        menu = ambilPilihanUser(inputTerminal);
                        error = true;
                }
            }
            pilihan = lanjutUser(inputTerminal, "Lanjutkan Program (y/n)?");
        }

        System.out.println("akhir program");
        clearScreen();
    }

    // Create: Tambah data buku
    private static void tambahBuku() throws IOException {
        Scanner input = new Scanner(System.in);
        char pilihan;
        boolean confirm = true;
        boolean lanjut = true;

        FileWriter fileWriter = new FileWriter("input.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        //ambil data yang ingin dimasukkan
        String judul, penulis, penerbit, tahun;
        while (lanjut) {
            System.out.println("====================== Tambah Buku ======================");
            System.out.print("Judul: ");
            judul = input.nextLine();
            ;
            System.out.print("Penulis: ");
            penulis = input.nextLine();
            System.out.print("Penerbit: ");
            penerbit = input.nextLine();
            // validasi format tahun yang sesuai
            System.out.print("Tahun (Format: YYYY): ");
            tahun = ambilTahun();

            //Cek apakah ada buku yang sama di database
            String keywords[] = {judul + "," + penulis + "," + penerbit + "," + tahun};

            //jika tidak ada buku yang sama
            if (!cariDataBuku(keywords, false)) {
                String primaryKey = judul.toLowerCase().replaceAll(" ", "_");
                primaryKey = primaryKey + "_" + penulis.toLowerCase().replaceAll(" ", "_");
                primaryKey = primaryKey + "_" + penerbit.toLowerCase().replaceAll(" ", "_");
                primaryKey = primaryKey + "_" + tahun;

                System.out.println("----------------------------------------------------------------------------------");
                System.out.printf("%25sData buku yang akan Anda input:\n", " ");
                System.out.println("----------------------------------------------------------------------------------");
                System.out.printf("%-20s: %s\n", "Primary Key", primaryKey);
                System.out.printf("%-20s: %s\n", "Judul Buku", judul);
                System.out.printf("%-20s: %s\n", "Penulis", penulis);
                System.out.printf("%-20s: %s\n", "Penerbit", penerbit);
                System.out.printf("%-20s: %s\n", "Tahun", tahun);
                System.out.println("----------------------------------------------------------------------------------");
                pilihan = lanjutUser(new Scanner(System.in), "Lanjutkan proses input (y/n)?");

                if (pilihan == 'y') {
                    bufferedWriter.write(primaryKey+","+judul+","+penulis+","+penerbit+","+tahun);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else {
                    System.out.println("Proses tambah buku dibatalkan!");
                }

            } else {
                System.out.println("Data buku yang ingin dimasukkan sudah ada di database dengan data berikut");
            }

            pilihan = lanjutUser(new Scanner(System.in), "Ingin menambahkan data lagi (y/n)?");

            if(pilihan == 'n'){
                lanjut = false;
            }
        }

        fileWriter.close();
        bufferedWriter.close();
    }

    // Retrieve: List Data Buku
    private static void retrieveBuku() throws IOException {
        int i = 1;
        FileReader fileRead = new FileReader("input.txt");
        BufferedReader bufferRead = new BufferedReader(fileRead);

        String data = bufferRead.readLine();
        tampilkanHeaderTabel();
        while(data != null){
            tampilkanDataBuku(data,i);
            i++;
            data = bufferRead.readLine();
        }

        System.out.println("============================================================================================================================================================================");
        fileRead.close();
        bufferRead.close();
    }

    // Retrieve: Searching -> Ambil keyword dari user
    private static void mencariBuku(){
        // ambil semua keyword yang diinput user
        System.out.print("Masukkan keyword: ");
        Scanner input = new Scanner(System.in);
        String kata = input.nextLine();

        // pecah menjadi array, nanti tiap indeks akan dicek dengan daftar per baris
        String keywords[] = kata.split("\\s+");

        // cek keyword apakah ada di dalam data
        try{
            cariDataBuku(keywords, true);
        }catch (IOException ex){
            System.err.println("\nTidak dapat membaca file daftar buku! Database tidak ditemukan!");
        }
    }

    // Retrieve: Searching -> cari data buku
    private static boolean cariDataBuku(String keywords[], boolean display) throws IOException {
        boolean isExist;
        boolean notFound = false;
        int i = 1;

        // buka file, simpan ke buffer
        FileReader fileRead = new FileReader("input.txt");
        BufferedReader bufferRead = new BufferedReader(fileRead);

        if(display) {
            //cetak tabel
            tampilkanHeaderTabel();
        }

        // ambil data per baris
        String data = bufferRead.readLine();

        // lakukan pengecekan data hingga habis
        while (data != null){
            // cek per baris dengan setiap keyword
            isExist = true;
            for(String keyword : keywords){
                // nilai boolean, kedua keyword harus ada di data
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
                notFound = isExist || notFound;
            }

            //tampilkan data yang sesuai
            if(isExist){
                tampilkanDataBuku(data, i);
                i++;
            }

            //pindah baris
            data = bufferRead.readLine();
        }

        if (display) {
            if (!notFound) {
                System.out.printf("\t%65s\t\n", "Data buku tidak ditemukan");
            }
        }

        if (display) {
            System.out.println("============================================================================================================================================================================\n");
        }

        fileRead.close();
        bufferRead.close();

        return notFound;
    }

    public static void hapusBuku(Scanner inputTerminal) throws IOException{
        char pilihan = 'y';
        // buka file database original
        FileReader fileReader = new FileReader("input.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // siapkan file temp
        FileWriter fileWriter = new FileWriter("temp.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        while(pilihan != 'n') {
            //tampilkan daftar buku
            retrieveBuku();

            //ambil keyword yang mau didelete
            System.out.println("Pilih buku yang ingin di delete (nomor)");
            int keyword = inputTerminal.nextInt();


            pilihan = lanjutUser(inputTerminal, "Ingin menghapus data nomor " + keyword + "?");

            if (pilihan != 'n') {
                //copy ke database temp yang tidak di delete
                int i = 1;
                String data = bufferedReader.readLine();

                boolean numFound = false;
                while (data != null) {
                    if (i != keyword) {
                        bufferedWriter.write(data);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }

                    if(i == keyword){
                        numFound = true;
                    }

                    i++;
                    data = bufferedReader.readLine();
                }

                fileReader.close();
                fileWriter.close();
                bufferedReader.close();
                bufferedWriter.close();

                if (!numFound) {
                    System.out.println("Data tidak ditemukan. Tidak dapat menghapus file!");
                } else {
                    // buka file temp
                    FileReader tempFile = new FileReader("temp.txt");
                    BufferedReader bufferedTemp = new BufferedReader(tempFile);

                    // siapkan writer untuk menimpa db original dengan hasil perubahan
                    FileWriter dbWrite = new FileWriter("input.txt");
                    BufferedWriter bufferDB = new BufferedWriter(dbWrite);

                    String updateData = bufferedTemp.readLine();
                    while (updateData != null) {
                        bufferDB.write(updateData);
                        bufferDB.newLine();
                        bufferDB.flush();

                        updateData = bufferedTemp.readLine();
                    }

                    System.out.println("Data berhasil dihapus!");
                    bufferDB.close();
                    bufferedTemp.close();
                    dbWrite.close();
                    tempFile.close();
                }

            }

            pilihan = lanjutUser(inputTerminal, "Ingin menghapus file lainnya (y/n)?");
        }
    }

    private static void tampilkanMenu(){
        System.out.println("======== Menu Database Perpustakaan ========");
        System.out.println("1. Tampilkan Data Buku");
        System.out.println("2. Cari Data Buku");
        System.out.println("3. Tambah Data Buku");
        System.out.println("4. Update Data Buku");
        System.out.println("5. Hapus Data Buku");
        System.out.println("=============================================");
        System.out.print("Pilih Menu: ");
    }

    private static void tampilkanHeaderTabel(){
        System.out.println("=========================================================================== Cari Data Buku =================================================================================");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("|\t%2s\t|\t%-60s\t|\t%-20s\t|\t%-20s\t|\t%-20s\t|\tTAHUN\n","NO","KEY","JUDUL BUKU","PENULIS","PENERBIT");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void tampilkanDataBuku(String data, int i){
        StringTokenizer buku = new StringTokenizer(data, ",");
        System.out.printf("|\t%2d\t", i);
        System.out.printf("|\t%-60s\t",buku.nextToken());
        System.out.printf("|\t%-20s\t", buku.nextToken());
        System.out.printf("|\t%-20s\t",buku.nextToken());
        System.out.printf("|\t%-20s\t",buku.nextToken());
        System.out.printf("|\t%s\n",buku.nextToken());
    }

    private static char lanjutUser(Scanner inputTerminal, String message){
        char menu;

        System.out.print(message);
        menu = ambilPilihanUser(inputTerminal);

        while(menu != 'y' && menu != 'n'){
            System.out.print("Pilihan tidak tersedia.\nSilahkan input lagi: ");
            menu = ambilPilihanUser(inputTerminal);
        }

        return menu;
    }

    private static char ambilPilihanUser(Scanner inputTerminal){
        String input;

        input = inputTerminal.next();
        input = input.toLowerCase();
        return input.charAt(0);
    }

    private static String ambilTahun(){
        boolean tahunValid = false;
        Scanner input = new Scanner(System.in);
        String tahun = input.nextLine();
        while(!tahunValid){
            try{
                Year.parse(tahun);
                tahunValid = true;
            }catch (Exception e){
                System.err.print("Tahun tidak valid, silahkan input tahun lagi (Format: YYYY):");
                tahun = input.nextLine();
            }
        }
        return tahun;
    }

    private static void clearScreen(){
        try{
            if(System.getProperty("os.name").contains("Windows")){ //Jika osnya windows
                // untuk mengakses cmd dari windows dan berikan command cls untuk clear screen,
                // inheritIO ambil dari console IDE,
                // lalu jalankan perintah cls, tunggu hingga selesai
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            }else{ //Jika bukan windows
                System.out.print("\033\143");
            }
        }catch (Exception ex){
            System.out.println("Tidak bisa clear screen!");
        }
    }
}
