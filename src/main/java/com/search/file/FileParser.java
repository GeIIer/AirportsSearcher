package com.search.file;

import com.search.exceptions.FileParserException;
import com.search.exceptions.InputParameterException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileParser {
    private File file;

    private Map<String, ArrayList<Long>> indexMap;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Map<String, ArrayList<Long>> getIndexMap() {
        return indexMap;
    }

    public void setIndexMap(int column) throws FileParserException {
        this.indexMap = generateIndexMap(column);
    }

    public FileParser(File file) {
        this.file = file;
        this.indexMap = new HashMap<>();
    }

    public FileParser(File file, String[] args) throws FileParserException, InputParameterException {
        if (args.length < 2) {
            throw new InputParameterException("Неверное количество параметров");
        }
        int column;
        try {
            column = Integer.parseInt(args[1]);
        } catch (NumberFormatException numberFormatException) {
            throw new InputParameterException("Параметр должен быть предоставлен целочисленным значением");
        }
        if (column < 1) {
            throw new InputParameterException("Параметр должен быть положительным");
        }
        this.file = file;
        this.indexMap = generateIndexMap(column - 1);
    }

    private Map<String, ArrayList<Long>> generateIndexMap(int columnIndex) throws FileParserException {
        HashMap<String, ArrayList<Long>> indexMap = new HashMap<>();
        String record = null;
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "r")) {
            long index = accessFile.getFilePointer();
            while ((record = accessFile.readLine()) != null) {
                var arrayList = indexMap.get(getDataInRecord(record, columnIndex));
                if (arrayList == null) arrayList = new ArrayList<>();
                arrayList.add(index);
                indexMap.put(getDataInRecord(record, columnIndex), arrayList);
                index = accessFile.getFilePointer();
            }
        } catch (Exception ex) {
            throw new FileParserException(ex.getMessage());
        }
        return indexMap;
    }

    private String getDataInRecord(String record, int columnIndex) {
        Object[] cells = record.split(",");
        String cell = (String) cells[columnIndex];
        if (cell.charAt(0) == '"') cell = cell.substring(1, cell.length() - 1);
        return cell;
    }

    private String getRecord(Long index) throws FileParserException {
        String record = null;
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "r")) {
            accessFile.seek(index);
            record = accessFile.readLine();
        } catch (IOException ex) {
            throw new FileParserException(ex.getMessage());
        }
        return record;
    }

    public void printResult(ArrayList<String> results, long time) throws FileParserException {
        if (results.isEmpty()) {
            System.out.println("Подходящие записи не найдены");
        }
        for (String result : results) {
            ArrayList<Long> list = this.indexMap.get(result);
            for (Long index : list) {
                System.out.println("\"" + result + "\"" + "[" + getRecord(index) + "]");
            }
        }
        System.out.println("Количество найденных строк: " + results.size());
        System.out.println("Время затраченное на поиск: " + time);
    }
}
