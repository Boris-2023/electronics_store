'use strict';

const btn = document.querySelector('.js-order');

if (btn) {
    btn.addEventListener('click', () => {
        const cart = getCart();
        // console.log(Object.keys(cart).length)

        if (cart && Object.keys(cart).length) {
            makeOrder(cart)
        } else {
            window.location.href = '/cart?empty_cart'
        }
    })
}


async function makeOrder(cart) {

    try {
        const response = await fetch('/api/order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(cart)
        });
        console.log(window.location.href, response)
        if (response.ok) {
            clearLocalStorage();
        }

        //window.location.href = 'http://localhost:8000/cart/order'
    } catch (err) {
        console.warn(err);
    }
}