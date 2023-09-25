document.getElementById("productForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const label = document.getElementById("label").value;
    const description = document.getElementById("description").value;
    const price = parseFloat(document.getElementById("price").value);
    const image = document.getElementById("image").value;
    const category = document.getElementById("category").value;
    const promotionStartDateInput = document.getElementById("promotionStartDate");
    const promotionEndDateInput = document.getElementById("promotionEndDate");
    const promotionDiscountInput = document.getElementById("promotionDiscount");

    const defaultStartDate = "2022-01-01"; // Date par défaut au format yyyy-mm-dd
    const defaultEndDate = "2022-01-01";   // Date par défaut au format yyyy-mm-dd
    const defaultDiscount = 0.0;           // Valeur de réduction par défaut

    const promotionStartDate = promotionStartDateInput.value || defaultStartDate;
    const promotionEndDate = promotionEndDateInput.value || defaultEndDate;
    const promotionDiscount = parseFloat(promotionDiscountInput.value) || defaultDiscount;

    const product = {
        label: label,
        description: description,
        price: price,
        image: image,
        category: category,
        promotionStartDate: promotionStartDate,
        promotionEndDate: promotionEndDate,
        promotionDiscount: promotionDiscount
    };

    // Envoie les données du produit au serveur via une requête AJAX
    fetch("http://localhost:8080/api/products", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(product)
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("message").textContent = "Produit ajouté avec succès.";
        // Réinitialise les champs du formulaire
        document.getElementById("label").value = "";
        document.getElementById("description").value = "";
        document.getElementById("price").value = "";
        document.getElementById("image").value = "";
        document.getElementById("category").value = "";
        document.getElementById("promotionStartDate").value = "";
        document.getElementById("promotionEndDate").value = "";
        document.getElementById("promotionDiscount").value = "";
    })
    .catch(error => {
        console.error("Erreur lors de l'ajout du produit :", error);
        document.getElementById("message").textContent = "Erreur lors de l'ajout du produit.";
    });
})