$(function(){
    //基本信息
    $("#set_p").unbind('click');
    $("#set_p").click(function(){
        $(".JI_b,.nei_r").show();
        $("#select1").html("");
        //进入页面获取数据
        $.ajax({
            url:  'sys/user/baseInfo',
            type: "POST",
            success: function (res) {
                if (res.code == "200") {
                    // var r_header_img = res.user.headUrl;
                    // 图片
                    // $("#r_header_img").attr("src", "user.headUrl");
                    var r_ide = res.user.userId;
                    $("#r_ide").val(r_ide);
                    var select1 = res.user.roleId;
                    var  n_ame
                    if(res.user.roleId == 1){
                        n_ame="管理者"
                    }else if(res.user.roleId == 2){
                        n_ame="使用者"
                    }

                    var html="";
                    html+="<option id="+res.user.roleId+">"+n_ame+"</option>"
                    $("#select1").append(html);

                    var r_num = res.user.username;
                    $("#r_num").val(r_num);
                    var r_name_l = res.user.realName;
                    $("#r_name_l").val(r_name_l)
                    var r_rlm = res.user.email;
                    $("#r_rlm").val(r_rlm);
                    var r_rlp = res.user.mobile;
                    $("#r_rlp").val(r_rlp);
                    //    头像
                    var url = res.user.headUrl;
                    if(url !=""){
                        $("#pic").attr("src","https://guangxun-wulian.com"+url);
                    }else{
                        $("#pic").attr("src", "/statics/image/touXa.svg");
                    }

                } else {

                }
            }
        })
    })
    $(".shade,.wrong_a").click(function(){
        $(".JI_b,.nei_r").hide();
    })
    //反馈
    $("#feedback").click(function(){
        $(".JI_b_a,.nei_r_a,.shade_a").show();
    })
    $(".shade_a,.wrong,.sha_cancel").click(function(){
        $(".JI_b_a,.nei_r_a,.shade_a").hide();
        $(".mistake").hide();
    })
    //验证码
    $(".r_be,.r_be_a").click(function(){
        $(".r_lay_a,.r_beise_a,.r_lay,.r_beise,.r_be,.r_be_a").hide();
        // clearTimeout(timer);
            countdown=0
    })

//我要反馈

    $("#r-btn-las").click(function(){
        //标题
        var rTitle = $("#rTitle").val();
        //邮箱
        var r_eml_fan = $("#r_eml_fan").val();
        //手机号
        var r_ph_fan = $("#r_ph_fan").val();
        //内容
        var r_wen_fan = $("#r_wen_fan").val();
        if(rTitle ==""||r_eml_fan ==""||r_ph_fan==""||r_wen_fan==""){
            $(".rrbol").html("输入不能為空");
            lay()
        } else {
            // 邮箱
            if (!patrn2.exec(r_eml_fan)) {
                $(".rrbol").html("邮箱错误");
                $(".mistake").css("display","block");
                return false;
            }
            //手机号错误
            if (!patrn1.exec(r_ph_fan)){
                $(".rrbol").html("手机号错误");
                $(".rrbol").html("手机号错误");
                $(".mistake").css("display","block");
                return false;
            }

            $.ajax({
                url:'sys/feedback/save',
                type: "POST",
                data:
                "&backContent=" + r_wen_fan +
                "&email=" + r_eml_fan +
                "&mobile=" + r_ph_fan +
                "&title=" + rTitle,
                success: function (res) {
                    if (res.code == "200") {
                        $(".mistake").hide();
                        layer.open({
                            title: '信息',
                            content: '提交成功',
                            skin: 'demo-class'
                        });
                        $("#rTitle").val("");
                        $("#r_eml_fan").val("");
                        $("#r_ph_fan").val("");
                        $("#r_wen_fan").val("");
                        $(".JI_b_a,.nei_r_a,.shade_a").hide();
                    } else {
                        layer.open({
                            title: '信息',
                            content: 'res.msg',
                            skin: 'demo-class'
                        });
                    }
                }
            })
        }
    })
})