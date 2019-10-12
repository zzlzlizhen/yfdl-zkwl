$(function () {
    $("#in_p input").on("click",function(){
        $(this).css("background","red");
    });
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
    var state=href['state'];
    var con=href['con'];
    var ass;
    if(longitude != undefined){
        var partition=longitude.split(",");
        ass=[{y:partition[0],x:partition[1],branch: Name,state:state,con:con}];
    }
    var $color = localStorage.getItem("mycolor");
    if($color == 2){
        $("#hear_control,#confirm").hide();
        $("#operation").css("margin-left","40%")
    }
//总览
    $("#pandect").click(function () {
        window.open("../pandect/pandect.html");
    })
//抽屉
    $("#drawer_img").click(function(){
        if($("#drawer").attr("class") == "drawer"){
            $("#drawer").addClass("drawer_hid");
            $("#drawer_img").css("transform","rotate(180deg)");
        }else{
            $("#drawer").removeClass("drawer_hid");
            $("#drawer_img").css("transform","rotate(0deg)");
        }
    })
    var type;
    var deviceCode;
    var groId;
    var na_me;
    var deviceId;
    var deviceStatus = 0;
    var devStaus=0;
    var motion
    var motion_a
//搜索
    $("#seek").click(function(){
        var SN=$("#seek_a").val();
        $("#nav_a").html("");
        if(SN !=""){
            unit(deviceStatus,SN);
        }else{
            window.location.reload()
        }
    })
   // 右侧边开关
   $("#toog").click(function(){
        var ele = $(this).children(".move");
        if(ele.attr("data-state") == "on"){
            ele.animate({left: "0"}, 300, function(){
                ele.attr("data-state", "off");
            });
            $(this).removeClass("on").addClass("off");
        }else if(ele.attr("data-state") == "off"){
            ele.animate({left: '35'}, 300, function(){
                $(this).attr("data-state", "on");
            });
            $(this).removeClass("off").addClass("on");
        }
   })
    var arrb;
    var arrc;
    var arra;
    var arr=[];
    var arb=[];
    var arc=[];
    //搜索功能
    function unit(deviceStatus,SN){
        var pro_je
        var groupId
        var barc=[]
        var sarc=[];
        var arra=[];
        $.ajax({
            url: baseURL + 'fun/device/getDeviceByGroupIdNoPageLike?status=' + deviceStatus + "&deviceCode=" + SN,
            contentType: "application/json;charset=UTF-8",
            data: {},
            children:{},
            success: function (res) {
                console.log("设备返回搜索");
                console.log(res);
                var html="";
                var htmla="";
                var htmll="";
                var loconL="";
                var locon="";
                var locona="";
                for (var i = 0; i < res.data.length; i++) {
                    var projectStatus=res.data[i].projectStatus;
                    if (projectStatus == null ) {
                        projectStatus = "";
                    } else if(projectStatus == 1) {
                        projectStatus = "启用";
                    }else if(projectStatus == 2) {
                        projectStatus = "停用";
                    }
                    html+="<li class='pro_li' id="+res.data[i].id+">\n" +
                        "<div class=\"nav_pro_v nav_pro_v_L\" >\n" +
                        "<div class=\"nav_project\">"+res.data[i].name+"</div>\n" +
                        "<p class=\"nav_pro_p\">运行状态："+projectStatus+"</p>\n" +
                        "</div>\n" +
                        "<ul class=\"nav_gro nav_gro_"+i+"\"  id="+res.data[i].id+"1"+"   >\n" +
                        "</ul>"
                        "</li>\n";
                    $("#nav_a").append(html);
                    html = "";
                    loconL={
                        y:res.data[i].longitude,
                        x:res.data[i].latitude,
                        title:"L",
                        con:res.data[i].projectStatus,
                        branch:res.data[i].name,
                        id:"",
                        cliId:res.data[i].id,
                        state:1
                    };
                    barc.push(loconL); //push经纬度
                    for(var k = 0; k < res.data[i].children.length; k++){
                        var deviceCount=res.data[i].children[k].deviceNumber;
                        if (deviceCount == null ) {
                            deviceCount = "";
                        } else  {
                            deviceCount = res.data[i].children[k].deviceNumber;
                        }
                        htmll +="<li class='pro_li_a' id="+res.data[i].children[k].id+">\n" +
                            "<div class=\"nav_pro_v_b_L\" style='cursor: pointer;height:40px;clear: both' pid="+res.data[i].children[k].parentId+" id="+res.data[i].children[k].name+" t=\"0\">\n" +
                            "<img src='/statics/image/youjianxuan.svg' alt='' class='active' >"+
                            "<div class=\"nav_project nav_pro_fen\" >"+ res.data[i].children[k].name+"分组</div>\n" +
                            "<p class=\"nav_pro_p\">设备数："+ deviceCount+"</p>\n" +
                            "</div>\n" +
                            "<ul class=\"nav_gro_b_L nav_gro_b_L_"+i+k+"\" id="+res.data[i].children[k].id+"2"+">\n" +
                            "</ul>"
                            "</li>\n";
                        $(".nav_gro_"+i).append(htmll);
                        htmll = "";
                        locon={
                            y:res.data[i].children[k].longitude,
                            x:res.data[i].children[k].latitude,
                            con:res.data[i].children[k].projectStatus,
                            branch:res.data[i].children[k].name,
                            id:"",
                            cliId:res.data[i].children[k].id,
                            state:2
                        };
                        sarc.push(locon);
                        for(var v = 0; v < res.data[i].children[k].children.length; v++) {
                            var runState = res.data[i].children[k].children[v].runState
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
                            } else if (runState == 5) {
                                runState = "升级中";
                            }
                            htmla += "<li class='pro_li_a' name=" + res.data[i].children[k].children[v].deviceNumber + " type=" + res.data[i].children[k].children[v].deviceType + " deviceCode=" + res.data[i].children[k].children[v].deviceCode + " id=" + res.data[i].children[k].children[v].id + ">\n" +
                                "<div class=\"nav_pro_v_ba nav_bb_a\" style='cursor: pointer' title=" + res.data[i].children[k].children[v].deviceCode + " id=" + res.data[i].children[k].children[v].longitude + ">\n" +
                                "<div class=\"nav_project nav_pro_she\"  id=" + res.data[i].children[k].children[v].parentId + " name="+ res.data[i].children[k].children[v].name + ">设备名称：" + res.data[i].children[k].children[v].name + "</div>\n" +
                                "<p class=\"nav_pro_p\" id=" + res.data[i].children[k].children[v].latitude + ">运行状态：" + runState + "</p>\n" +
                                "</li>\n";
                            $(".nav_gro_b_L_"+i+k).append(htmla);
                            htmla = "";
                            locona = {
                                y: res.data[i].children[k].children[v].longitude,
                                x: res.data[i].children[k].children[v].latitude,
                                branch: res.data[i].children[k].children[v].name,
                                con: "",
                                fenId: res.data[i].children[k].children[v].parentId,
                                id: "",
                                cliId: res.data[i].children[k].children[v].id,
                                state: 3,
                            };
                            arra.push(locona);
                        }
                    }
                }
                m_p(0, arra);//调用地图搜索设备
                //点击项目
                $(".nav_pro_v_L").click(function(){
                    var  ID=$(this).parent(".pro_li").attr("id")
                    if($(this).siblings(".nav_gro").is(":visible")){
                        m_p(0, barc)
                        Bing(ID,"")
                        $(this).siblings(".nav_gro").slideUp();
                    }else{
                        $(this).siblings(".nav_gro").hide().slideDown();
                    }
                })
                //点击分组
                $(".nav_pro_v_b_L").click(function () {
                    if( $(this).find("img").attr("active")=="false"){
                        $(this).find(".rllimg").removeClass("rllimg").addClass("active");
                        $(this).find("img").attr("active","true");
                    }else{
                        $(this).find("img").removeClass("active").addClass("rllimg");
                        $(this).find("img").attr("active","false");
                        $('.nav_pro_v_b_L').unbind('mouseenter').unbind('mouseleave');// 分组移除hover
                    }

                    if($(this).siblings(".nav_gro_b_L").is(":visible")){
                        var pid=$(this).attr("pid");
                        $(this).siblings(".nav_gro_b_L").slideUp();
                        f_en(pid,"");// 分组/项目右侧边栏
                        m_p(0, sarc);//调用地图分组位置
                       $(".She_a,.ssde").css("display","hiden");
                    }else{
                        var gid=$(this).parents(".pro_li_a").attr("id");
                        $(this).siblings(".nav_gro_b_L").hide().slideDown();
                        $(".She_a,.ssde").css("display","show");
                        na_me=$(this).attr("id");
                        deviceCode=""
                        f_en("",gid);// 分组/项目右侧边栏
                        m_p(0, arra);//调用地图搜索设备
                    }
                })

                $(".She_a,.ssde").css("display","none");
                f_en("",groupId);// 分组/项目右侧边栏
                var par_id
                //点击设备
                $(".nav_bb_a").click(function(){
                   $(".ssde").css("display", "block");
                   var pro_je = $(this).parents(".pro_li").attr("id");//项目id
                   var groupId = $(this).parent().attr('id');//设备id
                   var fenId = $(this).children(".nav_pro_she").attr('id');
                   $(this).addClass("nav_bb_color").css({'color': "#fff"});
                   $(this).parents().siblings().children(".nav_bb_a").removeClass("nav_bb_color").css({'background': '', 'color': "#333"});
                   $(this).parents().parents().parents().parents().parents().siblings(".pro_li").children().children().children().children().children(".nav_bb_a ").removeClass("nav_bb_color").css({'background': '', 'color': "#333"});
                   par_id = $(this).parent(".pro_li_a").attr('id');
                   type = $(this).parents().attr('type');
                   deviceCode = $(this).parents().attr('deviceCode');
                   groId = $(this).children(".nav_pro_she").attr('id');
                   na_me = $(this).children(".nav_pro_she").attr('name');//设备名称
                   deviceId = par_id
                   var arrhove = [];
                    for(var i=0;i<arra.length;i++){
                        if(arra[i].cliId == $(this).parents(".pro_li_a").attr("id")){
                            arrhove.unshift({y: arra[i].y, x: arra[i].x,  con:"", branch: arra[i].branch,fenId:arra[i].fenId, id:arra[i].id, cliId:arra[i].cliId, state:3,hover:3})
                        }else if(arra[i].cliId != $(this).parents(".pro_li_a").attr("id")){
                            arrhove.push({y: arra[i].y, x: arra[i].x,con:"", branch: arra[i].branch,fenId:arra[i].fenId, id:arra[i].id, cliId:arra[i].cliId, state:3})//剩下的
                        }
                    }
                   //单台设备详情右侧
                   zhuang(pro_je, groId, par_id, deviceCode);
                   //调用地图
                   m_p(0, arrhove);
                   //日志信息
                   fu(par_id, "");
                   //历史数据、高级设置
               })
            }
        })
    }

    ///////////////////////////////////////////
    Xiang()
    var devStaus
    function Xiang(){
        $(".D_xuan_a").unbind('click');
        $(".D_xuan_a").click(function () {
            console.log("++++++++++++++++++")
            devStaus = $(this).attr('id');
            var arr=[];
            if (devStaus == "0") {
                $(".ra_state_d").css("transform", "rotate(0deg)");
                $(".ra_state_d").css("left", "40px");
                $(".ra_state_d").css("top", "40px");
            } else if (devStaus == "1") {
                $(".ra_state_d").css("transform", "rotate(-70deg)");
                $(".ra_state_d").css("top", "39px");
            } else if (devStaus == "2") {
                $(".ra_state_d").css("transform", "rotate(70deg)");
                $(".ra_state_d").css("left", "38px");
                $(".ra_state_d").css("top", "39px");
            } else if (devStaus == "3") {
                $(".ra_state_d").css("transform", "rotate(-145deg)");
                $(".ra_state_d").css("left", "40px");
                $(".ra_state_d").css("top", "38px");
            } else if (devStaus == "4") {
                $(".ra_state_d").css("transform", "rotate(145deg)");
                $(".ra_state_d").css("left", "39px");
                $(".ra_state_d").css("top", "38px");
            }
            $.ajax({
                url: baseURL + 'fun/project/queryProjectNoPage?deviceStatus=' + devStaus,
                contentType: "application/json;charset=UTF-8",
                type: "get",
                data: {},
                success: function (res) {
                    for (var i = 0; i < res.data.length; i++) {
                        var locon = {
                            y: res.data[i].longitude,
                            x: res.data[i].latitude,
                            title: "L",
                            con: res.data[i].sumCount,
                            branch: res.data[i].projectName,
                            id: "",
                            cliId: res.data[i].projectId,
                            state: 1
                        };
                        arr.push(locon); //push经纬度
                    }
                    m_p(devStaus,arr);
                }
            })
        })
    }
    function aas(pro_je) {
        $(".D_xuan_a").unbind('click');
        $(".D_xuan_a").click(function () {
            console.log("-----------------")
            var devStaus = $(this).attr('id');
            var arr=[];
            if (devStaus == "0") {
                $(".ra_state_d").css("transform", "rotate(0deg)");
                $(".ra_state_d").css("left", "40px");
                $(".ra_state_d").css("top", "40px");
            } else if (devStaus == "1") {
                $(".ra_state_d").css("transform", "rotate(-70deg)");
                $(".ra_state_d").css("top", "39px");
            } else if (devStaus == "2") {
                $(".ra_state_d").css("transform", "rotate(70deg)");
                $(".ra_state_d").css("left", "38px");
                $(".ra_state_d").css("top", "39px");
            } else if (devStaus == "3") {
                $(".ra_state_d").css("transform", "rotate(-145deg)");
                $(".ra_state_d").css("left", "40px");
                $(".ra_state_d").css("top", "38px");
            } else if (devStaus == "4") {
                $(".ra_state_d").css("transform", "rotate(145deg)");
                $(".ra_state_d").css("left", "39px");
                $(".ra_state_d").css("top", "38px");
            }
            $.ajax({
                url: baseURL + 'fun/group/queryGroupIdNoPage?deviceStatus=' + devStaus+ "&projectId=" + pro_je,
                contentType: "application/json;charset=UTF-8",
                type: "get",
                data: {},
                success: function (res) {
                    console.log("切换分组");
                    console.log(res)
                    for (var i = 0; i < res.data.length; i++) {
                        var locon={y:res.data[i].longitude,x:res.data[i].latitude,title:"L",con:res.data[i].deviceCount,branch:res.data[i].groupName,id:"",cliId:res.data[i].groupId,state:2};
                        arr.push(locon); //push经纬度
                    }
                    m_p(devStaus,arr);
                }
            })
        })
    }

    map(ass);
    function map(ass) {
        var ass; //跳转
        //全部/经纬度
        //项目数据
        $.ajax({
            url:baseURL + 'fun/project/queryProjectNoPage?deviceStatus=' + 0,
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data:{},
            success: function(res){
                console.log("项目信息")
                console.log(res)
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
                    var locon={y:res.data[i].longitude,x:res.data[i].latitude,title:"L",
                        con:res.data[i].sumCount,branch:res.data[i].projectName, id:"",cliId:res.data[i].projectId,state:1};
                    arb.push(locon); //push经纬度
                }
                $("#nav_a").append(html);
                if(ass != undefined){
                    arrb = ass ; //单个项目
                    ass = undefined;
                }else{
                    arrb=arb; //总项目经纬度获取全局变量
                }
                //地图
                m_p(0,arrb);
                //鼠标滑过项目
                $('.nav_pro_v').mouseenter(function(event) {
                    var arrhove=[];
                    for(var i=0;i<arrb.length;i++){
                        if(arrb[i].cliId == $(this).parents(".pro_li").attr("id")){
                            arrhove.unshift({y:arrb[i].y,x:arrb[i].x,title:"L",
                                con:arrb[i].con,branch:arrb[i].branch, id:"",cliId:arrb[i].cliId,state:1,hover:1})
                        }else if(arrb[i].cliId != $(this).parents(".pro_li").attr("id")){
                            arrhove.push({y:arrb[i].y,x:arrb[i].x,title:"L",
                                con:arrb[i].con,branch:arrb[i].branch, id:"",cliId:arrb[i].cliId,state:1,})//剩下的
                        }
                    }
                    m_p(devStaus,arrhove);
                })
                $('.nav_pro_v').mouseleave(function(event) {
                    m_p(devStaus,arrb);
                });
                //左侧边栏
                $(".She,.She_a").slideUp();
                $(".grouping").slideDown();
                $(".nav_pro_v").attr("t","0");
                $(".nav_pro_v").click(function(){
                    $(".ssde,.con_Gu,.She_a").css("display","none");
                    $(".con_Xu ").css("display","block");
                    var pro_je=$(this).parents(".pro_li").attr("id");
                    if( $(this).attr("t")=="0"){
                        aas(pro_je)
                        $(this).parents(".pro_li").siblings().find(".nav_pro_v").attr("t","0");
                        $(this).parents(".pro_li").siblings().find(".nav_gro").slideUp().empty();
                        $.ajax({
                            url: baseURL + 'fun/group/queryGroupIdNoPage?projectId=' + pro_je,
                            contentType: "application/json;charset=UTF-8",
                            type: "get",
                            data: {},
                            success: function (res) {
                                arc=[];
                                var htmla="";
                                for (var i = 0; i < res.data.length; i++) {
                                    var deviceCount=res.data[i].deviceCount;
                                    if (deviceCount == null ) {
                                        deviceCount = "";
                                    } else {
                                        deviceCount = res.data[i].deviceCount;
                                    }
                                    htmla += "<li class='pro_li_a' id="+res.data[i].groupId+">\n" +
                                        "<div class=\"nav_pro_v_b\" id="+res.data[i].groupName+" t=\"0\">\n" +
                                        "<img src='/statics/image/youjianxuan.svg' alt='' class='rllimg' >"+
                                        "<div class=\"nav_project nav_pro_fen\" >"+ res.data[i].groupName+"分组</div>\n" +
                                        "<p class=\"nav_pro_p\" style='margin-left:3%;'>设备数："+ deviceCount+"</p>\n" +
                                        "</div>\n" +
                                        "<ul class=\"nav_gro_b\" id="+res.data[i].groupId+"2"+">\n" +
                                        "</ul>"
                                    "</li>\n"
                                    var locon={y:res.data[i].longitude,x:res.data[i].latitude,title:"L",con:res.data[i].deviceCount,branch:res.data[i].groupName,id:"",cliId:res.data[i].groupId,state:2};
                                    arc.push(locon); //push经纬度
                                }
                                $("#" + pro_je + "1").append(htmla).hide().slideDown();
                                $("#"+pro_je).css('color','#333').siblings().css('color','#fff');//点击选项改变状态
                                arrc=arc ;//分组经纬度获取全局变量
                                m_p(0,arrc);//地图
                                //鼠标滑过分组改变地图上分组图标状态（hover）
                                $('.nav_pro_v_b').mouseenter(function(event) {
                                    var arrhove=[];
                                    for(var i=0;i<arrc.length;i++){
                                        if(arrc[i].cliId == $(this).parents(".pro_li_a").attr("id")){
                                            arrhove.unshift({y:arrc[i].y,x:arrc[i].x,title:"L",con:arrc[i].con,branch:arrc[i].branch,id:"",cliId:arrc[i].cliId,state:2,hover:2})
                                        }else if(arrc[i].cliId != $(this).parents(".pro_li_a").attr("id")){
                                            arrhove.push({y:arrc[i].y,x:arrc[i].x,title:"L",con:arrc[i].con,branch:arrc[i].branch,id:"",cliId:arrc[i].cliId,state:2,})//剩下的
                                        }
                                    }
                                    m_p(devStaus,arrhove);
                                })
                                $('.nav_pro_v_b').mouseleave(function(event) {
                                    m_p(devStaus,arrc);
                                });
                                //右侧边栏数据
                                Bing(pro_je,"");//控制项目
                                // 分组下设备
                                var lindex=localStorage.getItem('key')
                                var lindexs=localStorage.getItem('keys')
                                var xindex=localStorage.getItem('key2')
                                if(sessionStorage.getItem('l')=='1'){
                                    console.log($(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").length);
                                    $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").eq(lindexs).find('.nav_pro_v_b').click();
                                }
                                //  分组下设备结束
                                //禁止项目鼠标移入事件
                                $('.nav_pro_v').unbind('mouseenter').unbind('mouseleave');
                            }
                        });
                        $(this).attr("t","1");
                    }else{
                        $(this).attr("t","0");
                        $("#" + pro_je + "1").slideUp().empty();
                        /*轮盘筛选项目*/
                        Xiang();
                        //右侧边栏
                        $(".She,.She_a").hide();
                        $(".grouping").show();
                        //项目绑定鼠标移出事件
                        $('.nav_pro_v').mouseenter(function(event) {
                            var arrhove=[];
                            for(var i=0;i<arrb.length;i++){
                                if(arrb[i].cliId == $(this).parents(".pro_li").attr("id")){
                                    arrhove.unshift({y:arrb[i].y,x:arrb[i].x,title:"L",
                                        con:arrb[i].con,branch:arrb[i].branch, id:"",cliId:arrb[i].cliId,state:1,hover:1})
                                }else if(arrb[i].cliId != $(this).parents(".pro_li").attr("id")){
                                    arrhove.push({y:arrb[i].y,x:arrb[i].x,title:"L",
                                        con:arrb[i].con,branch:arrb[i].branch, id:"",cliId:arrb[i].cliId,state:1,})//剩下的
                                }
                            }
                            m_p(0,arrhove);
                        })
                        $('.nav_pro_v').mouseleave(function(event) {
                            m_p(0,arrb);
                        });
                        //关闭项目列表
                        m_p(0,arrb);
                }
                    var pro_je=$(this).parent().attr('id');
                    if($("#"+pro_je+"1").html() == ""){
                        $("#"+pro_je+"1").html("");
                        return
                    }
                })

               $(".nav_pro_v_b").attr('t','0');
               $(document).delegate(".nav_pro_v_b","click",function () {
                    $(".ssde").css("display","none");
                //    单选
                    if( $(this).find("img").attr("active")=="true"){
                        $(this).find("img").removeClass("active").addClass("rllimg");
                        $(this).find("img").attr("active","false");
                        //关闭分组定位
                        m_p(0,arrc);
                        //分组绑定hover事件
                        $('.nav_pro_v_b').mouseenter(function(event) {
                            console.log("__________9999")
                            var arrhove=[];
                            for(var i=0;i<arrc.length;i++){
                                if(arrc[i].cliId == $(this).parents(".pro_li_a").attr("id")){
                                    arrhove.unshift({y:arrc[i].y,x:arrc[i].x,title:"L",con:arrc[i].con,branch:arrc[i].branch,id:"",cliId:arrc[i].cliId,state:2,hover:2})
                                }else if(arrc[i].cliId != $(this).parents(".pro_li_a").attr("id")){
                                    arrhove.push({y:arrc[i].y,x:arrc[i].x,title:"L",con:arrc[i].con,branch:arrc[i].branch,id:"",cliId:arrc[i].cliId,state:2,})//剩下的
                                }
                            }
                            m_p(0,arrhove);
                        })
                        $('.nav_pro_v_b').mouseleave(function(event) {
                            m_p(0,arrc);
                        });
                    }else{
                        $(this).find(".rllimg").removeClass("rllimg").addClass("active");
                        $(this).find("img").attr("active","true");
                        // 分组移除hover
                        $('.nav_pro_v_b').unbind('mouseenter').unbind('mouseleave');
                    }
                    console.log($(this).attr('t'));
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
                            console.log("==================")
                            console.log("分组下设备")
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
                            zu(deviceStatus);
                        });
                        zu(deviceStatus);
                        $(this).siblings(".nav_gro_b").slideDown();
                        function zu(deviceStatus) {
                            $("#" + groupId + "2").html("");
                            $.ajax({
                                url: baseURL + 'fun/device/getDeviceByGroupIdNoPage?groupId=' + groupId + "&projectId=" + pro_je + "&status=" + deviceStatus ,
                                contentType: "application/json;charset=UTF-8",
                                type: "get",
                                data: {},
                                success: function (res) {
                                    console.log("设备返回");
                                    console.log(res);
                                    arr = [];
                                    arra = [];
                                    var htmlb = "";
                                    for (var i = 0; i < res.data.length; i++) {
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
                                            "<div class=\"nav_project nav_pro_she\"  id=" + res.data[i].groupId + ">" + res.data[i].deviceName + "</div>\n" +
                                            "<p class=\"nav_pro_p\" id=" + res.data[i].latitude + ">" + runState + "</p>\n" +
                                            "</li>\n"
                                        var locon = {y: res.data[i].longitude, x: res.data[i].latitude, title: "L", con:"", branch: res.data[i].deviceName,fenId:res.data[i].groupId, id:res.data[i].deviceId, cliId:res.data[i].deviceId, state:3}
                                        arr.push(locon); //push经纬度
                                    }
                                    $("#" + groupId + "2").append(htmlb).hide().slideDown();
                                    // $("#" + groupId + "2").slideUp();
                                    arra = arr //经纬度获取全局变量
                                    f_en(pro_je, groupId);   //控制分组
                                    fu("", groupId);//日志信息
                                    m_p(deviceStatus, arra); //地图
                                    console.log(sessionStorage.getItem('l'))
                                    var lindex=localStorage.getItem('key')
                                    var lindexs=localStorage.getItem('keys')
                                    var xindex=localStorage.getItem('key2')
                                    if(sessionStorage.getItem('l')=='1'){
                                        console.log( $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").length);
                                        $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").eq(lindexs).find('.nav_gro_b').find(".pro_li_a").eq(xindex).find(".nav_bb").click()
                                    }
                                    $('.nav_bb').mouseenter(function(event) {
                                        var arrhove=[];
                                        for(var i=0;i<arra.length;i++){
                                            if(arra[i].cliId == $(this).parents(".pro_li_a").attr("id")){
                                                arrhove.unshift({y: arra[i].y, x: arra[i].x, title: "L", con:"", branch: arra[i].branch,fenId:arra[i].fenId, id:arra[i].id, cliId:arra[i].cliId, state:3,hover:3})
                                            }else if(arra[i].cliId != $(this).parents(".pro_li_a").attr("id")){
                                                arrhove.push({y: arra[i].y, x: arra[i].x, title: "L", con:"", branch: arra[i].branch,fenId:arra[i].fenId, id:arra[i].id, cliId:arra[i].cliId, state:3})//剩下的
                                            }
                                        }
                                        m_p(deviceStatus,arrhove);
                                    })
                                    $('.nav_bb').mouseleave(function(event) {
                                        m_p(deviceStatus,arra);
                                    });
                                }
                            });
                        }
                    } else {
                        $("#" + groupId + "2").slideUp().html("");
                        $(this).attr('t','0');
                        f_en(pro_je, groupId);
                        return
                    }
                })

                //单台设备
                var par_id
                var event=event||window.event
                $(document).delegate(".nav_bb","click",function () {
                    //event.stopPropagation()
                    $(".ssde").css("display","block");
                    var pro_je=$(this).parents(".pro_li").attr("id");//项目id
                    var groupId = $(this).parent().attr('id');//设备id
                    var fenId= $(this).children(".nav_pro_she").attr('id');
                    $(this).addClass("nav_bb_color").css({'color':"#fff"});
                    $(this).parents().siblings().children(".nav_bb").removeClass("nav_bb_color").css({'background':'','color':"#333"});
                    console.log(deviceStatus + "单台设备");
                    console.log(arra);
                    par_id = $(this).parent(".pro_li_a").attr('id');
                    type = $(this).parents().attr('type');
                    deviceCode = $(this).parents().attr('deviceCode');
                    na_me = $(this).parents().attr('name');
                    groId = $(this).children(".nav_pro_she").attr('id');
                    deviceId=par_id
                    var arrhove=[];
                    for(var i=0;i<arra.length;i++){
                        if(arra[i].cliId == $(this).parents(".pro_li_a").attr("id")){
                            arrhove.unshift({y: arra[i].y, x: arra[i].x, title: "L", con:"", branch: arra[i].branch,fenId:arra[i].fenId, id:arra[i].id, cliId:arra[i].cliId, state:3,hover:3})
                        }else if(arra[i].cliId != $(this).parents(".pro_li_a").attr("id")){
                            arrhove.push({y: arra[i].y, x: arra[i].x, title: "L", con:"", branch: arra[i].branch,fenId:arra[i].fenId, id:arra[i].id, cliId:arra[i].cliId, state:3})//剩下的
                        }
                    }

                    //单台设备详情右侧
                    zhuang(pro_je, groId, par_id,deviceCode);
                    //调用地图
                    m_p(deviceStatus,arrhove);
                    //日志信息
                    fu(par_id, "");
                    //历史数据、高级设置
                })
                var lindex=localStorage.getItem('key')
                var lindexs=localStorage.getItem('keys')
                var xindex=localStorage.getItem('key2')
                if(sessionStorage.getItem('l')=='1'){
                    $("#drawer_img").click();
                    $(".nav_a").find("li").eq(lindex).find(".nav_pro_v").click();
                    $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").eq(lindexs).find('.nav_pro_v_b').click()
                    $(".nav_a").find(".pro_li").eq(lindex).find(".pro_li_a").eq(lindexs).find('.nav_pro_v_ba').click();
                }
                //项目下分组
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
    var infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30)});
    var removeList = [];	//移除标记点
    var markers=[];
    var preIcon         //图标定义
    var imags           //图标状态
    var nsize
    var newSize
    var setLabel= new AMap.Pixel(11,10);
    function m_p(deviceStatus,arra) {
        if(removeList.length != 0){
            map.remove(removeList);
            removeList = [];
        }
        console.log("地图里面的定位信息")
        console.log(arra);
        var marker
        for(var i = 0; i < arra.length; i++){
             var draggable
            $(".amap-marker-label").css("color","#fff");
             if( arra[i].id ==""){
                 draggable=false
             }else{
                 draggable=true
             }
            // 项目，分组，设备图标类型
            if(arra[i].state == 1){
                nsize=new AMap.Size(64, 64);
                newSize=new AMap.Size(46,46);
                setLabel= new AMap.Pixel(11,10);
                if(deviceStatus ==0){
                    imags= "/statics/image/cbcx.png",
                        hov_x()
                }else if(deviceStatus ==1){
                    imags= "/statics/image/cccx.png",
                        hov_x()
                }else if(deviceStatus ==2){
                    imags= "/statics/image/cdcx.png",
                        hov_x()
                }else if(deviceStatus ==3){
                    imags= "/statics/image/cacx.png",
                        hov_x()
                }else if(deviceStatus ==4){
                    imags= "/statics/image/cecx.png",
                        hov_x()
                }
            }else if(arra[i].state == 2){
                nsize=new AMap.Size(64, 64);
                newSize=new AMap.Size(36,36);
                setLabel= new AMap.Pixel(4,7);
                if(deviceStatus ==0){
                    imags= "/statics/image/bbh.png",
                        hov_z()
                }else if(deviceStatus ==1){
                    imags= "/statics/image/bch.png",
                        hov_z()
                }else if(deviceStatus ==2){
                    imags= "/statics/image/bdh.png",
                        hov_z()
                }else if(deviceStatus ==3){
                    imags= "/statics/image/bah.png",
                        hov_z()
                }else if(deviceStatus ==4){
                    imags= "/statics/image/beh.png",
                        hov_z()
                }
            }else if(arra[i].state == 3){
                nsize=new AMap.Size(64, 64);
                newSize=new AMap.Size(36,36);
                if(deviceStatus ==0){
                    imags= "/statics/image/abh.png"
                     hov_s()
                }else if(deviceStatus ==1){
                    imags= "/statics/image/ach.png",
                    hov_s()
                }else if(deviceStatus ==2){
                    imags= "/statics/image/aah.png",
                    hov_s()
                }else if(deviceStatus ==3){
                    imags= "/statics/image/adh.png",
                    hov_s()
                }else if(deviceStatus ==4){
                    imags= "/statics/image/aeh.png",
                     hov_s()
                }
            }
            preIcon = new AMap.Icon({
                image: imags,
                size:nsize,  //图标大小
                imageSize:newSize ,
            });
            marker = new AMap.Marker({
                map: map,
                position:[arra[i].y, arra[i].x],
                offset: new AMap.Pixel(-10, -10),
                icon: preIcon,
                title: '北京',
                zoom: 20, // 设置是否可以拖拽
                draggable: draggable,
                cursor: 'move', // 设置拖拽效果
                raiseOnDrag: true,
                resizeEnable: true//拖拽地图结束绑定监听
            });
            //提示框
            marker.content ='<p></p><div>'+arra[i].branch+'</div>';
            marker.emit('click',{target: marker});
            marker.on('click',showInfoClick);//单击
            marker.on('dragend',showInfoDragend);//拖拽结束
            marker.on('mouseover',infoOpen);//鼠标移入
            marker.on('mouseout',infoClose);//鼠标移出
            marker.on('dblclick',showInfoDbClick);//双击
            markers.push(marker);
            marker.setLabel({
                offset:setLabel,
                content: arra[i].con ,
            });
            marker.id=arra[i].id;
            marker.fenId=arra[i].fenId;
            marker.branch=arra[i].branch;
            marker.cliId=arra[i].cliId;
            marker.state=arra[i].state;
            marker.deviceStatus=deviceStatus;
            removeList.push(marker);
            //鼠标滑过左侧菜单栏设备地图改变当前鼠标滑过的图标
            function hov_s() {
                if(arra[i].hover == 3){
                    if(deviceStatus ==0) {
                        imags= "/statics/image/abax.png"
                    }else if(deviceStatus ==1){
                        imags= "/statics/image/acax.png"
                    }else if(deviceStatus ==2){
                        imags= "/statics/image/aaax.png"
                    }else if(deviceStatus ==3){
                        imags= "/statics/image/adax.png"
                    }else if(deviceStatus ==4) {
                        imags = "/statics/image/aeax.png"
                    }
                    nsize=new AMap.Size(68,68)
                    newSize=new AMap.Size(40,40)
                }
            }
            //鼠标滑过左侧菜单栏分组地图改变当前鼠标滑过的图标
            function hov_z() {
                if(arra[i].hover == 2){
                    if(deviceStatus ==0) {
                        imags= "/statics/image/bbbx.png"
                    }else if(deviceStatus ==1){
                        imags= "/statics/image/bcbx.png"
                    }else if(deviceStatus ==2){
                        imags= "/statics/image/babx.png"
                    }else if(deviceStatus ==3){
                        imags= "/statics/image/bdbx.png"
                    }else if(deviceStatus ==4) {
                        imags = "/statics/image/bebx.png"
                    }
                    nsize=new AMap.Size(69, 69)
                    newSize=new AMap.Size(48,48)
                    setLabel= new AMap.Pixel(9,10);
                }
            }
            //鼠标滑过左侧菜单栏项目地图改变当前鼠标滑过的图标
            function hov_x() {
                if(arra[i].hover == 1){
                    if(deviceStatus ==0) {
                        imags= "/statics/image/cbh.png"
                    }else if(deviceStatus ==1){
                        imags= "/statics/image/cch.png"
                    }else if(deviceStatus ==2){
                        imags= "/statics/image/cdh.png"
                    }else if(deviceStatus ==3){
                        imags= "/statics/image/cah.png"
                    }else if(deviceStatus ==4) {
                        imags = "/statics/image/ceh.png"
                    }
                    nsize=new AMap.Size(70, 70)
                    newSize=new AMap.Size(50,50)
                    setLabel= new AMap.Pixel(12,11);
                }
            }
        }
    }
    //鼠标划入
    function infoClose(e) {
        infoWindow.close(map, e.target.getPosition());
    }
    function infoOpen(e) {
        infoWindow.setContent(e.target.content);
        infoWindow.open(map, e.target.getPosition());
    }
    //拖拽地图结束
    function showInfoDragend(e){
        var groupId=this.fenId
        var deviceId=this.id
        var dev_name=this.branch
        layer.confirm('是否改动当前位置？', {
            btn: ['确定','取消'] ,//按钮
            yes: function(index){
                $.ajax({
                    url:baseURL + 'fun/device/updateDevice',
                    contentType: "application/json;charset=UTF-8",
                    type:"POST",
                    data: JSON.stringify({
                        "deviceName":dev_name,
                        "groupId":groupId,
                        "deviceId":deviceId,
                        "longitude":e.lnglat.lng,
                        "latitude":e.lnglat.lat,
                    }),
                    success: function(res) {
                        if(res.code== "203"){
                            layer.open({
                                title: '信息',
                                content:res.msg,
                                skin: 'demo-class'
                            });
                        }
                    }
                })
                 layer.close(layer.index)
             },
        })
    }
    //单击（项目数据）
    //点击鼠标选中状态图标
    var imgs
    function showInfoClick(e){
        var cliId=this.cliId;
        //地图图表转换
        for (var i = 0; i < markers.length; i++) {
            markers[i].setIcon(preIcon);
        }
        //左侧菜单栏切换状态
        if(this.state === 1){
            $("#"+cliId).css('color','#333').siblings().css('color','#fff');
            if(this.deviceStatus== 0){
                imgs= "/statics/image/cbcx.png"
            }else if(this.deviceStatus== 1){
                imgs= "/statics/image/cccx.png"
            }else if(this.deviceStatus== 2){
                imgs= "/statics/image/cdcx.png"
            }else if(this.deviceStatus== 3){
                imgs= "/statics/image/cacx.png"
            }else if(this.deviceStatus== 4){
                imgs= "/statics/image/cecx.png"
            }
            var clickIcon = new AMap.Icon({
                image: imgs,
                size: new AMap.Size(64, 64),  //图标大小
                imageSize: new AMap.Size(41,41)
            });
            e.target.setIcon(clickIcon);
        }else if(this.state === 2){
            $("#"+cliId).css('color','#000').siblings().css('color','#333');
            if(this.deviceStatus== 0){
                imgs= "/statics/image/bbbx.png"
            }else if(this.deviceStatus== 1){
                imgs= "/statics/image/bcbx.png"
            }else if(this.deviceStatus== 2){
                imgs= "/statics/image/babx.png"
            }else if(this.deviceStatus== 3){
                imgs= "/statics/image/bdbx.png"
            }else if(this.deviceStatus== 4){
                imgs= "/statics/image/bebx.png"
            }
            var clickIcons = new AMap.Icon({
                image: imgs,
                size: new AMap.Size(64, 64),  //图标大小
                imageSize: new AMap.Size(41,41)
            });
            e.target.setIcon(clickIcons);
        }else if(this.state === 3){
            $("#"+cliId).children(".nav_bb").css({'background':'rgba(0,165,160,0.7)','color':'#fff'});
            $("#"+cliId).siblings(".pro_li_a").children(".nav_bb").removeClass("nav_bb_color").css({'background':'','color':"#333"})
            if(deviceStatus ==0) {
                imgs= "/statics/image/abax.png"
            }else if(deviceStatus ==1){
                imgs= "/statics/image/acax.png"
            }else if(deviceStatus ==2){
                imgs= "/statics/image/aaax.png"
            }else if(deviceStatus ==3){
                imgs= "/statics/image/adax.png"
            }else if(deviceStatus ==4) {
                imgs = "/statics/image/aeax.png"
            }
            var clickIconc = new AMap.Icon({
                image: imgs,
                size: new AMap.Size(64, 64),  //图标大小
                imageSize: new AMap.Size(41,41)
            });
            e.target.setIcon(clickIconc);
        }

    }
    //双击
    function showInfoDbClick(e){
        map.remove(removeList);
        var cliId=this.cliId;
        //地图项目定位列表*****
        $("#"+cliId).children().trigger('click');
        var text = '您在 [ '+e.lnglat.getLng()+','+e.lnglat.getLat()+' ] 的位置双击了地图！'
        infoWindow.setContent(e.target.content);
        infoWindow.open(map, e.target.getPosition());
        var zoom
        if(this.state==1){
            zoom = 7;
        }else if(this.state==2){
            zoom = 11;
        }else if(this.state==3){
            zoom = 26;
        }
        var lng = this.Nh.position.lng;
        var lat = this.Nh.position.lat;
        map.setZoomAndCenter(zoom, [lng, lat]); //同时设置地图层级与中心点
        infoWindow.close(map, e.target.getPosition());//关闭标点提示框
    }
    map.setFitView();
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
    /////////////////////////////地图结束

    //日志方法
    function fu(par_id,groupId){
        $("#operation").unbind('click');
        $("#operation").click(function(){
            $(".shade_project,.shade_b_project").css("display","block");
            $(".Bian_P").html("操作日志")
            r(par_id,groupId)
        })
        function r(par_id,groupId){
            $("#pro_list").html("");
            $("#pro_list").append("");
            $.ajax({
                url:baseURL + 'fun/faultlog/queryFaultlog?deviceId='+par_id+"&groupId="+groupId,
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
    //控制面板
    $("#hear_control").click(function(){
        localStorage.setItem("key",$(".nav_pro_v[t='1']").parent('.pro_li').index());
        localStorage.setItem("keys",$(".nav_pro_v_b[t='1']").parent('.pro_li_a').index());
        localStorage.setItem("key2",$(".nav_bb_color").parent('.pro_li_a').index());
        console.log($(".nav_pro_v[t='1']").parent().index());
        console.log($(".nav_pro_v_b[t='1']").parent('.pro_li_a').index());
        console.log($(".nav_bb_color").parent('.pro_li_a').index());
        var searchUrl=encodeURI('../control/control.html?deviceCode='+deviceCode+"&grod="+groId+"&type="+type+"&name="+na_me+"&deviceId="+deviceId)
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
