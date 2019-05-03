# meldApp

De app maakt het mogelijk voor klanten (hierna genoemd: melder) van servicebedrijven direct in contact te komen met een servicemonteur (hierna genoemd: behandelaar). Op deze wijze worden problemen bij klanten sneller opgelost wat de winstgevendheid kan bevorderen en de klanttevredenheid verbeterd.

Android app ten behoeve van de eindopdracht CPP java/ android programmeur bij de Open Universiteit Nederland. (december 2018). De App is gemaakt in groepsverband met vier personen. Er is gebruikgemaakt van <b>Slack</b> voor regelmatig overleg en <b>Gitlab</b> voor versiebeheer. Voor de database wordt gebruikgemaakt van de Firebase realtime database (Google). Voor het verzenden van notificaties is gebruik gemaakt van een externe server die een luisteraar heeft op de database. 

## Getting Started

De code kan via android studio gecompileerd worden op een android toestel via usb of kan gedraaid worden op een emulator. De server voor het verzenden van notificaties was alleen online tijdens het project. De firebase database is momenteel (nog) functioneel (juni 2019).

### Prerequisites

Om de app te kunnen draaien is android studio vereist.

Voor het aanmelden is gebruikt van Google authentication om de gebruiker te kunnen verifieren. Deze wordt bij de aanmelding ook geverifieerd door de administrator. Wanneer de gebruiker voorkomt als valide gebruiker dan kan deze de app gebruiken.
![](img src="aanmeldscherm.png" width="150px")

Een gebruiker die geregisteerd is als melder kan een melding aanmaken.
![](img src="aanmakenmelding.png" width="150px")

Deze melding komt dan te staan in het scherm overzicht meldingen. Wanneer een behandelaar de melding afhandeld verschuift de melding naar gesloten en dan kan deze bevestigen dat de melding naar wens is afgehandeld.
![](img src="overzichtmeldingen.png" width="150px")

Wanneer een melding is gesloten kan de melder daarvan een notificatie ontvangen indien deze dat wil.
![](img src="notificaties.png" width="150px")

## Running the tests

testen zijn steekproefsgewijs toegepast op klassen in de diverse domeinlagen. De datalaag test of data juist wordt toegevoegd in de firebase database.

### Architectuur

Er is gekozen om de app op te bouwen uit diverse domeinlagen. De datalaag zorgt voor de interactie met de database. De domeinlaag bevat de feitelijke klassen zoals de melding, de aantekening en o.a. enums voor de status en de rol van de gebruiker. De view laag bevat de activities en zorgt voor interactie met de gebruiker. Het viewmodel ondersteund de activities en zorgt voor de inteactie met de datalaag.

## Built With

* Android Studio
* Firebase
* Java

## Versioning

Versiebeheer is verwijderd ivm met de privacy van de studenten.

## Authors

anoniem


This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

