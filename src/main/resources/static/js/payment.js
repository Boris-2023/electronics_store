'use strict';

async function makePayment(email, order_number, order_amount) {
    console.log(email + ' ' + order_number + ' ' + order_amount);

    try {
        const payDTO = new Object();
        payDTO.user = email;
        payDTO.order = order_number;
        payDTO.amount = order_amount;

        // makes POST request to api/payment - wait for payment results back
        const response = await fetch('/api/payment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(payDTO)
        });

        if (response.ok) {
            window.location.href = '/cart/order?pay=' + await response.text();
        } else {
            window.location.href = '/cart/order?pay=-1'
        }

    } catch (err) {
        console.warn(err);
    }
}
