package pl.javastart.library.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class Publication implements Serializable, Comparable<Publication> {
    private String title;
    private String publisher;
    private int year;

    Publication(String title, String publisher, int year) {
        this.title = title;
        this.publisher = publisher;
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public abstract String toCsv();

    @Override
    public String toString() {
        return title + ", " + publisher + ", " + year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return year == that.year &&
                Objects.equals(title, that.title) &&
                Objects.equals(publisher, that.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, publisher, year);
    }

    @Override
    public int compareTo(Publication p) {
        return title.compareToIgnoreCase(p.title);
    }
}