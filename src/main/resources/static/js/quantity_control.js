$(document).ready(function () {
  $(".minusButton").on("click",function(evt) {
		evt.preventDefault();
		bookId = $(this).attr("pid");
		qtyInput = $("#quantity" + bookId);
		newQty = parseInt(qtyInput.val()) -1;
		if(newQty > 0) 	qtyInput.val(newQty);
	});
	$(".plusButton").on("click",function(evt) {
		evt.preventDefault();
		bookId = $(this).attr("pid");
		qtyInput = $("#quantity" + bookId);
		quantity = $(this).attr("q");
		newQty = parseInt(qtyInput.val()) +1;
		if(newQty <= quantity)  qtyInput.val(newQty);
	});
});