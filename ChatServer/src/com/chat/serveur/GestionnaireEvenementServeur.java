package com.chat.serveur;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import com.echecs.PartieEchecs;
import com.echecs.Position;

import java.util.ArrayList;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
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
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                {
                    cnx.envoyer("END");
                    serveur.enlever(cnx);
                    cnx.close();
                    break;
                }

                case "LIST": //Envoie la liste des alias des personnes connectées :
                {
                    cnx.envoyer("LIST " + serveur.list());
                    break;
                }

                case "MSG":
                {
                    serveur.envoyerATousSauf(evenement.getArgument(),aliasExpediteur);
                    break;
                }
                case "HIST":
                {
                    cnx.envoyer("Historique\n");
                    cnx.envoyer(serveur.historique());
                    break;
                }
                case "JOIN":
                {
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
                                    serveur.envoyerA(aliasInvite, "CREATED Salon cree avec "+aliasExpediteur);
                                    aSalon=true;
                                    break;
                                }
                            }
                            if(!aSalon)
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
                }

                case "DECLINE":
                {
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
                }
                case "INV":
                {
                    cnx.envoyer(serveur.voirInvitations(aliasExpediteur));
                    break;
                }
                case "PRV":
                {
                    String[] list = evenement.getArgument().split("\\s+");
                    String alias = list[0];
                    String message = "";
                    boolean envoyer = false;

                    for (int i = 1; i<list.length; i++)
                        message+=list[i]+" ";

                    for(SalonPrive sal: serveur.salonsPrives)
                    {
                        if ( (sal.getAliasHote().equals(aliasExpediteur)  && sal.getAliasInviter().equals(alias)) || (sal.getAliasHote().equals(alias) && sal.getAliasInviter().equals(aliasExpediteur)))
                        {
                            serveur.envoyerA(alias,"PRIV "+ aliasExpediteur+ " "+message);
                            cnx.envoyer("MESSAGE_ENVOYER");
                            envoyer = true;
                            break;
                        }
                    }
                    if (!envoyer)
                        cnx.envoyer("Pas de Salon priver avec cette alias");
                    break;
                }
                case "QUIT":
                {
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
                }
                case "CHESS":
                {
                    String aliasInvite = evenement.getArgument();
                    boolean enPartie = false;
                    for(SalonPrive sal: serveur.salonsPrives) // check si il est deja en partie
                    {
                        if(sal.getAliasHote().equals(aliasExpediteur) || sal.getAliasInviter().equals(aliasExpediteur))
                            if (sal.getPartieEchecs() != null)
                            {
                                enPartie = true;
                            }

                    }

                    if (!aliasInvite.equals(aliasExpediteur)  && serveur.aliasExiste(aliasInvite) && !enPartie ) // check si le alias
                    {
                        SalonPrive salon = serveur.getSalonPrive(aliasExpediteur, aliasInvite);

                        if (salon != null)
                        {
                            if(serveur.invitationsEchec.isEmpty())
                            {
                                serveur.invitationsEchec.add(new Invitation(aliasExpediteur, aliasInvite));
                                serveur.envoyerA(aliasInvite, "CHESS "+aliasExpediteur);
                                cnx.envoyer("INVITATION_ECHEC_ENVOYER");
                            }
                            else
                            {
                                for(Invitation inv:serveur.invitationsEchec){
                                    if(
                                            (aliasExpediteur.equals(inv.getAliasInvite()) && aliasInvite.equals(inv.getAliasHote()))
                                    )
                                    {
                                        PartieEchecs partie = new PartieEchecs();
                                        partie.setAliasJoueur1(aliasExpediteur);
                                        partie.setAliasJoueur2(aliasInvite);
                                        if (Math.round( Math.random() ) > 0)
                                        {
                                            partie.setCouleurJoueur1('b');
                                            partie.setCouleurJoueur2('n');
                                        }
                                        else
                                        {
                                            partie.setCouleurJoueur1('n');
                                            partie.setCouleurJoueur2('b');
                                        }
                                        salon.setPartieEchecs(partie);
                                        serveur.invitationsEchec.remove(inv);
                                        serveur.envoyerA(aliasExpediteur,"CHESSOK "+partie.getCouleurJoueur1());
                                        serveur.envoyerA(aliasInvite,"CHESSOK "+partie.getCouleurJoueur2());
                                        break;
                                    }
                                }
                            }
                        }
                        else
                        {
                            cnx.envoyer("Pas de salon avec cette alias");
                        }
                    }
                    else{
                        if (enPartie)
                        {
                            cnx.envoyer("Partie déjà en jeux");
                        }
                        else {
                            cnx.envoyer("alias_invalide");
                        }
                    }
                    break;
                }
                case "MOVE":
                {
                    //check if right color
                    String [] moves = evenement.getArgument().split("-");
                    SalonPrive salon = serveur.getPartieEchec(aliasExpediteur);

                    if (salon != null)
                    {
//                        System.out.println("moves:0-0:"+moves[0].charAt(0)+"\tmoves:0-1:"+moves[0].charAt(1)+"\nmoves:1-0:"+moves[1].charAt(0)+"\tmoves:1-1:"+moves[1].charAt(1));
                        if (salon.getPartieEchecs().deplace(
                                new Position(moves[0].charAt(0), (byte)Character.getNumericValue(moves[0].charAt(1))),
                                new Position(moves[1].charAt(0), (byte)Character.getNumericValue(moves[1].charAt(1)))))
                        {
//                            cnx.envoyer("Mouvement valide");
                            serveur.envoyerA(aliasExpediteur,"MOVE "+salon.getPartieEchecs().getBoard());
                            serveur.envoyerA(salon.getAliasInviter(),"MOVE "+salon.getPartieEchecs().getBoard());
                        }
                        else{
                            cnx.envoyer("Mouvement invalide");
                        }
                        break;
                    }
                    cnx.envoyer("Salon n'existe pas");
                    break;
                }
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
                    break;
            }
        }
    }
}























