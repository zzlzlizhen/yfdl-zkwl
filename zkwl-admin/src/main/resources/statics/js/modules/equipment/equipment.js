$(function () {
//    抽屉
    $("#drawer_img").click(function(){
        if($("#drawer").attr("class") == "drawer"){
            $("#drawer").addClass("drawer_hid")
        }else{
            $("#drawer").removeClass("drawer_hid")
        }
    })
//总览
    $("#pandect").click(function () {
        window.open("../pandect/pandect.html")
    })
//控制面板
    $("#hear_control").click(function(){
        location.href ='../control/control.html';
    })
//    单选
    var ida
    $(".ra_div").click(function(){
        $(this).children("p").removeClass("clicka").addClass("ida")
        $(this).siblings().children("p").addClass("clicka").removeClass("ida")
        ida=$(this).children("p").attr('id');
        //调用地图
        map(ida)

    })


    map(0)

    function map(deviceStatus) {
        console.log("类型"+deviceStatus)
        //全部/经纬度
        var arra
        var arr=[]
        $.ajax({
            url:baseURL + 'fun/project/queryProjectNoPage?deviceStatus='+deviceStatus,
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data:{},
            success: function(res){
                console.log("地图数据")
                console.log(res)
                var html=""
                for(var i=0; i< res.data.length; i++){
                    html+=
                        "<li class='pro_li' id="+res.data[i].projectId+">\n" +
                        "<div class=\"nav_pro_v\" >\n" +
                        "<div class=\"nav_project\">"+res.data[i].projectName+"</div>\n" +
                        "<p class=\"nav_pro_p\">运行状态：正常</p>\n" +
                        "<span  class=\"nav_pro_s\"><img src='${request.contextPath}/statics/image/jszg.svg' class=\"drawer_img_a\"/></span>\n" +
                        "</div>\n" +
                        "<ul class=\"nav_gro\" id="+res.data[i].projectId+"1"+">\n" +
                        "</ul>"
                        "</li>\n"

                    var locon={y:res.data[i].longitude,x:res.data[i].latitude,title:"L",con:res.data[i].sumCount+"台设备",branch:res.data[i].projectName}
                    arr.push(locon) //push经纬度
                }

                $("#nav_a").append(html);
                arra=arr //经纬度获取全局变量

                console.log(deviceStatus+"项目变化定位")
                console.log(arra)
                m_p(deviceStatus,arra)

                $(".nav_pro_v").click(function(){
                    console.log($(this).parent().attr('id')+"项目id")
                    var pro_je=$(this).parent().attr('id');
                    console.log()
                    if($("#"+pro_je+"1").html() != ""){
                         $("#"+pro_je+"1").html("")
                        return
                    }else {
                        $.ajax({
                            url: baseURL + 'fun/group/queryGroupIdNoPage?projectId=' + pro_je,
                            contentType: "application/json;charset=UTF-8",
                            type: "get",
                            data: {},
                            success: function (res) {
                                console.log("分组")
                                console.log(res)
                                arr=[]
                                var htmla=""
                                for (var i = 0; i < res.data.length; i++) {
                                    htmla += "<li class='pro_li_a' id="+res.data[i].groupId+">\n" +
                                        "<div class=\"nav_pro_v_b\" >\n" +
                                        "<div class=\"nav_project nav_pro_fen\">"+ res.data[i].groupName+"分组</div>\n" +
                                        "<p class=\"nav_pro_p\">设备数："+ res.data[i].deviceCount+"</p>\n" +
                                        "</div>\n" +
                                        "<ul class=\"nav_gro_b\" id="+res.data[i].groupId+"2"+">\n" +
                                        "</ul>"
                                        "</li>\n"
                                }
                                $("#" + pro_je + "1").append(htmla)

                            //    分组下设备
                                $(".nav_pro_v_b").click(function(){
                                    console.log($(this).parent().attr('id')+"分组id")
                                    var groupId=$(this).parent().attr('id');
                                    if( $("#" + groupId + "2").html() != ""){
                                        $("#" + groupId + "2").html("")
                                        return
                                    }else{
                                        $.ajax({
                                            url: baseURL + 'fun/device/getDeviceByGroupIdNoPage?groupId=' + groupId,
                                            contentType: "application/json;charset=UTF-8",
                                            type: "get",
                                            data: {},
                                            success: function (res) {
                                                console.log("设备")
                                                console.log(res)
                                                arr=[]
                                                arra=[]
                                                var htmlb=""
                                                for (var i = 0; i < res.data.length; i++) {
                                                    htmlb += "<li class='pro_li_a' id="+res.data[i].deviceId+">\n" +
                                                        "<div class=\"nav_pro_v_b\" id="+res.data[i].longitude+">\n" +
                                                        "<div class=\"nav_project nav_pro_she\">设备名称："+res.data[i].deviceName+"</div>\n" +
                                                        "<p class=\"nav_pro_p\" id="+res.data[i].latitude+">运行状态："+ res.data[i].runState+"</p>\n" +
                                                        "<div class=\"swa\" > \n" +
                                                        "<div class=\"switcha\" > \n" +
                                                        "<div class='btn_fath btn_fatha clearfix  toogle on' > \n" +
                                                        "<div class=\"move\"  data-state='1'></div> \n" +
                                                        "<div class=\"btnSwitch btn1\">ON</div> \n" +
                                                        "<div class=\"btnSwitch btn2 \">OFF</div> \n" +
                                                        "</div> " +
                                                        "</div>\n" +
                                                        "</div>\n" +
                                                        "</li>\n"
                                                    var locon={y:res.data[i].longitude,x:res.data[i].latitude,title:"L",con:res.data[i].createTime,branch:res.data[i].deviceName}
                                                    arr.push(locon) //push经纬度
                                                }
                                                $("#" + groupId + "2").append(htmlb);
                                                arra=arr //经纬度获取全局变量
                                                console.log(deviceStatus+"设备变化定位")
                                                console.log(arra)
                                                m_p(deviceStatus,arra)
                                                // 滑动按钮
                                                $(".toogle").click(function () {
                                                    var ele = $(this).children(".move");
                                                    if (ele.attr("data-state") == "1") {
                                                        ele.animate({left: "0"}, 300, function () {
                                                            ele.attr("data-state", "0");
                                                        });
                                                        $(this).removeClass("on").addClass("off");
                                                        return
                                                    } else if (ele.attr("data-state") == "0") {
                                                        ele.animate({left: '50%'}, 300, function () {
                                                            ele.attr("data-state", "1");
                                                        });
                                                        $(this).removeClass("off").addClass("on");
                                                    }
                                                })
                                                //单台设备
                                                var par_id
                                                $(".nav_pro_v_b").click(function () {
                                                    arr=[]
                                                    arra=[]
                                                    console.log(deviceStatus+"单台设备")
                                                    console.log(arra)
                                                    par_id=$(this).parent(".pro_li_a").attr('id')

                                                    var locon={y:$(this).attr("id"),x:$(this).children(".nav_pro_p").attr("id"),branch:$(this).children(".nav_pro_she").html()}
                                                    arr.push(locon)
                                                    arra=arr
                                                    m_p(deviceStatus,arra)
                                                    fu(par_id,1)
                                                })

                                            }
                                        });
                                    }

                                })
                            //    分组下设备结束
                            }
                        });
                    }
                })
            //    项目下分组
            }
        });


        function m_p(deviceStatus,arra) {
            //百度地图
            var map = new BMap.Map("allmap");
            var geolocation = new BMap.Geolocation();
            geolocation.getCurrentPosition(function(r) {
                var point = new BMap.Point(r.point.lng, r.point.lat);
                map.centerAndZoom(point,9);
                map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
                map.addControl(new BMap.NavigationControl({enableGeolocation:true}));
                map.addControl(new BMap.OverviewMapControl());

                console.log("pppp")
                console.log(deviceStatus)

                var xy //地图数组
                var color  //背景颜色

                var marker
                var a=new BMap.Icon("../../../image/a.svg", new BMap.Size(300,157))
                var b=new BMap.Icon("../../../image/b.svg", new BMap.Size(300,157))
                var c=new BMap.Icon("../../../image/c.svg", new BMap.Size(300,157))
                var d=new BMap.Icon("../../../image/d.svg", new BMap.Size(300,157))
                var e=new BMap.Icon("../../../image/e.svg", new BMap.Size(300,157))
                if(deviceStatus == "0"){
                    //全部
                    xy=arra;
                    marker = new BMap.Marker(point,{icon :a});
                    color='#4783E7';
                }else if(deviceStatus == "1"){
                    //正常
                    xy=arra
                    marker = new BMap.Marker(point,{icon :b});
                    color='#00FF7F';
                }else if(deviceStatus == "2"){
                    //报警
                    xy=arra
                    marker = new BMap.Marker(point,{icon :c});
                    color='#FFD700';
                }else if(deviceStatus == "3"){
                    //故障
                    xy=arra
                    marker = new BMap.Marker(point,{icon :d});
                    color='#FF0000';
                }else if(deviceStatus == "4"){
                    //离线
                    xy=arra
                    marker = new BMap.Marker(point,{icon :e});
                    color='#696969';
                }

                console.log(xy)
                var mapPoints =xy;//经纬度定位
                var i = 0;
                map.addOverlay(marker);
                map.enableScrollWheelZoom(true);//开启鼠标滚动
                // 函数 创建多个标注
                function markerFun (points,label,infoWindows) {
                    var markers = new BMap.Marker(points);
                    map.addOverlay(markers);
                    markers.setLabel(label);
                    markers.addEventListener("click",function (event) {
                        map.openInfoWindow(infoWindows,points);//参数：窗口、点  根据点击的点出现对应的窗口
                    });
                }
                for (;i<mapPoints.length;i++) {
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


    }

    function fu(par_id,log){
        console.log("故障日志"+par_id)
        //故障日志
        $("#malfunction").click(function(){
            $(".shade_project,.shade_b_project").css("display","block");
            $.ajax({
                url:baseURL + 'fun/faultlog/queryFaultlog?deviceId='+par_id+"&status="+log,
                contentType: "application/json;charset=UTF-8",
                type:"get",
                data:{},
                success: function(res){
                    console.log("日志")
                    console.log(res)
                }
            });
        })
    }

    //关闭故障日志
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none")
    })
});
