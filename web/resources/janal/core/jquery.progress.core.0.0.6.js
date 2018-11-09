/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 07/11/2018
 *time 21:01:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
	PrimeFaces.widget.ProgressBar.prototype.top = 0;
	PrimeFaces.widget.ProgressBar.prototype.start = function(top) {
		this.value = 0;
		this.jqValue.hide().css('width', '0%');
		this.jqLabel.hide();
		this.top = top;
		var _self = this;
		if (this.cfg.ajax) {
			this.progressPoll = setInterval(function() {
				var options = {
					source: _self.id,
					process: _self.id,
					formId: _self.cfg._formId,
					async: true,
					oncomplete: function(xhr, status, args) {
						var value = args[_self.id + '_value'];
						_self.setValue(value);
						//trigger complete listener
						if ((_self.top> 0 && _self.value>= _self.top) || (_self.top=== 0 && _self.value>= 100)) {
							_self.fireCompleteEvent();
						} // if
					} // oncomplete
				};
				PrimeFaces.ajax.AjaxRequest(options);
			}, this.cfg.interval);
		} // if
	};

	PrimeFaces.widget.ProgressBar.prototype.setValue = function(value) {
		var percentage = value;
		if (value>= 0) {
			if(value> this.top)
				value= this.top;
			if (this.top> 0)
				percentage= janal.truncate(value * 100 / this.top);
			else
				if (value> 100)
					percentage= 100;
			if (value=== 0) {
				this.jqValue.hide().css('width', '0%');
				this.jqLabel.hide();
			}
			else {
				this.jqValue.show().animate({'width': percentage + '%'}, 5, 'easeInOutCirc');
				if (this.cfg.labelTemplate) {
					var formattedLabel = this.cfg.labelTemplate.replace(/{value}/gi, percentage).replace(/{index}/gi, value).replace(/{count}/gi, this.top);
					this.jqLabel.html(formattedLabel).show();
				} // if
			} // else
			this.value = value;
		} // if
	}; // function


