
function  zhuang(pro_je,groupId,par_id,deviceCode) {
    $(".She,.She_a").show();
    $(".grouping").hide();
    var volOverDisCharge
    var volCharge

//电池电压最大值最小值
    $.ajax({
        url: baseURL + 'advancedsetting/queryVol?deviceCode='+deviceCode,
        contentType: "application/json;charset=UTF-8",
        type: "get",
        data: {},
        success: function (res) {
            var result=res.result;
            if(result == null){
                volOverDisCharge=250
                volCharge=40000.
            }else{
                volOverDisCharge =res.result.volOverDisCharge;
                volCharge = res.result.volCharge;
            }
        }
    })

    $.ajax({
        url: baseURL + 'fun/device/getDeviceById?deviceId='+par_id,
        contentType: "application/json;charset=UTF-8",
        type: "get",
        data: {},
        success: function (res) {
            $("#Yx_a").html(res.data.runDay+"天")
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
            $("#Yx_b").html(communicationType);
            $("#Yx_c").html("1.0");
            $("#Yx_d").html(res.data.version)
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

            var dians = res.data.batteryVoltage;
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

            var slo = res.data.photovoltaicCellVoltage;
            var slo1 = res.data.chargingCurrent;
            var slo2 = res.data.chargingPower;
            var slo3 = res.data.loadVoltage;
            var slo5 = res.data.loadPower;
            var slo4 = res.data.loadCurrent;

            if(res.data.batteryMargin==null){
                res.data.batteryMargin=""
            }
            if(dians==null){
                dians=""
            }
            if(slo==null){
                slo=""
            }
            if(slo1==null){
                slo1=""
            }
            if(slo2==null){
                slo2=""
            }
            if(slo3==null){
                slo3=""
            }
            if(slo5==null){
                slo5=""
            }
            if(slo4==null){
                slo4=""
            }
            $(".battery_a").html(batteryState);
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
            $("#sser").html(res.data.batteryMargin+"%"); //电池余量
            $("#sser").width(res.data.batteryMargin+"%");
            $("#battery_cr").html(dians+"V");//电池电压
            $("#battery_cr").width(parseInt(dians/((volCharge-volOverDisCharge)/100)+"%"));
            $(".battery_h").html(loadState);//负载状态
            $("#battery_ir").html(slo3+"V");//负载电压
            $("#battery_ir").width(parseInt(slo3/(60/100))+"%");
            $("#battery_lr").html(slo5+"W");
            $("#battery_lr").width(parseInt(slo5/(40/100))+"%");
            $("#battery_or").html(slo4+"A");
            $("#battery_or").width(parseInt(slo4/(2/100))+"%");
            $(".battery_d").html(photocellState);//光电池状态
            $("#battery_er").html(slo+"V"); //光电池电压
            $("#battery_er").width(parseInt(slo/(dians/100))+"%");
            $("#battery_fr").html(slo1+"A");//充电电流
            $("#battery_fr").width(parseInt(slo1/(10/100))+"%");
            $("#battery_gr").html(slo2+"W");//充电功率
            $("#battery_gr").width(parseInt(slo2/(220/100))+"%");
            $(".battery_a").html(batteryState);

            var luminance=res.data.light;
            var lian_g=res.data.lightingDuration;
            var chen_g=res.data.morningHours;
            var deviceCode=res.data.deviceCode;
            var on0f=res.data.onOff;
            $('#in_p>input[name="category"]' == on0f).each(function(){
                $(this).prop("checked",true);
            });
            $('#in_p>input[name="category"]' != on0f).each(function(){
                $(this).prop("checked",false);
            });

            $("#slideTest1").children().children(".layui-slider-bar").width((luminance/$(".progres").width()*$(".progres").width())+"%");
            // $("#slideTest1").children().children(".layui-slider-tips").html(luminance)
            layui.use('slider', function() {
                var $ = layui.$
                    , slider = layui.slider;
                slider.render({
                    elem: '#slideTest1'
                    , theme: '#1E9FFF' //主题色
                    , value: luminance
                });
            })
            $("#slideTest1 .layui-slider-wrap").css('left', luminance/$(".progres").width()*$(".progres").width() + '%');
            $("#slideTest2").val(lian_g);
            $("#slideTest3").val(chen_g);

            var youVal = res.data.onOff; // 1,2 把拿到的开关灯值赋给单选按钮

            $("input[name='category']").each(function(index) {
                if ($("input[name='category']").get(index).value == youVal) {
                    $("input[name='category']").get(index).checked = true;
                }
            });
            $("#confirm").unbind('click');
            $("#confirm").click(function(){
                var category = parseInt($('input:radio[name="category"]:checked').val());
                var r_wen_a = parseInt($("#slideTest1").children().children(".layui-slider-bar").width()/$("#slideTest1").children(".layui-slider").width()*100);
                var r_wen_b = $('#slideTest2').val();
                var r_wen_c = $('#slideTest3').val();
                var s_witch
                s_witch=$(".move").attr("data-state");
                if(s_witch == "on"){
                    s_witch=1
                }else if(s_witch == "off"){
                    s_witch=0
                }
                equipment_b(pro_je,groupId,par_id,s_witch,category,r_wen_a,r_wen_b,r_wen_c,deviceCode,1)
            })
        }
    })
//    单台设备结束
}
//控制分组
function  f_en(pro_je,groupId) {
    $(".She").hide();
    $(".grouping,.She_a").show();
    //分组饼图
    Bing(pro_je,groupId)
//点击开关
    $("#confirm").unbind('click');
    $("#confirm").click(function(){
        var category = parseInt($('input:radio[name="category"]:checked').val());
        var r_wen_a = parseInt($("#slideTest1").children().children(".layui-slider-bar").width()/$("#slideTest1").children(".layui-slider").width()*100);
        var r_wen_b =$('#slideTest2').val();
        var r_wen_c = $('#slideTest3').val();
        var s_witch
            s_witch=$(".move").attr("data-state");
        if(s_witch == "on"){
            s_witch=1
        }else if(s_witch == "off"){
            s_witch=0
        }
        equipment_b(pro_je,groupId,"",s_witch,category,r_wen_a,r_wen_b,r_wen_c,0,0)
    })
}
function equipment_b(pro_je,groupId,par_id,s_witch,category,r_wen_a,r_wen_b,r_wen_c,deviceCode,par) {
    var r_wen_b=Number(r_wen_b)
    var r_wen_c=Number(r_wen_c)

    var Code
    if(deviceCode == ""){
        Code=[]
    }else{
        Code= [deviceCode]
    }
    var data = {};
    var data1 = {};
    data["projectId"] = pro_je;
    data["groupId"] = groupId;
    data["deviceId"] = par_id;
    data["deviceCode"] = deviceCode;
    data["onOff"] = category;   //开关
    data["light"] = r_wen_a;  //亮度
    data["lightingDuration"] = r_wen_b;   //亮灯时长
    data["morningHours"] = r_wen_c;  //晨亮时长
    data["transport"] = s_witch;

    data1["deviceCodes"] = Code
    data1["deviceType"] = 1
    data1["status"] = 2
    data1["groupId"] = groupId;
    data1["qaKey"] = ["onOff","lightingDuration","morningHours","light","transport"]
    data1["value"] = [category,r_wen_b,r_wen_c,r_wen_a,s_witch]
    if(par == 1){
        data1["groupId"] = ""
    }else{
        data1["groupId"] = groupId
    }
    $.ajax({
        url: baseURL + 'fun/device/updateOnOffByIds',
        contentType: "application/json;charset=UTF-8",
        type:"POST",
        data:JSON.stringify(data),
        success: function (res) {
            if(res.code == "200"){
                layer.open({
                    title: '信息',
                    content:"操作成功",
                    skin: 'demo-class'
                });
            }
        }
    })

    data["deviceCode"] = Code;
    //硬件
    $.ajax({
        url:baseURL + 'fun/device/change',
        contentType: "application/json;charset=UTF-8",
        type:"POST",
        data: JSON.stringify(data1),
        success: function(res) {

        }
    })
}


