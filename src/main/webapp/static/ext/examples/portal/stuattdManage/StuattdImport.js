/**
 * @开发商:jit.gwm
 */
// 树木识别数据导入
Ext.onReady(function() {
	Ext.tip.QuickTipManager.init();

	var stuattdTypeNameStore = Ext.create('Ext.data.JsonStore', {
		proxy : {
			type : 'ajax',
			url : appBaseUri + '/sys/stuattd/getStuattdTypeName',
			reader : {
				type : 'json',
				root : 'list',
				idProperty : 'ItemValue'
			}
		},
		fields : [ 'ItemText', 'ItemValue' ]
	});

	Ext.define('App.ImportWindow', {
		extend : 'Ext.window.Window',
		constructor : function(config) {
			config = config || {};
			var scope = this;
			Ext.apply(config, {
				title : '树木识别数据导入',
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
							html : '2、支持Microsoft Office Excel的xls和xlsx文件,模板<a href="' + appBaseUri + '/sys/stuattd/downloadImportedFile")>点此下载.</a>'
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
					url : appBaseUri + '/sys/stuattd/importStuattdFile',
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
						Ext.getCmp('stuattdimportgrid').getStore().reload();
					},
					failure : function(response, options) {
						globalObject.errTip(options.result.msg);
					}
				});
			}
		}
	});

	Ext.define('App.StuattdImportWindow', {
		extend : 'Ext.window.Window',
		constructor : function(config) {
			config = config || {};
			Ext.apply(config, {
				title : '树木识别信息',
				width : 350,
				height : 250,
				bodyPadding : '10 5',
				modal : true,
				layout : 'fit',
				items : [ {
					xtype : 'form',
					fieldDefaults : {
						labelAlign : 'left',
						labelWidth : 90,
						anchor : '100%'
					},
					items : [ {
						name : "cmd",
						xtype : "hidden",
						value : 'new'
					}, {
						xtype : 'hiddenfield',
						name : 'id'
					}, {
						xtype : 'textfield',
						name : 'epcId',
						fieldLabel : 'epc编码',
						maxLength : 100,
						allowBlank : false
					}, {
						xtype : 'textfield',
						name : 'name',
						fieldLabel : '名称',
						maxLength : 200,
						allowBlank : false
					}, {
						xtype : 'datefield',
						name : 'plantTime',
						fieldLabel : '种植时间',
						format : 'Y-m-d H:i:s',
						// maxValue : new Date(),
						// value : new Date(),
						allowBlank : true
					}, {
						xtype : 'datefield',
						name : 'entryTime',
						fieldLabel : '入园时间',
						format : 'Y-m-d H:i:s',
						allowBlank : true
					}, {
						xtype : 'combobox',
						fieldLabel : '所属种类名称',
						name : 'stuattdTypeName',
						store : stuattdTypeNameStore,
						valueField : 'ItemValue',
						displayField : 'ItemText',
						typeAhead : true,
						queryMode : 'remote',
						emptyText : '请选择...',
						allowBlank : false,
						editable : false,
						listeners : {
							select : function(combo, record, index) {
								Ext.getCmp("stuattdImportForm-stuattdTypeId").setValue(combo.getValue());
							}
						}
					}, {
						xtype : 'hiddenfield',
						id : 'stuattdImportForm-stuattdTypeId',
						name : 'stuattdTypeId'
					} ],
					buttons : [ '->', {
						id : 'stuattdimportwindow-save',
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
									url : appBaseUri + '/sys/stuattd/saveStuattd',
									params : {
										cmd : vals['cmd'],
										id : vals['id'],
										epcId : vals['epcId'],
										name : vals['name'],
										plantTime : vals['plantTime'],
										entryTime : vals['entryTime'],
										stuattdTypeId : vals['stuattdTypeId']
									},
									method : "POST",
									success : function(response) {
										// var result = eval("(" + response.responseText + ")");
										globalObject.msgTip('操作成功！');
										Ext.getCmp('stuattdimportgrid').getStore().reload();
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
						id : 'stuattdimportwindow-cancel',
						text : '取消',
						iconCls : 'icon-cancel',
						width : 80,
						handler : function() {
							this.up('window').close();
						}
					}, {
						id : 'stuattdimportwindow-accept',
						text : '确定',
						hidden : true,
						iconCls : 'icon-accept',
						width : 80,
						handler : function() {
							this.up('window').close();
						}
					}, '->' ]
				} ]
			});
			App.StuattdImportWindow.superclass.constructor.call(this, config);
		}
	});

	Ext.define('Stuattd.app.stuattdManage.StuattdImport', {
		extend : 'Ext.ux.custom.GlobalGridPanel',
		region : 'center',
		initComponent : function() {
			var scope = this;

			Ext.define('ModelList', {
				extend : 'Ext.data.Model',
				idProperty : 'id',
				fields : [ {
					name : 'id',
					type : 'long'
				}, 'epcId', 'name', {
					name : 'plantTime',
					type : 'datetime',
					dateFormat : 'Y-m-d H:i:s'
				}, {
					name : 'entryTime',
					type : 'datetime',
					dateFormat : 'Y-m-d H:i:s'
				}, 'stuattdTypeName', 'stuattdTypeId' ]
			});

			var store = scope.createStore({
				modelName : 'ModelList',
				proxyUrl : appBaseUri + '/sys/stuattd/getStuattd',
				proxyDeleteUrl : appBaseUri + '/sys/stuattd/deleteStuattd',
				proxyExportUrl : appBaseUri + '/sys/stuattd/exportStuattd',
				extraParams : scope.extraParams
			});

			var columns = [ {
				text : "ID",
				dataIndex : 'id',
				width : '5%'
			}, {
				text : "epc编码",
				dataIndex : 'epcId',
				width : '18%',
				sortable : false
			}, {
				text : "名称",
				dataIndex : 'name',
				width : '17%'
			}, {
				text : "种植时间",
				dataIndex : 'plantTime',
				width : '16%'
			}, {
				text : "入园时间",
				dataIndex : 'entryTime',
				width : '16%'
			}, {
				text : "所属种类名称",
				dataIndex : 'stuattdTypeName',
				width : '16%',
				sortable : false
			}, {
				text : "stuattdTypeId",
				dataIndex : 'stuattdTypeId',
				hidden : true,
				sortable : false
			}, {
				xtype : 'actioncolumn',
				width : '8%',
				items : [ {
					iconCls : 'icon-view',
					tooltip : '查看',
					disabled : !globalObject.haveActionMenu(scope.cButtons, 'View'),
					handler : function(grid, rowIndex, colIndex) {
						var gridRecord = grid.getStore().getAt(rowIndex);
						var win = new App.StuattdImportWindow({
							hidden : true
						});
						var form = win.down('form').getForm();
						form.loadRecord(gridRecord);
						form.findField('epcId').setReadOnly(true);
						form.findField('name').setReadOnly(true);
						form.findField('plantTime').setReadOnly(true);
						form.findField('entryTime').setReadOnly(true);
						form.findField('stuattdTypeName').setReadOnly(true);
						Ext.getCmp('stuattdimportwindow-save').hide();
						Ext.getCmp('stuattdimportwindow-cancel').hide();
						Ext.getCmp('stuattdimportwindow-accept').show();
						win.show();
					}
				}, {
					iconCls : 'edit',
					tooltip : '修改',
					disabled : !globalObject.haveActionMenu(scope.cButtons, 'Edit'),
					handler : function(grid, rowIndex, colIndex) {
						var gridRecord = grid.getStore().getAt(rowIndex);
						var win = new App.StuattdImportWindow({
							hidden : true
						});
						var form = win.down('form').getForm();
						form.loadRecord(gridRecord);
						form.findField("cmd").setValue("edit");
						win.show();
					}
				}, {
					iconCls : 'icon-delete',
					tooltip : '删除',
					disabled : !globalObject.haveActionMenu(scope.cButtons, 'Delete'),
					handler : function(grid, rowIndex, colIndex) {
						var entity = grid.getStore().getAt(rowIndex);
						singleId = entity.get('id');
						scope.onDeleteClick();
					}
				} ]
			} ];

			Ext.apply(this, {
				id : 'stuattdimportgrid',
				store : store,
				columns : columns
			});

			store.loadPage(1);

			this.callParent(arguments);
		},
		onAddClick : function() {
			new App.StuattdImportWindow().show();
		},
		onImportClick : function() {
			new App.ImportWindow().show();
		},
		onViewClick : function() {
			var grid = Ext.getCmp("stuattdimportgrid");
			var id = grid.getSelectionModel().getSelection()[0].get('id');
			var gridRecord = grid.getStore().findRecord('id', id);
			var win = new App.StuattdImportWindow({
				hidden : true
			});
			var form = win.down('form').getForm();
			form.loadRecord(gridRecord);
			form.findField('epcId').setReadOnly(true);
			form.findField('name').setReadOnly(true);
			form.findField('plantTime').setReadOnly(true);
			form.findField('entryTime').setReadOnly(true);
			form.findField('stuattdTypeName').setReadOnly(true);
			Ext.getCmp('stuattdimportwindow-save').hide();
			Ext.getCmp('stuattdimportwindow-cancel').hide();
			Ext.getCmp('stuattdimportwindow-accept').show();
			win.show();
		}
	});
});