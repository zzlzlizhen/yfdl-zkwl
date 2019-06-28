$(function () {
    var pages
    var pageSize
    var pageNum
    var Id_a
    var ids


    //搜索
    $("#proje_search").click(function () {
        var acc_id = $("#acc_id").val()
        var acc_hao = $("#acc_hao").val()
        var acc_name = $("#acc_name").val()
        var select = $("#sele_ht option:selected").text()
        $("#div").html("")
        form(10, 1, acc_id, acc_hao, acc_name, select)
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
                console.log("数据")
                console.log(res)
                pages = res.page.currPage;  //第几页
                pageSize = res.page.pageSize;//每页条数
                pageNum = res.page.totalPage;  //总页数
                console.log(pages + "===" + pageSize + "===" + pageNum)
                var html=""
                var offClass = "";
                for (var i = 0; i < res.page.list.length; i++) {
                    if (res.page.list[i].status == 0) {
                        offClass = "btn_fath clearfix  toogle off";
                    } else {
                        offClass = "btn_fath clearfix  toogle on";
                    }
                    html += " <tr class='r_pw'>\n" +
                        " <td>" + res.page.list[i].userId + "</td>\n" +
                        "<td class='username'>" + res.page.list[i].username + "</td>\n" +
                        " <td class='r_user'>" + res.page.list[i].realName + "</td>\n" +
                        "<td>" + res.page.list[i].projectCount + "</td>\n" +
                        "<td>" + res.page.list[i].deviceCount + "</td>\n" +
                        "<td>" + res.page.list[i].createTime + "</td>\n" +
                        "<td class='r_termof'>" + res.page.list[i].termOfValidity + "年</td>\n" +
                        "<td></td>\n" +
                        "<td>" +
                        "<div class=\"switch\" > \n" +
                        "<div class='" + offClass + "'  id=" + res.page.list[i].userId + "> \n" +
                        "<div class=\"move\"  data-state=" + res.page.list[i].status + "></div> \n" +
                        "<div class=\"btnSwitch btn1\">ON</div> \n" +
                        "<div class=\"btnSwitch btn2 \">OFF</div> \n" +
                        "</div> " +
                        "</td>\n" +
                        "<td class='r_typ'>" + res.page.list[i].mobile + "</td>\n" +
                        "<td class='r_emal'>" + res.page.list[i].email + "</td>\n" +
                        "<td id=" + res.page.list[i].userId + ">\n" +
                        " <a class='compile'><span class=\"glyphicon glyphicon-pencil \" ></span></a>\n" +
                        " <a href='javascript:void(0)'  class='Delete'><span class=\"glyphicon glyphicon-trash \"></span></a>\n" +
                        "</td>\n" +
                        "</tr>"

                }

                $("#div").append(html)
                //滑動按鈕
                var Id_a
                $(".toogle").click(function () {
                    var username = $(this).parent().parent().siblings(".username").html();
                    var ele = $(this).children(".move");
                    Id_a = $(this).attr("id");
                    if (ele.attr("data-state") == "1") {
                        ele.animate({left: "0"}, 300, function () {
                            ele.attr("data-state", "0");
                            var state = 0
                            aaa(state, username, Id_a)
                        });
                        $(this).removeClass("on").addClass("off");
                        return
                    } else if (ele.attr("data-state") == "0") {
                        ele.animate({left: '50%'}, 300, function () {
                            ele.attr("data-state", "1");
                            var state = 1
                            aaa(state, username, Id_a)
                        });
                        $(this).removeClass("off").addClass("on");
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
                            console.log(res)
                        }
                    })

                }

                //編輯
                $(".compile").click(function () {
                    console.log("iiiiiiii")
                    console.log($(".mistake").html())
                    $(".shade,.shade_b").css("display", "block")
                    //id
                    Id_a = $(this).parent().attr('id');
                    var na_ma = $(this).parent().siblings(".username").html();
                    $("#acc_number_b").val(na_ma);
                    var r_user = $(this).parent().siblings(".r_user").html();
                    $("#acc_user_b").val(r_user);
                    // 密码
                    var r_pw = $(this).parent().siblings(".r_pw").html();
                    $("#acc_password_b").val(r_pw);
                    var r_termof = $(this).parent().siblings(".r_termof").html();
                    $("#acc_select_b option:selected").text(r_termof);
                    var r_typ = $(this).parent().siblings(".r_typ").html();
                    $("#acc_call_b").val(r_typ);
                    var r_emal = $(this).parent().siblings(".r_emal").html();
                    $("#acc_mailbox_b").val(r_emal);
                })
                    //修改管理员
                $("#sha_que_can").click(function () {
                    console.log("8888888888888888888888")
                    //账号
                    var na_ma_mod = $("#acc_number_b").val();
                    //用户
                    var r_user_mod = $("#acc_user_b").val();
                    // 密码
                    var r_pw_mod = $("#acc_password_b").val();
                    // 年限
                    var r_termof_mod = $("#acc_select_b option:selected").text();
                    var termOfValidity = r_termof_mod.substr(r_termof_mod.length - 2, 1)
                    //图片
                    var r_src_mod = $('#previewr').css('backgroundImage');
                    //邮箱
                    var r_emal_mod = $("#acc_mailbox_b").val();
                    //手机
                    var r_typ_mod = $("#acc_call_b").val();
                    //管理者
                    var r_Ad_mod = $("#r_Ad_mod").val();

                    regular(r_typ_mod,r_emal_mod,na_ma_mod,r_user_mod,r_pw_mod)

                    lay()

                    if (na_ma_mod == "" || r_user_mod == "" || r_pw_mod == "" || r_termof_mod == "" || r_emal_mod == "" || r_typ_mod == "" || r_Ad_mod == "") {
                        // alert("輸入不能為空")
                    } else {
                        $.ajax({
                            url: baseURL + 'sys/user/update',
                            type: "POST",
                            data:
                            "&userId=" + Id_a +
                            "&username=" + na_ma_mod +
                            "&email=" + r_emal_mod +
                            "&mobile=" + r_typ_mod +
                            "&headUrl=" + "" +
                            "&realName=" + r_user_mod +
                            "&termOfValidity=" + termOfValidity +
                            "&type=" + r_Ad_mod,
                            success: function (res) {
                                console.log(JSON.stringify(res));
                                if (res.code == "200") {
                                    alert('修改成功')
                                     window.location.reload()

                                } else {
                                    alert(res.msg);
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
                                alert(r.msg);
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
                        $("#div").html("")
                        form(pageSize, pagesb)
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
    //编辑弹窗刪除////////////////(编辑头像)
    $("#shade_a,.sha_cancel,.guan_shc").click(function () {
        $(".shade,.shade_b").css("display", "none")
    })


    //删除彈窗/////////////////////
    $(".shade_a_delete,.sha_cancel_delete,.guan_sha").click(function () {
        $(".shade_delete,.shade_b_delete").css("display", "none")
    })

    //新建//////////////////////////（新建传参少头像）
    $(".btn_m_new").click(function () {
        $(".shade_new,.shade_b_new").css("display", "block")
    })
    $(".shade_a_new,.sha_cancel_new,.guan_shb").click(function () {
        $(".shade_new,.shade_b_new").css("display", "none")
    })
    $("#confirm_Z").click(function () {
        var acc_number = $("#acc_number").val()
        var acc_user = $("#acc_user").val()
        var acc_password = $("#acc_password").val()
        //图片
        var r_src_mod = $('#preview').css('backgroundImage');
        var acc_select = $("#acc_select option:selected").text()
        var acc_mailbox = $("#acc_mailbox").val()
        var acc_call = $("#acc_call").val();
        var roleId = $("#role").val();
        regular(acc_call,acc_select,acc_number,acc_user,acc_password)
        lay()
        if (acc_number == "" || acc_user == "" || acc_password == "" || acc_mailbox == "" || acc_call == "") {
            alert("輸入不能為空")
        } else {
            $.ajax({
                url: baseURL + 'sys/user/save',
                type: "POST",
                data:
                "&headUrl=" + "" +
                "&username=" + acc_number +
                "&realName=" + acc_user +
                "&password=" + acc_password +
                "&termOfValidity=" + acc_select +
                "&email=" + acc_mailbox +
                "&mobile=" + acc_call +
                "&roleId=" + roleId,
                success: function (res) {
                    console.log(JSON.stringify(res));
                    if (res.code == 200) {
                        $("#acc_number").val("")
                        $("#acc_user").val("")
                        $("#acc_password").val("")
                        $("#acc_select option:selected").text("")
                        $("#acc_mailbox").val("")
                        $("#acc_call").val("");
                        $("#role").val("");
                        alert('保存成功', function () {
                             window.location.reload()
                        });
                    } else {
                        alert(res.msg);
                    }
                }
            })
        }
    });


//账户修改  选择图片
//     var preview = document.querySelector('#previewr');
//     var eleFile = document.querySelector('#file');
//     eleFile.addEventListener('change', function () {
//         var file = this.files[0];
//         // 确认选择的文件是图片
//         if (file.type.indexOf("image") == 0) {
//             var reader = new FileReader();
//             reader.readAsDataURL(file);
//             reader.onload = function (e) {
//                 // 图片base64化
//                 var newUrl = this.result;
//                 preview.style.backgroundImage = 'url(' + newUrl + ')';
//             };
//         }
//     });


    $(function() {
        $("#pic").click(function() {
            $("#upload").click(); //隐藏了input:file样式后，点击头像就可以本地上传
            $("#upload").on("change", function() {
                var objUrl = getObjectURL(this.files[0]); //获取图片的路径，该路径不是图片在本地的路径
                if (objUrl) {
                    $("#pic").attr("src", objUrl); //将图片路径存入src中，显示出图片
                    upimg();
                }
            });
        });
    });

    //建立一?可存取到?file的url
    function getObjectURL(file) {
        var url = null;
        if (window.createObjectURL != undefined) { // basic
            url = window.createObjectURL(file);
        } else if (window.URL != undefined) { // mozilla(firefox)
            url = window.URL.createObjectURL(file);
        } else if (window.webkitURL != undefined) { // webkit or chrome
            url = window.webkitURL.createObjectURL(file);
        }
        return url;
    }
    //上传头像到服务器
    function upimg() {
        console.log(344)
        var pic = $('#upload')[0].files[0];
        var file = new FormData();
        file.append('image', pic);
        $.ajax({
            url: "/uploadImg",
            type: "post",
            data: file,
            cache: false,
            contentType: false,
            processData: false,
            success: function(data) {
                console.log(data);
                var res = data;
                $("#resimg").append("<img src='/" + res + "'>")
            }
        });
    }


//分页
    $(".ui-pagination-container").pagination({
        currentPage: pageNum,
        totalPage: pages,
        isShow: true,
        count: 7,
        homePageText: "首页",
        endPageText: "尾页",
        prevPageText: "上一页",
        nextPageText: "下一页",
        callback: function (current) {
            //当前页数current
            var pagesb = current
            $("#div").html("")
            form(pageSize, pagesb)
        }
    });


});


