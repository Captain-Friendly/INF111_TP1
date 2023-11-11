package com.chat.serveur;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;

import java.util.ArrayList;

/**
 * Cette classe repr�sente un gestionnaire d'�v�nement d'un serveur. Lorsqu'un serveur re�oit un texte d'un client,
 * il cr�e un �v�nement � partir du texte re�u et alerte ce gestionnaire qui r�agit en g�rant l'�v�nement.
 *
 * @author Abdelmoum�ne Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    /**
     * Construit un gestionnaire d'�v�nements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire g�re des �v�nements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * M�thode de gestion d'�v�nements. Cette m�thode contiendra le code qui g�re les r�ponses obtenues d'un client.
     *
     * @param evenement L'�v�nement � g�rer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String msg, typeEvenement, aliasExpediteur;
        ServeurChat serveur = (ServeurChat) this.serveur;


        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            System.out.println("SERVEUR-Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            aliasExpediteur = cnx.getAlias();
            switch (typeEvenement) {
                case "EXIT": //Ferme la connexion avec le client qui a envoy� "EXIT":
                    cnx.envoyer("END");
                    serveur.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des alias des personnes connect�es :
                    cnx.envoyer("LIST " + serveur.list());
                    break;
                case "MSG":
                    serveur.envoyerATousSauf(evenement.getArgument(),aliasExpediteur);
                    break;
                case "HIST":
                    cnx.envoyer("Historique\n");
                    cnx.envoyer(serveur.historique());
                    break;
                case "JOIN":
                    String aliasInvite = evenement.getArgument();


                    for(SalonPrive sal: serveur.salonsPrives)
                    {
                        if ( (sal.getAliasHote().equals(aliasExpediteur)  && sal.getAliasInviter().equals(aliasInvite)) || (sal.getAliasHote().equals(aliasInvite) && sal.getAliasInviter().equals(aliasExpediteur)))
                        {
                            cnx.envoyer("SalonPrive_existe_deja");
                            break;
                        }
                    }

                    if (!aliasInvite.equals(aliasExpediteur)  && serveur.aliasExiste(aliasInvite))
                    {
                        if(serveur.invitations.isEmpty())
                        {
                            serveur.invitations.add(new Invitation(aliasExpediteur, aliasInvite));
                            serveur.envoyerA(aliasInvite, "JOIN "+aliasExpediteur);
                            cnx.envoyer("Invitation envoyer");
                            break;
                        }
                        else
                        {
                            boolean aSalon=false;
                            for(Invitation inv:serveur.invitations){
                                if(
                                        (aliasExpediteur.equals(inv.getAliasInvite()) && aliasInvite.equals(inv.getAliasHote()))
                                )
                                {
                                    serveur.salonsPrives.add(new SalonPrive(inv));
                                    serveur.invitations.remove(inv);
                                    cnx.envoyer("SALON_CREE");
                                    aSalon=true;
                                    break;
                                }
                            }
                            if(aSalon == false)
                            {
                                serveur.invitations.add(new Invitation(aliasExpediteur, aliasInvite));
                                serveur.envoyerA(aliasInvite, "JOIN "+aliasExpediteur);
                                cnx.envoyer("Invitation envoyer");
                                break;
                            }
                        }
                        break;
                    }
                    else{
                        cnx.envoyer("alias_invalide");
                        break;
                    }
                case "DECLINE":
                    String aliasInv = evenement.getArgument();
                    if(!serveur.invitations.isEmpty())
                    {
                        for(Invitation inv:serveur.invitations){
                            if(
                                    (inv.getAliasInvite().equals(aliasExpediteur) && aliasInv.equals(inv.getAliasHote()))
                                    || (inv.getAliasInvite().equals(aliasInv) && aliasExpediteur.equals(inv.getAliasHote()))
                            )
                            {
                                serveur.invitations.remove(inv);
                                cnx.envoyer("INVITATION_ANNULER");
                                serveur.envoyerA(aliasInv,"DECLINE "+aliasExpediteur);
                                break;
                            }
                        }
                    }
                    else {
                        cnx.envoyer("PAS_D'INVITATIONS");
                    }
                    break;
                case "INV":
                    cnx.envoyer(serveur.voirInvitations(aliasExpediteur));
                    break;
                case "PRV":
                    String[] list = evenement.getArgument().split("\\s+");
                    String alias = list[0];
                    String message = "";

                    for (int i = 1; i<list.length; i++)
                        message+=list[i]+" ";

                    for(SalonPrive sal: serveur.salonsPrives)
                    {
                        if ( (sal.getAliasHote().equals(aliasExpediteur)  && sal.getAliasInviter().equals(alias)) || (sal.getAliasHote().equals(alias) && sal.getAliasInviter().equals(aliasExpediteur)))
                        {
                            serveur.envoyerA(alias,"PRIV "+ aliasExpediteur+ " "+message);
                            cnx.envoyer("MESSAGE_ENVOYER");
                            break;
                        }
                    }
                    break;
                case "QUIT":
                    for(SalonPrive sal: serveur.salonsPrives)
                    {
                        if ( (sal.getAliasHote().equals(aliasExpediteur)  && sal.getAliasInviter().equals(evenement.getArgument())) || (sal.getAliasHote().equals(evenement.getArgument()) && sal.getAliasInviter().equals(aliasExpediteur)))
                        {
                            serveur.salonsPrives.remove(sal);
                            serveur.envoyerA(evenement.getArgument(),"QUIT "+ aliasExpediteur);
                            cnx.envoyer("SALON_QUITTER");
                            break;
                        }
                    }
                    break;
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}