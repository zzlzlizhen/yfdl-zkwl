$(function(){


    //    数值增殖期
    (function ($) {
        $("#r_inpl_one").keyup(function(){
            var $this = $(this);
            var dian =Number ($("#chong").val());
            if(dian<=$this.val()){
                $this.val(dian)
            }
        })
        $("#r_Load_power").keyup(function(){
            var $this = $(this);
            if($this.val()>40){
                $this.val(40)
            }
        })
        $("#r_teml_forl").keyup(function(){
            var $this = $(this);
            if($this.val()>8){
                $this.val(8)
            }
        })
        $("#chong3").keyup(function(){
            var $this = $(this);
            if($this.val()>120){
                $this.val(120)
            }
        })
        $("#r_inpl_tw").keyup(function(){
            var $this = $(this);
            if($this.val()>40){
                $this.val(20)
            }
        })
        $("#r_inpl_one2").keyup(function(){
            var $this = $(this);
            if($this.val()>120){
                $this.val(120)
            }
        })
        $("#r_teml_ma").keyup(function(){
            var $this = $(this);
            if($this.val()>600){
                $this.val(30)
            }
        })
        $(".rrjia").click(function () {
            if($(this).parent().siblings(".form-control").val()<=38){
                $(this).parent().siblings(".form-control").val($(this).parent().siblings(".form-control").val(), 0 + 1);
            }else{
                $(this).parent().siblings(".form-control").val("39")
            }
        })
        $("#r_teml_thglte,#r_teml_thglt,#r_teml_thete,#r_teml_thet").keyup(function(){
            var $this = $(this);
            $this.val($this.val().replace(/[^\-?\d.]/g,'')); //输入负数或正数
            // var hshu = Number( $("#r_teml_thete").val()); || $this.val()>hshu
            if($this.val()<-40){
                $this.val(-40)
            }
        });
        $("#r_teml_thglte").keyup(function(){
            var $this = $(this);
            if($this.val()>99){
                $this.val(99)
            }
        })
        $(".nqiu").on('click', function () {
            if($(this).parent().siblings(".form-control").val()<=97  ){
                $(this).parent().siblings(".form-control").val($(this).parent().siblings(".form-control").val(), 0 + 1);
            }else{
                $(this).parent().siblings(".form-control").val("98")
            }
        })
        $("#r_teml_thet,#r_teml_thete").keyup(function(){
            var $this = $(this);
            // var qianshu =Number( $("#r_teml_thglte").val())
        // ||$this.val()<qianshu
            if($this.val()>99 ){
                $this.val(99)
            }
        });
        $('.spinner .btn:first-of-type').on('click', function () {
            $(this).parent().siblings(".form-control").val(parseInt($(this).parent().siblings(".form-control").val(), 0) + 1);
        });
        $('.spinner .btn:last-of-type').on('click', function () {
            // $('.spinner input').val( parseInt($('.spinner input').val(), 10) - 1);
            if($(this).parent().siblings(".form-control").val()<1&&$(this).hasClass("jianr")!=true){
                return false
            }else{
                if($(this).parent().siblings(".form-control").val()>-40){
                    $(this).parent().siblings(".form-control").val(parseInt($(this).parent().siblings(".form-control").val(), 0) - 1);
                }
            }
        });
        $(".jiar").click(function () {
            if($(this).parent().siblings(".form-control").val()<=98){
                $(this).parent().siblings(".form-control").val($(this).parent().siblings(".form-control").val(), 0 + 1);
            }else{
                $(this).parent().siblings(".form-control").val("99")
            }
        })

        //电池串数参数限制1--8
        $(".jiaba").click(function(){
            var add
            var symbol=$(this).attr("value");
            var bunch=$(this).parent().siblings(".form-control").val();
            var th=$(this).parent().siblings(".form-control");
            if(symbol == "+"){
               add=bunch++;
               if(add >= 8){
                   th.val("8");
                   return
               }
            }
            else if(symbol == "-"){
                add=bunch--;
                if(add <= 1){
                    th.val("1");
                    return
                }
            }
            th.val(add);
        })
    })(jQuery);
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
    var deviceCode=href['deviceCode'];//设备编号
    var projectId=href['projectId'];//项目id
    var grod=href['grod'];//分组id
    var deviceId=href['deviceId'];//设备id
    var type=href['type'];//设备类型
    var name=href['name'];//设备名称
    var disabled  //人体感应开关
    var t; //定时刷新
    var sffd=""
    console.log("设备deviceCode")
    console.log(deviceCode)
    console.log("项目id")
    console.log(projectId)
    console.log("分组id")
    console.log(grod)
    console.log("设备id")
    console.log(deviceId)
    console.log("设备类型")
    console.log(type)
    $("#bian").html("编号:"+deviceCode+">")
    $("#bian_b").html("名称:"+name)
    $.ajax({
        url: baseURL + 'fun/history/getUsers',
        contentType: "application/json;charset=UTF-8",
        type: "get",
        success: function (res) {
            if (res.data.username == "admin" || res.data.username == "test01" || res.data.username == "yfdl_cs"|| res.data.username == "yfdl_cj") {
                $("#hardware").show();
            }
        }
    })

    //判断高级设置页面针对是单台设备还是多台设备（做处理）
    if(deviceCode != "" ){
        var myDate = new Date;
        var ms = (1000 * 60 * 60 * 24 * 30)
        var date1 = new Date(myDate.getTime() - ms);
        var str = "" + date1.getFullYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDate() + "";
        var stra = "" + date1.getFullYear() + "-" + (date1.getMonth() + 2) + "-" + date1.getDate() + "";
        var str_m="" + date1.getFullYear() + "-" + (date1.getMonth()- 5)
        var str_F="" + (date1.getFullYear()-3)
        var selse
        function getNow(s) {
            return s < 10 ?  + s: s;
        }
        var year=myDate.getFullYear();//获取当前年
        var month=myDate.getMonth()+1;//获取当前月
        var date=myDate.getDate();//获取当前日
        var now=year+'-'+getNow(month)+"-"+getNow(date);

        day(deviceCode,now);
        $("#d421").val(now);
        var data={dateFmt:'yyyy-M-d',minDate:str,maxDate:'%y-%M-%d'}
        var data_a
        $("#sele_con").change(function () {
            var selectId = $('#sele_con>option:selected').attr("id");
            selse=selectId
            data_a=1
            if(selse == "1"){
                data={dateFmt:'yyyy-M-d',minDate:str,maxDate:'%y-%M-%d'};
            }else if(selse == "2"){
                data={dateFmt:'yyyy-M',minDate:str_m,maxDate:'%y-%M'};
            }else if(selse == "3"){
                data={dateFmt:'yyyy',minDate:str_F,maxDate:'%y'};
            }
        })

        $("#d421").click(function(){
            WdatePicker(data);
        });

        $("#statistics").unbind('click');
        $("#statistics").click(function () {
            selse = $('#sele_con>option:selected').attr("id");
            var va_l=$("#d421").val();
            if(selse =="1"){
                day(deviceCode,va_l);
            }else if(selse == "2"){
                Month(deviceCode,va_l);
            }else if(selse == "3"){
                Year(deviceCode,va_l);
            }
        })
    }else if(deviceCode == ""){
         $("#opt").addClass("cur").siblings().removeClass('cur');
         $("#op_a").addClass("on").siblings().removeClass('on');
         $(".history,#shuju").css({"pointer-events":"none","color":"#ccc"});
         gao();
    }

    var dischargeCapacityList ;//放电量
    var chargingCapacityList; //充电量
    var chargingCurrentList ; //充电电流
    var dischargeCurrentList;//放电电流
    var batteryVoltageList;  //电池电压
    var ambientTemperatureList;//环境温度
    var internalTemperatureList;  //内部温度
    var visitorsFlowrateList ;   //人流量
    var inductionFrequencyList ; //感应次数
    var meteorologicalList ;  //气象信息
    var hours;
    var newHours;
    var newH;
    //根据年月日调取渲染数据表格
    function day(deviceCode,va_l){
        $.ajax({
            url: baseURL + 'fun/history/queryHistoryByDay?deviceCode=' + deviceCode+"&time="+va_l,
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
                dischargeCapacityList=res.data.loadPowerList;//放电量/负载功率
                chargingCapacityList=res.data.chargingPowerList;  //充电量/充电功率
                chargingCurrentList=res.data.chargingCurrentList;    //充电电流
                dischargeCurrentList=res.data.dischargeCurrentList;  //放电电流
                batteryVoltageList=res.data.batteryVoltageList;      //电池电压
                ambientTemperatureList=res.data.ambientTemperatureList;  //环境温度
                internalTemperatureList=res.data.internalTemperatureList; //内部温度
                visitorsFlowrateList =res.data.visitorsFlowrateList;   //人流量
                inductionFrequencyList =res.data.inductionFrequencyList; //感应次数
                meteorologicalList=res.data.meteorologicalList;   //气象信息
                hours=res.data.hours; //X坐标
                newHours=res.data.newHours;
                newH="1";
                f(hours,newHours,newH,dischargeCapacityList,chargingCapacityList,chargingCurrentList,dischargeCurrentList,batteryVoltageList,ambientTemperatureList,internalTemperatureList,visitorsFlowrateList,inductionFrequencyList,meteorologicalList)
            }
        })
    }
    function Month(deviceCode,va_l){
        var va_y=va_l.split("-");
        $.ajax({
            url: baseURL + 'fun/history/queryHistoryByMouth?deviceCode=' + deviceCode+"&year="+va_y[0]+"&month="+va_y[1],
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
                dischargeCapacityList=res.data.loadPowerList;//放电量
                chargingCapacityList=res.data.chargingPowerList;  //充电量
                chargingCurrentList=res.data.chargingCurrentList;    //充电电流
                dischargeCurrentList=res.data.dischargeCurrentList;  //放电电流
                batteryVoltageList=res.data.batteryVoltageList;      //电池电压
                ambientTemperatureList=res.data.ambientTemperatureList;  //环境温度
                internalTemperatureList=res.data.internalTemperatureList; //内部温度
                visitorsFlowrateList =res.data.visitorsFlowrateList;   //人流量
                inductionFrequencyList =res.data.inductionFrequencyList; //感应次数
                hours=res.data.hours; //X坐标
                meteorologicalList=res.data.meteorologicalList;   //气象信息
                newHours=res.data.newHours;
                newH="2"
                f(hours,newHours,newH,dischargeCapacityList,chargingCapacityList,chargingCurrentList,dischargeCurrentList,batteryVoltageList,ambientTemperatureList,internalTemperatureList,visitorsFlowrateList,inductionFrequencyList,meteorologicalList)
            }
        })
    }
    function Year(deviceCode,va_l){
        $.ajax({
            url: baseURL + 'fun/history/queryHistoryByYear?deviceCode='+ deviceCode+"&year="+va_l,
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
                dischargeCapacityList=res.data.loadPowerList;//放电量
                chargingCapacityList=res.data.chargingPowerList;  //充电量
                chargingCurrentList=res.data.chargingCurrentList;    //充电电流
                dischargeCurrentList=res.data.dischargeCurrentList;  //放电电流
                batteryVoltageList=res.data.batteryVoltageList;      //电池电压
                ambientTemperatureList=res.data.ambientTemperatureList;  //环境温度
                internalTemperatureList=res.data.internalTemperatureList; //内部温度
                visitorsFlowrateList =res.data.visitorsFlowrateList;   //人流量
                inductionFrequencyList =res.data.inductionFrequencyList; //感应次数
                hours=res.data.hours; //X坐标
                meteorologicalList=res.data.meteorologicalList;   //气象信息
                newHours=res.data.newHours;
                newH="2"
                f(hours,newHours,newH,dischargeCapacityList,chargingCapacityList,chargingCurrentList,dischargeCurrentList,batteryVoltageList,ambientTemperatureList,internalTemperatureList,visitorsFlowrateList,inductionFrequencyList,meteorologicalList)
            }
        })
    }
    ////////////////////////////////渲染年月日数据完成
    //实时数据
    function shuju(deviceId,deviceCode){
        var volCharge;
        var volOverDisCharge;
        $.ajax({
            url: baseURL + 'advancedsetting/queryVol?deviceCode='+deviceCode,
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
                var result=res.result;
                if(result == null){
                    volOverDisCharge=250
                    volCharge=40000
                }else{
                    volOverDisCharge =res.result.volOverDisCharge;
                    volCharge = res.result.volCharge;
                }
            }
        })

        $.ajax({
            url: baseURL + 'fun/device/getDeviceById?deviceId='+deviceId,
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
                console.log(res)
                console.log("实时数据")
                $("#rtia").html(res.data.runDay);
                var communicationType=res.data.communicationType
                if (communicationType == null ) {
                    communicationType = "";
                } else if(communicationType == 1) {
                    communicationType = "2G";
                }else if(communicationType == 2) {
                    communicationType = "Lora";
                }else if(communicationType == 3) {
                    communicationType = "NBlot";
                }

                var sslo1 = res.data.batteryVoltage;
                var sslo2 = res.data.photovoltaicCellVoltage;
                var sslo3 = res.data.chargingCurrent;
                var sslo4 = res.data.chargingPower;
                var sslo5 = res.data.loadVoltage;
                var sslo9 = res.data.batteryMargin;
                var sslo6 = res.data.loadCurrent;
                var sslo7 = res.data.loadPower;

                if(sslo2==null){
                    sslo2=""
                }
                if(sslo3==null){
                    sslo3=""
                }
                if(sslo4==null){
                    sslo4=""
                }
                if(sslo5==null){
                    sslo5=""
                }
                if(sslo6==null){
                    sslo6=""
                }
                if(sslo7==null){
                    sslo7=""
                }
                if(sslo1==null){
                    sslo1=""
                }
                if(sslo9 == null){
                    sslo9 = ""
                }

                $("#rxun").html(communicationType);
                $("#rben").html("1.0");
                $("#ruan").html(res.data.version);
                var batteryState=res.data.batteryState;
                if (batteryState == null ) {
                    batteryState = "--";
                } else if(batteryState == 1) {
                    batteryState = "欠压";
                }else if(batteryState == 2) {
                    batteryState = "正常";
                }else if(batteryState == 3) {
                    batteryState = "限压";
                } else if(batteryState == 4) {
                    batteryState = "超压";
                }else if(batteryState == 5) {
                    batteryState = "过温";
                }else if(batteryState == 6) {
                    batteryState = "激活";
                }else if(batteryState == 0) {
                    batteryState = "过放";
                }
                var photocellState=res.data.photocellState;
                if (photocellState == null ) {
                    photocellState = "--";
                } else if(photocellState == 1) {
                    photocellState = "光强";
                }else if(photocellState == 2) {
                    photocellState = "正在充电";
                }else if(photocellState == 0) {
                    photocellState = "光弱";
                }
                var loadState=res.data.loadState;
                if (loadState == null ) {
                    loadState = "--";
                } else if(loadState == 1) {
                    loadState = "开";
                }else if(loadState == 2) {
                    loadState = "开路保护";
                }else if(loadState == 3) {
                    loadState = "直通";
                } else if(loadState == 4) {
                    loadState = "短路保护";
                }else if(loadState == 5) {
                    loadState = "过载保护";
                }else if(loadState == 6) {
                    loadState = "过载警告";
                }else if(loadState == 0) {
                    loadState = "关";
                }

                $("#rzhuan").html(batteryState);//蓄电池状态
                $("#yula").css("width",parseInt(1*sslo9)+"%");//蓄电池电池余量
                $("#yula").html(sslo9+"%");
                $("#rdiay").css("width",parseInt(sslo1/((volCharge-volOverDisCharge)/100))+"%");//蓄电池电池电压
                $("#rdiay").html(sslo1+"V");
                $("#rguang").html(photocellState);//光电池状态
                var asa = (1*sslo1)/100;
                $("#rya").css("width",parseInt(1*(sslo2/asa))+"%");//光电池电压
                $("#rya").html(sslo2+"V");//光电池电压
                $("#rliu").css("width",parseInt(sslo3/(10/100))+"%");//光电池充电电流
                $("#rliu").html(sslo3+"A");
                $("#rdianl").css("width",parseInt(sslo4/(220/100))+"%");//光电池充电功率
                $("#rdianl").html(sslo4+"W");
                $("#rzhun").html(loadState);//负载状态
                $("#rfu").css("width",parseInt(sslo5/(60/100))+"%");//负载电压
                $("#rfu").html(sslo5+"V");
                $("#rful").css("width",parseInt(sslo6/(2/100))+"%");//负载电流
                $("#rful").html(sslo6+"A");
                $("#rfugon").css("width",parseInt(sslo7/(40/100))+"%");//负载功率
                $("#rfugon").html(sslo7+"W");

                var on0f=res.data.onOff
                var luminance=res.data.light;
                var lian_g=res.data.lightingDuration;
                var chen_g=res.data.morningHours;
                //实时数据负载开关
                // $('#in_p>input[name="category"]' == on0f).each(function(){
                //     console.log("333")
                //     $(this).prop("checked",true);
                //     clearInterval(t);
                // });
                // $('#in_p>input[name="category"]' != on0f).each(function(){
                //     console.log("4444")
                //     $(this).prop("checked",false);
                //     clearInterval(t);
                // });
                var youVal = res.data.onOff; // 1,2 把拿到的开关灯值赋给单选按钮
                $("input[name='category']").each(function(index) {
                    if ($("input[name='category']").get(index).value == youVal) {
                        $("input[name='category']").get(index).checked = true;
                    }
                });
                // //运输模式
                // $('#transport>input[name="tran"]' == on0f).each(function(){
                //     console.log("1111")
                //     $(this).prop("checked",true);
                //     clearInterval(t);
                // });
                // $('#transport>input[name="tran"]' != on0f).each(function(){
                //     console.log("2222")
                //     $(this).prop("checked",false);
                //     clearInterval(t);
                // });
                var tran_a = res.data.transport; // 1,2 把拿到的运输值赋给单选按钮
                $("input[name='tran']").each(function(index) {
                    if ($("input[name='tran']").get(index).value == tran_a) {
                        $("input[name='tran']").get(index).checked = true;
                    }
                });
                //实时数据亮度赋值
                $("#slideTest_r ").children().children(".layui-slider-bar").width((luminance/$(".progres").width()*$(".progres").width())+"%");
                layui.use('slider', function() {
                    var $ = layui.$
                        , slider = layui.slider;
                    slider.render({
                        elem: '#slideTest_r'
                        , theme: '#1E9FFF' //主题色
                        , value: res.data.light
                    });
                })
                $("#slideTest_r .layui-slider-wrap").css('left',luminance+ '%');

                //亮灯时长
                var lian1s = Number($("#r_one_teml").val()*60);
                var lian2s = Number($("#r_teml_sv").val()*60);
                var lian3s = Number($("#r_teml_th").val()*60);
                var lian4s = Number($("#r_teml_ng").val()*60);
                var lian5s = Number($("#r_teml_fif").val()*60);

                var lian1f = Number($("#r_teml_tw").val());
                var lian2f = Number($("#r_teml_eg").val());
                var lian3f = Number($("#r_teml_for").val());
                var lian4f = Number($("#r_teml_te").val());
                var lian5f = Number($("#r_teml_six").val());
                $("#rzyb").val(res.data.lightingDuration)
                $("#rac").val(res.data.morningHours);//晨亮时长
            }
        })
    }
    $(".mositn").css("display","none");
    $("#select1_b").click(function(){
        var sele=$("#select1_b option:selected").attr("class");
        console.log($("#select1_b option:selected"))
        if(sele == "1"){
            console.log(sele)
            console.log("kkkkkkkkkkkkkkkkkkk")
            $(".dinstn").css("display","none");
            $(".mositn").css("display","block");
            $("#d414").css("margin-top","2%!important")
        }else{
            $(".dinstn").css("display","block");
            $(".mositn").css("display","none");
            $(".kai_b").css("margin","5% 0px 0px 3%!important")
        }
    })


    //实时数据操作点击确定
    $("#confirm_Z").click(function(){
        var category = parseInt($('input:radio[name="category"]:checked').val());//实时数据负载开关
        var r_wena =parseInt($("#slideTest_r").children().children(".layui-slider-bar").width()/$("#slideTest_r").children(".layui-slider").width()*100);//亮度
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb = $('#rzyb').val();//亮度时长
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  $('#rac').val();//晨亮时长
        var r_wen_c = Math.ceil(r_wenc);
        var s_witch= parseInt($('input:radio[name="tran"]:checked').val());//运输开关
        equipment_b(category,r_wen_a,r_wen_b,r_wen_c,s_witch);//调用上传实时数据
    })
    //实时数据简易操作上传数据
    function equipment_b(category,r_wen_a,r_wen_b,r_wen_c,s_witch) {
        var data = {};
        var data1 = {};
        data1["deviceCodes"] = [deviceCode]
        data1["deviceType"] = 1
        data1["status"] = 2
        data["projectId"] = projectId;
        data["groupId"] = grod;
        data["deviceId"] = deviceId;
        data["deviceCode"] = deviceCode;
        data["onOff"] = category;
        data["lightingDuration"] = r_wen_b;
        data["morningHours"] = r_wen_c;
        data["light"] = r_wen_a;
        data["transport"] = s_witch;
        data1["qaKey"] = ["onOff","lightingDuration","morningHours","light","transport"]
        data1["value"] = [category,r_wen_b,r_wen_c,r_wen_a,s_witch]
        //简易操作后台接口传输数据
        $.ajax({
            url: baseURL + 'fun/device/updateOnOffByIds',
            contentType: "application/json;charset=UTF-8",
            type:"POST",
            data:JSON.stringify(data),
            success: function (res) {
                console.log(res)
                if(res.code == "200"){
                    layer.open({
                        title: '信息',
                        content:"操作成功",
                        skin: 'demo-class'
                    });
                }
            }
        })
        //硬件
        $.ajax({
            url:baseURL + 'fun/device/change',
            contentType: "application/json;charset=UTF-8",
            type:"POST",
            data: JSON.stringify(data1),
            success: function(res) {
                console.log(res)
            }
        })
    }

    //判断选项卡处于那页调取那页数据（切换时调用）
    $(".tab li").unbind('click');
    $(".tab li").click(function() {
        $(".tab li").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
        $(".option_a>.option").hide().eq($(this).index()).show();
        if($(this).attr("id") == "opt"){
            clearInterval(t);
            gao();
        }else if($(this).attr("id") == "hardware"){
            clearInterval(t);
            hardware();
        }else if($(this).attr("id") == "shuju"){
            shuju(deviceId,deviceCode)
            t=setInterval(function(){
                    shuju(deviceId,deviceCode)
            }, 60000);
        }
    });
    //点击负载开关按钮停止刷新实时数据页面数据点击确定后继续刷新
    $("#in_p>input,#slideTest_r,#rzyb,#rac").click(function(){
        clearInterval(t);
    })
    var loadWorkMode//负载工作模式
    var powerLoad //负载功率
    var timeTurnOn//开灯时刻
    var timeTurnON_a
    var timeTurnON_b
    var timeTurnOff//关灯时刻
    var timeTurnOff_a
    var timeTurnOff_b
    var time1//1时段时长
    var time2//2时段时长
    var time3//3时段时长
    var time4//4时段时长
    var time5//5时段时长
    var timeDown //晨亮时段时长

    var powerPeople1//时段1 有人功率
    var powerPeople2//时段2 有人功率
    var powerPeople3//时段3 有人功率
    var powerPeople4//时段4 有人功率
    var powerPeople5//时段5 有人功率
    var powerDawnPeople//晨亮 有人功率
    var powerSensor1//时段1 无人功率
    var powerSensor2//时段2 无人功率
    var powerSensor3//时段3无人功率
    var powerSensor4//时段4 无人功率
    var powerSensor5//时段5 无人功率
    var powerSensorDown //晨亮 无人功率

    var autoSleepTime//自动休眠时间
    var savingSwitch//智能节能开关
    var inductionLightOnDelay //感应亮灯时长
    var switchDelayTime //负载手动控制生效时长

    var vpv//光控电压
    var pvSwitch //光控关灯开灯
    var customeSwitch// 自定义
    var temControlSwitch //温控开关
    var lowPowerConsumption//低功耗
    var batType// 电池类型
    var ligntOnDuration//光控延迟时间长度
    var batStringNum//电池串数
    var volOverDisCharge//过放电压
    var volCharge//充电电压
    var tempCharge//充电温度范围(最大值  做高八位低八位处理)
    var tempDisCharge//放电温度范围（最大值做高八位低八位处理)）
    var tempCharge0 //充电温度范围(最小值做高八位低八位处理)
    var tempDisCharge25 //放电温度范围（最小值做高八位低八位处理)）
    var newTempCharge
    var newTempDisCharge
    var inspectionTime //巡检时间
    var icharge//充电电流
    // 第一阶段
    var firDownPower//一阶降压功率
    var firReducAmplitude//一阶降功率幅度
    // 第二阶段
    var twoDownPower//二阶降压功率
    var twoReducAmplitude //二阶降功率幅度
    // 第三阶段
    var threeDownPower//三阶降压功率
    var threeReducAmplitude//三阶降功率幅度
    var se_a
    var se_b
    var se_c
    var se_d
    var se_e
    var se_f
    var inductionSwitch
    function gao(){
        //取消
        $("#r_cl").click(function(){
            window.location.reload();
        })
//高级设置自定义
        $("#custom").click(function(){
            var select=$("#custom option:selected").attr("class");
            if(select == "1"){
                $(".shade_project,.shade_b_project,.Bian_j").show();
            }else{
                $(".shade_project,.shade_b_project,.Bian_j").hide();
            }
        })
    //电池类型
        $("#r_select_b").click(function(){
            var select=$("#r_select_b option:selected").attr("class");
            if(select == "4"){
                var recharge=$("#r_teml_forl").val()*4.1
                var recharge_a= recharge.toFixed(2);
                var overfall=$("#r_teml_forl").val()*3
                var overfall_a=overfall.toFixed(2);
                $("#r_inpl_one").val(overfall_a);
                $("#chong").val(recharge_a);
            }else if(select == "3"){
                var recharge=$("#r_teml_forl").val()*3.6
                var recharge_a= recharge.toFixed(2);
                var overfall=$("#r_teml_forl").val()*2.8
                var overfall_a=overfall.toFixed(2);
                $("#r_inpl_one").val(overfall_a);
                $("#chong").val(recharge_a);
            }
        })
    //感应多选
        $(".se_a,.se_b,.se_c,.se_d,.se_e,.se_f").click(function(){
            if( $(this).attr("checked") == 'checked'){
                $(this).attr("checked",false);
            }else{
                $(this).attr("checked",true);
            }
        });
        //有人无人
        $(".someone,.unmanned").hover(function(){
            $(this).children("span").show();
        },function(){
            $(this).children("span").hide();
        });
        var flag=true
        if(deviceCode != ""){
            //设备高级设置数据渲染
            console.log("设备高级设置上传");
            var ur=baseURL + 'advancedsetting/queryDevAdvInfo?deviceCode='+deviceCode+"&groupId="+grod
            url(ur)
            $("input").blur(function(){
                if($(this).val() == ""){
                    alert("输入不能为空");
                    flag=false
                }else{
                    flag=true
                }
            })
            //设备保存值
            $("#r_bttn").unbind('click');
            $("#r_bttn").click(function () {
                if(!flag){
                    return
                }
                uploading()
                console.log("lllllllll")
                console.log(lowPowerConsumption)
                var qaKey=["loadWorkMode","powerLoad","timeTurnOn","timeTurnOff","time1",
                    "time2","time3","time4","time5","timeDown","powerPeople1","powerPeople2","powerPeople3","powerPeople4","powerPeople5",
                    "powerDawnPeople","powerSensor1","powerSensor2","powerSensor3","powerSensor4","powerSensor5","powerSensorDown","autoSleepTime","vpv",
                    "pvSwitch","batType","batStringNum","volOverDisCharge","volCharge","tempCharge","tempDisCharge","inspectionTime","iCharge","switchDelayTime","firDownPower",
                    "twoDownPower","threeDownPower","firReducAmplitude","twoReducAmplitude","threeReducAmplitude","ligntOnDuration","savingSwitch","inductionLightOnDelay","inductionSwitch","lowPowerConsumption"];
                var value=[loadWorkMode,powerLoad,timeTurnON_b,timeTurnOff_b,time1,
                    time2,time3,time4,time5,timeDown,powerPeople1,powerPeople2,powerPeople3,powerPeople4,powerPeople5,
                    powerDawnPeople,powerSensor1,powerSensor2,powerSensor3,powerSensor4,powerSensor5,powerSensorDown,autoSleepTime,vpv,
                    pvSwitch,batType,batStringNum,volOverDisCharge,volCharge,newTempCharge,newTempDisCharge,inspectionTime,icharge,switchDelayTime,firDownPower,
                    twoDownPower,threeDownPower,firReducAmplitude,twoReducAmplitude,threeReducAmplitude,ligntOnDuration,savingSwitch,inductionLightOnDelay,inductionSwitch,lowPowerConsumption];
                $.ajax({
                    url:baseURL + 'advancedsetting/updateDevice',
                    type:"POST",
                    data:
                        "&deviceCode="+ deviceCode + //设备code
                        "&groupId="+ grod + //分组id
                        "&loadWorkMode="+ loadWorkMode + //负载工作模式
                        "&powerLoad="+ powerLoad +//负载功率
                        "&timeTurnOn="+ timeTurnON_b + //开灯时刻
                        "&timeTurnOff="+ timeTurnOff_b + //关灯时刻
                        "&time1="+ time1 +//1时段时长
                        "&time2="+ time2 +//2时段时长
                        "&time3="+ time3 +//3时段时长
                        "&time4="+ time4 +//4时段时长
                        "&time5="+ time5 +//5时段时长
                        "&timeDown="+ timeDown +//晨亮时段时长
                        "&powerPeople1="+ powerPeople1 +//时段1 有人功率
                        "&powerPeople2="+ powerPeople2 +//时段2 有人功率
                        "&powerPeople3="+ powerPeople3 +//时段3 有人功率
                        "&powerPeople4="+ powerPeople4 +//时段4 有人功率
                        "&powerPeople5="+ powerPeople5 +//时段5 有人功率
                        "&powerDawnPeople="+ powerDawnPeople+//晨亮 有人功率
                        "&powerSensor1="+ powerSensor1 +//时段1 无人功率
                        "&powerSensor2="+ powerSensor2 +//时段2 无人功率
                        "&powerSensor3="+ powerSensor3 +//时段3无人功率
                        "&powerSensor4="+ powerSensor4 +//时段4 无人功率
                        "&powerSensor5="+ powerSensor5 +//时段5 无人功率
                        "&powerSensorDown="+ powerSensorDown +//晨亮 无人功率
                        "&savingSwitch="+ savingSwitch + //节能开关
                        "&autoSleepTime="+ autoSleepTime + //自动休眠时间长度
                        "&vpv="+ vpv + //光控电压
                        "&pvSwitch="+ pvSwitch +//光控关灯开灯
                        "&lowPowerConsumption="+lowPowerConsumption+
                        "&customeSwitch="+ customeSwitch +//温控开关
                        "&temControlSwitch="+ temControlSwitch +//自定义
                        "&ligntOnDuration="+ ligntOnDuration +//光控延迟时间长度
                        "&batType="+ batType + // 电池类型 0:未设置；1：胶体电池；2：铅酸电池； 3磷酸铁锂电池；4：三元锂电池
                        "&batStringNum="+batStringNum +//电池串数
                        "&volOverDisCharge="+ volOverDisCharge + //过放电压
                        "&volCharge="+ volCharge +//充电电压
                        "&tempCharge="+ newTempCharge +//充电温度范围(最大值和最小值做高八位低八位处理)
                        "&tempDisCharge="+ newTempDisCharge +//放电温度范围（最大值和最小值做高八位低八位处理)）
                        "&inspectionTime="+ inspectionTime+//巡检时间
                        "&iCharge="+ icharge + //充电电流
                        "&inductionSwitch="+ inductionSwitch +//感应开关
                        "&inductionLightOnDelay="+ inductionLightOnDelay +//感应后的亮灯延时
                        "&switchDelayTime="+ switchDelayTime + //开灯延时
                        "&firDownPower="+ firDownPower + //一阶降压功率
                        "&twoDownPower="+ twoDownPower + //二阶降压功率
                        "&threeDownPower="+ threeDownPower + //三阶降压功率
                        "&firReducAmplitude="+ firReducAmplitude + //一阶降功率幅度
                        "&twoReducAmplitude="+ twoReducAmplitude + //二阶降功率幅度
                        "&threeReducAmplitude="+ threeReducAmplitude //三阶降功率幅度
                    ,
                    success: function(res){
                        console.log("设备设置上传成功");
                        console.log(res);
                        if(res.code == 500){
                            alert(res.msg);
                        }else if(res.code == 200){
                            alert("保存成功");
                            //    硬件设备
                            var dev=[];
                            dev.push(deviceCode);
                            $.ajax({
                                url:baseURL + 'fun/device/change',
                                contentType: "application/json;charset=UTF-8",
                                type:"POST",
                                data: JSON.stringify({
                                    "deviceCodes":dev , //需要修改的设备code   /0
                                    "qaKey": qaKey, //需要修改的参数键
                                    "value": value, //需要修改的参数值
                                    "deviceType":type, //设备类型   /1
                                    "status":2
                                }),
                                success: function(res) {
                                    console.log("硬件分组返回");
                                    console.log(res);
                                }
                            })

                        }
                    }

                });
            })
        }else {
            console.log("分组高级设置上传");
            var ur=baseURL + 'advancedsetting/settingInfo?groupId='+grod
            url(ur)
            $("input").blur(function(){
                if($(this).val() == ""){
                    alert("输入不能为空");
                    flag=false
                }else{
                    flag=true
                }
            })
            //保存分组数据
            $("#r_bttn").unbind('click');
            $("#r_bttn").click(function () {
                if(!flag){
                    return
                }
                uploading()
                var qaKey=["loadWorkMode","powerLoad","timeTurnOn","timeTurnOff","time1",
                    "time2","time3","time4","time5","timeDown","powerPeople1","powerPeople2","powerPeople3","powerPeople4","powerPeople5",
                    "powerDawnPeople","powerSensor1","powerSensor2","powerSensor3","powerSensor4","powerSensor5","powerSensorDown","autoSleepTime","vpv",
                    "pvSwitch","batType","batStringNum","volOverDisCharge","volCharge","tempCharge","tempDisCharge","inspectionTime","iCharge","switchDelayTime","firDownPower",
                    "twoDownPower","threeDownPower","firReducAmplitude","twoReducAmplitude","threeReducAmplitude","ligntOnDuration","savingSwitch","inductionLightOnDelay","inductionSwitch","lowPowerConsumption"];
                var value=[loadWorkMode,powerLoad,timeTurnON_b,timeTurnOff_b,time1,
                    time2,time3,time4,time5,timeDown,powerPeople1,powerPeople2,powerPeople3,powerPeople4,powerPeople5,
                    powerDawnPeople,powerSensor1,powerSensor2,powerSensor3,powerSensor4,powerSensor5,powerSensorDown,autoSleepTime,vpv,
                    pvSwitch,batType,batStringNum,volOverDisCharge,volCharge,newTempCharge,newTempDisCharge,inspectionTime,icharge,switchDelayTime,firDownPower,
                    twoDownPower,threeDownPower,firReducAmplitude,twoReducAmplitude,threeReducAmplitude,ligntOnDuration,savingSwitch,inductionLightOnDelay,inductionSwitch,lowPowerConsumption];
                $.ajax({
                    url:baseURL + 'advancedsetting/updateGroup',
                    type:"POST",
                    data:
                        "&groupId="+ grod + //分组id
                        "&loadWorkMode="+ loadWorkMode + //负载工作模式
                        "&powerLoad="+ powerLoad +//负载功率
                        "&timeTurnOn="+ timeTurnON_b + //开灯时刻
                        "&timeTurnOff="+ timeTurnOff_b + //关灯时刻
                        "&time1="+ time1 +//1时段时长
                        "&time2="+ time2 +//2时段时长
                        "&time3="+ time3 +//3时段时长
                        "&time4="+ time4 +//4时段时长
                        "&time5="+ time5 +//5时段时长
                        "&timeDown="+ timeDown +//晨亮时段时长
                        "&powerPeople1="+ powerPeople1 +//时段1 有人功率
                        "&powerPeople2="+ powerPeople2 +//时段2 有人功率
                        "&powerPeople3="+ powerPeople3 +//时段3 有人功率
                        "&powerPeople4="+ powerPeople4 +//时段4 有人功率
                        "&powerPeople5="+ powerPeople5 +//时段5 有人功率
                        "&powerDawnPeople="+ powerDawnPeople+//晨亮 有人功率
                        "&powerSensor1="+ powerSensor1 +//时段1 无人功率
                        "&powerSensor2="+ powerSensor2 +//时段2 无人功率
                        "&powerSensor3="+ powerSensor3 +//时段3无人功率
                        "&powerSensor4="+ powerSensor4 +//时段4 无人功率
                        "&powerSensor5="+ powerSensor5 +//时段5 无人功率
                        "&powerSensorDown="+ powerSensorDown +//晨亮 无人功率
                        "&savingSwitch="+ savingSwitch + //节能开关
                        "&autoSleepTime="+ autoSleepTime + //自动休眠时间长度
                        "&vpv="+ vpv + //光控电压
                        "&pvSwitch="+ pvSwitch +//光控关灯开灯
                        "&lowPowerConsumption="+lowPowerConsumption+
                        "&temControlSwitch="+ temControlSwitch +//光控关灯开灯
                        "&customeSwitch="+ customeSwitch +//光控关灯开灯
                        "&ligntOnDuration="+ ligntOnDuration +//光控延迟时间长度
                        "&batType="+ batType + // 电池类型 0:未设置；1：胶体电池；2：铅酸电池； 3磷酸铁锂电池；4：三元锂电池
                        "&batStringNum="+batStringNum +//电池串数
                        "&volOverDisCharge="+ volOverDisCharge + //过放电压
                        "&volCharge="+ volCharge +//充电电压
                        "&tempCharge="+ newTempCharge +//充电温度范围(最大值和最小值做高八位低八位处理)
                        "&tempDisCharge="+ newTempDisCharge +//放电温度范围（最大值和最小值做高八位低八位处理)）
                        "&inspectionTime="+ inspectionTime+//巡检时间
                        "&iCharge="+ icharge + //充电电流
                        "&inductionSwitch="+ inductionSwitch +//感应开关
                        "&inductionLightOnDelay="+ inductionLightOnDelay +//感应后的亮灯延时switchDelayTime
                        "&switchDelayTime="+ switchDelayTime + //开关灯延时
                        "&firDownPower="+ firDownPower + //一阶降压功率
                        "&twoDownPower="+ twoDownPower + //二阶降压功率
                        "&threeDownPower="+ threeDownPower + //三阶降压功率
                        "&firReducAmplitude="+ firReducAmplitude + //一阶降功率幅度
                        "&twoReducAmplitude="+ twoReducAmplitude + //二阶降功率幅度
                        "&threeReducAmplitude="+ threeReducAmplitude //三阶降功率幅度
                    ,
                    success: function(res){
                        console.log("分组高级设置上传成功")
                        if(res.code == 500){
                            alert(res.msg)
                        }else if(res.code == 200) {
                            alert("分组保存成功");
                            // 硬件分组
                            $.ajax({
                                url:baseURL + 'fun/device/change',
                                contentType: "application/json;charset=UTF-8",
                                type:"POST",
                                data: JSON.stringify({
                                    "qaKey": qaKey, //需要修改的参数键
                                    "value": value, //需要修改的参数值
                                    "deviceType": "1", //设备类型   /1
                                    "groupId":grod,
                                    "status":2
                                }),
                                success: function(res) {
                                    console.log("硬件分组返回");
                                    console.log(res);
                                }
                            })

                        }
                    }
                });
            })
        }
        //上传高级设置数据
        function uploading() {
            //负载
             loadWorkMode = parseInt($("#select1_b option:selected").attr("class"));   //负载工作模式
             powerLoad = parseInt($("#r_Load_power").val()*100); //负载功率
             timeTurnOn=$("#d414").val(); //开灯时刻
             timeTurnON_a=timeTurnOn.split(":");
             timeTurnON_b=Number(timeTurnON_a[0]*60)+Number(timeTurnON_a[1]);
             timeTurnOff=$("#d4141").val(); //关灯时刻
             timeTurnOff_a=timeTurnOff.split(":");
             timeTurnOff_b=Number(timeTurnOff_a[0]*60)+Number(timeTurnOff_a[1]);
             time1= Number($("#r_one_teml").val())*60+Number($("#r_teml_tw").val());//1时段时长
             time2= Number($("#r_teml_sv").val())*60+Number($("#r_teml_eg").val());  //2时段时长
             time3= Number($("#r_teml_th").val())*60+Number($("#r_teml_for").val());//3时段时长
             time4= Number($("#r_teml_ng").val())*60+Number($("#r_teml_te").val());//4时段时长
             time5=Number($("#r_teml_fif").val())*60+Number($("#r_teml_six").val());//5时段时长
             timeDown= Number($("#r_teml_el").val())*60+Number($("#r_teml_egl").val());//晨亮时段时长
            //关灯时段
             powerPeople1=parseInt($("#slideTest4").children().children(".layui-slider-bar").width()/$("#slideTest4").children(".layui-slider").width()*100); //时段1 有人功率
             powerPeople2=parseInt($("#slideTest12").children().children(".layui-slider-bar").width()/$("#slideTest12").children(".layui-slider").width()*100);//时段2 有人功率
             powerPeople3=parseInt($("#slideTest8").children().children(".layui-slider-bar").width()/$("#slideTest8").children(".layui-slider").width()*100); //时段3 有人功率
             powerPeople4=parseInt($("#slideTest10").children().children(".layui-slider-bar").width()/$("#slideTest10").children(".layui-slider").width()*100);//时段4 有人功率
             powerPeople5=parseInt($("#slideTest13").children().children(".layui-slider-bar").width()/$("#slideTest13").children(".layui-slider").width()*100);//时段5 有人功率
             powerDawnPeople=parseInt($("#slideTest6").children().children(".layui-slider-bar").width()/$("#slideTest6").children(".layui-slider").width()*100);//晨亮 有人功率
             powerSensor1=parseInt($("#slideTest5").children().children(".layui-slider-bar").width()/$("#slideTest5").children(".layui-slider").width()*100);//时段1 无人功率
             powerSensor2=parseInt($("#slideTest16").children().children(".layui-slider-bar").width()/$("#slideTest16").children(".layui-slider").width()*100);//时段2 无人功率
             powerSensor3=parseInt($("#slideTest9").children().children(".layui-slider-bar").width()/$("#slideTest9").children(".layui-slider").width()*100);//时段3无人功率
             powerSensor4=parseInt($("#slideTest11").children().children(".layui-slider-bar").width()/$("#slideTest11").children(".layui-slider").width()*100); //时段4 无人功率
             powerSensor5=parseInt($("#slideTest14").children().children(".layui-slider-bar").width()/$("#slideTest14").children(".layui-slider").width()*100);//时段5 无人功率
             powerSensorDown=parseInt($("#slideTest7").children().children(".layui-slider-bar").width()/$("#slideTest7").children(".layui-slider").width()*100); //晨亮 无人功率
             //光电池
             autoSleepTime =Number($("#r_inpl_one2").val()) ;//自动休眠时间
             savingSwitch=parseInt($("#custom option:selected").attr("class"));//智能节能开关
             inductionLightOnDelay = parseInt($("#r_teml_ma").val());//感应亮灯时长
             switchDelayTime = Number($("#chong3").val());//负载手动控制生效时长
             //蓄电池
             vpv=parseInt($("#r_teml_tgl").val()*100);//光控电压
             pvSwitch=parseInt($("#r_btl_th>.r_btl_th[name='sexa4']:checked").val()); //光控关灯开灯
             temControlSwitch=parseInt($("#r_btl_th1>.r_btl_th[name='sexa2']:checked").val()); //温控开关

            lowPowerConsumption=parseInt($("#r_btl_thr>.r_btl_th[name='sexdg']:checked").val()); //低功耗
            console.log("++++++++++++++")
            console.log(temControlSwitch)
             customeSwitch=parseInt($("#r_btl_th0>.r_btl_th[name='sexa1']:checked").val()); //自定义
             batType=parseInt($("#r_select_b option:selected").attr("class"));// 电池类型
             ligntOnDuration=parseInt($("#slideTest_m").children().children(".layui-slider-tips").html())//光控延迟时间长度
             if(isNaN(ligntOnDuration)){
                ligntOnDuration=5
             }
             batStringNum= parseInt($("#r_teml_forl").val());//电池串数
             volOverDisCharge=parseInt($("#r_inpl_one").val()*100);//过放电压
             volCharge=parseInt($("#chong").val()*100);//充电电压
             tempCharge=parseInt($("#r_teml_thglte").val()); //充电温度范围(最大值  做高八位低八位处理)
             tempDisCharge=parseInt($("#r_teml_thglt").val()); //放电温度范围（最大值做高八位低八位处理)）
             tempCharge0=parseInt($("#r_teml_thete").val()); //充电温度范围(最小值做高八位低八位处理)
             tempDisCharge25=parseInt($("#r_teml_thet").val()); //放电温度范围（最小值做高八位低八位处理)）
             newTempCharge = ((tempCharge& 0xFF)<<8) + (tempCharge0 & 0xFF);
             newTempDisCharge = ((tempDisCharge & 0xFF)<<8) + (tempDisCharge25 & 0xFF);
             inspectionTime=parseInt($("#r_teml_thgl option:selected").attr("class")); //巡检时间
             icharge= parseInt($("#r_inpl_tw").val())*100;//充电电流
            if(isNaN(icharge)){
                icharge=20
            }
            // 第一阶段
             firDownPower = parseInt($("#slideTest_a").val()*100);//一阶降压功率
             firReducAmplitude = parseInt($("#slideTest_b").children().children(".layui-slider-bar").width()/$("#slideTest_b").children(".layui-slider").width()*100)//一阶降功率幅度
            if(isNaN(firReducAmplitude)){
                firReducAmplitude=60
            }
            // 第二阶段
             twoDownPower = parseInt($("#slideTest_c").val()*100);//二阶降压功率
             twoReducAmplitude = parseInt($("#slideTest_d").children().children(".layui-slider-bar").width()/$("#slideTest_d").children(".layui-slider").width()*100)//二阶降功率幅度
            if(isNaN(twoReducAmplitude)){
                twoReducAmplitude=40
            }
            // 第三阶段
             threeDownPower = parseInt($("#slideTest_e").val()*100);//三阶降压功率
             threeReducAmplitude = parseInt($("#slideTest_f").children().children(".layui-slider-bar").width()/$("#slideTest_f").children(".layui-slider").width()*100)//三阶降功率幅度
            if(isNaN(threeReducAmplitude)){
                threeReducAmplitude=20
            }

            if($(".se_a[type='checkbox']").attr('checked') == "checked"){
                se_a=1
            }else{
                se_a=0
            }
            if($(".se_b[type='checkbox']").attr('checked') == "checked"){
                se_b=2
            }else{
                se_b=0
            }
            if($(".se_c[type='checkbox']").attr('checked') == "checked"){
                se_c=4
            }else{
                se_c=0
            }
            if($(".se_d[type='checkbox']").attr('checked') == "checked"){
                se_d=8
            }else{
                se_d=0
            }
            if($(".se_e[type='checkbox']").attr('checked') == "checked"){
                se_e=16
            }else{
                se_e=0
            }
            if($(".se_f[type='checkbox']").attr('checked') == "checked"){
                se_f=32
            }else{
                se_f=0
            }
            inductionSwitch=se_a+se_b+se_c+se_d+se_e+se_f
            var sex = "";
            $.each($("input[name='sexa']"),function(i,n){//参数i为遍历索引值，n为当前的遍历对象.
                if($(this).is(":checked")){
                    sex = $(this).val();
                }
            });
            if(sex == 0){
                inductionSwitch=0
            }
        }
        //渲染高级设置数据
        function url(ur){
            $.ajax({
                url:ur,
                contentType: "application/json;charset=UTF-8",
                type:"get",
                data: {},
                success: function(res){
                    if(res.info == "" ||res.info== null){
                        laiui(0,0,0,0,0,0,0,0,0,0,0,0,0,0)
                        return
                    }else{
                        console.log("设备高级设置参数获取1")
                        console.log(res)
                        var loadWorkMode=res.info.loadWorkMode;
                        if(loadWorkMode == 0){
                            loadWorkMode="定时模式";
                        }else if(loadWorkMode == 1){
                            loadWorkMode="光控模式";
                        }else if(loadWorkMode == 3){
                            loadWorkMode="常开";
                        }else if(loadWorkMode == 4){
                            loadWorkMode="常关";
                        }else if(loadWorkMode == 5){
                            loadWorkMode="测试";
                        }
                        $("#select1_b").val(loadWorkMode);//负载工作模式
                        if(loadWorkMode =="光控模式"){
                            $(".dinstn").css("display","none");
                            $(".mositn").css("display","block");
                        }
                        $("#r_Load_power").val(res.info.powerLoad/100);// 负载功率
                        var turnon=parseInt(res.info.timeTurnOn/60);
                        var turnona=res.info.timeTurnOn-(turnon*60);
                        var turnonb=turnon+":"+turnona
                        $("#d414").val(turnonb) ; //开灯时刻
                        var timeTurnOff=parseInt(res.info.timeTurnOff/60);
                        var timeTurnOff_a=res.info.timeTurnOff-(timeTurnOff*60);
                        var timeTurnOff_b=timeTurnOff+":"+timeTurnOff_a
                        $("#d4141").val(timeTurnOff_b);
                        var time1=parseInt(res.info.time1/60);
                        var time1_a=res.info.time1-(time1*60);
                        $("#r_one_teml").val(time1);   //一阶段
                        $("#r_teml_tw").val(time1_a);
                        var time2=parseInt(res.info.time2/60);
                        var time2_a=res.info.time2-(time2*60);
                        $("#r_teml_sv").val(time2);//二阶段
                        $("#r_teml_eg").val(time2_a);
                        var time3=parseInt(res.info.time3/60);
                        var time3_a=res.info.time3-(time3*60);
                        $("#r_teml_th").val(time3);//三阶段
                        $("#r_teml_for").val(time3_a);
                        var time4=parseInt(res.info.time4/60);
                        var time4_a=res.info.time4-(time4*60);
                        $("#r_teml_ng").val(time4);//4阶段
                        $("#r_teml_te").val(time4_a);
                        var time5=parseInt(res.info.time5/60);
                        var time5_a=res.info.time5-(time5*60);
                        $("#r_teml_fif").val(time5);//5阶段
                        $("#r_teml_six").val(time5_a);
                        var timeDown=parseInt(res.info.timeDown/60);
                        var timeDown_a=res.info.timeDown-(timeDown*60);
                        $("#r_teml_el").val(timeDown);//晨亮时段时长
                        $("#r_teml_egl").val(timeDown_a);
                        $("#r_teml_tgl").val(res.info.vpv/100);//光控电压
                        $("#r_teml_forl").val(res.info.batStringNum); //电池串数

                        var batType=res.info.batType;
                        if(batType == 3){
                            batType="磷酸铁锂电池";
                        }else if(batType == 4){
                            batType="三元锂电池";
                        }
                        $("#r_select_b").val(batType);//电池类型

                        var savingSwitch=res.info.savingSwitch;
                        if(savingSwitch == 0){
                            savingSwitch="关"
                        }else if(savingSwitch == 1){
                            savingSwitch="自定义"
                        }else if(savingSwitch == 2){
                            savingSwitch="智能1级"
                        }else if(savingSwitch == 3){
                            savingSwitch="智能2级"
                        }else if(savingSwitch == 4){
                            savingSwitch="智能3级"
                        }
                        $("#custom").val(savingSwitch);//智能节能开关
                        $("#r_inpl_one").val(res.info.volOverDisCharge/100);//过放电压
                        $("#r_inpl_tw").val(res.info.icharge/100);//充电电流
                        $("#chong").val(res.info.volCharge/100);//充电电压
                        $("#r_teml_ma").val(res.info.inductionLightOnDelay) ; //人体感应功能开关
                        var inspectionTime=res.info.inspectionTime+"分钟";
                        $("#r_teml_thgl").val(inspectionTime);//巡检
                        var pvSwitch=res.info.pvSwitch;
                        var customeSwitch=res.info.customeSwitch;
                        var temControlSwitch=res.info.temControlSwitch;
                         if(pvSwitch ==1 ){
                             $("#sexa4").attr("checked",true);
                             $("#sexa4_a").attr("checked",false);
                         }else{
                             $("#sexa4_a").attr("checked",true);
                             $("#sexa4").attr("checked",false);
                         }
                         var lowPowerConsumption = res.info.lowPowerConsumption;//低功耗

                        if(lowPowerConsumption ==1 ){
                            $("#sexdg").attr("checked",true);
                            $("#sexdg_a").attr("checked",false);
                        }else{
                            $("#sexdg_a").attr("checked",true);
                            $("#sexdg").attr("checked",false);
                        }
                         if(customeSwitch==0){
                             $(".nqd").trigger('click');
                             $(".nqd").attr("checked",true);
                             $(".nqde").attr("checked",false);
                         }else{
                             $(".nqde").attr("checked",true);
                             $(".nqd").attr("checked",false);
                         }

                        if(temControlSwitch==0){
                            $(".kudxe").trigger('click');
                            $(".kudxe").attr("checked",true);
                            $(".kudx").attr("checked",false);
                        }else{
                            $(".kudx").attr("checked",true);
                            $(".kudxe").attr("checked",false);
                        }

                        var tempCharge=parseInt(res.info.tempCharge/60);
                        var tempCharge_a=res.info.tempCharge-(tempCharge*60);
                        $("#r_teml_thglr").val(tempCharge);//充电时间范围
                        $("#r_teml_thelr").val(tempCharge_a);
                        var tempDisCharge=parseInt(res.info.tempDisCharge/60);
                        var tempDisCharge_a=res.info.tempDisCharge-(tempDisCharge*60);
                        $("#r_teml_thglt").val(tempDisCharge);//放电时间范围
                        $("#r_teml_thet").val(tempDisCharge_a);

                        var newTempCharge2=  (res.info.tempCharge >> 8) & 0xFF;
                        if(newTempCharge2 > 127){
                            newTempCharge2 |= 0xFFFFFF00;
                        }
                        var newTempCharge1 = res.info.tempCharge & 0xFF;

                        var newTempDisCharge2 = (res.info.tempDisCharge >> 8) & 0xFF;
                        if(newTempDisCharge2 > 127){
                            newTempDisCharge2 |= 0xFFFFFF00;
                        }
                        var newTempDisCharge1 = res.info.tempDisCharge&0xFF;

                        var inductionLightOnDelay
                        if(res.info.inductionLightOnDelay== null || res.info.inductionLightOnDelay==""){
                            inductionLightOnDelay="1";
                        }else{
                            inductionLightOnDelay=res.info.inductionLightOnDelay;
                        }
                        $("#r_teml_ma").val(inductionLightOnDelay)//人体感应后的亮灯延时时间
                        $("#r_teml_thglte").val(newTempCharge2);
                        $("#r_teml_thete").val(newTempCharge1);
                        $("#r_teml_thglt").val(newTempDisCharge2);
                        $("#r_teml_thet").val(newTempDisCharge1);
                        //有人无人功率
                        var a=res.info.inductionSwitch
                        var c=a.toString(2);
                        var b ="";
                        for(var i = c.length; i > 0; i--) {
                            b += c.charAt(i - 1);
                        }
                        if(b.slice(0,1) == "1"){
                            $(".se_a").attr("checked",true);
                        }else{
                            $(".se_a").attr("checked",false);
                        }
                        if(b.slice(1,2) == "1"){
                            $(".se_b").attr("checked",true);
                        }else{
                            $(".se_b").attr("checked",false);
                        }
                        if(b.slice(2,3) == "1"){
                            $(".se_c").attr("checked",true);
                        }else{
                            $(".se_c").attr("checked",false);
                        }
                        if(b.slice(3,4) == "1"){
                            $(".se_d").attr("checked",true);
                        }else{
                            $(".se_d").attr("checked",false);
                        }
                        if(b.slice(4,5) == "1"){
                            $(".se_e").attr("checked",true);
                        }else{
                            $(".se_e").attr("checked",false);
                        }
                        if(b.slice(5,6) == "1"){
                            $(".se_f").attr("checked",true);
                        }else{
                            $(".se_f").attr("checked",false);
                        }

                        $("#slideTest4").children().children(".layui-slider-bar").css("height",res.info.powerPeople1+"%");//灯亮时段1有人
                        $("#slideTest4").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople1+"%");

                        $("#slideTest5").children().children(".layui-slider-bar").css("height",res.info.powerSensor1+"%");//灯亮时段1无人
                        $("#slideTest5").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor1+"%");

                        $("#slideTest12").children().children(".layui-slider-bar").css("height",res.info.powerPeople2+"%");//灯亮时段2有人
                        $("#slideTest12").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople2+"%");

                        $("#slideTest16").children().children(".layui-slider-bar").css("height",res.info.powerSensor2+"%");//灯亮时段2无人
                        $("#slideTest16").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor2+"%");

                        $("#slideTest8").children().children(".layui-slider-bar").css("height",res.info.powerPeople3+"%");//灯亮时段3有人
                        $("#slideTest8").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople3+"%");

                        $("#slideTest9").children().children(".layui-slider-bar").css("height",res.info.powerSensor3+"%");//灯亮时段3无人
                        $("#slideTest9").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor3+"%");

                        $("#slideTest10").children().children(".layui-slider-bar").css("height",res.info.powerPeople4+"%");//灯亮时段4有人
                        $("#slideTest10").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople4+"%");

                        $("#slideTest11").children().children(".layui-slider-bar").css("height",res.info.powerSensor4+"%");//灯亮时段4无人
                        $("#slideTest11").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor4+"%");

                        $("#slideTest13").children().children(".layui-slider-bar").css("height",res.info.powerPeople5+"%");//灯亮时段5有人
                        $("#slideTest13").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople5+"%");
                        $("#slideTest13").children().children().children(".layui-input").val(res.info.powerPeople5);
                        $("#slideTest14").children().children(".layui-slider-bar").css("height",res.info.powerSensor5+"%");//灯亮时段5无人
                        $("#slideTest14").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor5+"%");

                        $("#slideTest6").children().children(".layui-slider-bar").css("height",res.info.powerDawnPeople+"%");//晨亮有人
                        $("#slideTest6").children().children(".layui-slider-wrap").css("bottom",res.info.powerDawnPeople+"%");

                        $("#slideTest7").children().children(".layui-slider-bar").css("height",res.info.powerSensorDown+"%");//晨亮无人
                        $("#slideTest7").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensorDown+"%");

                        $("#r_inpl_one2").val(res.info.autoSleepTime);
                        $("#slideTest_a").val(res.info.firDownPower/100);//一阶前
                        $("#slideTest_b").children().children(".layui-slider-bar").css("width",res.info.firReducAmplitude+"%");//一阶后
                        $("#slideTest_b").children().children(".layui-slider-wrap").css("left",res.info.firReducAmplitude+"%");

                        $("#slideTest_c").val(res.info.twoDownPower/100);//2阶前
                        $("#slideTest_d").children().children(".layui-slider-bar").css("width",res.info.twoReducAmplitude+"%");//2阶后
                        $("#slideTest_d").children().children(".layui-slider-wrap").css("left",res.info.twoReducAmplitude+"%");

                        $("#slideTest_e").val(res.info.threeDownPower/100);//3阶前
                        $("#slideTest_f").children().children(".layui-slider-bar").css("width",res.info.threeReducAmplitude+"%");//3阶后
                        $("#slideTest_f").children().children(".layui-slider-wrap").css("left",res.info.threeReducAmplitude+"%");

                        $("#chong3").val(res.info.switchDelayTime);
                        $("#slideTest_m").children().children(".layui-slider-bar").css("width",res.info.ligntOnDuration+"%");//光控延时时间
                        $("#slideTest_m").children().children(".layui-slider-wrap").css("left",res.info.ligntOnDuration+"%");

                        var powerPeople1=res.info.powerPeople1;
                        var powerSensor1=res.info.powerSensor1;
                        var powerPeople2=res.info.powerPeople2;
                        var powerSensor2=res.info.powerSensor2;
                        var powerPeople3=res.info.powerPeople3;
                        var powerSensor3=res.info.powerSensor3;
                        var powerPeople4=res.info.powerPeople4;
                        var powerSensor4=res.info.powerSensor4;
                        var powerPeople5=res.info.powerPeople5;
                        var powerSensor5=res.info.powerSensor5;
                        var powerDawnPeople=res.info.powerDawnPeople;
                        var powerSensorDown=res.info.powerSensorDown;
                        var switchDelayTime=res.info.switchDelayTime;
                        var ligntOnDuration=res.info.ligntOnDuration;
                        laiui(powerPeople1,powerSensor1,powerPeople2,powerSensor2,powerPeople3,powerSensor3,powerPeople4,powerSensor4,powerPeople5,powerSensor5,
                            powerDawnPeople,powerSensorDown,switchDelayTime,ligntOnDuration)
                        //判断人体感应开关是否开关
                        if(a == 0){
                            $(".sdke").trigger('click');
                            $(".sdk").attr("checked",false);
                            $(".sdke").attr("checked",true);
                        }else if(a != 0){
                            $(".sdk").attr("checked",true);
                            $(".sdke").attr("checked",false);
                        }
                    }
                }
            });

            function laiui(powerPeople1,powerSensor1,powerPeople2,powerSensor2,powerPeople3,powerSensor3,powerPeople4,powerSensor4,powerPeople5,powerSensor5,
                           powerDawnPeople,powerSensorDown,switchDelayTime,ligntOnDuration) {
                //人体感应开关
                $(".sdke").click(function(){
                    $(".progres,.sdsds,.nbo input").css("color","#ccc");
                    $(".nbo").css("pointer-events","none");
                    $(".nbo input").attr("readonly","readonly");
                    $(".progres input").prop("checked",false);
                    $(".progres input").attr("disabled","disabled");
                    $("#r_teml_ma").val("30");
                    disabled=true
                    layui.use('slider', function(){
                        var $ = layui.$
                            , slider = layui.slider;
                        slider.render({
                            elem: '#slideTest4'
                            , theme: '#1E9FFF' //主题色
                            , value: powerPeople1//初始值
                        });
                        slider.render({
                            elem: '#slideTest5'
                            , theme: '#1E9FFF' //主题色
                            , value: powerSensor1 //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest12'
                            , theme: '#1E9FFF' //主题色
                            , value: powerPeople2 //初始值
                        });
                        slider.render({
                            elem: '#slideTest16'
                            , theme: '#1E9FFF' //主题色
                            , value:powerSensor2  //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest8'
                            , theme: '#1E9FFF' //主题色
                            , value:powerPeople3  //初始值
                        });
                        slider.render({
                            elem: '#slideTest9'
                            , theme: '#1E9FFF' //主题色
                            , value:powerSensor3  //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest10'
                            , theme: '#1E9FFF' //主题色
                            , value: powerPeople4 //初始值
                        });
                        slider.render({
                            elem: '#slideTest11'
                            , theme: '#1E9FFF' //主题色
                            , value: powerSensor4 //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest13'
                            , theme: '#1E9FFF' //主题色
                            , value:powerPeople5 //初始值
                        });
                        slider.render({
                            elem: '#slideTest14'
                            , theme: '#1E9FFF' //主题色
                            , value: powerSensor5 //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest6'
                            , theme: '#1E9FFF' //主题色
                            , value: powerDawnPeople //初始值
                        });
                        slider.render({
                            elem: '#slideTest7'
                            , theme: '#1E9FFF' //主题色
                            , value: powerSensorDown //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest1'
                            , theme: '#1E9FFF' //主题色
                            , input: true //输入框
                            , value: switchDelayTime //初始值
                        });
                        slider.render({
                            elem: '#slideTest_m'
                            , theme: '#1E9FFF' //主题色
                            , min: 5
                            , value: ligntOnDuration //初始值
                        });
                    })
                })
                $(".sdk").click(function(){
                    $(".progres,.sdsds,.nbo input").css("color","");
                    $(".nbo").css("pointer-events","");
                    $(".nbo input").removeAttr("readonly","readonly");
                    // $(".progres input").prop("checked",true);
                    $(".progres input").removeAttr("disabled","disabled")
                    disabled=false
                    layui.use('slider', function(){
                        var $ = layui.$
                            , slider = layui.slider;
                        slider.render({
                            elem: '#slideTest4'
                            , theme: '#1E9FFF' //主题色
                            , value: powerPeople1//初始值
                        });
                        slider.render({
                            elem: '#slideTest5'
                            , theme: '#1E9FFF' //主题色
                            , value: powerSensor1 //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest12'
                            , theme: '#1E9FFF' //主题色
                            , value: powerPeople2 //初始值
                        });
                        slider.render({
                            elem: '#slideTest16'
                            , theme: '#1E9FFF' //主题色
                            , value:powerSensor2  //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest8'
                            , theme: '#1E9FFF' //主题色
                            , value:powerPeople3  //初始值
                        });
                        slider.render({
                            elem: '#slideTest9'
                            , theme: '#1E9FFF' //主题色
                            , value:powerSensor3  //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest10'
                            , theme: '#1E9FFF' //主题色
                            , value: powerPeople4 //初始值
                        });
                        slider.render({
                            elem: '#slideTest11'
                            , theme: '#1E9FFF' //主题色
                            , value: powerSensor4 //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest13'
                            , theme: '#1E9FFF' //主题色
                            , value:powerPeople5 //初始值
                        });
                        slider.render({
                            elem: '#slideTest14'
                            , theme: '#1E9FFF' //主题色
                            , value: powerSensor5 //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest6'
                            , theme: '#1E9FFF' //主题色
                            , value: powerDawnPeople //初始值
                        });
                        slider.render({
                            elem: '#slideTest7'
                            , theme: '#1E9FFF' //主题色
                            , value: powerSensorDown //初始值
                            , disabled: disabled
                        });
                        slider.render({
                            elem: '#slideTest1'
                            , theme: '#1E9FFF' //主题色
                            , input: true //输入框
                            , value: switchDelayTime //初始值
                        });
                        slider.render({
                            elem: '#slideTest_m'
                            , theme: '#1E9FFF' //主题色
                            , min: 5
                            , value: ligntOnDuration //初始值
                        });
                    })
                })
                layui.use('slider', function(){
                    var $ = layui.$
                        , slider = layui.slider;
                    slider.render({
                        elem: '#slideTest4'
                        , theme: '#1E9FFF' //主题色
                        , value: powerPeople1//初始值
                    });
                    slider.render({
                        elem: '#slideTest5'
                        , theme: '#1E9FFF' //主题色
                        , value: powerSensor1 //初始值
                        , disabled: disabled
                    });
                    slider.render({
                        elem: '#slideTest12'
                        , theme: '#1E9FFF' //主题色
                        , value: powerPeople2 //初始值
                    });
                    slider.render({
                        elem: '#slideTest16'
                        , theme: '#1E9FFF' //主题色
                        , value:powerSensor2  //初始值
                        , disabled: disabled
                    });
                    slider.render({
                        elem: '#slideTest8'
                        , theme: '#1E9FFF' //主题色
                        , value:powerPeople3  //初始值
                    });
                    slider.render({
                        elem: '#slideTest9'
                        , theme: '#1E9FFF' //主题色
                        , value:powerSensor3  //初始值
                        , disabled: disabled
                    });
                    slider.render({
                        elem: '#slideTest10'
                        , theme: '#1E9FFF' //主题色
                        , value: powerPeople4 //初始值
                    });
                    slider.render({
                        elem: '#slideTest11'
                        , theme: '#1E9FFF' //主题色
                        , value: powerSensor4 //初始值
                        , disabled: disabled
                    });
                    slider.render({
                        elem: '#slideTest13'
                        , theme: '#1E9FFF' //主题色
                        , value:powerPeople5 //初始值
                    });
                    slider.render({
                        elem: '#slideTest14'
                        , theme: '#1E9FFF' //主题色
                        , value: powerSensor5 //初始值
                        , disabled: disabled
                    });
                    slider.render({
                        elem: '#slideTest6'
                        , theme: '#1E9FFF' //主题色
                        , value: powerDawnPeople //初始值
                    });
                    slider.render({
                        elem: '#slideTest7'
                        , theme: '#1E9FFF' //主题色
                        , value: powerSensorDown //初始值
                        , disabled: disabled
                    });
                    slider.render({
                        elem: '#slideTest1'
                        , theme: '#1E9FFF' //主题色
                        , input: true //输入框
                        , value: switchDelayTime //初始值
                    });
                    slider.render({
                        elem: '#slideTest_m'
                        , theme: '#1E9FFF' //主题色
                        , min: 5
                        , value: ligntOnDuration //初始值
                    });
                });
            }
        }
    }

    function hardware() {
        var url
        var urlstate
        var urlGPRS
        var loadIndex
        //硬件升级
        if(deviceCode != ""){
            url=baseURL + 'fun/device/version?deviceId='+ deviceId +"&groupId="+"",
            urlstate=baseURL + 'fun/device/checkMCURunState?deviceId='+ deviceId +"&groupId="+"",
            urlGPRS=baseURL + 'fun/device/checkGPRSRunState?deviceId='+ deviceId +"&groupId="+"",
            loadIndex_a(0)
            gprs(0)
            $(".hardware").unbind('click');
            $(".hardware").click(function(){
                var sele=Number($("#upgrade option:selected").html());
                $.ajax({
                    url:baseURL + 'fun/device/updateVersion',
                    contentType: "application/json;charset=UTF-8",
                    type:"POST",
                    data: JSON.stringify({
                        "deviceCodes":[deviceCode] ,
                        "groupId":"",
                        "version":sele,
                        "deviceType":1,
                        "status":1
                    }),
                    success: function(res) {
                        console.log("11111")
                        console.log(res)
                        loadIndex_a(1)
                        if(res.code == "200"){

                        }else{
                            layer.open({
                                title: '信息',
                                content: "硬件正在升级",
                                skin: 'demo-class'
                            });
                        }
                    }
                })
            })
            $(".hardware_a").unbind('click');
            $(".hardware_a").click(function(){
                var sele=$("#upgrade_a option:selected").html();
                var sele_s=sele.toString()
                sele_s= sele_s.slice(4)
                $.ajax({
                    url:baseURL + 'fun/device/updateGprsVersion?deviceId='+deviceId+"&groupId="+""+"&version="+sele_s,
                    contentType: "application/json;charset=UTF-8",
                    type:"get",
                    data:{},
                    success: function(res) {
                        console.log("22222")
                        console.log(res)
                        gprs(1)
                        if(res.code == "200"){

                        }else{
                            layer.open({
                                title: '信息',
                                content: "GPRS正在升级",
                                skin: 'demo-class'
                            });
                        }
                    }
                })

            })
        }else{
            url=baseURL + 'fun/device/version?deviceId='+ "" +"&groupId="+grod,
            urlstate=baseURL + 'fun/device/checkMCURunState?deviceId='+ "" +"&groupId="+grod,
            urlGPRS=baseURL + 'fun/device/checkGPRSRunState?deviceId='+ "" +"&groupId="+grod,
            loadIndex_a(0)
            gprs(0)
            $(".hardware").unbind('click');
            $(".hardware").click(function(){
                var sele = Number($("#upgrade option:selected").html());
                $.ajax({
                    url: baseURL + 'fun/device/updateVersion',
                    contentType: "application/json;charset=UTF-8",
                    type: "POST",
                    data: JSON.stringify({
                        "deviceCodes": [],
                        "version": sele,//数值
                        "groupId": grod,
                        "deviceType": 1,
                        "status": 1
                    }),
                    success: function (res) {
                        console.log("3333")
                        console.log(res)
                        loadIndex_a(1)
                        if (res.code == "200") {

                        } else {
                            layer.open({
                                title: '信息',
                                content: "硬件正在升级",
                                skin: 'demo-class'
                            });
                        }
                    }
                })

            })
            $(".hardware_a").unbind('click');
            $(".hardware_a").click(function(){
                var sele = $("#upgrade_a option:selected").html();
                var sele_s = sele.toString();
                sele_s = sele_s.slice(4)
                $.ajax({
                    url: baseURL + 'fun/device/updateGprsVersion?deviceId=' + "" + "&groupId=" + grod + "&version=" + sele_s,
                    contentType: "application/json;charset=UTF-8",
                    type: "get",
                    data: {},
                    success: function (res) {
                        console.log("4444")
                        console.log(res)
                        gprs(1)
                        if (res.code == "200") {

                        } else {
                            layer.open({
                                title: '信息',
                                content: "GPRS正在升级",
                                skin: 'demo-class'
                            });
                        }
                    }
                })

            })
        }
        //升级回调
        $.ajax({
            url:url,
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data:{},
            success: function(res) {
                console.log("5555")
                console.log(res)
                var html
                var htmlv
                for(var i=0;i<res.data.gprsList.length;i++){
                   html+=" <option>"+res.data.gprsList[i]+"</option>";
                }
                for(var i=0;i<res.data.versionList.length;i++){
                    htmlv+=" <option>"+res.data.versionList[i]+"</option>";
                }
                $("#upgrade_a").empty().append(html);
                $("#upgrade").empty().append(htmlv);
            }
        })

        function loadIndex_a(add) {
            if(add == 1){
                add_z()
            }
            $.ajax({
                url:urlstate,
                contentType: "application/json;charset=UTF-8",
                type:"get",
                data:{},
                success: function(res) {
                    console.log("66666")
                    console.log(res)
                    if(res.code == 200){
                        $(".hardware").removeAttr("disabled");
                    }else{
                        layer.open({
                            title: '信息',
                            content: res.msg,
                            skin: 'demo-class'
                        });
                        $(".hardware").attr({"disabled":"disabled"});
                    }
                }
            })
        }
        function gprs(add) {
            if(add == 1){
                add_z()
            }
            $.ajax({
                url:urlGPRS,
                contentType: "application/json;charset=UTF-8",
                type:"get",
                data:{},
                success: function(res) {
                    console.log("7777")
                    console.log(res)
                    if(res.code == 200){
                        $(".hardware_a").removeAttr("disabled");
                    }else{
                        layer.open({
                            title: '信息',
                            content: res.msg,
                            skin: 'demo-class'
                        });
                        $(".hardware_a").attr({"disabled":"disabled"});
                    }
                }
            })
        }
        //状态提示框
        function add_z() {
            loadIndex=layer.load(0, { //icon支持传入0-2
                shade: [0.3, 'gray'], //0.5透明度的灰色背景
                content: '传输中...',
                success: function (layero) {
                    layero.find('.layui-layer-content').css({
                        'padding-top': '39px',
                        'width': '60px'
                    });
                }
            });
            setTimeout(function () {
                layer.close(loadIndex);
            }, 2000);
        }
    }

