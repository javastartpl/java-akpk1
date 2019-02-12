package pl.javastart.library.app;

import pl.javastart.library.exception.*;
import pl.javastart.library.io.ConsolePrinter;
import pl.javastart.library.io.DataReader;
import pl.javastart.library.io.file.FileManager;
import pl.javastart.library.io.file.FileManagerBuilder;
import pl.javastart.library.model.Book;
import pl.javastart.library.model.Library;
import pl.javastart.library.model.LibraryUser;
import pl.javastart.library.model.Magazine;

import java.util.Comparator;
import java.util.InputMismatchException;

class LibraryControl {
    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);
    private FileManager fileManager;

    private Library library;

    LibraryControl() {
        fileManager = new FileManagerBuilder(printer, dataReader).build();
        try {
            library = fileManager.importData();
            printer.printLine("Zaimportowane dane z pliku");
        } catch (DataImportException | InvalidDataException e) {
            printer.printLine(e.getMessage());
            printer.printLine("Zainicjowano nową bazę.");
            library = new Library();
        }
    }

    void controlLoop() {
        Option option;

        do {
            printOptions();
            option = getOption();
            switch (option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case PRINT_MAGAZINES:
                    printMagazines();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                case ADD_USER:
                    addUser();
                    break;
                case PRINT_USERS:
                    printUsers();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    printer.printLine("Nie ma takiej opcji, wprowadź ponownie: ");
            }
        } while (option != Option.EXIT);
    }

    private Option getOption() {
        boolean optionOk = false;
        Option option = null;
        while (!optionOk) {
            try {
                option = Option.createFromInt(dataReader.getInt());
                optionOk = true;
            } catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage() + ", podaj ponownie:");
            } catch (InputMismatchException ignored) {
                printer.printLine("Wprowadzono wartość, która nie jest liczbą, podaj ponownie:");
            }
        }

        return option;
    }

    private void printOptions() {
        printer.printLine("Wybierz opcję: ");
        for (Option option : Option.values()) {
            printer.printLine(option.toString());
        }
    }

    private void addBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            library.addPublication(book);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć książki, niepoprawne dane");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Osiągnięto limit pojemności, nie można dodać kolejnej książki");
        }
    }

    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication(magazine);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć magazynu, niepoprawne dane");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Osiągnięto limit pojemności, nie można dodać kolejnego magazynu");
        }
    }

    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try {
            library.addUser(libraryUser);
        } catch (UserAlreadyExistsException e) {
            printer.printLine(e.getMessage());
        }
    }

    private void printBooks() {
        printer.printBooks(library.getSortedPublications(
                (p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle())
        ));
    }

    private void printMagazines() {
        printer.printMagazines(library.getSortedPublications(
                (p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle())
        ));
    }

    private void printUsers() {
        printer.printUsers(library.getSortedUsers(
                (p1, p2) -> p1.getLastName().compareToIgnoreCase(p2.getLastName())
        ));
    }

    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            if (library.removePublication(magazine))
                printer.printLine("Usunięto magazyn.");
            else
                printer.printLine("Brak wskazanego magazynu.");
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć magazynu, niepoprawne dane");
        }
    }

    private void deleteBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            if (library.removePublication(book))
                printer.printLine("Usunięto książkę.");
            else
                printer.printLine("Brak wskazanej książki.");
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć książki, niepoprawne dane");
        }
    }

    private void exit() {
        try {
            fileManager.exportData(library);
            printer.printLine("Export danych do pliku zakończony powodzeniem");
        } catch (DataExportException e) {
            printer.printLine(e.getMessage());
        }
        dataReader.close();
        printer.printLine("Koniec programu, papa!");
    }

    private enum Option {
        EXIT(0, "Wyjście z programu"),
        ADD_BOOK(1, "Dodanie książki"),
        ADD_MAGAZINE(2, "Dodanie magazynu/gazety"),
        PRINT_BOOKS(3, "Wyświetlenie dostępnych książek"),
        PRINT_MAGAZINES(4, "Wyświetlenie dostępnych magazynów/gazet"),
        DELETE_BOOK(5, "Usuń książkę"),
        DELETE_MAGAZINE(6, "Usuń magazyn"),
        ADD_USER(7, "Dodaj czytelnika"),
        PRINT_USERS(8, "Wyświetl czytelników");

        private int value;
        private String description;

        Option(int value, String desc) {
            this.value = value;
            this.description = desc;
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("Brak opcji o id " + option);
            }
        }
    }
}