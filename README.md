### Prérequis

* npm version : 11.2.0
* node version : v22.14.0
* angular version : 19.2.6
* kotlin version : 2.1.20
* ktor version : 3.1.1
* mongodb version : 5.2.0

Il est néssaire d'avoir mongoDB installé en local.

## Frontend

Le frontend écrit en Angular, fournit une interface graphique permettant de :

  * Créer une nouvelle partie de poker
  * Ajouter un joueur à une partie
  * Mettre à jour le score d'un joueur
  * Afficher les parties existantes
  * Afficher le classement des joueurs d'une partie

Pour une expérience fluide, commencez par créer une nouvelle partie, puis récupérer la partie à partir du bouton présent en haut de page. 
Pour afficher le classement, selectionnez la partie en cliquant sur celle-ci.
Lors de l'ajout d'un joueur ou de la modification du score d'un joueur, assurez vous que la partie correspondante est selectionée.

Pour lancer le frontend Angular, vous pouvez exécuter lancer le script suivant depuis le dossier racine du projet  : 
```bash
./start-frontend.sh
```

## backend

Le backend basé sur Ktor répond à l'url `http://127.0.0.1:8080` fournit les endpoints suivant : 
  * `/get-poker-game` : pour avoir la liste de toutes les parties ou `/get-poker-game/{pokerGameId}` :  pour recevoir les données d'une partie spécifique
  * `/create-poker-game` : qui nécessite l'envoie d'un objet *PokerGame(Id : string, name : String, players : [])* dans le corps de la requête, le seul champ obligatoire est le nom de la partie
  * `/delete-poker-game/{pokerGameIId}` : pour supprimer une partie
  * `/add-player/{pokerGameId}` : pour ajouter un joueur à une partie. Nécessite l'envoie d'un objet *Player(id : string, username : string, score : Int)* dans le corps de la requête, le seul champ obligatoire est username
  * `/update-player/poker-game/player/score?pokerGameId={pokerGameId}&playerId={playerId}` : nécessite l'envoie du score dans le corps de la requête
  * `/get-player-ranking?pokerGameId={pokerGameId}` : pour obtenir le classement des joueurs sur une partie
  * `/get-player-info?pokerGameId={pokerGameId}&playerId={playerId}` : pour obtenir les informations d'un joueur.

Pour lancer le backend, exécutez le script bash suivant depuis le dossier racine du projet : 
```bash
./start-backend.sh
```

La base de données mongodb repond sur le port :  `27017`