//input输入框只能输入数字和 小数点后两位
    function num(obj,val){
        obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
        obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
        obj.value = obj.value.replace(/\.{2,}/g,""); //只保留第一个, 清除多余的
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
    }
    function numc(obj,val){
        obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
        obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
        obj.value = obj.value.replace(/\.{2,}/g,""); //只保留第一个, 清除多余的
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
        var a=document.getElementById("slideTest_a").value;
        if( parseFloat(obj.value)>parseFloat(a)){
            obj.value=a
        }
    }
    function num_e(obj,val){
        obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
        obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
        obj.value = obj.value.replace(/\.{2,}/g,""); //只保留第一个, 清除多余的
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
        var c=document.getElementById("slideTest_c").value;
        if( parseFloat(obj.value)>parseFloat(c)){
            obj.value=c
        }
    }



    $("#custom").click(function(){
        var selesl=$("#custom option:selected").attr("class");
        if(selesl=="1"){
            $(".ndey").css("display","block");
        }else {
            $(".ndey").css("display","none");
        }
    })

    $(".nqd").click(function(){
        $(".snds input").attr("readonly","readonly");
        $(".slgn,.snds input").css("color","#ccc");
        $("#r_select_b").trigger('click');
        $("#r_inpl_tw").val("20");
    })
    $(".nqde").click(function(){
        $(".snds input").removeAttr("readonly","readonly");
        $(".slgn,.snds input").css("color","");
    })
    //温控开关
    var chOn_d
    var chOn_g
    var fAng_d
    var fAng_g
    $(".kudxe").click(function(){
        chOn_d=$("#r_teml_thglte").val();
        chOn_g=$("#r_teml_thete").val();
        fAng_d=$("#r_teml_thglt").val();
        fAng_g=$("#r_teml_thet").val();
        $(".nqq,.sndsm input").css("color","#ccc");
        $(".nqiu,.jianr").css("pointer-events","none" );
        $(".sndsm input").attr("readonly","readonly");
        $("#r_teml_thete,#r_teml_thet").val("99");
        $("#r_teml_thglte,#r_teml_thglt").val("-40");
    })
    $(".kudx").click(function(){
        $(".sndsm input").removeAttr("readonly","readonly");
        $(".nqq,.sndsm input").css("color","");
        $(".nqiu,.jianr").css("pointer-events","" );
        $("#r_teml_thglte").val(chOn_d);
        $("#r_teml_thete").val(chOn_g);
        $("#r_teml_thglt").val(fAng_d);
        $("#r_teml_thet").val(fAng_g);
    })

})

