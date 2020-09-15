import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MergeSort {
    CLIParser parser;
    FileWriter writer;
    ArrayList<Scanner> scanners = new ArrayList<>();

    public MergeSort(CLIParser parser) {
        this.parser = parser;
    }

    private void makeIO() {
        for (int i = 0; i < parser.inputFilenames.size(); i++) {
            try {
                scanners.add(new Scanner(new File(parser.inputFilenames.get(i))));
            } catch (FileNotFoundException e) {
                System.out.println("File with name " + parser.inputFilenames.get(i) + " not found.");
            }
        }

        try {
            File outFile = new File(parser.outputFilename);
            outFile.createNewFile();
            writer = new FileWriter(outFile);
        } catch (IOException e) {
            System.err.println("Cannot create/write to the output file.");
            System.exit(-1);
        }
    }


    private void findMinInt() {
        Integer min = Integer.MAX_VALUE;
        int minIndex = 0;
        boolean filesNotEmpty = true;
        ArrayList<Integer> currentLine = new ArrayList<>(scanners.size());
        for (int i = 0; i < scanners.size(); i++) {
            if (scanners.get(i).hasNextLine()) {
                try {
                    currentLine.add(i, Integer.parseInt(scanners.get(i).nextLine()));
                    if (currentLine.get(i) <= min) {
                        min = currentLine.get(i);
                        minIndex = i;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        try {
            writer.write(min.toString() + '\n');
        } catch (IOException e) {
            System.out.println("Cannot write to the output file.");
        }

        while (filesNotEmpty) {
            filesNotEmpty = false;
            min = Integer.MAX_VALUE;
            if (scanners.get(minIndex).hasNextLine()) {
                try {
                    currentLine.set(minIndex, Integer.parseInt(scanners.get(minIndex).nextLine()));
                } catch (NumberFormatException ignored) {
                }
            } else {
                currentLine.set(minIndex, null);
            }

            for (int i = 0; i < currentLine.size(); i++) {
                if ((currentLine.get(i) != null) && (currentLine.get(i) <= min)) {
                    filesNotEmpty = true;
                    min = currentLine.get(i);
                    minIndex = i;
                }
            }

            try {
                if (filesNotEmpty) {
                    writer.write(min.toString() + '\n');
                }
            } catch (IOException e) {
                System.out.println("Cannot write to the output file.");
            }
        }
    }


    public void sort() {
        makeIO();

        findMinInt();

        try {
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
