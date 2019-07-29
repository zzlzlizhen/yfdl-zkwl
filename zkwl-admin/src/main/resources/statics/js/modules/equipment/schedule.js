
function  zhuang(pro_je,groupId,par_id) {
    $(".She,.She_a").show()
    $(".grouping").hide()
    $.ajax({
        url: baseURL + 'fun/device/getDeviceById?deviceId='+par_id,
        contentType: "application/json;charset=UTF-8",
        type: "get",
        data: {},
        success: function (res) {
            console.log("单台设备")
            console.log(res)
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
            $("#Yx_b").html(communicationType)
            $("#Yx_c").html("1.0")
            $("#Yx_d").html(res.data.version)
            var batteryState=res.data.batteryState;
            if (batteryState == null ) {
                batteryState = "";
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

            $(".battery_a").html(batteryState);

            $(".battery_b").html(res.data.batteryMargin+"%");
            $(".battery_c").html(res.data.batteryVoltage);//充电电压
            var photocellState=res.data.photocellState;
            if (photocellState == null ) {
                photocellState = "";
            } else if(photocellState == 1) {
                photocellState = "光强";
            }else if(photocellState == 2) {
                photocellState = "正在充电";
            }else if(photocellState == 0) {
                photocellState = "光弱";
            }
            $(".battery_d").html(photocellState);//光电池状态

            $(".battery_e").html(res.data.photovoltaicCellVoltage);
            $(".battery_f").html(res.data.chargingCurrent);//充电电流
            $(".battery_g").html(res.data.chargingPower);//充电功率
            var loadState=res.data.loadState;
            if (loadState == null ) {
                loadState = "";
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
            $(".battery_h").html(loadState);//负载状态
            $(".battery_i").html(res.data.loadVoltage);
            $(".battery_l").html(res.data.loadPower);
            $(".battery_o").html(res.data.loadCurrent);
            var luminance=res.data.light;
            var lian_g=res.data.lightingDuration;
            var chen_g=res.data.morningHours;
            var deviceCode=res.data.deviceCode;
            var on0f=res.data.onOff
            $('#in_p>input[name="category"]' == on0f).each(function(){
                $(this).prop("checked",true);
            });
            $('#in_p>input[name="category"]' != on0f).each(function(){
                $(this).prop("checked",false);
            });


            $("#slideTest1").children().children(".layui-slider-bar").width((luminance/$(".progres").width()*$(".progres").width())+"%");
            $("#slideTest1").children().children(".layui-slider-tips").html(luminance)
            $("#slideTest1 .layui-slider-wrap").css('left', luminance/$(".progres").width()*$(".progres").width() + '%');

            $("#slideTest2").val(lian_g)
            $("#slideTest3").val(chen_g)

            var youVal = res.data.onOff; // 1,2 把拿到的开关灯值赋给单选按钮

            $("input[name='category']").each(function(index) {
                if ($("input[name='category']").get(index).value == youVal) {
                    $("input[name='category']").get(index).checked = true;
                }
            });
            console.log("===="+groupId)
            $("#confirm").unbind('click');
            $("#confirm").click(function(){
                var category = parseInt($('input:radio[name="category"]:checked').val());

                var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
                var r_wena =  r_wen1/$(".progres").width()*100
                var r_wen_a = Math.ceil(r_wena);

                var r_wenb = $('#slideTest2').val()
                var r_wen_b = Math.ceil(r_wenb);
                var r_wenc =  $('#slideTest3').val()
                var r_wen_c = Math.ceil(r_wenc);

                equipment_b(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c,deviceCode,1)
            })

            //点击开关
            // $(".control-label").unbind('click');
            // $(".control-label").click(function(){
            //     var category = parseInt($('input:radio[name="category"]:checked').val());
            //     equipment_b(pro_je,"",par_id,category,"","","",deviceCode)
            // })
            // //滑动单台亮度
            // $("#slideTest1").unbind('blur');
            // $('#slideTest1').blur(function(){
            //     var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
            //     var r_wena =  r_wen1/$(".progres").width()*100
            //     var r_wen_a = Math.ceil(r_wena);
            //     equipment_b(pro_je,"",par_id,3,r_wen_a,"","",deviceCode)
            // })
            // // 亮灯时长
            // $("#slideTest2").unbind('blur');
            // $('#slideTest2').blur(function(){
            //     var r_wenb = $('#slideTest2').val()
            //     var r_wen_b = Math.ceil(r_wenb);
            //     equipment_b(pro_je,"",par_id,3,"",r_wen_b,"",deviceCode)
            // })
            // //晨亮时长
            // $("#slideTest3").unbind('blur');
            // $('#slideTest3').blur(function(){
            //     var r_wenc =  $('#slideTest3').val()
            //     var r_wen_c = Math.ceil(r_wenc);
            //     equipment_b(pro_je,"",par_id,3,"","",r_wen_c,deviceCode)
            // })
        }

    })
//    单台设备结束
}

//控制分组
function  f_en(pro_je,groupId) {
    $(".She").hide()
    $(".grouping,.She_a").show()
    //分组饼图
    Bing(pro_je,groupId)
//点击开关
    $("#confirm").unbind('click');
    $("#confirm").click(function(){
        var category = parseInt($('input:radio[name="category"]:checked').val());
        var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wena =  r_wen1/$(".progres").width()*100
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb = $('#slideTest2').val()
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  $('#slideTest3').val()
        var r_wen_c = Math.ceil(r_wenc);
        equipment_b(pro_je,groupId,"",category,r_wen_a,r_wen_b,r_wen_c,0,0)
    })

    // $(".control-label").unbind('click');
    // $(".control-label").click(function(){
    //     var category = parseInt($('input:radio[name="category"]:checked').val());
    //     equipment_b(pro_je,groupId,"",category,"","","",0)
    //
    // })
    // //    滑动单台亮度
    // $("#slideTest1").unbind('blur');
    // $('#slideTest1').blur(function(){
    //     var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
    //     var r_wena =  r_wen1/$(".progres").width()*100
    //     var r_wen_a = Math.ceil(r_wena);
    //     equipment_b(pro_je,groupId,"",3,r_wen_a,"","",0)
    // })
    // // 亮灯时长
    // $("#slideTest2").unbind('blur');
    // $('#slideTest2').blur(function(){
    //     var r_wenb =  $('#slideTest2').val()
    //     var r_wen_b = Math.ceil(r_wenb);
    //     equipment_b(pro_je,groupId,"",3,"",r_wen_b,"",0)
    // })
    // // 晨亮时长
    // $("#slideTest3").unbind('blur');
    // $('#slideTest3').blur(function(){
    //     var r_wenc =  $('#slideTest3').val()
    //     var r_wen_c = Math.ceil(r_wenc);
    //     equipment_b(pro_je,groupId,"",3,"","",r_wen_c,0)
    // })
}
function equipment_b(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c,deviceCode,par) {
    var Code
    if(deviceCode == ""){
        Code=[]
    }else{
        Code= [deviceCode]
    }
    var data = {};
    var data1 = {};
    data1["deviceCodes"] = Code
    data1["deviceType"] = 1
    data1["status"] = 2
    data1["groupId"] = groupId
    data["projectId"] = pro_je;
    data["groupId"] = groupId;
    data["deviceId"] = par_id;
    data["deviceCode"] = deviceCode;
    data["onOff"] = category;
    data["lightingDuration"] = r_wen_b;
    data["morningHours"] = r_wen_c;
    data["light"] = r_wen_a;
    data1["qaKey"] = ["onOff","lightingDuration","morningHours","light"]
    data1["value"] = [category,r_wen_b,r_wen_c,r_wen_a]
    if(par == 1){
        data1["groupId"] = ""
    }else{
        data1["groupId"] = groupId
    }
    // if(category != 3){
    //     data["onOff"] = category;
    // }else if(r_wen_b !=""){
    //     data["lightingDuration"] = r_wen_b;
    // }else if (r_wen_c !=""){
    //     data["morningHours"] = r_wen_c;
    // }else if (r_wen_a !=""){
    //     data["light"] = r_wen_a;
    // }


    $.ajax({
        url: baseURL + 'fun/device/updateOnOffByIds',
        contentType: "application/json;charset=UTF-8",
        type:"POST",
        data:JSON.stringify(data),
        success: function (res) {
            console.log(res)
            if(res.code == "200"){

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
            console.log(res)
            if(res.code == "200"){
               alert("修改成功");
            }
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