/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 21/08/2018
 *time 11:15:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */

$(document).ready(function() {			
			window.readingWeight = false;			
			startConnection();										
});

qz.security.setCertificatePromise(function (resolve, reject) {	
	resolve("-----BEGIN CERTIFICATE-----\n" +
					"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\n" +
					"-----END CERTIFICATE-----\n" +
					"--START INTERMEDIATE CERT--\n" +
					"-----BEGIN CERTIFICATE-----\n" +
					"MIIFEjCCA/qgAwIBAgICEAAwDQYJKoZIhvcNAQELBQAwgawxCzAJBgNVBAYTAlVT\n" +
					"MQswCQYDVQQIDAJOWTESMBAGA1UEBwwJQ2FuYXN0b3RhMRswGQYDVQQKDBJRWiBJ\n" +
					"bmR1c3RyaWVzLCBMTEMxGzAZBgNVBAsMElFaIEluZHVzdHJpZXMsIExMQzEZMBcG\n" +
					"A1UEAwwQcXppbmR1c3RyaWVzLmNvbTEnMCUGCSqGSIb3DQEJARYYc3VwcG9ydEBx\n" +
					"emluZHVzdHJpZXMuY29tMB4XDTE1MDMwMjAwNTAxOFoXDTM1MDMwMjAwNTAxOFow\n" +
					"gZgxCzAJBgNVBAYTAlVTMQswCQYDVQQIDAJOWTEbMBkGA1UECgwSUVogSW5kdXN0\n" +
					"cmllcywgTExDMRswGQYDVQQLDBJRWiBJbmR1c3RyaWVzLCBMTEMxGTAXBgNVBAMM\n" +
					"EHF6aW5kdXN0cmllcy5jb20xJzAlBgkqhkiG9w0BCQEWGHN1cHBvcnRAcXppbmR1\n" +
					"c3RyaWVzLmNvbTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBANTDgNLU\n" +
					"iohl/rQoZ2bTMHVEk1mA020LYhgfWjO0+GsLlbg5SvWVFWkv4ZgffuVRXLHrwz1H\n" +
					"YpMyo+Zh8ksJF9ssJWCwQGO5ciM6dmoryyB0VZHGY1blewdMuxieXP7Kr6XD3GRM\n" +
					"GAhEwTxjUzI3ksuRunX4IcnRXKYkg5pjs4nLEhXtIZWDLiXPUsyUAEq1U1qdL1AH\n" +
					"EtdK/L3zLATnhPB6ZiM+HzNG4aAPynSA38fpeeZ4R0tINMpFThwNgGUsxYKsP9kh\n" +
					"0gxGl8YHL6ZzC7BC8FXIB/0Wteng0+XLAVto56Pyxt7BdxtNVuVNNXgkCi9tMqVX\n" +
					"xOk3oIvODDt0UoQUZ/umUuoMuOLekYUpZVk4utCqXXlB4mVfS5/zWB6nVxFX8Io1\n" +
					"9FOiDLTwZVtBmzmeikzb6o1QLp9F2TAvlf8+DIGDOo0DpPQUtOUyLPCh5hBaDGFE\n" +
					"ZhE56qPCBiQIc4T2klWX/80C5NZnd/tJNxjyUyk7bjdDzhzT10CGRAsqxAnsjvMD\n" +
					"2KcMf3oXN4PNgyfpbfq2ipxJ1u777Gpbzyf0xoKwH9FYigmqfRH2N2pEdiYawKrX\n" +
					"6pyXzGM4cvQ5X1Yxf2x/+xdTLdVaLnZgwrdqwFYmDejGAldXlYDl3jbBHVM1v+uY\n" +
					"5ItGTjk+3vLrxmvGy5XFVG+8fF/xaVfo5TW5AgMBAAGjUDBOMB0GA1UdDgQWBBSQ\n" +
					"plC3hNS56l/yBYQTeEXoqXVUXDAfBgNVHSMEGDAWgBQDRcZNwPqOqQvagw9BpW0S\n" +
					"BkOpXjAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQAJIO8SiNr9jpLQ\n" +
					"eUsFUmbueoxyI5L+P5eV92ceVOJ2tAlBA13vzF1NWlpSlrMmQcVUE/K4D01qtr0k\n" +
					"gDs6LUHvj2XXLpyEogitbBgipkQpwCTJVfC9bWYBwEotC7Y8mVjjEV7uXAT71GKT\n" +
					"x8XlB9maf+BTZGgyoulA5pTYJ++7s/xX9gzSWCa+eXGcjguBtYYXaAjjAqFGRAvu\n" +
					"pz1yrDWcA6H94HeErJKUXBakS0Jm/V33JDuVXY+aZ8EQi2kV82aZbNdXll/R6iGw\n" +
					"2ur4rDErnHsiphBgZB71C5FD4cdfSONTsYxmPmyUb5T+KLUouxZ9B0Wh28ucc1Lp\n" +
					"rbO7BnjW\n" +
					"-----END CERTIFICATE-----\n");
});

