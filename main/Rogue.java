// Copyright (c), 2017, Razvan Radoi, first of his name
package main;

class Rogue extends Hero {
    protected int backstabCnt;

    Rogue(final char land, final int xPoz, final int yPoz) {
        super(land, xPoz, yPoz);  // initializarea rogue ca hero
        final int initMaxHp = 600;  // initializarea hp-ului lui rogue
        this.hp = initMaxHp;
        this.maxHp = initMaxHp;
        this.backstabCnt = 0;  // numara de cate ori ataca rouge
    }

    void attack(final Hero opponent) {  // atacul rogue
        //backstab damage
        final float bstbIinit = 200;  // initial damage
        final float bstbModifier = 20;
        final float bstbCriticalCoef = 1.5f;
        final float criticalCase = 3;
        float bstbActiveDamage = bstbIinit + bstbModifier * this.level;
        if (backstabCnt % criticalCase == 0) { // daca e o a treia lovitura
            if (this.land == 'W') {  // pe un teren Woods
                bstbActiveDamage *= bstbCriticalCoef;  // atunci critical hit
            }
        }
        this.backstabCnt++;  // se incrementeaza nr de lovituri date pentru a
        // determina urmatoarele dati cand rogue va da critical hit

        // paralysis damage
        final float initPara = 40;  // initial damage
        final float paraModifier = 10;
        final int basicRoundsAffected = 3;
        final int maxRoundsAffected = 6;
        float paraActiveDamage = initPara + paraModifier * this.level;
        float paraPassiveDamage = initPara + paraModifier * this.level;
        int roundsAffected = basicRoundsAffected;  // in cazul general, 3 runde
        if (this.land == 'W') {  // modificator de tip land pentru hit pasiv
            roundsAffected = maxRoundsAffected;  // pe teren woods -> 6 runde
        }

        final float bstbPyroCoef = 1.25f;  // modificatori in functie de clasa
        final float paraPyroCoef = 1.2f;  // din care face parte oponentul
        if (opponent instanceof Pyromancer) {
            bstbActiveDamage *= bstbPyroCoef;
            paraActiveDamage *= paraPyroCoef;
            paraPassiveDamage *= paraPyroCoef;
        }
        final float bstbKnightCoef = 0.9f;
        final float paraKnightCoef = 0.8f;
        if (opponent instanceof Knight) {
            bstbActiveDamage *= bstbKnightCoef;
            paraActiveDamage *= paraKnightCoef;
            paraPassiveDamage *= paraKnightCoef;
        }
        final float bstbWizardCoef = 1.25f;
        final float paraWizardCoef = 1.25f;
        if (opponent instanceof Wizard) {
            bstbActiveDamage *= bstbWizardCoef;
            paraActiveDamage *= paraWizardCoef;
            paraPassiveDamage *= paraWizardCoef;
        }
        final float bstbRogueCoef = 1.2f;
        final float paraRogueCoef = 0.9f;
        if (opponent instanceof Rogue) {
            bstbActiveDamage *= bstbRogueCoef;
            paraActiveDamage *= paraRogueCoef;
            paraPassiveDamage *= paraRogueCoef;
        }

        final float rogueLandModifier = 1.15f;  // modificator de tip land
        if (this.land == 'W') {
            bstbActiveDamage *= rogueLandModifier;
            paraActiveDamage *= rogueLandModifier;
            paraPassiveDamage *= rogueLandModifier;
        }
        // calcul final al damage-ului activ si pasiv
        bstbActiveDamage = Math.round(bstbActiveDamage);
        paraActiveDamage = Math.round(paraActiveDamage);
        float totalActiveDamage = bstbActiveDamage + paraActiveDamage;

        int dealtActiveDamage = (int) Math.round(totalActiveDamage);
        int dealtPassiveDamage = (int) Math.round(paraPassiveDamage);
        // aplicarea damage-ului pasiv si activ + incapacitarea adversarului
        opponent.getActiveDamage(dealtActiveDamage);
        opponent.setPassiveDamage(roundsAffected, dealtPassiveDamage);
        opponent.getImobilized(roundsAffected);
    }
}
