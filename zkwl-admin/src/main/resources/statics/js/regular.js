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
    //校验EMail
    var patrn2 =/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    //账号验证    验证规则：字母、数字、下划线组成，字母开头，4-16位。
    var patrn3 = /^[a-zA-Z0-9]\w{3,15}$/;
    //用户名
    var patrn4= /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
    //密码
    var patrn5= /^[a-zA-Z0-9]{4,10}$/;


