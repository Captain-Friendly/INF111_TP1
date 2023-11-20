package com.echecs;

import java.util.Arrays;

public class EtatPartieEchecs {
    public EtatPartieEchecs() {
        this.etatEchiquier = new char[][]{
                {'t', 'c', 'f', 'd', 'r', 'f', 'c', 't'},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {'T', 'C', 'F', 'D', 'R', 'F', 'C', 'T'}
        };
    }

    public char[][] getEtatEchiquier() {
        return etatEchiquier;
    }

    public void setEtatEchiquier(String echiquier) {
        String[] chaine = echiquier.split("\n");
        //WARNING ne marche pas, je sais pas comment fix
//        throw new RuntimeException(chaine[1]);
//        char[][] etatEchiquier = new char[8][8];
        for (int i = 0; i < 8; i++){
            etatEchiquier[i] = chaine[i].toCharArray() ;
        }
        this.etatEchiquier = etatEchiquier;
    }

    private char[][] etatEchiquier;

    @Override
    public String toString() {
        String chaine ="";

        for (int i = 0; i < 8; i++){
            chaine+=i+1;
            for (int j = 0; j<8; j++){
                if (etatEchiquier[i][j] != ' ')
                    chaine+=" "+etatEchiquier[i][j];
                else
                    chaine+=" ·";
            }
            chaine+='\n';
        }

        chaine+="  a b c d e f g h\n";

        return chaine;

    }
}






















