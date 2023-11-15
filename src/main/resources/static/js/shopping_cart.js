$(document).ready(function() {
	$(".minusButton").on("click", function(evt) {
		evt.preventDefault();
		bookId = $(this).attr("pid");
		qtyInput = $("#quantity" + bookId);
		newQty = parseInt(qtyInput.val()) - 1;
		if (newQty > 0) qtyInput.val(newQty);
		updateQuantity(bookId,newQty);
	});
	$(".plusButton").on("click", function(evt) {
		evt.preventDefault();
		bookId = $(this).attr("pid");
		qtyInput = $("#quantity" + bookId);
		quantity = $(this).attr("q");
		newQty = parseInt(qtyInput.val()) + 1;
		if (newQty <= quantity) qtyInput.val(newQty);
		updateQuantity(bookId,newQty);
	});
	$(".link-remove").on("click", function(evt) {
		evt.preventDefault();
		removeFromCart($(this))
	});
		
	updateTotal();
});

function removeFromCart(link){
	url=link.attr("href");
	$.ajax({
		type:"POST",
		url:url
		
	}).done(function(response){
		alert(response);
		document.location.reload();
		
	}).fail(function(){
		alert("Error while deleting book to cart");
	});
	
}

function updateQuantity(bookId,quantity){
	quantity = $("#quantity" + bookId).val();
	url = "http://springbootonlinebookstore-env.eba-hzipvppe.us-east-1.elasticbeanstalk.com/cart/update/" + bookId +"/"+quantity;
	//url = "http://localhost:8080/cart/update/" + bookId +"/"+quantity;
	console.log(url);
	console.log(url);
	$.ajax({
		type:"POST",
		url:url
		
	}).done(function(newSubtotal){
		updateSubtotal(newSubtotal,bookId);
		updateTotal();
		
	}).fail(function(){
		alert("Error while updating book to cart");
	});
}

function updateSubtotal(newSubtotal,bookId){
	$("#subtotal" + bookId).text(newSubtotal)
}

function updateTotal() {
	total = 0.0;

	$(".productSubtotal").each(function(index, element) {
		total = total + parseFloat(element.innerHTML);
	});
	$("#totalAmount").text(total);
}