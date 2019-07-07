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
    console.log("Code")
    console.log(deviceCode)
    console.log("分组id")
    console.log(grod)
    console.log("设备id")
    console.log(deviceId)



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
    //根据年月日调取渲染数据表格
    function day(deviceCode,va_l){
        $.ajax({
            url: baseURL + 'fun/history/queryHistoryByDay?deviceCode=' + deviceCode+"&time="+va_l,
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
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
                f(hours,dischargeCapacityList,chargingCapacityList,chargingCurrentList,dischargeCurrentList,batteryVoltageList,ambientTemperatureList,internalTemperatureList,visitorsFlowrateList,inductionFrequencyList,meteorologicalList)
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
                f(hours,dischargeCapacityList,chargingCapacityList,chargingCurrentList,dischargeCurrentList,batteryVoltageList,ambientTemperatureList,internalTemperatureList,visitorsFlowrateList,inductionFrequencyList,meteorologicalList)
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
                f(hours,dischargeCapacityList,chargingCapacityList,chargingCurrentList,dischargeCurrentList,batteryVoltageList,ambientTemperatureList,internalTemperatureList,visitorsFlowrateList,inductionFrequencyList,meteorologicalList)
            }
        })
    }


    $(".tab li").click(function() {
        $(".tab li").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
        $(".option_a>.option").hide().eq($(this).index()).show();
        //另一种方法: $("div").eq($(".tab li").index(this)).addClass("on").siblings().removeClass('on');
    });

