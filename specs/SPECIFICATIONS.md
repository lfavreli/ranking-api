# Specifications 

## Instructions

Réalisation d'une application exposant une API REST pour gérer le classement de joueurs lors d'un tournoi.
Les joueurs sont triés en fonction du nombre de points de chacun : du joueur ayant le plus de points à celui qui en a le moins.

L'API devra permettre :
* d'ajouter un joueur (son pseudo)
* de mettre à jour le nombre de points du joueur
* de récupérer les données d'un joueur (pseudo, nombre de points et classement dans le tournoi)
* de retourner les joueurs triés en fonction de leur nombre de points
* de supprimer tous les joueurs à la fin du tournoi


L'application devra être réalisée sous Kotlin, utiliser le framework d'injection Koin, et sera basée sur Ktor.
L'application utilisera DynamoDB comme base de données.
Le service devra s'initialiser et se lancer par un script.

Livrable : 
* code source avec tout ce qu'il faut pour que l'on puisse le lancer
* Readme file
* instruction pour lancer le service
* liste du "reste à faire" pour un déploiement en production serein

## Do not forget

+ Versionning dans l'URL
+ base_path = /api
+ Swagger
+ Unit testing
* Integration testing

+ Authentication (API Key)
+ Login
+ Analytics 

+ Pagination
+ Caching
+ Rate-limit
+ Health-check

+ Friend Leaderboard
+ Is user eliminated ?
+ Add sorting tournements list on GET /players (default 'lastUpdated' ; but may be sortBy=latestTournament|earliestTournament)

-----------


Prefix/BasePath : /api/ranking/v1


1. Ressource : Players

1.1 Liste des attributs

- playerId
- displayName
- tournaments[]
	- tournamentId
	- score
	- rank
- lastUpdated

1.2 Endpoints

1.2.1 

Request : GET /players
Description : Lister les joueurs
Output :
	> {
		"items": [
			{
		    	"playerId": "1234",
		    	"displayName": "Alice",
		    	"tournaments": [
			    	{
				      "tournamentId": "001",
				      "score": 1562,
				      "rank": 3
				    }
		    	],
		    	"lastUpdated": "2030-10-10T12:11:42Z"
			}
		]
	}

1.2.2

Request : GET /players/:playerId
Description : Récupérer un joueur par son identifiant
Output :
	> {
    	"playerId": "1234",
    	"displayName": "Alice",
    	"tournaments": [
	    	{
		      "tournamentId": "001",
		      "score": 1562,
		      "rank": 3
		    }
    	],
    	"lastUpdated": "2030-10-10T12:11:42Z"
	}

1.2.3

Request : POST /players
Description : Ajouter un joueur
Input :
	> { displayName: "Alice" }
Output :
	> {
    	"playerId": "1234",
    	"displayName": "Alice",
    	"tournaments": []
    	"lastUpdated": "2030-10-10T12:11:42Z"
	}

1.2.4

Request : PUT /players/:playerId
Description : Modifier un joueur
Input :
	> NOT YET IMPLEMENTED
Output :
	> NOT YET IMPLEMENTED


2. Ressource : Tournaments

2.1 Liste des attributs

2.1.1 Tournaments

- tournamentId
- description
- order (enum: LARGER_IS_BETTER / SMALLER_IS_BETTER)
- numPlayers
- lastUpdated

2.1.2 Leaderboard

- playerId
- displayName
- score
- rank

2.2 Endpoints

2.2.1

Request : GET /tournaments
Description : Lister tous les tournois
Output : 
	> {
		"items": [
			{
				"tournamentId": "001",
				"description": "Tournoi 1",
				"order": "LARGER_IS_BETTER",
				"numPlayers": 24,
				"lastUpdated": "2030-10-10T12:11:42Z"
			}
		]
	}

2.2.2

Request : GET /tournaments/:tournamentId
Description : Récupérer un tournoi par son ID
Output :
	> {
		"tournamentId": "001",
		"description": "Tournoi 1",
		"order": "LARGER_IS_BETTER",
		"numPlayers": 24,
		"lastUpdated": "2030-10-10T12:11:42Z"
	}

2.2.3

Request : GET /tournaments/:tournamentId/leaderboard
Description : Récupérer un Leaderboard
Output :
	> {
	  "tournamentId": "001",
	  "leaderboard": [
	    { "playerId": "1", "displayName": "Alice", "score": 1500, "rank": 1 },
	    { "playerId": "2", "displayName": "Bob", "score": 1400, "rank": 2 }
	  ]
	}

2.2.3

Request : GET /tournaments/{tournamentId}/leaderboard/top?number=:number
Description : Récupérer les tops joueurs
Input : 
	> Query param :number : indique le nombre de tops joueurs récupérer (meilleurs scores)
Output :
	> {
	  "tournamentId": "001",
	  "topPlayers": [
	    { "playerId": "1", "displayName": "Alice", "score": 1500 },
	    { "playerId": "2", "displayName": "Bob", "score": 1400 }
	  ]
	}

2.2.4

Request : POST /tournaments
Description : Création d'un nouveau tournoi
Input : 
	> {
		"description": "Tournoi 1",
		"order": "LARGER_IS_BETTER"
	}
Output :
	> {
		"tournamentId": "001", (Generated value)
		"description": "Tournoi 1",
		"order": "LARGER_IS_BETTER",
		"numPlayers": 0,
		"lastUpdated": "2030-10-10T12:11:42Z"
	}

2.2.5

Request : GET /tournaments/:tournamentId/players
Description : Lister tous les joueurs
Output :
	> {
	  "tournamentId": "001",
	  "players": [
	    { "playerId": "1", "displayName": "Alice", "score": 1500, "rank": 1 },
	    { "playerId": "2", "displayName": "Bob", "score": 1400, "rank": 1 }
	  ]
	}

2.2.6

Request : DELETE /tournaments/:tournamentId/players
Description : Supprimer tous les joueurs
Output :
	> {
		"tournamentId": "001",
		"description": "Tournoi 1",
		"order": "LARGER_IS_BETTER",
		"numPlayers": 0,
		"lastUpdated": "2030-10-10T12:11:42Z"
	}

2.2.7

Request : PATCH /tournaments/:tournamentId/players/:playerId/score
Description : Mise à jour du score
Input :
	> { "newScore": 1600 }
Output :
	> { "playerId": "2", "displayName": "Bob", "score": 700, "rank": 6 }


2.2.8

Request : DELETE /tournaments/:tournamentId
Description : Supprimer un Tournoi
Output :
	> {
	  	"tournamentId": "001",
	  	"message": "Tournament deleted"
	}

2.2.9

Request : GET /tournaments/:tournamentId/players/:playerId/nearby?range=:range
Description : Retrouver les joueurs classés autour d'un joueur donné
Input : 
	> Query param "range" : indique combien de joueurs au-dessus et endessous récupérer
Output :
	> {
		"tournamentId": "001",
		"playerId": "1234",
		"startRank": 2,
		"endRank": 2,
		"players": [
			{
		    	"playerId": "1234",
		    	"displayName": "Alice",
		    	"score": 1562,
		    	"rank": 2
			},
			{
		    	"playerId": "5678",
		    	"displayName": "Bob",
		    	"score": 1201,
		      "rank": 3
			}
		]
	}
