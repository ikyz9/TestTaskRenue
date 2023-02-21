package airports.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Search {

    public String pathToFileCSV;
    public int idColumn;


    public void searchByColumn(){
        HashMap<String, List<Integer>> searchMap = handlingFile();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите строку:");
        String word = scanner.nextLine();

        while (!word.equals("!quit")){
            long start = System.currentTimeMillis();
            printAllByString(word, searchMap);
            System.out.printf("затраченное время на поиск: %d мс\n\n", System.currentTimeMillis() - start);
            System.out.println("Введите строку:");
            word = scanner.nextLine();
        }
    }

    private HashMap<String, List<Integer>> handlingFile(){
        HashMap<String, List<Integer>> searchMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFileCSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String tmpChar = extractStringColumn(line).substring(0,1).toLowerCase();
                Integer tmpId = extractId(line);
                if (searchMap.containsKey(tmpChar)){
                    List<Integer> tmpList = searchMap.get(tmpChar);
                    tmpList.add(tmpId);
                } else {
                    List<Integer> tmpList = new ArrayList<>();
                    tmpList.add(tmpId);
                    searchMap.put(tmpChar,tmpList);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return searchMap;
    }

    private String extractStringColumn(String line){
        int columnIndex = 0;
        for(int i = 1; i < idColumn; i++){
            columnIndex = line.indexOf(',',columnIndex)+1;
        }
        String tmpString = line.substring(columnIndex);
        return tmpString.substring(0,tmpString.indexOf(',')).replace("\"", "");
    }

    private Integer extractId(String line) {
        return Integer.parseInt(line.substring(0,line.indexOf(',')));
    }

    private void printAllByString(String _char, HashMap<String, List<Integer>> searchData){
        int countFindString = 0;

        if(!searchData.containsKey(_char.toLowerCase().substring(0, 1))){
            System.out.printf("Количество найденных строк: %d, ", countFindString);
            return;
        }

        List<Integer> listIndex = searchData.get(_char.toLowerCase().substring(0, 1));
        List<String> tmpValue = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFileCSV))) {

            for (Integer index :listIndex) {
                String data = reader.lines().filter(i -> i.startsWith(index.toString())).findFirst().get();
                String word = extractStringColumn(data);
                String tmpWord = word.toLowerCase();

                if (tmpWord.startsWith(_char.toLowerCase())) {
                    tmpValue.add("\"" + word + "\"" + "[" +  data + "]");
                    countFindString++;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        tmpValue.stream().sorted((i1, i2)-> i1.substring(1, i1.indexOf("\"", 2)).compareToIgnoreCase(i2.substring(1, i2.indexOf("\"", 2)))).forEach(System.out::println);
        System.out.printf("Количество найденных строк: %d, ", countFindString);
    }
}
