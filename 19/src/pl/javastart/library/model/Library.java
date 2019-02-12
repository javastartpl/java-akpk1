package pl.javastart.library.model;

import java.io.Serializable;
import java.util.Arrays;

public class Library implements Serializable {

    private static final int INITIAL_CAPACITY = 1;
    private int publicationsNumber;
    private Publication[] publications = new Publication[INITIAL_CAPACITY];

    public Publication[] getPublications() {
        Publication[] result = new Publication[publicationsNumber];
        for (int i = 0; i < publicationsNumber; i++) {
            result[i] = publications[i];
        }
        return result;
    }

    public void addPublication(Publication publication) {
        if(publicationsNumber == publications.length) {
            publications = Arrays.copyOf(publications, publications.length * 2);
        }
        publications[publicationsNumber] = publication;
        publicationsNumber++;
    }

    public boolean removePublication(Publication pub) {
        final int NOT_FOUND = -1;
        int found = NOT_FOUND;
        int i = 0;
        while (i < publications.length && found == NOT_FOUND) {
            if (pub.equals(publications[i])) {
                found = i;
            } else {
                i++;
            }
        }

        if (found != NOT_FOUND) {
            System.arraycopy(publications, found + 1, publications, found, publications.length - found - 1);
            publicationsNumber--;
        }

        return found != NOT_FOUND;
    }
}