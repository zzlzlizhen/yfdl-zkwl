
var lay
function lay(){
    var rdiplay = $(".mistake").css("display","block");
    var rrbol = $(".rrbol").html();
    if(rrbol!="" ){
        $(".mistake").css("display","block");
        return
    }else{
        $(".mistake").css("display","none");
    }
    if(rdiplay == "block"){
        $(".mistake").css("display","block");
    }
}

    // 手机号
    var patrn1 = /^1[3456789]\d{9}$/;
    // if (!patrn1.exec(r_typ_mod)){
    //     $(".rrbol").html("手机号错误");
    //     console.log("手机号错误")
    //     $(".rrbol").html("手机号错误");
    //     $(".mistake").css("display","block");
    // }
    //校验EMail
    var patrn2 =/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    // if (!patrn2.exec(r_emal_mod)) {
    //     $(".rrbol").html("邮箱错误");
    //     console.log("邮箱错误")
    //     $(".mistake").css("display","block");
    //
    // }
    //账号验证    验证规则：字母、数字、下划线组成，字母开头，4-16位。
    var patrn3 = /^[a-zA-Z0-9]\w{3,15}$/;
    // if (!patrn3.exec(na_ma_mod)) {
    //     $(".rrbol").html("账号错误");
    //     console.log("账号错误")
    //     $(".mistake").css("display","block");
    //
    // }
    //用户名
   // var patrn4= /^[a-zA-Z0-9_-]{4,16}$/;
var patrn4= /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
    // if (!patrn4.exec(r_user_mod)) {
    //     $(".rrbol").html("用户名错误");
    //     console.log("用户名错误")
    //     $(".mistake").css("display","block");
    //
    // }
    //密码
    var patrn5= /^[a-zA-Z0-9]{4,10}$/;
    // if (!patrn5.exec(r_pw_mod)) {
    //     $(".rrbol").html("密码错误");
    //     console.log("密码错误")
    //     $(".shade_b").css("display","block");
    // }