function  f(hours,newHours,newH,dischargeCapacityList,chargingCapacityList,chargingCurrentList,dischargeCurrentList,batteryVoltageList,ambientTemperatureList,internalTemperatureList,visitorsFlowrateList,inductionFrequencyList,meteorologicalList) {
    var time =hours
    var time_sky
    if(newH == "1"){
        time_sky=newHours
    }else {
        time_sky=hours
    }

    //充电量
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        color: ['#27e2d4', '#508fea'],
        title: {
            text: '负载功率/充电功率'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['负载功率', '充电功率']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                axisTick: {show: false},
                data:time_sky
            }
        ],
        dataZoom: [{
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            left: '5%',
            bottom: 0,
            height: '5%',//组件高度
            start: 5,
            end:40, //初始化滚动条
        }],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '负载功率',
                type: 'bar',
                smooth: true,
                barGap: 0,
                data: dischargeCapacityList
            },
            {
                name: '充电功率',
                type: 'bar',
                smooth: true,
                data: chargingCapacityList
            }
        ]
    };
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }

    //  充电电流
    var dom_z = document.getElementById("zhe_x");
    var myChart_z = echarts.init(dom_z);
    var app_z = {};
    option_z = null;
    option_z = {
        color: ['#27e2d4','#508fea'],
        title: {
            text: '放电电流/充电电流'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['放电电流','充电电流']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: time_sky
        },
        dataZoom: [{
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            left: '5%',
            bottom: 0,
            height: '5%',//组件高度
            start: 5,
            end:40, //初始化滚动条
        }],
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'充电电流',
                type:'line',
                smooth: true,
                data:chargingCurrentList
            },
            {
                name:'放电电流',
                type:'line',
                smooth: true,
                data:dischargeCurrentList
            },

        ]
    };
    if (option_z && typeof option_z === "object") {
        myChart_z.setOption(option_z, true);
    }