function Bing(pro_je,groupId) {
    $.ajax({
        url: baseURL + 'fun/project/queryProjectById?projectId='+ pro_je+"&groupId="+groupId,
        contentType: "application/json;charset=UTF-8",
        type:"get",
        data:{},
        success: function (res) {
            $(".lu_m_a").html(res.data.sumCount);
            $(".lu_m_c").html(res.data.deviceCount);
            if(res.code == "200"){
                var dom=document.getElementById("Bing");
                var myChart = echarts.init(dom);
                option= null;
                option = {
                    color: ['#f9b712','#3385ff', '#00ccb3','#ff6e86','#999999' ],
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        x: 'left',
                        data:[
                            '警告'+res.data.deviceMap[0].警告+"台",
                            '升级中'+res.data.deviceMap[1].升级中+"台",
                            '正常'+res.data.deviceMap[2].正常+"台",
                            '故障'+res.data.deviceMap[3].故障+"台",
                            '离线'+res.data.deviceMap[4].离线+"台"
                        ]
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
                                        fontSize: '14',
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
                                {value:res.data.deviceScale[0].警告, name:'警告'+res.data.deviceMap[0].警告+"台"},
                                {value:res.data.deviceScale[1].升级中, name:'升级中'+res.data.deviceMap[1].升级中+"台"},
                                {value:res.data.deviceScale[2].正常, name:'正常'+res.data.deviceMap[2].正常+"台"},
                                {value:res.data.deviceScale[3].故障, name:'故障'+res.data.deviceMap[3].故障+"台"},
                                {value:res.data.deviceScale[4].离线, name:'离线'+res.data.deviceMap[4].离线+"台"}
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