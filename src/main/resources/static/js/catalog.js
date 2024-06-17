'use strict';

let count = document.querySelector('.header__cart-quantity');

let buttonsAdd = document.querySelectorAll('.promo_add');
buttonsAdd.forEach((elem) => {
		elem.addEventListener('click', () => {
				let id = elem.getAttribute('data-id');
				addToLocalStorage(id);
				setQuantity();
	})
});
