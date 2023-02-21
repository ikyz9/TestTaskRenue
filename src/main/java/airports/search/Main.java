package airports.search;

public class Main {

        public static void main(String[] args) {
        Search search = new Search();
        search.pathToFileCSV = "./airports.csv";
        search.idColumn = Integer.parseInt(args[0]);
        search.searchByColumn();
    }
}