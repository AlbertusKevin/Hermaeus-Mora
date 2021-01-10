package id.vin;

import java.io.IOException;
import java.util.Scanner;
import CRUD.*;

public class Main {
    public static void main(String[] args){
        char menu;
        char pilihan = 'y';
        boolean error;

        while(pilihan == 'y') {
            Show.tampilkanMenu();
            menu = CRUD.Utility.ambilPilihanUser(new Scanner(System.in));
            error = true;
            while (error) {
                switch (menu) {
                    case '1':
                        error = false;

                        try{
                            OperasiCRUD.retrieveBuku();
                        }catch (IOException ex){
                            System.err.println("\nTidak dapat membaca file daftar buku! Database belum ada.\nSilahkan tambah file terlebih dahulu di menu 3.");
                        }

                        break;
                    case '2':
                        error = false;
                        OperasiCRUD.mencariBuku();
                        break;
                    case '3':
                        error = false;

                        try {
                            OperasiCRUD.tambahBuku();
                            OperasiCRUD.retrieveBuku();
                        }catch (IOException ex){
                            System.err.println("\nTidak dapat membaca file daftar buku! File belum ada.\nSilahkan tambah file terlebih dahulu di menu 3.");
                        }

                        break;
                    case '4':
                        error = false;
                        try{
                            OperasiCRUD.updateData();
                        }catch (IOException ex){
                            System.err.println("\nTidak dapat membaca file daftar buku! Database belum ada.\nSilahkan tambah file terlebih dahulu di menu 3.");
                        }
                        break;
                    case '5':
                        error = false;

                        try{
                            OperasiCRUD.hapusBuku();
                        }catch (IOException ex){
                            System.err.println("\nTidak dapat membaca file daftar buku! Database belum ada.\nSilahkan tambah file terlebih dahulu di menu 3.");
                        }

                        break;
                    default:
                        System.out.println("Pilihan tidak tersedia\n");
                        Show.tampilkanMenu();
                        menu = Utility.ambilPilihanUser(new Scanner(System.in));
                        error = true;
                }
            }
            pilihan = Utility.lanjutUser(new Scanner(System.in), "Lanjutkan Program (y/n)?");
        }

        System.out.println("akhir program");
        Utility.clearScreen();
    }

}
