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
        if (response.ok) {
            clearLocalStorage();
            window.location.href = '/cart/order'
        } else {
            window.location.href = '/cart$order_fail'
        }

    } catch (err) {
        console.warn(err);
    }
}