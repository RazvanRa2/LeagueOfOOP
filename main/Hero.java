// Copyright (c), 2017, Razvan Radoi, first of his name
package main;
// clasa hero contine lucrurile specifice oricarui erou din joc, indiferent de
// tipul acestuia
class Hero {
    protected int xp;  // variablie ce tin de xp
    protected int level;

    protected int hp;  // variabile ce tin de hp
    protected int maxHp;
    protected boolean killed;

    protected char land;  // variabile ce tin de pozitionarea eroului
    protected int xPoz;
    protected int yPoz;

    protected int passiveDamage;  // variabile ce tine de abilitati
    protected int passiveCnt;  // si de batalii
    protected int imobilized;

    Hero(final char landType, final int xPoz, final int yPoz) {
        this.xp = 0;  // initializarea variabilelor unui erou
        this.level = 0;

        this.land = landType;
        this.xPoz = xPoz;
        this.yPoz = yPoz;

        this.passiveDamage = 0;
        this.passiveCnt = 0;
        this.imobilized = 0;
    }

    void getXpPoints(final int opponentLevel) {  // primirea xp points
        final int maxXpPoints = 200;
        final int maxXpCoef = 40;
        this.xp += Math.max(0, maxXpPoints - (this.level - opponentLevel)
        * maxXpCoef);  // calcul al nr max de puncte xp primite

        int localLevel = 0;  // de fiecare data cand un erou primeste xp
        int localXp = this.xp;  // recalculam level-ul sau pentru a determina
        final int maxGeneralLocalXp = 50;  // daca acesta a facut level up
        final int maxFirstLocalXpLimit = 250;
        while (localXp >= maxGeneralLocalXp) {
            if (localLevel == 0) {
                if (localXp >= maxFirstLocalXpLimit) {  // daca are level 0
                    localXp -= maxFirstLocalXpLimit;
                    localLevel++;  // si sunt indeplinite cond., level up
                } else {  // altfel, daca suntem pe cazul general
                    localXp = 0; // cazul in care eroul nu ajunge la level 1
                }
            } else {
                    localXp -= maxGeneralLocalXp;  // se face level up
                    localLevel++;
            }
        }

        final int pyroModifier = 50;  // cresterea max hp la level up
        final int knightModifier = 80;  // dupa fiecare tip de erou
        final int wizardModifier = 30;
        final int rogueModifier = 40;
        if (localLevel > this.level) {
            if (this instanceof Pyromancer) {
                this.hp = this.maxHp + pyroModifier * localLevel;
            }
            if (this instanceof Knight) {
                this.hp = this.maxHp + knightModifier * localLevel;
            }
            if (this instanceof Wizard) {
                this.hp = this.maxHp + wizardModifier * localLevel;
            }
            if (this instanceof Rogue) {
                this.hp = this.maxHp + rogueModifier * localLevel;
            }
        }
        this.level = localLevel;
    }

    int getX() {  // getter x position
        return this.xPoz;
    }

    int getY() {  // getter y position
        return this.yPoz;
    }

    boolean checkAlive() {  // verifica daca eroul e inca in joc
        return !(this.killed);
    }

    void move(final char newLand, final int newX, final int newY) {
        if (checkAlive()) {  // daca eroul este viu
            if (this.imobilized == 0) {  // si nu a fost imobilizat anterior
                this.land = newLand;  // acesta va fi mutat
                this.xPoz = newX;
                this.yPoz = newY;
            } else {
                this.imobilized--;  // altfel scade nr de runde imobilizate
            }
        }
    }

    void getImobilized(final int rounds) {  // hero devine imobilizat
        this.imobilized = rounds;  // pentru un nr de runde (rounds)
    }

    void getActiveDamage(final int activeDamage) {  // hero primeste un damage
        this.hp -= activeDamage;  // activ, adica damage in runda curenta
        if (this.hp <= 0) {  // si daca este omorat
            this.killed = true;  // marcam acest lucru corespunzator
        }
    }

    void getPassiveDamage() {  // hero primeste un damage pasiv
        if (this.checkAlive()) {  // daca este viu
            if (this.passiveCnt != 0) {  // si daca trebuie sa primeasca
                this.hp -= this.passiveDamage;  // atunci primeste
                if (this.hp <= 0) {  // si daca este omorat pasiv
                    this.killed = true;  // marcam acest lucru corespunzator
                }
                this.passiveCnt--;
            }
        }
    }

    void setPassiveDamage(final int newCnt, final int newPassiveDamage) {
        this.passiveCnt = newCnt;  // adaugam un nou tip de pasive damage
        this.passiveDamage = newPassiveDamage;  // primit in urma unei batalii
    }

    void attack(final Hero opponent) { }  // dummy pentru clasele ce extind hero
}
