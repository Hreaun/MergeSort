public class Main {
    public static void main(String[] args) {
        CLIParser parser = new CLIParser();
        parser.parse(args);

        System.out.println(parser.sortMode);
        System.out.println(parser.dataType);
        System.out.println("out " + parser.outputFilename);
        parser.inputFilenames.forEach(System.out::println);


    }
}
