/**
 * @开发商:jit.gwm
 */
 
 // 用户管理
Ext.onReady(function() {
	Ext.tip.QuickTipManager.init();

	//登录的学生ID：xsjbxxId = 1
	var xsjbxxId = userName;
	
	//获取可批假的老师
	/* 此处store为异步方式，会造成意想不到的错误，采用ajax同步操作则没问题
	 * var approveTeacherStore = Ext.create('Ext.data.JsonStore', {
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/attd/attdApply/getApproveTeachers?xsjbxxId='+xsjbxxId,
				reader : {
					type : 'json',
					root : 'list',
					idProperty : 'ItemValue'
				}
			},
			fields : [ 'ItemText', 'ItemValue' ]
		});	

	var itemsInTeacherGroup = [];
	approveTeacherStore.load({
		callback: function() { 	
			alert("dd");
			this.each( function(record) {
				  alert(record.get('ItemText'));
				  itemsInTeacherGroup.push( {
				      boxLabel: record.get('ItemText'), 
				      name: 'teachers', 
				      inputValue: record.get('ItemValue')
				    });  
			});	
		
		}
	});*/
	
	var itemsInTeacherGroup = [];
	Ext.Ajax.request({
				url : appBaseUri + '/attd/attdApply/getApproveTeachers?xsjbxxId='+xsjbxxId,
				method : "GET",
			    async: false, 
				success : function(response) {
					if (response.responseText != '') {
						//alert(response.responseText);
						var res = Ext.JSON.decode(response.responseText);
						//返回json-root为list
						//alert(res.list[0].ItemText);
						Ext.each( res.list,function(record) {
							  //alert(record.ItemText);
							  itemsInTeacherGroup.push( {
							      boxLabel: record.ItemText, 
							      name: 'jzgjbxxId', 
							      inputValue: record.ItemValue
							    }); 
						});	
							
						
					}
				},
				failure : function(response) {
					globalObject.errTip('返回审批老师操作失败！');
				}
	});
	
	
	


 Ext.define('Stuattd.app.attdApply.ApplyAction', {
    extend: 'Ext.form.Panel',
	xtype: 'ApplyAction',
    requires: [
        'Ext.form.field.Date',
        'Ext.form.field.TextArea',
        'Ext.form.RadioGroup',
        'Ext.form.field.Radio',
        'Ext.button.Button'
    ],

    height: 425,
    width: 633,
    bodyPadding: 10,
    initComponent: function() {
    	/*if (mainTab.getComponent('tab27')) {
			mainTab.getComponent('tab27').close();
		}*/
        var me = this;

        Ext.applyIf(me, {
            items: [
                 {
					name : "cmd",
					xtype : "hidden",
					value : 'new'
				}, {
					name : 'id',
					xtype : 'hidden',
					id : 'id'
				},
				{
						name : "xsjbxxId",
						xtype : "hidden",
						value : xsjbxxId
				}, 
				{
					layout:'column',
					items: [
					       {
					    	 //columnwidth:.5,
					    	 xtype: 'datefield',
			                 anchor: '100%',
			                 name: 'kssj',
			                 id: 'kssj',
			                 fieldLabel: '请假开始时间',
			                 minValue: new Date(),
			                 format: 'Y-m-d',
			                 listeners:{
			                	 'select':function(){
			                		 var kssj =  Ext.util.Format.date(Ext.getCmp('kssj').getValue(),'Y-m-d');
			                		 //alert(start);
			                		 Ext.getCmp('jssj').setMinValue(new Date(kssj));
			                	 }
			                 }
					       }, 
					       {
					    	 //columnwidth:.5,
					    	 xtype: 'datefield',
			                 anchor: '100%',
			                 name: 'jssj',
			                 id: 'jssj',
			                 fieldLabel: '请假结束时间',
			                 format: 'Y-m-d',
			                 minValue: new Date(),
			                 listeners:{
			                	 'select':function(){
			                		 var jssj =  Ext.util.Format.date(Ext.getCmp('jssj').getValue(),'Y-m-d');
			                		 //alert(start);
			                		 Ext.getCmp('kssj').setMaxValue(new Date(jssj));
			                	 }
			                 }
					       }
					        ]
				},
               /* {
                    xtype: 'datefield',
                    anchor: '100%',
                    name: 'kssj',
                    fieldLabel: '请假开始时间：'
                },
                {
                    xtype: 'datefield',
                    anchor: '100%',
                    name: 'jssj',
                    fieldLabel: '请假结束时间：'
                },*/
                {
                    xtype: 'textareafield',
                    anchor: '50%',
                    name: 'qjly',
                    id : 'qjly',
                    height: 201,
                    fieldLabel: '请假理由'
                },
                {
                    xtype: 'radiogroup',
                    id : 'jzgjbxxId',
                    fieldLabel: '选择审批老师',
                    items: itemsInTeacherGroup,
                    layout:'table'
                },
       				
                {
                    xtype: 'button',
                    id: 'clear',
                    text: '清空',
                    handler: function(btn,enentObj){
                    	var form  = btn.up("form").getForm();
                    	if(form.isValid()){
                    		form.findField('kssj').setValue(null);
                    		form.findField('jssj').setValue(null);
                    		form.findField('qjly').setValue(null);
                    	}
                    }
                },
                {
                    xtype: 'button',
                    id: 'save',
                    text: '保存到草稿箱',
                  	handler : function(btn, eventObj) {
                    		var form  = btn.up("form").getForm();		
							if (form.isValid()) {							
								var vals = form.getValues();
								var qjzt=3;
								var action = 'save';
								me.onAction(action,qjzt,vals);
							} 
					} 
                }, {
                    xtype: 'button',
                    hidden : true,
                    id: 'Usave',
                    text: '保存到草稿箱',
                  	handler : function(btn, eventObj) {
                    		var form  = btn.up("form").getForm();		
							if (form.isValid()) {							
								var vals = form.getValues();
								var qjzt=3;
								var action = 'update';
								me.onAction(action,qjzt,vals);
							} 
					} 
                },{
                	xtype: 'button',
                	id: 'confirm',
                	text: '确定',
                	handler : function() {
						this.up('window').close();
						globalObject.openTab('27', '请假单草稿箱', Ext.create('Stuattd.app.attdApply.ApplyDraft', {
			
						}));
					},
                	hidden : true
                },{
                	xtype: 'button',
                	id: 'confirmd',
                	text: '确定',
                	handler : function() {
						this.up('window').close();
					
			
					
					},
                	hidden : true
                },
                {
                    xtype: 'button',
                    text: '发送请假单',
                    id: 'send',
                    handler : function(btn, eventObj) {
                    		var form  = btn.up("form").getForm();		
							if (form.isValid()) {							
								var vals = form.getValues();
								var qjzt=1;
								var action = 'save';
								me.onAction(action,qjzt,vals);
							} 
							this.up('window').close();
						} 
                    }, {
                    xtype: 'button',
                    hidden : true,
                    text: '发送请假单',
                    id: 'Usend',
                    handler : function(btn, eventObj) {
                    	var form  = btn.up("form").getForm();		
						if (form.isValid()) {							
							var vals = form.getValues();
							var qjzt=1;
							var action = 'update';
							me.onAction(action,qjzt,vals);
						}
						this.up('window').close();
					} 
              }]
        });

        me.callParent(arguments);
    },
    onAction : function(action,qjzt,vals){
    	Ext.Ajax.request({
			url : appBaseUri + '/attd/attdApply/Action',
			params : {
				cmd : vals['cmd'],
				id : vals['id'] || 0,
				xsjbxxId : vals['xsjbxxId'],
				kssj : vals['kssj'],
				jssj : vals['jssj'],
				qjly : vals['qjly'],
				jzgjbxxId : vals['jzgjbxxId'],
				qjzt : qjzt,
				action : action
			},
			method : "POST",
			success : function(response) {
				if (response.responseText != '') {
					var res = Ext.JSON.decode(response.responseText);
					if (res.success) {
						globalObject.msgTip('操作成功！');
					} else {
						globalObject.errTip('操作失败！');
					}
				}
			},
			failure : function(response) {
				globalObject.errTip('请假单操作失败！');
			}
		});
    }

});


}); //end onready