$(function(){
//获取上一个页面参数
    function showWindowHref(){
        var sHref = decodeURI(window.parent.document.getElementById("test").contentWindow.location.href);
        var args = sHref.split('?');
        if(args[0] == sHref){
            return "";
        }
        var arr = args[1].split('&');
        var obj = {};
        for(var i = 0;i< arr.length;i++){
            var arg = arr[i].split('=');
            obj[arg[0]] = arg[1];
        }
        return obj;
    }
    var href = showWindowHref();
    //项目id
    var  Id=href['projectId'];
    var  groupId=href['groupId'];
    var  character=href['character'];
    var  groupName=href['groupName'];
    var pages
    var pageSize
    var pageNum
    var proid //编辑id

    //头部导航
    $("#pa").html(character+">")
    $("#pb").html(groupName)

    //搜索
    $("#proje_search").click(function(){
        var sing_id=$("#sing_id").val()
        var sing_name=$("#sing_name").val()
        var select=$("#sing_se option:selected").text()
        $("#div").html("")
        form(pageSize,pageNum,sing_id,sing_name,select)
    })

    form(10,1,"","","")
    function form(pageSizea,pagesa,deviceCode,deviceName,deviceType) {
        $.ajax({
            url: baseURL + 'fun/device/queryDevice',
            contentType: "application/json;charset=UTF-8",
            type: "POST",
            data: JSON.stringify({
                "deviceCode":deviceCode,//设备编号
                "deviceName":deviceName,//设备名称
                "deviceType":deviceType,//设备类型
                "groupId": groupId,//分组id
                "projectId": Id, //项目id
                "pageSize": pageSizea,
                "pageNum": pagesa
            }),
            success: function (res) {
                pages = res.data.pages;
                pageSize = res.data.pageSize;
                pageNum = res.data.pageNum
                var html
                for (var i = 0; i < res.data.list.length; i++) {
                    html += "<tr>\n" +
                        "<td id=" + res.data.list[i].deviceId + " style=\"width:4%;\"> <input type= \"checkbox\" class=\"checkbox_in checkbox_i\"> </td>\n" +
                        "<td>" + res.data.list[i].deviceCode + "</td>\n" +
                        "<td>" + res.data.list[i].deviceName + "</td>\n" +
                        "<td>" + res.data.list[i].deviceType + "</td>\n" +
                        "<td>" + res.data.list[i].photocellState + "</td>\n" +
                        "<td>" + res.data.list[i].batteryState + "</td>\n" +
                        "<td>" + res.data.list[i].loadState + "</td>\n" +
                        "<td>" + res.data.list[i].signalState + "</td>\n" +
                        "<td>" + res.data.list[i].runState + "</td>\n" +
                        "<td>" + res.data.list[i].light + "</td>\n" +
                        "<td>" + res.data.list[i].communicationType + "</td>\n" +
                        "<td>" +
                        "<div class=\"switch\"> \n" +
                        "<div class=\"btn_fath clearfix on toogle\"  > \n" +
                        "<div class=\"move\" data-state=1></div> \n" +
                        "<div class=\"btnSwitch btn1\">ON</div> \n" +
                        "<div class=\"btnSwitch btn2 \">OFF</div> \n" +
                        "</div> " +
                        "</td>\n" +
                        "<td>" + res.data.list[i].updateTime + "</td>\n" +
                        "<td id=" + res.data.list[i].deviceId + ">" +
                        "<a class='particulars'><span class=\"glyphicon glyphicon-search\"></span></a>\n" +
                        "<a href=\"\"><span class=\"glyphicon glyphicon-picture\"></span></a>\n" +
                        "<a  class='deleteq'><span class=\"glyphicon glyphicon-trash\"></span></a>\n" +
                        "</td>\n" +
                        "</tr>"
                }
                $("#div").append(html);
                //移动分组删除
                var arr=[]
                $(".checkbox_i").click(function () {
                    var che_c=$(this).prop('checked');
                    if(che_c == true){
                        $("#checkbox[name=all]:checkbox").prop('checked', true);
                        var devId=$(this).parent().attr('id')
                        arr.push(devId)
                        var len=arr.length;
                        $("#mo_sp").html(len+"项")
                        $(".move_a").show()
                    }
                    else if(che_c == false){
                        if($(".checkbox_i").prop('checked') == true){
                            $("#checkbox[name=all]:checkbox").prop('checked', true);
                            $(".move_a").show()
                        }else{
                            $("#checkbox[name=all]:checkbox").prop('checked', false);
                            $(".move_a").hide()
                        }
                        var devId=$(this).parent().attr('id');
                        var index = arr.indexOf(devId);
                        arr.splice(index, 1);
                        var len=arr.length;
                        $("#mo_sp").html(len+"项")
                    }
                })
                // 批量删除
                $(".deleteAll").click(function(){
                    if(arr.length <=0 ){
                        alert("请选择删除的设备!");
                        return;
                    }
                    var ids = "";
                    for(var i = 0 ;i< arr.length;i++){
                        ids = ids + arr[i] + ",";
                    }
                    ids = ids.substr(0,ids.length - 1);
                    deletes(ids)
                })
                //删除
                $(".deleteq").click(function(){
                    var Dele_id=$(this).parent().attr('id');
                    deletes(Dele_id)
                })
                function deletes(Dele_id){
                    $.ajax({
                        url:baseURL + 'fun/device/delete?deviceIds='+Dele_id,
                        contentType:"application/json;charset=UTF-8",
                        type:"get",
                        data:{},
                        success: function(res) {
                            if(res.code == "200"){
                                window.location.reload()
                            }else{
                                alert("删除失败")
                            }
                        }
                    })
                }
                //移动分组
                $(".mobile_pac").click(function(){
                    $(".shade_Yi,.shade_b_yi").css("display","block");
                })
                //移动搜索
                $(".sousuo_i").click(function(){
                    var Input_v=$(".sousuo_input").val();
                    $("#Dan_x").html("");
                     pro_gro(Input_v)
                })

                var gr_Id
                pro_gro("")
                function pro_gro(groupName){
                    $.ajax({
                        url:baseURL + 'fun/group/queryGroupIdNoPage?projectId='+Id+"&groupName="+groupName,
                        contentType:"application/json;charset=UTF-8",
                        type:"get",
                        data:{},
                        success: function(res) {
                            var html
                            for (var i = 0; i < res.data.length; i++) {
                                html += "<span id="+res.data[i].groupId+">\n" +
                                    "<input type= \"checkbox\" class=\"checkbox_in che_i\" name=\"alla\">\n" +
                                    "<p>"+res.data[i].groupName+"</p>\n" +
                                    "</span>"
                            }
                            $("#Dan_x").append(html);
                            $(".che_i").click(function(){
                                var che_c=$(this).prop('checked');
                                if(che_c == true){
                                    $(this).parent().siblings().children(".che_i[name=alla]:checkbox").prop('checked', false);
                                    gr_Id=$(this).parent().attr('id')
                                }

                            })
                        }
                    })
                }
                //确认移动
                $("#que_yi").click(function(){
                    var ids = "";
                    for(var i = 0 ;i< arr.length;i++){
                        ids = ids + arr[i] + ",";
                    }
                    ids = ids.substr(0,ids.length - 1);
                    $.ajax({
                        url:baseURL + 'fun/device/moveGroup?deviceIds='+ids+"&groupId="+gr_Id,
                        contentType:"application/json;charset=UTF-8",
                        type:"get",
                        data:{},
                        success: function(res) {
                            if(res.code== "200"){
                                alert("移动分组成功")
                                window.location.reload()
                            }
                        }
                    })
                })


                //滑动按钮
                $(".toogle").click(function () {
                    var ele = $(this).children(".move");
                    if (ele.attr("data-state") == "1") {
                        ele.animate({left: "0"}, 300, function () {
                            ele.attr("data-state", "0");
                            alert("关！");
                        });
                        $(this).removeClass("on").addClass("off");
                    } else if (ele.attr("data-state") == "0") {
                        ele.animate({left: '50%'}, 300, function () {
                            $(this).attr("data-state", "1");
                            alert("开！");
                        });
                        $(this).removeClass("off").addClass("on");
                    }
                })

                //编辑

                $(".particulars").click(function(){
                    $(".shade_modifier,.shade_b_modifier").css("display","block");
                    proid=$(this).parent().attr('id');
                })
                $("#confirm_x").click(function(){
                    var pro_s_b=$(".pro_s_b").val()
                    if(pro_s_b == ""){
                        $(".mistake").css("display","block")
                        return
                    }else{
                        $.ajax({
                            url:baseURL + 'fun/device/updateDevice',
                            contentType: "application/json;charset=UTF-8",
                            type:"POST",
                            data: JSON.stringify({
                                "projectId":Id,//项目id
                                "groupId":groupId,//分组id
                                "deviceName":pro_s_b,//设备名称
                                "deviceId":proid
                            }),
                            success: function(res) {
                                if(res.code == "200"){
                                    window.location.reload()
                                }else{
                                    alert("编辑失败")
                                }
                            }
                        })
                    }
                })
                //    分页
                $("#pagination3").pagination({
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


            }
        })


    //    表格渲染数据结束
    }

//    新增设备
    $("#add_single").click(function(){
        $(".shade_project,.shade_b_project").css("display","block");
    })
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none")
    })
    $("#project_confirm").click(function(){
        var pro_name=$(".pro_name").val()
        var pro_s=$(".pro_s").val()
        if(pro_name =="" || pro_s==""){
            $(".mistake").css("display","block")
            return
        }else{
            $.ajax({
                url:baseURL + 'fun/device/add',
                contentType: "application/json;charset=UTF-8",
                type:"POST",
                data: JSON.stringify({
                    "projectId":Id,//项目id
                    "groupId":groupId,//分组id
                    "deviceName":pro_s,//设备名称
                    "deviceCode":pro_name//设备编号
                }),
                success: function(res) {
                    if(res.code == "200"){
                       $(".pro_name").val("")
                       $(".pro_s").val("")
                        window.location.reload()
                    }else{
                        alert("添加失败")
                    }
                }
            })
        }

    })

//编辑去弹窗/////////////////////////////////
    $(".shade_modifier_project").click(function(){
        $(".shade_modifier,.shade_b_modifier").css("display","none")
    })
//移动分组弹窗
    $(".shade_modifier_yi").click(function(){
        $(".shade_Yi,.shade_b_yi").css("display","none")
    })





})