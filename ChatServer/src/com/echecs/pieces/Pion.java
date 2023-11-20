package com.echecs.pieces;

import com.echecs.Position;
import com.echecs.util.EchecsUtil;

public class Pion extends Piece {
    public Pion(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        if (!EchecsUtil.positionValide(pos1) || !EchecsUtil.positionValide(pos2) || !pos2.estVoisineDe(pos1) )
            return false;

        boolean estEnemie = false;
        for (int i = 0; i<echiquier.length; i++)
            for (int j = 0; j<echiquier[i].length;j++)
                if (EchecsUtil.indiceLigne(pos2) == i && EchecsUtil.indiceColonne(pos2) == j)
                    if (this.getCouleur() != echiquier[i][j].getCouleur())
                        estEnemie = true;

        boolean peutAvancer2 = getCouleur()=='b'?EchecsUtil.indiceLigne(pos1)==6:EchecsUtil.indiceLigne(pos1)==1;
        int distMaxLigne = peutAvancer2?2:1;
        int distLigne = getCouleur()=='b'?EchecsUtil.indiceLigne(pos1) - EchecsUtil.indiceLigne(pos2):EchecsUtil.indiceLigne(pos2) - EchecsUtil.indiceLigne(pos1);
        boolean estVersLavant = distLigne > 0;

        //deplacer vers l'avant
        if (!pos1.estSurLaMemeDiagonaleQue(pos2) && estVersLavant && distMaxLigne <= distLigne)
            return true;

        //Deplacer en diagonalle
        if (estEnemie && estVersLavant && pos1.estDiagonaleDirect(pos2))
            return true;

        return false;
    }
}
