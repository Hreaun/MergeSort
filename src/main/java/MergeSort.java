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

    private boolean compare(Integer a, Integer b) {
        if ("-a".equals(parser.sortMode)) {
            return a <= b;
        } else return a >= b;
    }

    private void sortInt() {
        Integer nextPushVal;
        if ("-a".equals(parser.sortMode)) {
            nextPushVal = Integer.MAX_VALUE;
        } else nextPushVal = Integer.MIN_VALUE;

        int pushValIndex = 0;
        boolean filesNotEmpty = true;
        ArrayList<Integer> currentLine = new ArrayList<>(scanners.size());
        for (int i = 0; i < scanners.size(); i++) {
            if (scanners.get(i).hasNextLine()) {
                try {
                    currentLine.add(i, Integer.parseInt(scanners.get(i).nextLine()));
                    if (compare(currentLine.get(i), nextPushVal)) {
                        nextPushVal = currentLine.get(i);
                        pushValIndex = i;
                    }
                } catch (NumberFormatException e) {
                    i--;
                }
            } else {
                scanners.remove(i);
                currentLine.remove(i);
            }
        }
        try {
            writer.write(nextPushVal.toString() + '\n');
        } catch (IOException e) {
            System.out.println("Cannot write to the output file.");
        }

        while (filesNotEmpty) {
            filesNotEmpty = false;

            if (scanners.get(pushValIndex).hasNextLine()) {
                filesNotEmpty = true;
                try {
                    Integer nextLineInt = Integer.parseInt(scanners.get(pushValIndex).nextLine());
                    if (compare(nextLineInt, nextPushVal)) {
                        continue;
                    }
                    currentLine.set(pushValIndex, nextLineInt);
                } catch (NumberFormatException ignored) {
                    continue;
                }
            } else {
                scanners.remove(pushValIndex);
                currentLine.remove(pushValIndex);
            }

            if ("-a".equals(parser.sortMode)) {
                nextPushVal = Integer.MAX_VALUE;
            } else nextPushVal = Integer.MIN_VALUE;

            for (int i = 0; i < currentLine.size(); i++) {
                if ((compare(currentLine.get(i), nextPushVal))) {
                    filesNotEmpty = true;
                    nextPushVal = currentLine.get(i);
                    pushValIndex = i;
                }
            }

            try {
                if (filesNotEmpty) {
                    writer.write(nextPushVal.toString() + '\n');
                }
            } catch (IOException e) {
                System.out.println("Cannot write to the output file.");
            }
        }
    }


    public void sort() {
        makeIO();

        if ("-i".equals(parser.dataType)) {
            sortInt();
        }

        try {
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
