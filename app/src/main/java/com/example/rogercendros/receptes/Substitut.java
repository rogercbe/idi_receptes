package com.example.rogercendros.receptes;

public class Substitut {

    private int idOriginal;
    private int idNou;

    public Substitut(int idOriginal, int idNou) {
        this.idOriginal = idOriginal;
        this.idNou = idNou;
    }

    public int getIdOriginal() {
        return idOriginal;
    }

    public void setIdOriginal(int idOriginal) {
        this.idOriginal = idOriginal;
    }

    public int getIdNou() {
        return idNou;
    }

    public void setIdNou(int idNou) {
        this.idNou = idNou;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof Substitut){
            Substitut i = (Substitut) v;
            retVal = (i.idNou == this.idNou && i.idOriginal == this.idOriginal);
        }

        return retVal;
    }
}
