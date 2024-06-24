'use strict';
class Item {
    rendered = false;

    constructor(product, quantity, img = `images/img_small/card${product.id}.jpg`) {
        this.id_product = product.id;
        this.product_name = product.name + " " + product.manufacturer + " " + product.model;
        this.price = product.price;
        this.quantity = quantity;
        this.img = img;
    }

    changeQuantity(count) {
        this.quantity += count;
        this._updateItem();
    }

    _updateItem() {
        const block = document.querySelector(`.shop-cart__line[data-id="${this.id_product}"]`);
        const inputQuantity = block.querySelector('.shop-cart__quantity');
        inputQuantity.textContent = `${this.quantity}`;
        block.querySelector('.shop-cart__price').textContent = `${this.quantity * this.price}`;
    }

    markUp() {
        this.rendered = true;
        return `
			<div class = 'shop-cart__line' data-id = "${this.id_product}">
				<div class='shop-cart__details'>
						<img class="shop-cart__photo" src="${this.img}" alt="Товар">
						<div class='shop-cart__details-wrapper shop-cart__title'>${this.product_name}</div>
				</div>

				<span class="shop-cart__data">${this.price}</span>

				<div class="shop-cart__data-input">
						<div class="shop-cart__quantity-wrap">
								<button data-id = "${this.id_product}" class="cart__quantity-btn quantityMinus" type="submit">
										<i data-id = "${this.id_product}" class="fas fa-caret-left arrow fa-lg quantityMinus"></i>
								</button>

								<span data-id = "${this.id_product}" class="shop-cart__quantity">${this.quantity}</span>
						
								<button data-id = "${this.id_product}" class="cart__quantity-btn quantityPlus" type="submit">
										<i data-id = "${this.id_product}" class="fas fa-caret-right arrow fa-lg quantityPlus"></i>
								</button>
						</div>
				</div>

				<span class="shop-cart__data">БЕСПЛАТНО</span>
				<span class="shop-cart__data shop-cart__price subtotal">${this.quantity * this.price}</span>
				<button class="shop-cart__delete-wrapper js-dlt" data-id = "${this.id_product}">
						<svg class="cart__delete js-dlt" data-id = "${this.id_product}" fill="currentColor" height="15" width="15" viewBox="0 0 512 512">
						<path class="js-dlt" data-id = "${this.id_product}" d="M256 8C119 8 8 119 8 256s111 248 248 248 248-111 248-248S393 8 256 8zm121.6 313.1c4.7 4.7 4.7 12.3 0 17L338 377.6c-4.7 4.7-12.3 4.7-17 0L256 312l-65.1 65.6c-4.7 4.7-12.3 4.7-17 0L134.4 338c-4.7-4.7-4.7-12.3 0-17l65.6-65-65.6-65.1c-4.7-4.7-4.7-12.3 0-17l39.6-39.6c4.7-4.7 12.3-4.7 17 0l65 65.7 65.1-65.6c4.7-4.7 12.3-4.7 17 0l39.6 39.6c4.7 4.7 4.7 12.3 0 17L312 256l65.6 65.1z"></path></svg>
				</button>
		</div>
		`
    }
}

class Cart {

    allProducts = [];

    constructor(selector, place) {
        this.container = selector;
        this.place = place;
        this._getProducts();
    }

    _getProducts() {
        let cart = getCart();
        if (cart && Object.keys(cart).length) {
            let ids = Object.keys(cart);
            (async () => {

                try {
                    const response = await fetch('/api/products', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json;charset=utf-8'
                        },
                        body: JSON.stringify(ids)
                    });

                    let answer = await response.json();
                    this.handleData(answer);

                } catch (err) {
                    console.warn(err);
                }
            })();
        }
    }

    handleData(data) {
        for (let item of data) {
            let quantity = getCart()[item.id];
            this.allProducts.push(new Item(item, quantity));
        }
        this._render();
        this.updateTotal();
        this._init();
    }

    _render() {
        const block = document.querySelector(this.container);

        for (let product of this.allProducts) {
            if (product.rendered) {
                continue;
            }
            block.insertAdjacentHTML(this.place, product.markUp());
        }
    }

    getItem(id) {
        return this.allProducts.find(el => el.id_product === id);
    }

    _init() {
        document.querySelector(this.container).addEventListener('click', e => {

//УДАЛЕНИЕ ПОЛНОСТЬЮ ТОВАРА КНОПКОЙ

            if (e.target.classList.contains('js-dlt')) {

                const id = +e.target.dataset['id'];
                this.removeMarkUp(id);
                let item = this.getItem(id);
                this.allProducts.splice(this.allProducts.indexOf(item), 1);
                this.updateCart();
                this.updateTotal();
                deleteFromLocalStorage(id);
            }

//ИЗМЕНЕНИЕ КОЛИЧЕСТВА ТОВАРА ЧЕРЕЗ КНОПКУ В КОРЗИНЕ

            if (e.target.classList.contains('quantityMinus')) {
                const id = +e.target.dataset['id'];
                let item = this.getItem(id);
                this.deleteItem(item);
                this.updateCart();
                this.updateTotal();
                decreaseInLocalStorage(id);
            }

            if (e.target.classList.contains('quantityPlus')) {
                const id = +e.target.dataset['id'];
                let item = this.getItem(id);
                item.changeQuantity(1);
                this.updateCart();
                this.updateTotal();
                addToLocalStorage(id)
            }
        });

        //УДАЛЕНИЕ СРАЗУ ВСЕХ ТОВАРОВ ИЗ КОРЗИНЫ КНОПКОЙ CLEAR
        document.querySelector('.js-clear').addEventListener('click', (e) => {
            e.preventDefault();
            this.allProducts.forEach(el => {
                document.querySelector(`.shop-cart__line[data-id="${el.id_product}"]`).remove();
            })
            this.allProducts = [];
            this.updateCart();
            this.updateTotal();
            clearLocalStorage();
        });
    }

    removeMarkUp(id) {
        //удалить разметку товара со страницы
        document.querySelector(`.shop-cart__line[data-id="${id}"]`).remove();
    }

    calcQuantity() {
        return this.allProducts.reduce((accum, item) => accum += item.quantity, 0);
    }

    updateCart() {
        let newQuantity = this.calcQuantity();
        if (newQuantity === 0) {
            document.querySelector('.header__cart-quantity').textContent = '';
        } else {
            document.querySelector('.header__cart-quantity').textContent = newQuantity;
        }
    }

    updateTotal() {
        let total = this.allProducts.reduce((accum, item) => accum += item.quantity * item.price, 0);
        document.querySelector('.total').textContent = total;
    }

    deleteItem(item) {
        if (item.quantity > 1) {
            item.changeQuantity(-1);
            return;
        }
        this.allProducts.splice(this.allProducts.indexOf(item), 1); //удалить сам товар из общего списка товаров
        this.removeMarkUp(item.id_product);
    }


}

new Cart('.js-cart', 'beforeend');
