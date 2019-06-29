//生成菜单
var menuItem = Vue.extend({
    name: 'menu-item',
    props:{item:{}},
    template:[
        '<li>',
        '	<a v-if="item.type === 0" href="javascript:;">',
        '		<i v-if="item.icon != null" :class="item.icon"></i>',
        '		<span>{{item.name}}</span>',
        '		<i class="fa fa-angle-left pull-right"></i>',
        '	</a>',
        '	<ul v-if="item.type === 0" class="treeview-menu">',
        '		<menu-item :item="item" v-for="item in item.list"></menu-item>',
        '	</ul>',

        '	<a v-if="item.type === 1 && item.parentId === 0" :href="\'#\'+item.url">',
        '		<i v-if="item.icon != null" :class="item.icon"></i>',
        '		<span>{{item.name}}</span>',
        '	</a>',

        '	<a v-if="item.type === 1 && item.parentId != 0" :href="\'#\'+item.url"><i v-if="item.icon != null" :class="item.icon"></i><i v-else class="fa fa-circle-o"></i> {{item.name}}</a>',
        '</li>'
    ].join('')
});

//iframe自适应
$(window).on('resize', function() {
	var $content = $('.content');
	$content.height($(this).height() - 154);
	$content.find('iframe').each(function() {
		$(this).height($content.height());
	});

}).resize();

//注册菜单组件
Vue.component('menuItem',menuItem);
var vm = new Vue({
	el:'#rrapp',
	data:{
		user:{},
		menuList:{},
		main:"main.html",
		password:'',
		newPassword:'',
        navTitle:"控制台"
	},
	methods: {
		getMenuList: function (event) {
			$.getJSON("sys/menu/nav?_"+$.now(), function(r){
				vm.menuList = r.menuList;
			});
		},
		getUser: function(){
			$.getJSON("sys/user/info?_"+$.now(), function(r){
				vm.user = r.user;
			});
		},
		updatePassword: function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: "修改密码",
				area: ['550px', '270px'],
				shadeClose: false,
				content: jQuery("#passwordLayer"),
				btn: ['修改','取消'],
				btn1: function (index) {
					var data = "password="+vm.password+"&newPassword="+vm.newPassword;
					$.ajax({
						type: "POST",
					    url: "sys/user/password",
					    data: data,
					    dataType: "json",
					    success: function(result){
							if(result.code == 200){
								layer.close(index);
								layer.alert('修改成功', function(index){
									location.reload();
								});
							}else{
								layer.alert(result.msg);
							}
						}
					});
	            }
			});
		},
        donate: function () {
            // layer.open({
            //     type: 2,
            //     title: false,
            //     area: ['806px', '467px'],
            //     closeBtn: 1,
            //     shadeClose: false,
            //     content: ['http://cdn.shebei.io/donate.jpg', 'no']
            // });
        }
	},
	created: function(){
		this.getMenuList();
		this.getUser();
	},
	updated: function(){
		//路由
		var router = new Router();
		routerList(router, vm.menuList);
		router.start();
	}
});



function routerList(router, menuList){
	for(var key in menuList){
		var menu = menuList[key];
		if(menu.type == 0){
			routerList(router, menu.list);
		}else if(menu.type == 1){
			router.add('#'+menu.url, function() {
				var url = window.location.hash;
				
				//替换iframe的url
			    vm.main = url.replace('#', '');
			    
			    //导航菜单展开
			    $(".treeview-menu li").removeClass("active");
			    $("a[href='"+url+"']").parents("li").addClass("active");
			    
			    vm.navTitle = $("a[href='"+url+"']").text();
			});
		}
	}
	router.add('#modules/informa/informa.html', function() {
		var url = window.location.hash;
		vm.main = url.replace('#', '');
	});
}
//点击弹窗
$("#r_val_eml").click(function(){
    $(".r_lay").css("display","block")
})
//点击获取验证码
$("#btn").click(function(){
    var r_val_eml = $("#r_rlm").val()
	$("#r_upd").val(r_val_eml)
    // console.log(r_val_eml)
		$.ajax({
			url:  'contact/sendBindEmail',
			type: "GET",
			data: "&email=" + r_val_eml ,
			success: function (res) {
				console.log(JSON.stringify(res));
				if (res.code == "200") {
					console.log("///////////////////////")
                    ryanzhengma()
				} else {
					console.log(res.msg);
			}}
	  })
})
//提交  验证码  邮箱
function ryanzhengma(){
    $("#r_bte").click(function(){
        var r_upd = $("#r_upd").val();
        var r_el_upd = $("#r_el_upd").val()
        $.ajax({
            url:  'contact/checkBindEmail',
            type: "GET",
            data: "&email=" + r_upd +
            "&securityCode=" + r_el_upd ,
            success: function (res) {
                console.log(JSON.stringify(res));
                if (res.code == "200") {
                    console.log("///////////////////////")
                    $(".r_lay").css("display","none")
                } else {
                    console.log(res.msg);
                }}
        })
    })
}
//进入页面获取数据
$.ajax({
    url:  'sys/user/baseInfo',
    type: "POST",
    success: function (res) {
        console.log(JSON.stringify(res));
        if (res.code == "200") {
        	console.log(res)
            var r_ide = res.user.userId;
            $("#r_ide").val(r_ide)
            var select1 = res.user.roleId;
			$("#acc_select_b option:selected").text(select1);
            var r_ide = res.user.userId;
            $("#r_ide").val(r_ide)


            $(".r_lay").css("display","none")
        } else {
            console.log(res.msg);
        }}
})

$("#r_tijiao").click(function(){

})


//倒计时60秒
var countdown=60;
function sendemail(){
    var obj = $("#btn");
    settime(obj);

}
function settime(obj) { //发送验证码倒计时
    if (countdown == 0) {
        obj.attr('disabled',false);
        //obj.removeattr("disabled");
        obj.val("获取验证码");
        countdown = 60;
        return;
    } else {
        obj.attr('disabled',true);
        obj.val("重新发送(" + countdown + ")");
        countdown--;
    }
    setTimeout(function() {
            settime(obj) }
        ,1000)
}









