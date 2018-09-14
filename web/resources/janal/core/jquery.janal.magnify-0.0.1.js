/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 14/09/2018
 *time 08:52:55 AM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
(function(window) {
})(window);

$(document).ready(function() {
	$('[data-magnify=gallery]').magnify({
		// is draggable
		draggable: true,
		// is resizable
		resizable: true,
		// is draggable
		movable: true,
		// keyboard navigation
		keyboard: true,
		// shows titles
		title: true,
		// modal size
		modalWidth: 620,
		modalHeight: 620,
		// fixed content
		fixedContent: true,
		// maximize the image viewer on init
		initMaximized: false,
		// threshold 
		gapThreshold: 0.02, 
		ratioThreshold: 0.1,
		// min/max ratio
		minRatio: 0.1,
		maxRatio: 16,
		// toolbar options
		headToolbar: ['maximize', 'close'],
		footToolbar: ['zoomIn', 'zoomOut', 'prev', 'fullscreen', 'next', 'actualSize', 'rotateRight'],
		// customize icons
		icons: {minimize: 'fa fa-window-minimize', maximize: 'fa fa-window-maximize', close: 'fa fa-close', zoomIn: 'fa fa-search-plus', zoomOut: 'fa fa-search-minus', prev: 'fa fa-arrow-left', 
						next: 'fa fa-arrow-right', fullscreen: 'fa fa-photo', actualSize: 'fa fa-arrows-alt', rotateLeft: 'fa fa-rotate-left', rotateRight: 'fa fa-rotate-right', loader: 'fa fa-spinner fa-pulse'},
		// language
		i18n: {minimize: 'minimize', maximize: 'maximize', close: 'close', zoomIn: 'zoom-in(+)', zoomOut: 'zoom-out(-)', prev: 'prev(?)', next: 'next(?)', fullscreen: 'fullscreen', 
					 actualSize: 'actual-size(Ctrl+Alt+0)', rotateLeft: 'rotate-left(Ctrl+,)', rotateRight: 'rotate-right(Ctrl+.)'},
		// enable multiple instances
		multiInstances: true,
		// default trigger event
		initEvent: 'click',
		// the modal size will be fixed when change images
		fixedModalPos: false,
		// z-index
		zIndex: 1090,
		// css selector of drag handler
		dragHandle: '.magnify-modal',
		// callback functions
		callbacks: { beforeOpen: $.noop, opened: $.noop, beforeClose: $.noop, closed: $.noop, beforeChange: $.noop, changed: $.noop }
	});		
  console.info('Janal.Image.Magnify.initialized: '+ janal.initialized);
});


