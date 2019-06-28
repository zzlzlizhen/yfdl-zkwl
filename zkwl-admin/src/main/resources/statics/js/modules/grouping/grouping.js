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
    //总装机数
    var sumCount=href['sumCount'];
    $("#sumCount").html(sumCount)
    //网关数
    var gatewayCount=href['gatewayCount'];
    $("#gatewayCount").html(gatewayCount)
    //故障数
    var faultCount=href['faultCount'];
    $("#faultCount").html(faultCount)
    //报警数
    var callPoliceCount=href['callPoliceCount'];
    $("#callPoliceCount").html(callPoliceCount)
    //项目id
    var  Id=href['projectId'];
    var pages
    var pageSize
    var pageNum

    //搜索
    $("#proje_search").click(function(){
       var Se_id=$("#Se_id").val();
       var Se_name=$("#Se_name").val();
       var select=$("#se_for option:selected").text()
       var selecta=$("#se_fora option:selected").text()
        $("#div").html("");
        form(pageSize,pageNum,Se_id,Se_name,select,selecta)
    })

    //渲染表格
    form(10,1,"","","","")
    function form(pageSizea,pagesa,deviceCode,deviceName,groupId,deviceType){
        $.ajax({
            url: baseURL + 'fun/device/queryDevice',
            contentType: "application/json;charset=UTF-8",
            type: "POST",
            data: JSON.stringify({
                "deviceCode":deviceCode,//设备编号
                "deviceName":deviceName,//设备名称
                "groupId":groupId,//分组id
                "deviceType":deviceType,//设备类型
                "projectId":Id,
                "pageSize":pageSizea,
                "pageNum":pagesa
            }),
            success: function (res) {
                console.log("数据")
                console.log(res)
                pages=res.data.pages;
                pageSize=res.data.pageSize;
                pageNum=res.data.pageNum
                var html=""
                for (var i = 0; i < res.data.list.length; i++) {
                    html += "<tr>\n" +
                        "<td id="+res.data.list[i].deviceId+" style=\"width:4%;\"> <input type= \"checkbox\" class=\"checkbox_in  checkbox_i\"> </td>\n" +
                        "<td id='r_nm'>"+res.data.list[i].deviceCode+"</td>\n" +
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
                        "<a href=\"#\" class='ma_p' id="+res.data.list[i].longitude+","+res.data.list[i].latitude+"><span class=\"glyphicon glyphicon-picture\"></span></a>\n" +
                        "<a href='' class='deleteq'><span class=\"glyphicon glyphicon-trash\"></span></a>\n" +
                        "</td>\n" +
                        "</tr>"
                }
                $("#div").append(html);
                // 地图定位
                $(".ma_p").click(function(){
                    var longitude=$(this).attr("id")
                    var searchUrl=encodeURI('../equipment/equipment.html?longitude='+longitude)
                    location.href =searchUrl;
                })
                //移动分组删除
                var arr=[]
                var ass=[]
                $(".checkbox_i").click(function () {
                    var che_c=$(this).prop('checked');
                    if(che_c == true){
                        $("#checkbox[name=all]:checkbox").prop('checked', true);
                        var devId=$(this).parent().attr('id');
                            arr.push(devId)
                        var deviceCode=$(this).parent().siblings("#r_nm").html();
                            ass.push(deviceCode)
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
                        var deviceCode=$(this).parent().siblings("#r_nm").html();
                        var index1 = ass.indexOf(deviceCode);
                            ass.splice(index1, 1);

                        var len=arr.length;
                        $("#mo_sp").html(len+"项")
                    }
                })
                //控制面板
                $("#hear_control").click(function(){
                    console.log("ass++++++++++++++++++++")
                    console.log(ass)
                    var searchUrl=encodeURI('../control/control.html?deviceCode='+ass)
                    location.href =searchUrl;
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
                    dele(ids);
                })
                //   删除
                $(".deleteq").click(function(){
                    var Dele_id=$(this).parent().attr('id');
                    dele(Dele_id);
                })
                function dele(Dele_id){
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
                            var html=""
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
                    //正则  丸子
                    if(pro_s_b == ""||select_b == ""){
                        $(".mistake").css("display","block")
                    }else{
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
                                if(res.code == "200"){
                                    window.location.reload()
                                }

                            }
                        });
                    }

                })

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
            $(".mistake").css("display","block")
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
                    if(res.code == "200"){
                        $(".pro_name").val("");
                        $(".pro_s").val("");
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

//分组管理
    $("#grouping").click(function(){
        var proid=Id
        var searchUrl=encodeURI('../management/management.html?projectId='+proid+"&character="+character)
        location.href =searchUrl;
    })
//移动分组弹窗
    $(".shade_modifier_yi").click(function(){
        $(".shade_Yi,.shade_b_yi").css("display","none")
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
            console.log("城市")
            console.log(data)
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
                    // if(type == "多云"){
                    //     var lujing=$("#img").attr("src");
                    //     lujing='天气.png'
                    // }else if(){
                    //
                    // }else if(){
                    //
                    // }

                    var T_an=data.data.forecast[0].type+" "+str_bb+"-"+str_a;

                    if(data.data.forecast[0].type== "多云"){
                        $("#img").attr("src","${request.contextPath}/statics/image/duoyun.svg")
                    }else if(data.data.forecast[0].type== "晴"){
                        $("#img").attr("src","${request.contextPath}/statics/image/qing.svg")
                    }else if(data.data.forecast[0].type== "雨"){
                        $("#img").attr("src","${request.contextPath}/statics/image/yu.svg")
                    }
                    console.log(  $("#img").attr("src",url))
                    // $("#T_an").html(T_an)
                }
            })
        }
    });





})