'use strict';

document.querySelector('.product-page__add-btn').addEventListener('click', (event) => {
	let id = event.target.getAttribute('data-id');
	addToLocalStorage(id);
	setQuantity();
});
