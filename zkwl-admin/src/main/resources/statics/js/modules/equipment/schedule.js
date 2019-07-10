
function  zhuang(pro_je,groupId,par_id) {
    $(".She").show()
    $(".grouping").hide()
    $.ajax({
        url: baseURL + 'fun/device/getDeviceById?deviceId='+par_id,
        contentType: "application/json;charset=UTF-8",
        type: "get",
        data: {},
        success: function (res) {
            console.log("单台设备")
            console.log(res)
            $(".battery_a").html(res.data.batteryState);
            $(".battery_b").html(res.data.batteryMargin);
            $(".battery_c").html(res.data.batteryVoltage);

            $(".battery_d").html(res.data.photocellState);
            $(".battery_e").html(res.data.photovoltaicCellVoltage);
            $(".battery_f").html(res.data.chargingCurrent);
            $(".battery_g").html(res.data.chargingPower);

            $(".battery_h").html();
            $(".battery_i").html(res.data.loadVoltage);
            $(".battery_l").html(res.data.loadPower);
            $(".battery_o").html(res.data.loadCurrent);
            var luminance=res.data.light;
            var lian_g=res.data.lightingDuration;
            var chen_g=res.data.morningHours;
            var deviceCode=res.data.deviceCode
            $("#slideTest1").children().children(".layui-slider-bar").width((luminance/$(".progres").width()*$(".progres").width())+"%");
            $("#slideTest1").children().children(".layui-slider-tips").html(luminance)
            $("#slideTest1 .layui-slider-wrap").css('left', luminance/$(".progres").width()*$(".progres").width() + '%');

            $("#slideTest2").children().children(".layui-slider-bar").width((lian_g/$(".progres").width()*$(".progres").width())+"%");
            $("#slideTest2").children().children(".layui-slider-tips").html(lian_g)
            $("#slideTest2 .layui-slider-wrap").css('left', lian_g/$(".progres").width()*$(".progres").width() + '%');

            $("#slideTest3").children().children(".layui-slider-bar").width((chen_g/$(".progres").width()*$(".progres").width())+"%");
            $("#slideTest3").children().children(".layui-slider-tips").html(chen_g)
            $("#slideTest3 .layui-slider-wrap").css('left', chen_g/$(".progres").width()*$(".progres").width() + '%');

            var youVal = res.data.onOff; // 1,2 把拿到的开关灯值赋给单选按钮
            $("input[name='category']").each(function(index) {
                if ($("input[name='category']").get(index).value == youVal) {
                    $("input[name='category']").get(index).checked = true;
                }
            });

            //点击开关
            $(".control-label").click(function(){
                var category = parseInt($('input:radio[name="category"]:checked').val());
                var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
                var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
                var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

                var r_wena =  r_wen1/$(".progres").width()*100
                var r_wen_a = Math.ceil(r_wena);
                var r_wenb =  r_wen2/$(".progres").width()*100
                var r_wen_b = Math.ceil(r_wenb);
                var r_wenc =  r_wen3/$(".progres").width()*100
                var r_wen_c = Math.ceil(r_wenc);
                equipment_b(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c,deviceCode)

            })
            //    滑动单台亮度
            $('#slideTest1').blur(function(){
                var category = parseInt($('input:radio[name="category"]:checked').val());
                var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
                var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
                var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

                var r_wena =  r_wen1/$(".progres").width()*100
                var r_wen_a = Math.ceil(r_wena);
                var r_wenb =  r_wen2/$(".progres").width()*100
                var r_wen_b = Math.ceil(r_wenb);
                var r_wenc =  r_wen3/$(".progres").width()*100
                var r_wen_c = Math.ceil(r_wenc);
                equipment_b(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c,deviceCode)
            })
            //    滑动亮灯时长
            $('#slideTest2').blur(function(){
                var category = parseInt($('input:radio[name="category"]:checked').val());
                var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
                var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
                var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

                var r_wena =  r_wen1/$(".progres").width()*100
                var r_wen_a = Math.ceil(r_wena);
                var r_wenb =  r_wen2/$(".progres").width()*100
                var r_wen_b = Math.ceil(r_wenb);
                var r_wenc =  r_wen3/$(".progres").width()*100
                var r_wen_c = Math.ceil(r_wenc);
                equipment_b(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c,deviceCode)
            })
            //    滑动晨亮时长
            $('#slideTest3').blur(function(){
                var category = parseInt($('input:radio[name="category"]:checked').val());
                var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
                var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
                var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

                var r_wena =  r_wen1/$(".progres").width()*100
                var r_wen_a = Math.ceil(r_wena);
                var r_wenb =  r_wen2/$(".progres").width()*100
                var r_wen_b = Math.ceil(r_wenb);
                var r_wenc =  r_wen3/$(".progres").width()*100
                var r_wen_c = Math.ceil(r_wenc);
                equipment_b(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c,deviceCode)
            })
        }

    })
//    单台设备结束
}

