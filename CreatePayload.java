import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CreatePayload {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter transaction base path - ");
        String basePath = sc.next();

        System.out.print("Enter transaction id - ");
        String baseTransactionId = sc.next();

        System.out.print("Enter start number - ");
        int startId = sc.nextInt();

        System.out.print("Enter max number - ");
        int maxId = sc.nextInt();

//        String basePath = "E:\\Testing\\";
//        String baseTransactionId = "S111110000ABC";
//        int startId = 1;
//        int maxId = 98;

        String firstTransactionId = String.format("%s%02d", baseTransactionId, startId);

        try {
            Path fromFolder = Paths.get(basePath, firstTransactionId);

            for (int i = startId + 1; i <= maxId; i++) {
                String newTransactionId = String.format("%s%02d", baseTransactionId, i);
                Path toFolder = Paths.get(basePath, newTransactionId);

                // Copy Folder
                String command = String.format("Xcopy %s %s /E/I", fromFolder, toFolder);
                System.out.println("Executing command: command: " + command);
                java.lang.Runtime rt = java.lang.Runtime.getRuntime();
                Process pr = rt.exec(command);
                pr.waitFor();

                // Replace transaction id in parameters file
                Path paramFile = Paths.get(basePath, String.format("%s/optimization/optimize-parameters.txt", newTransactionId));

                StringBuilder oldContent = new StringBuilder();
                BufferedReader reader;
                FileWriter writer;
                try
                {
                    reader = new BufferedReader(new FileReader(paramFile.toFile()));
                    String line = reader.readLine();

                    while (line != null) {
                        oldContent.append(line).append(System.lineSeparator());
                        line = reader.readLine();
                    }

                    reader.close();

                    //Replacing oldString with newString in the oldContent
                    String newContent = oldContent.toString().replaceAll(firstTransactionId, newTransactionId);

                    //Rewriting the input text file with newContent
                    writer = new FileWriter(paramFile.toFile());
                    writer.write(newContent);
                    writer.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
