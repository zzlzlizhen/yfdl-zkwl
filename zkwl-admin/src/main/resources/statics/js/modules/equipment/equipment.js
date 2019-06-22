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
//故障日志
    $("#malfunction").click(function () {
        console.log("111111111111111111")
        $(".shade_project,.shade_b_project").css("display","block");
        $.ajax({
            url:baseURL + 'fun/faultlog/queryFaultlog',
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data:{},
            success: function(res){
                console.log("日志")
                console.log(res)
            }
        });
    })
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none")
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

        $.ajax({
            url:baseURL + 'fun/project/queryProjectNoPage?deviceStatus='+deviceStatus,
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data:{},
            success: function(res){
                console.log("地图数据")
                console.log(res)
            }
        });

        //全部/经纬度
        var arra=[
            {'x':116.391621,'y':39.904973},
            {'x':116.386446,'y':39.929322},
            {'x':118.457882,'y':32.023232},
            {'x':118.62882,'y':32.039839},
            {'x':118.3882,'y':32.034343},
            {'x':118.6666,'y':32.019839},
            {'x':118.577882,'y':32.051839},
            {'x':118.377882,'y':32.052839},
            {'x':118.277882,'y':32.053839},
            {'x':118.177882,'y':32.057632},
            {'x':118.077882,'y':31.055839},
            {'x':118.795394,'y':32.027002},
            {'x':118.85952,'y':32.0781},
            {'x':118.651976,'y':32.047353},
            {'x':118.735051,'y':32.059839},
            {'x':118.777882,'y':32.054019},
            {'x':118.677882,'y':32.059839},
        ]
        //正常/经纬度
        var arrb= [
            {'x':118.777882,'y':32.059839},
            {'x':118.777882,'y':32.059839},
            {'x':118.457882,'y':32.023232},
            {'x':118.62882,'y':32.039839},
            {'x':118.3882,'y':32.034343},
        ];
        //报警/经纬度
        var arrc= [
            {'x':118.6666,'y':32.019839},
            {'x':118.577882,'y':32.051839},
            {'x':118.377882,'y':32.052839},
            {'x':118.277882,'y':32.053839},
            {'x':118.177882,'y':32.057632},
        ];
        //故障/经纬度
        var arrd= [
            {'x':118.077882,'y':31.055839},
            {'x':118.795394,'y':32.027002}
        ];
        //离线/经纬度
        var arre= [
            {'x':118.85952,'y':32.0781},
            {'x':118.651976,'y':32.047353},
            {'x':118.735051,'y':32.059839},
            {'x':118.777882,'y':32.054019},
            {'x':118.677882,'y':32.059839},
        ];

        // 百度地图API功能
        var map = new BMap.Map("allmap");    // 创建Map实例
        var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function(r){
            console.log('您的位置：'+r.point.lng+','+r.point.lat);
            map.centerAndZoom(new BMap.Point(r.point.lng,r.point.lat),9);
            //map.centerAndZoom(new BMap.Point(118.777882,32.059839), 12);  // 初始化地图,设置中心点坐标和地图级别
            // map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
            map.addControl(new BMap.NavigationControl({enableGeolocation:true}));
            map.addControl(new BMap.OverviewMapControl());
            map.setCurrentCity("北京市");          // 设置地图显示的城市 此项是必须设置的
            map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

            console.log("pppp")
            console.log(deviceStatus)

            var xy
            var xy1
            var xy2
            var xy3
            var xy4
            if(deviceStatus == "0"){
                xy=arra
            }else if(deviceStatus == "1"){
                xy1=arrb
            }else if(deviceStatus == "2"){
                xy2=arrc
            }else if(deviceStatus == "3"){
                xy3=arrd
            }else if(deviceStatus == "4"){
                xy4=arre
            }
            //================================================
            //全部
            var markers = [];
            var pt = null;
            for (var i in xy) {
                pt = new BMap.Point(xy[i].x , xy[i].y);
                markers.push(new BMap.Marker(pt));
            }
            //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
            var markerClusterer = new BMapLib.MarkerClusterer(map,
                {
                    markers:markers,
                    girdSize : 100,
                    styles : [{
                        url:'${request.contextPath}/statics/image/yuana.svg',
                        size: new BMap.Size(92, 92),
                        backgroundColor : '#4783E7'
                    }],
                });
            markerClusterer.setMaxZoom(13);
            markerClusterer.setGridSize(100);

            //正常===================

            var markers1 = [];
            var pt = null;
            for (var i in xy1) {
                pt = new BMap.Point(xy1[i].x , xy1[i].y);
                markers1.push(new BMap.Marker(pt));
            }
            //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
            var markerClusterer1 = new BMapLib.MarkerClusterer(map,
                {
                    markers:markers1,
                    girdSize : 100,
                    styles : [{
                        url:'${request.contextPath}/statics/image/yuanb.svg',
                        size: new BMap.Size(92, 92),
                        backgroundColor : '#00FF7F'
                    }],
                });
            markerClusterer1.setMaxZoom(13);
            markerClusterer1.setGridSize(100);
            console.log(markerClusterer1);
            console.log(markerClusterer);

            //报警===================

            var markers2 = [];
            var pt = null;
            for (var i in xy2) {
                pt = new BMap.Point(xy2[i].x , xy2[i].y);
                markers2.push(new BMap.Marker(pt));
            }
            //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
            var markerClusterer2 = new BMapLib.MarkerClusterer(map,
                {
                    markers:markers2,
                    girdSize : 100,
                    styles : [{
                        url:'${request.contextPath}/statics/image/yuanc.svg',
                        size: new BMap.Size(92, 92),
                        backgroundColor : '#FFD700'
                    }],
                });
            markerClusterer2.setMaxZoom(13);
            markerClusterer2.setGridSize(100);
            //故障===================

            var markers3 = [];
            var pt = null;
            for (var i in xy3) {
                pt = new BMap.Point(xy3[i].x , xy3[i].y);
                markers3.push(new BMap.Marker(pt));
            }
            //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
            var markerClusterer3 = new BMapLib.MarkerClusterer(map,
                {
                    markers:markers3,
                    girdSize : 100,
                    styles : [{
                        url:'${request.contextPath}/statics/image/yuand.svg',
                        size: new BMap.Size(92, 92),
                        backgroundColor : '#FF0000'
                    }],
                });
            markerClusterer3.setMaxZoom(13);
            markerClusterer3.setGridSize(100);
            //离线===================

            var markers4 = [];
            var pt = null;
            for (var i in xy4) {
                pt = new BMap.Point(xy4[i].x , xy4[i].y);
                markers4.push(new BMap.Marker(pt));
            }
            //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
            var markerClusterer4 = new BMapLib.MarkerClusterer(map,
                {
                    markers:markers4,
                    girdSize : 100,
                    styles : [{
                        url:'${request.contextPath}/statics/image/yuane.svg',
                        size: new BMap.Size(92, 92),
                        backgroundColor : '#696969'
                    }],
                });
            markerClusterer4.setMaxZoom(13);
            markerClusterer4.setGridSize(100);
        });


    }
});
