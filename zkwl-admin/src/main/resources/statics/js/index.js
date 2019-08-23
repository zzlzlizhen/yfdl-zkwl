//生成菜单跳转页面
var url
var menuItem = Vue.extend({
    name: 'menu-item',
    props:{item:{}},
    template:[
        '<li >',
        '	<a v-if="item.type === 0" href="javascript:;">',
        '		<i v-if="item.icon != null" :class="item.icon"></i>',
        '		<span>{{item.name}}</span>',
        '		<i class="fa fa-angle-left pull-right"></i>',
        '	</a>',
        '	<ul v-if="item.type === 0" class="treeview-menu">',
        '		<menu-item :item="item" v-for="item in item.list"></menu-item>',
        '	</ul>',
        '	<a v-if="item.type === 1 && item.parentId === 0" v-on:click="clickTest($event)" :href="\'#\'+item.url" >',
        '		<i v-if="item.icon != null" :class="item.icon"></i>',
        '		<span style="margin-left:18%;font-size:16px;">{{item.name}}</span>',
        '	</a>',
        '	<a v-if="item.type === 1 && item.parentId != 0" v-on:click="clickTest($event)" :href="\'#\'+item.url"><i v-if="item.icon != null" :class="item.icon"></i><i v-else class="fa fa-circle-o"></i> {{item.name}}</a>',
        '</li>'
    ].join(''),
    methods: {
        clickTest: function (e) {
            var url = e.currentTarget.toString().split("#")[1];
            var hash = window.location.hash.split("#")[1];
            if(url.indexOf(hash)>-1){
                vm.main = url+"?t="+new Date().getTime();
                //导航菜单展开
                $(".treeview-menu li").removeClass("active");
                $("a[href='"+url+"']").parents("li").addClass("active");
                $("a[href='"+url+"']").parents().siblings("li").removeClass("active");
                vm.navTitle = $("a[href='"+url+"']").text();
            }
         }
   },

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
        url:'',
		main:"modules/project/project_index.html",
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
			    if(r.user.headUrl !="") {
                    vm.url = "https://guangxun-wulian.com" + r.user.headUrl;
                }else{
                    vm.url ="/statics/image/touXa.svg"
                }
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
                                layer.open({
                                    title: '信息',
                                    content: '修改成功',
                                    skin: 'demo-class'
                                });
							}else{
								//layer.alert(result.msg);
                                layer.open({
                                    title: '信息',
                                    content: result.msg,
                                    skin: 'demo-class'
                                });
							}
						}
					});
	            }
			});
		},
        donate: function () {

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
			    $("a[href='"+url+"']").parents().siblings("li").removeClass("active");
			    vm.navTitle = $("a[href='"+url+"']").text();
			});
		}
	}
	router.add('#modules/informa/informa.html', function() {
		var url = window.location.hash;
		vm.main = url.replace('#', '');
	});
}

$.ajax({
    url:  'sys/user/baseInfo',
    type: "POST",
    success: function (res) {
        localStorage.mycolor= res.user.roleId;
    }
})
//点击弹窗
$("#r_val_eml").click(function(){
    $(".r_lay,.r_beise,.r_be").css("display","block")
    $(".r_lay_a,.r_beise_a,.r_be_a").css("display","none")
})
//提交  验证码  邮箱
$("#r_bte").click(function(){
    var r_rlm=$("#r_rlm").val()
    var r_el_upd = $("#r_el_upd").val()
    if(r_rlm == "" || r_el_upd == ""){
       // alert("输入不能为空")
        layer.open({
            title: '信息',
            content: '输入不能为空',
            skin: 'demo-class'
        });
    }else{
        // 邮箱
        if (!patrn2.exec(r_rlm)) {
            return false;
        }
        $.ajax({
            url:  'contact/checkBindEmail',
            type: "GET",
            data: "&email=" + r_rlm +
            "&securityCode=" + r_el_upd ,
            success: function (res) {
                if (res.code == "200") {
                    $(".r_lay,.r_beise,.r_be").hide()
                    $("#r_el_upd").val();
                    countdown=60
                    return
                } else {
                    $("#r_el_upd").val();
                    countdown=60
                }
            }
        })
    }
})


