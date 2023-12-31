package com.chat.client;

import com.echecs.EtatPartieEchecs;

/**
 * Cette classe étend la classe Client pour lui ajouter des fonctionnalités
 * spécifiques au chat et au jeu d'échecs en réseau.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class ClientChat extends Client {
    //Cette classe est pour le moment vide et sera compléter dans le TP.

    public EtatPartieEchecs getEtatPartieEchecs() {
        return etatPartieEchecs;
    }

    public void setEtatPartieEchecs(String etatPartieEchecs) {
        this.etatPartieEchecs.setEtatEchiquier(etatPartieEchecs);
    }

    private EtatPartieEchecs etatPartieEchecs;

    public void nouvellePartie()
    {
        etatPartieEchecs = new EtatPartieEchecs();
    }
}
