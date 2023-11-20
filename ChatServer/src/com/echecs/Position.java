package com.echecs;

import com.echecs.util.EchecsUtil;
/**
 * Représente une position sur un échiquier de jeu d'échecs. Les lignes de
 * l'échiquier sont numérotées de 8 à 1 et les colonnes de a à h.
 * Cette classe fournit quelques méthodes de comparaison de 2 positions :
 * sont-elles voisines ? sur la même ligne ? colonne ? diagonale ?
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class Position {
    private char colonne;  //a à h
    private byte ligne;    //0 à 7

    /**
     * Crée une position correspondant à une case d'un échiquier de jeu d'échecs.
     *
     * @param colonne char Colonne a à h de la case.
     * @param ligne byte Ligne 8 à 1 de la case.
     */
    public Position(char colonne, byte ligne) {
        this.colonne = Character.toLowerCase(colonne);
        this.ligne = ligne;
    }

    public char getColonne() {
        return colonne;
    }

    public byte getLigne() {
        return ligne;
    }

    /**
     * Indique si 2 positions VALIDE sont voisines sur un échiquier.
     *
     * @param p Position La position à comparer avec this.
     * @return boolean true si les 2 positions sont voisines, false sinon.
     */
    public boolean estVoisineDe(Position p)
    {
        if (!EchecsUtil.positionValide(p)) return  false;
        if(this.estSurLaMemeLigneQue(p) && distColonne(p) == 1)
            return true;
        if(this.estSurLaMemeColonneQue(p) && distLigne(p) == 1)
            return true;
        return estDiagonaleDirect(p);
    }

    public int distColonne(Position p)
    {
        return Math.abs(p.colonne - this.colonne);
    }

    public int distLigne(Position p)
    {
        return Math.abs(p.ligne - this.ligne);
    }

    public boolean estDiagonaleDirect(Position p)
    {
        return distColonne(p) == 1 && distLigne(p) == 1;
    }

    /**
     * Indique si 2 positions VALIDE sont sur la même ligne sur un échiquier.
     *
     * @param p Position La position à comparer avec this.
     * @return boolean true si les 2 positions sont sur la même ligne, false sinon.
     */
    public boolean estSurLaMemeLigneQue(Position p){
        if (!EchecsUtil.positionValide(p)) return  false;
        return p.ligne == this.ligne;
    }
    /**
     * Indique si 2 positions VALIDE sont sur la même colonne sur un échiquier.
     *
     * @param p Position La position à comparer avec this.
     * @return boolean true si les 2 positions sont sur la même colonne, false sinon.
     */
    public boolean estSurLaMemeColonneQue(Position p) {
        if (!EchecsUtil.positionValide(p)) return  false;
        return p.colonne == this.colonne;
    }
    /**
     * Indique si 2 positions VALIDE sont sur la même diagonale sur un échiquier.
     *
     * @param p Position La position à comparer avec this.
     * @return boolean true si les 2 positions sont sur la même diagonale, false sinon.
     */
    public boolean estSurLaMemeDiagonaleQue(Position p)
    {
        if (!EchecsUtil.positionValide(p)) return  false;
        return Math.abs(p.colonne - this.colonne) == Math.abs(p.ligne - this.ligne);
    }
}
