$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/feedback/list',
        datatype: "json",
        colModel: [			
			{ label: 'backId', name: 'backId', index: 'back_id', width: 50, key: true },
			{ label: '反馈用户id', name: 'uid', index: 'uid', width: 80 }, 			
			{ label: '反馈内容', name: 'backContent', index: 'back_content', width: 80 }, 			
			{ label: '时间', name: 'backCreateTime', index: 'back_create_time', width: 80 }, 			
			{ label: '反馈解答内容', name: 'answerContent', index: 'answer_content', width: 80 }, 			
			{ label: '反馈解答人', name: 'answerUser', index: 'answer_user', width: 80 }, 			
			{ label: '反馈解答时间', name: 'answerCreateTime', index: 'answer_create_time', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		feedback: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.feedback = {};
		},
		update: function (event) {
			var backId = getSelectedRow();
			if(backId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(backId)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.feedback.backId == null ? "sys/feedback/save" : "sys/feedback/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.feedback),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});

		},
		del: function (event) {
			var backIds = getSelectedRows();
			if(backIds == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "sys/feedback/delete",
                        contentType: "application/json",
                        data: JSON.stringify(backIds),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(backId){
			$.get(baseURL + "sys/feedback/info/"+backId, function(r){
                vm.feedback = r.feedback;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        message:function(event){
            location.replace('../message/message.html');
        }
	}
});