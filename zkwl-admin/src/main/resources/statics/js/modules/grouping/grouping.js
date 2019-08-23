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
    //项目状态
    var projectStatus=href['projectStatus'];
    if (projectStatus == null ) {
        projectStatus = "";
    } else if(projectStatus == 1) {
        projectStatus = "正常运行";
    }else if(projectStatus == 2) {
        projectStatus = "停用运行";
    }
    $("#r_xiugai_six_tu").html(projectStatus);

    //项目id
    var  Id=href['projectId'];
    console.log(projectStatus)
    console.log(faultCount)
    console.log(gatewayCount)
    console.log(sumCount)
    console.log(Id)
    var pages
    var pageSize
    var pageNum
    var $color = localStorage.getItem("mycolor");
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
            $("#select1_b,#se_for").append(html)
        }
    })
    $.ajax({
        url:baseURL + 'fun/device/getDeviceType',
        contentType: "application/json;charset=UTF-8",
        type:"get",
        data:{},
        success: function(res) {
            var html=""
            for (var i = 0; i < res.data.length; i++) {
                html += "<option class='option opti_a' id="+res.data[i].deviceType+">"+res.data[i].deviceTypeName+"</option>\n"
            }
            $("#se_fora").append(html)
        }
    });

    //地图
    $("#hear_map").click(function(){
        var searchUrl=encodeURI('../equipment/equipment.html')
        location.href =searchUrl;
    })
    //搜索
    $("#proje_search").unbind('click');
    $("#proje_search").click(function(){
       var Se_id=$("#Se_id").val();
       var Se_name=$("#Se_name").val();
       // var select=$("#se_for option:selected").attr("id")
        var select=$("#se_for").val();
       var selecta=$("#se_fora option:selected").attr("id")
        var selectr="";
        $("#div").html("");
        form(pageSize,pageNum,Se_id,Se_name,select,selecta,selectr);
        refresh();
    })
    //自动刷新
    var t;
    refresh();
    function refresh(){
        if($("#Se_id").val()=="" && $("#Se_name").val()=="" && $("#se_for").val()=="" && $("#se_fora").val()==""){
            t=setInterval(function(){
                form(10,1,"","","","");
            }, 60000);
        }else{
            clearInterval(t);
        }
    }

    //渲染表格
    form(10,1,"","","","");
    function form(pageSizea,pagesa,deviceCode,deviceName,groupId,deviceType,selectr){
        $.ajax({
            url: baseURL + 'fun/device/queryDevice',
            contentType: "application/json;charset=UTF-8",
            type: "POST",
            data: JSON.stringify({
                "deviceCode":deviceCode,//设备编号
                "deviceName":deviceName,//设备名称
                "groupName":groupId,
                "deviceType":deviceType,//设备类型
                "projectId":Id,
                "pageSize":pageSizea,
                "pageNum":pagesa,
                "groupId":selectr//分组id
            }),
            success: function (res) {
                console.log("项目下设备数据")
                console.log(res)
                pages=res.data.pages;
                pageSize=res.data.pageSize;
                pageNum=res.data.pageNum
                var html=""
                for (var i = 0; i < res.data.list.length; i++) {
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

                    html += "<tr>\n" +
                        "<td id="+res.data.list[i].deviceId+" style=\"width:4%;\"> <input type= \"checkbox\" name='clk' class=\"checkbox_in  checkbox_i\"> </td>\n" +
                        "<td id='r_nm' class='r_nm' title="+res.data.list[i].deviceCode+">" +
                        "<div class='r_d'>"+res.data.list[i].deviceCode+"</div>" +
                        "</td>\n" +
                        "<td style=\"width:10%;\" class='r_name' id='r_namem'>"+res.data.list[i].deviceName+"</td>\n" +
                        "<td class='grod' id="+res.data.list[i].groupId+">"+res.data.list[i].groupName+"</td>\n" +
                        "<td class='type' title="+res.data.list[i].deviceTypeName+">"+
                        "<div class='r_d' id="+res.data.list[i].deviceType+">"+res.data.list[i].deviceTypeName+"</div>" +
                        "</td>\n" +
                        "<td>"+photocellState+" </td>\n" +
                        "<td>"+batteryState+"</td>\n" +
                        "<td>"+loadState+"</td>\n" +
                        "<td>"+signalState+"</td>\n" +
                        "<td>"+ runState+"</td>\n" +
                        "<td>"+light+"</td>\n" +
                        "<td>"+communicationType+"</td>\n" +
                        "<td id="+res.data.list[i].deviceId+" projectId="+res.data.list[i].projectId+">" +
                        "<a href=\"#\" class='particulars_a' ><img src='/statics/image/r_kongzhi.svg' alt='' class='r_erkongzhi'></a>\n" +
                        "<a href=\"#\" class='particulars' id="+res.data.list[i].deviceId+"><img src='/statics/image/bianji.png' alt=''></a>\n" +
                        "<a href=\"#\" class='ma_p' id="+res.data.list[i].longitude+","+res.data.list[i].latitude+"><img src='/statics/image/ditu.svg' alt=''style='width: 25px;height:25px;'></a>\n" +
                        "<a class='deleteq'><img src='/statics/image/shanchu.png' alt=''></a>\n" +
                        "</td>\n" +
                        "</tr>"
                }
                $("#div").empty().append(html);
                if($color == 2){
                    $("#add_equipments,.particulars_a,.particulars,.deleteq").hide();
                }
                // 地图定位
                $(".ma_p").unbind('click');
                $(".ma_p").click(function(){
                    var longitude=$(this).attr("id");
                    var name=$(this).parent().siblings(".r_name").html()
                    var searchUrl=encodeURI('../equipment/equipment.html?longitude='+longitude+"&name="+name)
                    location.href =searchUrl;
                })

                //控制面板
                $(".particulars_a").unbind('click');
                $(".particulars_a").click(function(){
                    var deviceCode=$(this).parent().siblings(".r_nm").children(".r_d").html();
                    var grod=$(this).parent().siblings(".grod").attr('id');
                    var type=$(this).parent().siblings(".type").children(".r_d").attr("id");
                    var name=$(this).parent().siblings(".r_name").html();
                    var deviceId=$(this).parent().attr("id");
                    var projectId=$(this).parent().attr("projectId");

                    var searchUrl=encodeURI('../control/control.html?deviceCode='+deviceCode+"&grod="+grod+"&type="+type+"&name="+name+"&deviceId="+deviceId+"&projectId="+projectId)
                    location.href =searchUrl;
                })

                //移动分组删除
                var arr=[]
                //全选
                $('#checkbox[name="all"]').unbind('click');
                $('#checkbox[name="all"]').click(function(){
                    arr=[]
                    if($(this).is(':checked')){
                        $('input[name="clk"]').each(function(){
                            $(this).prop("checked",true);
                            var devId=$(this).parent().attr('id');
                            arr.push(devId)
                            var len=arr.length;
                            $("#mo_sp").html(len+"项")
                            $(".move_a").show();
                        });
                    }else{
                        $('input[name="clk"]').each(function(){
                            $(this).prop("checked",false);
                            var devId=$(this).parent().attr('id');
                            arr=[]
                            $("#mo_sp").html(""+"项");
                            $(".move_a").hide();
                        });
                    }
                });
                //单选
                $('.checkbox_i[name="clk"]').click(function () {
                    var che_c=$(this).prop('checked');
                    if(che_c == true){
                        $("#checkbox[name=all]:checkbox").prop('checked', true);
                        var devId=$(this).parent().attr('id');
                            arr.push(devId)
                        var len=arr.length;
                        $("#mo_sp").html(len+"项");
                        $(".move_a").show();
                    }
                    else if(che_c == false){
                        if($(".checkbox_i").prop('checked') == true){
                            $("#checkbox[name=all]:checkbox").prop('checked', true);
                            $(".move_a").show();

                        }else{
                            $("#checkbox[name=all]:checkbox").prop('checked', false);
                            $(".move_a").hide();
                        }
                        var devId=$(this).parent().attr('id');
                        var index = arr.indexOf(devId);
                            arr.splice(index, 1);
                        var len=arr.length;
                        $("#mo_sp").html(len+"项")
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
                    dele(ids);
                })
                //删除彈窗/////////////////////
                $(".shade_a_delete,.sha_cancel_delete,.guan_sha").unbind('click');
                $(".shade_a_delete,.sha_cancel_delete,.guan_sha").click(function () {
                    $(".shade_delete,.shade_b_delete").css("display", "none")
                })
                //   删除
                var Dele_id
                $(".deleteq").unbind('click');
                $(".deleteq").click(function(){
                    $(".shade_delete,.shade_b_delete").css("display", "block");
                    Dele_id=$(this).parent().attr('id');
                })
                $(".sha_que_delete").unbind('click');
                $(".sha_que_delete").click(function(){
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
                                layer.open({
                                    title: '信息',
                                    content: '删除成功',
                                    skin: 'demo-class'
                                });
                                form(10,1,"","","","");
                                $(".shade_delete,.shade_b_delete").css("display", "none");
                            }else{
                                layer.open({
                                    title: '信息',
                                    content: '删除失败',
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
                $(".sousuo_i").unbind('click');
                $(".sousuo_i").click(function(){
                     var Input_v=$(".sousuo_input").val();
                      $("#Dan_x").html("");
                      pro_gro(Input_v);
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
                                    gr_Id=$(this).parent().attr('id');
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
                                window.location.reload();
                            }
                        }
                    })
                })

                //编辑
                var proid
                var diCode
                $(".particulars").unbind('click');
                $(".particulars").click(function(){
                    $(".shade_modifier,.shade_b_modifier").css("display","block");
                     proid=$(this).parent().attr('id');
                    var  r_namem = $(this).parent().siblings("#r_namem").html();
                    $(".pro_s_b").val(r_namem);
                    var r_rrena = $(this).parent().siblings(".grod").html();
                    diCode=$(this).parent().siblings(".r_nm").attr("title")
                    $("#select1_b").val(r_rrena);
                    // fen()
                })
                $("#confirm_x").unbind('click');
                $("#confirm_x").click(function(){
                    var pro_s_b= $(".pro_s_b").val();
                    var select_b=$("#select1_b option:selected").attr("id");
                    //正则  丸子
                    if(pro_s_b == ""||select_b == ""){
                        $(".mistake").css("display","block");
                    }else{
                        $.ajax({
                            url:baseURL + 'fun/device/updateDevice',
                            contentType: "application/json;charset=UTF-8",
                            type:"POST",
                            data:JSON.stringify({
                                "projectId":Id,
                                "groupId":select_b,
                                "deviceName":pro_s_b,
                                "deviceId":proid,
                                "deviceCode":diCode
                            }),
                            success: function(res){
                                if(res.code == "200"){
                                    $(".shade_modifier").hide();
                                    $(".mistake").hide();
                                    layer.open({
                                        title: '信息',
                                        content: '修改成功',
                                        skin: 'demo-class'
                                    });
                                    form(10,1,"","","","");
                                    $(".shade_modifier,.shade_b_modifier").css("display","none");
                                }else {
                                    layer.open({
                                        title: '信息',
                                        content: res.msg,
                                        skin: 'demo-class'
                                    });
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
                        $("#div").html("");
                        form(pageSize, pagesb);
                    }
                });

            }
        })
    }


     //    添加
    $("#add_equipments").unbind('click');
    $("#add_equipments").click(function(){
        $("#select1").empty()
        $("#select1").val("");
        $(".shade_project,.shade_b_project").css("display","block");
        $(".pro_name").val("");
        $(".pro_s").val("");
       // $("#select1").append("<option class='option opti_a' ></option>");
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
                $("#select1").append(html);
            }

        })
        //添加设备-分组id
        // fen()
    })
    // function fen(){
    //     $.ajax({
    //         url:baseURL + 'fun/group/queryGroupNoPage?projectId='+Id,
    //         contentType: "application/json;charset=UTF-8",
    //         type:"get",
    //         data:{},
    //         success: function(res) {
    //             var html=""
    //             for (var i = 0; i < res.data.length; i++) {
    //                 html += "<option class='option opti_a' id="+res.data[i].groupId+">"+res.data[i].groupName+"</option>\n"
    //             }
    //             $("#select1,#select1_b").append(html)
    //         }
    //     })
    // }
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none");
        $("#select1").val("");

    })
    $(".glyphicon,.rqubtn").click(function(){
        $(".mistake").css("display","none");
    })

    //确认添加
    $("#project_confirm").unbind('click');
    $("#project_confirm").click(function(){
        var pro_name= $(".pro_name").val();
        var pro_s= $(".pro_s").val();
        var select_op=$("#select1 option:selected").attr("id");
        if(pro_s =="" || pro_name==""||select_op==""){
            $(".mistake").css("display","block");
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
                        $("#select1").val("");
                        $(".shade_project").hide();
                        $(".mistake").hide();
                        layer.open({
                            title: '信息',
                            content: '添加成功',
                            skin: 'demo-class'
                        });
                        form(10, 1, "", "", "", "")
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
    //确定删除
    $(".rqubtn,.shade_a_delete").click(function () {
        $(".shade_delete,.shade_b_delete").css("display", "none");
    })
//编辑去弹窗/////////////////////////////////
    $(".shade_modifier_project").click(function(){
        $(".shade_modifier,.shade_b_modifier").css("display","none");
    })

//分组管理
    $("#grouping").unbind('click');
    $("#grouping").click(function(){
        var proid=Id
        var searchUrl=encodeURI('../management/management.html?projectId='+proid+"&character="+character);
        location.href =searchUrl;
    })
//移动分组弹窗
    $(".shade_modifier_yi").click(function(){
        $(".shade_Yi,.shade_b_yi").css("display","none");
    })
//天气
    //获取城市ajax
    $.ajax({
        url: 'http://api.map.baidu.com/location/ip?ak=H7Apgj0jSA8WFUOyKZyL20MgZmjfHz7V',
        type: 'POST',
        dataType: 'jsonp',
        success:function(data) {
            var Cheng_s=JSON.stringify(data.content.address_detail.province);
            var cut_a=Cheng_s.substring(1);
            var cut_b=cut_a.substring(0,cut_a.length-2);
            $('#weather').html(cut_b);
            console.log("城市");
            console.log(data);
            //天气
            $.ajax({
                url:'http://wthrcdn.etouch.cn/weather_mini?city='+cut_b,
                data:"",
                dataType:"jsonp",
                success:function(data){
                    var str=data.data.forecast[0].high
                    var str_a=str.slice(-3)
                    var str_b=data.data.forecast[0].low
                    var str_bb=str_b.slice(-3)
                    var T_an=data.data.forecast[0].type+" "+str_bb+"-"+str_a;
                    var myList = data.data.forecast[0].type;
                       var newList=myList.split("");
                        for( i=0;i<newList.length;i++){
                            if(newList[i]=="雨"&&newList[i]=="雪"){
                                $("#img").attr("src","/statics/image/yujiaxue.svg");
                            }else if(newList[i]=="云"){
                                $("#img").attr("src","/statics/image/duoyun.svg");
                            }else if(newList[i]=="雨"){
                                $("#img").attr("src","/statics/image/xiaoyu.svg");
                            }else if(newList[i]== "雪"){
                                $("#img").attr("src","/statics/image/xue.svg");
                            }else if(newList[i]== "阴"){
                                $("#img").attr("src","/statics/image/yintian.svg");
                            }else if(newList[i]== "晴"){
                                $("#img").attr("src","/statics/image/qing.svg");
                            }else if(newList[i]== "冰"){
                                $("#img").attr("src","/statics/image/rbingbao.svg");
                            }
                        }


                    // if(data.data.forecast[0].type== "多云"){
                    //     // console.log(data.data.forecast[0].type)
                    //     $("#img").attr("src","/statics/image/duoyun.svg");
                    // }else if(data.data.forecast[0].type== "晴"){
                    //     $("#img").attr("src","/statics/image/qing.svg");
                    // }else if(data.data.forecast[0].type== "雨"){
                    //     $("#img").attr("src","/statics/image/yu.svg");
                    // }else if(data.data.forecast[0].type== "小雨"){
                    //     $("#img").attr("src","/statics/image/xiaoyu.svg");
                    // }else if(data.data.forecast[0].type== "阵雨"){
                    //     $("#img").attr("src","/statics/image/zhenyu.svg");
                    // }else if(data.data.forecast[0].type== "雪"){
                    //     $("#img").attr("src","/statics/image/xue.svg");
                    // }else if(data.data.forecast[0].type== "阴"){
                    //     $("#img").attr("src","/statics/image/yintian.svg");
                    // }else if(data.data.forecast[0].type== "雨夹雪"){
                    //     $("#img").attr("src","/statics/image/yujiaxue.svg");
                    // }else if(data.data.forecast[0].type== "雷阵雨"){
                    //     $("#img").attr("src","/statics/image/zhenyu.svg");
                    // }
                    $("#T_an").html(T_an);
                }
            })
        }
    });

})