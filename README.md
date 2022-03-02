# Projet 9 - Application [Realestate Manager](https://github.com/gtdevgit/P9)

Application développée en **java** avec **Android Studio**.

![Android](https://img.shields.io/badge/Android-Studio-blue)
![git](https://img.shields.io/github/languages/code-size/gtdevgit/P9)

***Devenez un as de la gestion immobilière***

>[Consulter la présentation fonctionelle et techniques de l'application au format pdf.](https://github.com/gtdevgit/P9/blob/main/Documentation/Pr%C3%A9sentation%20-%20Real%20Estate%20Manager.pdf)

## Fonctionnalités principales

>[Consulter la documentation fonctionnelles complete au format pdf.](https://github.com/gtdevgit/P9/blob/main/Documentation/P9_Documentation%20fonctionnelle%20Real%20Estate%20Manager.pdf)

- Gérer les annonces (consultation, création, modification, vente)
- Géolocaliser chaque biens et afficher l’ensemble des biens sur une carte interactive
- Rechercher un bien
- Calculer un emprunt
- Responsive, adapté au téléphone et au tablette en mode portrait et paysage
- Fonctionne en mode déconnecter
- Partager les données
- Traduction
- Selectionner une photo depuis la gallerie du device ou depuis un drive internet
- Prendre une photo

## Caractéristiques techniques

### Généralités

- L’application a été développée pour **Android** avec **Android Studio**.

- Le langage utilisé est le **Java**.

- L'application supporte **Android 5.0 Lollipop**.

- Les sources de l’application sont hébergées sur **Github**

> https://github.com/gtdevgit/P9

- Services tiers **Google** : *Map*, *Geocode*, *StaticMap*
  - L'application reqiére une **cle d'API** Google qui doit être intégrée dans le fichier *gradle.property*.
- **Geolocation** obtenue avec *FusedLocationProviderClient*.
- **Autorisation** *ACCESS_FINE_LOCATION* nécessaire.
- **Affichage de la Map** avec *Google Map SDK*.
- La base de données est une base de données **Room**

### Design

- L'Application respecte les rêgles de design étblie par *Material Design*
- Personnalisation des couleurs
- Personnalisation des styles
- Utilisation des **qualificateurs** dans les *layout* et les *dimens*.

### Architecture

L'application utilise **Android Architecture Components** et les patrons de conception :

- *MVVM*
- *Singleton* pour la fabrique des ViewModel
- *Observer* pour dialogue entre les couche applicatives

Utilisation des classes :

- ViewModel,
- ViewModelProvider.Factory,
- MutableLiveData,
- LiveData,
- Transformation,
- MediatorLiveData.

Content Provider

Gestion des fragments et de la navigation avec **Navigation**

Gestion des autorisations

### Tests

- Tests unitaires réalisées avec **Junit**

- Tests des view modèles réalisés avec **Mokito** et *l'injection de dépendance*
