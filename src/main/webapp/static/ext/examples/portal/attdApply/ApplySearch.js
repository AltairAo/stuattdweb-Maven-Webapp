/**
 * @开发商:jit.gwm
 */

Ext.define('App.StuattdTypeListWindow', {
	extend : 'Ext.window.Window',
	constructor : function(config) {
		config = config || {};
		
		Ext.apply(config, {
			title : '查看',
			width : 600,
			height : 400,
			bodyPadding : '10 5',
			// maximizable: true,
			modal : true,
			layout : 'fit',
			items : [{
				xtype : 'ApplyAction'
			}]
	
		});
		App.StuattdTypeListWindow.superclass.constructor.call(this, config);
	}
});

Ext.define('Stuattd.app.attdApply.ApplySearch', {
	extend : 'Ext.ux.custom.NewGlobalGridPanel',
	requires: [
	           'Stuattd.app.attdApply.ApplyAction'
	       ],
	id : 'ApplySearch-grid',
	xtype : 'ApplySearch',
	region : 'center',
	
	initComponent : function() {
		var me = this;
		var xsjbxxId = userName;
		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [ 'id','xsjbxxId','kssj','jssj','qjly' ]
		});

		/*var store = me.createStore({
			modelName : 'ModelList',
			proxyUrl : appBaseUri + '/app/attdDraft/getQjxx',
			proxyDeleteUrl : appBaseUri + '/sys/stuattdtype/deleteStuattdType',
			extraParams : me.extraParams
		});*/
		var store = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/attd/attdSearch/getQjxx?xsjbxxId='+xsjbxxId,
				extraParams : me.extraParams || null,
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'id',
				direction : 'ASC'
			} ]
		});

		/*var filters = {
			ftype : 'filters',
			encode : true,
			filters : [ {
				type : 'string',
				dataIndex : 'id'
			},{
				type : 'string',
				dataIndex : 'xsjbxxId'
			}, {
				type : 'string',
				dataIndex : 'kssj'
			}, {
				type : 'string',
				dataIndex : 'jssj'
			}, {
				type : 'string',
				dataIndex : 'qjly'
			} ]
		}*/
		
		var columns = [{
			text : "编号",
			dataIndex : 'id'
		},{
			text : "请假人",
			dataIndex : 'xsjbxxId'
		}, {
			text : "开始时间",
			dataIndex : 'kssj'
		}, {
			text : "结束时间",
			dataIndex : 'jssj'
		}, {
			text : "请假理由",
			dataIndex : 'qjly'
			
		}, {
			text : "操作",
			xtype : 'actioncolumn',
			width : '15%',
			items : [ {
				iconCls : 'icon-view',
				//disabled : !globalObject.haveActionMenu(me.cButtons, 'View'),
				tooltip : '查看',
				handler : function(grid, rowIndex, colIndex) {
					var gridRecord = grid.getStore().getAt(rowIndex);
					var win = new App.StuattdTypeListWindow({
						hidden : true
					});
					
					var form = win.down('form').getForm();
					form.loadRecord(gridRecord);
					form.findField('kssj').setReadOnly(true);
					form.findField('jssj').setReadOnly(true);
					form.findField('qjly').setReadOnly(true);
					form.findField('jzgjbxxId').setReadOnly(true);
					Ext.getCmp('confirm').show();
					Ext.getCmp('clear').hide();
					Ext.getCmp('save').hide();
					Ext.getCmp('send').hide();
					win.show();
				}
			}, {
				iconCls : 'edit',
				//disabled : !globalObject.haveActionMenu(me.cButtons, 'Edit'),
				tooltip : '修改',
				handler : function(grid, rowIndex, colIndex) {
					var gridRecord = grid.getStore().getAt(rowIndex);
					var win = new App.StuattdTypeListWindow({
						hidden : true
					});
					
					var form = win.down('form').getForm();
					Ext.getCmp('save').hide();
					Ext.getCmp('send').hide();
					Ext.getCmp('Usave').show();
					Ext.getCmp('Usend').show();
					form.loadRecord(gridRecord);
					//Ext.getCmp('confirm').hide();
					win.show();
				}
			}, {
				iconCls : 'icon-delete',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Delete'),
				tooltip : '删除',
				handler : function(grid, rowIndex, colIndex) {
					var entity = grid.getStore().getAt(rowIndex);
					singleId = entity.get('id');
					me.onDeleteClick();
					
				}
			} ]
		} ];
		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'textfield',
				id : 'applysearch-xsjbxxId',
				name : 'xsjbxxIdquery',
				fieldLabel : '请假人',
				labelWidth : 60,
				width : '25%'
			}, {
				xtype : 'datefield',
				id : 'applysearch-kssjquery',
				name : 'kssjquery',
				fieldLabel : '开始时间',
				labelWidth : 60,
				width : '30%'
			}, {
				xtype : 'datefield',
				id : 'applysearch-jssjquery',
				name : 'jssjquery',
				fieldLabel : '结束时间',
				labelWidth : 60,
				width : '30%'
			}, {
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						param : Ext.getCmp('applysearch-xsjbxxId').getValue(),
						kssj : Ext.util.Format.date(Ext.getCmp('applysearch-kssjquery').getValue(),'Y-m-d'),
						jssj : Ext.util.Format.date(Ext.getCmp('applysearch-jssjquery').getValue(),'Y-m-d')
						
					};
					Ext.apply(store.proxy.extraParams, searchParams);
					store.reload();
				}
			}, {
				xtype : 'button',
				text : '重置',
				iconCls : 'icon-reset',
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					Ext.getCmp('applysearch-xsjbxxId').setValue(null);
					Ext.getCmp('applysearch-kssjquery').setValue(null);
					Ext.getCmp('applysearch-jssjquery').setValue(null);
				}
			} ]
		});
		Ext.apply(this, {
			store : store,
			tbar : ttoolbar,
			// features : [ filters ],
			columns : columns
		});

		store.loadPage(1);

		this.callParent(arguments);
	}

});