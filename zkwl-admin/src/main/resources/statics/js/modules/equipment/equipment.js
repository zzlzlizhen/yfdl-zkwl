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
    var longitude=href['longitude'];
    var Name=href['name'];
    var ass;
    if(longitude != undefined){
        var partition=longitude.split(",");
        ass=[{y:partition[0],x:partition[1],branch: Name}];
    }

//    抽屉
    $("#drawer_img,#qu_a").click(function(){
        if($("#drawer").attr("class") == "drawer"){
            $("#drawer").addClass("drawer_hid");
        }else{
            $("#drawer").removeClass("drawer_hid");
        }
    })
//总览
    $("#pandect").click(function () {
        window.open("../pandect/pandect.html");
    })

    var type;
    var deviceCode;
    var groId;
    var na_me;
    var deviceStatus = 0;

    map(ass);
    function map(ass) {
        var ass; //跳转
        //全部/经纬度
        var arra;
        var arr=[];
        $.ajax({
            url:baseURL + 'fun/project/queryProjectNoPage',
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data:{},
            success: function(res){
                console.log("状态");
                console.log(res);
                var html="";
                for(var i=0; i< res.data.length; i++){
                    var projectStatus=res.data[i].projectStatus;
                    if (projectStatus == null ) {
                        projectStatus = "";
                    } else if(projectStatus == 1) {
                        projectStatus = "启用";
                    }else if(projectStatus == 2) {
                        projectStatus = "停用";
                    }
                    html+=
                        "<li class='pro_li' id="+res.data[i].projectId+">\n" +
                        "<div class=\"nav_pro_v\" >\n" +
                        "<div class=\"nav_project\">"+res.data[i].projectName+"</div>\n" +
                        "<p class=\"nav_pro_p\">运行状态："+projectStatus+"</p>\n" +
                        "</div>\n" +
                        "<ul class=\"nav_gro\" id="+res.data[i].projectId+"1"+">\n" +
                        "</ul>"
                    "</li>\n"

                    var locon={y:res.data[i].longitude,x:res.data[i].latitude,title:"L",con:res.data[i].sumCount+"台设备",branch:res.data[i].projectName};
                    arr.push(locon); //push经纬度
                }

                $("#nav_a").append(html);
                if(ass != undefined){
                    arra = ass ; //单个项目
                    ass = undefined;
                }else{
                    arra=arr; //总项目经纬度获取全局变量
                }
                //地图
                m_p(0,arra);
                //右侧边栏
                $(".She,.She_a").hide();
                $(".grouping").show();

                $(".nav_pro_v").attr("t","0");
                $(".nav_pro_v").click(function(){
                    var pro_je=$(this).parents(".pro_li").attr("id");
                    if( $(this).attr("t")=="0"){
                        $(this).parents(".pro_li").siblings().find(".nav_pro_v").attr("t","0");
                        $(this).parents(".pro_li").siblings().find(".nav_gro").hide().empty();
                        $.ajax({
                            url: baseURL + 'fun/group/queryGroupIdNoPage?projectId=' + pro_je,
                            contentType: "application/json;charset=UTF-8",
                            type: "get",
                            data: {},
                            success: function (res) {
                                arr=[];
                                var htmla="";
                                for (var i = 0; i < res.data.length; i++) {
                                    var deviceCount=res.data[i].deviceCount;
                                    if (deviceCount == null ) {
                                        deviceCount = "";
                                    } else  {
                                        deviceCount = res.data[i].deviceCount;
                                    }
                                    htmla += "<li class='pro_li_a' id="+res.data[i].groupId+">\n" +
                                        "<div class=\"nav_pro_v_b\" id="+res.data[i].groupName+">\n" +
                                        "<div class=\"nav_project nav_pro_fen\" >"+ res.data[i].groupName+"分组</div>\n" +
                                        "<p class=\"nav_pro_p\">设备数："+ deviceCount+"</p>\n" +
                                        "</div>\n" +
                                        "<ul class=\"nav_gro_b\" id="+res.data[i].groupId+"2"+">\n" +
                                        "</ul>"
                                    "</li>\n"
                                    var locon={y:res.data[i].longitude,x:res.data[i].latitude,title:"L",con:res.data[i].deviceCount+"台设备",branch:res.data[i].groupName};
                                    arr.push(locon); //push经纬度
                                }
                                $("#" + pro_je + "1").show().append(htmla);
                                arra=arr ;//分组经纬度获取全局变量
                                m_p(0,arra);//地图
                                //右侧边栏数据
                                Bing(pro_je,"");//控制项目
                                // 分组下设备
                                $(".nav_pro_v_b").click(function () {
                                    //    单选
                                    var groupId = $(this).parent().attr('id');
                                    var gr_d = groupId;
                                    deviceCode = "";
                                    groId = gr_d;
                                    na_me = $(this).attr("id");
                                    $(".D_xuan").show();

                                    if ($("#" + groupId + "2").html() == "") {
                                        $(".D_xuan_a").unbind('click');
                                        $(".D_xuan_a").click(function () {
                                            deviceStatus = $(this).attr('id');
                                            if(deviceStatus == "0"){
                                                $(".ra_state_d").css("transform","rotate(0deg)");
                                                $(".ra_state_d").css("left","40px");
                                                $(".ra_state_d").css("top","40px");
                                            }else if(deviceStatus == "1"){
                                                $(".ra_state_d").css("transform","rotate(-70deg)");
                                                $(".ra_state_d").css("top","39px");
                                            }else if(deviceStatus == "2"){
                                                $(".ra_state_d").css("transform","rotate(70deg)");
                                                $(".ra_state_d").css("left","38px");
                                                $(".ra_state_d").css("top","39px");
                                            }else if(deviceStatus == "3"){
                                                $(".ra_state_d").css("transform","rotate(-145deg)");
                                                $(".ra_state_d").css("left","40px");
                                                $(".ra_state_d").css("top","38px");
                                            }else if(deviceStatus == "4"){
                                                $(".ra_state_d").css("transform","rotate(145deg)");
                                                $(".ra_state_d").css("left","39px");
                                                $(".ra_state_d").css("top","38px");
                                            }
                                            //调用地图
                                            $(".nav_gro_b").html("");
                                             zu(deviceStatus)
                                        });
                                        zu(deviceStatus);
                                        function zu(deviceStatus) {
                                            $.ajax({
                                                url: baseURL + 'fun/device/getDeviceByGroupIdNoPage?groupId=' + groupId + "&projectId=" + pro_je + "&status=" + deviceStatus,
                                                contentType: "application/json;charset=UTF-8",
                                                type: "get",
                                                data: {},
                                                success: function (res) {
                                                    console.log("设备1");
                                                    console.log(res);
                                                    arr = [];
                                                    arra = [];
                                                    var htmlb = "";
                                                    var offClass = "";
                                                    for (var i = 0; i < res.data.length; i++) {
                                                        if (res.data[i].onOff == 1) {
                                                            offClass = "btn_fath btn_fatha clearfix  toogle on";
                                                        } else {
                                                            offClass = "btn_fath btn_fatha clearfix  toogle off";
                                                        }
                                                        var runState = res.data[i].runState
                                                        if (runState == null) {
                                                            runState = "";
                                                        } else if (runState == 1) {
                                                            runState = "正常";
                                                        } else if (runState == 2) {
                                                            runState = "报警";
                                                        } else if (runState == 3) {
                                                            runState = "故障";
                                                        } else if (runState == 4) {
                                                            runState = "离线";
                                                        }else if (runState == 5) {
                                                            runState = "升级中";
                                                        }
                                                        htmlb += "<li class='pro_li_a' name=" + res.data[i].deviceName + " type=" + res.data[i].deviceType + " deviceCode=" + res.data[i].deviceCode + " id=" + res.data[i].deviceId + ">\n" +
                                                            "<div class=\"nav_pro_v_b nav_bb\" title="+ res.data[i].deviceCode +" id=" + res.data[i].longitude + ">\n" +
                                                            "<div class=\"nav_project nav_pro_she\"  id=" + res.data[i].groupId + ">设备名称：" + res.data[i].deviceName + "</div>\n" +
                                                            "<p class=\"nav_pro_p\" id=" + res.data[i].latitude + ">运行状态：" + runState + "</p>\n" +
                                                            "</li>\n"
                                                        var locon = {
                                                            y: res.data[i].longitude,
                                                            x: res.data[i].latitude,
                                                            title: "L",
                                                            con: res.data[i].createTime,
                                                            branch: res.data[i].deviceName
                                                        }
                                                        arr.push(locon); //push经纬度
                                                    }
                                                    $("#" + groupId + "2").append(htmlb);
                                                    arra = arr //经纬度获取全局变量
                                                    m_p(deviceStatus, arra); //地图
                                                    f_en(pro_je, groupId);   //控制分组
                                                    fu("", "", groupId);//日志信息

                                                    //单台设备
                                                    var par_id
                                                    $(".nav_bb").click(function () {
                                                        $(".D_xuan").hide();
                                                        $(this).addClass("nav_bb_color");
                                                        $(this).parents().siblings().children(".nav_bb").removeClass("nav_bb_color");
                                                        arr = [];
                                                        arra = [];
                                                        console.log(deviceStatus + "单台设备");
                                                        console.log(arra);
                                                        par_id = $(this).parent(".pro_li_a").attr('id');
                                                        type = $(this).parents().attr('type');
                                                        deviceCode = $(this).parents().attr('deviceCode');
                                                        na_me = $(this).parents().attr('name');
                                                        groId = $(this).children(".nav_pro_she").attr('id');
                                                        var locon = {
                                                            y: $(this).attr("id"),
                                                            x: $(this).children(".nav_pro_p").attr("id"),
                                                            branch: $(this).children(".nav_pro_she").html()
                                                        };
                                                        arr.push(locon);
                                                        arra = arr;

                                                        //单台设备详情右侧
                                                        zhuang(pro_je, groupId, par_id);
                                                        //调用地图
                                                        m_p(deviceStatus, arra);
                                                        //日志信息
                                                        fu(par_id, "", "");
                                                        //历史数据、高级设置

                                                    })
                                                }
                                            });
                                        }
                                    } else {
                                        $("#" + groupId + "2").html("");
                                        f_en(pro_je, groupId);
                                        return
                                    }
                                })


                                //    分组下设备结束
                            }
                        });
                        $(this).attr("t","1");
                    }else{
                        $(this).attr("t","0");
                        $("#" + pro_je + "1").empty().hide();
                        //右侧边栏
                        $(".She,.She_a").hide();
                        $(".grouping").show();
                    }
                    var pro_je=$(this).parent().attr('id');
                    if($("#"+pro_je+"1").html() == ""){
                        $("#"+pro_je+"1").html("");
                        return
                    }else {


                    }
                })
                //    项目下分组
            }
        });

    }

    //地图方法
    function m_p(deviceStatus,arra) {
        //百度地图
        var map = new BMap.Map("allmap");
        var point
        var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function(r) {
            if(arra[0].y !="" && arra[0].y !=null && arra[0].y !="0"){
                var xx=Number(arra[0].x);
                var yy=Number(arra[0].y);
                point=new BMap.Point(yy, xx);
            }else if(arra==[] ){
                point= new BMap.Point(r.point.lng, r.point.lat);
            }else {
                point= new BMap.Point(r.point.lng, r.point.lat);
            }

            map.centerAndZoom(point,16);
            //map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
            map.addControl(new BMap.NavigationControl({enableGeolocation:true}));
            map.addControl(new BMap.OverviewMapControl());

            var xy //地图数组
            var color  //背景颜色

            var marker
            var a=new BMap.Icon("/statics/image/a.svg", new BMap.Size(32,32));
            var b=new BMap.Icon("/statics/image/b.svg", new BMap.Size(32,32));
            var c=new BMap.Icon("/statics/image/c.svg", new BMap.Size(32,32));
            var d=new BMap.Icon("/statics/image/d.svg", new BMap.Size(32,32));
            var e=new BMap.Icon("/statics/image/e.svg", new BMap.Size(32,32));
            if(deviceStatus == "0"){
                xy=arra;//全部
                color='#4783E7';
            }else if(deviceStatus == "1"){
                xy=arra ;//正常
                color='#00FF7F';
            }else if(deviceStatus == "2"){
                xy=arra;//报警
                color='#FFD700';
            }else if(deviceStatus == "3"){
                xy=arra;//故障
                color='#FF0000';
            }else if(deviceStatus == "4"){
                xy=arra;//离线
                color='#696969';
            }
            console.log("是不是")
            console.log(deviceStatus)
            console.log(xy)

            var mapPoints =xy;//经纬度定位
            var i = 0;
            //map.addOverlay(marker);
            map.enableScrollWheelZoom(true);//开启鼠标滚动
            // 函数 创建多个标注
            function markerFun (points,label,infoWindows) {
                var markers = new BMap.Marker(points);
                if(deviceStatus == "0"){
                    markers = new BMap.Marker(points,{icon :a,});
                }else if(deviceStatus == "1"){
                    markers = new BMap.Marker(points,{icon :b,});
                }else if(deviceStatus == "2"){
                    markers = new BMap.Marker(points,{icon :c,});
                }else if(deviceStatus == "3"){
                    markers = new BMap.Marker(points,{icon :d,});
                }else if(deviceStatus == "4"){
                    markers = new BMap.Marker(points,{icon :e,});
                }
                map.addOverlay(markers);
                markers.setLabel(label);
                label.setStyle({
                    display:"none" //给label设置样式，任意的CSS都是可以的
                });
                markers.addEventListener("mouseover", function(){
                    label.setStyle({  //给label设置样式，任意的CSS都是可以的
                        display:"block"
                    });

                });
                markers.addEventListener("mouseout", function(){
                    label.setStyle({  //给label设置样式，任意的CSS都是可以的
                        display:"none"
                    });
                });

                markers.addEventListener("click",function (event) {
                    map.openInfoWindow(infoWindows,points);//参数：窗口、点  根据点击的点出现对应的窗口
                });
            }
            for (var i=0; i<mapPoints.length;i++) {
                var points = new BMap.Point(mapPoints[i].y,mapPoints[i].x);//创建坐标点
                var opts = {
                    width:250,
                    height: 100,
                    title:mapPoints[i].title,
                    backgroundColor :color,
                };
                var label = new BMap.Label(mapPoints[i].branch,{
                    offset:new BMap.Size(25,5)
                });
                var infoWindows = new BMap.InfoWindow(mapPoints[i].con,opts);
                markerFun(points,label,infoWindows);
            }
        })

    }
    //地图结束

    //日志方法
    function fu(par_id,log,groupId){

        //日志
        $("#malfunction").unbind('click');
        $("#malfunction").click(function(){
            $(".shade_project,.shade_b_project").css("display","block");
            $(".Bian_P").html("故障日志")
            r(par_id,1,groupId)
        })
        $("#operation").unbind('click');
        $("#operation").click(function(){
            $(".shade_project,.shade_b_project").css("display","block");
            $(".Bian_P").html("操作日志")
            r(par_id,2,groupId)
        })
        function r(par_id,log,groupId){
            $("#pro_list").html("")
            $("#pro_list").append("")
            $.ajax({
                url:baseURL + 'fun/faultlog/queryFaultlog?deviceId='+par_id+"&status="+log+"&groupId="+groupId,
                contentType: "application/json;charset=UTF-8",
                type:"get",
                data:{},
                success: function(res){
                    var html=""
                    for(var i=0; i< res.data.length; i++){
                        html+="  <div class=\"facility\">\n" +
                            "<span>放电异常</span>\n" +
                            "<p>"+res.data[i].createTime+"</p>\n" +
                            "<div>"+res.data[i].faultLogDesc+"</div>\n" +
                            "</div>"
                    }
                    $("#pro_list").append(html)
                }
            });
        }

    }

    //关闭日志弹窗
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none")
    })
    $("#hear_control").click(function(){
        var searchUrl=encodeURI('../control/control.html?deviceCode='+deviceCode+"&grod="+groId+"&type="+type+"&name="+na_me)
        location.href =searchUrl;
    })
    // // 百度地图API功能
    // var map = new BMap.Map("allmap");    // 创建Map实例
    // var geolocation = new BMap.Geolocation();
    // geolocation.getCurrentPosition(function(r){
    //     console.log('您的位置：'+r.point.lng+','+r.point.lat);
    //     map.centerAndZoom(new BMap.Point(r.point.lng,r.point.lat),9);
    //
    //     //map.centerAndZoom(new BMap.Point(118.777882,32.059839), 12);  // 初始化地图,设置中心点坐标和地图级别
    //      map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
    //     map.addControl(new BMap.NavigationControl({enableGeolocation:true}));
    //     map.addControl(new BMap.OverviewMapControl());
    //     map.setCurrentCity("北京市");          // 设置地图显示的城市 此项是必须设置的
    //     map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    //
    //     console.log("pppp")
    //     console.log(deviceStatus)
    //
    //     var xy //地图数组
    //     var color  //背景颜色
    //     var urla //背景圈
    //     if(deviceStatus == "0"){
    //         //全部
    //         xy=arra;
    //         urla='${request.contextPath}/statics/image/yuana.svg';
    //         color='#4783E7';
    //     }else if(deviceStatus == "1"){
    //         //正常
    //         xy=arrb
    //         urla='${request.contextPath}/statics/image/yuanb.svg';
    //         color='#00FF7F';
    //     }else if(deviceStatus == "2"){
    //         //报警
    //         xy=arrc
    //         urla='${request.contextPath}/statics/image/yuanc.svg';
    //         color='#FFD700';
    //     }else if(deviceStatus == "3"){
    //         //故障
    //         xy=arrd
    //         urla='${request.contextPath}/statics/image/yuand.svg';
    //         color='#FF0000';
    //     }else if(deviceStatus == "4"){
    //         //离线
    //         xy=arre
    //         urla='${request.contextPath}/statics/image/yuane.svg';
    //         color='#696969';
    //     }
    //================================================
    //全部
    // var markers = [];
    // var pt = null;
    // for (var i in xy) {
    //     pt = new BMap.Point(xy[i].x , xy[i].y);
    //     markers.push(new BMap.Marker(pt));
    // }
    // //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
    //
    // var markerClusterer = new BMapLib.MarkerClusterer(map,{
    //     markers:markers,
    //     girdSize : 100,
    //     styles : [{
    //         url:urla,
    //         size: new BMap.Size(92, 92),
    //         backgroundColor :color,
    //     }
    //     ],
    // });
    // markerClusterer.setMaxZoom(13);
    // markerClusterer.setGridSize(100);
    // console.log(markerClusterer);
    // });

//    丸子
    layui.use('slider', function(){
        var $ = layui.$
            ,slider = layui.slider;
        slider.render({
            elem: '#slideTest1'
            ,theme: '#1E9FFF' //主题色
            ,value:0
        });
        slider.render({
            elem: '#slideTest2'
            ,theme: '#1E9FFF' //主题色
        });
        slider.render({
            elem: '#slideTest3'
            ,theme: '#1E9FFF' //主题色
        });

    });
});
