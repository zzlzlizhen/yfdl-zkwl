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
    //设备编号
    var deviceCode=href['deviceCode'];
    var partition=deviceCode.split(",");
    console.log(partition[1])

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

    function day(deviceCode,va_l){
        console.log(deviceCode)
        console.log(va_l)
        $.ajax({
            url: baseURL + 'fun/history/queryHistoryByDay?deviceCode=' + deviceCode+"&time="+va_l,
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
                console.log("日")
                console.log(res)
            }
        })
    }
    function Month(deviceCode,va_l){
        var va_y=va_l.split("-");
        $.ajax({
            url: baseURL + 'fun/history/queryHistoryByMouth?deviceCode=' + deviceCode+"&year="+va_y[0]+"&year="+va_y[1],
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
                console.log("月")
                console.log(res)
            }
        })
    }
    function Year(deviceCode,va_l){
        $.ajax({
            url: baseURL + 'fun/history/queryHistoryByMouth?deviceCode=' + deviceCode+"&year="+va_l,
            contentType: "application/json;charset=UTF-8",
            type: "get",
            data: {},
            success: function (res) {
                console.log("年")
                console.log(res)
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
        var r_teml_t = $(".rda").val();
        var r_one_teml = $("#r_one_teml").val();
        var r_teml_tw = $("#r_teml_tw").val();
        var  r_teml_th = $("#r_teml_th").val();
        var r_teml_for = $("#r_teml_for").val();
        var r_teml_fif = $("#r_teml_fif").val();
        var r_teml_six = $("#r_teml_six").val();
        var r_Load_power = $("#r_Load_power").val();
        var r_teml_sv =$("#r_teml_sv").val();
        var r_teml_eg = $("#r_teml_eg").val();
        var r_teml_ng = $("#r_teml_ng").val();
        var r_teml_te = $("#r_teml_te").val();
        var r_teml_el = $("#r_teml_el").val();
        var r_teml_egl = $("#r_teml_egl").val();
        var r_teml_tgl = $("#r_teml_tgl").val();
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
        var r_wunl = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wen1 = $("#slideTest2").children().children(".layui-slider-bar").width();
        var r_wen2 = $("#slideTest3").children().children(".layui-slider-bar").width();
        var r_wen3 = $("#slideTest4").children().children(".layui-slider-bar").width();
        var r_wen4 = $("#slideTest5").children().children(".layui-slider-bar").width();
        var r_wen5 = $("#slideTest6").width();
        var r_wen6 = $("#slideTest7").width();
        var r_wen7 = $("#slideTest8").width();
        var r_wen8 = $("#slideTest9").width();
        var r_wen9 = $("#slideTest10").width();
        var r_wen10 = $("#slideTest11").width();
        var r_wen11 = $("#slideTest12").width();
        var r_wen12 = $("#slideTest13").width();
        var slideTest14 = $("#slideTest14").width();
        var slideTest15 = $("#slideTest15").width();
        var slideTest16 = $("#slideTest16").width();
        var slideTest17 = $("#slideTest17").width();
        var slideTest18 = $("#slideTest18").width();
        var slideTest19 = $(".layui-slider-input").width();
        var rc_select_b=$("#rc_select_b option:selected").text();
        var r_jin_z = $("#r_jin_z").width();

        var a =  r_wunl/$(".progres").width()*100
        var abc = Math.ceil(a);
        console.log( abc)

    })


    $("#r_cl").click(function(){
        $("#slideTest1").children().children(".layui-slider-bar").width((50/$(".progres").width()*$(".progres").width())+"%");
        // $(".layui-slider-wrap").lef(30)
    })

    f()

})

function  f(p_id) {
    //充电量
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    app.title = '环形图';
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
        align: {
            options: {
                left: 'left',
                center: 'center',
                right: 'right'
            }
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
            show: true,
            position: app.config.position,
            distance: app.config.distance,
            align: app.config.align,
            verticalAlign: app.config.verticalAlign,
            rotate: app.config.rotate,
            formatter: '{c}  {name|{a}}',
            fontSize: 16,
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
                data: ['2012', '2013', '2014', '2015', '2016']
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
                data: [320, 332, 301, 334, 390]
            },
            {
                name: '放电量',
                type: 'bar',
                label: labelOption,
                data: [220, 182, 191, 234, 290]
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
    app_z.title = '环形图';

    option_z = {
        title: {
            text: '折线图堆叠'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['邮件营销','联盟广告']
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
            data: ['周一','周二','周三','周四','周五','周六','周日']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'邮件营销',
                type:'line',
                stack: '总量',
                data:[120, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'联盟广告',
                type:'line',
                stack: '总量',
                data:[220, 182, 191, 234, 290, 330, 310]
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
    app_c.title = '环形图';

    option_c = {
        title: {
            text: '堆叠区域图'
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
            data:['邮件营销']
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
                data : ['周一','周二','周三','周四','周五','周六','周日']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'邮件营销',
                type:'line',
                stack: '总量',
                areaStyle: {},
                data:[120, 132, 101, 134, 90, 230, 210]
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
    app_h.title = '环形图';

    option_h = {
        title: {
            text: '堆叠区域图'
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
            data:['邮件营销','联盟广告']
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
                data : ['周一','周二','周三','周四','周五','周六','周日']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'邮件营销',
                type:'line',
                stack: '总量',
                areaStyle: {},
                data:[120, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'联盟广告',
                type:'line',
                stack: '总量',
                areaStyle: {},
                data:[220, 182, 191, 234, 290, 330, 310]
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
    app_r.title = '环形图';

    option_r = {
        title: {
            text: '折线图堆叠'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['邮件营销','联盟广告']
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
            data: ['周一','周二','周三','周四','周五','周六','周日']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'邮件营销',
                type:'line',
                stack: '总量',
                data:[120, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'联盟广告',
                type:'line',
                stack: '总量',
                data:[220, 182, 191, 234, 290, 330, 310]
            },
        ]
    };
    if (option_r && typeof option_r === "object") {
        myChart_r.setOption(option_r, true);
    }


}
