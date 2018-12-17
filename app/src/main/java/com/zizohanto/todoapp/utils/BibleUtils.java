package com.zizohanto.todoapp.utils;

import com.zizohanto.todoapp.data.BooksOfBibleCodes;

import java.util.Map;

public class BibleUtils {

    public static String getBibleUrl(CharSequence selectedText) {
        String selectedTextString = selectedText.toString();
        String selectedTextStringWithoutSpaces = selectedTextString.toLowerCase().replace(" ", "");

        String selectedTextStringWithoutPrefix = "";
        String bookPrefix = "";
        if (bookHasPrefix(selectedTextStringWithoutSpaces)) {
            bookPrefix = getPrefix(selectedTextStringWithoutSpaces);
            selectedTextStringWithoutPrefix = removePrefix(selectedTextStringWithoutSpaces);
        } else {
            selectedTextStringWithoutPrefix = selectedTextStringWithoutSpaces;
        }
        String bookName = getBookname(selectedTextStringWithoutPrefix);
        String bookVerse = getVerse(selectedTextStringWithoutPrefix);

        String bookMapKey = "";
        if (bookPrefix.isEmpty()) {
            bookMapKey = bookName;
        } else {
            bookMapKey = bookPrefix + " " + bookName;
        }
        Map<String, String> booksOfBibleCodesMap = BooksOfBibleCodes.getBookOfBibleCodes();
        String booksOfBibleCodesMapValue = "";
        booksOfBibleCodesMapValue = booksOfBibleCodesMap.get(bookMapKey);

        // Build the intent
        // URL Format --> https://www.bible.com/en-GB/bible/1/1CO.13.4-5
        // String url = "https://www.bible.com/en-GB/bible/1/" + booksOfBibleCodesMapValue + "." + bookVerse;
        //String url = "youversion://bible?reference=" + bookMapValue + "." + bookVerse;

        return "https://www.bible.com/en-GB/bible/1/" + booksOfBibleCodesMapValue + "." + bookVerse;
    }

    private static String removePrefix(String selectectedTextStringWithoutPrefix) {
        selectectedTextStringWithoutPrefix = selectectedTextStringWithoutPrefix.substring(1);
        if (selectectedTextStringWithoutPrefix.substring(0, 2).equals("ii")) {
            selectectedTextStringWithoutPrefix = selectectedTextStringWithoutPrefix.substring(2);
        } else if (selectectedTextStringWithoutPrefix.substring(0, 1).equals("i")) {
            selectectedTextStringWithoutPrefix = selectectedTextStringWithoutPrefix.substring(1);
        }
        return selectectedTextStringWithoutPrefix;
    }

    private static String getBookname(String givenText) {
        String bookName;
        // Remove integers
        bookName = givenText.replaceAll("\\d", "");
        // Remove colon
        bookName = bookName.replaceAll(":", "");
        // Capitalize first letter of book name
        bookName = bookName.substring(0, 1).toUpperCase() + bookName.substring(1);
        if (bookName.contains("Song")) {
            // For when users enter Songs of Solomon, Song of Songs or Songs of Songs
            bookName = "Song of Solomon";
        }
        return bookName;
    }

    private static String getVerse(String givenText) {
        String verse;
        verse = givenText.replaceAll("[a-zA-Z]*", "");
        verse = verse.replaceAll(":", ".");
        return verse;
    }

    private static String getPrefix(String givenText) {
        String prefix;
        if (givenText.substring(0, 3).equals("iii")) {
            prefix = "3";
            return prefix;
        } else if (givenText.substring(0, 2).equals("ii")) {
            prefix = "2";
            return prefix;
        } else if (givenText.substring(0, 1).equals("i")) {
            prefix = "1";
            return prefix;
        }
        // The prefix is either 1, 2 or 3
        prefix = givenText.substring(0, 1);
        return prefix;
    }

    private static boolean bookHasPrefix(String book) {
        if (book.startsWith("1") || book.startsWith("2") || book.startsWith("3")
                || book.startsWith("i") || book.startsWith("ii") || book.startsWith("iii")) {
            if (!book.contains("isaiah")) {
                return true;
            }
        }
        return false;
    }
}
