package it.polimi.ingsw.MODEL;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Serializable {

    private int value;
    private int movements;

    public Card(int value, int movements) {
        this.value = value;
        this.movements = movements;
    }

    public int getValue() {
        return this.value;
    }

    public int getMovement() {
        return this.movements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return value == card.value && movements == card.movements;
    }

}