//高级设置自定义
    $("#custom").click(function(){
        $(".shade_project,.shade_b_project,.Bian_j").show()
    })
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project,.Bian_j").hide()
    })


    if(deviceCode != ""){
        //设备高级设置数据渲染
        console.log("设备高级设置上传")
        $.ajax({
            url:baseURL + 'advancedsetting/queryDevAdvInfo?deviceCode='+deviceCode+"&groupId="+grod,
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data: {},
            success: function(res){
                console.log("设备高级设置参数获取")
                console.log(res)

            }
        });
        //设备保存值
        $("#r_bttn").click(function () {
            //负载
            var loadWorkMode = $("#r_btn_l>.r_btn_l[type='radio']:checked").val();   //负载工作模式
            var powerLoad = $("#r_Load_power").val(); //负载功率
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
            var powerPeople1=parseInt($("#slideTest4").children().children(".layui-slider-bar").width()/$("#slideTest4").parent(".progres").width()*100); //时段1 有人功率
            var powerPeople2=parseInt($("#slideTest6").children().children(".layui-slider-bar").width()/$("#slideTest6").parent(".progres").width()*100);//时段2 有人功率
            var powerPeople3=parseInt($("#slideTest8").children().children(".layui-slider-bar").width()/$("#slideTest8").parent(".progres").width()*100); //时段3 有人功率
            var powerPeople4=parseInt($("#slideTest10").children().children(".layui-slider-bar").width()/$("#slideTest10").parent(".progres").width()*100);//时段4 有人功率
            var powerPeople5=parseInt($("#slideTest12").children().children(".layui-slider-bar").width()/$("#slideTest12").parent(".progres").width()*100); //时段5 有人功率
            var powerDawnPeople=parseInt($("#slideTest13").children().children(".layui-slider-bar").width()/$("#slideTest13").parent(".progres").width()*100);//晨亮 有人功率
            var powerSensor1=parseInt($("#slideTest5").children().children(".layui-slider-bar").width()/$("#slideTest5").parent(".progres").width()*100); //时段1 无人功率
            var powerSensor2=parseInt($("#slideTest7").children().children(".layui-slider-bar").width()/$("#slideTest7").parent(".progres").width()*100);//时段2 无人功率
            var powerSensor3=parseInt($("#slideTest9").children().children(".layui-slider-bar").width()/$("#slideTest9").parent(".progres").width()*100);//时段3无人功率
            var powerSensor4=parseInt($("#slideTest11").children().children(".layui-slider-bar").width()/$("#slideTest11").parent(".progres").width()*100); //时段4 无人功率
            var powerSensor5=parseInt($("#slideTest16").children().children(".layui-slider-bar").width()/$("#slideTest16").parent(".progres").width()*100);//时段5 无人功率
            var powerSensorDown=parseInt($("#slideTest14").children().children(".layui-slider-bar").width()/$("#slideTest14").parent(".progres").width()*100); //晨亮 无人功率
            var autoSleepTime=parseInt($("#slideTest15").children().children(".layui-slider-bar").width()/$("#slideTest15").parent(".progres").width()*100); //自动休眠时间长度
            //光电池
            var vpv=$("#r_teml_tgl").val();//光控电压
            var pvSwitch=$("#r_btl_th>.r_btl_th[type='radio']:checked").val(); //光控关灯开灯
            var batType=$("#r_select_b option:selected").attr("class");// 电池类型
            //蓄电池
            var batStringNum= $("#r_teml_forl").val()//电池串数
            var volOverDisCharge=$("#r_inpl_one").val()//过放电压
            var volCharge=$("#chong").val()//充电电压
            var tempCharge=parseInt($("#slideTest3").children().children(".layui-slider-bar").width()/$("#slideTest3").parent(".progres").width()*100); //充电温度范围(最大值和最小值做高八位低八位处理)
            var tempDisCharge=parseInt($("#slideTest17").children().children(".layui-slider-bar").width()/$("#slideTest17").parent(".progres").width()*100); //放电温度范围（最大值和最小值做高八位低八位处理)）
            var inspectionTime=Number($("#r_teml_thgl").val())*60+Number($("#r_teml_thel").val()) //巡检时间
            var icharge=$("#r_inpl_tw").val();//充电电流
            var firDownPower=parseInt($("#slideTest_a").children().children(".layui-slider-bar").width()/$("#slideTest_a").parent(".progres").width()*100); //一阶降压功率
            var twoDownPower=parseInt($("#slideTest_c").children().children(".layui-slider-bar").width()/$("#slideTest_c").parent(".progres").width()*100);//二阶降压功率
            var threeDownPower=parseInt($("#slideTest_e").children().children(".layui-slider-bar").width()/$("#slideTest_e").parent(".progres").width()*100);//三阶降压功率
            var firReducAmplitude=parseInt($("#slideTest_b").children().children(".layui-slider-bar").width()/$("#slideTest_b").parent(".progres").width()*100);//一阶降功率幅度
            var twoReducAmplitude=parseInt($("#slideTest_d").children().children(".layui-slider-bar").width()/$("#slideTest_d").parent(".progres").width()*100);//二阶降功率幅度
            var threeReducAmplitude=parseInt($("#slideTest_f").children().children(".layui-slider-bar").width()/$("#slideTest_f").parent(".progres").width()*100);//三阶降功率幅度

            $.ajax({
                url:baseURL + 'advancedsetting/updateDevice',
                contentType: "application/json;charset=UTF-8",
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

                    "&savingSwitch="+ "" + //节能开关
                    "&autoSleepTime="+ autoSleepTime + //自动休眠时间长度
                    "&vpv="+ vpv + //光控电压
                    "&pvSwitch="+ pvSwitch +//光控关灯开灯

                    "&ligntOnDuration="+ "" +//光控延迟时间长度
                    "&batType="+ batType + // 电池类型 0:未设置；1：胶体电池；2：铅酸电池； 3磷酸铁锂电池；4：三元锂电池
                    "&batStringNum="+batStringNum +//电池串数
                    "&volOverDisCharge="+ volOverDisCharge + //过放电压
                    "&volCharge="+ volCharge +//充电电压
                    "&tempCharge="+ tempCharge +//充电温度范围(最大值和最小值做高八位低八位处理)
                    "&tempDisCharge="+ tempDisCharge +//放电温度范围（最大值和最小值做高八位低八位处理)）
                    "&inspectionTime="+ inspectionTime+//巡检时间
                    "&icharge="+ icharge + //充电电流

                    "&inductionSwitch="+ "" +//感应开关
                    "&inductionLightOnDelay="+ "" +//感应后的亮灯延时
                    "&firDownPower="+ firDownPower + //一阶降压功率
                    "&twoDownPower="+ twoDownPower + //二阶降压功率
                    "&threeDownPower="+ threeDownPower + //三阶降压功率
                    "&firReducAmplitude="+ firReducAmplitude + //一阶降功率幅度
                    "&twoReducAmplitude="+ twoReducAmplitude + //二阶降功率幅度
                    "&threeReducAmplitude="+ threeReducAmplitude //三阶降功率幅度
                    ,
                success: function(res){
                   console.log("设备高级设置上传成功")
                }
            });
        })
    }else{
        console.log("分组高级设置上传");
        $.ajax({
            url:baseURL + 'advancedsetting/settingInfo?groupId='+grod,
            contentType: "application/json;charset=UTF-8",
            type:"get",
            data: {},
            success: function(res){
                console.log("分组高级设置参数获取")
                console.log(res)
            }
        });
        //分组保存值
        $("#r_bttn").click(function () {
            //负载
            var loadWorkMode = $("#r_btn_l>.r_btn_l[type='radio']:checked").val();   //负载工作模式
            var powerLoad = $("#r_Load_power").val(); //负载功率
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
            var powerPeople1=parseInt($("#slideTest4").children().children(".layui-slider-bar").width()/$("#slideTest4").parent(".progres").width()*100); //时段1 有人功率
            var powerPeople2=parseInt($("#slideTest6").children().children(".layui-slider-bar").width()/$("#slideTest6").parent(".progres").width()*100);//时段2 有人功率
            var powerPeople3=parseInt($("#slideTest8").children().children(".layui-slider-bar").width()/$("#slideTest8").parent(".progres").width()*100); //时段3 有人功率
            var powerPeople4=parseInt($("#slideTest10").children().children(".layui-slider-bar").width()/$("#slideTest10").parent(".progres").width()*100);//时段4 有人功率
            var powerPeople5=parseInt($("#slideTest12").children().children(".layui-slider-bar").width()/$("#slideTest12").parent(".progres").width()*100); //时段5 有人功率
            var powerDawnPeople=parseInt($("#slideTest13").children().children(".layui-slider-bar").width()/$("#slideTest13").parent(".progres").width()*100);//晨亮 有人功率
            var powerSensor1=parseInt($("#slideTest5").children().children(".layui-slider-bar").width()/$("#slideTest5").parent(".progres").width()*100); //时段1 无人功率
            var powerSensor2=parseInt($("#slideTest7").children().children(".layui-slider-bar").width()/$("#slideTest7").parent(".progres").width()*100);//时段2 无人功率
            var powerSensor3=parseInt($("#slideTest9").children().children(".layui-slider-bar").width()/$("#slideTest9").parent(".progres").width()*100);//时段3无人功率
            var powerSensor4=parseInt($("#slideTest11").children().children(".layui-slider-bar").width()/$("#slideTest11").parent(".progres").width()*100); //时段4 无人功率
            var powerSensor5=parseInt($("#slideTest16").children().children(".layui-slider-bar").width()/$("#slideTest16").parent(".progres").width()*100);//时段5 无人功率
            var powerSensorDown=parseInt($("#slideTest14").children().children(".layui-slider-bar").width()/$("#slideTest14").parent(".progres").width()*100); //晨亮 无人功率
            var autoSleepTime=parseInt($("#slideTest15").children().children(".layui-slider-bar").width()/$("#slideTest15").parent(".progres").width()*100); //自动休眠时间长度
            //光电池
            var vpv=$("#r_teml_tgl").val();//光控电压
            var pvSwitch=$("#r_btl_th>.r_btl_th[type='radio']:checked").val(); //光控关灯开灯
            var batType=$("#r_select_b option:selected").attr("class");// 电池类型
            //蓄电池
            var batStringNum= $("#r_teml_forl").val()//电池串数
            var volOverDisCharge=$("#r_inpl_one").val()//过放电压
            var volCharge=$("#chong").val()//充电电压
            var tempCharge=parseInt($("#slideTest3").children().children(".layui-slider-bar").width()/$("#slideTest3").parent(".progres").width()*100); //充电温度范围(最大值和最小值做高八位低八位处理)
            var tempDisCharge=parseInt($("#slideTest17").children().children(".layui-slider-bar").width()/$("#slideTest17").parent(".progres").width()*100); //放电温度范围（最大值和最小值做高八位低八位处理)）
            var inspectionTime=Number($("#r_teml_thgl").val())*60+Number($("#r_teml_thel").val()) //巡检时间
            var icharge=$("#r_inpl_tw").val();//充电电流
            var firDownPower=parseInt($("#slideTest_a").children().children(".layui-slider-bar").width()/$("#slideTest_a").parent(".progres").width()*100); //一阶降压功率
            var twoDownPower=parseInt($("#slideTest_c").children().children(".layui-slider-bar").width()/$("#slideTest_c").parent(".progres").width()*100);//二阶降压功率
            var threeDownPower=parseInt($("#slideTest_e").children().children(".layui-slider-bar").width()/$("#slideTest_e").parent(".progres").width()*100);//三阶降压功率
            var firReducAmplitude=parseInt($("#slideTest_b").children().children(".layui-slider-bar").width()/$("#slideTest_b").parent(".progres").width()*100);//一阶降功率幅度
            var twoReducAmplitude=parseInt($("#slideTest_d").children().children(".layui-slider-bar").width()/$("#slideTest_d").parent(".progres").width()*100);//二阶降功率幅度
            var threeReducAmplitude=parseInt($("#slideTest_f").children().children(".layui-slider-bar").width()/$("#slideTest_f").parent(".progres").width()*100);//三阶降功率幅度
            console.log(grod);
            $.ajax({
                url:baseURL + 'advancedsetting/updateGroup',
                type:"POST",
                data:
                    "&groupId="+ grod + //分组id
                    "&loadWorkMode="+ "1" + //负载工作模式
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

                    "&savingSwitch="+ "" + //节能开关
                    "&autoSleepTime="+ autoSleepTime + //自动休眠时间长度
                    "&vpv="+ vpv + //光控电压
                    "&pvSwitch="+ pvSwitch +//光控关灯开灯

                    "&ligntOnDuration="+ "" +//光控延迟时间长度
                    "&batType="+ batType + // 电池类型 0:未设置；1：胶体电池；2：铅酸电池； 3磷酸铁锂电池；4：三元锂电池
                    "&batStringNum="+batStringNum +//电池串数
                    "&volOverDisCharge="+ volOverDisCharge + //过放电压
                    "&volCharge="+ volCharge +//充电电压
                    "&tempCharge="+ tempCharge +//充电温度范围(最大值和最小值做高八位低八位处理)
                    "&tempDisCharge="+ tempDisCharge +//放电温度范围（最大值和最小值做高八位低八位处理)）
                    "&inspectionTime="+ inspectionTime+//巡检时间
                    "&icharge="+ icharge + //充电电流

                    "&inductionSwitch="+ "" +//感应开关
                    "&inductionLightOnDelay="+ "" +//感应后的亮灯延时
                    "&firDownPower="+ firDownPower + //一阶降压功率
                    "&twoDownPower="+ twoDownPower + //二阶降压功率
                    "&threeDownPower="+ threeDownPower + //三阶降压功率
                    "&firReducAmplitude="+ firReducAmplitude + //一阶降功率幅度
                    "&twoReducAmplitude="+ twoReducAmplitude + //二阶降功率幅度
                    "&threeReducAmplitude="+ threeReducAmplitude //三阶降功率幅度
                ,
                success: function(res){
                    console.log("分组高级设置上传成功")
                }
            });
        })
    }




    $("#r_cl").click(function(){
        $("#slideTest1").children().children(".layui-slider-bar").width((50/$(".progres").width()*$(".progres").width())+"%");
        // $(".layui-slider-wrap").lef(30)
    })

})

function  f(hours,dischargeCapacityList,chargingCapacityList,chargingCurrentList,dischargeCurrentList,batteryVoltageList,ambientTemperatureList,internalTemperatureList,visitorsFlowrateList,inductionFrequencyList,meteorologicalList) {

    var time =hours
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
                barGap: 0,
                label: labelOption,
                data: chargingCapacityList
            },
            {
                name: '放电量',
                type: 'bar',
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
            data: time
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'充电电流',
                type:'line',
                stack: '总量',
                data:chargingCurrentList
            },
            {
                name:'放电电流',
                type:'line',
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
                data : time
            }
        ],
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
                data : time
            }
        ],
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
                areaStyle: {},
                data:ambientTemperatureList,
            },
            {
                name:'内部温度',
                type:'line',
                stack: '总量',
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
                stack: '总量',
                data:visitorsFlowrateList
            },
            {
                name:'感应次数',
                type:'line',
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
                stack: '总量',
                data:meteorologicalList
            }
        ]
    };
    if (option_t && typeof option_t === "object") {
        myChart_t.setOption(option_t, true);
    }
}
