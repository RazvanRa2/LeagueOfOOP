// Copyright (c), 2017, Razvan Radoi, first of his name
package main;

class Wizard extends Hero {
    Wizard(final char land, final int xPoz, final int yPoz) {
        super(land, xPoz, yPoz);  // initializam wizard ca hero

        final int initHealth = 400;  // apoi initializam health-ul acestuia
        this.maxHp = initHealth;
        this.hp = initHealth;
    }

    void attack(final Hero opponent) {  // atacul wizard
        //drain attack - calcul initial al atacului
        final float initDrainPerc = 20;
        final float perLevelPerc = 5;
        final float maxmHpCoef = 0.3f;

        final float pyromancerCoef = 0.9f;
        final float knightCoef = 1.2f;
        final float wizardCoef = 1.05f;
        final float rogueCoef = 0.8f;

        float drainPerc = initDrainPerc + perLevelPerc * this.level;
        float baseHp = (float) Math.min(maxmHpCoef * (float) opponent.maxHp,
        (float) opponent.hp);
        if (opponent instanceof Pyromancer) {  // aplicarea modificatorilor in
            drainPerc *= pyromancerCoef;
        }
        if (opponent instanceof Knight) {  // functie de tipul eroului
            drainPerc *= knightCoef;
        }
        if (opponent instanceof Wizard) {  // care va fi atacat
            drainPerc *= wizardCoef;
        }
        if (opponent instanceof Rogue) {
            drainPerc *= rogueCoef;
        }

        final float hundread = 100;
        drainPerc /= hundread;  // these checksyle errors are ridiculous

        float drainDamage = baseHp * drainPerc;
        final float landCoef = 1.1f;
        if (this.land == 'D') {  // aplicarea modificatorului de tip land
            drainDamage *= landCoef;
        }
        drainDamage = Math.round(drainDamage);
        //deflect attack
        final float initDeflectPerc = 35;
        final float perLevelDeflect = 2;
        final float maxPerc = 70;
        float deflectPerc = initDeflectPerc + this.level * perLevelDeflect;
        if (deflectPerc > maxPerc) {
            deflectPerc = maxPerc;
        }
        deflectPerc /= hundread;

        float opponentAbilityPower = 0;
        if (opponent instanceof Pyromancer) {  // drain damage daca e atacat
            final float initPyroPwr1 = 350;  // un pyromancer
            final float initPyroPwr2 = 150;
            final float perLevelPyro1 = 50;
            final float perLevelPyro2 = 30;
            float power1 = initPyroPwr1 + perLevelPyro1 * opponent.level;
            float power2 = initPyroPwr2 + perLevelPyro2 * opponent.level;

            final float landPyroCoef = 1.25f;
            if (this.land == 'V') {
                power1 *= landPyroCoef;
                power2 *= landPyroCoef;
            }
            power1 = Math.round(power1);
            power2 = Math.round(power2);
            opponentAbilityPower = power1 + power2;

        }
        if (opponent instanceof Knight) {  // drain damage daca e atacat knight
            //calculating hp limit
            opponentAbilityPower = 0;
            final float knightHpCoef = 0.2f;
            final float maxKnightLevel = 40;
            final float knightMaxHpDamageCoef = 0.4f;
            float hpLimit = knightHpCoef * (float) this.maxHp;
            if (opponent.level < maxKnightLevel) {
                hpLimit += ((float) opponent.level / (float) hundread)
                * (float) this.maxHp;
            } else {
                hpLimit += knightMaxHpDamageCoef * (float) this.maxHp;
            }
            //checking if knight will execute wizard
            boolean execFlag = false;
            if ((float) this.hp < hpLimit && this.hp > 0) {
                opponentAbilityPower = this.hp;
                execFlag = true;
            } else {
                final float knightInitPwr1 = 200;
                final float knightInitPwr2 = 100;
                final float knightPerLevelPwr1 = 30;
                final float knightPerLevelPwr2 = 40;
                opponentAbilityPower = knightInitPwr1
                + knightPerLevelPwr1 * opponent.level;
                opponentAbilityPower += knightInitPwr2
                + knightPerLevelPwr2 * opponent.level;
            }
            final float knightLandModifier = 1.15f;
            if (this.land == 'L' && !execFlag) {
                opponentAbilityPower *= knightLandModifier;
            }

        }
        if (opponent instanceof Rogue) {  // drain damage daca e atacat rogue
            opponentAbilityPower = 0;
            final float rogueInitAbility1 = 200;
            final float roguePerLevelAbility1 = 20;
            opponentAbilityPower = rogueInitAbility1
            + roguePerLevelAbility1 * opponent.level;

            final float criticalHitCaseCnt = 3;
            final float criticalHitAmplifier = 1.5f;
            if (this.land == 'W' && (((Rogue) opponent).backstabCnt - 1)
            % criticalHitCaseCnt == 0) {
                opponentAbilityPower *= criticalHitAmplifier;
            }
            final float rogueInitAbility2 = 40;
            final float roguePerLevelAbility2 = 10;
            opponentAbilityPower += rogueInitAbility2
            + rogueInitAbility2 * opponent.level;

            final float rogueLandModifier = 1.15f;
            if (this.land == 'W') {
                opponentAbilityPower *= rogueLandModifier;
            }

        }

        final float attackingPyroModifier = 1.3f;
        final float attackingKnightModifier = 1.4f;
        final float attackingRogueModifier = 1.2f;
        if (opponent instanceof Pyromancer) {  // modificatori in functie de
            deflectPerc *= attackingPyroModifier;  // clasa tintei
        }
        if (opponent instanceof Knight) {
            deflectPerc *= attackingKnightModifier;
        }

        if (opponent instanceof Rogue) {
            deflectPerc *= attackingRogueModifier;
        }

        float totalDeflectPower = deflectPerc * opponentAbilityPower;
        final float wizardLandCoef = 1.1f;
        if (this.land == 'D') {  // modificator land type
            totalDeflectPower *= wizardLandCoef;
        }
        // calcul final al damage-ului activ
        totalDeflectPower = Math.round(totalDeflectPower);
        float totalActiveDamage = totalDeflectPower + drainDamage;
        // aplicarea damage-ului
        int totalDealtDamage =  (int) Math.round(totalActiveDamage);
        opponent.getActiveDamage(totalDealtDamage);
    }
}
