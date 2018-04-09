/*
 * company KAANA
 * project KAJOOL (Control system polls)
 * date Oct 21, 2013
 * time 14:02:10 PM
 * author Team Developer 2016 <team.developer@kaana.org.mx>
 */
function pageTop(){var o=$(".sticky-wrapper").position();return o.top}$().ready(function(){$.getScript(janal.toContext()+"/resources/janal/core/jquery.float.thead.min-1.2.7.js").done(function(o,n){janal.console(n+" jquery.float.thead loaded"),$.getScript(janal.toContext()+"/resources/janal/core/jquery.underscore.min-1.3.3.js").done(function(o,n){janal.console(n+" jquery.underscore loaded"),$(".sticky table:first-child").floatThead({scrollingTop:pageTop(),useAbsolutePositioning:!1,scrollContainer:function(){return $(".sticky-wrapper > div")}})}).fail(function(o,n,t){janal.console("Error: "+t)})}).fail(function(o,n,t){janal.console("Error: "+t)})});
