package CRUD;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class OperasiCRUD {
    // Create: Tambah data buku
    public static void tambahBuku() throws IOException {
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
            System.out.print("Penulis: ");
            penulis = input.nextLine();
            System.out.print("Penerbit: ");
            penerbit = input.nextLine();
            // validasi format tahun yang sesuai
            System.out.print("Tahun (Format: YYYY): ");
            tahun = Utility.ambilTahun();

            //Cek apakah ada buku yang sama di database
            String keywords[] = {judul + "," + penulis + "," + penerbit + "," + tahun};

            //jika tidak ada buku yang sama
            if (!Utility.cariDataBuku(keywords, false)) {
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
                pilihan = Utility.lanjutUser(new Scanner(System.in), "Lanjutkan proses input (y/n)?");

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

            pilihan = Utility.lanjutUser(new Scanner(System.in), "Ingin menambahkan data lagi (y/n)?");

            if(pilihan == 'n'){
                lanjut = false;
            }
        }

        fileWriter.close();
        bufferedWriter.close();
    }

    // Retrieve: List Data Buku
    public static void retrieveBuku() throws IOException {
        int i = 1;
        FileReader fileRead = new FileReader("input.txt");
        BufferedReader bufferRead = new BufferedReader(fileRead);

        String data = bufferRead.readLine();
        Show.tampilkanHeaderTabel();
        while(data != null){
            Show.tampilkanDataBuku(data,i);
            i++;
            data = bufferRead.readLine();
        }

        System.out.println("============================================================================================================================================================================");
        fileRead.close();
        bufferRead.close();
    }

    // Retrieve: Searching -> Ambil keyword dari user
    public static void mencariBuku(){
        // ambil semua keyword yang diinput user
        System.out.print("Masukkan keyword: ");
        Scanner input = new Scanner(System.in);
        String kata = input.nextLine();

        // pecah menjadi array, nanti tiap indeks akan dicek dengan daftar per baris
        String keywords[] = kata.split("\\s+");

        // cek keyword apakah ada di dalam data
        try{
            Utility.cariDataBuku(keywords, true);
        }catch (IOException ex){
            System.err.println("\nTidak dapat membaca file daftar buku! Database tidak ditemukan!");
        }
    }

    // Update
    public static void updateData() throws IOException{
        char pilihan = 'y';

        while(pilihan != 'n') {
            // buka file database original
            FileReader fileReader = new FileReader("input.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // siapkan file temp
            FileWriter fileWriter = new FileWriter("temp.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //tampilkan daftar buku
            retrieveBuku();

            //ambil keyword yang mau diupdate
            System.out.println("Pilih buku yang ingin di update (nomor)");
            Scanner inputTerminal = new Scanner(System.in);
            int keyword = inputTerminal.nextInt();

            pilihan = Utility.lanjutUser(inputTerminal, "Ingin mengupdate data nomor " + keyword + "? ");

            if (pilihan != 'n') {
                int i = 1;
                String data = bufferedReader.readLine();
                boolean numFound = false;
                while (data != null) {
                    StringTokenizer token = new StringTokenizer(data, ",");

                    if (i == keyword) {
                        numFound = true;
                        System.out.println("----------------------------------------------------------------------------------");
                        System.out.printf("%25sData buku yang akan Anda ubah:\n", " ");
                        System.out.println("----------------------------------------------------------------------------------");
                        System.out.printf("%-20s: %s\n", "Primary Key", token.nextToken());
                        System.out.printf("%-20s: %s\n", "Judul Buku", token.nextToken());
                        System.out.printf("%-20s: %s\n", "Penulis", token.nextToken());
                        System.out.printf("%-20s: %s\n", "Penerbit", token.nextToken());
                        System.out.printf("%-20s: %s\n", "Tahun", token.nextToken());
                        System.out.println("----------------------------------------------------------------------------------");

                        String fieldData[] = {"judul", "penulis", "penerbit", "tahun"};
                        String temp[] = new String[4];
                        String originalData;
                        token = new StringTokenizer(data, ",");
                        token.nextToken();

                        for (int index = 0; index < fieldData.length; index++) {
                            pilihan = Utility.lanjutUser(inputTerminal, "Apakah Anda ingin mengubah " + fieldData[index] + " (y/n)? ");
                            originalData = token.nextToken();

                            if (pilihan == 'y') {
                                inputTerminal = new Scanner(System.in);
                                System.out.print("Masukkan " + fieldData[index] + " baru: ");
                                if(fieldData[index].equals("tahun")){
                                    temp[index] = Utility.ambilTahun();
                                }else {
                                    temp[index] = inputTerminal.nextLine();
                                }
                            } else {
                                temp[index] = originalData;
                            }
                        }

                        token = new StringTokenizer(data, ",");
                        token.nextToken();

                        System.out.println("----------------------------------------------------------------------------------------");
                        System.out.printf("%30sPerubahan data buku yang baru:\n", " ");
                        System.out.println("----------------------------------------------------------------------------------------");
                        System.out.printf("%-12s: %-30s ---> %s\n", "Judul Buku", token.nextToken(), temp[0]);
                        System.out.printf("%-12s: %-30s ---> %s\n", "Penulis", token.nextToken(), temp[1]);
                        System.out.printf("%-12s: %-30s ---> %s\n", "Penerbit", token.nextToken(), temp[2]);
                        System.out.printf("%-12s: %-30s ---> %s\n", "Tahun", token.nextToken(), temp[3]);
                        System.out.println("----------------------------------------------------------------------------------------");

                        pilihan = Utility.lanjutUser(inputTerminal, "Ingin mengupdate data tersebut? ");

                        if (pilihan != 'n') {
                            boolean isExist = Utility.cariDataBuku(temp, false);

                            if (isExist) {
                                System.err.println("Data buku sudah ada. Disarankan untuk menghapus data buku yang bersangkutan");
                            } else {
                                // format data yang diupdate ke database
                                // Buat primary key
                                String primaryKey = "";
                                for (int index = 0; index < temp.length; index++) {
                                    if (index == temp.length - 1) {
                                        primaryKey += temp[index].toLowerCase().replaceAll(" ", "_");
                                    } else {
                                        primaryKey += temp[index].toLowerCase().replaceAll(" ", "_") + "_";
                                    }
                                }

                                //tulis ke database
                                bufferedWriter.write(primaryKey + "," + temp[0] + "," + temp[1] + "," + temp[2] + "," + temp[3]);
                            }
                            System.out.println("Data buku berhasil diupdate!");
                        }
                    } else {
                        bufferedWriter.write(data);
                    }

                    i++;
                    bufferedWriter.newLine();
                    data = bufferedReader.readLine();
                }

                bufferedWriter.flush();
                fileReader.close();
                fileWriter.close();
                bufferedReader.close();
                bufferedWriter.close();

                if (!numFound) {
                    System.out.println("Data tidak ditemukan. Tidak dapat mengubah data!");
                } else {
                    // buka file temp
                    FileReader tempWriter = new FileReader("temp.txt");
                    BufferedReader tempBuffered = new BufferedReader(tempWriter);

                    // siapkan writer untuk menimpa db original dengan hasil perubahan
                    FileWriter dbWriter = new FileWriter("input.txt");
                    BufferedWriter dbBuffered = new BufferedWriter(dbWriter);

                    // Timpa file asli dengan hasil perubahan
                    String updateData = tempBuffered.readLine();
                    while (updateData != null) {
                        dbBuffered.write(updateData);
                        dbBuffered.newLine();
                        dbBuffered.flush();

                        updateData = tempBuffered.readLine();
                    }

                    //close file
                    dbBuffered.close();
                    tempBuffered.close();
                    dbWriter.close();
                    tempWriter.close();
                }

            }

            pilihan = Utility.lanjutUser(new Scanner(System.in), "Ingin update data lainnya (y/n)?");
        }
    }

    // Delete
    public static void hapusBuku() throws IOException{
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
            System.out.print("Pilih buku yang ingin di delete (nomor): ");
            Scanner inputTerminal = new Scanner(System.in);
            int keyword = inputTerminal.nextInt();

            pilihan = Utility.lanjutUser(new Scanner(System.in), "Ingin menghapus data nomor " + keyword + "? ");

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
                    System.out.println("Data tidak ditemukan. Tidak dapat menghapus file!\n");
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

            pilihan = Utility.lanjutUser(new Scanner(System.in), "Ingin menghapus file lainnya (y/n)?");
        }
    }
}
