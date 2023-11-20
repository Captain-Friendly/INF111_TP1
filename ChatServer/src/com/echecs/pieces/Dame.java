package com.echecs.pieces;

import com.echecs.Position;
import com.echecs.util.EchecsUtil;

public class Dame extends Piece {
    public Dame(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        if (!EchecsUtil.positionValide(pos1) || !EchecsUtil.positionValide(pos2))
            return false;
        if (!pos1.estSurLaMemeColonneQue(pos2) && !pos1.estSurLaMemeLigneQue(pos2) && !pos1.estSurLaMemeDiagonaleQue(pos2))
            return false;
        return EchecsUtil.estDirectionLibre(pos1,pos2,echiquier);
    }


}
