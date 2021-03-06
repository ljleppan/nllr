nllr
====

[![Build Status](https://travis-ci.org/ljleppan/nllr.png?branch=master)](https://travis-ci.org/ljleppan/nllr)
[![Coverage Status](https://coveralls.io/repos/ljleppan/nllr/badge.png?branch=master)](https://coveralls.io/r/ljleppan/nllr?branch=master)

Ohjelman ajaminen
===
Ohjelma ajetaan komennolla ```java -jar nllr-1.0.jar``` tai NetBeansin run-komennolla. Mikäli ohjelman ajaminen ei onnistu, voidaan yrittää kääntää ohjelma uudelleen lopumpana olevin ohjein.

Syötteet
===
Ohjelma kysyy käynnistettäessä päivämääräformaattia. Painamalla enter käytetään oletusasetusta joka vastaa projektin ohessa olevien demo-tiedostojen formaattia. Oletusformaatti on ```d-MMM-yyyy```.

Ohjelma kysyy myös esiprosessoijaa. Painamalla enter käytetään oletusarvoista, varsin yksinkertaista esiprosessoijaa. Jos syötteeksi annetaan "snowball", kysytään käyttäjä aineiston kieltä. Painamalla enter saadaan oletuskieli englanti, joka on myös demo-aineiston kieli. Kieleksi kelpaa myös ```Finnish```. Mikäli syötettyä kieltä ei tunnisteta, käytetään oletuskieltä eli englantia.

Ohjelmaa kokeiltaessa on referenssikorpukseksi helpointa syttää polku demo-kansion train.csv-tiedostoon.

Kun ohjelma on käynnissä, voidaan ```corpus``` komennolla syöttää kokonainen testikorpus, josta esimerkkinä toimii demo-kansion test.csv.

Kääntäminen
===
Kääntämistä varten on järkevintä poistaa kommentointi pom.xml-tiedostossa olevan pidemmän kommentoidun koodilohkon ympäriltä, jolloin syntyy .jar-tiedosto joka sisältää riippuvuudet. Juurikansiossa oleva nllr-1.0.jar on luotu näin. Kommentoitu osa voidaan myös jättää rauhaan, mutta tällöin syntynyt tiedosto ei välttämättä ole siirrettävissä tietokoneelta toiselle.
