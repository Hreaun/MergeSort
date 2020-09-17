public class Main {
    public static void main(String[] args) {
        CLIParser parser = new CLIParser();
        parser.parse(args);

        MergeSort mergeSort = new MergeSort(parser);
        mergeSort.sort();

    }
}
