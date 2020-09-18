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
            System.out.println("Cannot create/write to the output file.");
            System.exit(-1);
        }
    }

    private boolean compare(Integer a, Integer b) {
        if ("-a".equals(parser.sortMode)) {
            return a <= b;
        } else return a >= b;
    }

    private boolean compare(String a, String b) {
        if ("-a".equals(parser.sortMode)) {
            return b == null || a.compareTo(b) <= 0;
        } else {
            return b == null || a.compareTo(b) >= 0;
        }
    }

    private void sortInt() {
        Integer nextAddVal;
        if ("-a".equals(parser.sortMode)) {
            nextAddVal = Integer.MAX_VALUE;
        } else nextAddVal = Integer.MIN_VALUE;

        int addValIndex = 0;
        boolean filesNotEmpty = true;
        ArrayList<Integer> currentLine = new ArrayList<>(scanners.size());
        for (int i = 0; i < scanners.size(); i++) { // заполняет начальные значения
            if (scanners.get(i).hasNextLine()) {
                try {
                    currentLine.add(i, Integer.parseInt(scanners.get(i).nextLine().trim()));
                    if (compare(currentLine.get(i), nextAddVal)) {
                        nextAddVal = currentLine.get(i);
                        addValIndex = i;
                    }
                } catch (NumberFormatException e) {
                    i--;
                }
            } else { // если файл пустой - удаляет сканнер
                scanners.get(i).close();
                scanners.remove(i);
                i--;
                if (scanners.size() == 0) {
                    return;
                }
            }
        }
        try {
            writer.write(nextAddVal.toString() + '\n');
        } catch (IOException e) {
            System.out.println("Cannot write to the output file.");
        }

        while (filesNotEmpty) {
            filesNotEmpty = false;

            if (scanners.get(addValIndex).hasNextLine()) { // читает следующее значение из файла, с которого добавлял в out
                filesNotEmpty = true;
                try {
                    Integer nextLineInt = Integer.parseInt(scanners.get(addValIndex).nextLine().trim());
                    if (!compare(nextAddVal, nextLineInt)) { // игнорируем значения, которые нарушают порядок сортировки
                        continue;
                    }
                    currentLine.set(addValIndex, nextLineInt);
                } catch (NumberFormatException ignored) {
                    continue;
                }
            } else {
                scanners.get(addValIndex).close();
                scanners.remove(addValIndex);
                if (scanners.size() == 0) {
                    return;
                }
                currentLine.remove(addValIndex);
            }

            if ("-a".equals(parser.sortMode)) {
                nextAddVal = Integer.MAX_VALUE;
            } else nextAddVal = Integer.MIN_VALUE;

            for (int i = 0; i < currentLine.size(); i++) {
                if ((compare(currentLine.get(i), nextAddVal))) {
                    filesNotEmpty = true;
                    nextAddVal = currentLine.get(i);
                    addValIndex = i;
                }
            }

            try {
                if (filesNotEmpty) {
                    writer.write(nextAddVal.toString() + '\n');
                }
            } catch (IOException e) {
                System.out.println("Cannot write to the output file.");
            }
        }
    }


    private void sortStrings() {
        String nextAddVal = null;

        int addValIndex = 0;
        boolean filesNotEmpty = true;
        ArrayList<String> currentLine = new ArrayList<>(scanners.size());
        for (int i = 0; i < scanners.size(); i++) { // заполняет начальные значения
            if (scanners.get(i).hasNextLine()) {
                String nextLine = scanners.get(i).nextLine().trim();
                if (nextLine.isBlank()) {
                    i--;
                    continue;
                }
                currentLine.add(i, nextLine);
                if (compare(currentLine.get(i), nextAddVal)) {
                    nextAddVal = currentLine.get(i);
                    addValIndex = i;
                }
            } else {
                scanners.get(i).close();
                scanners.remove(i);
                i--;
                if (scanners.size() == 0) {
                    return;
                }
            }
        }
        try {
            writer.write(nextAddVal + '\n');
        } catch (IOException e) {
            System.out.println("Cannot write to the output file.");
        }

        while (filesNotEmpty) {
            filesNotEmpty = false;

            if (scanners.get(addValIndex).hasNextLine()) { // читает следующее значение из файла, с которого добавлял в out
                filesNotEmpty = true;
                String nextLine = scanners.get(addValIndex).nextLine().trim();
                if ((nextLine.isBlank()) || (!compare(nextAddVal, nextLine))) {  // игнорируем пустые строки и строки, которые нарушают порядок сортировки
                    continue;
                }
                currentLine.set(addValIndex, nextLine);
            } else {
                scanners.get(addValIndex).close();
                scanners.remove(addValIndex);
                if (scanners.size() == 0) {
                    return;
                }
                currentLine.remove(addValIndex);
            }

            nextAddVal = null;
            for (int i = 0; i < currentLine.size(); i++) {
                if ((compare(currentLine.get(i), nextAddVal))) {
                    filesNotEmpty = true;
                    nextAddVal = currentLine.get(i);
                    addValIndex = i;
                }
            }

            try {
                if (filesNotEmpty) {
                    writer.write(nextAddVal + '\n');
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
        } else {
            sortStrings();
        }

        try {
            writer.close();
            scanners.forEach(Scanner::close);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