qz.security.setSignaturePromise(function (toSign) {
	return function (resolve, reject) {		
		resolve();
	};
});

qz.websocket.setClosedCallbacks(function (evt) {
	console.log(evt);
	if (evt.reason) {
		displayMessage("<strong>Connection closed:</strong> " + evt.reason, 'alert-warning');
	}
});

qz.websocket.setErrorCallbacks(handleConnectionError);

function handleConnectionError(err) {
	if (err.target !== undefined) {
		if (err.target.readyState >= 2) { //if CLOSING or CLOSED
			displayError("Connection to QZ Tray was closed");
		} else {
			displayError("A connection error occurred, check log for details");
			console.error(err);
		}
	} else {
		displayError(err);
	}
}

qz.serial.setSerialCallbacks(function (streamEvent) {
	if (streamEvent.type !== 'ERROR') {
		console.log('Serial', streamEvent.portName, 'received output', streamEvent.output);
		displayMessage("Received output from serial port [" + streamEvent.portName + "]: <em>" + streamEvent.output + "</em>");
	} else {
		console.log(streamEvent.exception);
		displayMessage("Received an error from serial port [" + streamEvent.portName + "]: <em>" + streamEvent.exception + "</em>", 'alert-error');

	}
});

qz.usb.setUsbCallbacks(function (streamEvent) {
	var vendor = streamEvent.vendorId;
	var product = streamEvent.productId;

	if (vendor.substring(0, 2) !== '0x') {
		vendor = '0x' + vendor;
	}
	if (product.substring(0, 2) !== '0x') {
		product = '0x' + product;
	}
	var $pin = $('#' + vendor + product);

	if (streamEvent.type !== 'ERROR') {
		if (window.readingWeight) {
			$pin.html("<strong>Weight:</strong> " + readScaleData(streamEvent.output));
		} else {
			$pin.html("<strong>Raw data:</strong> " + streamEvent.output);
		}
	} else {
		console.log(streamEvent.exception);
		$pin.html("<strong>Error:</strong> " + streamEvent.exception);
	}
});

qz.hid.setHidCallbacks(function (streamEvent) {
	var vendor = streamEvent.vendorId;
	var product = streamEvent.productId;

	if (vendor.substring(0, 2) !== '0x') {
		vendor = '0x' + vendor;
	}
	if (product.substring(0, 2) !== '0x') {
		product = '0x' + product;
	}
	var $pin = $('#' + vendor + product);

	if (streamEvent.type === 'RECEIVE') {
		if (window.readingWeight) {
			var weight = readScaleData(streamEvent.output);
			if (weight) {
				$pin.html("<strong>Weight:</strong> " + weight);
			}
		} else {
			$pin.html("<strong>Raw data:</strong> " + streamEvent.output);
		}
	} else if (streamEvent.type === 'ACTION') {
		displayMessage("<strong>Device status changed:</strong> " + "[v:" + vendor + " p:" + product + "] - " + streamEvent.actionType);
	} else { //ERROR type
		console.log(streamEvent.exception);
		$pin.html("<strong>Error:</strong> " + streamEvent.exception);
	}
});

