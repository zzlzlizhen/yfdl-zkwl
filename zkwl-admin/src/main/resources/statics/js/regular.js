$(function(){
    // 邮箱
    // var rem = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/g;
    // if(rem!=r_emal_mod){
    //     console.log('邮箱不正确');
    //     console.log(r_emal_mod)
    // }

})
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
        $(".shade_b").css("display","block");
    }
}
function regular(r_typ_mod,r_emal_mod,na_ma_mod,r_user_mod,r_pw_mod){
    // 手机号
    var patrn1 = /^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/;
    if (!patrn1.exec(r_typ_mod)){
        $(".rrbol").html("手机号错误");
        console.log("手机号错误")
        $(".shade_b").css("display","block");
    }else{
        $(".rrbol").html("");
    }
    //校验EMail
    var patrn2 = /^([0-9A-Za-z\-_\.]+)@([0-9A-Za-z]+\.[A-Za-z]{2,3}(\.[A-Za-z]{2})?)$/g;
    if (!patrn2.exec(r_emal_mod)) {
        $(".rrbol").html("邮箱错误");
        console.log("邮箱错误")
        $(".shade_b").css("display","block");
    }else{
        $(".rrbol").html("");
    }
    //账号验证    验证规则：字母、数字、下划线组成，字母开头，4-16位。
    var patrn3 = /^[a-zA-z]\w{3,15}$/;
    if (!patrn3.exec(na_ma_mod)) {
        $(".rrbol").html("账号错误");
        console.log("账号错误")
        $(".shade_b").css("display","block");
    }else{

    }
    //用户名
    var patrn4= /^[a-zA-Z0-9_-]{4,16}$/;
    if (!patrn4.exec(r_user_mod)) {
        $(".rrbol").html("用户名错误");
        console.log("用户名错误")
        $(".shade_b").css("display","block");
    }else{
        $(".rrbol").html("");
    }
    //密码
    var patrn5= /^[a-zA-Z0-9]{4,10}$/;
    if (!patrn5.exec(r_pw_mod)) {
        $(".rrbol").html("密码错误");
        console.log("密码错误")
        $(".shade_b").css("display","block");
    }else{
        $(".rrbol").html("");
    }

}