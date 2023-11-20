package com.echecs.util;

import com.echecs.Position;
import com.echecs.pieces.Piece;

/**
 * Classe utilitaire pour le jeu d'échecs.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @since 2023-10-01
 * @version 1.0
 */
public class EchecsUtil {
    /**
     * Constructeur privé pour empêcher l'instanciation inutile de la classe.
     */
    private EchecsUtil() {
    }
    /**
     * Convertit les indices d'une case de l'échiquier en objet Position
     *
     * @param i byte indice de la ligne d'une case (0 à 7)
     * @param j byte indice de la colonne d'une case (0 à 7)
     * @return Position position de la case
     */
    public static Position getPosition(byte i, byte j) {
        byte  ligne = (byte)(i+1);
        char colonne = (char)('a'+j);
        return new Position(colonne,ligne);
    }
    /**
     * Retourne le numéro d'une ligne de l'échiquier (8 à 1) à partir de son indice (0 à 7).
     *
     * @param i int indice d'une ligne entre 0 et 7
     * @return byte numéro de la ligne entre 8 et 1
     */
    public static byte getLigne(byte i) {
        return (byte)(8-i);
    }

    /**
     * Retourne la colonne de l'échiquier (a à h) à partir de son indice (0 à 7).
     *
     * @param j int indice d'une colonne entre 0 et 7
     * @return char caractère de la colonne entre a et h
     */
    public static char getColonne(byte j) {
        return (char)('a'+j);
    }
    public static byte indiceLigne(byte ligne) {
        return (byte)(8-ligne);
    }
    public static byte indiceLigne(Position p) {
        return (byte)(p.getLigne()-1);
    }
    public static byte indiceColonne(char colonne) {
        return (byte)(colonne-'a');
    }
    public static byte indiceColonne(Position p) {
        return (byte)(p.getColonne() - 'a');
    }
    /**
     * Indique si une position est valide sur un échiquier (ligne entre 1
     * et 8 et colonne entre a et h).
     *
     * @param p Position position à valider
     * @return true, si p est une position valide de l'échiquier, false sinon
     */
    public static boolean positionValide(Position p) {
        return p.getLigne()>=1 && p.getLigne()<=8 && p.getColonne()>='a' && p.getColonne()<='h';
    }

    public static boolean estDirectionLibre(Position p1, Position p2, Piece[][] echiquier) {
        if (!positionValide(p1) || !positionValide(p2))
            return false;

        if (p1.estSurLaMemeColonneQue(p2) && indiceLigne(p1) < indiceLigne(p2)) //vers le haut
            for (int i = 1/*pour commencer vers le prochain*/; i < p1.distLigne(p2); i++) {
                if (echiquier[indiceLigne(p1)-1][indiceColonne(p1)] != null)
                    return false;
            }
        else if (p1.estSurLaMemeColonneQue(p2) && indiceLigne(p1) > indiceLigne(p2)) //vers le bas
            for (int i = 1; i < p1.distLigne(p2); i++) {
                if (echiquier[indiceLigne(p1)+1][indiceColonne(p1)] != null)
                    return false;
            }
        else if (p1.estSurLaMemeLigneQue(p2) && indiceColonne(p1) < indiceColonne(p2)) //vers la droite
            for (int i = 1; i < p1.distColonne(p2); i++) {
                if (echiquier[indiceLigne(p1)][indiceColonne(p1)+i] != null)
                    return false;
            }
        else if (p1.estSurLaMemeLigneQue(p2) && indiceColonne(p1) > indiceColonne(p2)) //vers la gauche
            for (int i = 1; i < p1.distColonne(p2); i++) {
                if (echiquier[indiceLigne(p1)][indiceColonne(p1)-i] != null)
                    return false;
            }


        else if (p1.estSurLaMemeDiagonaleQue(p2)
                && indiceColonne(p1) < indiceColonne(p2)
                && indiceLigne(p1) > indiceLigne(p2)) { //haut-droit
            for (int i = 1; i<p1.distColonne(p2);i++)//on veut atteindre la position du p2 de notre position, donc on reduit notre distence entre les deux
                if (echiquier[indiceLigne(p1) - i][indiceColonne(p1)+i] != null)
                    return false;
        }
        else if (p1.estSurLaMemeDiagonaleQue(p2)
                && indiceColonne(p1) > indiceColonne(p2)
                && indiceLigne(p1) > indiceLigne(p2)) { //haut-gauche
            for (int i = 1; i<p1.distColonne(p2);i++)
                if (echiquier[indiceLigne(p1) - i][indiceColonne(p1) - i] != null)
                    return false;
        }

        else if (p1.estSurLaMemeDiagonaleQue(p2)
                && indiceColonne(p1) < indiceColonne(p2)
                && indiceLigne(p1) < indiceLigne(p2)) { //bas droite
            for (int i = 1; i<p1.distColonne(p2);i++)
                if (echiquier[indiceLigne(p1) + i][indiceColonne(p1) + i] != null)
                    return false;
        }
        else if (p1.estSurLaMemeDiagonaleQue(p2)
                && indiceColonne(p1) > indiceColonne(p2)
                && indiceLigne(p1) < indiceLigne(p2)) { //bas gauche
            for (int i = 1; i<p1.distColonne(p2);i++)
                if (echiquier[indiceLigne(p1) + i][indiceColonne(p1) - i] != null)
                    return false;
        }
        return true;
    }
}


































