package com.search;

import com.search.exceptions.FileParserException;
import com.search.file.FileParser;
import com.search.filter.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws FileParserException {
        File file = new File(args[0]);
        FileParser fileParser = new FileParser(file, args);

        Scanner sc = new Scanner(System.in);
        System.out.print("Введите строку: ");
        String searchStr = sc.nextLine();
        while (!searchStr.equals("!quit")) {

            long time = System.currentTimeMillis();
            List<String> data = fileParser.getIndexMap().keySet().stream().sorted().collect(Collectors.toList());
            ArrayList<String> results = Filter.search(data, searchStr.toLowerCase());
            time = System.currentTimeMillis() - time;

            fileParser.printResult(results, time);
            System.out.print("Введите строку: ");
            searchStr = sc.nextLine();
        }
    }
}
