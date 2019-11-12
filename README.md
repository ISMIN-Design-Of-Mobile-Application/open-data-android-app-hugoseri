
## [APP NAME]  
  
Codé par :   
 - Pierre SEITE   
 - Hugo SERIEYS   

## Description  
  
URL des données : [Lien](https://data.opendatasoft.com/explore/dataset/archives-sncf-new%40datasncf/information/?sort=-id&dataChart=eyJxdWVyaWVzIjpbeyJjb25maWciOnsiZGF0YXNldCI6ImFyY2hpdmVzLXNuY2YtbmV3QGRhdGFzbmNmIiwib3B0aW9ucyI6eyJzb3J0IjoiLWlkIn19LCJjaGFydHMiOlt7ImFsaWduTW9udGgiOnRydWUsInR5cGUiOiJsaW5lIiwiZnVuYyI6IkFWRyIsInlBeGlzIjoiaWQiLCJzY2llbnRpZmljRGlzcGxheSI6dHJ1ZSwiY29sb3IiOiIjMTQyRTdCIn1dLCJ4QXhpcyI6ImRhdGVfZGVfbnVtZXJpc2F0aW9uIiwibWF4cG9pbnRzIjoiIiwidGltZXNjYWxlIjoieWVhciIsInNvcnQiOiIifV0sImRpc3BsYXlMZWdlbmQiOnRydWUsImFsaWduTW9udGgiOnRydWV9&fbclid=IwAR1BPU3k_74faJKKRMM9JXwwFe_beRJjB9wH9kdl-tfiLWmVbMIMipMpzj0&location=7,47.69867,0.36804&basemap=jawg.sunny).
  
[A completer]  
  
## Librairies externes  
  
[A completer]  
  
## Consignes  
  
Lien vers le sujet : https://docs.google.com/presentation/d/1mwu2xx7_qfCZDfsRxseC94n7oBGYfhw-9xIftaTDbzk/edit#slide=id.p97  
  
### But du projet  
  
 - Le but du projet est de réaliser une application permettant de visualiser une série de données OpenData  
 - Les données devront être récupérées sur un serveur distant et affichées dans une liste et sur une carte  
 - Un clique sur un élément de la liste ou sur un marker de la carte permet d’accéder à un écran présentant le détail de l’élément  
 - Un écran présentera des informations générales sur les données récupérées  
  
### Exigences    
- [ ] Format des données OpenData :   
	 - Format Json  
	 - Avec un champ correspondant à l’url d’une image  
	 - Avec un champ correspondant à une position  
 - [ ] Application composée au minimum de 3 Fragment et 2 Activity  
 - [ ] Une Actionbar sera présente et permettra de rafraîchir les données récupérées et affichées
 
 ## Bonus
 
 - Amélioration de l’expérience utilisateur :
    - Clustering des markers sur la carte en fonction du niveau de zoom
    - Mise en place d’un système de recherche/filtre sur la liste affichée
 
 - Enrichissements techniques :
    - Mise en place d’une base de données locale pour afficher la liste d’élément en mode hors connexion
    - Utilisation de LiveData ou d’Observable pour la récupération de données dans la BDD
 
