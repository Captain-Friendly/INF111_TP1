package com.chat.serveur;

public class Invitation {
    public String getAliasHote() {
        return aliasHote;
    }

    public void setAliasHote(String aliasHote) {
        this.aliasHote = aliasHote;
    }

    public String getAliasInvite() {
        return aliasInvite;
    }

    public void setAliasInvite(String aliasInvite) {
        this.aliasInvite = aliasInvite;
    }

    private String aliasHote;
    private String aliasInvite;
    public Invitation(String aliasHote, String aliasInvite)
    {
        this.aliasHote = aliasHote;
        this.aliasInvite = aliasInvite;
    }
}
