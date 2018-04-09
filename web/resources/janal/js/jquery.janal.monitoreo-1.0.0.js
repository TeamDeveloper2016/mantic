function truncate(value) {
  if (value >= 0)
    return Math.floor(value);
  else
    return Math.ceil(value);
}

$(function() {
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
            if (value === 100 || value >= top) {
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
    if (value >= 0) {
      if (this.top > 0)
        percentage = truncate(value * 100 / this.top);
      else
      if (value > 100)
        percentage = 100;
      if (value === 0) {
        this.jqValue.hide().css('width', '0%');
        this.jqLabel.hide();
      }
      else {
        this.jqValue.show().animate({
          'width': percentage + '%'
        }, 500, 'easeInOutCirc');
        if (this.cfg.labelTemplate) {
          var formattedLabel = this.cfg.labelTemplate.replace(/{value}/gi, percentage).replace(/{index}/gi, value).replace(/{count}/gi, this.top);
          this.jqLabel.html(formattedLabel).show();
        } // if
      } // else
      this.value = percentage;
    } // if
  } // function
  $('#barra').hide();
});

function download(xhr, status, args) {
  if (args.janalOk) {
    janal.bloquear();
    PF('dialogoConfirmacion').show();
  } // if
  PF('progreso').cancel();
}

function start(registros, grupoValidacion) {
  var ok = false;
  if (typeof(grupoValidacion) !== 'undefined')
    ok = janal.partial(grupoValidacion);
  else
    ok = janal.execute();
  if (ok) {
    $('#barra').show();
    PrimeFaces.scrollTo('barra');
    PF('progreso').start(registros);
  } // if	
  return ok;
}
