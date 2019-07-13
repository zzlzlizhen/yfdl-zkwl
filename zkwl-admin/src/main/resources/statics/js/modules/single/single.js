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
                console.log("分组下设备")
                console.log(res)
                pages = res.data.pages;
                pageSize = res.data.pageSize;
                pageNum = res.data.pageNum
                var html="";
                var offClass = "";
                for (var i = 0; i < res.data.list.length; i++) {
                    if (res.data.list[i].onOff != 0 ) {
                        offClass = "btn_fath clearfix  toogle on";
                    } else {
                        offClass = "btn_fath clearfix  toogle off";
                    }

                    //光电池状态
                    var photocellState=res.data.list[i].photocellStat
                    if (photocellState == null ) {
                        photocellState = "";
                    } else if(photocellState == 0) {
                        photocellState = "光弱";
                    }else if(photocellState == 1) {
                        photocellState = "光强";
                    }else if(photocellState == 2) {
                        photocellState = "正在充电";
                    }
                    //蓄电池状态
                    var batteryState=res.data.list[i].batteryState
                    if (batteryState == null ) {
                        batteryState = "";
                    } else if(batteryState == 0) {
                        batteryState = "过放";
                    }else if(batteryState == 1) {
                        batteryState = "欠压";
                    }else if(batteryState == 2) {
                        batteryState = "正常";
                    }else if(batteryState == 3) {
                        batteryState = "限压值";
                    }else if(batteryState == 4) {
                        batteryState = "超压";
                    }else if(batteryState == 5) {
                        batteryState = "过温";
                    }else if(batteryState == 6) {
                        batteryState = "激活";
                    }
                    //负载
                    var loadState=res.data.list[i].loadState
                    if (loadState == null ) {
                        loadState = "";
                    } else if(loadState == 0) {
                        loadState = "关";
                    }else if(loadState == 1) {
                        loadState = "开";
                    }else if(loadState == 2) {
                        loadState = "开路保护";
                    }else if(loadState == 3) {
                        loadState = "直通";
                    }else if(loadState == 4) {
                        loadState = "短路保护";
                    }else if(loadState == 5) {
                        loadState = "过载保护";
                    }else if(loadState == 6) {
                        loadState = "过载警告";
                    }
                    //运行状态
                    var runState=res.data.list[i].runState
                    if (runState == null ) {
                        runState = "";
                    } else if(runState == 1) {
                        runState = "正常";
                    }else if(runState == 2) {
                        runState = "报警";
                    }else if(runState == 3) {
                        runState = "故障";
                    }

                    //通讯类型
                    var communicationType=res.data.list[i].communicationType
                    if (communicationType == null ) {
                        communicationType = "";
                    } else if(communicationType == 1) {
                        communicationType = "2G";
                    }else if(communicationType == 2) {
                        communicationType = "Lora";
                    }else if(communicationType == 3) {
                        communicationType = "NBlot";
                    }
                    //亮度
                    var light=res.data.list[i].light
                    if (light == null ) {
                        light = "";
                    } else  {
                        light = light;
                    }
                    //信号状态
                    var signalState=res.data.list[i].signalState
                    if(signalState == null){
                        signalState=""
                    }else if(signalState == 0){
                        signalState="-0"
                    }else if(signalState == 1){
                        signalState="-1"
                    }else if(2 <= signalState <= 30){
                        signalState="-2"
                    }else if(31 <= signalState <= 51){
                        signalState="-31"
                    }else if(52 <= signalState <= 99){
                        signalState="-99"
                    }

                    var updateTime=res.data.list[i].updateTime
                    if(updateTime == null){
                        updateTime=""
                    }else{
                        updateTime=updateTime
                    }

                    html += "<tr>\n" +
                        "<td id=" + res.data.list[i].deviceId + " style=\"width:4%;\"> <input type= \"checkbox\" name='clk' class=\"checkbox_in checkbox_i\"> </td>\n" +
                        "<td class='li_deviceCode'>" + res.data.list[i].deviceCode + "</td>\n" +
                        "<td id='r_namem'>" + res.data.list[i].deviceName + "</td>\n" +
                        "<td class='li_deviceType' id=" + res.data.list[i].projectId + ">" + res.data.list[i].deviceType + "</td>\n" +
                        "<td class='grod' id="+res.data.list[i].groupId+">" + photocellState + "</td>\n" +
                        "<td>" + batteryState + "</td>\n" +
                        "<td>" + loadState + "</td>\n" +
                        "<td>" + signalState + "</td>\n" +
                        "<td>" + runState + "</td>\n" +
                        "<td>" + light + "</td>\n" +
                        "<td>" + communicationType + "</td>\n" +
                        "<td>" +
                        "<div class=\"switch\"> \n" +
                        "<div class='" + offClass + "' id=" + res.data.list[i].deviceId + " > \n" +
                        "<div class=\"move\" data-state=" + res.data.list[i].onOff + "></div> \n" +
                        "<div class=\"btnSwitch btn1\">关</div> \n" +
                        "<div class=\"btnSwitch btn2 \">开</div> \n" +
                        "</div>" +
                        "</td>\n"+
                        "<td id="+ res.data.list[i].groupName + " class='sha'>" + updateTime + "</td>\n" +
                        "<td id=" + res.data.list[i].deviceId + ">" +
                        "<a class='particulars_a' ><img src='/remote-admin/statics/image/r_kongzhi.svg' alt='' class='r_erkongzhi'></a>\n" +
                        "<a class='particulars'><img src='/remote-admin/statics/image/bianji.png' alt=''></a>\n" +
                        "<a href=\"#\" class='ma_p' id="+res.data.list[i].longitude+","+res.data.list[i].latitude+"><img src='/remote-admin/statics/image/ditu.svg' alt=''style='width: 25px;height:25px;'></a>\n" +
                        "<a  class='deleteq'><img src='/remote-admin/statics/image/shanchu.png' alt=''></a>\n" +
                        "</td>\n" +
                        "</tr>"
                }
                $("#div").append(html);
                // 地图定位
                $(".ma_p").click(function(){
                    var longitude=$(this).attr("id");
                    var name=$(this).parent().siblings("#r_namem").html()
                    var searchUrl=encodeURI('../equipment/equipment.html?longitude='+longitude+"&name="+name)
                    location.href =searchUrl;
                })
                //控制面板
                $(".particulars_a").click(function(){
                    var deviceCode=$(this).parent().siblings(".li_deviceCode").html();
                    var grod=$(this).parent().siblings(".grod").attr('id');
                    var type=$(this).parent().siblings(".li_deviceType").html();

                    var searchUrl=encodeURI('../control/control.html?deviceCode='+deviceCode+"&grod="+grod+"&type="+type)
                    location.href =searchUrl;
                })
                //移动分组删除
                var arr=[]
                //全选
                $('#checkbox[name="all"]').click(function(){
                    arr=[]
                    if($(this).is(':checked')){
                        $('input[name="clk"]').each(function(){
                            $(this).prop("checked",true);
                            var devId=$(this).parent().attr('id');
                            arr.push(devId)
                            var len=arr.length;
                            $("#mo_sp").html(len+"项")
                            $(".move_a").show()
                        });
                    }else{
                        $('input[name="clk"]').each(function(){
                            $(this).prop("checked",false);
                            var devId=$(this).parent().attr('id');
                            arr=[]
                            $("#mo_sp").html(""+"项")
                            $(".move_a").hide()
                        });
                    }
                });
                //单选
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
                var Dele_id
                $(".deleteq").click(function(){
                    $(".shade_delete,.shade_b_delete").css("display", "block");
                     Dele_id=$(this).parent().attr('id');
                })

                $(".sha_que_delete").click(function(){
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

                //滑动按钮
                $(".toogle").click(function () {
                    var li_deviceCode = $(this).parent().parent().siblings(".li_deviceCode").html();
                    var li_deviceType = $(this).parent().parent().siblings(".li_deviceType").html();
                    var deviceId=$(this).attr("id") //设备id
                    var projectId =$(this).parent().parent().siblings(".li_deviceType").attr("id")
                    var ele = $(this).children(".move");

                    if (ele.attr("data-state") != "0") {
                        ele.animate({left: "0"}, 300, function () {
                            ele.attr("data-state", "0");
                            var value=[0]
                            off(li_deviceCode,value,li_deviceType,deviceId,projectId)
                        });
                        $(this).removeClass("on").addClass("off");
                    } else if (ele.attr("data-state") == "0") {
                        ele.animate({left: '50%'}, 300, function () {
                            ele.attr("data-state", "1");
                            var value=[1]
                            off(li_deviceCode,value,li_deviceType,deviceId,projectId)
                        });
                        $(this).removeClass("off").addClass("on");
                    }
                })

                function  off(li_deviceCode,value,li_deviceType,deviceId,projectId){
                    var ass=[];
                        ass.push(li_deviceCode)
                    $.ajax({
                        url:baseURL + 'fun/device/change',
                        contentType: "application/json;charset=UTF-8",
                        type:"POST",
                        data: JSON.stringify({
                            "deviceCodes": ass, //需要修改的设备code   /0
                            "qaKey": ["onOff"], //需要修改的参数键
                            "value": value, //需要修改的参数值
                            "deviceType": li_deviceType, //设备类型   /1
                            "status": 2
                        }),
                        success: function(res) {
                          console.log(res)
                        }
                    })
                    $.ajax({
                        url:baseURL + 'fun/device/updateOnOffByIds',
                        contentType: "application/json;charset=UTF-8",
                        type:"POST",
                        data: JSON.stringify({
                            "deviceId":deviceId,//设备id
                            "projectId":projectId,//项目id
                            "onOff":value[0], //0：关；1：开：
                        }),
                        success: function(res) {
                            console.log(res)
                        }
                    })
                }

                //编辑
                var projectId
                $(".particulars").click(function(){
                    $(".shade_modifier,.shade_b_modifier").css("display","block");
                    proid=$(this).parent().attr('id');
                    var r_namem = $(this).parent().siblings("#r_namem").html();
                    projectId = $(this).parent().siblings(".li_deviceType").attr("id");
                    $(".pro_s_b").val(r_namem)
                    var r_rrenae = $(this).parent().siblings(".sha").attr("id");
                    $("#select1_b option:selected").text(r_rrenae);


                    fen(projectId)
                })
                $("#confirm_x").click(function(){
                    var pro_s_b=$(".pro_s_b").val();
                    var groupId=$("#select1_b option:selected").attr("id");
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

                function fen(projectId){
                    console.log(projectId)
                    $.ajax({
                        url:baseURL + 'fun/group/queryGroupNoPage?projectId='+projectId,
                        contentType: "application/json;charset=UTF-8",
                        type:"get",
                        data:{},
                        success: function(res) {
                            var html=""
                            for (var i = 0; i < res.data.length; i++) {
                                html += "<option class='option opti_a' id="+res.data[i].groupId+">"+res.data[i].groupName+"</option>\n"
                            }
                            $("#select1,#select1_b").append(html)
                        }
                    })
                }
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

//确定删除
    $(".rqubtn,.shade_a_delete").click(function () {
        $(".shade_delete,.shade_b_delete").css("display", "none")
    })
    //删除彈窗/////////////////////
    $(".glyphicon,.glyphicon-remove,.guan_sha").click(function () {
        $(".shade_delete,.shade_b_delete").css("display", "none")
    })
//    新增设备
    $("#add_single").click(function(){
        $(".shade_project,.shade_b_project").css("display","block");
        $.ajax({
            url:baseURL + 'fun/group/queryGroupNoPage?projectId='+Id,
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data:{},
            success: function(res) {
                var html=""
                for (var i = 0; i < res.data.length; i++) {
                    html += "<option class='option opti_a' id="+res.data[i].groupId+">"+res.data[i].groupName+"</option>\n"
                }
                $("#select1,#select1_b").append(html)
            }
        })
    })

    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none")
    })
    $("#project_confirm").click(function(){
        var pro_name=$(".pro_name").val()
        var pro_s=$(".pro_s").val()
        var groupId=$("#select1 option:selected").attr("id");
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