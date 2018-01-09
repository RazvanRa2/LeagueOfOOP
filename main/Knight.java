// Copyright (c), 2017, Razvan Radoi, first of his name
package main;

class Knight extends Hero {

    Knight(final char land, final int xPoz, final int yPoz) {
        super(land, xPoz, yPoz);  // initializare knight ca hero
        final int knightInitHp = 900;  // initializare health
        this.hp = knightInitHp;
        this.maxHp = knightInitHp;
    }
    void attack(final Hero opponent) {  // atacul knight
        final float hpLimitCoef = 0.2f;
        final int maxHpDamageLevel = 40;
        final float hundread = 100;
        final float hpAttackCoef = 0.4f;
        float hpLimit = hpLimitCoef * opponent.maxHp;  // determinare hp limit
        if (this.level < maxHpDamageLevel) {
            hpLimit += ((float) this.level / hundread) * opponent.maxHp;
        } else {
            hpLimit += hpAttackCoef * opponent.maxHp;
        }
        boolean execFlag = false;
        float execActiveDamage = 0;  // determinare caz de execution
        if ((float) opponent.hp < hpLimit && opponent.hp > 0) {
            execActiveDamage = opponent.hp;
            execFlag = true;
            int totalDealtDamage = (int) Math.round(execActiveDamage);
            int imobilizedCnt = 1;
            opponent.getActiveDamage(totalDealtDamage);

        } else {  // daca nu este executat adversarul
            final float  initExecDamage = 200;  // se aplica damage-ul general
            final float execLevelModifier = 30;
            execActiveDamage = initExecDamage;
            execActiveDamage += execLevelModifier * this.level;
            // calcul slam damage
            final float initSlamDamage = 100;
            final float slamLevelModifier = 40;
            float slamActiveDamage = initSlamDamage;
            slamActiveDamage += slamLevelModifier * this.level;

            final float rogueCoef1 = 1.15f;
            final float rogueCoef2 = 0.8f;
            if (opponent instanceof Rogue) {  // modificatori in functie de rasa
                execActiveDamage *= rogueCoef1;  // adversarului
                slamActiveDamage *= rogueCoef2;
            }

            final float knightCoef1 = 1.0f;
            final float knightCoef2 = 1.2f;
            if (opponent instanceof Knight) {
                // execActiveDamage stays unchanged
                execActiveDamage *= knightCoef1;
                slamActiveDamage *= knightCoef2;
            }

            final float pyroCoef1 = 1.1f;
            final float pyroCoef2 = 0.9f;
            if (opponent instanceof Pyromancer) {
                execActiveDamage *= pyroCoef1;
                slamActiveDamage *= pyroCoef2;
            }

            final float wizardCoef1 = 0.8f;
            final float wizardCoef2 = 1.05f;
            if (opponent instanceof Wizard) {
                execActiveDamage *= wizardCoef1;
                slamActiveDamage *= wizardCoef2;
            }
            float totalActiveDamage;  // determinare damage total
            if (!execFlag) {
                totalActiveDamage = slamActiveDamage + execActiveDamage;
            } else {
                totalActiveDamage = execActiveDamage;
            }

            final float knightLandModifier = 1.15f;
            if (this.land == 'L' && !execFlag) {  // land modifier aplicat doar
                totalActiveDamage *= knightLandModifier;  // daca adversarul nu
            }  // urmeaza sa fie executat in runda curenta

            int totalDealtDamage = (int) Math.round(totalActiveDamage);
            int imobilizedCnt = 1;
            // aplicarea damage-ului si incapacitarea adversarului
            opponent.getActiveDamage(totalDealtDamage);
            opponent.getImobilized(imobilizedCnt);

        }
    }
}
