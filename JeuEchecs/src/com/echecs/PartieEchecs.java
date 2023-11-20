package com.echecs;

import com.echecs.pieces.Piece;
import com.echecs.pieces.Roi;
import com.echecs.util.EchecsUtil;
import com.sun.xml.internal.bind.v2.TODO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Représente une partie de jeu d'échecs. Orcheste le déroulement d'une partie :
 * déplacement des pièces, vérification d'échec, d'échec et mat,...
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class PartieEchecs {
    /**
     * Grille du jeu d'échecs. La ligne 0 de la grille correspond à la ligne
     * 8 de l'échiquier. La colonne 0 de la grille correspond à la colonne a
     * de l'échiquier.
     */
    private Piece[][] echiquier;

    private String aliasJoueur1, aliasJoueur2;
    private char couleurJoueur1, couleurJoueur2;

    private boolean blancPeutRoque = true, noirPeutRoque = true;

    /**
     * La couleur de celui à qui c'est le tour de jouer (n ou b).
     */
    private char tour = 'b'; //Les blancs commencent toujours
    /**
     * Crée un échiquier de jeu d'échecs avec les pièces dans leurs positions
     * initiales de début de partie.
     * Répartit au hasard les couleurs n et b entre les 2 joueurs.
     */
    public PartieEchecs() {
        echiquier = new Piece[8][8];
        //Placement des pièces :

    }

    /**
     * Change la main du jeu (de n à b ou de b à n).
     */
    private void changerTour() {
        if (tour=='b')
            tour = 'n';
        else
            tour = 'b';
    }
    /**
     * Tente de déplacer une pièce d'une position à une autre sur l'échiquier.
     * Le déplacement peut échouer pour plusieurs raisons, selon les règles du
     * jeu d'échecs. Par exemples :
     *  Une des positions n'existe pas;
     *  Il n'y a pas de pièce à la position initiale;
     *  La pièce de la position initiale ne peut pas faire le mouvement;
     *  Le déplacement met en échec le roi de la même couleur que la pièce.
     *
     * @param initiale Position la position initiale
     * @param finale Position la position finale
     *
     * @return boolean true, si le déplacement a été effectué avec succès, false sinon
     */
    public boolean deplace(Position initiale, Position finale) {
        if (!EchecsUtil.positionValide(initiale) || !EchecsUtil.positionValide(finale))
            return false; //Position invalide

        Piece piece = null;
        for (int i = 0; i<echiquier.length; i++)
            for (int j = 0; j<echiquier[i].length;j++)
                if (EchecsUtil.indiceLigne(initiale) == i && EchecsUtil.indiceColonne(initiale) == j)
                    piece=echiquier[i][j];
        if (piece==null)
            return false; //Pas de piece a deplacer

        if (piece.getCouleur() != getTour())
            return false; //Pas le tour de cette couleur

        //TODO: check for castle
        //TODO: promotion

        for (int i = 0; i<echiquier.length; i++)
            for (int j = 0; j<echiquier[i].length;j++)
                if (EchecsUtil.indiceLigne(finale) == i && EchecsUtil.indiceColonne(finale) == j)
                    if (echiquier[i][j].getCouleur() == piece.getCouleur())
                        return false; // on ne peut pas capturer un piece de la meme couleur


        Piece[][] passe = echiquier.clone();
        //tester si en echec
        echiquier[EchecsUtil.indiceLigne(initiale)][EchecsUtil.indiceColonne(initiale)] = null;
        echiquier[EchecsUtil.indiceLigne(finale)][EchecsUtil.indiceColonne(finale)] = piece;

        if (estEnEchec() == tour)// si on est toujours en echec apres le mouvement on reverse et c'est invalide
        {
            echiquier = passe;
            return false;
        }

        changerTour();
        return true;
    }

    /**
     * Vérifie si un roi est en échec et, si oui, retourne sa couleur sous forme
     * d'un caractère n ou b.
     * Si la couleur du roi en échec est la même que celle de la dernière pièce
     * déplacée, le dernier déplacement doit être annulé.
     * Les 2 rois peuvent être en échec en même temps. Dans ce cas, la méthode doit
     * retourner la couleur de la pièce qui a été déplacée en dernier car ce
     * déplacement doit être annulé.
     *
     * @return char Le caractère n, si le roi noir est en échec, le caractère b,
     * si le roi blanc est en échec, tout autre caractère, sinon.
     */
    public char estEnEchec()
    {
        Position posRoiNoir=null;
        Position posRoiBlanc=null;
        for (int i = 0; i<echiquier.length; i++)
        {
            for (int j = 0; j<echiquier[i].length;j++)
            {
                if (echiquier[i][j] instanceof  Roi)
                {
                    if (echiquier[i][j].getCouleur()=='b')
                        posRoiNoir = EchecsUtil.getPosition((byte)i,(byte)j);
                    else
                        posRoiBlanc = EchecsUtil.getPosition((byte)i,(byte)j);
                }
            }
        }

        for (int i = 0; i<echiquier.length; i++)
        {
            for (int j = 0; j<echiquier[i].length;j++)
            {
                if (echiquier[i][j].getCouleur()=='b')//si une piece blanche
                    if (echiquier[i][j].peutSeDeplacer(EchecsUtil.getPosition((byte)i,(byte)j),posRoiNoir,echiquier))
                        return 'n';
                if (echiquier[i][j].getCouleur()=='n')//si une piece blanche
                    if (echiquier[i][j].peutSeDeplacer(EchecsUtil.getPosition((byte)i,(byte)j),posRoiBlanc,echiquier))
                        return 'b';

            }
        }
        return 'g';
    }
    /**
     * Retourne la couleur n ou b du joueur qui a la main.
     *
     * @return char la couleur du joueur à qui c'est le tour de jouer.
     */
    public char getTour() {
        return tour;
    }
    /**
     * Retourne l'alias du premier joueur.
     * @return String alias du premier joueur.
     */
    public String getAliasJoueur1() {
        return aliasJoueur1;
    }
    /**
     * Modifie l'alias du premier joueur.
     * @param aliasJoueur1 String nouvel alias du premier joueur.
     */
    public void setAliasJoueur1(String aliasJoueur1) {
        this.aliasJoueur1 = aliasJoueur1;
    }
    /**
     * Retourne l'alias du deuxième joueur.
     * @return String alias du deuxième joueur.
     */
    public String getAliasJoueur2() {
        return aliasJoueur2;
    }
    /**
     * Modifie l'alias du deuxième joueur.
     * @param aliasJoueur2 String nouvel alias du deuxième joueur.
     */
    public void setAliasJoueur2(String aliasJoueur2) {
        this.aliasJoueur2 = aliasJoueur2;
    }
    /**
     * Retourne la couleur n ou b du premier joueur.
     * @return char couleur du premier joueur.
     */
    public char getCouleurJoueur1() {
        return couleurJoueur1;
    }
    /**
     * Retourne la couleur n ou b du deuxième joueur.
     * @return char couleur du deuxième joueur.
     */
    public char getCouleurJoueur2() {
        return couleurJoueur2;
    }
}