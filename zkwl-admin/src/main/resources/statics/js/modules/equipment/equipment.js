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
                //左侧边栏
                $(".She,.She_a").hide();
                $(".grouping").show();

                $(".nav_pro_v").attr("t","0");

                $(".nav_pro_v").click(function(){
                    $(".D_xuan").hide();
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
                                        "<div class=\"nav_pro_v_b\" id="+res.data[i].groupName+" t=\"0\">\n" +
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
                                if(sessionStorage.getItem('l')=='1'){
                                    console.log(   $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").length)
                                    $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").eq(lindexs).find('.nav_pro_v_b').click()
                                }
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
                $(".nav_pro_v_b").attr('t','0')
               $(document).delegate(".nav_pro_v_b","click",function () {
                    //    单选
                    console.log($(this).attr('t'))
                    var pro_je=$(this).parents(".pro_li").attr("id");
                    var groupId = $(this).parent().attr('id');
                    var gr_d = groupId;
                    deviceCode = "";
                    groId = gr_d;
                    na_me = $(this).attr("id");
                    if($(this).siblings("nav_gro_b").html() !=""){
                        $(".D_xuan").show();
                    }

                    if ($(this).attr('t')=='0') {
                        $(this).attr('t','1')
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
                                            "<div class=\"nav_pro_v_ba nav_bb\" title="+ res.data[i].deviceCode +" id=" + res.data[i].longitude + ">\n" +
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
                                    if(sessionStorage.getItem('l')=='1'){
                                        console.log( $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").length)
                                        $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").eq(lindexs).find('li').eq(xindex).find(".nav_bb").click()
                                    }
                                }
                            });
                        }
                    } else {
                        $("#" + groupId + "2").html("");
                        $(this).attr('t','0')
                        f_en(pro_je, groupId);
                        return
                    }

                })
                //单台设备
                var par_id
                var event=event||window.event
                $(document).delegate(".nav_bb","click",function () {
                    //event.stopPropagation()
                    $(".D_xuan").hide();
                    var pro_je=$(this).parents(".pro_li").attr("id");
                    var groupId = $(this).parent().attr('id');
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
                var lindex=localStorage.getItem('key')
                var lindexs=localStorage.getItem('keys')
                var xindex=localStorage.getItem('key2')
                if(sessionStorage.getItem('l')=='1'){
                    $("#drawer_img").click()
                    $(".nav_a").find("li").eq(lindex).find(".nav_pro_v").click()
                    console.log(   $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").length)
                    $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").eq(lindexs).find('.nav_pro_v_b').click()

                }
                //    项目下分组
            }
        });
    }

    //高德地图方法
    var center=[108.06345,34.913385]
    var map = new AMap.Map("allmap",{
        resizeEnable: true,
        rotateEnable:true,
        pitchEnable:true,
        zoom:4.6,
        pitch:10,
        rotation:-15,
        viewMode:'3D',//开启3D视图,默认为关闭
        expandZoomRange:true,
        center:center
    });
    //3D地图图表
    map.addControl(new AMap.ControlBar({
        showZoomBar:false,
        showControlButton:true,
        position:{
            left:'-65px',
            top:'10px'
        }
    }))
    //实时路况图层
    var trafficLayer = new AMap.TileLayer.Traffic({
        zIndex: 10
    });
    map.add(trafficLayer);//添加图层到地图
    var infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30)});

    var removeList = [];	//移除标记点
    function m_p(deviceStatus,arra) {
        var marker
        if(removeList.length != 0){
            map.remove(removeList);
            removeList = [];
        }
        for(var i = 0, marker; i < arra.length; i ++){
             marker = new AMap.Marker({
                map: map,
                position:[arra[i].y, arra[i].x],
                offset: new AMap.Pixel(-10, -10),
                icon: new AMap.Icon({
                    image: "/statics/image/a.svg",
                    size: new AMap.Size(64, 64),  //图标大小
                    imageSize: new AMap.Size(36,36)
                }),
                title: '北京',
                zoom: 20, // 设置是否可以拖拽
                draggable: true,
                cursor: 'move', // 设置拖拽效果
                raiseOnDrag: true,
            });
            //提示框
            marker.content =arra[i].branch+"("+arra[i].con+")";
            marker.emit('click',{target: marker});
            marker.on('dblclick',showInfoDbClick)//双击
            marker.on('click',showInfoClick);//单击
            removeList.push(marker);
        }
    }

    //单击
    function showInfoClick(e){
        var text = '您在 [ '+e.lnglat.getLng()+','+e.lnglat.getLat()+' ] 的位置单击了地图！'
        infoWindow.setContent(e.target.content);
        infoWindow.open(map, e.target.getPosition());
        var zoom = Math.floor(Math.random() * 7) + 10;
        var lng = e.lnglat.getLng() + Math.floor(Math.random() * 589828) / 1e6;
        var lat = e.lnglat.getLat() + Math.floor(Math.random() * 514923) / 1e6;
        map.setZoomAndCenter(zoom, [lng, lat]); //同时设置地图层级与中心点
    }
    //双击
    function showInfoDbClick(e){
        var text = '您在 [ '+e.lnglat.getLng()+','+e.lnglat.getLat()+' ] 的位置双击了地图！'
    }
    //监听热点点击
    var hotSpotMarker;
    map.on("hotspotclick",function(e){
        if(hotSpotMarker){
            hotSpotMarker.setMap(null);
        }
        hotSpotMarker = new AMap.Marker({
            position:e.lnglat,
            map:map,
            content:'<div>'+ e.name +'</div>'
        });
    });
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
            $("#pro_list").html("");
            $("#pro_list").append("");
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
                    $("#pro_list").append(html);
                }
            });
        }

    }
    //关闭日志弹窗
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none")
    })
    $("#hear_control").click(function(){
        localStorage.setItem("key",$(".nav_pro_v[t='1']").parent().index());
        localStorage.setItem("keys",$(".nav_pro_v_b[t='1']").parent('.pro_li_a').index());
        localStorage.setItem("key2",$(".nav_bb_color").parent('.pro_li_a').index());
        console.log("控制面板");
        console.log($(".nav_pro_v[t='1']").parent().index());
        console.log($(".nav_pro_v_b[t='1']").parent('.pro_li_a').index());
        console.log($(".nav_bb_color").parent('.pro_li_a').index());
        var searchUrl=encodeURI('../control/control.html?deviceCode='+deviceCode+"&grod="+groId+"&type="+type+"&name="+na_me)
        location.href =searchUrl;
    })
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
