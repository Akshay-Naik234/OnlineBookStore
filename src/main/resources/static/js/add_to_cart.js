$(document).ready(function () {
  $("#buttonAdd2Cart").on("click",function(e){
	  bookId = $(this).attr("bookId");
	  addToCart(bookId);
  })
});


function addToCart(bookId){
	
	quantity = $("#quantity" + bookId).val();
	url = "http://springbootonlinebookstore-env.eba-hzipvppe.us-east-1.elasticbeanstalk.com/cart/add/" + bookId + "/"+quantity;
	//url = "http://localhost:8080/cart/add/" + bookId +"/"+quantity;
	//url = "http://localhost:8080/cart/add/" + bookId +"/"+quantity;
	console.log(url);
	$.ajax({
		type:"POST",
		url:url
		
	}).done(function(response){
		alert(response);
		console.log(response);
	}).fail(function(){
		alert("Error while adding book to cart");
	});
	
}



