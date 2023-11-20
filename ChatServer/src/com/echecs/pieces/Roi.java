package com.echecs.pieces;

import com.echecs.Position;
import com.echecs.util.EchecsUtil;

public class Roi extends Piece {
    public Roi(char couleur) {
        super(couleur);
    }

    /***
     * permet de voir si le roi peut se deplacer vers cette position
     * permet le deplacement de roque, mais vous etes responsable
     *
     * */
    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        if (!EchecsUtil.positionValide(pos1) || !EchecsUtil.positionValide(pos2))
            return false;

        return pos1.estDiagonaleDirect(pos2) || pos1.distColonne(pos2) == 2 || pos1.distLigne(pos2)==1;
    }
}
