/**
 * @开发商:jit.gwm
 */
//树木失踪警报
Ext.define('Stuattd.app.stuattdMonitor.StuattdAlarm', {
	extend : 'Ext.panel.Panel',
	// style : 'padding:10px;',
	html : '<iframe src= "' + appBaseUri + '/static/leaflet/stuattdAlarm.html" width="100%" height="100%" marginwidth="0" framespacing="0" marginheight="0" frameborder="0" ></iframe>',
	initComponent : function() {
		this.callParent(arguments);
	}
});