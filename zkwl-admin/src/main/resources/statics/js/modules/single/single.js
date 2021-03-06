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
    var J_t
    var proid //编辑id
    var sing_id
    var sing_name
    var select
    //头部导航
    $("#pa").html(character+">");
    $("#pb").html(groupName);
    var $color = localStorage.getItem("mycolor");

    $.ajax({
        url:baseURL + 'fun/deviceType/getDeviceType',
        contentType: "application/json;charset=UTF-8",
        type:"get",
        data:{},
        success: function(res) {
            var html=""
            for (var i = 0; i < res.data.length; i++) {
                html += "<option class='option opti_a' id="+res.data[i].deviceTypeCode+">"+res.data[i].deviceTypeName+"</option>\n"
            }
            $("#sing_se").append(html)
        }
    });



    //搜索
    $("#proje_search").click(function(){
         sing_id=$("#sing_id").val();
         sing_name=$("#sing_name").val();
        select=$("#sing_se option:selected").attr("id")
        $("#div").html("");
        form(pageSize,pageNum,sing_id,sing_name,select);
        refresh();
    })
    //自动刷新
    var t;
    refresh();
    function refresh(){
        if($("#sing_id").val()=="" && $("#sing_name").val()=="" && $("#sing_se").val()=="" ){
            t=setInterval(function(){
                form(10,1,"","","");
            }, 50000);
        }else{
            clearInterval(t);
        }
    }

    form(10,1,"","","");
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
                pageNum = res.data.pageNum;
                J_t=res.data.list.length;
                var html="";
                var offClass = "";
                for (var i = 0; i < res.data.list.length; i++) {
                    if (res.data.list[i].onOff != 0 ) {
                        offClass = "btn_fath clearfix  toogle on";
                    } else {
                        offClass = "btn_fath clearfix  toogle off";
                    }

                    //光电池状态
                    var photocellState=res.data.list[i].photocellState
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
                    var batteryState=res.data.list[i].batteryState;
                    if (batteryState == null ) {
                        batteryState = "";
                    } else if(batteryState == 0) {
                        batteryState = "过放";
                    }else if(batteryState == 1) {
                        batteryState = "欠压";
                    }else if(batteryState == 2) {
                        batteryState = "正常";
                    }else if(batteryState == 3) {
                        batteryState = "限压";
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
                    }else if(runState == 4) {
                        runState = "离线";
                    }else if(runState == 5) {
                        runState = "升级中";
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
                        signalState="<img src='/statics/image/rxinhao-7.svg' alt='' style='display: inline-block;width: 29px;height: 27px' class='xinhao'>"
                    }else if(signalState == 1){
                        signalState="<img src='/statics/image/rxinhao_8.svg' alt='' style='display: inline-block;width: 29px;height: 27px' class='xinhao'>"
                    }else if(2 <= signalState <= 30){
                        signalState="<img src='/statics/image/rxinhao-10.svg' alt='' style='display: inline-block;width: 29px;height: 27px' class='xinhao'>"
                    }else if(31 <= signalState <= 51){
                        signalState="<img src='/statics/image/rxinhao_2.svg' alt='' style='  display: inline-block;width: 29px;height: 27px' class='xinhao'>"
                    }else if(52 <= signalState <= 99){
                        signalState="<img src='/statics/image/zhuyi.png' alt='' >"
                    }

                    var updateTime=res.data.list[i].updateTime
                    if(updateTime == null){
                        updateTime=""
                    }else{
                        updateTime=updateTime
                    }

                    html += "<tr>\n" +
                        "<td id=" + res.data.list[i].deviceId + " style=\"width:4%;\"> <input type= \"checkbox\" name='clk' class=\"checkbox_in checkbox_i\"> </td>\n" +
                        "<td class='li_deviceCode' id=" + res.data.list[i].deviceType + ">" + res.data.list[i].deviceCode + "</td>\n" +
                        "<td id='r_namem' title= "+ res.data.list[i].deviceName + ">" + res.data.list[i].deviceName + "</td>\n" +
                        "<td class='li_deviceType' title="+ res.data.list[i].deviceTypeName +" id=" + res.data.list[i].projectId + ">" + res.data.list[i].deviceTypeName + "</td>\n" +
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
                        "<td id="+ res.data.list[i].groupName + " class='sha' title=" + updateTime + ">" + updateTime + "</td>\n" +
                        "<td id=" + res.data.list[i].deviceId + ">" +
                        "<a class='particulars_a' ><img src='/statics/image/r_kongzhi.svg' alt='' class='r_erkongzhi'></a>\n" +
                        "<a class='particulars'><img src='/statics/image/bianji.png' alt=''></a>\n" +
                        "<a href=\"#\" class='ma_p' id="+res.data.list[i].longitude+","+res.data.list[i].latitude+"><img src='/statics/image/ditu.svg' alt=''style='width: 25px;height:25px;'></a>\n" +
                        "<a  class='deleteq'><img src='/statics/image/shanchu.png' alt=''></a>\n" +
                        "</td>\n" +
                        "</tr>"
                }
                $("#div").empty().append(html);
                if($color == 2){
                    $("#add_single,.particulars_a,.particulars,.deleteq,.switch").hide();
                }
                // 地图定位
                $(".ma_p").click(function(){
                    var longitude=$(this).attr("id");
                    var name=$(this).parent().siblings("#r_namem").html();
                    var con
                    var searchUrl=encodeURI('../equipment/equipment.html?longitude='+longitude+"&name="+name+"&state="+3+"&con="+"")
                    location.href =searchUrl;
                })
                //控制面板
                $(".particulars_a").click(function(){
                    var deviceCode=$(this).parent().siblings(".li_deviceCode").html();
                    var grod=$(this).parent().siblings(".grod").attr('id');
                    var type=$(this).parent().siblings(".li_deviceCode").attr("id");
                    var name=$(this).parent().siblings("#r_namem").html();
                    var deviceId=$(this).parent().attr("id");
                    var projectId=$(this).parent().siblings(".li_deviceType").attr('id');
                    var searchUrl=encodeURI('../control/control.html?deviceCode='+deviceCode+"&grod="+grod+"&type="+type+"&name="+name+"&deviceId="+deviceId+"&projectId="+projectId)
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
                $(".checkbox_i").unbind('click');
                $(".checkbox_i").click(function () {
                    var che_c=$(this).prop('checked');
                    if(che_c == true){
                        $(this).parents("li").siblings("li").each(function() {
                            $("#checkbox[name=all]:checkbox").prop('checked', true);
                        })
                        var devId=$(this).parent().attr('id');
                        arr.push(devId);
                        var len=arr.length;
                        $("#mo_sp").html(len+"项");
                        $(".move_a").show();
                        if(len == J_t){
                            $("#checkbox[name=all]:checkbox").prop('checked', true);
                        }
                    }
                    else if(che_c == false){
                        $("#checkbox[name=all]:checkbox").prop('checked', false);
                        var devId=$(this).parent().attr('id');
                        var index = arr.indexOf(devId);
                        arr.splice(index, 1);
                        var len=arr.length;
                        $("#mo_sp").html(len+"项");
                        if(len < J_t){
                            $("#checkbox[name=all]:checkbox").prop('checked', false);
                        }
                    }
                    if($(".checkbox_i[name=clk]:checkbox:checked").length>0){
                        $(".move_a").show();
                    }else{
                        $(".move_a").hide();
                    }
                })
                // 批量删除
                $("#deleteAll").unbind('click');
                $("#deleteAll").click(function(){
                    if(arr.length <=0 ){
                        layer.open({
                            title: '信息',
                            content: '请选择删除的设备',
                            skin: 'demo-class'
                        });
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
                $(".deleteq").unbind('click');
                $(".deleteq").click(function(){
                    $(".shade_delete,.shade_b_delete").css("display", "block");
                     Dele_id=$(this).parent().attr('id');
                })
                $(".sha_que_delete").unbind('click');
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
                                layer.open({
                                    title: '信息',
                                    content: res.msg,
                                    skin: 'demo-class'
                                });
                            }
                        }
                    })
                }
                //移动分组
                $("#mobile_pac").click(function(){
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
                $("#que_yi").unbind('click');
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
                                layer.open({
                                    title: '信息',
                                    content: '移动分组成功',
                                    skin: 'demo-class'
                                });
                                window.location.reload()
                            }
                        }
                    })
                })

                //滑动按钮
                $(".toogle").unbind('click');
                $(".toogle").click(function () {
                    var li_deviceCode = $(this).parent().parent().siblings(".li_deviceCode").html();
                    var li_deviceType = $(this).parent().parent().siblings(".li_deviceCode").attr("id");
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

                        }
                    })
                }

                //编辑
                var projectId
                $(".particulars").unbind('click');
                $(".particulars").click(function(){
                    $(".shade_modifier,.shade_b_modifier").css("display","block");
                    proid=$(this).parent().attr('id');
                    var r_namem = $(this).parent().siblings("#r_namem").html();
                    projectId = $(this).parent().siblings(".li_deviceType").attr("id");
                    $(".pro_s_b").val(r_namem)
                    // var r_rrenae = $(this).parent().siblings(".sha").attr("id");
                    fen(projectId)
                })
                $("#confirm_x").unbind('click');
                $("#confirm_x").click(function(){
                    var pro_s_b=$(".pro_s_b").val();
                    var groupId=$("#select1_b option:selected").attr("id");
                    if(pro_s_b == ""){
                        $(".mistake").css("display","block")
                        $(".rrbol").html("输入不能为空");
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
                                    //window.location.reload()
                                    $(".shade_modifier").hide()
                                    $(".mistake").hide()
                                    layer.open({
                                        title: '信息',
                                        content: '修改成功',
                                        skin: 'demo-class'
                                    });
                                    form(10, 1, "", "", "", "")
                                }else{
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

                function fen(projectId){
                    $.ajax({
                        url:baseURL + 'fun/group/queryGroupNoPage?projectId='+projectId,
                        contentType: "application/json;charset=UTF-8",
                        type:"get",
                        data:{},
                        success: function(res) {
                            var arra=[];
                            for(var i = 0; i < res.data.length; i++){
                                if(res.data[i].groupName == groupName){
                                    arra.unshift({groupId:res.data[i].groupId,groupName:res.data[i].groupName})
                                }
                                if(res.data[i].groupName != groupName){
                                    arra.push({groupId:res.data[i].groupId,groupName:res.data[i].groupName})//剩下的
                                }
                            }
                            var html=""
                            for (var i = 0; i < arra.length; i++) {
                                html += "<option class='option opti_a' id="+arra[i].groupId+">"+arra[i].groupName+"</option>\n"
                            }
                            $("#select1,#select1_b").html(html)
                        }
                    })
                    $("#select1_b").val(groupName);
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
                        $("#checkbox[name=all]:checkbox").prop('checked', false);
                        $(".move_a").hide();
                        $("#mo_sp").html("");
                        var pagesb = current
                        $("#div").html("")
                        form(pageSize, pagesb,sing_id,sing_name,select)
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
    $("#add_single").unbind('click');
    $("#add_single").click(function(){
        $(".shade_project,.shade_b_project").css("display","block");
        $(".pro_name").val("")
        $(".pro_s").val("")
        $("#select1").val(groupName);
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
                $("#select1,#select1_b").html(html)
            }
        })
    })

    $(".shade_add_project,.shade_modifier_project ").click(function(){
        $(".shade_project,.shade_b_project").css("display","none");
        $(".mistake").css("display","none")
    })
    $(".glyphicon").click(function(){
        $(".mistake").css("display","none")
    })
    $("#project_confirm").unbind('click');
    $("#project_confirm").click(function(){
        var pro_name=$(".pro_name").val()
        var pro_s=$(".pro_s").val()
        var groupId=$("#select1 option:selected").attr("id");
        if(pro_name =="" || pro_s==""||groupId==""){
            $(".mistake").css("display","block")
             $(".rrbol").html("输入不能为空");
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
                       // window.location.reload()
                        $(".shade_project").hide()
                        $(".mistake").hide()
                        layer.open({
                            title: '信息',
                            content: '添加成功',
                            skin: 'demo-class'
                        });
                        form(10,1,"","","","")
                    }else{
                        layer.open({
                            title: '信息',
                            content: '添加失败',
                            skin: 'demo-class'
                        });
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