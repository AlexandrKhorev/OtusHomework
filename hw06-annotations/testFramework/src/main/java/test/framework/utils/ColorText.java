package test.framework.utils;

public enum ColorText {

    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m");

    private String color;

    ColorText(String color) {
        this.color = color;
    }

    public static void printColorString(String value, ColorText color) {
        System.out.printf("%s%s%s%n", color.color, value, RESET.color);
    }

    public static void printWarning(String value){
        printColorString(value, YELLOW);
    }

    public static void printException(String value) {
        printColorString(value, RED);
    }

    public static void printInfo(String value) {
        printColorString(value, BLUE);
    }

    public static void print() {
        System.out.println();
    }

    public static void print(String value) {
        System.out.println(value);
    }
}

