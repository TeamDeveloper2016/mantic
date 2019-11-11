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
		RESERVED_GROUP: 'group',
		RESERVED_KEY: 'CGOR',
		nacional: '#iconoNacional',
		georreferencia: '#iconoInformacion',
		charts: {},
		backup: {},
		histoy: {},
		init: function(names) { // Constructor
			$echarts= this;
			this.charts= names;
			Object.assign(this.backup, names);
		}, // init
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
			if(this.charts[$echarts.RESERVED_ID])
				this.search(this.charts[$echarts.RESERVED_ID]);
		},
		search: function(value) {
			if(value[this.RESERVED_NAMES])
				$.each(value[this.RESERVED_NAMES], function(id, value) {
					if($('#'+ id).length> 0)
						$('#'+ id).html(value);
				}); 
		},
		create: function(id, value) {
			if($('#'+ id).length> 0 && value[this.RESERVED_NAMES]) {
  			window[id]= echarts.init(document.getElementById(id), {renderer: 'svg', width: 'auto', height: 'auto'});
	  		window[id].setOption(value[this.RESERVED_NAMES], true);
		  	window[id].on('click', 'series', function (params) {params.chart= id; $echarts.send(params);});
			} // id
			else
				console.info('El marco ['+ id+ '] de la grafica no existe !');
		},
		reserved: function(id, value) {
			// si se definio un agrupador se mete al historial para ya no ir la backend
			if(!value[this.RESERVED_GROUP]) 
         value[this.RESERVED_GROUP]= this.RESERVED_KEY;
			if(!this.histoy[value[this.RESERVED_GROUP]]) 
				this.histoy[value[this.RESERVED_GROUP]]= {};
			this.histoy[value[this.RESERVED_GROUP]][id]= value;
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
		update: function(id, value, look) {
			if(typeof(look)=== 'undefined')
				look= true;
			// esto es para actualizar una sola grafica porque sus datos cambiaron
			if(window[id]) {
				if(value[this.RESERVED_NAMES]) {
					this.charts[id]= value;
					window[id].clear();
					window[id].setOption(value[this.RESERVED_NAMES]);
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
			return value.replace(/\s/g, '\n');
		},
		format: function (params, type) {
			// params.seriesName
			// params.name
			// params.value
			// params.data is Object
			// params.color
			if(typeof(type)=== 'undefined')
				type= '';
			var text= '';
			var data= parseFloat(params.value);
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
					text= params.name+ "\n("+ data.toLocaleString('en-US', {style: 'percent', minimumFractionDigits: 1, maximumFractionDigits: 2})+ ")"; // (37.53%)
					break;
				case 'cgor-double':
					text= params.name+ "\n"+ data.toLocaleString('en-US', {style: 'percent', minimumFractionDigits: 1, maximumFractionDigits: 1}); // 1,234,567.1
					break;
				case 'cgor-percent':
					text= params.name+ "\n("+ data.toLocaleString('en-US', {style: 'percent', minimumFractionDigits: 1, maximumFractionDigits: 1})+ ")"; // 37.5%
					break;
				default:
					text= data.toLocaleString('en-US'); // 1,234,567.123
			} // switch
			return text;
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
				if(id!== $echarts.RESERVED_ID)
  				$echarts.update(id, value, false);
			});				
		},
		add: function(items) {
			$.each(items, function(id, value) {
				if($echarts.charts[id])
					console.info('Esta grafica ['+ id+ '] ya existe y se va a sobre escribir !');
 				$echarts.charts[id]= value;
				if($echarts.charts[id]) 
          $echarts.update(id, value);
				else 
					$echarts.create(id, value);
			});
		},
		remove: function(id) {
			if($echarts.charts[id])
			  delete $echarts.charts[id];
		},
		exists: function(group, update) {
			if(typeof(update)=== 'undefined')
				update= true;
			var ok= this.histoy[group]!== undefined;
      if(ok && update) 
			  this.refresh(this.histoy[group], false);
			return ok;
		},
		clean: function() {
		  this.histoy= {};
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
			  group= this.RESERVED_GROUP;
			else
				if(typeof(group)=== "boolean") {
				  update= group;
  			  group = this.RESERVED_GROUP;
				} // if	
			if(typeof(update)=== 'undefined')
				update= false;
			// esto es para actualizar una sola grafica porque sus datos cambiaron
			if(this.history[group]) {
				if(this.history[group][id]) {
					if(update)
						if(id== this.RESERVED_ID)
							this.search(this.history[group][id]);
						else
  				    this.update(id, this.history[group][id], false);	
					ok= true;
				} // if
			} // if
			return ok;
		}
	});
	console.info('Janal.Control.Echarts initialized');
})(window);	
jsEcharts= new Janal.Control.Echarts.Core(Janal.Control.Echarts.names);

$(document).ready(function() {
	jsEcharts.load(Janal.Control.Echarts.names);
});