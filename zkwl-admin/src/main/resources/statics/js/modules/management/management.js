$(function () {
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
    var character=href['character'];
    //头部数据
    $("#p_a").html(character+">")

    var pages
    var pageSize
    var pageNum
    //搜索
    $("#proje_search").click(function(){
        var select=$("#form-control option:selected").text()
        $("#div").html("")
        form(pageSize,pageNum,select)
    })


    form(10,1,"")
    function form(pageSizea,pagesa,select) {
        $.ajax({
            url: baseURL + 'fun/group/queryGroup',
            contentType: "application/json;charset=UTF-8",
            type: "POST",
            data: JSON.stringify({
                "groupName":select,
                "projectId": Id,//项目id
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
                        "<td id=" + res.data.list[i].groupId + " style=\"width:4%;\"> <input type= \"checkbox\" class=\"checkbox_in checkbox_i\"> </td>\n" +
                        "<td class='groupName'>" + res.data.list[i].groupName + "</td>\n" +
                        "<td>" + res.data.list[i].groupName + "</td>\n" +
                        "<td>" + res.data.list[i].deviceCount + "</td>\n" +
                        "<td>" + res.data.list[i].createTime + "</td>\n" +
                        "<td>" + res.data.list[i].callPoliceCount + "</td>\n" +
                        "<td>" + res.data.list[i].faultCount + "</td>\n" +
                        "<td id=" + res.data.list[i].groupId + ">" +
                        "<a  class='modifier'><span class=\"glyphicon glyphicon-pencil\"></span></a>\n" +
                        "<a  class='particulars'><span class=\"glyphicon glyphicon-search\"></span></a>\n" +
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
                    dele(ids)
                })
                //    删除
                $(".deleteq").click(function () {
                    var aproid = $(this).parent().attr('id');
                    dele(aproid)
                })
                function dele(aproid){
                    $.ajax({
                        url: baseURL + 'fun/group/delete?groupIds=' + aproid + "&projectId=" + Id,
                        contentType: "application/json;charset=UTF-8",
                        type: "get",
                        data: {},
                        success: function (res) {
                            if (res.code == "200") {
                                window.location.reload()
                            } else {
                                alert("删除失败")
                            }
                        }
                    })
                }

                //分组下设备
                $(".particulars").click(function(){
                    var bproid = $(this).parent().attr('id');
                    var groupName= $(this).parent().siblings(".groupName").html();
                    var searchUrl=encodeURI('../single/sing.html?projectId='+Id+"&groupId="+bproid+"&character="+character+"&groupName="+groupName)
                    location.href =searchUrl;
                })
                //编辑
                var proid
                $(".modifier").click(function () {
                    $(".shade_modifier,.shade_b_modifier").css("display", "block");
                    proid = $(this).parent().attr('id');
                })
                $("#confirm_man").click(function () {
                    var pro_s_b = $(".pro_s_b").val();
                    if (pro_s_b != "") {
                        $.ajax({
                            url: baseURL + 'fun/group/updateGroup',
                            contentType: "application/json;charset=UTF-8",
                            type: "POST",
                            data: JSON.stringify({
                                "groupName": pro_s_b,
                                "groupId": proid
                            }),
                            success: function (res) {
                                if (res.code == "200") {
                                    window.location.reload()
                                } else {
                                    alert("编辑失败")
                                }
                            }
                        })
                    } else {
                        alert("分组名称不能为空")
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



    //    表格结束
    }
    //添加
    $("#add_grouping").click(function(){
        $(".shade_project,.shade_b_project").css("display","block");
    })
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none")
    })
    $("#add_queD").click(function(){
        var pro_name=$(".pro_name").val()
        $.ajax({
            url: baseURL + 'fun/group/add',
            contentType: "application/json;charset=UTF-8",
            type: "POST",
            data: JSON.stringify({
                "groupName": pro_name,
                "projectId": Id
            }),
            success: function (res) {
                if(res.code == "200"){
                    $(".pro_name").val("")
                    window.location.reload()
                }else{
                    $(".mistake").css("display","block")
                }
            }
        })

    })


//编辑去弹窗/////////////////////////////////
    $(".shade_modifier_project").click(function(){
        $(".shade_modifier,.shade_b_modifier").css("display","none")
    })







})