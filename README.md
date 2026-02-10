## Architecture

### Design Pattern MVC
- **Model** : `Product`, `Clothing`, `Shoes`, `Accessory`, `Store`
- **View** : Interface JavaFX (dans `WomenShopApp`)
- **Controller** : Logique intégrée dans `WomenShopApp`

### Design Pattern DAO (optionnel)
- `ProductDAO` (interface)
- `ClothingDAO` (implémentation)
- `DatabaseConnection` (utilitaire)

## Installation et exécution

### Option 1 : Sans base de données (mode mémoire)
1. Compiler le projet :
```bash
mvn clean compile
```

2. Exécuter l'application :
```bash
mvn javafx:run
```

### Option 2 : Avec base de données MySQL
1. Créer la base de données :
```bash
mysql -u root -p < database.sql
```

2. Configurer les paramètres dans `DatabaseConnection.java` :
```java
private static final String URL = "jdbc:mysql://localhost:3306/womenshop";
private static final String USER = "root";
private static final String PASSWORD = "votre_mot_de_passe";
```

3. Compiler et exécuter :
```bash
mvn clean compile
mvn javafx:run
```

## Démonstration du scénario

### Données initiales chargées
- Capital initial : 30 000 €
- 6 produits créés :
  - Dress 1 (Vêtement) : Achat 70€, Vente 100€
  - Dress 2 (Vêtement) : Achat 90€, Vente 120€
  - Shoe 1 (Chaussures) : Achat 30€, Vente 50€
  - Shoe 2 (Chaussures) : Achat 50€, Vente 70€
  - Accessory 1 : Achat 20€, Vente 30€
  - Accessory 2 : Achat 30€, Vente 40€

### Étapes du scénario de démonstration

1. **Affichage des produits**
   - Voir tous les produits
   - Filtrer par catégorie
   - Trier par prix

2. **Achat de stock** (20 unités de chaque)
   - Coût total : 5 800 €
   - Capital restant : 24 200 €

3. **Ventes sans réduction**
   - Dress 1 : 5 unités → 500 €
   - Shoe 1 : 5 unités → 250 €
   - Accessory 1 : 5 unités → 150 €
   - Total : 900 €
   - Capital : 25 100 €

4. **Application des réductions**
   - Vêtements : -30%
   - Chaussures : -20%
   - Accessoires : -50%

5. **Ventes avec réduction**
   - Dress 1 : 5 unités → 350 €
   - Shoe 1 : 5 unités → 200 €
   - Accessory 1 : 5 unités → 75 €
   - Total : 625 €
   - Capital final : 25 725 €

6. **Désactivation des réductions**

## Utilisation de l'interface

### Boutons principaux
- **All / Clothing / Shoes / Accessories** : Filtrer les produits
- **Sort by Price** : Trier par prix croissant
- **Add Product** : Ajouter un nouveau produit
- **Edit Product** : Modifier un produit sélectionné
- **Delete Product** : Supprimer un produit (stock = 0)
- **Purchase Stock** : Acheter du stock
- **Sell Product** : Vendre des produits
- **Apply Discounts** : Activer les réductions
- **Remove Discounts** : Désactiver les réductions

### Statistiques affichées
- Capital actuel
- Coût total d'achat
- Revenu total des ventes
- Profit

## Points techniques

### Validation des données
Toutes les validations sont implémentées et affichent des messages d'erreur appropriés.

### Gestion de la mémoire
L'application fonctionne entièrement en mémoire. La base de données est optionnelle.

### Code propre
- Respect du pattern MVC
- Séparation des responsabilités
- Gestion des exceptions
- Code commenté et lisible

## Améliorations possibles
- Persistance complète avec base de données
- Historique des transactions
- Rapports et graphiques
- Gestion des utilisateurs
- Export des données

## Auteur
Projet réalisé dans le cadre du cours "Advanced Object Oriented Programming" - ESILV 2025/2026
