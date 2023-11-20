package com.chat.client;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;

/**
 * Cette classe représente un gestionnaire d'événement d'un client. Lorsqu'un client reçoit un texte d'un serveur,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementClient implements GestionnaireEvenement {
    private ClientChat client;

    /**
     * Construit un gestionnaire d'événements pour un client.
     *
     * @param client Client Le client pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementClient(ClientChat client) {
        this.client = client;
    }
    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un serveur.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String typeEvenement, arg;
        String[] membres;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                case "END" : //Le serveur demande de fermer la connexion
                {
                    client.deconnecter(); //On ferme la connexion
                    break;
                }

                case "LIST" : //Le serveur a renvoyé la liste des connectés
                {
                    arg = evenement.getArgument();
                    membres = arg.split(":");
                    System.out.println("\t\t"+membres.length+" personnes dans le salon :");
                    for (String s:membres)
                        System.out.println("\t\t\t- "+s);
                    break;
                }

                case "JOIN": // quelqu'un a envoyer une invitation
                {
                    System.out.println("\t\t"+evenement.getArgument()+" vous a inviter a un salon priver");
                    break;
                }
                case "CREATED":
                {
                    System.out.println("\t\t\t"+evenement.getArgument());
                    break;
                }

                case "DECLINE":
                {
                    System.out.println("\t\t"+evenement.getArgument()+" a canceller l'invitation");
                    break;
                }

                case "PRIV":
                {
                    String[] list = evenement.getArgument().split("\\s+");
                    String alias = list[0];
                    StringBuilder msg = new StringBuilder();
                    for (int i = 1; i<list.length; i++)
                        msg.append(list[i]).append(" ");
                    System.out.println("\t\t\t.MSG_PRV("+alias+"): "+ msg);
                    break;
                }

                case "CHESS":
                {
                    String[] list = evenement.getArgument().split("\\s+");
                    String alias = list[0];
                    System.out.println("\t\t\t"+alias+" vous invite a une partie d'Echec");
                    break;
                }
                case "CHESSOK":
                {

                    client.nouvellePartie();
                    System.out.println("\t\t\t CHESSOK "+evenement.getArgument());
                    System.out.println(client.getEtatPartieEchecs().toString());
                    break;
                }

                case "MOVE":
                {
                    System.out.println("_____________________________________");
                    client.setEtatPartieEchecs(evenement.getArgument());
                    System.out.println(client.getEtatPartieEchecs().toString());
                    break;
                }
                case "QUIT":
                {
                    System.out.println("\t\t"+evenement.getArgument()+" est sorti du salon priver");
                    break;
                }

                //Ajoutez ici d’autres case pour gérer d’autres commandes du serveur.
                default: //Afficher le texte recu :
                {
                    System.out.println("\t\t\t."+evenement.getType()+" "+evenement.getArgument());
                }

            }
        }
    }
}
