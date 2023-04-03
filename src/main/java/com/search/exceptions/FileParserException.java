package com.search.exceptions;

public class FileParserException extends RuntimeException {
    public String attrName;
    public FileParserException (String message) {
        super("Произошла ошибка чтения файла: " + message);
        attrName = message;
    }
}