//    充电电压
    var dom_c = document.getElementById("voltage");
    var myChart_c = echarts.init(dom_c);
    var app_c = {};
    option_c = null;
    option_c = {
        color: ['#14d4ce'],
        title: {
            text: '电池电压'
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            data:['电池电压']
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : time_sky
            }
        ],
        dataZoom: [{
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            left: '5%',
            bottom: 0,
            height: '5%',//组件高度
            start: 5,
            end:40, //初始化滚动条
        }],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'电池电压',
                type:'line',
                smooth: true,
                areaStyle: {},
                data:batteryVoltageList
            },
        ]
    };
    if (option_c && typeof option_c === "object") {
        myChart_c.setOption(option_c, true);
    }

//    环境温度
    var dom_h = document.getElementById("environment");
    var myChart_h= echarts.init(dom_h);
    var app_h = {};
    option_h = null;
    option_h = {
        color: ['#27e2d4', '#508fea'],
        title: {
            text: '环境温度/内部温度'
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#cccccc'
                }
            }
        },
        legend: {
            data:['环境温度','内部温度']
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : time_sky
            }
        ],
        dataZoom: [{
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            left: '5%',
            bottom: 0,
            height: '5%',//组件高度
            start: 5,
            end:40, //初始化滚动条
        }],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'环境温度',
                type:'line',
                smooth: true,
                areaStyle: {},
                data:ambientTemperatureList,
            },
            {
                name:'内部温度',
                type:'line',
                smooth: true,
                areaStyle: {},
                data:internalTemperatureList
            },
        ]
    };
    if (option_h && typeof option_h === "object") {
        myChart_h.setOption(option_h, true);
    }

