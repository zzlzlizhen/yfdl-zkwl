$(function () {
    function PercentPie(option){
        this.backgroundColor = option.backgroundColor||'#fff';
        this.color           = option.color||['#38a8da','#d4effa'];
        this.fontSize        = option.fontSize||14;
        this.domEle          = option.domEle;
        this.value           = option.value;
        this.name            = option.name;
        this.title           = option.title;
    }
    PercentPie.prototype.init = function(){
        var _that = this;
        var option = {
            textStyle: {
                color: '#333333'          // 图例文字颜色
            },
            backgroundColor:_that.backgroundColor,
            color:_that.color,
            title: {
                text: _that.title,
                top:'3%',
                left:'1%',
                textStyle:{
                    color: '#333',
                    fontStyle: 'normal',
                    fontWeight: 'normal',
                    fontFamily: 'sans-serif',
                    fontSize: 16,
                }
            },
            series: [{
                name: '来源',
                type: 'pie',
                radius: ['60%', '75%'],
                avoidLabelOverlap: false,
                hoverAnimation:false,
                label: {
                    normal: {
                        show: false,
                        position: 'center',
                        textStyle: {
                            fontSize: _that.fontSize,
                            fontWeight: 'bold'
                        },
                        formatter:'{b}\n{c}%'
                    }
                },
                data: [{
                    value: _that.value,
                    name: _that.name,
                    label:{
                        normal:{
                            show:true
                        }
                    }
                },
                    {
                        value: 100-_that.value,
                        name: ''
                    }
                ]
            }]
        };

        echarts.init(_that.domEle).setOption(option);
    };

    ////////////
    var jsonarr=[];
    var failRate
    $.ajax({
        url:" /overview/overview",
        type:"GET",
        success:function (res) {
            console.log("=======11111")
            console.log(res)
            var lampsNum = res.info.lampsNum;
            $(".r_dengshu").html(lampsNum);
            var gatewaysNum = res.info.gatewaysNum;
            $(".r_wangshu").html(gatewaysNum);
            var lightingArea = res.info.lightingArea;
            $("#enum").html(lightingArea);
            var reduCarbonEmi = res.info.reduCarbonEmi;
            $("#rtan").html(reduCarbonEmi);
            console.log("111");
            console.log(res);
            var failRate_b=res.info.failRate;
            var failRate_a=failRate_b.substr(0, failRate_b.length - 1);
            var failRate_b=Number(failRate_a);
            failRate=failRate_b;
            for(var i=0;i<res.info.deviceInfoList.length;i++){
                if(res.info.deviceInfoList[i]!=null){
                    var json={
                        name: "",
                        value: [Number(res.info.deviceInfoList[i].longitude),Number(res.info.deviceInfoList[i].latitude),30],
                    }
                    jsonarr.push(json);
                }
            }
            var myChart = echarts.init(document.getElementById('main'));
            var color  = ['#00D3C8','#00D3C8'];
            var series=[];
            series.push({
                type: 'effectScatter',
                coordinateSystem: 'geo',
                zlevel: 3,
                rippleEffect: {
                    brushType: 'stroke'
                },
                label: {
                    normal: {
                        show: true,
                        position: 'left',
                        formatter: '{b}'
                    }
                },
                symbolSize: function(val) {
                    return val[2] / 8;
                },
                itemStyle: {
                    normal: {
                        color: color[1]
                    }
                },
                data: jsonarr
            });

            option5 = {
                title: {
                    text: 'demo',
                    textStyle: {
                        color: '#fff',
                        fontSize: 40
                    },
                    top: '10px',
                    left: '10px'
                },
                geo: {
                    map: 'world',       // 与引用进来的地图js名字一致
                    roam: false,        // 禁止缩放平移
                    itemStyle: {        // 每个区域的样式
                        normal: {
                            areaColor: '#1E90FF'
                        },
                        emphasis: {
                            areaColor: '#1E90FF'
                        }
                    },
                    regions: [{        // 选中的区域
                        name: 'China',
                        selected: true,
                        itemStyle: {   // 高亮时候的样式
                            emphasis: {
                                areaColor: '#1E90FF'
                            }
                        },
                        label: {    // 高亮的时候不显示标签
                            emphasis: {
                                show: false
                            }
                        }
                    }]
                },
                series: series,   // 将之前处理的数据放到这里
                textStyle: {
                    fontSize: 12
                }
            };
            myChart.setOption(option5);

            //故障率
            var option1 = {
                value:failRate,//百分比,必填
                name:'故障率',//必填
                // title:'学习成绩',
                backgroundColor:null,
                color:['#FDC03D','#CCCCCC'],
                fontSize:14,
                domEle:document.getElementById("pieDiagram")//必填

            },percentPie1 = new PercentPie(option1);
            percentPie1.init();
            var totalDc=res.info.totalDc;
        //    发电量.功率
            myChaRt(totalDc);
        }

    })

})


function  myChaRt(totalDc) {
    console.log("图表");
    console.log(totalDc);
    //累计发点数据(数据不全代补充循环)
    var mouth=totalDc[0].mouth;
    var totalDC=totalDc[0].totalDC;

   //冲放电量(没有接口数据，城市等)
    var city=["北京","上海","广州","香港"];
    var f_Ang=[2100,9761,4586,1298];
    var c_Ong=[1200,2375,2345,6792];

    //月累计发电量
    var myChart = echarts.init(document.getElementById('r_rchers_l'));
    var colors = ['#5793f3', '#d14a61', '#675bba'];
    option = {
        title: {
            text: '累计发电量'
        },
        color: colors,
        tooltip: {
            trigger: 'none',
            axisPointer: {
                type: 'cross',
            }
        },
        grid: {
            top: 70,
            bottom: 50
        },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLabel: {
                    show: true,
                    // textStyle: {
                    //     color: '#DEDFDF'
                    // }
                },
                axisLine: {
                    onZero: false,
                    // lineStyle: {
                    //     color:'#ccc'
                    // }
                },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return '发电量  ' + params.value
                                + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
                        }
                    }
                },
                data:[mouth]
            },
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: colors[0]
                    }
                },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return '发电量  ' + params.value
                                + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
                        }
                    }
                },
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    show: true,
                },
                axisLine: {
                },
            }
        ],
        areaStyle: {
            normal: {
                type: 'default',
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: '#4a9eff' // 区域颜色               
                }, {
                    offset: 1,
                    color: '#4a9eff' // 区域颜色             
                }], false)
            }
        },
        series: [
            {
                name:'发电量',
                type:'line',
                xAxisIndex: 1,
                smooth: true,
                data: [totalDC],
            },

        ]
    };
    myChart.setOption(option);

    //充放电量
    var myChart = echarts.init(document.getElementById('r_rchers_e'));
    option2 = {
        color: ['#27e2d4', '#508fea'],
        title: {
            text: 'LoRa安装数量'
        },
        tooltip: {},
        legend: {
            data: ['充电量', '放电量']
        },
        dataset: {},
        xAxis: [
            {
                type: 'category',
                data:city
            }
        ],
        yAxis: {},
        series: [
            {
                name: '充电量',
                type: 'bar',
                smooth: true,
                barGap: 0,
                data:c_Ong
            },
            {
                name: '放电量',
                type: 'bar',
                smooth: true,
                data: f_Ang
            }
        ]
    };
    myChart.setOption(option2);
}