//控制分组
function  f_en(pro_je,groupId) {
    $(".She").hide()
    $(".grouping").show()
    //分组饼图
    Bing(pro_je,groupId)
//点击开关
    $(".control-label").click(function(){
        var category = parseInt($('input:radio[name="category"]:checked').val());
        var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
        var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

        var r_wena =  r_wen1/$(".progres").width()*100
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb =  r_wen2/$(".progres").width()*100
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  r_wen3/$(".progres").width()*100
        var r_wen_c = Math.ceil(r_wenc);
        equipment_b(pro_je,groupId,"",category,r_wen_a,r_wen_b,r_wen_c,0)

    })
    //    滑动单台亮度
    $('#slideTest1').blur(function(){
        var category = parseInt($('input:radio[name="category"]:checked').val());
        var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
        var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

        var r_wena =  r_wen1/$(".progres").width()*100
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb =  r_wen2/$(".progres").width()*100
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  r_wen3/$(".progres").width()*100
        var r_wen_c = Math.ceil(r_wenc);
        equipment_b(pro_je,groupId,"",category,r_wen_a,r_wen_b,r_wen_c,0)
    })
    //    滑动亮灯时长
    $('#slideTest2').blur(function(){
        var category = parseInt($('input:radio[name="category"]:checked').val());
        var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
        var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

        var r_wena =  r_wen1/$(".progres").width()*100
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb =  r_wen2/$(".progres").width()*100
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  r_wen3/$(".progres").width()*100
        var r_wen_c = Math.ceil(r_wenc);
        equipment_b(pro_je,groupId,"",category,r_wen_a,r_wen_b,r_wen_c,0)
    })
    //    滑动晨亮时长
    $('#slideTest3').blur(function(){
        var category = parseInt($('input:radio[name="category"]:checked').val());
        var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
        var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

        var r_wena =  r_wen1/$(".progres").width()*100
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb =  r_wen2/$(".progres").width()*100
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  r_wen3/$(".progres").width()*100
        var r_wen_c = Math.ceil(r_wenc);
        equipment_b(pro_je,groupId,"",category,r_wen_a,r_wen_b,r_wen_c,0)
    })
}
function equipment_b(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c,deviceCode) {
    var Code
    if(deviceCode == 0){
        Code=""
    }else{
        Code= [deviceCode]
    }
    $.ajax({
        url: baseURL + 'fun/device/updateOnOffByIds',
        contentType: "application/json;charset=UTF-8",
        type:"POST",
        data:JSON.stringify({
            "projectId":pro_je,//项目id
            "groupId":groupId,//分组id
            "deviceId":par_id,//设备id
            "lightingDuration":r_wen_b,//亮灯时长
            "morningHours":r_wen_c,//晨亮时长
            "light":r_wen_a,//亮度
            "onOff": category,//开关
        }),
        success: function (res) {
            console.log(res)
            if(res.code == "200"){

            }
        }
    })


    //硬件
    $.ajax({
        url:baseURL + 'fun/device/change',
        contentType: "application/json;charset=UTF-8",
        type:"POST",
        data: JSON.stringify({
            "deviceCodes": deviceCode, //需要修改的设备code
            "qaKey": ["category","r_wen_a","r_wen_b","r_wen_c"], //需要修改的参数键
            "value": [category,r_wen_a,r_wen_b,r_wen_c], //需要修改的参数值
            "deviceType": 1, //设备类型
            "status": 2,
            "groupId":groupId,
        }),
        success: function(res) {
            console.log("1111")
            console.log(res)
        }
    })
}

function Bing(pro_je,groupId) {
    console.log("分组切换1")
    console.log(pro_je+groupId)

    $.ajax({
        url: baseURL + 'fun/project/queryProjectById?projectId='+ pro_je+"&groupId="+groupId,
        contentType: "application/json;charset=UTF-8",
        type:"get",
        data:{},
        success: function (res) {
            console.log(res)
            $(".lu_m_a").html(res.data.sumCount)
            $(".lu_m_c").html(res.data.deviceCount)
            if(res.code == "200"){
                var dom= document.getElementById("Bing");
                var myChart = echarts.init(dom);
                var app = {};
                option= null;
                option = {
                    color: ['#ff6e86', '#00ccb3','#f9b712', '#999999'],
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        x: 'left',
                        data:['警告'+res.data.deviceScale[0].警告+"%",'正常'+res.data.deviceScale[1].正常+"%",
                            '故障'+res.data.deviceScale[2].故障+"%",'离线'+res.data.deviceScale[3].离线+"%"]
                    },
                    series: [
                        {
                            name:'访问来源',
                            type:'pie',
                            radius: ['50%', '70%'],
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        fontSize: '30',
                                        fontWeight: 'bold'
                                    }
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data:[
                                {value:res.data.deviceScale[0].警告, name:'警告'+res.data.deviceScale[0].警告+"%"},
                                {value:res.data.deviceScale[1].正常, name:'正常'+res.data.deviceScale[1].正常+"%"},
                                {value:res.data.deviceScale[2].故障, name:'故障'+res.data.deviceScale[2].故障+"%"},
                                {value:res.data.deviceScale[3].离线, name:'离线'+res.data.deviceScale[3].离线+"%"}
                            ]
                        }
                    ]
                };
                if (option && typeof option === "object") {
                    myChart.setOption(option, true);
                }
            }
        }
    })
}