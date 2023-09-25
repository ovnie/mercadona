document.addEventListener("DOMContentLoaded", function () {
    // Remplit la liste des produits depuis l'API
    fetch("http://localhost:8080/api/products")
        .then(response => response.json())
        .then(products => {
            const productIdSelect = document.getElementById("productId");
            products.forEach(product => {
                const option = document.createElement("option");
                option.value = product.id;
                option.textContent = product.label;
                productIdSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error("Erreur lors de la récupération de la liste des produits :", error);
        });

    // Gestionnaire de soumission du formulaire
    document.getElementById("updateProductForm").addEventListener("submit", function (e) {
        e.preventDefault();

        const productId = document.getElementById("productId").value;
        const promotionStartDate = document.getElementById("promotionStartDate").value;
        const promotionEndDate = document.getElementById("promotionEndDate").value;
        const promotionDiscount = parseFloat(document.getElementById("promotionDiscount").value);

        const promotionData = {
            productId: productId,
            promotionStartDate: promotionStartDate,
            promotionEndDate: promotionEndDate,
            promotionDiscount: promotionDiscount
        };

        // Envoie les données de la promotion au serveur via une requête AJAX
        fetch("http://localhost:8080/api/products/promotion", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(promotionData)
        })
        .then(response => response.json())
        .then(data => {
            // Affiche un message de succès ou effectuez d'autres actions nécessaires
            document.getElementById("message").textContent = "Promotion ajoutée avec succès.";
        })
        .catch(error => {
            console.error("Erreur lors de l'ajout de la promotion :", error);
            document.getElementById("message").textContent = "Erreur lors de l'ajout de la promotion.";
        });
    });
});