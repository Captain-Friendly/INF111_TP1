package com.chat.serveur;

public class SalonPrive {
    private String aliasHote;

    public String getAliasHote() {
        return aliasHote;
    }

    public void setAliasHote(String aliasHote) {
        this.aliasHote = aliasHote;
    }

    public String getAliasInviter() {
        return aliasInviter;
    }

    public void setAliasInviter(String aliasInviter) {
        this.aliasInviter = aliasInviter;
    }

    private String aliasInviter;

    public SalonPrive(Invitation invitation) {
        this.aliasHote = invitation.getAliasHote();
        this.aliasInviter = invitation.getAliasInvite();
    }
}
