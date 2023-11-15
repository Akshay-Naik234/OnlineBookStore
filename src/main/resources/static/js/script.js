console.log("Hi ");
const toggleSidebar = () => {

	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	}
	else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};


const search = () => {
	// console.log("Searching");
	let query = $("#search-input").val();


	if (query == '') {
		$(".search-result").hide();
	}
	else {
		//console.log(query);
		// 
		let url = 'http://springbootonlinebookstore-env.eba-hzipvppe.us-east-1.elasticbeanstalk.com/search/' + query;
		//let url = 'http://localhost:8080/search/' + query;

		fetch(url)
			.then((response) => {
				return response.json();
			})
			.then((data) => {
				//console.log(data);
				let text = `<div class='list-group'>`
				data.forEach((book) => {
					text += `<a href="/user/books/${book.id}/book" class='list-group-item list-group-item-action'>${book.name}</a>`
				});
				text += `</div>`;
				$(".search-result").html(text);
				$(".search-result").show();
			});
	}
}


const paymentStart = () => {

	console.log("Payment started .. ");
	var amount = document.getElementById("totalAmount").innerText;
	
	//console.log(sum);
	//let amount = $("#total").val();
	console.log(amount);
	if (amount == '' || amount == null) {
		swal("Failed ", "Amount is required", "error");

		return;
	}

	//We  will use ajax to send request to create order jquery

	$.ajax(
		{
			url: "/user/create_order",
			data: JSON.stringify({ amount: amount, info: "order_request" }),
			contentType: "application/json",
			type: "POST",
			dataType: "json",
			success: function(response) {
				console.log(response);
				if (response.status == "created") {
					//open payment form

					let options = {
						key: 'rzp_test_w9FS1rJrpea6jE',
						amount: response.amount,
						currency: 'INR',
						name: 'Online Book Store',
						description: 'Purchasing Book',
						order_id: response.id,
						handler: function(response) {
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature);
							console.log('Payment successful');
							//alert("Congrats payment successful");

							updatePaymentOnServer(
								response.razorpay_payment_id,
								response.razorpay_order_id,
								"paid"
							)
						
						},
						"prefill": {
							"name": "",
							"email": "",
							"contact": ""
						},
						"notes": {
							"address": "Bug buster"
						},
						"theme": {
							"color": "#3399cc"
						}
					};
					var rzp = new Razorpay(options);
					rzp.on('payment.failed', function(response) {
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);
						//alert("Oops payment failed")
						swal("Failed ", "Oops payment failed", "error");

					});

					rzp.open();
				}
			},
			error: function(error) {
				console.log(error)
				alert("Something went wrong !!")
			}
		}
	)




};

function updatePaymentOnServer(payment_id, order_id, status) {
	$.ajax(
		{
			url: "/user/update_order",
			data: JSON.stringify({ payment_id: payment_id, order_id: order_id, status: status }),
			contentType: "application/json",
			type: "POST",
			dataType: "json",

			success: function(response) {
				swal("Good job!", "Congrats !! Payment Successful !! Order is Placed", "success");
			},
			error: function(error) {
				swal("Failed ", "Your payment is successful ,But we did not get on server,We will contact you as soon as possible", "error");
			}
			
		});
		

}



