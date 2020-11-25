.PHONY: all, help, cleanCode, clean, ressources

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

bin/%.class : src/%.java
	@echo "compilation de <$<>"
	@$(JC) $(JOPT) $<

cleanCode :
	@echo "Suppression des fichiers compilés."
	@rm $(CLASSES) -vf

ressources :
	@echo "Initialisation des fichiers ressources"
	@rm ./ressources/* -vf
	@cp ./backup/* ./ressources/ -vf

clean : cleanCode, ressources 

start-server : all
	@$(JE) $(JCP) $(SERVER) $(port)
	
help :
	@echo "======================================================="
	@echo "TO DO"
	@echo "========================================================="
