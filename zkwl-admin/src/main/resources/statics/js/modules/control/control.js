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





})
