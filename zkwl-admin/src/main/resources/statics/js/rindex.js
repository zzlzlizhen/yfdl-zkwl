$(function(){
    $("#set_p").click(function(){
        $(".JI_b,.nei_r").show()
    })
    $(".shade,.wrong_a").click(function(){
        $(".JI_b,.nei_r").hide()
    })
    $("#feedback").click(function(){
        $(".JI_b_a,.nei_r_a").show()
    })
    $(".shade_a,.wrong").click(function(){
        $(".JI_b_a,.nei_r_a").hide()
    })


//我要反馈

    $("#r-btn-las").click(function(){
        //标题
        var rTitle = $("#rTitle").val()
        //邮箱
        var r_eml_fan = $("#r_eml_fan").val()
        //手机号
        var r_ph_fan = $("#r_ph_fan").val()
        //内容
        var r_wen_fan = $("#r_wen_fan").val()
        // console.log(r_wen_fan)
        regular(r_ph_fan,r_eml_fan);
        if(rTitle ==""||r_eml_fan ==""||r_ph_fan==""||r_wen_fan==""){
            alert("不能为空")
        }else{
            $.ajax({
                url:'sys/feedback/save',
                type: "POST",
                data:
                "&backContent=" + r_wen_fan +
                "&email=" + r_eml_fan +
                "&mobile=" + r_ph_fan +
                "&title=" + rTitle,
                success: function (res) {
                    console.log(JSON.stringify(res));
                    if (res.code == "200") {
                        alert('提交成功')
                        $("#rTitle").val("")
                        $("#r_eml_fan").val("")
                        $("#r_ph_fan").val("")
                        $("#r_wen_fan").val("")
                    } else {
                        alert(res.msg);
                    }
                }
            })
        }


    })

})