//    人流量
    var dom_r = document.getElementById("induced");
    var myChart_r = echarts.init(dom_r);
    var app_r = {};
    option_r = null;
    option_r = {
        color: ['#27e2d4', '#508fea'],
        title: {
            text: '人流量/感应次数'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['人流量','感应次数']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: time
        },
        dataZoom: [{
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            left: '5%',
            bottom: 0,
            height: '5%',//组件高度
            start: 5,
            end:40, //初始化滚动条
        }],
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'人流量',
                type:'line',
                smooth: true,
                data:visitorsFlowrateList
            },
            {
                name:'感应次数',
                type:'line',
                smooth: true,
                data:inductionFrequencyList
            },
        ]
    };
    if (option_r && typeof option_r === "object") {
        myChart_r.setOption(option_r, true);
    }

//    天气
    var dom_t = document.getElementById("tian_q");
    var myChart_t = echarts.init(dom_t);
    var app_t = {};
    option_t = null;
    option_t = {
        color: ['#27e2d4'],
        title: {
            text: '气象信息'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['气象信息']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: time
        },
        dataZoom: [{
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            left: '5%',
            bottom: 0,
            height: '5%',//组件高度
            start: 5,
            end:40, //初始化滚动条
        }],
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'气象信息',
                type:'line',
                smooth: true,
                data:meteorologicalList
            }
        ]
    };
    if (option_t && typeof option_t === "object") {
        myChart_t.setOption(option_t, true);
    }

}

