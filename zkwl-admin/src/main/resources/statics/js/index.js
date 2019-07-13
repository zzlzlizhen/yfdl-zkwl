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
    $(".r_beise").css("display","block")
    $(".r_lay_a").css("display","none")
})
//点击获取验证码
$("#btn").click(function(){
    var r_val_eml = $("#r_rlm").val()
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
			var r_rlm=$("#r_rlm").val()
			var r_el_upd = $("#r_el_upd").val()
		if(r_rlm == "" || r_el_upd == ""){
					alert("输入不能为空")
		}else{
            // 邮箱
            if (!patrn2.exec(r_rlm)) {
                console.log("邮箱错误")
                return false;
            }
			$.ajax({
				url:  'contact/checkBindEmail',
				type: "GET",
				data: "&email=" + r_rlm +
				"&securityCode=" + r_el_upd ,
				success: function (res) {
					console.log(JSON.stringify(res));
					if (res.code == "200") {
						console.log("///////////////////////")
						$(".r_lay").css("display","none")
					} else {
						console.log(res.msg);
					}
				}
			})
		}
	})
}
//进入页面获取数据
$.ajax({
    url:  'sys/user/baseInfo',
    type: "POST",
    success: function (res) {
        console.log("修改账户信息")
        console.log(JSON.stringify(res));
        if (res.code == "200") {
        	console.log(res)
            // var r_header_img = res.user.headUrl;
            // 图片
            // $("#r_header_img").attr("src", "user.headUrl");
            var r_ide = res.user.userId;
            $("#r_ide").val(r_ide)
            var select1 = res.user.roleId;
			$("#acc_select_b option:selected").text(select1);
            var r_num = res.user.username;
            $("#r_num").val(r_num)
            var r_name_l = res.user.realName;
            $("#r_name_l").val(r_name_l)
            var r_rlm = res.user.email;
            $("#r_rlm").val(r_rlm)
            var r_rlp = res.user.mobile;
            $("#r_rlp").val(r_rlp)
            $(".r_lay").css("display","none")
        } else {
            console.log(res.msg);
        }}
})

//点击获取手机验证码
$("#btne").click(function(){
    var r_el_upde = $("#r_rlp").val()
    $.ajax({
        url:  'contact/sendBindMobile',
        type: "GET",
        data: "&mobile=" + r_el_upde ,
        success: function (res) {
            console.log(JSON.stringify(res));
            if (res.code == "200") {
                console.log("///////////////////////")
                ryph()
            } else {
                console.log(res.msg);
            }
        }
    })
})

//提交  验证码 手机号
function ryph(){
    $("#r_btee").click(function(){
        var r_rlmn=$("#r_rlp").val()
        var r_el_updn = $("#r_el_upde").val()
		console.log(r_rlmn)
        console.log(r_el_updn)
        if(r_rlmn == "" || r_el_updn == ""){
            alert("输入不能为空")
        }else{
            //手机号错误
            if (!patrn1.exec(r_rlmn)){
                console.log("手机号错误")
                return false;
            }
            $.ajax({
                url:  'contact/checkBindMobile',
                type: "GET",
                data: "&email=" + r_rlmn +
                "&securityCode=" + r_el_updn ,
                success: function (res) {
                    console.log(JSON.stringify(res));
                    if (res.code == "200") {
                        console.log("///////////////////////")
                        $(".r_lay_a").css("display","none")
                    } else {
                        console.log(res.msg);
                    }
                }
            })
        }
    })
}




$("#r_tijiao").click(function(){
    var r_rlma=$("#r_num").val()
    var r_rlmb = $("#r_name_l").val()
    var r_rlmc=$("#r_rlm").val()
    var r_rlmd = $("#r_rlp").val()
	if(r_rlma == "" || r_rlmb == ""|| r_rlmc == "" || r_rlmd == ""){
alert("输入不能为空")
	}else{
        //    账号
        if (!patrn3.exec(r_rlma)) {
            console.log("账号错误")
            return false;
        }
        //    用户名
        if (!patrn4.exec(r_rlmb)) {
            console.log("用户名错误")
            return false;
        }

        // 邮箱
        if (!patrn2.exec(r_rlmc)) {
            console.log("邮箱错误")
            return false;
        }
        //手机号错误
        if (!patrn1.exec(r_rlmd)){
            console.log("手机号错误")
            return false;

        }
        $.ajax({
            url:  'sys/user/updateBaseInfo',
            type: "POST",
            data:
            "&username=" + r_rlma +
            "&realName=" + r_rlmb +
            "&email=" + r_rlmc +
            "&mobile=" + r_rlmd  ,
            success: function (res) {
                console.log(JSON.stringify(res));
                if (res.code == "200") {
                    console.log(res)
                    $(".r_lay").css("display","none")
                } else {
                    console.log(res.msg);
                }}
        })
	}
})

$("#r_val_pho").click(function(){
    $(".r_lay_a").css("display","block")
    $(".r_beise").css("display","block")
})
//倒计时60秒
var countdown=60;
function sendemail(){
    console.log("22222")
    var obje = $("#btn");
    countdown=60
    settime(obje);
}

function sendemail_a() {
	console.log("111111111")
    var obj = $("#btne");
    countdown=60
    settime(obj);
}
function settime(obja) { //发送验证码倒计时
    if (countdown == 0) {
        obja.attr('disabled',false);
        //obj.removeattr("disabled");
        obja.val("获取验证码");
        countdown = 60;
        return;
    } else {
        obja.attr('disabled',true);
        obja.val("重新发送(" + countdown + ")");
        countdown--;
    }
    setTimeout(function() {
            settime(obja) }
        ,1000)
}






