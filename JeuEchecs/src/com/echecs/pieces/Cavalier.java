package com.echecs.pieces;

import com.echecs.Position;
import com.echecs.util.EchecsUtil;

public class Cavalier extends Piece{
    public Cavalier(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        if (!EchecsUtil.positionValide(pos1) || !EchecsUtil.positionValide(pos2))
            return false;

        return (pos1.distColonne(pos2) == 1 && pos1.distLigne(pos2) == 2) || (pos1.distColonne(pos2) == 2 && pos1.distLigne(pos2) == 1);
    }
}