//提交  验证码 手机号
$("#r_btee").click(function(){
    var r_rlmn=$("#r_rlp").val()
    var r_el_updn = $("#r_el_upde").val()
    if(r_rlmn == "" || r_el_updn == ""){
       // alert("输入不能为空")
        layer.open({
            title: '信息',
            content: '输入不能为空',
            skin: 'demo-class'
        });
    }else{
        //手机号错误
        if (!patrn1.exec(r_rlmn)){
            return false;
        }
        $.ajax({
            url:  'contact/checkBindMobile',
            type: "GET",
            data: "&mobile=" + r_rlmn +
            "&securityCode=" + r_el_updn ,
            success: function (res) {
                if (res.code == "200") {
                    $("#r_el_upde").val()
                    $(".r_lay_a,.r_beise_a,.r_be_a").hide()
                } else {
                    //alert(res.msg);
                    layer.open({
                        title: '信息',
                        content: res.msg,
                        skin: 'demo-class'
                    });
                }
            }
        })
    }
})


$("#r_tijiao").click(function(){
    var r_rlma=$("#r_num").val();
    var r_rlmb = $("#r_name_l").val();
    var r_rlmc=$("#r_rlm").val();
    var r_rlmd = $("#r_rlp").val();
    var select=$("#select1 option:selected").attr("id");
    var uRl=$("#headUrl").val()
	if(r_rlma == "" || r_rlmb == ""|| r_rlmc == "" || r_rlmd == ""){
        //alert("输入不能为空")
        layer.open({
            title: '信息',
            content: '输入不能为空',
            skin: 'demo-class'
        });
	}else{
        //    账号
        if (!patrn3.exec(r_rlma)) {
            layer.open({
                title: '信息',
                content: '账号错误',
                skin: 'demo-class'
            });
            return false;
        }
        //    用户名
        if (!patrn4.exec(r_rlmb)) {
            layer.open({
                title: '信息',
                content: '用户名错误',
                skin: 'demo-class'
            });
            return false;
        }
        // 邮箱
        if (!patrn2.exec(r_rlmc)) {
            layer.open({
                title: '信息',
                content: '邮箱错误',
                skin: 'demo-class'
            });
            return false;
        }
        //手机号错误
        if (!patrn1.exec(r_rlmd)){
            layer.open({
                title: '信息',
                content: '手机号错误',
                skin: 'demo-class'
            });
            return false;
        }

        $.ajax({
            url:  'sys/user/updateBaseInfo',
            type: "POST",
            data:
            "&username=" + r_rlma +
            "&realName=" + r_rlmb +
            "&email=" + r_rlmc +
            "&mobile=" + r_rlmd +
            "&headUrl=" + uRl +
            "&roleId=" + select ,
            success: function (res) {
                if (res.code == "200") {
                    // alert(res)
                    $(".JI_b").hide()
                    layer.open({
                        title: '信息',
                        content: "更新成功",
                        skin: 'demo-class'
                    });
                } else {
                   // alert(res.msg);
                    layer.open({
                        title: '信息',
                        content: res.msg,
                        skin: 'demo-class'
                    });
                }
            }
        })
	}
})

$("#r_val_pho").click(function(){
    $(".r_lay_a,.r_beise_a,.r_be_a").css("display","block")
    $(".r_lay,.r_beise,.r_be").css("display","none")
})
//倒计时60秒
var countdown=60;
function sendemail(){
    var obje = $("#btn");
    var r_val_eml = $("#r_rlm").val()
    countdown=60
    settime(obje);
    $.ajax({
        url:  'contact/sendBindEmail',
        type: "GET",
        data: "&email=" + r_val_eml ,
        success: function (res) {
            if (res.code == "200") {

            } else {
               // alert(res.msg);
                layer.open({
                    title: '信息',
                    content: res.msg,
                    skin: 'demo-class'
                });
            }
        }
    })
}


function sendemail_a() {
    var obj = $("#btne");
    var r_el_upde = $("#r_rlp").val()
    countdown=60
    settime(obj);
    $.ajax({
        url:  'contact/sendBindMobile',
        type: "GET",
        data: "&mobile=" + r_el_upde ,
        success: function (res) {
            if (res.code == "200") {

            } else {
                //alert(res.msg);
                layer.open({
                    title: '信息',
                    content: res.msg,
                    skin: 'demo-class'
                });
            }
        }
    })
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
            settime(obja)
     },1000)
}






