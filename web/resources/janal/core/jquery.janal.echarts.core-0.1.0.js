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
		charts: {},
		backup: {},
		init: function(names) { // Constructor
			$echarts= this;
			this.charts= names;
			this.backup= names;
			this.events();
		}, // init
		events: function() {
			
		},
		load: function(names) {
			if(typeof(names)) {
				this.charts= names;
				this.backup= names;
			} // if	
	    $.each(this.charts, function(id, json) {
				$echarts.create(id, json);
			}); 
		},
		create: function(id, json) {
			window[id]= echarts.init(document.getElementById(id), {renderer: 'svg', width: 'auto', height: 'auto'});
			window[id].setOption(json, true);
			window[id].on('click', 'series', function (params) {params.chart= id; $echarts.send(params);});
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
			refreshEChartFrame(JSON.stringify(json));
		},
		update: function (name, json) {
			if(window[name]) {
				window[name].clear();
				window[name].setOption(json);
				this.charts[name]= json;
			} // if	
			else
				console.info('El marco ['+ name+ '] de la grafica no existe !');
		},
		resize: function (name) {
			if(window[name]) {
				window[name].resize();
			} // if	
			else
				console.info('El marco ['+ name+ '] de la grafica no existe !');
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
		restore: function() {
			this.charts= this.backup;
			$.each(this.charts, function(id, json) {
				$echarts.update(id, json);
			});				
		},
		add: function(items) {
			$.each(items, function(id, json) {
				if($echarts[id]) {
					console.info('Esta grafica ['+ id+ '] ya existe y se va a sobreescribir !');
  				$echarts.charts[id]= json;
          $echarts.update(id, json);
				} // if
				else {
  				$echarts.charts[id]= json;
					$echarts.create(id, json);
				} // else	
			});
		},
		remove: function(id) {
			if($echarts.charts[id])
			  delete $echarts.charts[id];
		}
	});
	console.info('Janal.Control.Echarts initialized');
})(window);	
jsEcharts= new Janal.Control.Echarts.Core(Janal.Control.Echarts.names);

$(document).ready(function() {
	jsEcharts.load(Janal.Control.Echarts.names);
});
