$(function () {
    var pages
    var pageSize
    var pageNum
    var Id_a
    var ids
    var $color = localStorage.getItem("mycolor");
    //搜索
    $("#proje_search").unbind('click');
    $("#proje_search").click(function () {
        var acc_id = $("#acc_id").val();
        var acc_hao = $("#acc_hao").val();
        var acc_name = $("#acc_name").val();
        var select = $("#sele_ht option:selected").attr("class");
        $("#div").html("");
        pageNum = 1;
        form(pageSize, pageNum, acc_id, acc_hao, acc_name, select)
    })
    //渲染表格
    form(10, 1, "", "", "", "")

    function form(pageSizea, pagesa, userId, username, realName, status) {
        $.ajax({
            url: baseURL + 'sys/user/userList',
            type: "get",
            data: {
                "userId": userId,  //账号id
                "username": username, //账号
                "realName": realName,  //用户名
                "status": status,
                "limit": pageSizea,
                "page": pagesa
            },
            success: function (res) {
                pages = res.page.currPage;  //第几页
                pageSize = res.page.pageSize;//每页条数
                pageNum = res.page.totalPage;  //总页数
                var html=""
                var offClass = "";
                //当前时间
                var date = new Date();
                var Tima = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " +date.getHours()+ ":" + date.getMinutes()+":"+ date.getSeconds();
                for (var i = 0; i < res.page.list.length; i++) {
                    var state
                    var dead=res.page.list[i].deadline
                    if (res.page.list[i].status == 0) {
                        offClass = "btn_fath clearfix  toogle off";
                    } else {
                        offClass = "btn_fath clearfix  toogle on";
                    }
                    if(res.page.list[i].status == 1 &&  dead > Tima ){
                        state="<img src='/statics/image/zhengchang.png' alt=''>"
                    }else{
                        state="<img src='/statics/image/yichang.png' alt=''>"
                    }
                    //有效期
                    var termOfValidity=res.page.list[i].termOfValidity
                    if(termOfValidity == "0"){
                        termOfValidity="半年"
                    }else if(termOfValidity == "6"){
                        termOfValidity="永久"
                    }else{
                        termOfValidity=res.page.list[i].termOfValidity+"年"
                    }

                    html += " <tr class='r_pw'>\n" +
                        " <td>" + res.page.list[i].userId + "</td>\n" +
                        "<td class='username' title=" + res.page.list[i].username + ">" + res.page.list[i].username + "</td>\n" +
                        " <td class='r_user' title=" + res.page.list[i].realName + ">" + res.page.list[i].realName + "</td>\n" +
                        "<td>" + res.page.list[i].projectCount + "</td>\n" +
                        "<td class='roleId' id=" + res.page.list[i].roleId + ">" + res.page.list[i].deviceCount + "</td>\n" +
                        "<td title=" + res.page.list[i].createTime + ">" + res.page.list[i].createTime + "</td>\n" +
                        "<td class='r_termof'>" + termOfValidity + "</td>\n" +
                        "<td>"+state+"</td>\n" +
                        "<td>" +
                        "<div class=\"switch\" > \n" +
                        "<div class='" + offClass + "'  id=" + res.page.list[i].userId + "> \n" +
                        "<div class=\"move\"  data-state=" + res.page.list[i].status + "></div> \n" +
                        "<div class=\"btnSwitch btn1\">关</div> \n" +
                        "<div class=\"btnSwitch btn2 \">开</div> \n" +
                        "</div> " +
                        "</td>\n" +
                        "<td class='r_typ' title=" + res.page.list[i].mobile + ">" + res.page.list[i].mobile + "</td>\n" +
                        "<td class='r_emal' title=" + res.page.list[i].email + ">" + res.page.list[i].email + "</td>\n" +
                        "<td id=" + res.page.list[i].userId + ">\n" +
                        " <a class='compile r_bu_sou1' id=" + res.page.list[i].headUrl + "><img src='/statics/image/bianji.png' alt=''></a>\n" +
                        " <a href='javascript:void(0)'  class='Delete r_bu_sou2'><img src='/statics/image/shanchu.png' alt=''></a>\n" +
                        "</td>\n" +
                        "</tr>"
                }

                $("#div").empty().append(html);
                if($color == 2){
                    $(".btn_m_new,.r_bu_sou1,.r_bu_sou2").hide();
                }
                //滑動按鈕
                var Id_a
                $(".toogle").unbind('click');
                $(".toogle").click(function () {
                    var username = $(this).parent().parent().siblings(".username").html();
                    var ele = $(this).children(".move");
                    Id_a = $(this).attr("id");
                    if (ele.attr("data-state") == "1") {
                        ele.animate({left: "10%"}, 300, function () {
                            ele.attr("data-state", "0");
                            var state = 0
                            aaa(state, username, Id_a)
                        });
                        $(this).removeClass("on").addClass("off");
                        setTimeout(function () {
                            window.location.reload()
                        }, 2000);
                        return
                    } else if (ele.attr("data-state") == "0") {
                        ele.animate({left: '50%'}, 300, function () {
                            ele.attr("data-state", "1");
                            var state = 1
                            aaa(state, username, Id_a)
                        });
                        $(this).removeClass("off").addClass("on");
                        setTimeout(function () {
                            window.location.reload()
                        }, 2000);
                    }
                })

                function aaa(state, username, Id_a) {
                    $.ajax({
                        url: baseURL + '/sys/user/status',
                        type: "POST",
                        data:
                        "&userId=" + Id_a +
                        "&status=" + state,
                        success: function (res) {

                        }
                    })
                }
                //编辑
                var Id_aa
                $(".compile").unbind('click');
                $(".compile").click(function () {
                    $(".shade,.shade_b").css("display", "block");
                    //id
                    Id_aa = $(this).parent().attr('id');
                    var na_ma = $(this).parent().siblings(".username").html();
                    $("#acc_number_b").val(na_ma);
                    var r_user = $(this).parent().siblings(".r_user").html();
                    $("#acc_user_b").val(r_user);
                    // 密码
                    var r_pw = $(this).parent().siblings(".r_pw").html();
                    $("#acc_password_b").val(r_pw);
                    var r_termof = $(this).parent().siblings(".r_termof").html();
                    $("#acc_select_b ").val(r_termof);
                    var r_typ = $(this).parent().siblings(".r_typ").html();
                    $("#acc_call_b").val(r_typ);
                    var r_emal = $(this).parent().siblings(".r_emal").html();
                    $("#acc_mailbox_b").val(r_emal);
                    var roleId = $(this).parent().siblings(".roleId").attr("id");
                    if(roleId == 1){
                        roleId="管理者"
                    }else if(roleId == 2){
                        roleId="使用者"
                    }
                    $("#r_Ad_mod").val(roleId);
                    //图片
                    var url=$(this).attr("id");
                    if(url !=""){
                        $("#pice").attr("src","https://guangxun-wulian.com"+url);
                    }else{
                        $("#pice").attr("src", "/statics/image/ttxa.svg");
                    }

                })
                //修改管理员
                $("#sha_que_can").unbind('click');
                $("#sha_que_can").click(function () {
                    //账号
                    var na_ma_mod = $("#acc_number_b").val();
                    //用户
                    var r_user_mod = $("#acc_user_b").val();
                    // 密码
                    var r_pw_mod = $("#acc_password_b").val();
                    // 年限
                    var r_termof_mod = $("#acc_select_b option:selected").attr("class");
                    //图片
                    var r_src_mod = $('#previewr').css('backgroundImage');
                    var headUrle=$("#headUrle").val()
                    //邮箱
                    var r_emal_mod = $("#acc_mailbox_b").val();
                    //手机
                    var r_typ_mod = $("#acc_call_b").val();
                    //管理者
                    var r_Ad_mod = $("#r_Ad_mod option:selected").attr("class");

                    if (na_ma_mod == "" || r_user_mod == "" || r_termof_mod == "" || r_emal_mod == "" || r_typ_mod == "" || r_Ad_mod == "") {
                       $(".rrbol").html("输入不能为空");
                         lay()
                    } else {
                        //    账号
                        if (!patrn3.exec(na_ma_mod)) {
                            $(".rrbol").html("账号错误");
                            $(".mistake").css("display","block");
                            return false;
                        }
                        //    用户名
                        if (!patrn4.exec(r_user_mod)) {
                            $(".rrbol").html("用户名错误");
                            $(".mistake").css("display","block");
                            return false;
                        }
                        // 邮箱
                        if (!patrn2.exec(r_emal_mod)) {
                            $(".rrbol").html("邮箱错误");
                            $(".mistake").css("display","block");
                            return false;
                        }
                        //手机号错误
                        if (!patrn1.exec(r_typ_mod)){
                            $(".rrbol").html("手机号错误");
                            $(".rrbol").html("手机号错误");
                            $(".mistake").css("display","block");
                            return false;
                        }
                        $.ajax({
                            url: baseURL + 'sys/user/update',
                            type: "POST",
                            data:
                            "&userId=" + Id_aa +
                            "&username=" + na_ma_mod +
                            "&email=" + r_emal_mod +
                            "&mobile=" + r_typ_mod +
                            "&headUrl=" + headUrle +
                            "&realName=" + r_user_mod +
                            "&termOfValidity=" + r_termof_mod +
                            "&roleId=" + r_Ad_mod+
                            "&password=" + r_pw_mod,
                            success: function (res) {
                                if (res.code == "200") {
                                    $(".shade").hide()
                                    $(".mistake").hide()
                                    layer.open({
                                        title: '信息',
                                        content: '修改成功',
                                        skin: 'demo-class'
                                    });
                                    form(10, 1, "", "", "", "")
                                } else {
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

                //刪除
                $(".Delete").click(function () {
                    $(".shade_delete,.shade_b_delete").css("display", "block");
                    Id_a = $(this).parent().attr('id');
                })
                //確定刪除
                $(".sha_que_delete").unbind('click');
                $(".sha_que_delete").click(function () {
                    $.ajax({
                        url: baseURL + "sys/user/delete",
                        type: "POST",
                        dataType: "json",
                        data: "ids=" + Id_a,
                        success: function (r) {
                            if (r.code == 200) {
                                window.location.reload()
                            } else {
                                layer.open({
                                    title: '信息',
                                    content: ''+r.msg,
                                    skin: 'demo-class'
                                });
                            }
                        }
                    });

                })

                //  分頁
                $("#pagination3").pagination({
                    currentPage: pages,
                    totalPage: pageNum,
                    isShow: true,
                    count: 7,
                    homePageText: "首页",
                    endPageText: "尾页",
                    prevPageText: "上一页",
                    nextPageText: "下一页",
                    callback: function (current) {
                        //当前页数current
                        var pagesb = current
                        var acc_id = $("#acc_id").val();
                        var acc_hao = $("#acc_hao").val();
                        var acc_name = $("#acc_name").val();
                        var select = $("#sele_ht option:selected").attr("class");
                        $("#div").html("");
                        form(pageSize, pagesb, acc_id, acc_hao, acc_name, select);
                    }
                });
            }
        })
    }

    // $("#getPage").on("click", function() {
    //     var info = $("#pagination3").pagination("getPage");
    //     alert("当前页数：" + info.current + ",总页数：" + info.total);
    // });
    //
    // $("#setPage").on("click", function() {
    //     $("#pagination3").pagination("setPage", 1, 10);
    // });
    //编辑弹窗刪除
    $("#shade_a,.sha_cancel,.guan_shc").click(function () {
        $(".shade,.shade_b").css("display", "none")
    })
    //删除弹窗
    $(".shade_a_delete,.sha_cancel_delete,.guan_sha,.sha_cancel_newa").unbind('click');
    $(".shade_a_delete,.sha_cancel_delete,.guan_sha,.sha_cancel_newa,.layui-layer-btn0,.layui-layer-setwin").click(function () {
        $(".shade_delete,.shade_b_delete").css("display", "none")
        $(".mistake").css("display", "none")
    })

    //新建
    $(".btn_m_new").unbind('click');
    $(".btn_m_new").click(function () {
        $(".shade_new,.shade_b_new").css("display", "block");
        $("#acc_number").val("")
        $("#acc_user").val("")
        $("#acc_password").val("")
        $("#acc_select").val("")
        $("#acc_mailbox").val("")
        $("#acc_call").val("");
        $("#role").val("");
    })
    $(".shade_a_new,.sha_cancel_new,.guan_shb").click(function () {
        $(".shade_new,.shade_b_new").css("display", "none")
        $(".mistake").css("display","none")
    })

    $("#confirm_Z").unbind('click');
    $("#confirm_Z").click(function () {
        var acc_number = $("#acc_number").val()
        var acc_user = $("#acc_user").val()
        var acc_password = $("#acc_password").val()
        //图片
        var r_src_mod = $('#preview').css('backgroundImage');
        var acc_select = $("#acc_select option:selected").attr("class");
        var acc_mailbox = $("#acc_mailbox").val()
        var acc_call = $("#acc_call").val();
        var roleId = $("#role").val();
        var headUrl = $("#headUrl").val();
            if(roleId==null){
                roleId=""
            }
        if (acc_number == "" || acc_user == "" || acc_password == "" || acc_mailbox == "" || acc_call == ""||roleId == "") {
          $(".rrbol").html("输入不能为空");
            lay()
        } else {
            //    账号
            if (!patrn3.exec(acc_number)) {
                $(".rrbol").html("账号错误");
                $(".mistake").css("display","block");
                return false;
            }
            //    用户名
            if (!patrn4.exec(acc_user)) {
                $(".rrbol").html("用户名错误");
                $(".mistake").css("display","block");
                return false;
            }
            //    密码
            if (!patrn5.exec(acc_password)) {
                $(".rrbol").html("密码错误");
                $(".shade_b").css("display","block");
                return false;
            }
            // 邮箱
            if (!patrn2.exec(acc_mailbox)) {
                $(".rrbol").html("邮箱错误");
                $(".mistake").css("display","block");
                return false;
            }
            //手机号错误
            if (!patrn1.exec(acc_call)){
                $(".rrbol").html("手机号错误");
                $(".rrbol").html("手机号错误");
                $(".mistake").css("display","block");
                return false;
            }


            $.ajax({
                url: baseURL + 'sys/user/save',
                type: "POST",
                data:
                "&headUrl=" + headUrl +
                "&username=" + acc_number +
                "&realName=" + acc_user +
                "&password=" + acc_password +
                "&termOfValidity=" + acc_select +
                "&email=" + acc_mailbox +
                "&mobile=" + acc_call +
                "&roleId=" + roleId ,
                success: function (res) {
                    if (res.code == 200) {
                        $("#acc_number").val("")
                        $("#acc_user").val("")
                        $("#acc_password").val("")
                        $("#acc_select").val("")
                        $("#acc_mailbox").val("")
                        $("#acc_call").val("");
                        $("#role").val("");
                        $(".shade_new").hide()
                        layer.open({
                            title: '信息',
                            content: '保存成功',
                            skin: 'demo-class'
                        });
                        form(10, 1, "", "", "", "")
                    } else {
                        layer.open({
                            title: '信息',
                            content: ''+res.msg,
                            skin: 'demo-class'
                        });
                    }
                }
            })
        }
    });
$(".wrong").click(function(){
    $(".mistake").css("display","none")
})
//上传头像   添加
    new AjaxUpload('#upload', {
        action: baseURL + "sys/upload",
        name: 'file',
        autoSubmit:true,
        responseType:"json",
        onSubmit:function(file, extension){
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))){
                layer.open({
                    title: '信息',
                    content:'只支持jpg、png、gif格式的图片！',
                    skin: 'demo-class'
                });
                return false;
            }
        },
        onComplete : function(file, r){
            if(r.code == 200){
                $("#headUrl").val(r.url);
                $("#pic").attr("src","https://guangxun-wulian.com"+r.url);
            }else{
                layer.open({
                    title: '信息',
                    content: ''+r.msg,
                    skin: 'demo-class'
                });
            }
        }
    });
});


//修改 上传图片
new AjaxUpload('#uploade', {
    action: baseURL + "sys/upload",
    name: 'file',
    autoSubmit:true,
    responseType:"json",
    onSubmit:function(file, extension){
        if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))){
            layer.open({
                title: '信息',
                content:'只支持jpg、png、gif格式的图片！',
                skin: 'demo-class'
            });
            return false;
        }
    },
    onComplete : function(file, r){
        if(r.code == 200){
            $("#headUrle").val(r.url);
            $("#pice").attr("src","https://guangxun-wulian.com"+r.url);
        }else{
            layer.open({
                title: '信息',
                content: ''+r.msg,
                skin: 'demo-class'
            });
        }
    }
});


