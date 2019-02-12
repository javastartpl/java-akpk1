package pl.javastart.library.io.file;

import pl.javastart.library.exception.DataExportException;
import pl.javastart.library.exception.DataImportException;
import pl.javastart.library.exception.InvalidDataException;
import pl.javastart.library.model.*;

import java.io.*;
import java.util.Collection;
import java.util.Scanner;

public class CsvFileManager implements FileManager {
    private static final String PUBLICATIONS_FILE_NAME = "Library.csv";
    private static final String USERS_FILE_NAME = "Library_users.csv";

    @Override
    public void exportData(Library library) {
        exportPublications(library);
        exportUsers(library);
    }

    @Override
    public Library importData() {
        Library library = new Library();
        importPublications(library);
        importUsers(library);
        return library;
    }

    private void exportPublications(Library library) {
        Collection<Publication> publications = library.getPublications().values();
        try (FileWriter fileWriter = new FileWriter(PUBLICATIONS_FILE_NAME);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (Publication publication : publications) {
                bufferedWriter.write(publication.toCsv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new DataExportException("Błąd zapisu danych do pliku " + PUBLICATIONS_FILE_NAME);
        }
    }

    private void exportUsers(Library library) {
        Collection<LibraryUser> users = library.getUsers().values();
        try (FileWriter fileWriter = new FileWriter(USERS_FILE_NAME);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (LibraryUser libUser : users) {
                bufferedWriter.write(libUser.toCsv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new DataExportException("Błąd zapisu danych do pliku " + USERS_FILE_NAME);
        }
    }

    private Publication createObjectFromString(String csvText) {
        String[] split = csvText.split(";");
        String type = split[0];
        if(Book.TYPE.equals(type)) {
            return createBook(split);
        } else if(Magazine.TYPE.equals(type)) {
            return createMagazine(split);
        }
        throw new InvalidDataException("Nieznany typ publikacji: " + type);
    }

    private Book createBook(String[] data) {
        String title = data[1];
        String publisher = data[2];
        int year = Integer.valueOf(data[3]);
        String author = data[4];
        int pages = Integer.valueOf(data[5]);
        String isbn = data[6];
        return new Book(title, author, year, pages, publisher, isbn);
    }

    private Magazine createMagazine(String[] data) {
        String title = data[1];
        String publisher = data[2];
        int year = Integer.valueOf(data[3]);
        int month = Integer.valueOf(data[4]);
        int day = Integer.valueOf(data[5]);
        String language = data[6];
        return new Magazine(title, publisher, language, year, month, day);
    }

    private void importPublications(Library library) {
        try (Scanner fileReader = new Scanner(new File(PUBLICATIONS_FILE_NAME))) {
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                Publication publication = createObjectFromString(line);
                library.addPublication(publication);
            }
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + PUBLICATIONS_FILE_NAME);
        }
    }

    private void importUsers(Library library) {
        try (Scanner fileReader = new Scanner(new File(USERS_FILE_NAME))) {
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                LibraryUser libUser = createUserFromString(line);
                library.addUser(libUser);
            }
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + USERS_FILE_NAME);
        }
    }

    private LibraryUser createUserFromString(String csvText) {
        String[] split = csvText.split(";");
        String firstName = split[0];
        String lastName = split[1];
        String pesel = split[2];
        return new LibraryUser(firstName, lastName, pesel);
    }
}