$(function () {
    var Id_a
    var ids;
    //渲染表格
    $.ajax({
        url: baseURL + '/sys/user/userList',
        contentType: "application/json;charset=UTF-8",
        type: "get",
        data: {},
        success: function (res) {
            console.log(JSON.stringify(res))
            var html
            for (var i = 0; i < res.data.length; i++) {
                html +=" <tr>\n" +
                    " <td>"+res.list[i].userId+"</td>\n" +
                    "<td>"+res.list[i].username+"</td>\n" +
                    " <td>"+res.list[i].realName+"</td>\n" +
                    "<td>"+res.list[i].projectCount+"</td>\n" +
                    "<td>"+res.list[i].deviceCount+"</td>\n" +
                    "<td>"+res.list[i].createTime+"</td>\n" +
                    "<td>"+res.list[i].termOfValidity+"年</td>\n" +
                    "<td></td>\n" +
                    "<td>" +
                    "<div class=\"switch\"> \n" +
                    "<div class=\"btn_fath clearfix on toogle\"  > \n" +
                    "<div class=\"move\" data-state="+res.data[i].status+"></div> \n" +
                    "<div class=\"btnSwitch btn1\">ON</div> \n" +
                    "<div class=\"btnSwitch btn2 \">OFF</div> \n" +
                    "</div> " +
                    "</td>\n" +
                    "<td>"+res.list[i].mobile+"</td>\n" +
                    "<td>"+res.list[i].email+"</td>\n" +
                    "<td id="+res.list[i].userId+">\n" +
                    " <a href=\"#\" class='compile'><span class=\"glyphicon glyphicon-pencil \" ></span></a>\n" +
                    " <a href=\"#\" class='Delete'><span class=\"glyphicon glyphicon-trash \"></span></a>\n" +
                    "</td>\n" +
                    "</tr>"
            }
            $("#div").append(html)
        //滑動按鈕
            $(".toogle").click(function () {
                var ele = $(this).children(".move");
                if(ele.attr("data-state") == "1"){
                    ele.animate({left: "0"}, 300, function(){
                        ele.attr("data-state", "0");
                        alert("关！");
                    });
                    $(this).removeClass("on").addClass("off");
                }else if(ele.attr("data-state") == "0"){
                    ele.animate({left: '50%'}, 300, function(){
                        $(this).attr("data-state", "1");
                        alert("开！");
                    });
                    $(this).removeClass("off").addClass("on");
                }
            })

        //編輯
            $(".compile").click(function(){
                $(".shade,.shade_b").css("display","block")
                Id_a=$(this).parent().attr('id');
            })

        //刪除
            $(".Delete").click(function(){
                $(".shade_delete,.shade_b_delete").css("display","block");
                Id_a=$(this).parent().attr('id');
            })
        }
    })
    //    分頁///////////////////////////////////
    $("#pagination3").pagination({
        currentPage: 1,
        totalPage: 10,
        isShow: true,
        count: 7,
        homePageText: "首页",
        endPageText: "尾页",
        prevPageText: "上一页",
        nextPageText: "下一页",
        callback: function(current) {
            console.log("12121")
            console.log(current)
            $("#current3").text(current)
        }
    });

    // $("#getPage").on("click", function() {
    //     var info = $("#pagination3").pagination("getPage");
    //     alert("当前页数：" + info.current + ",总页数：" + info.total);
    // });
    //
    // $("#setPage").on("click", function() {
    //     $("#pagination3").pagination("setPage", 1, 10);
    // });
    //编辑弹窗刪除////////////////(编辑传参少密码，头像，報錯500)
    $("#shade_a,.sha_cancel,.guan_shc").click(function () {
        $(".shade,.shade_b").css("display","none")
    })
    $("#sha_que_can").click(function () {
        var acc_number=$("#acc_number_b").val()
        var acc_user=$("#acc_user_b").val()
        var acc_password=$("#acc_password_b").val()

        var acc_select=$("#acc_select_b option:selected").text()
        var acc_mailbox=$("#acc_mailbox_b").val()
        var acc_call=$("#acc_call_b").val()
        alert(acc_number+acc_user+acc_password+acc_select+acc_mailbox+acc_call)

        $.ajax({
            url: baseURL + 'sys/user/update',
           /* contentType: "application/json;charset=UTF-8",*/
            type: "POST",
            data:JSON.stringify({
                "userId":Id_a,
                "username":acc_number,
                "realName":acc_user,

                "termOfValidity":acc_select,

                "email":acc_mailbox,
                "mobile":acc_call,
            }),
            success: function (res) {
                console.log(res)
                // window.location.reload()
            }
        })
    })

    //删除彈窗/////////////////////
    $(".shade_a_delete,.sha_cancel_delete,.guan_sha").click(function () {
        $(".shade_delete,.shade_b_delete").css("display","none")
    })
    //確定刪除（删除报错500）
    $(".sha_que_delete").click(function(){
/*
        $.ajax({
            url: baseURL + 'sys/user/delete',
            contentType: "application/json;charset=UTF-8",
            type: "POST",
            data:JSON.stringify({
                "userIds":Id_a,
            }),
            success: function (res) {
                console.log(res)
                window.location.reload()
            }
        })

*/
        $.ajax({
            type: "POST",
            url: baseURL + "sys/user/delete",
            //contentType: "application/json;charset=UTF-8",
            dataType: "json",
            data: "ids="+Id_a,
            success: function(r){
                console.log(JSON.stringify(r));
                if(r.code == 200){
                    alert('操作成功', function(){
                        window.location.reload()
                    });
                }else{
                    alert(r.msg);
                }
            }
        });

    })


    //新建//////////////////////////（新建报错500 传参少头像）
    $(".btn_m").click(function(){
        $(".shade_new,.shade_b_new").css("display","block")
    })
    $(".shade_a_new,.sha_cancel_new,.guan_shb").click(function(){
        $(".shade_new,.shade_b_new").css("display","none")
    })
    $("#confirm_Z").click(function(){
      var acc_number=$("#acc_number").val()
      var acc_user=$("#acc_user").val()
      var acc_password=$("#acc_password").val()

      var acc_select=$("#acc_select option:selected").text()
      var acc_mailbox=$("#acc_mailbox").val()
      var acc_call=$("#acc_call").val();
      var roleId = $("#role").val();

        if(acc_number == "" || acc_user=="" || acc_password=="" || acc_mailbox=="" || acc_call==""){
           alert("輸入不能為空")
        }else{
            $.ajax({
                url: baseURL + 'sys/user/save',
                //contentType: "application/json;charset=UTF-8",
                type: "POST",
                data:
                    "username="+acc_number+
                    "&realName="+acc_user+
                    "&password="+acc_password+
                    "&termOfValidity="+acc_select+
                    "&email="+acc_mailbox+
                    "&mobile="+acc_call+
                    "&roleId="+roleId,
                success: function (res) {
                    console.log(JSON.stringify(res));
                    if(res.code == 200){
                        alert('保存成功', function(){
                            window.location.reload()
                        });
                    }else{
                        alert(res.msg);
                    }
                }
            })
        }
    })
});




