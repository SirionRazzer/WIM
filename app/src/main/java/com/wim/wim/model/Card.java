package com.wim.wim.model;

import java.util.Objects;

/**
 * Card is placeholder for any quote or other content showed in recycler view
 */
public class Card {
    private String header;
    private String text;

    public Card(String header, String text) {
        this.header = header;
        this.text = text;
    }

    public String getHeader() {
        return header;
    }
    public String getTextQuote() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(getHeader(), card.getHeader()) &&
                Objects.equals(text, card.text);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getHeader(), text);
    }
}
