'use strict';

const LS_KEY = 'store';

if (getCart()) setQuantity();

function getCart() {
	return JSON.parse(localStorage.getItem(LS_KEY));
}

function getQuantity() {
return Object.values(getCart()).reduce((a, b) => a + b, 0);
}

function setQuantity() {
document.querySelector('.header__cart-quantity').innerHTML = getQuantity();
}

function addToLocalStorage(id) {
	let cart = getCart();

	if (!cart) {
		setLocalStorageValue({[id]: 1});
	} else {
		if (cart[id]) {
			setLocalStorageValue({...cart, [id]: cart[id]+1});
		} else {
			setLocalStorageValue({...cart, [id]: 1});
		}
	}
}

function decreaseInLocalStorage(id) {
	let cart = getCart();
	if (cart[id] > 1) {
		setLocalStorageValue({...cart, [id]: cart[id]-1});
	}
	if (cart[id] === 1) {
		deleteFromLocalStorage(id);
	}
}

function deleteFromLocalStorage(id) {
	let cart = getCart();
	delete cart[id];
	setLocalStorageValue({...cart});
}

function clearLocalStorage() {
	localStorage.removeItem(LS_KEY);
}

function setLocalStorageValue(value) {
	localStorage.setItem(LS_KEY, JSON.stringify(value));
}