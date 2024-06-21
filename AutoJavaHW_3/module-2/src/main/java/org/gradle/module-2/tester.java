package org.gradle.module2;

import java.io.*;
import java.util.*;

public class tester {
    public static void main(String[] args) {
        SearchDictionary searchDictionary = new SearchDictionary();

        String filePath = "C:/Users/admin/Downloads/Practise10_OKA/src/wordlist.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                searchDictionary.addWord(line.trim()); //додавання слів до словника
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введіть слово з джокером чи без (наприклад, k*), або команди: add, del, has, exit:");
            String input = scanner.nextLine().trim();

            if (input.startsWith("exit")) {
                break;
            } else if (input.startsWith("add ")) {
                String wordToAdd = input.substring(4).trim();
                searchDictionary.addWord(wordToAdd);
                System.out.println("Добавлено: " + wordToAdd);
            } else if (input.startsWith("del ")) {
                String wordToDelete = input.substring(4).trim();
                String deletedWord = searchDictionary.delWord(wordToDelete);
                if (deletedWord != null) {
                    System.out.println("Видалено: " + deletedWord);
                } else {
                    System.out.println("Слово не знайдено: " + wordToDelete);
                }
            } else if (input.startsWith("has ")) {
                String wordToCheck = input.substring(4).trim();
                boolean hasWord = searchDictionary.hasWord(wordToCheck);
                System.out.println("Слово існує в словнику: " + hasWord);
            } else {
                System.out.println("Запит: " + input);
                Iterable<String> result = searchDictionary.query(input);
                System.out.println("Результат:");
                for (String word : result) {
                    System.out.println(word);
                }
            }
        }

        System.out.println("Кількість слів у словнику: " + searchDictionary.countWords());
    }
}
