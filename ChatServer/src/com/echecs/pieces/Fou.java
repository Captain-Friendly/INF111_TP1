package com.echecs.pieces;

import com.echecs.Position;
import com.echecs.util.EchecsUtil;

public class Fou extends Piece {
    public Fou(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        if (!EchecsUtil.positionValide(pos1) || !EchecsUtil.positionValide(pos2))
            return false;
        if (pos1.estSurLaMemeDiagonaleQue(pos2))
            return EchecsUtil.estDirectionLibre(pos1,pos2,echiquier);
        return false;
    }
}
