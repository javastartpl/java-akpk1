package pl.javastart.library.io;

import pl.javastart.library.model.Book;
import pl.javastart.library.model.LibraryUser;
import pl.javastart.library.model.Magazine;
import pl.javastart.library.model.Publication;

import java.util.Collection;

public class ConsolePrinter {
    //zmiana typu parametru
    public void printBooks(Collection<Publication> publications) {
        int counter = 0;
        for (Publication publication : publications) {
            if(publication instanceof Book) {
                printLine(publication.toString());
                counter++;
            }
        }
        if (counter == 0)
            printLine("Brak książek w bibliotece");
    }

    //zmiana typu parametru
    public void printMagazines(Collection<Publication> publications) {
        int counter = 0;
        for (Publication publication : publications) {
            if(publication instanceof Magazine) {
                printLine(publication.toString());
                counter++;
            }
        }
        if (counter == 0)
            printLine("Brak magazynów w bibliotece");
    }

    public void printUsers(Collection<LibraryUser> users) {
        for (LibraryUser user : users) {
            printLine(user.toString());
        }
    }

    public void printLine(String text) {
        System.out.println(text);
    }
}