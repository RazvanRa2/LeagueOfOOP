// Copyright (c), 2017, Razvan Radoi, first of his name
package main;

final class Main {

    private Main() { }  // private constructor of Main (coding style)

    public static void main(final String[] args)
    throws Exception, java.io.IOException {

        String filename = args[0];  // citirea datelor de intrare
        fileio.implementations.FileReader filereader =
        new fileio.implementations.FileReader(filename);
        //getting size of game arena
        int n = filereader.nextInt();  // marimi arena de joc
        int m = filereader.nextInt();  // numele variabilelor sunt cele din
        String imput;  // cerinta (n, m, p, r etc.) si de aceea nu sunt unele
        char[][] arena = new char[n][m];  // mai sugestive
        char[] imput2CharArr;  // se determina tipurile de land din arena
        for (int i = 0; i < n; i++) {  // pentru fiecare zona
            imput = filereader.nextWord();
            imput2CharArr = imput.toCharArray();
            for (int j = 0; j < m; j++) {
                arena[i][j] = imput2CharArr[j];
            }
        }
        int p = filereader.nextInt();  // se citeste nr de eroi
        Hero[] heroes = new Hero[p];  // si se initializeaza toti eroii
        int heroX;
        int heroY;
        char heroInitLand;  // si sunt pusi pe "harta" in functie de pozitie
        for (int i = 0; i < p; i++) {  // si clasa din care fac parte
            String heroType = filereader.nextWord();
            heroY = filereader.nextInt();
            heroX = filereader.nextInt();
            heroInitLand = arena[heroY][heroX];
            if (heroType.equals("P")) {
                heroes[i] = new Pyromancer(heroInitLand, heroX, heroY);
            }
            if (heroType.equals("K")) {
                heroes[i] = new Knight(heroInitLand, heroX, heroY);
            }
            if (heroType.equals("W")) {
                heroes[i] = new Wizard(heroInitLand, heroX, heroY);
            }
            if (heroType.equals("R")) {
                heroes[i] = new Rogue(heroInitLand, heroX, heroY);
            }
        }
        int r = filereader.nextInt();  // se citeste numarul de runde
        char[][] movement = new char[r][p];  // si miscarile in fiecare runda
        for (int i = 0; i < r; i++) {
            imput = filereader.nextWord();
            imput2CharArr = imput.toCharArray();
            for (int j = 0; j < p; j++) {
                movement[i][j] = imput2CharArr[j];
            }
        }
        filereader.close();  // finished reading imput

        for (int round = 0; round < r; round++) {  // in fiecare runda
            // mutam jucatorii, daca acestia trebuie mutati
            for (int i = 0; i < p; i++) {
                if (heroes[i].checkAlive()) {  // daca un jucator e viu
                    heroX = heroes[i].getX();
                    heroY = heroes[i].getY();

                    if (movement[round][i] == 'U') {  // si primeste comanda de
                        if (heroes[i].imobilized == 0) {  // mutare
                        heroY--;
                        }
                        heroes[i].move(arena[heroY][heroX], heroX, heroY);
                    } else if (movement[round][i] == 'D') {
                        if (heroes[i].imobilized == 0) {
                        heroY++;
                        }
                        heroes[i].move(arena[heroY][heroX], heroX, heroY);
                    } else if (movement[round][i] == 'L') {
                        if (heroes[i].imobilized == 0) {
                        heroX--;
                        }
                        heroes[i].move(arena[heroY][heroX], heroX, heroY);
                    } else if (movement[round][i] == 'R') {
                        if (heroes[i].imobilized == 0) {
                        heroX++;
                        }
                        // acesta va fi mutat
                        heroes[i].move(arena[heroY][heroX], heroX, heroY);
                    }
                }
            }

            // imediat dupa mutare, jucatorii primesc damage pasiv
            if (round != 0) {  // doar daca e necesar (see getPassiveDamage)
                for (int i = 0; i < p; i++) {  // in Hero.java
                    heroes[i].getPassiveDamage();
                }
            }

            //apoi are loc batalia
            for (int i = 0; i < p - 1; i++) {
                for (int j = i + 1; j < p; j++) { // daca doi jucatori sunt in
                    // acelasi loc pe harta si sunt vii, ei se vor duela
                    if (heroes[i].checkAlive() && heroes[j].checkAlive()) {
                        if (heroes[i].getX() == heroes[j].getX()
                        && heroes[i].getY() == heroes[j].getY()) {
                            // rogue trebuie sa fie mereu primul care ataca
                            // daca nu e primul, facem swap inainte de atac
                            // see README for details
                            if (heroes[j] instanceof Rogue) {
                                heroes[j].attack(heroes[i]);
                                heroes[i].attack(heroes[j]);
                            } else {
                                heroes[i].attack(heroes[j]);
                                heroes[j].attack(heroes[i]);
                            }
                            if (heroes[i].checkAlive()
                            && !heroes[j].checkAlive()) {  // daca isi ucide
                            // oponentul, atacatorul primeste xp
                                heroes[i].getXpPoints(heroes[j].level);
                            }
                            if (!heroes[i].checkAlive()
                            && heroes[j].checkAlive()) {
                                heroes[j].getXpPoints(heroes[i].level);
                            }
                        }
                    }
                }
            }
       }
       // crearea datelor de iesire
       String outFile = args[1];
       fileio.implementations.FileWriter filewriter =
            new fileio.implementations.FileWriter(outFile);

       String output = "";  // pentru fiecare erou generam output
       for (int i = 0; i < p; i++) {
           output = getOutput(heroes[i]);
           filewriter.writeWord(output);
           filewriter.writeNewLine();

       }
       filewriter.close();
    }

    private static String getOutput(final Hero currentHero) {
        // metoda generatoare de output
        String result = "";
        if (currentHero instanceof Pyromancer) {  // tipul eroului
            result = "P ";
        }
        if (currentHero instanceof Knight) {
            result = "K ";
        }
        if (currentHero instanceof Wizard) {
            result = "W ";
        }
        if (currentHero instanceof Rogue) {
            result = "R ";
        }

        if (currentHero.checkAlive()) {  // celelalte date despre el, daca e viu
            result += Integer.toString(currentHero.level);
            result += " ";

            result += Integer.toString(currentHero.xp);
            result += " ";

            result += Integer.toString(currentHero.hp);
            result += " ";

            result += Integer.toString(currentHero.yPoz);
            result += " ";

            result += Integer.toString(currentHero.xPoz);
        } else {
            result += "dead";  // dead, daca e mort
        }

        return result;
    }
}
