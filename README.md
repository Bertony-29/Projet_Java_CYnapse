# Projet_Java_CYnapse

> FILIÈRE : ING1-GI • 2024-2025
> AUTEURS: Makiese Bertony, Youssef El haiti, Rayanne Saighi, Khajiev Amine

---

## 📌 Description du projet

CYnapse est une application Java avec interface graphique JavaFX permettant de générer, visualiser, modifier et résoudre des **labyrinthes parfaits ou imparfaits** à l’aide d’algorithmes classiques de graphes.

Le projet met en œuvre des notions fondamentales en **algorithmique de graphes non orientés et acycliques**, ainsi que l’implémentation d’une **interface graphique ergonomique**.

---

## 🚀 Fonctionnalités principales

### 🔧 Génération de labyrinthes
- À partir d’une **graine choisie** par l’utilisateur
- Choix entre :
  - **Labyrinthe parfait** (un seul chemin entre deux points, sans cycle)
  - **Labyrinthe imparfait** (avec cycles, potentiellement sans solution)
- Algorithmes disponibles :
  - 🟦 **Prim**
  - 🟨 **Kruskal**

### 🔍 Résolution de labyrinthes
- Choix entre 4 algorithmes :
  - 🔵 BFS
  - 🔴 DFS
  - 🟢 Dijkstra
  - 🟣 A*
- Deux modes :
  - **Complet**
  - **Pas à pas**
- Affichage :
  - Cases visitées colorées différemment
  - Chemin final surligné
  - Statistiques affichées : nombre de cases parcourues, longueur du chemin


### 💾 Sauvegarde & restauration
- Sauvegarde d’un labyrinthe dans un fichier
- Restauration possible depuis l’historique
- Interface dédiée à la gestion de l’historique (restauration, suppression)

---

## 🖥️ Interface utilisateur (JavaFX)

L'application se compose de plusieurs scènes :
- **Menu principal**
- **Configuration du labyrinthe** : 
  - Dimensions, type (parfait/imparfait), mode de génération, algorithmes de résolution
- **Visualisation** en temps réel
- **Historique des labyrinthes** : visualisation, suppression, restauration

---




### 📁 Structure du projet

```
  Ybay_Maze/
├── .idea/                   # Fichiers de configuration IntelliJ
├── .mvn/wrapper/           # Wrapper Maven
├── saved_mazes/            # Sauvegardes des labyrinthes
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── demo/
│       │               ├── generator/       # Génération de labyrinthes (Prim, Kruskal)
│       │               ├── model/           # Représentation des entités (Labyrinthe, Cellule)
│       │               ├── solver/          # Algorithmes de résolution (BFS, DFS, A*, Dijkstra)
│       │               ├── util/            # Outil utilitaire 
│       │               ├── view/            # Interface JavaFX (scènes)
│       │               ├── MainApp.java     # Point d’entrée principal
│       │               
│       └── resources/     # Fichiers FXML, images, styles CSS
├── README.md
├── pom.xml                # Fichier de configuration Maven
```

---

## 📊 Diagrammes UML

Les diagrammes suivants sont disponibles dans le rapport écrit final :

- Diagramme de classes
- Diagramme de cas d'utilisation (Use Case)

Ils illustrent la conception globale du projet ainsi que les interactions principales avec l’utilisateur.

---

## 📚 Documentation

  Code entièrement commenté en anglais
  
  JavaDoc généré dans le dossier

---
  
### 📦 Livrables  
  
  📁 [Code Java source](./ybay_maze/src/main/java/)

  Rapport PDF sur GitHub
  
  Documentation JavaDoc sur GitHub

---

## ⚙️ Prérequis

- Java 17+
- JavaFX SDK
- Maven ou JDK configuré avec JavaFX
- IDE recommandé : IntelliJ IDEA Ultimate ou Eclipse JEE


## 🔄 Lancer l’application

### En ligne de commande

