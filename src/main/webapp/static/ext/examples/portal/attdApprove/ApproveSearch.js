 	/**
 * @开发商:jit.gwm
 */


Ext.define('Stuattd.app.attdApprove.ApproveSearch', {
	extend : 'Ext.ux.custom.NewGlobalGridPanel',
	id : 'approvesearchgrid',
	xtype : 'ApproveSearch',
	region : 'center',
	
	initComponent : function() {
		/*if (mainTab.getComponent('tab30')) {
			mainTab.getComponent('tab30').close();
		}*/
		var me = this;
		//var xsjbxxId = 1;
		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [ 'xh','xm','kssj','jssj','qjly','topCount','sfpj' ]
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
				url : appBaseUri + '/attd/ApproveSearch/getQjxx',
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
			text : "flag",
			dataIndex : 'topCount',
			hidden : true
		
		},{
			text : '是否批假',
			dataIndex : 'sfpj'
		}];
		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'combobox',
				fieldLabel : '班级',
				id : 'ApproveSearch-ClassName',
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
						var bh = Ext.getCmp('ApproveSearch-ClassName').getValue();
						var searchParams = {
							bh : bh
						};
						Ext.apply(store.proxy.extraParams, searchParams);
						store.reload();
					}
				}
			}, {
				xtype : 'textfield',
				id : 'ApproveSearch-StuName',
				name : 'stuattdStuName',
				fieldLabel : '姓名',
				emptyText : '请输入学生姓名',
				labelWidth : 60,
				width : '30%',
				listeners:{
					change : function(){
						var bh = Ext.getCmp('ApproveSearch-ClassName').getValue();
						var xm = Ext.getCmp('ApproveSearch-StuName').getValue();
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
			id : 'approvesearchgrid',
			store : store,
			tbar : ttoolbar,
			// features : [ filters ],
			columns : columns
		});

		store.loadPage(1);

		this.callParent(arguments);
	}

});