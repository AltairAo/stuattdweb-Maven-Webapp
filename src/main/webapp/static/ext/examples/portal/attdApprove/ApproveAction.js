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

Ext.define('Stuattd.app.attdApprove.ApproveAction', {
	extend : 'Ext.ux.custom.NewGlobalGridPanel',
	requires: [
	           'Stuattd.app.attdApply.ApplyAction'
	       ],
	id : 'approveactiongrid',
	xtype : 'ApproveAction',
	region : 'center',
	
	initComponent : function() {
		/*if (mainTab.getComponent('tab32')) {
			mainTab.getComponent('tab32').close();
		}*/
		var me = this;
		//var xsjbxxId = 1;
		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [ 'xh','xm','kssj','jssj','qjly' ]
		});
		Ext.define('ClassModelList', {
			extend : 'Ext.data.Model',
			idProperty : 'bh',
			fields : [ 'bh','bjmc' ]
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
				url : appBaseUri + '/attd/attdApprove/getQjxx',
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
		var classStore=Ext.create('Ext.data.Store',{
			model : 'ClassModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/attd/attdApprove/getBjmc',
				extraParams : me.extraParams || null,
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			}
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
			text : "请假人学号",
			dataIndex : 'xh'
		}, {
			text : "请假人姓名",
			dataIndex : 'xm'
		},{
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
				tooltip : '详情',
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
					Ext.getCmp('confirmd').show();
					Ext.getCmp('clear').hide();
					Ext.getCmp('save').hide();
					Ext.getCmp('send').hide();
					win.show();
				}
			}, {
				iconCls : 'icon-accept',
				//disabled : !globalObject.haveActionMenu(me.cButtons, 'Edit'),
				tooltip : '批假',
				handler : function(grid,rowIndex,colIndex){
					var entity = grid.getStore().getAt(rowIndex);
					singleId = entity.get('id');
					me.onPassClick();
				}
			}, {
				iconCls: 'icon-reset',
				tooltip : '退回',
				handler : function(grid,rowIndex,colIndex){
					var entity = grid.getStore().getAt(rowIndex);
					singleId = entity.get('id');
					me.onWithdrawClick();
				}
			},{
				iconCls : 'icon-cancel',
				tooltip : '不准',
				handler : function(grid, rowIndex, colIndex) {
					var entity = grid.getStore().getAt(rowIndex);
					singleId = entity.get('id');
					me.onNegativeClick();
					
				}
			} ]
		} ];
		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'combobox',
				fieldLabel : '班级',
				id : 'stuattdApprove-ClassName',
				name : 'stuattdClassName',
				store : classStore,
				valueField : 'bh',
				displayField : 'bjmc',
				typeAhead : true,
				queryMode : 'remote',
				emptyText : '请选择...',
				editable : false,
				labelWidth : 90,
				width : '35%',
				maxWidth : 320,
				listeners:{
					select : function() {
						var bh = Ext.getCmp('stuattdApprove-ClassName').getValue();
						var searchParams = {
							bh : bh
						};
						Ext.apply(store.proxy.extraParams, searchParams);
						store.reload();
					}
				}
			}, {
				xtype : 'textfield',
				id : 'stuattdApprove-StuName',
				name : 'stuattdStuName',
				fieldLabel : '姓名',
				emptyText : '请输入学生姓名',
				labelWidth : 60,
				width : '30%',
				listeners:{
					change : function(){
						var bh = Ext.getCmp('stuattdApprove-ClassName').getValue();
						var xm = Ext.getCmp('stuattdApprove-StuName').getValue();
						var searchParams = {
							bh : bh,
							xm : xm
						};
						if (bh==null){
       						//Ext.Msg.alert("提示","请先选择班级！");
							globalObject.msgTip('请先选择班级！');
						}else{
							Ext.apply(store.proxy.extraParams, searchParams);
							store.reload();
						}
						
					}
				}
			}]
		});
		Ext.apply(this, {
			id : 'approveactiongrid',
			store : store,
			tbar : ttoolbar,
			// features : [ filters ],
			columns : columns
		});

		store.loadPage(1);

		this.callParent(arguments);
	},
	onWithdrawClick : function() {
		var me = this;
			globalObject.confirmTip('确定要退回该请假单吗？', function(btn) {
			if (btn == 'yes') {
				Ext.Ajax.request({
					url : appBaseUri + '/attd/attdApprove/Withdraw',
					params : {
						id:singleId
					},
					success : function(response) {
						if (response.responseText != '') {
							var res = Ext.JSON.decode(response.responseText);
							if (res.success){
								globalObject.msgTip('操作成功！');
								// Ext.example.msg('系统信息', '{0}', "操作成功！");
								me.getStore().reload();
							} else {
								globalObject.errTip('操作失败！' + res.msg);
							}
						}
					}
				});
			}
		});
	},
	onNegativeClick : function(){
		var me = this;
		Ext.MessageBox.prompt("提示","请输入不批原因：",
			function (btn, text) {
				if (btn == 'ok') {
					Ext.Ajax.request({
						url : appBaseUri + '/attd/attdApprove/Negative',
						params : {
							id:singleId,
							bpyy : text
						},
						success : function(response) {
							if (response.responseText != '') {
								var res = Ext.JSON.decode(response.responseText);
								if (res.success){
									globalObject.msgTip('操作成功！');
									// Ext.example.msg('系统信息', '{0}', "操作成功！");
									me.getStore().reload();
								} else {
									globalObject.errTip('操作失败！' + res.msg);
								}
							}
						}
					});
				}
			},
			this,
    		true    
    	);
	},
	onPassClick : function(){
		var me = this;
			globalObject.confirmTip('确定要批准该请假单吗？', function(btn) {
			if (btn == 'yes') {
				Ext.Ajax.request({
					url : appBaseUri + '/attd/attdApprove/Pass',
					params : {
						id:singleId
					},
					success : function(response) {
						if (response.responseText != '') {
							var res = Ext.JSON.decode(response.responseText);
							if (res.success){
								globalObject.msgTip('操作成功！');
								// Ext.example.msg('系统信息', '{0}', "操作成功！");
								me.getStore().reload();
							} else {
								globalObject.errTip('操作失败！' + res.msg);
							}
						}
					}
				});
			}
		});
	}

});