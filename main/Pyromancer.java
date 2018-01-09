// Copyright (c), 2017, Razvan Radoi, first of his name
package main;

class Pyromancer extends Hero {

    Pyromancer(final char land, final int xPoz, final int yPoz) {
        super(land, xPoz, yPoz);  // initializam pyromancer ca hero
        final int initMaxHp = 500;  // apoi ii initializam viata
        this.hp = initMaxHp;
        this.maxHp = initMaxHp;
    }

    void attack(final Hero opponent) {  // metoda de atac a lui pyromancer
          //fireBlast (active damage only)
          final float initFbDamage = 350;  // calcul damage fireblast
          final float fbDamageModifier = 50;
          float fbActiveDamage = initFbDamage;
          fbActiveDamage += fbDamageModifier * this.level;

          //ignite (active damage)
          final float initIgniteDamage = 150;  // calcul damage ignite
          final float igniteDamageModifier = 20;
          final float initIgnitePassive = 50;
          final float ignitePassiveModifier = 30;
          float igActiveDamage = initIgniteDamage;
          igActiveDamage += igniteDamageModifier * this.level;
          //ignite (pasive damage)
          float igPassiveDamage = initIgnitePassive;
          igPassiveDamage += ignitePassiveModifier * this.level;

         // aplicarea modificatorilor specifici clasei din care face parte
         // cel care este atacat
         final float rogueGeneralCoef = 0.8f;
         if (opponent instanceof Rogue) {
              fbActiveDamage *= rogueGeneralCoef;
              igActiveDamage *= rogueGeneralCoef;
              igPassiveDamage *= rogueGeneralCoef;
         }
         final float knightGeneralCoef = 1.2f;
         if (opponent instanceof Knight) {
              fbActiveDamage *= knightGeneralCoef;
              igActiveDamage *= knightGeneralCoef;
             igPassiveDamage *= knightGeneralCoef;
         }
         final float pyroGeneralCoef = 0.9f;
         if (opponent instanceof Pyromancer) {
              fbActiveDamage *= pyroGeneralCoef;
              igActiveDamage *= pyroGeneralCoef;
              igPassiveDamage *= pyroGeneralCoef;
         }
         final float wizardGeneralCoef = 1.05f;
         if (opponent instanceof Wizard) {
              fbActiveDamage *= wizardGeneralCoef;
              igActiveDamage *= wizardGeneralCoef;
              igPassiveDamage *= wizardGeneralCoef;
         }

          float totalActiveDamage = Math.round(fbActiveDamage + igActiveDamage);
          igPassiveDamage = Math.round(igPassiveDamage);

          // aplicarea modificatorului dat de terenul pe care se duce batalia
          final float pyroLandCoef = 1.25f;
          if (this.land == 'V') {
              totalActiveDamage *= pyroLandCoef;
              igPassiveDamage *= pyroLandCoef;
          }
          // calcul final al damage-ului pasiv si activ dat
          int dealtActiveDamage = (int) Math.round(totalActiveDamage);
          int totalPassiveDamage = (int) Math.round(igPassiveDamage);
          int roundsAffected = 2;
          // aplicarea damage-ului activ si pasiv (pentru un nr de runde)
          opponent.getActiveDamage(dealtActiveDamage);
          opponent.setPassiveDamage(roundsAffected, totalPassiveDamage);
     }
}
