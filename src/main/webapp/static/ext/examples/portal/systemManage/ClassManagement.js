/**
 * @开发商:jit.gwm
 */

Ext.define('App.ClassManagementWindow', {
	extend : 'Ext.window.Window',
	constructor : function(config) {
		config = config || {};
		
		Ext.apply(config, {
			title : 'ActionWindow',
			width : 400,
			height : 200,
			bodyPadding : '10 5',
			// maximizable: true,
			modal : true,
			layout : 'fit',
			items : [{
				xtype : 'form',
				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 90,
					anchor : '100%'
				},items : [ {
					xtype : 'hiddenfield',
					name : 'id'
				},{
					xtype : 'textfield',
					name : 'bh',
					fieldLabel : '班号',
					allowBlank : false,
					maxLength : 30
					
				},{
					xtype : 'textfield',
					name : 'bjmc',
					fieldLabel : '班级名称',
					allowBlank : false,
					maxLength : 30
				}],
				buttons : [ '->', {
					id : 'classmanagementwindow-save',
					text : '保存',
					iconCls : 'icon-save',
					width : 80,
					handler : function(btn, eventObj) {
						var window = btn.up('window');
						var form = window.down('form').getForm();
						if (form.isValid()) {
							window.getEl().mask('数据保存中，请稍候...');
							var vals = form.getValues();
							Ext.Ajax.request({
								url : appBaseUri + '/sys/ClassManagement/saveClass',
								params : {
									
									bh : vals['bh'],
									bjmc : vals['bjmc'],
									
								},
								method : "POST",
								success : function(response) {
									if (response.responseText != '') {
										var res = Ext.JSON.decode(response.responseText);
										if (res.success) {
											globalObject.msgTip('操作成功！');
											Ext.getCmp('classmanagementgrid').getStore().reload();
										} else {
											globalObject.errTip('用户名已存在，请输入唯一值！');
										}
									}
								},
								failure : function(response) {
									globalObject.errTip('操作失败！');
								}
							});
							window.getEl().unmask();
							window.close();
						}
					}
				}, {
					id : 'classmanagementwindow-cancel',
					text : '取消',
					iconCls : 'icon-cancel',
					width : 80,
					handler : function() {
						this.up('window').close();
					}
				}, {
					id : 'classmanagementwindow-accept',
					text : '确定',
					hidden : true,
					iconCls : 'icon-accept',
					width : 80,
					handler : function() {
						this.up('window').close();
					}
				}, '->' ]
			}]
	
		});
		App.ClassManagementWindow.superclass.constructor.call(this, config);
	}
});
Ext.define('App.ImportWindow', {
	extend : 'Ext.window.Window',
	constructor : function(config) {
		config = config || {};
		var scope = this;
		Ext.apply(config, {
			title : '班级信息导入',
			width : 500,
			height : 190,
			bodyPadding : '10 5',
			modal : true,
			layout : 'fit',
			items : [ {
				xtype : 'form',
				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 70,
					anchor : '100%'
				},
				items : [ {
					xtype : 'fileuploadfield',
					fieldLabel : '选择文件',
					afterLabelTextTpl : '<span style="color:#FF0000;">*</span>',
					buttonText : '请选择...',
					name : 'importedFile',
					emptyText : '请选择Excel文件',
					blankText : 'Excel文件不能为空',
					allowBlank : false,
					listeners : {
						change : function(view, value, eOpts) {
							scope.checkImportFile(view, value);
						}
					}
				}, {
					columnWidth : 1,
					xtype : 'fieldset',
					title : '导入须知',
					layout : {
						type : 'table',
						columns : 1
					},
					collapsible : false,// 是否可折叠
					defaultType : 'label',// 默认的Form表单组件
					items : [ {
						html : '1、导入文件大小不超过2MB.'
					}, {
						html : '2、支持Microsoft Office Excel的xls和xlsx文件,模板<a href="' + appBaseUri + '/sys/ClassManagement/downloadImportedFile?name=class")>点此下载.</a>'
					} ]
				} ],
				buttons : [ '->', {
					text : '导入',
					iconCls : 'icon-excel',
					handler : function(btn) {
						scope.importStuattdFile(btn);
					}
				}, {
					text : '取消',
					iconCls : 'icon-cancel',
					handler : function(btn) {
						btn.up('window').close();
					}
				}, '->' ]
			} ]
		});
		App.ImportWindow.superclass.constructor.call(this, config);
	},
	checkImportFile : function(fileObj, fileName) {
		var scope = this;
		if (!(scope.getFileType(scope.getFileSuffix(fileName)))) {
			globalObject.errTip('导入文件类型有误！');
			fileObj.reset();// 清空上传内容
			return;
		}
	},
	getFileType : function(suffix) {
		var typestr = 'xls,xlsx';
		var types = typestr.split(',');
		for (var i = 0; i < types.length; i++) {
			if (suffix == types[i]) {
				return true;
			}
		}
		return false;
	},
	getFileSuffix : function(fileName) {
		var suffix = '';// 后缀
		var index = fileName.lastIndexOf('.');// 文件名称中最后一个.的位置
		if (index != -1) {
			suffix = fileName.substr(index + 1).toLowerCase();// 后缀转成小写
		}
		return suffix;
	},
	importStuattdFile : function(btn) {
		var windowObj = btn.up('window');// 获取Window对象
		var formObj = btn.up('form');// 获取Form对象
		if (formObj.isValid()) { // 验证Form表单
			formObj.form.doAction('submit', {
				url : appBaseUri + '/sys/ClassManagement/importClassFile',
				method : 'POST',
				submitEmptyText : false,
				waitMsg : '正在导入文件,请稍候...',
				timeout : 60000, // 60s
				success : function(response, options) {
					var result = options.result;
					if (!result.success) {
						globalObject.errTip(result.msg);
						return;
					}
					globalObject.infoTip(result.msg);
					// var url = result.data;
					windowObj.close();// 关闭窗体
					Ext.getCmp('classmanagementgrid').getStore().reload();
				},
				failure : function(response, options) {
					globalObject.errTip(options.result.msg);
				}
			});
		}
	}
});
Ext.define('Stuattd.app.systemManage.ClassManagement', {
	extend : 'Ext.ux.custom.NewGlobalGridPanel',
	id : 'classmanagementgrid',
	xtype : 'ClassManagement',
	region : 'center',
	
	initComponent : function() {
		
		var me = this;
		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
			idProperty : 'bh',
			fields : [ 'id','bh','bjmc','jbny' ]
		});
		var store = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/ClassManagement/getBjmc',
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
			text : "班号",
			dataIndex : 'bh',
			width : '25%',
		},{
			text : "班级名称",
			dataIndex : 'bjmc',
			width : '25%',
		},{
			text : "建班日期",
			dataIndex : 'jbny',
			width : '25%',
		},{
			text : "操作",
			xtype : 'actioncolumn',
			width : '20%',
			items : [ {
				iconCls : 'icon-view',
				//disabled : !globalObject.haveActionMenu(me.cButtons, 'View'),
				tooltip : '查看',
				handler : function(grid, rowIndex, colIndex) {
					var gridRecord = grid.getStore().getAt(rowIndex);
					var win = new App.ClassManagementWindow({
						hidden : true
					});
					
					var form = win.down('form').getForm();
					form.loadRecord(gridRecord);
					form.findField('bh').setReadOnly(true);
					form.findField('bjmc').setReadOnly(true);
					Ext.getCmp('classmanagementwindow-save').hide();
					Ext.getCmp('classmanagementwindow-cancel').hide();
					Ext.getCmp('classmanagementwindow-accept').show();
					win.show();
				}
			}, {
				iconCls : 'edit',
				//disabled : !globalObject.haveActionMenu(me.cButtons, 'Edit'),
				tooltip : '修改',
				handler : function(grid, rowIndex, colIndex) {
					var gridRecord = grid.getStore().getAt(rowIndex);
					var win = new App.ClassManagementWindow({
						hidden : true
					});
					
					var form = win.down('form').getForm();
					//Ext.getCmp('save').hide();
					//Ext.getCmp('send').hide();
					//Ext.getCmp('Usave').show();
					//Ext.getCmp('Usend').show();
					//form.loadRecord(gridRecord);
					//Ext.getCmp('confirm').hide();
					win.show();
				}
			}, {
				iconCls : 'icon-delete',
			
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
				xtype : 'button',
				text : '添加',
				iconCls : 'icon-add',
				width : '10%',
				maxWidth : 100,
				handler : function(btn, eventObj) {
					me.onAddClick();
					store.reload();
				}
			},{
				xtype : 'button',
				text : '从Excel导入',
				iconCls : 'icon-excel',
				width : '15%',
				maxWidth : 100,
				handler : function(btn, eventObj) {
					me.onImportClick();
					store.reload();
				}
			}]
		});
		Ext.apply(this, {
			store : store,
			tbar : ttoolbar,
			// features : [ filters ],
			columns : columns
		});

		store.loadPage(1);

		this.callParent(arguments);
	},
	onAddClick : function() {
		var win = new App.ClassManagementWindow({
			hidden : true
		});
		win.show();
		
	},
	onImportClick : function() {
		var win = new App.ImportWindow({
			hidden : true
		});
		win.show();
		
	}
	

});