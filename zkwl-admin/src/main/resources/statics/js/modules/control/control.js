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
    var partition=deviceCode.split(",");
    console.log(partition[1])

    //判断高级设置页面针对是单台设备还是多台设备（做处理）
    if(partition[1] == undefined ){
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
    //取值
    $("#r_bttn").click(function () {
        var r_btn_l = $("#r_btn_l input[type='radio']:checked").val();
        var r_btne_l = $("#r_btne_l input[type='radio']:checked").val();
        var r_l_p = $("#r_Load_power").val();
        var r_teml = $("#d414").val();
        var r_temlr = $("#d4141").val();
        var r_teml_t = $(".rda").val();
        var r_one_teml = Number($("#r_one_teml").val())*60+Number($("#r_teml_tw").val());
        // var r_teml_tw = $("#r_teml_tw").val();
        var  r_teml_th = Number($("#r_teml_th").val())*60+Number($("#r_teml_for").val());
        // var r_teml_for = $("#r_teml_for").val();
        var r_teml_fif =Number($("#r_teml_fif").val())*60+Number($("#r_teml_six").val());
        // var r_teml_six = $("#r_teml_six").val();
        var r_Load_power = $("#r_Load_power").val();
        var r_teml_sv =Number($("#r_teml_sv").val())*60+Number($("#r_teml_eg").val());
        // console.log(r_teml_sv)
        // console.log("llllllllllllllllllllllllllll")
        // var r_teml_eg = $("#r_teml_eg").val();

        var r_teml_ng = Number($("#r_teml_ng").val())*60+Number($("#r_teml_te").val());
        // var r_teml_te = $("#r_teml_te").val();
        var r_teml_el =Number($("#r_teml_el").val())*60+Number($("#r_teml_egl").val());
        // var r_teml_tgl = $("#r_teml_tgl").val();
        var r_btl_th = $("#r_btl_th input[type='radio']:checked").val();
        var r_jin_on = $("#r_jin_on").width();
        var r_jin_tw = $("#r_jin_tw").width();
        var r_teml_forl = $("#r_teml_forl").val()
        var r_inpl_one = $("#r_inpl_one").val()
        var r_inpl_tw = $("#r_inpl_tw").val()
        var r_jinl_th =$("#r_jinl_th").width()
        var r_teml_thgl=$("#r_teml_thgl").val()
        var r_teml_thel =$("#r_teml_thel").val()
        var r_select_b=$("#r_select_b option:selected").text();
        var r_wen13 = parseInt($("#slideTest13").children().children(".layui-slider-bar").width()/$("#slideTest13").parent(".progres").width()*100);
        var r_wen4 = parseInt($("#slideTest4").children().children(".layui-slider-bar").width()/$("#slideTest4").parent(".progres").width()*100);
        var r_wen6 = parseInt($("#slideTest6").children().children(".layui-slider-bar").width()/$("#slideTest6").parent(".progres").width()*100);
        var r_wen8 = parseInt($("#slideTest8").children().children(".layui-slider-bar").width()/$("#slideTest8").parent(".progres").width()*100);
        var r_wen10 = parseInt($("#slideTest10").children().children(".layui-slider-bar").width()/$("#slideTest10").parent(".progres").width()*100);
        var r_wen12 = parseInt($("#slideTest12").children().children(".layui-slider-bar").width()/$("#slideTest12").parent(".progres").width()*100);
        var r_wen14 = parseInt($("#slideTest14").children().children(".layui-slider-bar").width()/$("#slideTest14").parent(".progres").width()*100);
        var r_wen5 = parseInt($("#slideTest14").children().children(".layui-slider-bar").width()/$("#slideTest5").parent(".progres").width()*100);
       // var r_wunl = $("#slideTest1").children().children(".layui-slider-bar").width();
        // console.log(r_wunl)
      //  var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width()/$("#slideTest1").parent(".progres").width();
        // var r_wen2 = $("#slideTest3").children().children(".layui-slider-bar").width();
        // var r_wen3 = $("#slideTest4").children().children(".layui-slider-bar").width();
        // var r_wen4 = $("#slideTest5").children().children(".layui-slider-bar").width();
        // var r_wen5 = $("#slideTest6").width();
        // var r_wen6 = $("#slideTest7").width();
        // var r_wen7 = $("#slideTest8").width();
        // var r_wen8 = $("#slideTest9").width();
        // var r_wen9 = $("#slideTest10").width();
        // var r_wen10 = $("#slideTest11").width();
        // var r_wen11 = $("#slideTest12").width();
        // var r_wen12 = $("#slideTest12").children().children(".layui-slider-bar").width();

        console.log(r_wen12)
        console.log("6666666666666666")
        var slideTest14 = $("#slideTest14").width();
        var slideTest15 = $("#slideTest15").width();
        var slideTest16 = $("#slideTest16").width();
        var slideTest17 = $("#slideTest17").width();
        var slideTest18 = $("#slideTest18").width();
        var slideTest19 = $(".layui-slider-input").width();
        var rc_select_b=$("#rc_select_b option:selected").text();
        var r_jin_z = $("#r_jin_z").width();


        // var abc = Math.ceil(a);
        // console.log(parseInt(r_wen1*100))



        $.ajax({
            url:baseURL + 'advancedsetting/update',
            contentType: "application/json;charset=UTF-8",
            type:"POST",
            data:

                "&loadWorkMode=" + r_btn_l +
                // "&realName=" + pro_s_b +
                "&exclusiveUser=" + r_Load_power +
                "&timeTurnOn=" + r_teml +
                "&timeTurnOff=" + r_temlr +
                "&time1=" + r_one_teml +
                "&time2=" + r_teml_sv +
                "&time3=" + r_teml_th +
                "&time4=" + r_teml_ng +
                "&time5=" + r_teml_fif +
                "&timeDown=" + r_teml_el+
                "&powerDawnPeople=" + r_wen14+
                "&powerPeople1=" + r_btn_l +
                "&powerPeople2=" + r_wen6+
                 "&powerPeople3=" + r_wen8+
                 "&powerPeople4=" + r_wen10+
                "&powerPeople5=" + r_wen12+
                "&powerDawnPeople=" + slideTest13+
                "&powerSensor1=" + slideTest5
                // "&time3=" + r_teml_th +
                // "&time4=" + r_teml_ng +
                // "&time5=" + r_teml_fif +
                // "&timeDown=" + r_teml_el
                ,
            success: function(res){
                window.location.reload()
            }
        });
    })








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
