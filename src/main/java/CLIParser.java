import java.util.ArrayList;

public class CLIParser {
    String sortMode = "-a";
    String dataType = null;
    String outputFilename = null;
    ArrayList<String> inputFilenames = new ArrayList<>();

    public void parse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ((i == 0) && ("-d".equals(args[i]))) {
                sortMode = "-d";
                continue;
            }
            if (((i == 0) || (i == 1)) && ("-s".equals(args[i]) || "-i".equals(args[i]))) {
                dataType = args[i];
                continue;
            }
            if (((i == 1) || (i == 2)) && (outputFilename == null)) {
                outputFilename = args[i];
                continue;
            }
            if (i >= 1) {
                if (!inputFilenames.contains(args[i])) {
                    inputFilenames.add(args[i]);
                }
            }
        }

        if (dataType == null) {
            System.err.println("You must specify datatype [-s | -i].");
            System.exit(-1);
        }

        if (outputFilename == null) {
            System.err.println("You must specify output filename.");
            System.exit(-1);
        }

        if (inputFilenames.size() == 0) {
            System.err.println("You must specify input filenames.");
            System.exit(-1);
        }
    }
}
