package org.gradle.module2;

import java.util.ArrayList;
import java.util.List;

public class SearchDictionary {
    private TrieNode root;

    public SearchDictionary() {
        this.root = new TrieNode();
    }

    //додається слово в словник, яке попередньо приводиться до нижнього регістру
    //потім використовується метод findNode() для пошуку вузла, який відповідає початковій частині слова
    public void addWord(String word) {
        root.addWord(word.toLowerCase());
        TrieNode lastNode = root.findNode(word.toLowerCase());
        if (lastNode != null) {
            lastNode.isEndOfWord = true;
        }
    }
    //видаляється слово зі словника
    public String delWord(String word) {
        return root.delWord(word.toLowerCase()) ? word : null;
    } //якщо такий вузол існує, то isEndOfWord в ньому встановлюється в false, якщо не існує - метод повертає null

    //перевірка чи міститься слово в словнику
    public boolean hasWord(String word) {
        return root.hasWord(word.toLowerCase());
    }
    //повертає список всіх слів у словнику, які починаються з заданої літери
    public Iterable<String> query(String query) {
        List<String> result = new ArrayList<>();
        root.collectWordsWithPrefix(query.toLowerCase(), result);
        return result;
    }

    public int countWords() {
        return root.countWords();
    }

    private static class TrieNode {
        private TrieNode[] child;
        private boolean isEndOfWord;

        public TrieNode() {
            this.child = new TrieNode[26];
            this.isEndOfWord = false;
        }
        //cлово послідовно проходить по кожному символу,
        //для кожного символу використовується метод findNode() для пошуку дочірнього вузла, що відповідає символу
        //вузол створюється, якщо його не існує
        public void addWord(String word) {
            TrieNode current = this;
            for (char ch : word.toCharArray()) {
                if (Character.isLetter(ch)) {
                    int index = Character.toLowerCase(ch) - 'a';
                    if (current.child[index] == null) {
                        current.child[index] = new TrieNode();
                    }
                    current = current.child[index];
                }
            }
            current.isEndOfWord = true;
        }

        public boolean delWord(String word) {
            TrieNode node = findNode(word);
            if (node != null && node.isEndOfWord) {
                node.isEndOfWord = false;
                return true;
            }
            return false;
        }

        public boolean hasWord(String word) {
            TrieNode node = findNode(word);
            return node != null && node.isEndOfWord;
        }

        //повертає список слів, які починаються з заданого префікса
        private void collectWordsWithPrefix(String prefix, List<String> result) {
            TrieNode node = findNode(prefix);
            if (node != null) {
                if (prefix.endsWith("*")) {
                    node.collectWords(prefix.substring(0, prefix.length() - 1), result);
                } else {
                    node.collectWords(prefix, result);
                }
            }
        }
        //шукає вузол, який відповідає заданому слову
        private TrieNode findNode(String word) {
            TrieNode current = this;
            for (char ch : word.toCharArray()) {
                if (Character.isLetter(ch)) {
                    int index = Character.toLowerCase(ch) - 'a';
                    if (index < 0 || index >= 26 || current.child[index] == null) {
                        return null;
                    }
                    current = current.child[index];
                }
            }
            return current;
        }

        //збирає всі слова, що починаються з заданого префікса та добавляє в список
        private void collectWords(String currentWord, List<String> result) {
            if (isEndOfWord) {
                result.add(currentWord);
            }

            for (int i = 0; i < 26; i++) {
                if (child[i] != null) {
                    child[i].collectWords(currentWord + (char) ('a' + i), result);
                }
            }
        }
        //к-сть слів
        public int countWords() {
            int count = isEndOfWord ? 1 : 0;
            for (TrieNode child : child) {
                if (child != null) {
                    count += child.countWords();
                }
            }
            return count;
        }
    }
}



