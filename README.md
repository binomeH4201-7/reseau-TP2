# TP 2 : Serveur HTTP

## Introduction
Ce projet est fourni avec un Makefile qui permet de compiler et exécuter le projet en ligne de commande. Il est également possible de réaliser ces actions via un IDE. Les principales commandes sont :
  `all : génère les éxécutables
  clean : supprime les éxécutables et réinitialise les ressources
  ressources : initialise les ressources serveurs
  start-serveur : lance le serveur
  help : affiche une aide sommaire`

## I/Compiler le projet
La commande `make` permet de compiler le projet en ligne de commande. En cas de problème, il faut utiliser la commande suivante :
  `make all_no_errors`

## II/Lancer le serveur
La commande `make start-server` permet de lancer le serveur. Par défaut, le port est le port 3000. Il est possible de préciser un port avec la commande :
  `make start-server port=<numero-port>`

## III/Faire des requêtes
Pour faire des requêtes, il faut utiliser un navigateur web (Firefox fonctionne bien). La page `index.html` illustre quelques fonctions principales, mais on peut explorer plus les capacités du serveur en utilisant des outils comme PostMan. Nous avons utilisé l'extension Firefox RESTer.
