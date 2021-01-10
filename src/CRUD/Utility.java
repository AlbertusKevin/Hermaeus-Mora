package CRUD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.Scanner;

public class Utility {
    // Retrieve: Searching -> cari data buku
    public static boolean cariDataBuku(String keywords[], boolean display) throws IOException {
        boolean isExist = false;
        boolean notFound = false;
        int i = 1;

        // buka file, simpan ke buffer
        FileReader fileRead = new FileReader("input.txt");
        BufferedReader bufferRead = new BufferedReader(fileRead);

        if(display) {
            //cetak tabel
            Show.tampilkanHeaderTabel();
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
                Show.tampilkanDataBuku(data, i);
                i++;
            }

            //pindah baris di file input
            data = bufferRead.readLine();
        }

        if (display) {
            if (i == 1) {
                System.out.printf("\t%65s\t\n", "Data buku tidak ditemukan");
            }
        }

        if (display) {
            System.out.println("============================================================================================================================================================================\n");
        }

        fileRead.close();
        bufferRead.close();

        return isExist;
    }

    public static char lanjutUser(Scanner inputTerminal, String message){
        char menu;

        System.out.print(message);
        menu = ambilPilihanUser(inputTerminal);

        while(menu != 'y' && menu != 'n'){
            System.out.print("Pilihan tidak tersedia.\nSilahkan input lagi: ");
            menu = ambilPilihanUser(inputTerminal);
        }

        return menu;
    }

    public static char ambilPilihanUser(Scanner inputTerminal){
        String input;

        input = inputTerminal.next();
        input = input.toLowerCase();
        return input.charAt(0);
    }

    public static String ambilTahun(){
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

    public static void clearScreen(){
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