function startConnection(config) {
	if (!qz.websocket.isActive()) {
		qz.websocket.connect(config).then(function () {
			findVersion();
		}).catch(handleConnectionError);
	} else {
		displayMessage('An active connection with QZ already exists.', 'alert-warning');
	}
}
		
function endConnection() {
	if (qz.websocket.isActive()) {
		qz.websocket.disconnect().then(function () {
		}).catch(handleConnectionError);
	} else {
		displayMessage('No active connection with QZ exists.', 'alert-warning');
	}
}

function verifyPrinter(){	
	findPrinter('ZDesigner TLP 2844', true);			
}	// 	
		
function	findPrinter(query, set) {	
	qz.printers.find(query).then(function (data) {		
		var ok= janal.partial('general');
		if(ok){
			if (set) 
				setPrinter(data);		
			if(confirm('\u00BF Esta seguro que desea imprimir los códigos ?'))
				executeDoAceptar();
		} // if
	}).catch(function(){
		janal.partial('impresora');
	});
}

function printCode(codes, title){
	janal.bloquear();
	printEPL(codes, title);
	janal.desbloquear();
} // printCodes

function	findDefaultPrinter(set) {
	qz.printers.getDefault().then(function (data) {
		displayMessage("<strong>Found:</strong> " + data);
		if (set) {
			setPrinter(data);
		}
	}).catch(displayError);
}
	
function findPrinters() {
	qz.printers.find().then(function (data) {
		var list = '';
		for (var i = 0; i < data.length; i++) {
			list += "&nbsp; " + data[i] + "<br/>";
		}
		displayMessage("<strong>Available printers:</strong><br/>" + list);
	}).catch(displayError);
}
		/// Raw Printers ///
function printEPL(code, title) {	
	var config = getUpdatedConfig();
	var codes= code.split('~');
	$.each(codes, function(index, value){
		console.log(value);
		var printData = [
			'N\n',
			'B372,152,2,1,5,15,97,B,"'+value+'"\n',
			'A372,181,2,4,1,1,N,"'+title+'"\n',
			'\nP1,1\n'						
		];
		qz.print(config, printData).catch(function(){
			janal.partial('impresora');
		});
	});	
}		

function findVersion() {
	qz.api.getVersion().then(function (data) {
		qzVersion = data;
	}).catch(displayError);
}
		/// Helpers ///		
function displayError(err) {
	console.error(err);
	displayMessage(err, 'alert-danger');
}

function displayMessage(msg, css) {
	console.log(msg);	
}

function getPath() {
	var path = window.location.href;
	return path.substring(0, path.lastIndexOf("/"));
}

var cfg= null;
function getUpdatedConfig() {
	if (cfg == null) {
		cfg = qz.configs.create(null);
	}
	updateConfig();
	return cfg
}

function updateConfig() {	
	var copies = 1;
	var jobName = null;

	copies = '1';
	jobName = '';

	cfg.reconfigure({
		copies: copies,
		jobName: jobName
	});
}
	
function setPrintFile() {
	setPrinter({file: $("#askFile").val()});
}
	
function setPrintHost() {
	setPrinter({host: $("#askHost").val(), port: $("#askPort").val()});
}

function setPrinter(printer) {
	var cf = getUpdatedConfig();
	cf.setPrinter(printer);

	if (typeof printer === 'object' && printer.name == undefined) {
		var shown;
		if (printer.file != undefined) {
			shown = "<em>FILE:</em> " + printer.file;
		}
		if (printer.host != undefined) {
			shown = "<em>HOST:</em> " + printer.host + ":" + printer.port;
		}

		$("#configPrinter").html(shown);
	} else {
		if (printer.name != undefined) {
			printer = printer.name;
		}

		if (printer == undefined) {
			printer = 'NONE';
		}
		$("#configPrinter").html(printer);
	}
}