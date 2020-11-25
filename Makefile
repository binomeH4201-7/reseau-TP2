.PHONY: all, help, cleanCode, initRessources, clean
JC = javac
JE = java
JCP = -classpath ./bin
JOPT = -d ./bin -sourcepath ./src/http/ $(JCP)

SOURCES = ${wildcard src/http/client/*.java}
SOURCES += ${wildcard src/http/server/*.java}
CLASSES = ${SOURCES:src/%.java=bin/%.class}

SERVER = http.server.WebServer
CLIENT = http.client.WebPing

DEFAULT_PORT = 3000

port ?= $(DEFAULT_PORT)

all : $(CLASSES)
	@echo "Le projet est compilé"

all_no_errors: 
	@make bin/http/server/UnsupportedMediaTypeException.class 
	@make bin/http/server/Request.class 
	@make bin/http/server/Response.class 
	@make bin/http/server/RequestHandler.class 
	@make bin/http/server/WebServer.class 
	@echo "Le projet est compilé"

bin/%.class : src/%.java
	@echo "compilation de <$<>"
	@$(JC) $(JOPT) $<

cleanCode :
	@echo "Suppression des fichiers compilés."
	@rm bin/http/server/*.class -vf
	@rm bin/http/client/*.class -vf

initRessources:
	@echo "Initialisation des ressources."
	@rm ./ressources/* -vf
	@cp ./backup/* ./ressources/ -vf

clean : ressources cleanCode initRessources

start-server : all
	@$(JE) $(JCP) $(SERVER) $(port)
	
help :
	@echo "======================================================="
	@echo "Pour compiler l'application :"
	@echo "     make"
	@echo "======================================================="
	@echo "Port par défaut du serveur: $(DEFAULT_PORT)"
	@echo "Pour lancer le serveur sur un autre port :"
	@echo "     make start-server port=<numero-port>"
	@echo "========================================================"
	@echo "Pour nettoyer les dossiers :"
	@echo "     make clean"
	@echo "/!\ Cette commande supprime éxecutables et initialise "
	@echo "les ressources."
	@echo "Pour initialiser uniquement les ressources :"
	@echo "     make initRessources"
	@echo "Pour supprimer uniquement les éxecutables :"
	@echo "     make cleanCode"
	@echo "========================================================="
	@echo "Pour plus d'information quant à l'utilisation se référer"
	@echo "au fichier README.md ainsi qu'au contenu du dossier doc/"
	@echo "========================================================="
