$(function(){
    $(".tab li").click(function() {
        $(".tab li").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
        $(".option_a>.option").hide().eq($(this).index()).show();
        //另一种方法: $("div").eq($(".tab li").index(this)).addClass("on").siblings().removeClass('on');
    });

//充电量
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    app.title = '环形图';

    option = {
        legend: {},
        tooltip: {},
        dataset: {
            dimensions: ['product', '2015', '2016', '2017'],
            source: [
                {product: 'Matcha Latte', '2015': 43.3, '2016': 85.8, '2017': 93.7},
                {product: 'Milk Tea', '2015': 83.1, '2016': 73.4, '2017': 55.1},
                {product: 'Cheese Cocoa', '2015': 86.4, '2016': 65.2, '2017': 82.5},
                {product: 'Walnut Brownie', '2015': 72.4, '2016': 53.9, '2017': 39.1}
            ]
        },
        xAxis: {type: 'category'},
        yAxis: {},
        // Declare several bar series, each will be mapped
        // to a column of dataset.source by default.
        series: [
            {type: 'bar'},
            {type: 'bar'},
            {type: 'bar'}
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




    //取值
    $("#confirm_x").click(function () {

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
        var r_wunl = $("#r_wunl").width();
        var r_wen1 = $("#r_wen1").width();
        var r_wen2 = $("#r_wen2").width();
        var r_wen3 = $("#r_wen3").width();
        var r_wen4 = $("#r_wen4").width();
        var r_wen5 = $("#r_wen5").width();
        var r_wen6 = $("#r_wen6").width();
        var r_wen7 = $("#r_wen7").width();
        var r_wen8 = $("#r_wen8").width();
        var r_wen9 = $("#r_wen9").width();
        var r_wen10 = $("#r_wen10").width();
        var r_wen11 = $("#r_wen11").width();
        var r_wen12 = $("#r_wen12").width();
        var rc_select_b=$("#rc_select_b option:selected").text();
        var r_jin_z = $("#r_jin_z").width();







        console.log(r_jin_z )


    })



























})
