/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 10/09/2019
 *time 20:01:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */

(function(window){
	
	var captcha;
	
	Janal.Control.Captcha= {};
	
	Janal.Control.Captcha.Core= Class.extend({
		ID_ELEMENT: "sortable",
		init: function(){
			$main= this;
			$("#"+this.ID_ELEMENT).sortable();
			$("#"+this.ID_ELEMENT).disableSelection();
			$main.shuffle();
		}, // init
		random: function(arr,obj) {
			for(
				var j, x, i = arr.length; i;
				j = parseInt(Math.random() * i),
				x = arr[--i], arr[i] = arr[j], arr[j] = x
			);
				if(arr[0].innerHTML==="A") obj.html($main.random(arr,obj))
				else return arr;
		}, //random
		shuffle: function() {
			$("#"+$main.ID_ELEMENT).each(function(){
				var items = $(this).children();
				(items.length)? $(this).html($main.random(items,$(this))): this;
			});
		}, // shuffle
		validarCaptcha: function(){
			var res = false;
			var arr = $("#"+this.ID_ELEMENT).children();
			res =    ((arr[0].innerHTML==="A")&&
					(arr[1].innerHTML==="B")&&
					(arr[2].innerHTML==="C")&&
					(arr[3].innerHTML==="D")&&
					(arr[4].innerHTML==="E"));
			return res;
		} // validarCaptcha
	});
	
})(window);

$(document).ready(function(){
	captcha= new Janal.Control.Captcha.Core();
	console.info("Janal.Control.Captcha.Core initialized");
});
