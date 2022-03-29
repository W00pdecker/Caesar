package com.company;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.io.*;

public class Main {
    public static final char[] ALPHABET = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з',
            'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
            '.', ',', '”', ':', '-', '!', ' ', '»', '«', '—', '(', ')' };
    public static String str = "Губы его всегда сухи. " +
            "Внутри его шершавого рта медленно шевелится наждачный язык, стирая в пыль последние чёрные зубы.";
    public static String st = "! я ,";
    public static String source = "src/resources/text.txt"; //дефолтный файл для чтения
    public static String dest = "src/resources/dest.txt";//дефолтный файл для записи
    public static final String PLS_ENTER_THE_SOURCE = "ПОЖАЛУЙСТА ВВЕСТИ СУРС ИЛИ ЖМИ 1";
    public static final String PLS_ENTER_THE_DESTINATION = "СКАЖИТЕ КУДА ПИСАТЬ ИЛИ ЖМИ 1";
    public static final String PLS_ENTER_THE_KEY = "ПОЖАЛУЙСТА ВВЕСТИ КЛЮЧ";
    public static final String CANNOT_CREATE_FILE = "ИЗВИНИТЕ НЕ МОГУ НЕ ПОЛУЧАЕТСЯ";
    public static final String NOT_EXIST = "НЕПРАВИЛЬНО АДРЕС ВВЕСТИ ПРАВИЛЬНО ЕЩЕ РАЗОК";
    public static final String QUESTION = "ВВЕСТИ 1 ДЛЯ ШИФРОВАТЕЛЬ ВВЕСТИ 2 ДЛЯ ДЕШИФРОВАТЕЛЬ 3 ДЛЯ БРУТФОРС";
    public static final String CYPHERED = "ТЕКСТ ЗАШИФРОВАНО";
    public static final String DECYPHERED = "ТЕКСТ РАСШИФРОВАНО";

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println(PLS_ENTER_THE_SOURCE);
        String src = scan.next(); //просим ввести адрес, если адрес не существует, то просим ввести еще раз
        if (src.equals("1")) src = source; //если нажать 1, то программа читает из дефолтного файла
        else {
            while (Files.notExists(Path.of(src))) {
                System.out.println(NOT_EXIST);
                src = scan.next();
                if (src.equals("1")) {
                    src = source;
                    break;
                }

            }
        }


        System.out.println(PLS_ENTER_THE_DESTINATION);
        String dstn = scan.next(); //считываем место для записи, если там нет файла - создаем
        if (dstn.equals("1")) dstn = dest; //если нажата 1, то пишем в дефолтный файл.
        else {
            if (Files.notExists(Path.of(dstn))) {
                try {
                    Files.createFile(Path.of(dstn));
                } catch (IOException e) {
                    System.out.println(CANNOT_CREATE_FILE);
                }
            }
        }

        System.out.println(QUESTION);//спрашиваем, что должна делать программа
        int answer = scan.nextInt();//answer == 1 - шифровка, 2 -- дешифровка, 3 - брутфорс
        while (answer != 1 && answer != 2 && answer != 3) {
            System.out.println("ВВЕДДИТЕНО НЕ ТО ЧИСЛО ВВЕДИТЕ ТО ЧИСЛО");
            answer = scan.nextInt();
        }
        if (answer == 3) bruteforce(src, dstn);

        else {
            try (FileReader reader = new FileReader(src);
                 FileWriter writer = new FileWriter(dstn)) {


                char[] buffer = new char[65536];
                System.out.println(PLS_ENTER_THE_KEY); //вводим ключ для шифра или дешифровки
                int key = scan.nextInt();
                while (reader.ready()) { //считываем файл по кускам, каждый кусок шифруем и пишем в другой файл
                    int real = reader.read(buffer);
                    if (answer == 1)
                        writer.write(cypher(buffer, key), 0, real);
                    if (answer == 2)
                        writer.write(decypher(buffer, key), 0, real);
                }
                if (answer == 1)
                    System.out.println(CYPHERED);
                if (answer == 2)
                    System.out.println(DECYPHERED);
            } catch (Exception E) {
                System.out.println("Something went wrong");
            }

    }



    }

    public static char[] cypher(char[] charArray, int key) {//метод для шифрования

        char[] result = new char[charArray.length];
        Arrays.sort(ALPHABET);//можно было с самого начала сделать ALPHABET отсортированным, но мне впадлу (я знаю, это плохо)
        for (int i = 0; i < charArray.length; i++) {
            // для каждого символа во входящем тексте ищем соответствующий ему индекс в ALPHABET и прибавляем сдвиг.
            // для того, чтобы зациклить движение по алфавиту, используем деление с остатком на длину алфавита
            int j = (Arrays.binarySearch(ALPHABET, charArray[i]) + key) % ALPHABET.length;

            result[i] = ALPHABET[j];
        }
        return result;
    }

    public  static  char[] decypher(char[] charArray, int key) {//метод для дешифрования (все то же самое)
        char[] result = new char[charArray.length];
        Arrays.sort(ALPHABET);
        for (int i = 0; i < charArray.length; i++) {
            int j = (Arrays.binarySearch(ALPHABET, charArray[i]) - key) % ALPHABET.length;

            if (j < 0) j = ALPHABET.length + j;  //тут костыль для деления отрицательных чисел, наверно можно как-то умнее реализовать
            result[i] = ALPHABET[j];
        }
        return result;
    }

    public static void bruteforce(String src, String dest) {


            for (int key = 1; key < ALPHABET.length; key++) {
                try (FileReader reader = new FileReader(src);
                     FileWriter writer = new FileWriter(dest, true)) {
                    char[] buffer = new char[65536];
                    while (reader.ready()) {
                        int real = reader.read(buffer);
                        writer.write(decypher(buffer, key), 0, real);
                    }

                    writer.write(System.getProperty("line.separator"));
                    writer.write(System.getProperty("line.separator"));
                    writer.write(System.getProperty("line.separator"));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("ПРОИЗВЕДЕН БРУТФОРС");


    }

}
