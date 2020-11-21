.PHONY: all, help, cleanCode, clean

JC = javac
JE = java
JCP = -classpath ./bin/
JOPT = -d ./bin -sourcepath ./src/http/*

SOURCES = ${wildcard src/http/*.java}
CLASSES = ${SOURCES:src/http/%.java=bin/%.class}
HISTORY = ${wildcard history/*.save}

SERVER = server/WebServer
CLIENT = client/WebPing
DEFAULT_PORT = 5100

port ?= $(DEFAULT_PORT)

all : $(CLASSES)
	@echo "Le projet est compilé"

bin/%.class : src/http/%.java
	@echo "compilation de <$<>"
	@$(JC) $(JOPT) $<

cleanCode :
	@echo "Suppression des fichiers compilés."
	@rm ./bin/*.class -vf

clean : cleanCode 

start-client : all
	$(JE) $(JCP) $(CLIENT)

start-server : all
	@echo "Serveur lancé sur le port $(port)"
	@$(JE) $(JCP) $(SERVER) $(port)
	
help :
	@echo "======================================================="
	@echo "TO DO"
	@echo "========================================================="
