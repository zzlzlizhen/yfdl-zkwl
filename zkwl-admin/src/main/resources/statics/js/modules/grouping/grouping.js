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
    //项目名称
    var character=href['Na_me'];
    $("#hear_project").html(character)
    //用户名称
    var exclusiveUser=href['exclusiveUser'];
    $("#hear_city_b").html(exclusiveUser)
    //项目id
    var  Id=href['projectId'];
    var pages
    var pageSize
    var pageNum

    //渲染表格
    form(10,1)
    function form(pageSizea,pagesa){
        $.ajax({
            url: baseURL + 'fun/device/queryDevice',
            contentType: "application/json;charset=UTF-8",
            type: "POST",
            data: JSON.stringify({
                "projectId":Id,
                "pageSize":pageSizea,
                "pageNum":pagesa
            }),
            success: function (res) {
                console.log(res)
                pages=res.data.pages;
                pageSize=res.data.pageSize;
                pageNum=res.data.pageNum
                var html
                for (var i = 0; i < res.data.list.length; i++) {
                    html += "<tr>\n" +
                        "<td style=\"width:4%;\"> <input type= \"checkbox\" class=\"checkbox_in\"> </td>\n" +
                        "<td>"+res.data.list[i].deviceCode+"</td>\n" +
                        "<td style=\"width:10%;\">"+res.data.list[i].deviceName+"</td>\n" +
                        "<td>"+res.data.list[i].groupName+"</td>\n" +
                        "<td>"+res.data.list[i].deviceType+"</td>\n" +
                        "<td>"+res.data.list[i].photocellState+"</td>\n" +
                        "<td>"+res.data.list[i].batteryState+"</td>\n" +
                        "<td>"+res.data.list[i].loadState+"</td>\n" +
                        "<td>"+res.data.list[i].signalState+"</td>\n" +
                        "<td>"+res.data.list[i].runState+"</td>\n" +
                        "<td>"+res.data.list[i].light+"</td>\n" +
                        "<td>"+res.data.list[i].communicationType+"</td>\n" +
                        "<td id="+res.data.list[i].deviceId+" style=\"width:10%;\">" +
                        "<a href=\"#\" class='particulars'><span class=\"glyphicon glyphicon-search\"></span></a>\n" +
                        "<a href=''><span class=\"glyphicon glyphicon-picture\"></span></a>\n" +
                        "<a href='' class='deleteq'><span class=\"glyphicon glyphicon-trash\"></span></a>\n" +
                        "</td>\n" +
                        "</tr>"
                }
                $("#div").append(html);
                //   删除
                $(".deleteq").click(function(){
                    var Dele_id=$(this).parent().attr('id');
                    console.log(Dele_id)
                    $.ajax({
                        url:baseURL + 'fun/device/delete?deviceIds='+Dele_id,
                        contentType:"application/json;charset=UTF-8",
                        type:"get",
                        data:{},
                        success: function(res) {
                            console.log("删除")
                            console.log(res)
                            if(res.code == "200"){
                                window.location.reload()
                            }else{
                                alert("删除失败")
                            }
                        }
                    })
                })
                //编辑
                var proid
                $(".particulars").click(function(){
                    $(".shade_modifier,.shade_b_modifier").css("display","block");
                    proid=$(this).parent().attr('id');
                    fen()
                })
                $("#confirm_x").click(function(){
                    var pro_s_b= $(".pro_s_b").val();
                    var select_b=$("#select1_b option:selected").attr("id");
                    $.ajax({
                        url:baseURL + 'fun/device/updateDevice',
                        contentType: "application/json;charset=UTF-8",
                        type:"POST",
                        data:JSON.stringify({
                            "projectId":Id,
                            "groupId":select_b,
                            "deviceName":pro_s_b,
                            "deviceId":proid
                        }),
                        success: function(res){
                            console.log("编辑")
                            console.log(res)
                            if(res.code == "200"){
                                window.location.reload()
                            }

                        }
                    });
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
                    callback: function(current) {
                        //当前页数current
                        var pagesb = current
                        $("#div").html("")
                        form(pageSize, pagesb)
                    }
                });

            }
        })
    }


     //    添加
    $("#add_equipments").click(function(){
        $(".shade_project,.shade_b_project").css("display","block");
        //添加设备-分组id
        fen()
    })
    function fen(){
        $.ajax({
            url:baseURL + 'fun/group/queryGroupNoPage?projectId='+Id,
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data:{},
            success: function(res) {
                var html
                for (var i = 0; i < res.data.length; i++) {
                    html += "<option class='option' id="+res.data[i].groupId+">"+res.data[i].groupName+"</option>\n"
                }
                $("#select1,#select1_b").append(html)
            }
        })
    }
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none")
    })

    //确认添加
    $("#project_confirm").click(function(){
        var pro_name= $(".pro_name").val();
        var pro_s= $(".pro_s").val();
        var select_op=$("#select1 option:selected").attr("id");
        if(pro_s =="" || pro_name==""){
            alert("输入不能为空")
            return
        }else{
            $.ajax({
                url:baseURL + 'fun/device/add',
                contentType: "application/json;charset=UTF-8",
                type:"POST",
                data: JSON.stringify({
                    "projectId":Id,
                    "groupId":select_op,
                    "deviceName":pro_s,
                    "deviceCode":pro_name
                }),
                success: function(res) {
                    console.log("确认")
                    console.log(res)
                    if(res.code == "200"){
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
//控制面板
    $("#hear_control").click(function(){
        location.href ='../control/control.html';
    })
//刷新页面
//     $("#refresh").click(function(){
//         window.location.reload()
//     })
//分组管理
    $("#grouping").click(function(){
        var proid=Id
        var searchUrl=encodeURI('../management/management.html?projectId='+proid)
        location.href =searchUrl;
    })
//天气
    //获取城市ajax
    $.ajax({
        url: 'http://api.map.baidu.com/location/ip?ak=ia6HfFL660Bvh43exmH9LrI6',
        type: 'POST',
        dataType: 'jsonp',
        success:function(data) {
            var Cheng_s=JSON.stringify(data.content.address_detail.province);
            var cut_a=Cheng_s.substring(1);
            var cut_b=cut_a.substring(0,cut_a.length-2)
            $('#weather').html(cut_b)
            //天气
            $.ajax({
                url:'http://wthrcdn.etouch.cn/weather_mini?city='+cut_b,
                data:"",
                dataType:"jsonp",
                success:function(data){
                    console.log("天气")
                    console.log(data);
                    var str=data.data.forecast[0].high
                    var str_a=str.slice(-3)
                    var str_b=data.data.forecast[0].low
                    var str_bb=str_b.slice(-3)

                    var T_an=data.data.forecast[0].type+" "+str_bb+"-"+str_a
                    $("#T_an").html(T_an)
                }
            })
        }
    });





})