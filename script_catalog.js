// Fonction pour charger les catégories uniques depuis l'API
function loadCategories() {
	fetch("http://localhost:8080/api/products")
		.then(response => response.json())
		.then(data => {
			const categories = new Set();
			data.forEach(product => {
				categories.add(product.category);
			});

			const categoryFilter = document.getElementById("categoryFilter");
			categories.forEach(category => {
				const option = document.createElement("option");
				option.value = category;
				option.textContent = category;
				categoryFilter.appendChild(option);
			});

			// Affiche tous les produits par défaut
			filterProducts();
		})
		.catch(error => {
			console.error("Erreur lors du chargement des catégories :", error);
		});
}

// Fonction pour afficher les produits en fonction de la catégorie sélectionnée
function filterProducts() {
    const selectedCategory = document.getElementById("categoryFilter").value;
    const productList = document.getElementById("productList");
    productList.innerHTML = ""; // Efface la liste précédente

    fetch("http://localhost:8080/api/products")
        .then(response => response.json())
        .then(data => {
            data.forEach(product => {
                if (selectedCategory === "" || product.category === selectedCategory) {
                    const listItem = document.createElement("li");
                    const productInfo = document.createElement("div");
                    productInfo.classList.add("product-info");

                    // Ajoutez toutes les informations du produit
                    const productName = document.createElement("span");
                    productName.textContent = `Nom : ${product.label} - `;
                    productInfo.appendChild(productName);

                    const productDescription = document.createElement("span");
                    productDescription.textContent = `Description : ${product.description} - `;
                    productInfo.appendChild(productDescription);
					
					const productPrice = document.createElement("span");
                    productPrice.textContent = `Prix : ${product.price} €`;

                    const productImage = document.createElement("span");
                    productImage.textContent = `Image : ${product.image} - `;
                    productInfo.appendChild(productImage);

                    // Vérifiez si les dates de promotion sont égales à "2022,1,1"
                    const promotionStartDateComponents = String(product.promotionStartDate).split(',');
                    const promotionEndDateComponents = String(product.promotionEndDate).split(',');

                    if (promotionStartDateComponents[0] === "2022" && promotionStartDateComponents[1] === "1" && promotionStartDateComponents[2] === "1" && promotionEndDateComponents[0] === "2022" && promotionEndDateComponents[1] === "1" && promotionEndDateComponents[2] === "1"){
                        const naText = document.createElement("span");
                        naText.textContent = "Date début promotion : N/A - Date fin promotion : N/A - ";
                        productInfo.appendChild(naText);
                    } 
					else {
                        const productPromotionStartDate = document.createElement("span");
                        productPromotionStartDate.textContent = `Date début promotion : ${product.promotionStartDate} - `;
                        productInfo.appendChild(productPromotionStartDate);

                        const productPromotionEndDate = document.createElement("span");
                        productPromotionEndDate.textContent = `Date fin promotion : ${product.promotionEndDate} - `;
                        productInfo.appendChild(productPromotionEndDate);
                    }
					
					const productPromotionDiscount = document.createElement("span");
                    productPromotionDiscount.textContent = `Remise : ${product.promotionDiscount} - `;
					productInfo.appendChild(productPromotionDiscount);
					
                    // Vérifiez si une promotion est appliquée
                    if (product.promotionDiscount > 0) {
                        productPrice.classList.add("discounted-price");
                    }

                    productInfo.appendChild(productPrice);
					
                    listItem.appendChild(productInfo);
                    productList.appendChild(listItem);
                }
            });
        })
        .catch(error => {
            console.error("Erreur lors du chargement des produits :", error);
        });
}

// Écouteur d'événement pour le bouton de filtrage
document.getElementById("filterButton").addEventListener("click", filterProducts);

// Chargement initial des catégories
loadCategories();