$(function(){
    //    数值增殖期
    (function ($) {
        $('.spinner .btn:first-of-type').on('click', function () {
            // console.log( $(this).parent().siblings(".form-control").val() )
            $(this).parent().siblings(".form-control").val(parseInt($(this).parent().siblings(".form-control").val(), 0) + 1);
        });
        $('.spinner .btn:last-of-type').on('click', function () {
            // $('.spinner input').val( parseInt($('.spinner input').val(), 10) - 1);
            $(this).parent().siblings(".form-control").val(parseInt($(this).parent().siblings(".form-control").val(), 0) - 1);
        });

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
    //设备编号
    var deviceCode=href['deviceCode'];
    //分组id
    var grod=href['grod'];
    //设备id
    var deviceId=href['deviceId'];
    //设备类型
    var type=href['type'];
    console.log("Code")
    console.log(deviceCode)
    console.log("分组id")
    console.log(grod)
    console.log("设备id")
    console.log(deviceId)
    console.log("设备类型")
    console.log(type)

    f([1,2,3,4,5,6,7,8,9,10,11,12],[],[],[],[],[],
        [],[],[],[],[])
    //判断高级设置页面针对是单台设备还是多台设备（做处理）
    if(deviceCode != "" ){
        var myDate = new Date;
        var ms = (1000 * 60 * 60 * 24 * 30)
        var date1 = new Date(myDate.getTime() - ms);
        var str = "" + date1.getFullYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDate() + "";
        var str_m="" + date1.getFullYear() + "-" + (date1.getMonth()- 5)
        var str_F="" + (date1.getFullYear()-3)
        var selse

        $("#sele_con").click(function () {
            var selectId = $('#sele_con>option:selected').attr("id")
            selse=selectId
        })
        $("#d421").click(function(){
            var data
            if(selse == "1"){
                data={dateFmt:'yyyy-M-d',minDate:str,maxDate:'%y-%M-%d'}
            }else if(selse == "2"){
                data={dateFmt:'yyyy-M',minDate:str_m,maxDate:'%y-%M'}
            }else if(selse == "3"){
                data={dateFmt:'yyyy',minDate:str_F,maxDate:'%y'}
            }
            WdatePicker(data)
        })
        $("#statistics").click(function () {
            selse = $('#sele_con>option:selected').attr("id")
            var va_l=$("#d421").val()
            if(selse =="1"){
                day(deviceCode,va_l)
            }else if(selse == "2"){
                Month(deviceCode,va_l)
            }else if(selse == "3"){
                Year(deviceCode,va_l)
            }
        })
    }

    var dischargeCapacityList //放电量
    var chargingCapacityList //充电量
    var chargingCurrentList  //充电电流
    var dischargeCurrentList//放电电流
    var batteryVoltageList  //电池电压
    var ambientTemperatureList//环境温度
    var internalTemperatureList  //内部温度
    var visitorsFlowrateList    //人流量
    var inductionFrequencyList  //感应次数
    var meteorologicalList   //气象信息
    var hours
    var newHours
    var newH
    //根据年月日调取渲染数据表格
    function day(deviceCode,va_l){
        $.ajax({
            url: baseURL + 'fun/history/queryHistoryByDay?deviceCode=' + deviceCode+"&time="+va_l,
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
                console.log("日")
                console.log(res)
                dischargeCapacityList=res.data.dischargeCapacityList;//放电量
                chargingCapacityList=res.data.chargingCapacityList;  //充电量
                chargingCurrentList=res.data.chargingCurrentList;    //充电电流
                dischargeCurrentList=res.data.dischargeCurrentList;  //放电电流
                batteryVoltageList=res.data.batteryVoltageList;      //电池电压
                ambientTemperatureList=res.data.ambientTemperatureList;  //环境温度
                internalTemperatureList=res.data.internalTemperatureList; //内部温度
                visitorsFlowrateList =res.data.visitorsFlowrateList;   //人流量
                inductionFrequencyList =res.data.inductionFrequencyList; //感应次数
                meteorologicalList=res.data.meteorologicalList;   //气象信息
                hours=res.data.hours; //X坐标
                newHours=res.data.newHours
                newH="1"
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
                dischargeCapacityList=res.data.dischargeCapacityList;//放电量
                chargingCapacityList=res.data.chargingCapacityList;  //充电量
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
                dischargeCapacityList=res.data.dischargeCapacityList;//放电量
                chargingCapacityList=res.data.chargingCapacityList;  //充电量
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


    $(".tab li").click(function() {
        $(".tab li").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
        $(".option_a>.option").hide().eq($(this).index()).show();
        console.log($(this).attr("id")+"类")
        if($(this).attr("id") == "opt"){
            gao()
        }
    });

    function gao(){
//高级设置自定义
        $("#custom").click(function(){
            var select=$("#custom option:selected").attr("class");
            if(select == "1"){
                $(".shade_project,.shade_b_project,.Bian_j").show()
            }else{
                $(".shade_project,.shade_b_project,.Bian_j").hide()
            }
        })
        $(".shade_add_project").click(function(){
            $(".shade_project,.shade_b_project,.Bian_j").hide()
        })
    //电池类型
        $("#r_select_b").click(function(){
            var select=$("#r_select_b option:selected").attr("class");
            if(select == "4"){
                $("#r_inpl_one").val($("#r_teml_forl").val()*3)
                $("#chong").val($("#r_teml_forl").val()*4.1)
            }else if(select == "3"){
                $("#r_inpl_one").val($("#r_teml_forl").val()*2.8)
                $("#chong").val($("#r_teml_forl").val()*3.6)
            }
        })

        if(deviceCode != ""){
            //设备高级设置数据渲染
            console.log("设备高级设置上传")
            var ur=baseURL + 'advancedsetting/queryDevAdvInfo?deviceCode='+deviceCode+"&groupId="+grod
            url(ur)
            //设备保存值
            $("#r_bttn").click(function () {
                //负载
                var loadWorkMode = parseInt($("#select1_b option:selected").attr("class"));   //负载工作模式
                var powerLoad = parseInt($("#r_Load_power").val()*100); //负载功率
                var timeTurnOn=$("#d414").val(); //开灯时刻
                var timeTurnON_a=timeTurnOn.split(":")
                var timeTurnON_b=Number(timeTurnON_a[0]*60)+Number(timeTurnON_a[1])
                var timeTurnOff=$("#d4141").val(); //关灯时刻
                var timeTurnOff_a=timeTurnOff.split(":")
                var timeTurnOff_b=Number(timeTurnOff_a[0]*60)+Number(timeTurnOff_a[1])
                var time1= Number($("#r_one_teml").val())*60+Number($("#r_teml_tw").val())//1时段时长
                var time2= Number($("#r_teml_sv").val())*60+Number($("#r_teml_eg").val());  //2时段时长
                var time3= Number($("#r_teml_th").val())*60+Number($("#r_teml_for").val());//3时段时长
                var time4= Number($("#r_teml_ng").val())*60+Number($("#r_teml_te").val());//4时段时长
                var time5=Number($("#r_teml_fif").val())*60+Number($("#r_teml_six").val());//5时段时长
                var timeDown= Number($("#r_teml_el").val())*60+Number($("#r_teml_egl").val());//晨亮时段时长
                //关灯时段
                var powerPeople1=parseInt($("#slideTest4").children().children(".layui-slider-bar").height()/$("#slideTest4").children(".layui-slider").height()*100); //时段1 有人功率
                var powerPeople2=parseInt($("#slideTest12").children().children(".layui-slider-bar").height()/$("#slideTest12").children(".layui-slider").height()*100);//时段2 有人功率
                var powerPeople3=parseInt($("#slideTest8").children().children(".layui-slider-bar").height()/$("#slideTest8").children(".layui-slider").height()*100); //时段3 有人功率
                var powerPeople4=parseInt($("#slideTest10").children().children(".layui-slider-bar").height()/$("#slideTest10").children(".layui-slider").height()*100);//时段4 有人功率
                var powerPeople5=parseInt($("#slideTest13").children().children(".layui-slider-bar").height()/$("#slideTest13").children(".layui-slider").height()*100);//时段5 有人功率
                var powerDawnPeople=parseInt($("#slideTest6").children().children(".layui-slider-bar").height()/$("#slideTest6").children(".layui-slider").height()*100);//晨亮 有人功率

                var powerSensor1=parseInt($("#slideTest5").children().children(".layui-slider-bar").height()/$("#slideTest5").children(".layui-slider").height()*100);//时段1 无人功率
                var powerSensor2=parseInt($("#slideTest16").children().children(".layui-slider-bar").height()/$("#slideTest16").children(".layui-slider").height()*100);//时段2 无人功率
                var powerSensor3=parseInt($("#slideTest9").children().children(".layui-slider-bar").height()/$("#slideTest9").children(".layui-slider").height()*100);//时段3无人功率
                var powerSensor4=parseInt($("#slideTest11").children().children(".layui-slider-bar").height()/$("#slideTest11").children(".layui-slider").height()*100); //时段4 无人功率
                var powerSensor5=parseInt($("#slideTest14").children().children(".layui-slider-bar").height()/$("#slideTest14").children(".layui-slider").height()*100);//时段5 无人功率
                var powerSensorDown=parseInt($("#slideTest7").children().children(".layui-slider-bar").height()/$("#slideTest7").children(".layui-slider").height()*100); //晨亮 无人功率

                var autoSleepTime=parseInt($("#slideTest15").children().children(".layui-slider-bar").width()/$("#slideTest15").children(".layui-slider").width()*100); //自动休眠时间长度
                var savingSwitch=parseInt($("#custom option:selected").attr("class"));
                var inductionLightOnDelay=parseInt($("#r_teml_ma").val())
                //光电池
                var vpv=parseInt($("#r_teml_tgl").val());//光控电压
                var pvSwitch=parseInt($("#r_btl_th>.r_btl_th[type='radio']:checked").val()); //光控关灯开灯
                var batType=parseInt($("#r_select_b option:selected").attr("class"));// 电池类型
                var switchDelayTime=parseInt($("#slideTest1").children().children(".layui-slider-bar").width()/$("#slideTest1").children(".layui-slider").width()*100);//开关灯延时
                var ligntOnDuration=parseInt($("#slideTest_m").children().children(".layui-slider-bar").width()/$("#slideTest_m").children(".layui-slider").width()*100);//光控延迟时间长度
                //蓄电池
                var batStringNum= parseInt($("#r_teml_forl").val())//电池串数
                var volOverDisCharge=parseInt($("#r_inpl_one").val()*100)//过放电压
                var volCharge=parseInt($("#chong").val()*100)//充电电压
                var tempCharge=parseInt($("#r_teml_thglr").val()) //充电温度范围(最大值  做高八位低八位处理)
                var tempDisCharge=parseInt($("#r_teml_thglt").val()) //放电温度范围（最大值做高八位低八位处理)）
                var tempCharge0=parseInt($("#r_teml_thelr").val()) //充电温度范围(最小值做高八位低八位处理)
                var tempDisCharge25=parseInt($("#r_teml_thet").val()) //放电温度范围（最小值做高八位低八位处理)）
                console.log("高")
                console.log(tempCharge)

                var newTempCharge = ((tempCharge& 0xFF)<<8) + (tempCharge0 & 0xFF);
                var newTempDisCharge = ((tempDisCharge & 0xFF)<<8) + (tempDisCharge25 & 0xFF);
                var inspectionTime=Number($("#r_teml_thgl").val())*60+Number($("#r_teml_thel").val()) //巡检时间
                var icharge= parseInt($("#r_inpl_tw").val())*100;//充电电流
                // 第一阶段
                var firDownPower = parseInt($("#slideTest_a").children().children(".layui-slider-bar").width()*100 );//一阶降压功率
                var firReducAmplitude = parseInt($("#slideTest_b").children().children(".layui-slider-bar").width()*100);//一阶降功率幅度
                // 第二阶段
                var twoDownPower = parseInt($("#slideTest_c").children().children(".layui-slider-bar").width()*100);//二阶降压功率
                var twoReducAmplitude = parseInt($("#slideTest_d").children().children(".layui-slider-bar").width()*100);//二阶降功率幅度
                // 第三阶段
                var threeDownPower = parseInt($("#slideTest_e").children().children(".layui-slider-bar").width()*100);//三阶降压功率
                var threeReducAmplitude = parseInt($("#slideTest_f").children().children(".layui-slider-bar").width()*100);//三阶降功率幅度

                var qaKey=["loadWorkMode","powerLoad","timeTurnOn","timeTurnOff","time1",
                    "time2","time3","time4","time5","timeDown","powerPeople1","powerPeople2","powerPeople3","powerPeople4","powerPeople5",
                    "powerDawnPeople","powerSensor1","powerSensor2","powerSensor3","powerSensor4","powerSensor5","powerSensorDown","autoSleepTime","vpv",
                    "pvSwitch","batType","batStringNum","volOverDisCharge","volCharge","tempCharge","tempDisCharge","inspectionTime","iCharge","switchDelayTime","firDownPower",
                    "twoDownPower","threeDownPower","firReducAmplitude","twoReducAmplitude","threeReducAmplitude","ligntOnDuration","savingSwitch","inductionLightOnDelay"];
                var value=[loadWorkMode,powerLoad,timeTurnON_b,timeTurnOff_b,time1,
                    time2,time3,time4,time5,timeDown,powerPeople1,powerPeople2,powerPeople3,powerPeople4,powerPeople5,
                    powerDawnPeople,powerSensor1,powerSensor2,powerSensor3,powerSensor4,powerSensor5,powerSensorDown,autoSleepTime,vpv,
                    pvSwitch,batType,batStringNum,volOverDisCharge,volCharge,newTempCharge,newTempDisCharge,inspectionTime,icharge,switchDelayTime,firDownPower,
                    twoDownPower,threeDownPower,firReducAmplitude,twoReducAmplitude,threeReducAmplitude,ligntOnDuration,savingSwitch,inductionLightOnDelay];
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

                        "&ligntOnDuration="+ ligntOnDuration +//光控延迟时间长度
                        "&batType="+ batType + // 电池类型 0:未设置；1：胶体电池；2：铅酸电池； 3磷酸铁锂电池；4：三元锂电池
                        "&batStringNum="+batStringNum +//电池串数
                        "&volOverDisCharge="+ volOverDisCharge + //过放电压
                        "&volCharge="+ volCharge +//充电电压
                        "&tempCharge="+ newTempCharge +//充电温度范围(最大值和最小值做高八位低八位处理)
                        "&tempDisCharge="+ newTempDisCharge +//放电温度范围（最大值和最小值做高八位低八位处理)）
                        "&inspectionTime="+ inspectionTime+//巡检时间
                        "&iCharge="+ icharge + //充电电流

                        "&inductionSwitch="+ "" +//感应开关
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
                        console.log("设备高级设置上传成功")
                        alert("保存成功")
                    }
                });

                //    硬件设备
                var dev=[]
                    dev.push(deviceCode)
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
                        console.log("硬件分组返回")
                        console.log(res)

                    }
                })
            })
        }else{
            console.log("分组高级设置上传");
            var ur=baseURL + 'advancedsetting/settingInfo?groupId='+grod
            url(ur)
            $("#r_bttn").click(function () {
                //负载
                var loadWorkMode = parseInt($("#select1_b option:selected").attr("class"));   //负载工作模式
                var powerLoad = parseInt($("#r_Load_power").val()*100); //负载功率
                var timeTurnOn=$("#d414").val(); //开灯时刻
                var timeTurnON_a=timeTurnOn.split(":")
                var timeTurnON_b=Number(timeTurnON_a[0]*60)+Number(timeTurnON_a[1])
                var timeTurnOff=$("#d4141").val(); //关灯时刻
                var timeTurnOff_a=timeTurnOff.split(":")
                var timeTurnOff_b=Number(timeTurnOff_a[0]*60)+Number(timeTurnOff_a[1])
                var time1= Number($("#r_one_teml").val())*60+Number($("#r_teml_tw").val())//1时段时长
                var time2= Number($("#r_teml_sv").val())*60+Number($("#r_teml_eg").val());  //2时段时长
                var time3= Number($("#r_teml_th").val())*60+Number($("#r_teml_for").val());//3时段时长
                var time4= Number($("#r_teml_ng").val())*60+Number($("#r_teml_te").val());//4时段时长
                var time5=Number($("#r_teml_fif").val())*60+Number($("#r_teml_six").val());//5时段时长
                var timeDown= Number($("#r_teml_el").val())*60+Number($("#r_teml_egl").val());//晨亮时段时长
                //关灯时段
                var powerPeople1=parseInt($("#slideTest4").children().children(".layui-slider-bar").height()/$("#slideTest4").children(".layui-slider").height()*100); //时段1 有人功率
                var powerPeople2=parseInt($("#slideTest12").children().children(".layui-slider-bar").height()/$("#slideTest12").children(".layui-slider").height()*100);//时段2 有人功率
                var powerPeople3=parseInt($("#slideTest8").children().children(".layui-slider-bar").height()/$("#slideTest8").children(".layui-slider").height()*100); //时段3 有人功率
                var powerPeople4=parseInt($("#slideTest10").children().children(".layui-slider-bar").height()/$("#slideTest10").children(".layui-slider").height()*100);//时段4 有人功率
                var powerPeople5=parseInt($("#slideTest13").children().children(".layui-slider-bar").height()/$("#slideTest13").children(".layui-slider").height()*100);//时段5 有人功率
                var powerDawnPeople=parseInt($("#slideTest6").children().children(".layui-slider-bar").height()/$("#slideTest6").children(".layui-slider").height()*100);//晨亮 有人功率
                var powerSensor1=parseInt($("#slideTest5").children().children(".layui-slider-bar").height()/$("#slideTest5").children(".layui-slider").height()*100);//时段1 无人功率
                var powerSensor2=parseInt($("#slideTest16").children().children(".layui-slider-bar").height()/$("#slideTest16").children(".layui-slider").height()*100);//时段2 无人功率
                var powerSensor3=parseInt($("#slideTest9").children().children(".layui-slider-bar").height()/$("#slideTest9").children(".layui-slider").height()*100);//时段3无人功率
                var powerSensor4=parseInt($("#slideTest11").children().children(".layui-slider-bar").height()/$("#slideTest11").children(".layui-slider").height()*100); //时段4 无人功率
                var powerSensor5=parseInt($("#slideTest14").children().children(".layui-slider-bar").height()/$("#slideTest14").children(".layui-slider").height()*100);//时段5 无人功率
                var powerSensorDown=parseInt($("#slideTest7").children().children(".layui-slider-bar").height()/$("#slideTest7").children(".layui-slider").height()*100); //晨亮 无人功率
                var autoSleepTime=parseInt($("#slideTest15").children().children(".layui-slider-bar").width()/$("#slideTest15").children(".layui-slider").width()*100); //自动休眠时间长度
                var savingSwitch=parseInt($("#custom option:selected").attr("class"));
                var inductionLightOnDelay=parseInt($("#r_teml_ma").val())
                //光电池
                var vpv=parseInt($("#r_teml_tgl").val());//光控电压
                var pvSwitch=parseInt($("#r_btl_th>.r_btl_th[type='radio']:checked").val()); //光控关灯开灯
                var batType=parseInt($("#r_select_b option:selected").attr("class"));// 电池类型
                var switchDelayTime=parseInt($("#slideTest1").children().children(".layui-slider-bar").width()/$("#slideTest1").children(".layui-slider").width()*100);
                var ligntOnDuration=parseInt($("#slideTest_m").children().children(".layui-slider-bar").width()/$("#slideTest_m").children(".layui-slider").width()*100);//光控延迟时间长度
                //蓄电池
                var batStringNum= parseInt($("#r_teml_forl").val())//电池串数
                var volOverDisCharge=parseInt($("#r_inpl_one").val()*100)//过放电压
                var volCharge=parseInt($("#chong").val()*100)//充电电压
                var tempCharge=parseInt($("#r_teml_thglr").val()) //充电温度范围(最大值  做高八位低八位处理)
                var tempDisCharge=parseInt($("#r_teml_thglt").val()) //放电温度范围（最大值做高八位低八位处理)）
                var tempCharge0=parseInt($("#r_teml_thelr").val()) //充电温度范围(最小值做高八位低八位处理)
                var tempDisCharge25=parseInt($("#r_teml_thet").val()) //放电温度范围（最小值做高八位低八位处理)）
                var newTempCharge = ((tempCharge & 0xFF)<<8) + (tempCharge0 & 0xFF);
                var newTempDisCharge = ((tempDisCharge & 0xFF)<<8) + (tempDisCharge25 & 0xFF);

                var inspectionTime=Number($("#r_teml_thgl").val())*60+Number($("#r_teml_thel").val()) //巡检时间
                var icharge=parseInt($("#r_inpl_tw").val())*100;//充电电流
                //自定义
                // 第一阶段
                var firDownPower = parseInt($("#slideTest_a").children().children(".layui-slider-bar").width()*100 );//一阶降压功率
                var firReducAmplitude = parseInt($("#slideTest_b").children().children(".layui-slider-bar").width()*100);//一阶降功率幅度
                // 第二阶段
                var twoDownPower = parseInt($("#slideTest_c").children().children(".layui-slider-bar").width()*100);//二阶降压功率
                var twoReducAmplitude = parseInt($("#slideTest_d").children().children(".layui-slider-bar").width()*100 );//二阶降功率幅度
                //第三阶段
                var threeDownPower = parseInt($("#slideTest_e").children().children(".layui-slider-bar").width()*100);//三阶降压功率
                var threeReducAmplitude = parseInt($("#slideTest_f").children().children(".layui-slider-bar").width()*100);//三阶降功率幅度
                var qaKey=["loadWorkMode","powerLoad","timeTurnOn","timeTurnOff","time1",
                    "time2","time3","time4","time5","timeDown","powerPeople1","powerPeople2","powerPeople3","powerPeople4","powerPeople5",
                    "powerDawnPeople","powerSensor1","powerSensor2","powerSensor3","powerSensor4","powerSensor5","powerSensorDown","autoSleepTime","vpv",
                    "pvSwitch","batType","batStringNum","volOverDisCharge","volCharge","tempCharge","tempDisCharge","inspectionTime","iCharge","switchDelayTime","firDownPower",
                    "twoDownPower","threeDownPower","firReducAmplitude","twoReducAmplitude","threeReducAmplitude","ligntOnDuration","savingSwitch","inductionLightOnDelay"];
                var value=[loadWorkMode,powerLoad,timeTurnON_b,timeTurnOff_b,time1,
                    time2,time3,time4,time5,timeDown,powerPeople1,powerPeople2,powerPeople3,powerPeople4,powerPeople5,
                    powerDawnPeople,powerSensor1,powerSensor2,powerSensor3,powerSensor4,powerSensor5,powerSensorDown,autoSleepTime,vpv,
                    pvSwitch,batType,batStringNum,volOverDisCharge,volCharge,newTempCharge,newTempDisCharge,inspectionTime,icharge,switchDelayTime,firDownPower,
                    twoDownPower,threeDownPower,firReducAmplitude,twoReducAmplitude,threeReducAmplitude,ligntOnDuration,savingSwitch,inductionLightOnDelay];

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

                        "&ligntOnDuration="+ ligntOnDuration +//光控延迟时间长度
                        "&batType="+ batType + // 电池类型 0:未设置；1：胶体电池；2：铅酸电池； 3磷酸铁锂电池；4：三元锂电池
                        "&batStringNum="+batStringNum +//电池串数
                        "&volOverDisCharge="+ volOverDisCharge + //过放电压
                        "&volCharge="+ volCharge +//充电电压
                        "&tempCharge="+ newTempCharge +//充电温度范围(最大值和最小值做高八位低八位处理)
                        "&tempDisCharge="+ newTempDisCharge +//放电温度范围（最大值和最小值做高八位低八位处理)）
                        "&inspectionTime="+ inspectionTime+//巡检时间
                        "&iCharge="+ icharge + //充电电流

                        "&inductionSwitch="+ "" +//感应开关
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
                        alert("分组保存成功")
                    }
                });
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
                        console.log("硬件分组返回")
                        console.log(res)
                    }
                })
            })
        }
        function url(ur){
            $.ajax({
                url:ur,
                contentType: "application/json;charset=UTF-8",
                type:"get",
                data: {},
                success: function(res){
                    if(res.info == ""){
                        return
                    }else{
                        console.log("设备高级设置参数获取1")
                        console.log(res)
                        $("#r_Load_power").val(res.info.powerLoad/100)// 负载功率
                        var turnon=parseInt(res.info.timeTurnOn/60)
                        var turnona=res.info.timeTurnOn-(turnon*60);
                        var turnonb=turnon+":"+turnona
                        $("#d414").val(turnonb) ; //开灯时刻
                        var timeTurnOff=parseInt(res.info.timeTurnOff/60);
                        var timeTurnOff_a=res.info.timeTurnOff-(timeTurnOff*60);
                        var timeTurnOff_b=timeTurnOff+":"+timeTurnOff_a
                        $("#d4141").val(timeTurnOff_b)
                        var time1=parseInt(res.info.time1/60);
                        var time1_a=res.info.time1-(time1*60);
                        $("#r_one_teml").val(time1)   //一阶段
                        $("#r_teml_tw").val(time1_a)
                        var time2=parseInt(res.info.time2/60);
                        var time2_a=res.info.time2-(time2*60);
                        $("#r_teml_sv").val(time2)//二阶段
                        $("#r_teml_eg").val(time2_a)
                        var time3=parseInt(res.info.time3/60);
                        var time3_a=res.info.time3-(time3*60);
                        $("#r_teml_th").val(time3)//三阶段
                        $("#r_teml_for").val(time3_a)
                        var time4=parseInt(res.info.time4/60);
                        var time4_a=res.info.time4-(time4*60);
                        $("#r_teml_ng").val(time4)//4阶段
                        $("#r_teml_te").val(time4_a)
                        var time5=parseInt(res.info.time5/60);
                        var time5_a=res.info.time5-(time5*60);
                        $("#r_teml_fif").val(time5)//5阶段
                        $("#r_teml_six").val(time5_a)
                        var timeDown=parseInt(res.info.timeDown/60);
                        var timeDown_a=res.info.timeDown-(timeDown*60);
                        $("#r_teml_el").val(timeDown)//晨亮时段时长
                        $("#r_teml_egl").val(timeDown_a)
                        $("#r_teml_tgl").val(res.info.vpv)//光控电压
                        $("").val()// 光控关灯开关......
                        $("#r_teml_forl").val(res.info.batStringNum) //电池串数
                        $("#r_select_b option").val(res.info.batType);//电池类型
                        $("#r_inpl_one").val(res.info.volOverDisCharge/100);//过放电压
                        $("#r_inpl_tw").val(res.info.icharge/100);//充电电流
                        $("#chong").val(res.info.volCharge/100);//充电电压
                        $("#r_teml_ma").val(res.info.inductionLightOnDelay)  //人体感应功能开关
                        var inspectionTime=parseInt(res.info.inspectionTime/60);
                        var inspectionTime_a=res.info.inspectionTime-(inspectionTime*60);
                        $("#r_teml_thgl").val(inspectionTime)//巡检
                        $("#r_teml_thel").val(inspectionTime_a)
                        var tempCharge=parseInt(res.info.tempCharge/60);
                        var tempCharge_a=res.info.tempCharge-(tempCharge*60);
                        $("#r_teml_thglr").val(tempCharge)//充电时间范围
                        $("#r_teml_thelr").val(tempCharge_a)
                        var tempDisCharge=parseInt(res.info.tempDisCharge/60);
                        var tempDisCharge_a=res.info.tempDisCharge-(tempDisCharge*60);
                        $("#r_teml_thglt").val(tempDisCharge)//放电时间范围
                        $("#r_teml_thet").val(tempDisCharge_a)

                        $("#slideTest4").children().children(".layui-slider-bar").css("height",res.info.powerPeople1+"%")//灯亮时段1有人
                        $("#slideTest4").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople1+"%")
                        $("#slideTest4").children().children().children(".layui-input").val(res.info.powerPeople1)
                        $("#slideTest5").children().children(".layui-slider-bar").css("height",res.info.powerSensor1+"%")//灯亮时段1无人
                        $("#slideTest5").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor1+"%")
                        $("#slideTest5").children().children().children(".layui-input").val(res.info.powerSensor1)
                        $("#slideTest12").children().children(".layui-slider-bar").css("height",res.info.powerPeople2+"%")//灯亮时段2有人
                        $("#slideTest12").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople2+"%")
                        $("#slideTest12").children().children().children(".layui-input").val(res.info.powerPeople2)
                        $("#slideTest16").children().children(".layui-slider-bar").css("height",res.info.powerSensor2+"%")//灯亮时段2无人
                        $("#slideTest16").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor2+"%")
                        $("#slideTest16").children().children().children(".layui-input").val(res.info.powerSensor2)
                        $("#slideTest8").children().children(".layui-slider-bar").css("height",res.info.powerPeople3+"%")//灯亮时段3有人
                        $("#slideTest8").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople3+"%")
                        $("#slideTest8").children().children().children(".layui-input").val(res.info.powerPeople3)
                        $("#slideTest9").children().children(".layui-slider-bar").css("height",res.info.powerSensor3+"%")//灯亮时段3无人
                        $("#slideTest9").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor3+"%")
                        $("#slideTest9").children().children().children(".layui-input").val(res.info.powerSensor3)
                        $("#slideTest10").children().children(".layui-slider-bar").css("height",res.info.powerPeople4+"%")//灯亮时段4有人
                        $("#slideTest10").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople4+"%")
                        $("#slideTest10").children().children().children(".layui-input").val(res.info.powerPeople4)
                        $("#slideTest11").children().children(".layui-slider-bar").css("height",res.info.powerSensor4+"%")//灯亮时段4无人
                        $("#slideTest11").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor4+"%")
                        $("#slideTest11").children().children().children(".layui-input").val(res.info.powerSensor4)
                        $("#slideTest13").children().children(".layui-slider-bar").css("height",res.info.powerPeople5+"%")//灯亮时段5有人
                        $("#slideTest13").children().children(".layui-slider-wrap").css("bottom",res.info.powerPeople5+"%")
                        $("#slideTest13").children().children().children(".layui-input").val(res.info.powerPeople5)
                        $("#slideTest14").children().children(".layui-slider-bar").css("height",res.info.powerSensor5+"%")//灯亮时段5无人
                        $("#slideTest14").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensor5+"%")
                        $("#slideTest14").children().children().children(".layui-input").val(res.info.powerSensor5)
                        $("#slideTest6").children().children(".layui-slider-bar").css("height",res.info.powerDawnPeople+"%")//晨亮有人
                        $("#slideTest6").children().children(".layui-slider-wrap").css("bottom",res.info.powerDawnPeople+"%")
                        $("#slideTest6").children().children().children(".layui-input").val(res.info.powerDawnPeople)
                        $("#slideTest7").children().children(".layui-slider-bar").css("height",res.info.powerSensorDown+"%")//晨亮无人
                        $("#slideTest7").children().children(".layui-slider-wrap").css("bottom",res.info.powerSensorDown+"%")
                        $("#slideTest7").children().children().children(".layui-input").val(res.info.powerSensorDown)
                        $("#slideTest15").children().children(".layui-slider-bar").css("width", res.info.autoSleepTime + "%")//自动休眠
                        $("#slideTest15").children().children(".layui-slider-wrap").css("left", res.info.autoSleepTime + "%")
                        $("#slideTest15").children().children().children(".layui-input").val(res.info.autoSleepTime)

                        $("#slideTest_a").children().children(".layui-slider-bar").css("width",res.info.firDownPower/100+"%")//一阶前
                        $("#slideTest_a").children().children(".layui-slider-wrap").css("left",res.info.firDownPower/100+"%")
                        $("#slideTest_a").children().children().children(".layui-input").val(res.info.firDownPower/100)
                        $("#slideTest_b").children().children(".layui-slider-bar").css("width",res.info.firReducAmplitude/100+"%")//一阶后
                        $("#slideTest_b").children().children(".layui-slider-wrap").css("left",res.info.firReducAmplitude/100+"%")
                        $("#slideTest_b").children().children().children(".layui-input").val(res.info.firReducAmplitude/100)
                        $("#slideTest_c").children().children(".layui-slider-bar").css("width",res.info.twoDownPower/100+"%")//2阶前
                        $("#slideTest_c").children().children(".layui-slider-wrap").css("left",res.info.twoDownPower/100+"%")
                        $("#slideTest_c").children().children().children(".layui-input").val(res.info.twoDownPower/100)
                        $("#slideTest_d").children().children(".layui-slider-bar").css("width",res.info.twoReducAmplitude/100+"%")//2阶后
                        $("#slideTest_d").children().children(".layui-slider-wrap").css("left",res.info.twoReducAmplitude/100+"%")
                        $("#slideTest_d").children().children().children(".layui-input").val(res.info.twoReducAmplitude/100)
                        $("#slideTest_e").children().children(".layui-slider-bar").css("width",res.info.threeDownPower/100+"%")//3阶前
                        $("#slideTest_e").children().children(".layui-slider-wrap").css("left",res.info.threeDownPower/100+"%")
                        $("#slideTest_e").children().children().children(".layui-input").val(res.info.threeDownPower/100)
                        $("#slideTest_f").children().children(".layui-slider-bar").css("width",res.info.threeReducAmplitude/100+"%")//3阶后
                        $("#slideTest_f").children().children(".layui-slider-wrap").css("left",res.info.threeReducAmplitude/100+"%")
                        $("#slideTest_f").children().children().children(".layui-input").val(res.info.threeReducAmplitude/100)
                        $("#slideTest1").children().children(".layui-slider-bar").css("width",res.info.switchDelayTime+"%")//开关灯延时
                        $("#slideTest1").children().children(".layui-slider-wrap").css("left",res.info.switchDelayTime+"%")
                        $("#slideTest1").children().children().children(".layui-input").val(res.info.switchDelayTime)
                        $("#slideTest_m").children().children(".layui-slider-bar").css("width",res.info.ligntOnDuration+"%")//光控延时时间
                        $("#slideTest_m").children().children(".layui-slider-wrap").css("left",res.info.ligntOnDuration+"%")
                        $("#slideTest_m").children().children().children(".layui-input").val(res.info.ligntOnDuration)
                    }
                }
            });
        }
    }
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
    var posList = [
        'left', 'right', 'top', 'bottom',
        'inside',
        'insideTop', 'insideLeft', 'insideRight', 'insideBottom',
        'insideTopLeft', 'insideTopRight', 'insideBottomLeft', 'insideBottomRight'
    ];
    app.configParameters = {
        rotate: {
            min: -90,
            max: 90
        },
        verticalAlign: {
            options: {
                top: 'top',
                middle: 'middle',
                bottom: 'bottom'
            }
        },
        position: {
            options: echarts.util.reduce(posList, function (map, pos) {
                map[pos] = pos;
                return map;
            }, {})
        },
        distance: {
            min: 0,
            max: 100
        }
    };
    app.config = {
        rotate: 90,
        align: 'left',
        verticalAlign: 'middle',
        position: 'insideBottom',
        distance: 15,
        onChange: function () {
            var labelOption = {
                normal: {
                    rotate: app.config.rotate,
                    align: app.config.align,
                    verticalAlign: app.config.verticalAlign,
                    position: app.config.position,
                    distance: app.config.distance
                }
            };
            myChart.setOption({
                series: [{
                    label: labelOption
                },{
                    label: labelOption
                }]
            });
        }
    };
    var labelOption = {
        normal: {
            show: false, //图标上显示数据
            position: app.config.position,
            distance: app.config.distance,
            align: app.config.align,
            verticalAlign: app.config.verticalAlign,
            rotate: app.config.rotate,
            formatter: '{c}  {name|{a}}',
            rich: {
                name: {
                    textBorderColor: '#fff'
                }
            }
        }
    };
    option = {
        color: ['#003366', '#006699'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['充电量', '放电量']
        },
        toolbox: {
            show: true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                axisTick: {show: false},
                data:time
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '充电量',
                type: 'bar',
                smooth: true,
                barGap: 0,
                label: labelOption,
                data: chargingCapacityList
            },
            {
                name: '放电量',
                type: 'bar',
                smooth: true,
                label: labelOption,
                data: dischargeCapacityList
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
        title: {
            text: '充电电流/放电电流'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['充电电流','放电电流']
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
            left: '9%',
            bottom: -8,
            height: '8%',//组件高度
            start: 5,
            end:80 //初始化滚动条
        }],
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'充电电流',
                type:'line',
                smooth: true,
                stack: '总量',
                data:chargingCurrentList
            },
            {
                name:'放电电流',
                type:'line',
                smooth: true,
                stack: '总量',
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
            left: '9%',
            bottom: -8,
            height: '8%',//组件高度
            start: 5,
            end:80 //初始化滚动条
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
                stack: '总量',
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
        title: {
            text: '环境温度/内部温度'
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
            left: '9%',
            bottom: -8,
            height: '8%',//组件高度
            start: 5,
            end:80 //初始化滚动条
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
                stack: '总量',
                smooth: true,
                areaStyle: {},
                data:ambientTemperatureList,
            },
            {
                name:'内部温度',
                type:'line',
                stack: '总量',
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
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'人流量',
                type:'line',
                smooth: true,
                stack: '总量',
                data:visitorsFlowrateList
            },
            {
                name:'感应次数',
                type:'line',
                smooth: true,
                stack: '总量',
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
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'气象信息',
                type:'line',
                smooth: true,
                stack: '总量',
                data:meteorologicalList
            }
        ]
    };
    if (option_t && typeof option_t === "object") {
        myChart_t.setOption(option_t, true);
    }

}
