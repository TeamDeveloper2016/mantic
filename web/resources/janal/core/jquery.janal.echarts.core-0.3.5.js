/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 05/11/2019
 *time 11:15:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
(function(window) {
	var jsEcharts;
	
	Janal.Control.Echarts= {
		names: {}
	};
	
	Janal.Control.Echarts.Core= Class.extend({
		RESERVED_ID: 'items',
		RESERVED_NAMES: 'json',
		RESERVED_TITLE: 'title',
		RESERVED_HEADER: 'cgor-item-title',
		RESERVED_GROUP: 'group',
		RESERVED_KEY: 'CGOR',
		RESERVED_SYMBOL: 'cgor-item-symbol',
		RESERVED_CAROUSEL: 'cgor-item-carousel',
		RESERVED_HIDE: 'cgor-item-hide',
		LABEL_TOKEN: 'CGOR:',
		nacional: '#iconoNacional',
		georreferencia: '#iconoInformacion',
		charts: {},
		backup: {},
		history: {},
		selected: {
			group: ''
		},
		carousel: {
			index: 0,
			top: 0,
			items:[]
		},
		init: function(names) { // Constructor
			$echarts   = this;
			this.charts= names;
			this.selected.group= this.RESERVED_KEY;
			Object.assign(this.backup, names);
		}, // init
		start: function() {
			var items= $('.'+ this.RESERVED_CAROUSEL);
			if(items.length> 0) {
				$.each(items, function() {
					$(this).addClass($echarts.RESERVED_HIDE);
					$echarts.carousel.items.push($(this).attr('id'));
				});
				this.carousel.top= items.length- 1;
				this.show(this.carousel.items[0]);
			} // if	
		},
		display: function() {
//					$("#index").html('chart: ['+ this.carousel.items[this.carousel.index]+ '] '+ this.carousel.index+ ' de '+ this.carousel.top);
			if($("#index").length> 0) 
				if(this.carousel.items.length> 0)
          $("#index").html('gr\u00E1fica: '+(this.carousel.index+1)+' de '+ (this.carousel.top+1));
				else
				  $("#index").html(this.carousel.index+ ' de '+ this.carousel.top);
		},
		show: function(id) {
			var items= $('[id="'+ id+ '"]');
			if(items.length> 0) 
				$.each(items, function() {
					$(this).removeClass($echarts.RESERVED_HIDE);
				});
			this.display();
		},
		hide: function(id) {
			var items= $('[id="'+ id+ '"]');
			if(items.length> 0) 
				$.each(items, function() {
					$(this).addClass($echarts.RESERVED_HIDE);
				});
		},
		inc: function(group) {
			var ok= true;
			if(typeof(group)=== 'undefined')
			  group= this.selected.group;
			this.hide(this.carousel.items[this.carousel.index]);
			if(this.carousel.index< this.carousel.top)
				this.carousel.index++;
			else
				this.carousel.index= 0;
			this.show(this.carousel.items[this.carousel.index]);
			this.display();
			if(this.paint(this.carousel.items[this.carousel.index], group, true))
				ok= false;
			else
				this.single(this.carousel.items[this.carousel.index], group);
			return ok;
		},
		dec: function(group) {
			var ok= true;
			if(typeof(group)=== 'undefined')
			  group= this.selected.group;
			this.hide(this.carousel.items[this.carousel.index]);
			if(this.carousel.index> 0)
				this.carousel.index--;
			else
				this.carousel.index= this.carousel.top;
			this.show(this.carousel.items[this.carousel.index]);
			this.display();
			if(this.paint(this.carousel.items[this.carousel.index], group, true))
				ok= false;
			else
				this.single(this.carousel.items[this.carousel.index], group);
			return ok;
		},
		begin: function(paint) {
			if(this.carousel.items.length> 0 && this.carousel.index< this.carousel.items.length)
  			this.hide(this.carousel.items[this.carousel.index]);
			this.carousel.index= 0;
			if(this.carousel.items.length> 0)
   			this.show(this.carousel.items[this.carousel.index]);
			this.display();
			if(typeof(paint)!== 'undefined' && paint)
			  this.paint(this.carousel.items[this.carousel.index], this.selected.group, true);
		},
		single: function(id, group) {
      if(typeof refreshEChartSingle!== "undefined") {
				janal.bloquear();
			  refreshEChartSingle(id, group);
			} // if	
		},
		load: function(names) {
			if(typeof(names)!== 'undefined') {
				this.charts= names;
				Object.assign(this.backup, names);
			} // if	
	    $.each(this.charts, function(id, value) {
				if(id=== $echarts.RESERVED_ID)
					$echarts.search(value);
			  else
				  $echarts.create(id, value);
				$echarts.reserved(id, value);
			}); 
		},
		frames: function() {
			if(this.charts.hasOwnProperty($echarts.RESERVED_ID))
				this.search(this.charts[$echarts.RESERVED_ID]);
		},
		search: function(value) {
			if(value.hasOwnProperty(this.RESERVED_NAMES))
				$.each(value[this.RESERVED_NAMES], function(id, value) {
					var items= $('[id="'+ id+ '"]');
					if(items.length> 0) 
						$.each(items, function() {
 							$(this).html(value+ ($(this).hasClass($echarts.RESERVED_SYMBOL)? ' %': ''));
					  });
				}); 
		},
		create: function(id, value) {
			if($('#'+ id).length> 0 && value.hasOwnProperty(this.RESERVED_NAMES)) {
  			window[id]= echarts.init(document.getElementById(id), {renderer: 'svg', width: 'auto', height: 'auto'});
	  		window[id].setOption(value[this.RESERVED_NAMES], true);
		  	window[id].on('click', 'series', function (params) {params.chart= id; $echarts.send(params);});
        this.title(id, value);
			} // id
			else
				console.info('El marco ['+ id+ '] de la grafica no existe !');
		},
		reserved: function(id, value) {
			// si se definio un agrupador se mete al historial para ya no ir la backend
			if(!value.hasOwnProperty(this.RESERVED_GROUP)) 
         value[this.RESERVED_GROUP]= this.RESERVED_KEY;
			if(!this.history[value[this.RESERVED_GROUP]]) 
				this.history[value[this.RESERVED_GROUP]]= {};
			this.history[value[this.RESERVED_GROUP]][id]= value;
		},
		send: function (params) {
			var json= {
				chart: params.chart,
				color: params.color,
				name: params.name,
				value: params.value,
				percent: params.percent,
				dataIndex: params.dataIndex,
				dataType: params.dataType,
				seriesId: params.seriesId,
				seriesIndex: params.seriesIndex,
				seriesName: params.seriesName,
				seriesType: params.seriesType,
				event: params.type
			};
      if(typeof refreshEChartFrame!== "undefined")
			  refreshEChartFrame(JSON.stringify(json));
			else
				console.info('No existe la funcion de javascript llamada [refreshEChartFrame]');
		},
		refresh: function(items, look) {
			if(typeof(look)=== 'undefined')
				look= true;
			// esto es para actualizar varias graficas donde sus datos cambiaron
			$.each(items, function(id, value) {
				if(id== $echarts.RESERVED_ID)
					$echarts.search(value);
				else
					$echarts.update(id, value, look);
			}); 
		},
		title: function(id, value) {
			if(typeof(value) !== 'undefined')
				if(value.hasOwnProperty(this.RESERVED_TITLE))
					if($('#'+ id+ '-'+ this.RESERVED_TITLE).length> 0)
						$('#'+ id+ '-'+ this.RESERVED_TITLE).html(value[this.RESERVED_TITLE]);
					else
						if($('.'+ this.RESERVED_HEADER).length> 0)
							$('.'+ this.RESERVED_HEADER).html(value[this.RESERVED_TITLE]);
		},
		update: function(id, value, look) {
			if(typeof(look)=== 'undefined')
				look= true;
			// esto es para actualizar una sola grafica porque sus datos cambiaron
			if(window[id]) {
				if(value.hasOwnProperty(this.RESERVED_NAMES)) {
					this.charts[id]= value;
					window[id].clear();
					window[id].setOption(value[this.RESERVED_NAMES]);
					this.title(id, value);
					if(look)
					  this.reserved(id, value);
				} // if	
			} // if	
			else
				console.info('El marco ['+ id+ '] de la grafica no existe !');
		},
		resize: function (id) {
			if(window[id]) {
				window[id].resize();
			} // if	
			else
				console.info('El marco ['+ id+ '] de la grafica no existe !');
		},
		label: function (value) {
			value= this.capital(value);
			return value.length> 12? value.replace(/\s/g, '\n'): value;
		},
		capital: function(text) {
			if(typeof(text)=== 'string') {
				text= text.toLowerCase();
				text= text.charAt(0).toUpperCase()+ text.slice(1);
				if(text.length> 75)
					text= text.substring(0, 75)+ '...';
			} // if
			return text;
		}, 
		format: function (params, type, all) {
			// params.seriesName
			// params.name
			// params.value
			// params.data is Object
			// params.color
			if(typeof(type)=== 'undefined')
				type= '';
			if(typeof(all)=== 'undefined')
				all= true;
			var text= '';
			var data= parseFloat(params.value);
			if(params.name.startsWith(this.LABEL_TOKEN)) {
				data= parseFloat(params.name.substring(this.LABEL_TOKEN.length));
				all = true;
			} // if
			switch (type) {
				case 'integer':
					text= data.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 0, maximumFractionDigits: 0}); // 1,234,567
					break;
				case 'double':
					text= data.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 1, maximumFractionDigits: 2}); // 1,234,567.12
					break;
				case 'money':
					text= data.toLocaleString('en-US', {style: 'currency', currency: 'USD', minimumFractionDigits: 1, maximumFractionDigits: 2}); // $1,242.50
					break;
				case 'percent':
					text= this.capital(params.name)+ "\n("+ data.toLocaleString('en-US', {style: 'percent', minimumFractionDigits: 1, maximumFractionDigits: 2})+ ")"; // (37.53%)
					break;
				case 'cgor-double':
					text= this.capital(params.name)+ "\n"+ data.toLocaleString('en-US', {style: 'percent', minimumFractionDigits: 1, maximumFractionDigits: 1}); // 1,234,567.1
					break;
				case 'cgor-percent':
					text= this.capital(params.name)+ "\n("+ data.toLocaleString('en-US', {style: 'percent', minimumFractionDigits: 1, maximumFractionDigits: 1})+ ")"; // 37.5%
					break;
				default:
					text= data.toLocaleString('en-US'); // 1,234,567.123
			} // switch
			return all? text: '';
		},
		tooltip: function(params, format) {
		  var msg  = '<div style="text-align: left;">';
		  var label= '';
			if(typeof(format)=== 'undefined')
				format= 'double';
		  $.each(params, function(index, items) {
				if(index=== 0)
		      label= items['name'];
		    if(items['value']!== '-' && items['value']> 0)
		      msg= msg+ items['marker']+ '  '+ $echarts.legend(items['seriesName'])+ ': '+ $echarts.format(items, format)+ '<br/>';
		  });
		  msg= $echarts.label(label)+ '<br/>'+ msg+ '</div>';
      return msg;
		},
		legend: function(params) {
			if(typeof(params)=== 'string') {
				if(params.length> 7) {
  				params= params.toLowerCase();
	  			params= params.charAt(0).toUpperCase()+ params.slice(1);
				} // if	
				if(params.length> 75)
					params= params.substring(0, 75)+ '...';
			} // if
			return params;
		}, 
		responsive: function() {
			$.each(this.charts, function(id) {
				if(window[id])
				  window[id].resize();
			});				
		},
		reset: function() {
			Object.assign(this.charts, this.backup);
			$.each(this.charts, function(id, value) {
				if(id=== $echarts.RESERVED_ID)
					$echarts.search(value);
			  else
  				$echarts.update(id, value, false);
			});	
			this.toggle('display:none;');
			this.selected= {group: $echarts.RESERVED_KEY};
			return false;
		},
		add: function(items) {
			$.each(items, function(id, value) {
				$echarts.reserved(id, value);
				if(id=== $echarts.RESERVED_ID) 
					$echarts.search(value);
				else {
					if($echarts.charts[id]) 
						$echarts.update(id, value);
					else {
    				$echarts.charts[id]= value;
						$echarts.create(id, value);
					} // else	
					window[id].resize();
				} // if	
			});
      if(this.selected.hasOwnProperty('claveEntidad')) 
        $echarts.title(this.carousel.items[this.carousel.index],this.history[this.selected.claveEntidad][this.carousel.items[this.carousel.index]]);
		},
		remove: function(id) {
			if($echarts.charts.hasOwnProperty(id))
			  delete $echarts.charts[id];
		},
		exists: function(group, update) {
			if(typeof(update)=== 'undefined')
				update= true;
			var ok= this.history[group]!== undefined;
      if(ok && update)
			  this.refresh(this.history[group], false);
			return ok;
		},
		valid: function(group) {
			if(typeof(group)=== 'undefined')
				if(this.selected.hasOwnProperty('claveEntidad')) 
				  group= this.selected.claveEntidad;
			  else 
				  group= this.selected.group;
			var ok= !this.exists(group, true);
			if(ok)
			  janal.bloquear();
			else
				this.toggle('');
			return ok;
   	}, 
		clean: function() {
		  this.history= {};
		},
		get: function(id) {
			var json= undefined;
			if(this.charts[id])
				json= this.charts[id];
			return json;
		},
		group: function(id) {
			var json= undefined;
			if(this.history[id])
				json= this.history[id];
			return json;
		},
		item: function(params) {
			var token= params.split("|");
			var value= {
				idEntidad: token[0],
				claveEntidad: token[1],
				unidadEjecutora: token[2],
				ambito: token[3],
				descripcion: token[4],
				evento: (token.length>= 5? token[5]: '')
			};
			Object.assign(this.selected, value);
			return JSON.stringify(value);
		},
		map: function(params) {
      if(typeof loadItemEventMap!== "undefined")
			  loadItemEventMap(this.item(params));
			else
				console.info('No existe la funcion de javascript llamada [loadItemEventMap]');
		},
		toggle: function(style) {
      $(this.nacional).attr('style', style+ ' cursor:pointer; padding:4px 10px 4px 10px!important;');
      $(this.georreferencia).attr('style', style+ ' cursor:pointer; padding:4px 10px 4px 10px!important;');
    },
		paint: function(id, group, update) {
			var ok= false;
			if(typeof(group)=== 'undefined')
			  group= this.selected.group;
			else
				if(typeof(group)=== "boolean") {
				  update= group;
  			  group = this.selected.group;
				} // if	
			if(typeof(update)=== 'undefined')
				update= false;
			// esto es para actualizar una sola grafica porque sus datos cambiaron
			if(this.history.hasOwnProperty(group)) {
				if(this.history[group].hasOwnProperty(id)) {
					if(update)
						if(id=== this.RESERVED_ID)
							this.search(this.history[group][id]);
						else
  				    this.update(id, this.history[group][id], false);	
					ok= true;
				} // if
			} // if
			return ok;
		},
		total: function() {
			
		}
	});
	console.info('Janal.Control.Echarts initialized');
})(window);	
jsEcharts= new Janal.Control.Echarts.Core(Janal.Control.Echarts.names);

$(document).ready(function() {
	jsEcharts.load(Janal.Control.Echarts.names);
	jsEcharts.start();
});